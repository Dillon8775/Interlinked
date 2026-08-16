package net.dillon.interlinked.helper;

import net.dillon.interlinked.option.TeamData;
import net.minecraft.server.level.ServerPlayer;

import static net.dillon.interlinked.option.OptionInstances.common;

/**
 * Helper class for Interlinked.
 */
public class ModHelper {

    /**
     * @return if a player is an interlinked operator.
     */
    public static boolean isInterlinkedOperator(ServerPlayer player) {
        return common().operators.contains(player.getScoreboardName());
    }

    /**
     * @return if a team exists.
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
     * @return if a player is a leader in a specified team.
     */
    public static boolean isInterlinkedLeader(String teamName, ServerPlayer player) {
        TeamData team = getTeamByName(teamName);
        if (team == null) {
            return false;
        }

        return player.getScoreboardName().equals(team.leader);
    }
}