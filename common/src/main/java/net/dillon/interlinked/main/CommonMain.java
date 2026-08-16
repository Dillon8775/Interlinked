package net.dillon.interlinked.main;

import net.blay09.mods.balm.core.BalmRegistrars;
import net.dillon.interlinked.option.ModCommonOptions;

/**
 * The common entrypoint for Interlinked.
 */
public class CommonMain {

    public static void initialize(BalmRegistrars registrars) {
        ModCommonOptions.INSTANCE.load();
    }
}