package net.dillon.interlinked.platform.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.dillon.dillonlib.platform.client.ClientModPlatform;
import net.dillon.dillonlib.platform.info.PlatformMenuButton;
import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.main.ClientMain;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;

import static net.dillon.dillonlib.task.ClientTasks.getScreen;
import static net.dillon.interlinked.option.OptionInstances.client;

public class ClientInterlinkedPlatform extends ClientModPlatform {

    @Override
    public List<PlatformMenuButton> menuButtons() {
        return List.of(
                new PlatformMenuButton(
                        client().menuButton.enabled(),
                        client().menuButton.everywhere(),
                        ModConstants.HAS_UPDATE,
                        ClientMain.menuButton(getScreen()),
                        spriteIconButton -> {})
        );
    }

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