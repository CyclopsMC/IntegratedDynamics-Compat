package org.cyclops.integrateddynamicscompat.modcompat.rei.squeezer;

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
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.integrateddynamics.core.recipe.display.RecipeDisplaySqueezer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public class ReiSqueezerRecipe implements Display {

    public static final DisplaySerializer<ReiSqueezerRecipe> SERIALIZER = DisplaySerializer.of(
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    RecipeDisplaySqueezer.MAP_CODEC.fieldOf("display").forGetter(ReiSqueezerRecipe::getRecipeDisplay),
                    Codec.INT.xmap(RecipeDisplayId::new, RecipeDisplayId::index).optionalFieldOf("id").forGetter(ReiSqueezerRecipe::recipeDisplayId)
            ).apply(instance, ReiSqueezerRecipe::new)),
            StreamCodec.composite(
                    RecipeDisplaySqueezer.STREAM_CODEC,
                    ReiSqueezerRecipe::getRecipeDisplay,
                    ByteBufCodecs.optional(ByteBufCodecs.INT.map(RecipeDisplayId::new, RecipeDisplayId::index)),
                    ReiSqueezerRecipe::recipeDisplayId,
                    ReiSqueezerRecipe::new
            ), false
    );

    private final RecipeDisplaySqueezer recipeDisplay;
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final Optional<RecipeDisplayId> id;

    public ReiSqueezerRecipe(RecipeDisplaySqueezer recipeDisplay, Optional<RecipeDisplayId> id) {
        this.recipeDisplay = recipeDisplay;
        this.inputs = Lists.newArrayList();
        this.outputs = Lists.newArrayList();
        this.id = id;

        this.inputs.add(EntryIngredients.ofSlotDisplay(recipeDisplay.inputIngredient()));
        for (Pair<? extends SlotDisplay, Float> outputItem : recipeDisplay.outputItems()) {
            if (outputItem.getRight() == 1F) {
                this.outputs.add(EntryIngredients.ofSlotDisplay(outputItem.getLeft()));
            }
        }
        this.outputs.add(EntryIngredients.of(recipeDisplay.outputFluid().getFluid(), recipeDisplay.outputFluid().getAmount()));
    }

    public RecipeDisplaySqueezer getRecipeDisplay() {
        return recipeDisplay;
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
        return ReiSqueezerCategory.ID;
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
