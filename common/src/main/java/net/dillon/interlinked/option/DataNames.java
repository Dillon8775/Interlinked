package net.dillon.interlinked.option;

/**
 * Data names for teams.
 */
public enum DataNames {
    LEADER("leader", "Team Leader"),
    TELE_LINK("tele_link", "Tele-Link"),
    TELE_LINK_DISTANCE("tele_link_distance", "Tele-Link Distance"),
    HEALTH_LINK("health_link", "Health Link"),
    HEART_PER_PLAYER("heart_per_player", "Heart Per Player"),
    INVENTORY_LINK("inventory_link", "Inventory Link"),
    FRIENDLY_FIRE("friendly_fire", "Friendly Fire");

    private final String id;
    private final String name;

    DataNames(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}