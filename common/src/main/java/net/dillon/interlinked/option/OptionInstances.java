package net.dillon.interlinked.option;

import java.util.function.Consumer;

/**
 * Getters for Interlinked's option instances.
 */
public class OptionInstances {

    /**
     * @return the common options.
     */
    public static ModCommonOptions common() {
        return ModCommonOptions.INSTANCE.getInstance();
    }

    /**
     * Updates the common option instance.
     */
    public static void updateCommon(Consumer<ModCommonOptions> common) {
        ModCommonOptions.INSTANCE.update(common);
    }
}