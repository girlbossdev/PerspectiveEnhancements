package dev.girlboss.perspectivemod.compatibility;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.girlboss.perspectivemod.gui.PerspectiveOptionsScreen;

public class ModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PerspectiveOptionsScreen::new;
    }
}
