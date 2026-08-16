package net.dillon.interlinked.option;

import java.util.List;

/**
 * Stores team data for interlinked.
 * @param name the name of the team
 * @param playerLeader the leader of the team, who can control team settings
 * @param players all players in the team
 * @param teleLink determines if players should be teleported to team leader if they go so many blocks away
 * @param teleportDistance the maximum distances that players can go away from the team leader before they are teleported to them (default = 300 blocks)
 * @param healthLink links player health with team leader
 * @param heartPerPlayer gives all players an extra heart based on team size
 * @param invLink links all player inventories with team leader
 */
public class TeamData {
    public String name;
    public String leader;
    public List<String> players;
    public boolean teleLink;
    public int teleLinkDistance;
    public boolean healthLink;
    public boolean heartPerPlayer;
    public boolean invLink;

    public TeamData(
            String name,
            String playerLeader,
            List<String> players,
            boolean teleLink,
            int teleportDistance,
            boolean healthLink,
            boolean heartPerPlayer,
            boolean invLink
    ) {
       this.name = name;
       this.leader = playerLeader;
       this.players = players;
       this.teleLink = teleLink;
       this.teleLinkDistance = teleportDistance;
       this.healthLink = healthLink;
       this.heartPerPlayer = heartPerPlayer;
       this.invLink = invLink;
    }

    public void setLeader(String value) {
        this.leader = value;
    }

    public void setTeleLink(boolean value) {
        this.teleLink = value;
    }

    public void setTeleLinkDistance(int value) {
        this.teleLinkDistance = value;
    }

    public void setHealthLink(boolean value) {
        this.healthLink = value;
    }

    public void setHeartPerPlayer(boolean value) {
        this.heartPerPlayer = value;
    }

    public void setInvLink(boolean value) {
        this.invLink = value;
    }
}