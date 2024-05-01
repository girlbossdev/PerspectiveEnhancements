package dev.girlboss.perspectivemod;

import dev.girlboss.perspectivemod.options.ModOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

import java.io.File;

public class PerspectiveEnhancements implements ClientModInitializer {
    private static PerspectiveEnhancements instance;

    private MinecraftClient client;
    private ModOptions options;

    private long pressTime = -1L;
    private Perspective lastPerspective = Perspective.FIRST_PERSON;

    public static PerspectiveEnhancements getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        client = MinecraftClient.getInstance();
        options = new ModOptions(new File(client.runDirectory, "config/perspective-enhancements.properties"));
        options.load();
        ClientTickEvents.END_CLIENT_TICK.register(client -> handlePerspectiveKey());
        instance = this;
    }

    public ModOptions getOptions() {
        return options;
    }

    private Perspective getNextPerspective() {
        var currentPerspective = client.options.getPerspective();
        var perspectiveList = options.getPerspectiveList();

        var currentIndex = perspectiveList.indexOf(currentPerspective);
        if (currentIndex == -1) {
            return Perspective.FIRST_PERSON;
        }

        var nextIndex = currentIndex == perspectiveList.size() - 1 ? 0 : currentIndex + 1;
        return perspectiveList.get(nextIndex);
    }

    private void handlePerspectiveKey() {
        var clientOptions = client.options;

        if (!options.holdEnabled() && pressTime == -1L) {
            while (clientOptions.togglePerspectiveKey.wasPressed()) {
                changePerspective(getNextPerspective());
            }
            return;
        }

        if (clientOptions.togglePerspectiveKey.isPressed()) {
            if (pressTime != -1L) return;

            pressTime = System.currentTimeMillis();
            lastPerspective = clientOptions.getPerspective();
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

    private void changePerspective(Perspective perspective) {
        client.options.setPerspective(perspective);
        if (perspective.isFirstPerson() != client.options.getPerspective().isFirstPerson()) {
            client.gameRenderer.onCameraEntitySet(client.options.getPerspective().isFirstPerson() ? client.getCameraEntity() : null);
        }
        client.worldRenderer.scheduleTerrainUpdate();
    }
}
