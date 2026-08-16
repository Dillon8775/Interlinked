package net.dillon.interlinked.platform;

import net.dillon.dillonlib.platform.ModPlatform;
import net.dillon.dillonlib.platform.PlatformLoader;
import net.dillon.interlinked.helper.ModConstants;

public class InterlinkedPlatforms {
    private static final ModPlatform PLATFORM = PlatformLoader.load(ModPlatform.class, ModConstants.MOD_ID);

    public static ModPlatform getPlatform() {
        return PLATFORM;
    }
}