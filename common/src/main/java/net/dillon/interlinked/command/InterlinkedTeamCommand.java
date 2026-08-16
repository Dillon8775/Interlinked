package net.dillon.interlinked.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dillon.dillonlib.util.SimplePermissions;
import net.dillon.interlinked.helper.ModHelper;
import net.dillon.interlinked.option.DataNames;
import net.dillon.interlinked.option.ModCommonOptions;
import net.dillon.interlinked.option.TeamData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class InterlinkedTeamCommand {
    private static final String TEAM_NAME_ARGUMENT = "team name";
    private static final String TEAM_DATA_ARGUMENT = "team data";
    private static final String TEAM_VALUE_ARGUMENT = "value";
    private static final String PLAYER_LEADER_ARGUMENT = "Player Leader";
    private static final String TELE_LINK_ARGUMENT = "Tele-Link (true/false)";
    private static final String TELE_LINK_DISTANCE_ARGUMENT = "Tele-Link Distance (default = 300)";
    private static final String HEALTH_LINK_ARGUMENT = "Health Link (true/false)";
    private static final String HEART_PER_PLAYER = "Heart Per Player (true/false)";
    private static final String INV_LINK_ARGUMENT = "Inventory Link (true/false)";
    private static final int DEFAULT_TELEPORT_LINK_DISTANCE = 300;
    private static final IntegerArgumentType TELE_LINK_ARGUMENT_TYPE = IntegerArgumentType.integer(10, 10000);

    /**
     * @return the {@code /team} command.
     */
    public static LiteralArgumentBuilder<CommandSourceStack> getInterlinkedTeamCommand() {
        return Commands.literal("interlinkedteam")
                .requires(context -> {
                    try {
                        return SimplePermissions.admin(context) || ModHelper.isInterlinkedOperator(context.getPlayerOrException());
                    } catch (CommandSyntaxException c) {
                        context.sendFailure(Component.literal(c.getMessage()));
                        return false;
                    }
                })
                .then(
                        Commands.argument(TEAM_NAME_ARGUMENT, StringArgumentType.string())
                                .then(
                                        Commands.literal("list")
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
                                        Commands.literal("addplayer")
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
                                        Commands.literal("removeplayer")
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
                                        Commands.literal("get")
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
                                                        Commands.literal(DataNames.INVENTORY_LINK.getId())
                                                                .then(
                                                                        Commands.argument(TEAM_VALUE_ARGUMENT, BoolArgumentType.bool())
                                                                                .executes(context ->
                                                                                        modifyTeamData(
                                                                                                context,
                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                DataNames.INVENTORY_LINK.getId(),
                                                                                                BoolArgumentType.getBool(context, TEAM_VALUE_ARGUMENT)
                                                                                        )
                                                                                )
                                                                )
                                                )
                                )
                                .then(
                                        Commands.literal("create")
                                                .executes(
                                                        context ->
                                                                createTeam(
                                                                        context,
                                                                        StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                        context.getSource().getPlayerOrException().getScoreboardName(),
                                                                        List.of(
                                                                                context.getSource().getPlayerOrException().getScoreboardName()
                                                                        ),
                                                                        false,
                                                                        DEFAULT_TELEPORT_LINK_DISTANCE,
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
                                                                                        EntityArgument.getPlayer(context, PLAYER_LEADER_ARGUMENT).getScoreboardName()
                                                                                ),
                                                                                false,
                                                                                DEFAULT_TELEPORT_LINK_DISTANCE,
                                                                                false,
                                                                                false,
                                                                                false
                                                                        )
                                                                )
                                                                .then(
                                                                        Commands.argument(TELE_LINK_ARGUMENT, BoolArgumentType.bool())
                                                                                .executes(context ->
                                                                                        createTeam(
                                                                                                context,
                                                                                                StringArgumentType.getString(context, TEAM_NAME_ARGUMENT),
                                                                                                EntityArgument.getPlayer(context, PLAYER_LEADER_ARGUMENT).getScoreboardName(),
                                                                                                List.of(
                                                                                                        EntityArgument.getPlayer(context, PLAYER_LEADER_ARGUMENT).getScoreboardName()
                                                                                                ),
                                                                                                BoolArgumentType.getBool(context, TELE_LINK_ARGUMENT),
                                                                                                DEFAULT_TELEPORT_LINK_DISTANCE,
                                                                                                false,
                                                                                                false,
                                                                                                false
                                                                                        )
                                                                                )
                                                                                .then(
                                                                                        Commands.argument(TELE_LINK_DISTANCE_ARGUMENT, TELE_LINK_ARGUMENT_TYPE)
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
                                                                                                                false,
                                                                                                                false,
                                                                                                                false
                                                                                                        )
                                                                                                )
                                                                                                .then(
                                                                                                        Commands.argument(HEALTH_LINK_ARGUMENT, BoolArgumentType.bool())
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
                                                                                                                                false,
                                                                                                                                false
                                                                                                                        )
                                                                                                                )
                                                                                                                .then(
                                                                                                                        Commands.argument(HEART_PER_PLAYER, BoolArgumentType.bool())
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
                                                                                                                                                false
                                                                                                                                        )
                                                                                                                                )
                                                                                                                                .then(
                                                                                                                                        Commands.argument(INV_LINK_ARGUMENT, BoolArgumentType.bool())
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
                                                                                                                                                                BoolArgumentType.getBool(context, INV_LINK_ARGUMENT)
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
     * Creates a interlinked team.
     */
    private static int createTeam(
            CommandContext<CommandSourceStack> context,
            String name,
            String playerLeader,
            List<String> players,
            boolean teleLink,
            int teleportDistance,
            boolean healthLink,
            boolean heartPerPlayer,
            boolean invLink) {
        ModCommonOptions.INSTANCE.update(options -> {
            if (ModHelper.getTeamByName(name) != null) {
                context.getSource().sendSuccess(() -> Component.literal("A team with that name already exists."), true);
            } else {
                options.teams.add(
                        new TeamData(
                                name,
                                playerLeader,
                                players,
                                teleLink,
                                teleportDistance,
                                healthLink,
                                heartPerPlayer,
                                invLink
                        )
                );
                context.getSource().sendSuccess(() -> Component.literal("Team \"" + name + "\" created."), true);
            }
        });

        return 1;
    }

    /**
     * Deletes an interlinked team.
     */
    private static int deleteTeam(CommandContext<CommandSourceStack> context, String name) {
        ModCommonOptions.INSTANCE.update(options -> {
            TeamData team = ModHelper.getTeamByName(name);
            if (team == null) {
                context.getSource().sendFailure(Component.literal("No team with that name was found."));
            } else {
                options.teams.remove(team);
                context.getSource().sendSuccess(() -> Component.literal("Team \"" + name + "\" was deleted."), true);
            }
        });

        return -1;
    }

    /**
     * Modifies team data.
     */
    private static int modifyTeamData(CommandContext<CommandSourceStack> context, String name, String argument, Object value) {
        ModCommonOptions.INSTANCE.update(options -> {
            TeamData data = ModHelper.getTeamByName(name);

            if (data == null) {
                context.getSource().sendFailure(
                        Component.literal("No team with that name was found.")
                );
                return;
            }

            String argumentMessage = "unknown";
            if (argument.equals(DataNames.LEADER.getId())) {
                data.setLeader((String) value);
                argumentMessage = DataNames.LEADER.getName();

            } else if (argument.equals(DataNames.TELE_LINK.getId())) {
                data.setTeleLink((Boolean) value);
                argumentMessage = DataNames.TELE_LINK.getName();

            } else if (argument.equals(DataNames.TELE_LINK_DISTANCE.getId())) {
                data.setTeleLinkDistance((Integer) value);
                argumentMessage = DataNames.TELE_LINK_DISTANCE.getName();

            } else if (argument.equals(DataNames.HEALTH_LINK.getId())) {
                data.setHealthLink((Boolean) value);
                argumentMessage = DataNames.HEALTH_LINK.getName();

            } else if (argument.equals(DataNames.HEART_PER_PLAYER.getId())) {
                data.setHeartPerPlayer((Boolean) value);
                argumentMessage = DataNames.HEART_PER_PLAYER.getName();

            } else if (argument.equals(DataNames.INVENTORY_LINK.getId())) {
                data.setInvLink((Boolean) value);
                argumentMessage = DataNames.INVENTORY_LINK.getName();
            }

            String finalArgumentMessage = argumentMessage;

            context.getSource().sendSuccess(
                    () -> Component.literal("Updated " + finalArgumentMessage + " for team \"" + name + "\"."),
                    true
            );
        });

        return 1;
    }

    /**
     * Modifies a player in a team.
     */
    private static int modifyPlayerData(CommandContext<CommandSourceStack> context, String name, ServerPlayer player, boolean add) {
        ModCommonOptions.INSTANCE.update(options -> {
            TeamData data = ModHelper.getTeamByName(name);

            if (data == null) {
                context.getSource().sendFailure(
                        Component.literal("No team with that name was found.")
                );
                return;
            }

            String playerName = player.getScoreboardName();

            if (add) {
                if (data.players.contains(playerName)) {
                    context.getSource().sendSuccess(
                            () -> Component.literal(playerName + " is already on this team."),
                            true
                    );
                    return;
                }

                data.players.add(playerName);

                context.getSource().sendSuccess(
                        () -> Component.literal("Added " + playerName + " to team \"" + name + "\""),
                        true
                );
            } else {
                if (!data.players.contains(playerName)) {
                    context.getSource().sendFailure(
                            Component.literal(playerName + " is not on this team.")
                    );
                    return;
                }

                data.players.remove(playerName);

                context.getSource().sendSuccess(
                        () -> Component.literal("Removed " + playerName + " from team \"" + name + "\""),
                        true
                );
            }
        });

        return 1;
    }

    /**
     * Lists all players in a team.
     */
    private static int listAllPlayers(CommandContext<CommandSourceStack> context, String name) {
        TeamData data = ModHelper.getTeamByName(name);

        if (data == null) {
            context.getSource().sendFailure(
                    Component.literal("No team with that name was found.")
            );
            return 0;
        }

        Component message = Component.literal("Players in team \"" + name + "\":");

        int p = 0;
        for (String player : data.players) {
            message = message.copy().append(
                    Component.literal("\n- " + player)
            );
            p++;
        }

        Component finalMessage = message;
        context.getSource().sendSuccess(() -> finalMessage, false);
        return p;
    }

    /**
     * Gets all team data.
     */
    private static int getTeamData(CommandContext<CommandSourceStack> context, String name) {
        TeamData data = ModHelper.getTeamByName(name);

        if (data == null) {
            context.getSource().sendFailure(
                    Component.literal("No team with that name was found.")
            );
            return 0;
        }

        context.getSource().sendSuccess(
                () -> Component.literal("Team Name: " + data.name),
                false
        );

        context.getSource().sendSuccess(
                () -> Component.literal("Leader: " + data.leader),
                false
        );

        context.getSource().sendSuccess(
                () -> Component.literal("Tele-Link: " + data.teleLink),
                false
        );

        context.getSource().sendSuccess(
                () -> Component.literal("Tele-Link Distance: " + data.teleLinkDistance),
                false
        );

        context.getSource().sendSuccess(
                () -> Component.literal("Health Link: " + data.healthLink),
                false
        );

        context.getSource().sendSuccess(
                () -> Component.literal("Heart Per Player: " + data.heartPerPlayer),
                false
        );

        context.getSource().sendSuccess(
                () -> Component.literal("Inventory Link: " + data.invLink),
                false
        );

        return 1;
    }
}