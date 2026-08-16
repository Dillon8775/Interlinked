package net.dillon.interlinked;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.dillon.interlinked.event.FabricClientEvents;
import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.main.ClientMain;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientInterlinked implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricClientEvents.registerConnectionChecks();

        Balm.initializeMod(ModConstants.MOD_ID, FabricLoadContext.INSTANCE, ClientMain::cInitialize);
    }
}