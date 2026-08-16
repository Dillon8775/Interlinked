package net.dillon.interlinked.option;

import net.dillon.dillonlib.util.BaseOptions;

public class ModClientOptions {
    public static ModClientOptionsHandler INSTANCE = new ModClientOptionsHandler();
    public MenuButton menuButton = MenuButton.EVERYWHERE;

    public static class ModClientOptionsHandler extends BaseOptions<ModClientOptions> {

        public ModClientOptionsHandler() {
            super("interlinked_client.json");
        }

        @Override
        protected ModClientOptions createDefault() {
            return new ModClientOptions();
        }

        @Override
        protected Class<ModClientOptions> getConfigClass() {
            return ModClientOptions.class;
        }
    }
}