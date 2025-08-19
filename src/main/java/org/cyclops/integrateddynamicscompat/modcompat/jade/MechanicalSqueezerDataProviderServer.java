package org.cyclops.integrateddynamicscompat.modcompat.jade;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.blockentity.BlockEntityMechanicalSqueezer;
import snownee.jade.api.BlockAccessor;

import java.util.List;

/**
 * Waila data provider for the mechanical squeezer.
 * @author rubensworks
 *
 */
public class MechanicalSqueezerDataProviderServer extends SqueezerDataProviderServer {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "mechanical_squeezer");

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        BlockEntityMechanicalSqueezer tile = (BlockEntityMechanicalSqueezer) accessor.getBlockEntity();
        List<Component> tooltip = Lists.newArrayList();
        tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.energy",
                tile.getEnergyStored(), tile.getMaxEnergyStored()));
        if (!tile.getInventory().getItem(0).isEmpty()) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.item.in",
                    tile.getInventory().getItem(0).getDisplayName()));
        }
        for (int i = 1; i < tile.getInventory().getContainerSize(); i++) {
            if (!tile.getInventory().getItem(i).isEmpty()) {
                tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.item.out",
                        tile.getInventory().getItem(i).getDisplayName()));
            }
        }
        if (!tile.getTank().isEmpty()) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.fluid",
                    tile.getTank().getFluid().getHoverName(), tile.getTank().getFluidAmount()));
        }
        if (tile.getProgress() > 0) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.progress",
                    tile.getProgress() * 100 / tile.getMaxProgress()));
        }
        JadeIntegratedDynamicsConfig.putTooltip(tag, tooltip);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

}
