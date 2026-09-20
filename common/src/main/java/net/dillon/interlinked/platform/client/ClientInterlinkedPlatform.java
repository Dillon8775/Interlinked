package net.dillon.interlinked.platform.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.dillon.dillonlib.platform.client.ClientModPlatform;
import net.dillon.interlinked.helper.ModConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;

public class ClientInterlinkedPlatform extends ClientModPlatform {

    @Override
    public String modId() {
        return ModConstants.MOD_ID;
    }

    // Unused
    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, KeyMapping.Category category, int value) {
        return null;
    }

    // Unused
    @Override
    public boolean canSendPacket(LocalPlayer localPlayer) {
        return false;
    }
}