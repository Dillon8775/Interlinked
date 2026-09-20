package net.dillon.interlinked.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dillon.dillonlib.util.SimplePermissions;
import net.dillon.interlinked.manager.TeamManager;
import net.dillon.interlinked.option.DataNames;
import net.dillon.interlinked.option.ModCommonOptions;
import net.dillon.interlinked.util.ModTexts;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

import static net.dillon.interlinked.manager.TeamManager.*;

public class InterlinkedCommand {
    private static final String TEAM_NAME_ARGUMENT = "team name";
    private static final String TEAM_VALUE_ARGUMENT = "value";
    private static final String PLAYER_LEADER_ARGUMENT = "Player Leader";
    private static final String TELE_LINK_ARGUMENT = "Tele-Link (true/false)";
    private static final String TELE_LINK_DISTANCE_ARGUMENT = "Tele-Link Distance (default = 300)";
    private static final String HEALTH_LINK_ARGUMENT = "Health Link (true/false)";
    private static final String HEART_PER_PLAYER = "Heart Per Player (true/false)";
    private static final String INV_LINK_ARGUMENT = "Inventory Link (true/false)";
    private static final String FRIENDLY_FIRE_ARGUMENT = "Friendly Fire (true/false)";
    private static final IntegerArgumentType TELE_LINK_ARGUMENT_TYPE = IntegerArgumentType.integer(10, 10000);

