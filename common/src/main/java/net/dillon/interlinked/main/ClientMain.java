package net.dillon.interlinked.main;

import net.blay09.mods.balm.core.BalmRegistrars;
import net.dillon.dillonlib.annotation.Dill;
import net.dillon.dillonlib.annotation.DillType;
import net.dillon.dillonlib.platform.info.UpdatableSpriteButton;
import net.dillon.dillonlib.task.ClientTasks;
import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.screen.MainMenuScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Map;

import static net.dillon.dillonlib.task.ClientTasks.openScreen;

/**
 * The client entrypoint for Interlinked.
 */
@Dill(DillType.CLIENT)
public class ClientMain {

    public static void cInitialize(BalmRegistrars registrars) {
    }

    public static UpdatableSpriteButton menuButton(Screen parent) {
        return ClientTasks.createMenuButton(
                "Interlinked Main Menu",
                ModConstants.LOGO,
                (button) -> openScreen(new MainMenuScreen(parent)),
                Map.of(
                        ModConstants.HAS_UPDATE,
                        Component.translatable("interlinked.gui.update_available")
                ),
                Component.translatable("interlinked.title.menu"),
                true
        );
    }
}