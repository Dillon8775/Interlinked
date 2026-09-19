package net.dillon.interlinked.manager;

import com.mojang.brigadier.context.CommandContext;
import net.dillon.interlinked.option.DataNames;
import net.dillon.interlinked.option.ModCommonOptions;
import net.dillon.interlinked.option.TeamData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Set;

import static net.dillon.interlinked.option.OptionInstances.common;
import static net.dillon.interlinked.option.OptionInstances.updateCommon;

/**
 * Manages {@link TeamData}.
 */
public class TeamManager {

    /**
     * @return if a player is an {@code Interlinked Operator}.
     */
    public static boolean isOperator(ServerPlayer player) {
        return common().operators.contains(player.getScoreboardName());
    }

    /**
     * @return a team leader in a {@code Interlinked Team}.
     */
    public static ServerPlayer getTeamLeader(TeamData data, ServerLevel serverLevel) {
        if (data == null || data.leader == null || data.leader.isEmpty()) {
            throw new RuntimeException("Team data or team leader cannot be null or empty!");
        }

        return serverLevel.getServer()
                .getPlayerList()
                .getPlayer(data.leader);
    }

    /**
     * @return if a team {@code exists}.
     */
    public static TeamData getTeamByName(String teamName) {
        for (TeamData team : common().teams) {
            if (team.name.equals(teamName)) {
                return team;
            }
        }

        return null;
    }

    /**
     * Creates an {@code Interlinked Team}.
     * @see TeamData
     */
    public static int createTeam(
            CommandContext<CommandSourceStack> context,
            String name,
            String playerLeader,
            List<String> players,
            boolean teleLink,
            int teleportDistance,
            boolean healthLink,
            boolean heartPerPlayer,
            boolean invLink,
            boolean friendlyFire) {
        updateCommon(common -> {
            if (getTeamByName(name) != null) {
                context.getSource().sendSuccess(() -> Component.literal("A team with that name already exists."), true);
            } else {
                common.teams.add(
                        new TeamData(
                                name,
                                playerLeader,
                                players,
                                teleLink,
                                teleportDistance,
                                healthLink,
                                heartPerPlayer,
                                invLink,
                                friendlyFire
                        )
                );
                context.getSource().sendSuccess(() -> Component.literal("Team \"" + name + "\" created."), true);
            }
        });

        return 1;
    }

    /**
     * Deletes an {@code Interlinked Team}.
     * @see TeamData
     */
    public static int deleteTeam(CommandContext<CommandSourceStack> context, String name) {
        updateCommon(common -> {
            TeamData team = getTeamByName(name);
            if (team == null) {
                context.getSource().sendFailure(Component.literal("No team with that name was found."));
            } else {
                common.teams.remove(team);
                context.getSource().sendSuccess(() -> Component.literal("Team \"" + name + "\" was deleted."), true);
            }
        });

        return -1;
    }

    /**
     * Modifies a {@code Interlinked Team's data}.
     * @see TeamData
     */
    public static int modifyTeamData(CommandContext<CommandSourceStack> context, String name, String argument, Object value) {
        TeamData data = getTeamByName(name);

        if (data == null) {
            context.getSource().sendFailure(
                    Component.literal("No team with that name was found.")
            );
            return 0;
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
        } else if (argument.equals(DataNames.FRIENDLY_FIRE.getId())) {
            data.setFriendlyFire((Boolean) value);
            argumentMessage = DataNames.INVENTORY_LINK.getName();
        }

        ModCommonOptions.INSTANCE.save();

        String finalArgumentMessage = argumentMessage;
        context.getSource().sendSuccess(
                () -> Component.literal("Updated " + finalArgumentMessage + " for team \"" + name + "\" to \"" + value + "\"."),
                true
        );

        return 1;
    }

    /**
     * Modifies {@code player data} in an Interlinked team, by removing or adding them.
     * @see TeamData
     */
    public static int modifyPlayerData(CommandContext<CommandSourceStack> context, String name, ServerPlayer player, boolean add) {
        TeamData data = getTeamByName(name);

        if (data == null) {
            context.getSource().sendFailure(
                    Component.literal("No team with that name was found.")
            );
            return 0;
        }

        String playerName = player.getScoreboardName();

        if (add) {
            if (data.players.contains(playerName)) {
                context.getSource().sendSuccess(
                        () -> Component.literal(playerName + " is already on this team."),
                        true
                );
                return 0;
            }

            data.players.add(playerName);

            context.getSource().sendSuccess(
                    () -> Component.literal("Added " + playerName + " to team \"" + name + "\"."),
                    true
            );
        } else {
            if (!data.players.contains(playerName)) {
                context.getSource().sendFailure(
                        Component.literal(playerName + " is not on this team.")
                );
                return 0;
            }

            if (!data.leader.equals(playerName)) {
                data.players.remove(playerName);

                context.getSource().sendSuccess(
                        () -> Component.literal("Removed " + playerName + " from team \"" + name + "\"."),
                        true
                );
            } else {
                context.getSource().sendFailure(
                        Component.literal("You cannot remove " + playerName + " from team \"" + name + "\" because they are the leader.")
                );
                return 0;
            }
        }

        ModCommonOptions.INSTANCE.save();
        return 1;
    }

    /**
     * Lists all players in an {@code Interlinked Team}.
     * @see TeamData
     */
    public static int listAllPlayers(CommandContext<CommandSourceStack> context, String name) {
        TeamData data = getTeamByName(name);

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
     * Lists all current {@code Interlinked Teams}.
     * @see TeamData
     */
    public static int listAllTeams(CommandContext<CommandSourceStack> context) {
        Set<TeamData> allTeams = common().teams;

        if (allTeams.isEmpty()) {
            context.getSource().sendFailure(Component.literal("No Interlinked Teams were found."));
            return 0;
        }

        Component message = Component.literal("All current teams:");

        int t = 0;
        for (TeamData data : allTeams) {
            message = message.copy().append(
                    Component.literal("\n- " + data.name + " (" + data.players.size() + " players)")
            );
            t++;
        }

        Component finalMessage = message;
        context.getSource().sendSuccess(() -> finalMessage, false);
        return t;
    }

    /**
     * Gets all settings from a specified {@code Interlinked Team}.
     * @see TeamData
     */
    public static int getTeamData(CommandContext<CommandSourceStack> context, String name) {
        TeamData data = getTeamByName(name);

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

        context.getSource().sendSuccess(
                () -> Component.literal("Friendly Fire: " + data.friendlyFire),
                false
        );

        return 1;
    }
}