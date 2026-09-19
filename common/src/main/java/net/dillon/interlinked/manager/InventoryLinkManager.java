package net.dillon.interlinked.manager;

import net.dillon.interlinked.option.TeamData;
import net.minecraft.world.entity.player.Inventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryLinkManager {
    private static final Map<TeamData, LinkedInventory> INVENTORIES = new HashMap<>();
    private static final Map<TeamData, Map<String, Integer>> CHANGE_COUNTS = new HashMap<>();

    public static LinkedInventory get(TeamData team, Inventory source) {
        return INVENTORIES.computeIfAbsent(
                team,
                ignored -> new LinkedInventory(source)
        );
    }

    public static Map<String, Integer> getChangeCounts(TeamData team) {
        return CHANGE_COUNTS.computeIfAbsent(
                team,
                ignored -> new HashMap<>()
        );
    }

    public static void remove(TeamData team) {
        INVENTORIES.remove(team);
        CHANGE_COUNTS.remove(team);
    }

    public static void clear() {
        INVENTORIES.clear();
        CHANGE_COUNTS.clear();
    }
}