package dev.girlboss.perspectivemod.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(
            method = "handleInputEvents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z",
                    ordinal = 0
            )
    )
    private boolean disableVanillaPerspectiveHandling(KeyBinding instance) {
        return false;
    }
}
