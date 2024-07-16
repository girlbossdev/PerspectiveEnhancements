package dev.girlboss.perspectivemod.mixins;

import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.OptionListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameOptionsScreen.class)
public abstract class GameOptionsScreenMixin extends ScreenMixin {

    @Shadow
    protected OptionListWidget body;
}
