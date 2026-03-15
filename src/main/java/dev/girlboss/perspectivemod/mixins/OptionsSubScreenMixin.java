package dev.girlboss.perspectivemod.mixins;

import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OptionsSubScreen.class)
public abstract class OptionsSubScreenMixin {

    @Shadow
    protected @Nullable OptionsList list;
}
