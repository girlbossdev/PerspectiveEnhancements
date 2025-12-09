package dev.girlboss.perspectivemod.gui;

import dev.girlboss.perspectivemod.PerspectiveEnhancements;
import dev.girlboss.perspectivemod.options.ModOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.Perspective;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class PerspectiveOptionsScreen extends GameOptionsScreen {

    private final ModOptions modOptions = PerspectiveEnhancements.getInstance().getOptions();
    private OptionListWidget body;
    private SliderWidget holdTimeOptionSlider;
    private CyclingButtonWidget<Perspective> defaultPerspectiveOptionButton;

    public PerspectiveOptionsScreen(Screen parent) {
        super(parent, null, Text.translatable("perspectivemod.options.title"));
    }

    @Override
    protected void addOptions() {}

    @Override
    protected void initBody() {
        body = addDrawableChild(new OptionListWidget(client, width, this));

        var holdOptionButton = CyclingButtonWidget
                .onOffBuilder(modOptions.holdEnabled())
                .build(Text.translatable("perspectivemod.options.hold"), (button, value) -> {
                    holdTimeOptionSlider.active = value;
                    modOptions.setHold(value);
                });

        holdTimeOptionSlider = new SliderWidget(
                0, 0, 150, 20,
                Text.translatable("perspectivemod.options.holdTime", modOptions.getHoldTime() + " ms"),
                modOptions.getHoldTime() / 1000.0
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.translatable("perspectivemod.options.holdTime", modOptions.getHoldTime() + " ms"));
            }

            @Override
            protected void applyValue() {
                modOptions.setHoldTime((int) Math.round(MathHelper.clampedLerp(0, 1000, value) / 50) * 50);
            }
        };

        holdTimeOptionSlider.active = modOptions.holdEnabled();

        var backOptionButton = CyclingButtonWidget
                .onOffBuilder(modOptions.backEnabled())
                .build(Text.translatable("perspectivemod.perspective.back"), (button, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.frontEnabled();
                    modOptions.setBack(value);
                });

        var frontOptionButton = CyclingButtonWidget
                .onOffBuilder(modOptions.frontEnabled())
                .build(Text.translatable("perspectivemod.perspective.front"), (button, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.backEnabled();
                    modOptions.setFront(value);
                });

        defaultPerspectiveOptionButton = CyclingButtonWidget
                .builder(this::perspectiveText, modOptions.getDefaultPerspective())
                .values(Perspective.THIRD_PERSON_BACK, Perspective.THIRD_PERSON_FRONT)
                .build(Text.translatable("perspectivemod.options.defaultPerspective"),
                        (button, value) -> modOptions.setDefaultPerspective(value)
                );

        defaultPerspectiveOptionButton.active = modOptions.backEnabled() && modOptions.frontEnabled();

        body.addWidgetEntry(holdOptionButton, holdTimeOptionSlider);
        body.addWidgetEntry(backOptionButton, frontOptionButton);
        body.addWidgetEntry(defaultPerspectiveOptionButton, null);
    }

    @Override
    protected void refreshWidgetPositions() {
        super.refreshWidgetPositions();
        body.position(width, layout);
    }

    private Text perspectiveText(Perspective perspective) {
        return switch (perspective) {
            case FIRST_PERSON -> ScreenTexts.EMPTY;
            case THIRD_PERSON_BACK -> Text.translatable("perspectivemod.perspective.back");
            case THIRD_PERSON_FRONT -> Text.translatable("perspectivemod.perspective.front");
        };
    }
}
