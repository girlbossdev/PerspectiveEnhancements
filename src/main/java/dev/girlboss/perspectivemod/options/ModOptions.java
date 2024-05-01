package dev.girlboss.perspectivemod.options;

import net.minecraft.client.option.Perspective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ModOptions {
    private final Logger logger = LoggerFactory.getLogger("PerspectiveEnhancements");

    private final File file;

    private boolean hold = true;
    private int holdTime = 250;

    private boolean back = true;
    private boolean front = true;
    private Perspective defaultPerspective = Perspective.THIRD_PERSON_BACK;

    private final ArrayList<Perspective> perspectiveList = new ArrayList<>();

    public ModOptions(File file) {
        this.file = file;
    }

    public void load() {
        if (!file.exists()) {
            return;
        }

        var properties = new Properties();
        try {
            var inputStream = new FileInputStream(file);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            logger.error("Failed to load config file: ", e);
            return;
        }

        var hold = properties.getProperty("hold", Boolean.toString(this.hold));
        this.hold = Boolean.parseBoolean(hold);

        var holdTime = properties.getProperty("holdTime", Integer.toString(this.holdTime));
        try {
            this.holdTime = Integer.parseInt(holdTime);
        } catch (NumberFormatException ignored) {}

        var back = properties.getProperty("back", Boolean.toString(this.back));
        this.back = Boolean.parseBoolean(back);

        var front = properties.getProperty("front", Boolean.toString(this.front));
        this.front = Boolean.parseBoolean(front);

        var defaultPerspective = properties.getProperty("defaultPerspective", Integer.toString(this.defaultPerspective.ordinal()));
        try {
            var ordinal = Integer.parseInt(defaultPerspective);
            var values = Perspective.values();
            if (ordinal >= 0 && ordinal < values.length) {
                this.defaultPerspective = values[ordinal];
            }
        } catch (NumberFormatException ignored) {}
    }

    public void save() {
        var properties = new Properties();
        properties.setProperty("hold", Boolean.toString(hold));
        properties.setProperty("holdTime", Integer.toString(holdTime));
        properties.setProperty("back", Boolean.toString(back));
        properties.setProperty("front", Boolean.toString(front));
        properties.setProperty("defaultPerspective", Integer.toString(defaultPerspective.ordinal()));

        try {
            var inputStream = new FileOutputStream(file);
            properties.store(inputStream, "Configuration file for Perspective Enhancements");
        } catch (IOException e) {
            logger.error("Failed to save config file: ", e);
        }
    }

    public boolean holdEnabled() {
        return hold;
    }

    public void setHold(boolean value) {
        hold = value;
    }

    public int getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(int value) {
        holdTime = value;
    }

    public boolean backEnabled() {
        return back;
    }

    public void setBack(boolean value) {
        back = value;
        rebuildPerspectiveList();
    }

    public boolean frontEnabled() {
        return front;
    }

    public void setFront(boolean value) {
        front = value;
        rebuildPerspectiveList();
    }

    public Perspective getDefaultPerspective() {
        return defaultPerspective;
    }

    public void setDefaultPerspective(Perspective perspective) {
        defaultPerspective = perspective;
        rebuildPerspectiveList();
    }

    public List<Perspective> getPerspectiveList() {
        if (perspectiveList.isEmpty()) {
            rebuildPerspectiveList();
        }

        return perspectiveList;
    }

    private void rebuildPerspectiveList() {
        perspectiveList.clear();
        perspectiveList.add(Perspective.FIRST_PERSON);

        if (defaultPerspective == Perspective.THIRD_PERSON_BACK) {
            if (back) perspectiveList.add(Perspective.THIRD_PERSON_BACK);
            if (front) perspectiveList.add(Perspective.THIRD_PERSON_FRONT);
        } else {
            if (front) perspectiveList.add(Perspective.THIRD_PERSON_FRONT);
            if (back) perspectiveList.add(Perspective.THIRD_PERSON_BACK);
        }
    }
}
