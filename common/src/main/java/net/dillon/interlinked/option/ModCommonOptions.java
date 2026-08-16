package net.dillon.interlinked.option;

import net.dillon.dillonlib.util.BaseOptions;

import java.util.HashSet;
import java.util.Set;

public class ModCommonOptions {
    public static ModCommonOptionsHandler INSTANCE = new ModCommonOptionsHandler();
    public Set<String> operators = new HashSet<>();
    public Set<TeamData> teams = new HashSet<>();

    public static class ModCommonOptionsHandler extends BaseOptions<ModCommonOptions> {

        public ModCommonOptionsHandler() {
            super("interlinked.json");
        }

        @Override
        protected ModCommonOptions createDefault() {
            return new ModCommonOptions();
        }

        @Override
        protected Class<ModCommonOptions> getConfigClass() {
            return ModCommonOptions.class;
        }
    }
}