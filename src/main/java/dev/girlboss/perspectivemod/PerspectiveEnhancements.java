package dev.girlboss.perspectivemod;

import dev.girlboss.perspectivemod.options.ModOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

import java.io.File;

public class PerspectiveEnhancements implements ClientModInitializer {
    private static PerspectiveEnhancements instance;

    private Minecraft minecraft;
    private ModOptions options;

    private long pressTime = -1L;
    private CameraType lastPerspective = CameraType.FIRST_PERSON;

    public static PerspectiveEnhancements getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        minecraft = Minecraft.getInstance();
        options = new ModOptions(new File(minecraft.gameDirectory, "config/perspective-enhancements.properties"));
        options.load();
        ClientTickEvents.END_CLIENT_TICK.register(_ -> handlePerspectiveKey());
        instance = this;
    }

    public ModOptions getOptions() {
        return options;
    }

    private CameraType getNextPerspective() {
        var currentPerspective = minecraft.options.getCameraType();
        var perspectiveList = options.getPerspectiveList();

        var currentIndex = perspectiveList.indexOf(currentPerspective);
        if (currentIndex == -1) {
            return CameraType.FIRST_PERSON;
        }

        var nextIndex = currentIndex == perspectiveList.size() - 1 ? 0 : currentIndex + 1;
        return perspectiveList.get(nextIndex);
    }

    private void handlePerspectiveKey() {
        var clientOptions = minecraft.options;

        if (!options.holdEnabled() && pressTime == -1L) {
            while (clientOptions.keyTogglePerspective.consumeClick()) {
                changePerspective(getNextPerspective());
            }

            return;
        }

        if (clientOptions.keyTogglePerspective.isDown()) {
            if (pressTime != -1L) return;

            pressTime = System.currentTimeMillis();
            lastPerspective = clientOptions.getCameraType();
            changePerspective(getNextPerspective());
        } else {
            if (pressTime == -1L) return;

            var holdTime = System.currentTimeMillis() - pressTime;
            if (holdTime >= options.getHoldTime()) {
                changePerspective(lastPerspective);
            }

            this.pressTime = -1L;
        }
    }

    private void changePerspective(CameraType perspective) {
        minecraft.options.setCameraType(perspective);

        if (perspective.isFirstPerson() != minecraft.options.getCameraType().isFirstPerson()) {
            minecraft.gameRenderer.checkEntityPostEffect(
                    minecraft.options.getCameraType().isFirstPerson()
                            ? minecraft.getCameraEntity()
                            : null
            );
        }

        // TODO: is this really needed?
        minecraft.levelRenderer.needsUpdate();
    }
}
