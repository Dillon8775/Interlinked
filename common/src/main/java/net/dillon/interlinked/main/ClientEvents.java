package net.dillon.interlinked.main;

import net.dillon.dillonlib.core.DillonLibModReferences;
import net.dillon.dillonlib.task.CommonTasks;
import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.platform.InterlinkedPlatforms;
import net.dillon.interlinked.platform.ModReferences;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.net.URI;

import static net.dillon.dillonlib.task.CommonTasks.sendSystemMessage;

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

        if (InterlinkedPlatforms.getPlatform().platform().fabric() && !DillonLibModReferences.isModLoaded(ModReferences.SHARED_INVENTORY)) {
            sendSystemMessage(
                    player,
                    Component.translatable("interlinked.link_inventories")
                            .setStyle(
                                    Style.EMPTY
                                            .withClickEvent(
                                                    new ClickEvent.OpenUrl(
                                                            URI.create("https://modrinth.com/mod/sharedinv")
                                                    )
                                            )
                            )
            );
        }
    }
}