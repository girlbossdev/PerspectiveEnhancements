package dev.girlboss.perspectivemod.mixins;

import dev.girlboss.perspectivemod.gui.PerspectiveOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public abstract class ControlsOptionsScreenMixin extends OptionsSubScreenMixin {

    @SuppressWarnings("ConstantConditions")
    @Inject(
            method = "addOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall(Lnet/minecraft/client/gui/components/AbstractWidget;Lnet/minecraft/client/gui/components/AbstractWidget;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void injectPerspectiveOptionsButton(CallbackInfo callbackInfo) {
        list.addSmall(
                Button.builder(
                        Component.translatable("perspectivemod.options.button"),
                        _ -> Minecraft.getInstance()
                                .setScreen(new PerspectiveOptionsScreen((Screen) ((Object) this)))
                ).build(), null
        );
    }
}
