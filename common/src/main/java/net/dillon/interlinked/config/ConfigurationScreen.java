package net.dillon.interlinked.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import net.dillon.dillonlib.annotation.Dill;
import net.dillon.dillonlib.annotation.DillType;
import net.dillon.interlinked.option.MenuButton;
import net.minecraft.network.chat.Component;

import static net.dillon.interlinked.option.OptionInstances.client;

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
                                .name(Component.translatable("interlinked.title"))
                                .tooltip(Component.translatable("interlinked.title.tooltip"))
                                .group(
                                        OptionGroup.createBuilder()
                                                .name(Component.translatable("interlinked.options.client"))
                                                .description(OptionDescription.of(Component.translatable("interlinked.options.client.description")))
                                                .option(
                                                        Option.<MenuButton>createBuilder()
                                                                .name(Component.translatable("interlinked.options.menu_button"))
                                                                .description(OptionDescription.of(Component.translatable("interlinked.options.menu_button.description")))
                                                                .binding(MenuButton.EVERYWHERE, () -> client().menuButton, v -> client().menuButton = v)
                                                                .controller(o -> EnumControllerBuilder.create(o)
                                                                        .enumClass(MenuButton.class)
                                                                        .formatValue(v -> Component.translatable(v.getSerializedName())))
                                                                .build()
                                                )
                                                .build())
                                .build())
                .build();
    }
}