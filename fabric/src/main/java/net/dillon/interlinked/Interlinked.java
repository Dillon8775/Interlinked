package net.dillon.interlinked;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.dillon.interlinked.helper.ModConstants;
import net.dillon.interlinked.main.CommonMain;
import net.fabricmc.api.ModInitializer;

public class Interlinked implements ModInitializer {

    @Override
    public void onInitialize() {
        Balm.initializeMod(ModConstants.MOD_ID, FabricLoadContext.INSTANCE, CommonMain::initialize);
    }
}