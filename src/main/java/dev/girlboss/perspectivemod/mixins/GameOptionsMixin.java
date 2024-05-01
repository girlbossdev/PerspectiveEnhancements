package dev.girlboss.perspectivemod.mixins;

import dev.girlboss.perspectivemod.PerspectiveEnhancements;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Inject(method = "write", at = @At("RETURN"))
    private void saveModOptions(CallbackInfo callbackInfo) {
        PerspectiveEnhancements.getInstance().getOptions().save();
    }
}
