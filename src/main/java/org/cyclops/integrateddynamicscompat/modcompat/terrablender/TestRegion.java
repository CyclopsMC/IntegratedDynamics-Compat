package org.cyclops.integrateddynamicscompat.modcompat.terrablender;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import org.cyclops.integrateddynamics.Reference;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * @author rubensworks
 */
public class TestRegion extends Region
{
    public TestRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<com.mojang.datafixers.util.Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.FOREST, ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "meneglin")));
        });
    }
}
