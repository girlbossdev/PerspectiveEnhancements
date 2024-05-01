package dev.girlboss.perspectivemod.gui;

import dev.girlboss.perspectivemod.PerspectiveEnhancements;
import dev.girlboss.perspectivemod.options.ModOptions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.Perspective;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class PerspectiveOptionsScreen extends Screen {
    private final ModOptions modOptions = PerspectiveEnhancements.getInstance().getOptions();

    private final Screen parent;

    private SliderWidget holdTimeOptionSlider;
    private CyclingButtonWidget<Perspective> defaultPerspectiveOptionButton;

    public PerspectiveOptionsScreen(Screen parent) {
        super(Text.translatable("perspectivemod.options.title"));
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void init() {
        super.init();

        var x = this.width / 2 - 155;
        var x2 = x + 160;
        var y = this.height / 6 - 12;

        var holdOptionButton = CyclingButtonWidget
                .builder(this::enabledText)
                .values(true, false)
                .initially(modOptions.holdEnabled())
                .build(x, y, 150, 20, Text.translatable("perspectivemod.options.hold"), (button, value) -> {
                    holdTimeOptionSlider.active = value;
                    modOptions.setHold(value);
                });

        holdTimeOptionSlider = new SliderWidget(
                x2, y, 150, 20,
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
                .build(x, y += 24, 150, 20, Text.translatable("perspectivemod.perspective.back"), (button, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.frontEnabled();
                    modOptions.setBack(value);
                });

        var frontOptionButton = CyclingButtonWidget
                .builder(this::enabledText)
                .values(true, false)
                .initially(modOptions.frontEnabled())
                .build(x2, y, 150, 20, Text.translatable("perspectivemod.perspective.front"), (button, value) -> {
                    defaultPerspectiveOptionButton.active = value && modOptions.backEnabled();
                    modOptions.setFront(value);
                });

        defaultPerspectiveOptionButton = CyclingButtonWidget
                .builder(this::perspectiveText)
                .values(Perspective.THIRD_PERSON_BACK, Perspective.THIRD_PERSON_FRONT)
                .initially(modOptions.getDefaultPerspective())
                .build(x, y += 24, 150, 20, Text.translatable("perspectivemod.options.defaultPerspective"), (button, value) -> {
                    modOptions.setDefaultPerspective(value);
                });
        defaultPerspectiveOptionButton.active = modOptions.backEnabled() && modOptions.frontEnabled();

        addDrawableChild(holdOptionButton);
        addDrawableChild(holdTimeOptionSlider);
        addDrawableChild(backOptionButton);
        addDrawableChild(frontOptionButton);
        addDrawableChild(defaultPerspectiveOptionButton);
        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> client.setScreen(parent)).dimensions(width / 2 - 100, y + 24, 200, 20).build());
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void close() {
        client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
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
