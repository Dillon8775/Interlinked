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
     * @return the client options.
     */
    public static ModClientOptions client() {
        return ModClientOptions.INSTANCE.getInstance();
    }

    /**
     * Updates the common option instance.
     */
    public static void updateCommon(Consumer<ModCommonOptions> common) {
        ModCommonOptions.INSTANCE.update(common);
    }

    /**
     * Updates the client option instance.
     */
    public static void updateClient(Consumer<ModClientOptions> common) {
        ModClientOptions.INSTANCE.update(common);
    }
}