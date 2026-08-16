package net.dillon.interlinked.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Texts for {@code Interlinked}.
 */
public class ModTexts {

    /**
     * @return the player's name in {@link Component} form.
     */
    public static Component getPlayerName(ServerPlayer player) {
        return player.getDisplayName();
    }
}