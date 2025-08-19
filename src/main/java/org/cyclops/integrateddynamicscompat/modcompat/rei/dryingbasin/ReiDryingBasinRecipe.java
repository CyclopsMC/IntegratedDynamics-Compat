package org.cyclops.integrateddynamicscompat.modcompat.rei.dryingbasin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.display.RecipeDisplayId;
import org.apache.commons.compress.utils.Lists;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplayDryingBasin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public class ReiDryingBasinRecipe implements Display {

    public static final DisplaySerializer<ReiDryingBasinRecipe> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    RecipeDisplayDryingBasin.MAP_CODEC.fieldOf("display").forGetter(ReiDryingBasinRecipe::getRecipeDisplay),
                    Codec.INT.xmap(RecipeDisplayId::new, RecipeDisplayId::index).optionalFieldOf("id").forGetter(ReiDryingBasinRecipe::recipeDisplayId)
            ).apply(instance, ReiDryingBasinRecipe::new)),
            StreamCodec.composite(
                    RecipeDisplayDryingBasin.STREAM_CODEC,
                    ReiDryingBasinRecipe::getRecipeDisplay,
                    ByteBufCodecs.optional(ByteBufCodecs.INT.map(RecipeDisplayId::new, RecipeDisplayId::index)),
                    ReiDryingBasinRecipe::recipeDisplayId,
                    ReiDryingBasinRecipe::new
            ), false
    );

    private final RecipeDisplayDryingBasin recipeDisplay;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final int duration;
    private final Optional<RecipeDisplayId> id;

    public ReiDryingBasinRecipe(RecipeDisplayDryingBasin recipeDisplay, Optional<RecipeDisplayId> id) {
        this.recipeDisplay = recipeDisplay;
        this.inputs = Lists.newArrayList();
        this.outputs = Lists.newArrayList();
        this.duration = recipeDisplay.duration();
        this.id = id;

        this.inputs.add(EntryIngredients.ofSlotDisplay(recipeDisplay.inputIngredient()));
        this.inputs.add(EntryIngredients.of(recipeDisplay.inputFluid().getFluid(), recipeDisplay.inputFluid().getAmount()));
        this.outputs.add(EntryIngredients.ofSlotDisplay(recipeDisplay.outputItem()));
        this.outputs.add(EntryIngredients.of(recipeDisplay.outputFluid().getFluid(), recipeDisplay.outputFluid().getAmount()));
    }

    public RecipeDisplayDryingBasin getRecipeDisplay() {
        return recipeDisplay;
    }

    public int getDuration() {
        return this.duration;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return this.inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return this.outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ReiDryingBasinCategory.ID;
    }

    public Optional<RecipeDisplayId> recipeDisplayId() {
        return id;
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return Optional.empty();
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
