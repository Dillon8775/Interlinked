package net.dillon.interlinked.screen;

import net.dillon.dillonlib.annotation.Dill;
import net.dillon.dillonlib.annotation.DillType;
import net.dillon.dillonlib.task.ClientTasks;
import net.dillon.interlinked.config.ConfigurationScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.net.URI;

@Dill(DillType.CLIENT)
public class MainMenuScreen extends AbstractModScreen {

    public MainMenuScreen(Screen parent) {
        super(parent);
    }

    @Override
    protected void init() {
        super.init();

        AbstractWidget manageTeams = Button.builder(Component.translatable("interlinked.menu.manage_teams"), button -> {}).build();

        this.list.addHeader(Component.translatable("interlinked.menu.settings"));
        this.list.addSmall(
                manageTeams,

                Button.builder(Component.translatable("interlinked.menu.configure"), button -> ClientTasks.tryOpenYaclScreen(
                        () -> ConfigurationScreen.configScreen().generateScreen(this),
                        Component.translatable("interlinked.title")
                )).build()
        );

        this.list.addHeader(Component.translatable("interlinked.menu.ask_questions"));
        this.list.addSmall(
                Button.builder(Component.translatable("interlinked.menu.ask_questions"),
                        ConfirmLinkScreen.confirmLink(this, URI.create("https://discord.gg/vfqEAn4YFy"), false)).build(),

                Button.builder(Component.translatable("interlinked.menu.report_bugs"),
                        ConfirmLinkScreen.confirmLink(this, URI.create("https://github.com/Dillon8775/Interlinked/issues"), false)).build()
        );
    }
}