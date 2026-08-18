package net.dillon.interlinked.mixin.main;

import com.mojang.authlib.GameProfile;
import net.dillon.interlinked.option.TeamData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.dillon.interlinked.option.OptionInstances.updateCommon;

/**
 * Implements basic logic for interlinked players.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    @Shadow
    public abstract void teleportTo(double x, double y, double z);

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    /**
     * Ticks interlinked player logic.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void interlinkedPlayerTick(CallbackInfo ci) {
        Level level = this.level();

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        String playerName = this.getScoreboardName();

        updateCommon(common -> {
            for (TeamData data : common.teams) {
                int teleLinkDistance = data.teleLinkDistance * data.teleLinkDistance;
                List<String> players = data.players;
                ServerPlayer leaderPlayer = serverLevel.getServer()
                        .getPlayerList()
                        .getPlayer(data.leader);

                // If player is on team, continue
                if (players.contains(playerName)) {

                    // Heart Per Player Logic
                    if (data.heartPerPlayer) {
                        double extra = players.size() * 2;
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(extra);
                    }

                    // Only continue if leader player exists
                    if (leaderPlayer != null) {

                        // Health link logic
                        if (data.healthLink) {
                            this.setHealth(leaderPlayer.getHealth());
                        }

                        // Tele-Link logic (ensure player is not the leader)
                        if (!playerName.equals(leaderPlayer.getScoreboardName()) && data.teleLink && this.distanceToSqr(leaderPlayer) >= teleLinkDistance) {
                            this.teleportTo(leaderPlayer.getX(), leaderPlayer.getY(), leaderPlayer.getZ());
                        }
                    }
                }
            }
        });
    }
}