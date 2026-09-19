package net.dillon.interlinked.platform;

import com.mojang.brigadier.CommandDispatcher;
import net.blay09.mods.balm.Balm;
import net.dillon.dillonlib.platform.ModPlatform;
import net.dillon.dillonlib.platform.Platforms;
import net.dillon.dillonlib.platform.info.Platform;
import net.dillon.dillonlib.platform.info.Release;
import net.dillon.interlinked.command.InterlinkedCommand;
import net.dillon.interlinked.helper.ModConstants;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class InterlinkedPlatform extends ModPlatform {

    @Override
    public void registerCommonCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(InterlinkedCommand.getInterlinkedCommand());
    }

    @Override
    public String modId() {
        return ModConstants.MOD_ID;
    }

    @Override
    public String modVersion() {
        return Platforms.getCommonPlatform().commonModVersion(ModConstants.MOD_ID);
    }

    @Override
    public Release release() {
        return Release.BETA;
    }

    @Override
    public Platform platform() {
        return Balm.platform().name().equals("fabric") ? Platform.FABRIC : Platform.NEOFORGE;
    }
}