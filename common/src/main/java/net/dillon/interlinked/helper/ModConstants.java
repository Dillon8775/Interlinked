package net.dillon.interlinked.helper;

import net.dillon.dillonlib.util.UpdateChecker;
import net.dillon.interlinked.platform.InterlinkedPlatforms;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constant values for Interlinked.
 */
public class ModConstants {
    public static final String MOD_ID = "interlinked";
    public static final Component VERSION = Component.literal(InterlinkedPlatforms.getPlatform().modVersion());
    public static final Logger LOGGER = LoggerFactory.getLogger("Simple Keybinds");
    public static final boolean HAS_UPDATE = UpdateChecker.hasUpdate(UpdateChecker.checkForUpdate(
            "simple-keybinds",
            InterlinkedPlatforms.getPlatform().modVersion()
    ));
    public static final Identifier LOGO = Identifier.fromNamespaceAndPath("interlinked", "interlinked");
}