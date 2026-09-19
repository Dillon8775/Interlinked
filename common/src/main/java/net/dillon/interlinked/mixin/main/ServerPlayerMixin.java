package net.dillon.interlinked.mixin.main;

import com.mojang.authlib.GameProfile;
import net.dillon.interlinked.manager.InventoryLinkManager;
import net.dillon.interlinked.manager.LinkedInventory;
import net.dillon.interlinked.option.TeamData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

import static net.dillon.interlinked.helper.ModConstants.MOD_ID;
import static net.dillon.interlinked.manager.TeamManager.getTeamLeader;
import static net.dillon.interlinked.option.OptionInstances.common;

/**
 * Implements basic logic for interlinked players.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    @Shadow
    public abstract void teleportTo(double x, double y, double z);
    @Unique
    private static final Identifier HEART_PER_PLAYER = Identifier.fromNamespaceAndPath(
            MOD_ID,
            "heart_per_player"
    );

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    /**
     * Cancels out damage if it comes from another player in a {@code Interlinked Team} and {@code friendly fire} is enabled.
     */
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void cancelFriendlyFire(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        // Get this player name
        ServerPlayer self = (ServerPlayer)(Object)this;
        String selfName = self.getScoreboardName();

        for (TeamData data : common().teams) {
            // Continue to next statement if friendly fire isn't enabled
            if (!data.friendlyFire) {
                return;
            }

            List<String> players = data.players;

            // Continue to next statement if player isn't in this team
            if (!players.contains(selfName)) {
                continue;
            }

            // Check if attacker is player
            Entity attacker = source.getEntity();
            if (attacker instanceof ServerPlayer attackerPlayer) {
                String attackerName = attackerPlayer.getScoreboardName();

                // Cancel out damage from attacker if they are both on the same team and friendly fire is enabled
                if (players.contains(attackerName)) {
                    attackerPlayer.sendOverlayMessage(Component.literal("You cannot attack this player!").withStyle(ChatFormatting.RED));
                    cir.cancel();
                }
            }
        }
    }

    /**
     * Ticks interlinked player logic.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void interlinkedPlayerTick(CallbackInfo ci) {
        Level level = this.level();

        // Only continue in instance of server level
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Get player name
        String playerName = this.getScoreboardName();

        for (TeamData data : common().teams) {
            List<String> players = data.players;

            // HeartPerPlayer logic
            AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
            if (data.heartPerPlayer) {
                maxHealth.addOrUpdateTransientModifier(new AttributeModifier(
                        HEART_PER_PLAYER,
                        players.size() * 2.0,
                        AttributeModifier.Operation.ADD_VALUE
                ));
            } else {
                maxHealth.removeModifier(HEART_PER_PLAYER);
            }

            // Cancel out if player isn't in team because it won't work otherwise
            if (!players.contains(playerName)) {
                continue;
            }

            // Team Leader logic
            ServerPlayer leaderPlayer = getTeamLeader(data, serverLevel);
            if (leaderPlayer == null) {
                return;
            }

            // Health link logic
            if (data.healthLink) {
                this.setHealth(leaderPlayer.getHealth());
            }

            // Tele-Link logic (ensure player is not the leader)
            int teleLinkDistance = data.teleLinkDistance * data.teleLinkDistance;
            if (!playerName.equals(leaderPlayer.getScoreboardName()) && data.teleLink && this.distanceToSqr(leaderPlayer) >= teleLinkDistance) {
                this.teleportTo(leaderPlayer.getX(), leaderPlayer.getY(), leaderPlayer.getZ());
            }

            // Inventory link logic
            if (data.invLink) {
                ServerPlayer player = (ServerPlayer)(Object)this;
                Inventory inventory = player.getInventory();

                LinkedInventory linkedInventory = InventoryLinkManager.get(data, inventory);

                Map<String, Integer> changeCounts = InventoryLinkManager.getChangeCounts(data);

                int currentChanges = inventory.getTimesChanged();

                Integer previousChanges = changeCounts.get(playerName);

                // First time seeing this player
                if (previousChanges == null) {
                    changeCounts.put(playerName, currentChanges);

                    // Make sure the player starts with the shared inventory
                    linkedInventory.copyTo(inventory);
                    inventory.setChanged();
                    player.inventoryMenu.broadcastChanges();

                    return;
                }

                // Nothing changed for this player
                if (currentChanges == previousChanges) {
                    return;
                }

                // This player changed their inventory
                // Their inventory becomes the new shared state
                linkedInventory.copyFrom(inventory);

                // Record the new change count for the player who made the change
                changeCounts.put(playerName, inventory.getTimesChanged());

                // Update every other teammate
                for (String teammateName : data.players) {
                    if (teammateName.equals(playerName)) {
                        continue;
                    }

                    ServerPlayer teammate = serverLevel.getServer()
                            .getPlayerList()
                            .getPlayerByName(teammateName);

                    if (teammate == null) {
                        continue;
                    }

                    Inventory teammateInventory = teammate.getInventory();

                    linkedInventory.copyTo(teammateInventory);

                    /*
                     * The copy itself doesn't necessarily increment
                     * timesChanged, so explicitly mark it changed.
                     */
                    teammateInventory.setChanged();

                    /*
                     * Remember the change count we just caused.
                     */
                    changeCounts.put(
                            teammateName,
                            teammateInventory.getTimesChanged()
                    );

                    /*
                     * Tell the client about the new inventory state.
                     */
                    teammate.inventoryMenu.broadcastChanges();
                }
            }
        }
    }
}