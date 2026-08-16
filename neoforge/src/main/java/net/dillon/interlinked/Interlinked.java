package net.dillon.interlinked;

import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.main.ClientMain;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ModConstants.MOD_ID, dist = Dist.CLIENT)
public final class Interlinked {

    public Interlinked(IEventBus modEventBus, ModContainer container) {
        final var context = new NeoForgeLoadContext(container, modEventBus);
        Balm.initializeMod(ModConstants.MOD_ID, context, ClientMain::cInitialize);

        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> new MainMenuScreen(parent)
        );
    }
}