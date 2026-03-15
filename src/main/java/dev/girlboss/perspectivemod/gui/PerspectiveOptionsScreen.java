package dev.girlboss.perspectivemod.gui;

import dev.girlboss.perspectivemod.PerspectiveEnhancements;
import dev.girlboss.perspectivemod.options.ModOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;

public class PerspectiveOptionsScreen extends OptionsSubScreen {

    private final ModOptions modOptions = PerspectiveEnhancements.getInstance().getOptions();
    private AbstractSliderButton holdTimeOptionSlider;
    private CycleButton<CameraType> defaultPerspectiveOptionButton;

    public PerspectiveOptionsScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("perspectivemod.options.title"));
    }

    @Override
    protected void addOptions() {
        if (list == null) {
            throw new IllegalStateException("Called PerspectiveOptionsScreen#addOptions() with a null list");
        }

        var holdOptionButton = CycleButton
                .onOffBuilder(modOptions.holdEnabled())
                .create(Component.translatable("perspectivemod.options.hold"), (button, value) -> {
                    holdTimeOptionSlider.active = value;
                    modOptions.setHold(value);
                });

        holdTimeOptionSlider = new AbstractSliderButton(
                0, 0, 150, 20,
                Component.translatable("perspectivemod.options.holdTime", modOptions.getHoldTime() + " ms"),
                modOptions.getHoldTime() / 1000.0
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.translatable("perspectivemod.options.holdTime", modOptions.getHoldTime() + " ms"));
            }

            @Override
            protected void applyValue() {
                modOptions.setHoldTime((int) Math.round(Mth.clampedLerp(0, 1000, value) / 50) * 50);
            }
        };

        holdTimeOptionSlider.active = modOptions.holdEnabled();

        var backOptionButton = CycleButton
                .onOffBuilder(modOptions.backEnabled())
                .create(Component.translatable("perspectivemod.perspective.back"), (_, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.frontEnabled();
                    modOptions.setBack(value);
                });

        var frontOptionButton = CycleButton
                .onOffBuilder(modOptions.frontEnabled())
                .create(Component.translatable("perspectivemod.perspective.front"), (_, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.backEnabled();
                    modOptions.setFront(value);
                });

        defaultPerspectiveOptionButton = CycleButton
                .builder(this::perspectiveText, modOptions.getDefaultPerspective())
                .withValues(CameraType.THIRD_PERSON_BACK, CameraType.THIRD_PERSON_FRONT)
                .create(Component.translatable("perspectivemod.options.defaultPerspective"),
                        (_, value) -> modOptions.setDefaultPerspective(value)
                );

        defaultPerspectiveOptionButton.active = modOptions.backEnabled() && modOptions.frontEnabled();

        list.addSmall(
                List.of(
                        holdOptionButton, holdTimeOptionSlider,
                        backOptionButton, frontOptionButton,
                        defaultPerspectiveOptionButton
                )
        );
    }

    private Component perspectiveText(CameraType perspective) {
        return switch (perspective) {
            case FIRST_PERSON -> CommonComponents.EMPTY;
            case THIRD_PERSON_BACK -> Component.translatable("perspectivemod.perspective.back");
            case THIRD_PERSON_FRONT -> Component.translatable("perspectivemod.perspective.front");
        };
    }

    @Override
    public void onClose() {
        super.onClose();

        PerspectiveEnhancements.getInstance().getOptions().save();
    }
}
