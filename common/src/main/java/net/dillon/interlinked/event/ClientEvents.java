package net.dillon.interlinked.event;

import net.dillon.dillonlib.task.CommonTasks;
import net.dillon.interlinked.helper.ModConstants;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

/**
 * Client events for Interlinked.
 */
public class ClientEvents {

    public static void onPlayerJoin(Connection connection, LocalPlayer player) {
        if (ModConstants.HAS_UPDATE) {
            CommonTasks.sendUpdateMessage(player,
                    Component.translatable("interlinked.title"),
                    "https://modrinth.com/mod/interlinked/versions",
                    TextColor.WHITE.getValue());
        }
    }
}