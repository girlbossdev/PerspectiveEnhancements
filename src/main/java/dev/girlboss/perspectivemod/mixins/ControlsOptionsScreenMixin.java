package dev.girlboss.perspectivemod.mixins;

import dev.girlboss.perspectivemod.gui.PerspectiveOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin extends ScreenMixin {
    @ModifyVariable(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;",
                    ordinal = 5,
                    shift = At.Shift.AFTER
            ),
            ordinal = 2
    )
    @SuppressWarnings("ConstantConditions")
    private int injectPerspectiveOptionsButton(int y) {
        int x = width / 2 - 155;
        this.addDrawableChild(
                ButtonWidget.builder(
                    Text.translatable("perspectivemod.options.button"),
                    button -> client.setScreen(new PerspectiveOptionsScreen((Screen) ((Object) this)))
                ).dimensions(x, y += 24, 150, 20).build()
        );
        return y;
    }
}
