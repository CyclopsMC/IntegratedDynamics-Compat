package org.cyclops.integrateddynamicscompat.modcompat.almostunified;

import com.almostreliable.unified.api.plugin.AlmostUnifiedNeoPlugin;
import com.almostreliable.unified.api.plugin.AlmostUnifiedPlugin;
import com.almostreliable.unified.api.unification.recipe.RecipeUnifier;
import com.almostreliable.unified.api.unification.recipe.RecipeUnifierRegistry;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.integrateddynamicscompat.Reference;

@AlmostUnifiedNeoPlugin
public class AUIntegratedDynamicsConfig implements AlmostUnifiedPlugin {

    @Override
    public ResourceLocation getPluginId() {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "main");
    }

    @Override
    public void registerRecipeUnifiers(RecipeUnifierRegistry registry) {
        RecipeUnifier dryingBasinRecipeUnifier = new DryingBasinRecipeUnifier();
        registry.registerForRecipeType(ResourceLocation.parse("integrateddynamics:drying_basin"), dryingBasinRecipeUnifier);
        registry.registerForRecipeType(ResourceLocation.parse("integrateddynamics:mechanical_drying_basin"), dryingBasinRecipeUnifier);

        RecipeUnifier squeezerRecipeUnifier = new SqueezerRecipeUnifier();
        registry.registerForRecipeType(ResourceLocation.parse("integrateddynamics:squeezer"), squeezerRecipeUnifier);
        registry.registerForRecipeType(ResourceLocation.parse("integrateddynamics:mechanical_squeezer"), squeezerRecipeUnifier);
    }
}
