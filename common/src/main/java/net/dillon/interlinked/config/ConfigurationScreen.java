package net.dillon.interlinked.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.dillon.dillonlib.annotation.Dill;
import net.dillon.dillonlib.annotation.DillType;
import net.minecraft.network.chat.Component;

/**
 * The main configuration screen for Interlinked.
 */
@Dill(DillType.CLIENT)
public class ConfigurationScreen {

    public static YetAnotherConfigLib configScreen() {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("interlinked.title"))
                .category(
                        ConfigCategory.createBuilder()
                                .name(Component.translatable("interlinked.options.client"))
                                .tooltip(Component.translatable("interlinked.options.client.description"))
                                .group(
                                        OptionGroup.createBuilder()
                                                .name(Component.translatable("interlinked.options.client"))
                                                .description(OptionDescription.of(Component.translatable("interlinked.options.client.description")))
                                                .build())
                                .build())
                .build();
    }
}