package net.dillon.interlinked.screen;

import net.dillon.dillonlib.annotation.Dill;
import net.dillon.dillonlib.annotation.DillType;
import net.dillon.dillonlib.task.ClientTasks;
import net.dillon.interlinked.config.ConfigurationScreen;
import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.platform.InterlinkedPlatforms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The main menu screen for {@code Interlinked.}
 */
@Dill(DillType.CLIENT)
public class MainMenuScreen extends OptionsSubScreen {
    private AbstractWidget manageTeams;

    public MainMenuScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("interlinked.title"));
    }

    @Override
    protected void init() {
        super.init();
        this.manageTeams = Button.builder(Component.translatable("interlinked.gui.manage_teams"), button -> {}).build();

        List<AbstractWidget> options = new ArrayList<>(List.of(
                this.manageTeams,

                Button.builder(Component.translatable("interlinked.gui.configure"), button -> ClientTasks.tryOpenYaclScreen(
                        () -> ConfigurationScreen.configScreen().generateScreen(this),
                        Component.translatable("interlinked.title")
                )).build(),

                Button.builder(Component.translatable("interlinked.gui.ask_questions"), ConfirmLinkScreen.confirmLink(this, "https://discord.gg/vfqEAn4YFy", false)).build(),

                Button.builder(Component.translatable("interlinked.gui.report_bugs"), ConfirmLinkScreen.confirmLink(this, "https://github.com/Dillon8775/Interlinked/issues", false)).build()
        ));

        this.list.addSmall(options);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, deltaTicks);

        this.manageTeams.active = this.minecraft.level != null;

        ClientTasks.drawModInfo(
                graphics,
                this,
                ModConstants.VERSION,
                InterlinkedPlatforms.getPlatform().logoWidth().getWidthModifier(),
                ModConstants.LOGO,
                ModConstants.HAS_UPDATE
        );
    }

    @Override
    protected void addOptions() {
    }
}