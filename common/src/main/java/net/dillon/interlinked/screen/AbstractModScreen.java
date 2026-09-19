package net.dillon.interlinked.screen;

import net.dillon.dillonlib.annotation.Dill;
import net.dillon.dillonlib.annotation.DillType;
import net.dillon.dillonlib.task.ClientTasks;
import net.dillon.interlinked.helper.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

/**
 * An abstract representation of a screen for {@code Interlinked}.
 */
@Dill(DillType.CLIENT)
public abstract class AbstractModScreen extends OptionsSubScreen {

    public AbstractModScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("interlinked.title"));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, deltaTicks);

        ClientTasks.drawModInfo(
                graphics,
                this,
                ModConstants.VERSION,
                ModConstants.LOGO,
                ModConstants.HAS_UPDATE
        );
    }

    @Override
    protected void addOptions() {
    }
}