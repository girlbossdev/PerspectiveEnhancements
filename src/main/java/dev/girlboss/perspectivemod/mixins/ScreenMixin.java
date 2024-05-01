package dev.girlboss.perspectivemod.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow public int width;
    @Shadow @Nullable protected MinecraftClient client;

    @Shadow
    protected abstract <T extends Element & Drawable> T addDrawableChild(T drawableElement);
}
