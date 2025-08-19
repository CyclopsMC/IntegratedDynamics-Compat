package org.cyclops.integrateddynamicscompat;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<IntegratedDynamicsCompat> {

    @ConfigurablePropertyCommon(category = "core", comment = "If JEI recipe filling should heuristically try to determine item tags from recipes.", requiresMcRestart = true)
    public static boolean jeiHeuristicTags = true;

    @ConfigurablePropertyCommon(category = "core", comment = "If REI recipe filling should heuristically try to determine item tags from recipes.", requiresMcRestart = true)
    public static boolean reiHeuristicTags = true;

    @ConfigurablePropertyCommon(
            category = "biome",
            comment = "The weight of spawning in the overworld, 0 disables spawning.",
            minimalValue = 0
    )
    public static int meneglinBiomeSpawnWeight = 5;

    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(IntegratedDynamicsCompat._instance, "general");
    }

}
