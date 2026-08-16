package net.dillon.interlinked.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dillon.dillonlib.util.SimplePermissions;
import net.dillon.interlinked.helper.ModHelper;
import net.dillon.interlinked.option.ModCommonOptions;
import net.dillon.interlinked.util.ModTexts;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class InterlinkedOperatorCommand {

    /**
     *
     * @return the {@code /itopertator} command.
     */
    public static LiteralArgumentBuilder<CommandSourceStack> getInterlinkedOperator() {
        return Commands.literal("interlinkedoperator")
                .requires(SimplePermissions::admin)
                .then(
                        Commands.literal("add")
                                .then(
                                        Commands.argument("target", EntityArgument.player())
                                                .executes(
                                                        context -> modifyPlayerAsOperator(context, true)
                                                )
                                )
                )
                .then(
                        Commands.literal("remove")
                                .then(
                                        Commands.argument("target", EntityArgument.player())
                                                .executes(
                                                        context -> modifyPlayerAsOperator(context, false)
                                                )
                                )
                );
    }

    /**
     * Adds/removes a player as a {@code Interlinked operator.}
     */
    private static int modifyPlayerAsOperator(CommandContext<CommandSourceStack> context, boolean add) {
        try {
            ServerPlayer player = EntityArgument.getPlayer(context, "target");
            String uuid = player.getScoreboardName();

            ModCommonOptions.INSTANCE.update(options -> {
                if (add) {
                    if (ModHelper.isInterlinkedOperator(player)) {
                        context.getSource().sendSuccess(() -> alreadyExists(player), true);
                    } else {
                        options.operators.add(uuid);
                        context.getSource().sendSuccess(() -> successAdd(player), true);
                    }
                } else {
                    if (options.operators.remove(uuid)) {
                        context.getSource().sendSuccess(() -> successRemove(player), true);
                    } else {
                        context.getSource().sendFailure(doesntExist(player));
                    }
                }
            });

            return 1;
        } catch (CommandSyntaxException c) {
            context.getSource().sendFailure(Component.literal(c.getMessage()));
            return 0;
        }
    }

    private static Component alreadyExists(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" is already an interlinked operator.");
    }

    private static Component doesntExist(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" it not an interlinked operator.");
    }

    private static Component successAdd(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" has been added as a interlinked operator.");
    }

    private static Component successRemove(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" has been removed as a interlinked operator.");
    }
}