package net.dillon.interlinked.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class FabricClientEvents {

    public static void registerConnectionChecks() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientEvents.onPlayerJoin(handler.getConnection(), client.player);
        });
    }
}