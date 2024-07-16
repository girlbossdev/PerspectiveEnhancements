package dev.girlboss.perspectivemod.mixins;

import dev.girlboss.perspectivemod.gui.PerspectiveOptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsOptionsScreen.class)
public abstract class ControlsOptionsScreenMixin extends GameOptionsScreenMixin {

    @Inject(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/OptionListWidget;addWidgetEntry(Lnet/minecraft/client/gui/widget/ClickableWidget;Lnet/minecraft/client/gui/widget/ClickableWidget;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    @SuppressWarnings("ConstantConditions")
    private void injectPerspectiveOptionsButton(CallbackInfo callbackInfo) {
        this.body.addWidgetEntry(
                ButtonWidget.builder(
                        Text.translatable("perspectivemod.options.button"),
                        button -> this.client.setScreen(new PerspectiveOptionsScreen((Screen) ((Object) this)))
                ).build(), null
        );
    }
}
