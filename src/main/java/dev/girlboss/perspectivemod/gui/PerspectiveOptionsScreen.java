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

    private OptionListWidget optionListWidget;

    private SliderWidget holdTimeOptionSlider;
    private CyclingButtonWidget<Perspective> defaultPerspectiveOptionButton;

    public PerspectiveOptionsScreen(Screen parent) {
        super(parent, null, Text.translatable("perspectivemod.options.title"));
    }

    @Override
    protected void init() {
        optionListWidget = addDrawableChild(new OptionListWidget(client, width, height, this));

        var holdOptionButton = CyclingButtonWidget
                .builder(this::enabledText)
                .values(true, false)
                .initially(modOptions.holdEnabled())
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
                .builder(this::enabledText)
                .values(true, false)
                .initially(modOptions.backEnabled())
                .build(Text.translatable("perspectivemod.perspective.back"), (button, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.frontEnabled();
                    modOptions.setBack(value);
                });

        var frontOptionButton = CyclingButtonWidget
                .builder(this::enabledText)
                .values(true, false)
                .initially(modOptions.frontEnabled())
                .build(Text.translatable("perspectivemod.perspective.front"), (button, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.backEnabled();
                    modOptions.setFront(value);
                });

        defaultPerspectiveOptionButton = CyclingButtonWidget
                .builder(this::perspectiveText)
                .values(Perspective.THIRD_PERSON_BACK, Perspective.THIRD_PERSON_FRONT)
                .initially(modOptions.getDefaultPerspective())
                .build(Text.translatable("perspectivemod.options.defaultPerspective"),
                        (button, value) -> modOptions.setDefaultPerspective(value)
                );
        defaultPerspectiveOptionButton.active = modOptions.backEnabled() && modOptions.frontEnabled();

        optionListWidget.addWidgetEntry(holdOptionButton, holdTimeOptionSlider);
        optionListWidget.addWidgetEntry(backOptionButton, frontOptionButton);
        optionListWidget.addWidgetEntry(defaultPerspectiveOptionButton, null);

        super.init();
    }

    @Override
    protected void initTabNavigation() {
        super.initTabNavigation();
        optionListWidget.position(width, layout);
    }

    private Text enabledText(boolean enabled) {
        return enabled ? Text.translatable("options.on") : Text.translatable("options.off");
    }

    private Text perspectiveText(Perspective perspective) {
        return switch (perspective) {
            case FIRST_PERSON -> ScreenTexts.EMPTY;
            case THIRD_PERSON_BACK -> Text.translatable("perspectivemod.perspective.back");
            case THIRD_PERSON_FRONT -> Text.translatable("perspectivemod.perspective.front");
        };
    }
}
