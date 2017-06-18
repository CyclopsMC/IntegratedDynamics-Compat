package org.cyclops.integrateddynamicscompat.modcompat.minetweaker;

import minetweaker.MineTweakerAPI;
import org.cyclops.integrateddynamicscompat.modcompat.minetweaker.handlers.DryingBasinHandler;
import org.cyclops.integrateddynamicscompat.modcompat.minetweaker.handlers.SqueezerHandler;

/**
 * @author rubensworks
 */
public class MineTweaker {

    public static void register() {
        MineTweakerAPI.registerClass(DryingBasinHandler.class);
        MineTweakerAPI.registerClass(SqueezerHandler.class);
    }

}
