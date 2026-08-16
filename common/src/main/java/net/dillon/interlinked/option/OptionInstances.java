package net.dillon.interlinked.option;

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
}