    /**
     * @return The {@code /interlinked} command.
     */
    public static LiteralArgumentBuilder<CommandSourceStack> getInterlinkedCommand() {
        return Commands.literal("interlinked")
                .requires(SimplePermissions::all)
                .then(
                        Commands.literal("operator")
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
                                )
                )
                .then(
                        Commands.literal("listteams")
                                .requires(InterlinkedCommand::canExecuteOperatorCommand)
                                .executes(TeamManager::listAllTeams)
                )
                .then(
                        Commands.literal("team")
                                .requires(InterlinkedCommand::canExecuteOperatorCommand)
                                .then(
                                        Commands.argument(TEAM_NAME_ARGUMENT, StringArgumentType.string())
                                                .then(
                                                        Commands.literal("list_players")
                                                                .executes(context ->
                                                                        listAllPlayers(
                                                                                context,
                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT)
                                                                        )
                                                                )
                                                )
                                                .then(
                                                        Commands.literal("delete")
                                                                .executes(context ->
                                                                        deleteTeam(
                                                                                context,
                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT)
                                                                        )
                                                                )
                                                )
                                                .then(
                                                        Commands.literal("add_player")
                                                                .then(
                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, EntityArgument.player())
                                                                                .executes(context ->
                                                                                        modifyPlayerData(
                                                                                                context,
                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                EntityArgument.getPlayer(context, TEAM_VALUE_ARGUMENT),
                                                                                                true
                                                                                        )
                                                                                )
                                                                )
                                                )
                                                .then(
                                                        Commands.literal("remove_player")
                                                                .then(
                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, EntityArgument.player())
                                                                                .executes(context ->
                                                                                        modifyPlayerData(
                                                                                                context,
                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                EntityArgument.getPlayer(context, TEAM_VALUE_ARGUMENT),
                                                                                                false
                                                                                        )
                                                                                )
                                                                )
                                                )
                                                .then(
                                                        Commands.literal("get_data")
                                                                .executes(context ->
                                                                        getTeamData(
                                                                                context,
                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT)
                                                                        )
                                                                )
                                                )
                                                .then(
                                                        Commands.literal("set")
                                                                .then(
                                                                        Commands.literal(DataNames.LEADER.getId())
                                                                                .then(
                                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, EntityArgument.player())
                                                                                                .executes(context ->
                                                                                                        modifyTeamData(
                                                                                                                context,
                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                DataNames.LEADER.getId(),
                                                                                                                EntityArgument.getPlayer(context, TEAM_VALUE_ARGUMENT).getScoreboardName()
                                                                                                        )
                                                                                                )
                                                                                )
                                                                )
                                                                .then(
                                                                        Commands.literal(DataNames.TELE_LINK.getId())
                                                                                .then(
                                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, BoolArgumentType.bool())
                                                                                                .executes(context ->
                                                                                                        modifyTeamData(
                                                                                                                context,
                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                DataNames.TELE_LINK.getId(),
                                                                                                                BoolArgumentType.getBool(context, TEAM_VALUE_ARGUMENT)
                                                                                                        )
                                                                                                )
                                                                                )
                                                                )
                                                                .then(
                                                                        Commands.literal(DataNames.TELE_LINK_DISTANCE.getId())
                                                                                .then(
                                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, TELE_LINK_ARGUMENT_TYPE)
                                                                                                .executes(context ->
                                                                                                        modifyTeamData(
                                                                                                                context,
                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                DataNames.TELE_LINK_DISTANCE.getId(),
                                                                                                                IntegerArgumentType.getInteger(context, TEAM_VALUE_ARGUMENT)
                                                                                                        )
                                                                                                )
                                                                                )
                                                                )
                                                                .then(
                                                                        Commands.literal(DataNames.HEALTH_LINK.getId())
                                                                                .then(
                                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, BoolArgumentType.bool())
                                                                                                .executes(context ->
                                                                                                        modifyTeamData(
                                                                                                                context,
                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                DataNames.HEALTH_LINK.getId(),
                                                                                                                BoolArgumentType.getBool(context, TEAM_VALUE_ARGUMENT)
                                                                                                        )
                                                                                                )
                                                                                )
                                                                )
                                                                .then(
                                                                        Commands.literal(DataNames.HEART_PER_PLAYER.getId())
                                                                                .then(
                                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, BoolArgumentType.bool())
                                                                                                .executes(context ->
                                                                                                        modifyTeamData(
                                                                                                                context,
                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                DataNames.HEART_PER_PLAYER.getId(),
                                                                                                                BoolArgumentType.getBool(context, TEAM_VALUE_ARGUMENT)
                                                                                                        )
                                                                                                )
                                                                                )
                                                                )
                                                                .then(
                                                                        Commands.literal(DataNames.FRIENDLY_FIRE.getId())
                                                                                .then(
                                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, BoolArgumentType.bool())
                                                                                                .executes(context ->
                                                                                                        modifyTeamData(
                                                                                                                context,
                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                DataNames.FRIENDLY_FIRE.getId(),
                                                                                                                BoolArgumentType.getBool(context, TEAM_VALUE_ARGUMENT)
                                                                                                        )
                                                                                                )
                                                                                )
                                                                )
                                                )
                                                .then(
                                                        Commands.literal("create")
                                                                .executes(context ->
                                                                        createTeam(
                                                                                context,
                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                context.getSource().getPlayerOrException().getScoreboardName(),
                                                                                List.of(
                                                                                        context.getSource().getPlayerOrException().getScoreboardName()
                                                                                ),
                                                                                false,
                                                                                300,
                                                                                false,
                                                                                false,
                                                                                false
                                                                        )
                                                                )
                                                                .then(
                                                                        Commands.argument(PLAYER_LEADER_ARGUMENT, EntityArgument.player())
                                                                                .executes(context ->
                                                                                        createTeam(
                                                                                                context,
                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                EntityArgument.getPlayer(context, PLAYER_LEADER_ARGUMENT).getScoreboardName(),
                                                                                                List.of(
                                                                                                        context.getSource().getPlayerOrException().getScoreboardName()
                                                                                                ),
                                                                                                false,
                                                                                                300,
                                                                                                false,
                                                                                                false,
                                                                                                false
                                                                                        )
                                                                                )
                                                                                .then(
                                                                                        Commands.argument(TELE_LINK_ARGUMENT, BoolArgumentType.bool())
                                                                                                .then(
                                                                                                        Commands.argument(TELE_LINK_DISTANCE_ARGUMENT, TELE_LINK_ARGUMENT_TYPE)
                                                                                                                .then(
                                                                                                                        Commands.argument(HEALTH_LINK_ARGUMENT, BoolArgumentType.bool())
                                                                                                                                .then(
                                                                                                                                        Commands.argument(HEART_PER_PLAYER, BoolArgumentType.bool())
                                                                                                                                                .then(
                                                                                                                                                        Commands.argument(FRIENDLY_FIRE_ARGUMENT, BoolArgumentType.bool())
                                                                                                                                                                .executes(context ->
                                                                                                                                                                        createTeam(
                                                                                                                                                                                context,
                                                                                                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                                                                                                EntityArgument.getPlayer(context, PLAYER_LEADER_ARGUMENT).getScoreboardName(),
                                                                                                                                                                                List.of(
                                                                                                                                                                                        EntityArgument.getPlayer(context, PLAYER_LEADER_ARGUMENT).getScoreboardName()
                                                                                                                                                                                ),
                                                                                                                                                                                BoolArgumentType.getBool(context, TELE_LINK_ARGUMENT),
                                                                                                                                                                                IntegerArgumentType.getInteger(context, TELE_LINK_DISTANCE_ARGUMENT),
                                                                                                                                                                                BoolArgumentType.getBool(context, HEALTH_LINK_ARGUMENT),
                                                                                                                                                                                BoolArgumentType.getBool(context, HEART_PER_PLAYER),
                                                                                                                                                                                BoolArgumentType.getBool(context, FRIENDLY_FIRE_ARGUMENT)
                                                                                                                                                                        )
                                                                                                                                                                )
                                                                                                                                                )
                                                                                                                                )
                                                                                                                )
                                                                                                )
                                                                                )
                                                                )
                                                )
                                )
                );
    }

    /**
     * @return if a player can execute an operator command.
     */
    private static boolean canExecuteOperatorCommand(CommandSourceStack context) {
        try {
            return SimplePermissions.admin(context) || isOperator(context.getPlayerOrException());
        } catch (CommandSyntaxException c) {
            context.sendFailure(Component.literal(c.getMessage()));
            return false;
        }
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
                    if (isOperator(player)) {
                        context.getSource().sendSuccess(() -> alreadyExists(player), true);
                    } else {
                        options.operators.add(uuid);
                        context.getSource().sendSuccess(() -> addOpSuccess(player), true);
                    }
                } else {
                    if (options.operators.remove(uuid)) {
                        context.getSource().sendSuccess(() -> removeOpSuccess(player), true);
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

    /**
     * Warns that a player is {@code already an Interlinked operator}.
     */
    private static Component alreadyExists(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" is already an interlinked operator.");
    }

    /**
     * Warns that a player is {@code not an Interlinked operator}.
     */
    private static Component doesntExist(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" it not an interlinked operator.");
    }

    /**
     * Successfully {@code adds} a player as an Interlinked operator.
     */
    private static Component addOpSuccess(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" has been added as a interlinked operator.");
    }

    /**
     * Successfully {@code removes} a player as an Interlinked operator.
     */
    private static Component removeOpSuccess(ServerPlayer player) {
        return ModTexts.getPlayerName(player).copy()
                .append(" has been removed as a interlinked operator.");
    }
}