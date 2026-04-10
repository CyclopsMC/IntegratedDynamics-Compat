package org.cyclops.integrateddynamicscompat.modcompat.jade;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.blockentity.BlockEntityMechanicalDryingBasin;
import snownee.jade.api.BlockAccessor;

import java.util.List;

/**
 * Waila data provider for the mechanical drying basin.
 * @author rubensworks
 *
 */
public class MechanicalDryingBasinDataProviderServer extends SqueezerDataProviderServer {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "mechanical_drying_basin");

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        BlockEntityMechanicalDryingBasin tile = (BlockEntityMechanicalDryingBasin) accessor.getBlockEntity();
        List<Component> tooltip = Lists.newArrayList();
        tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.energy",
                (int) tile.getEnergyHandler().getAmountAsLong(0), tile.getMaxEnergyStored()));
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
        if (!tile.getTankInput().isEmpty()) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.fluid.in",
                    tile.getTankInput().getFluid().getHoverName(), tile.getTankInput().getFluidAmount()));
        }
        if (!tile.getTankOutput().isEmpty()) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.fluid.out",
                    tile.getTankOutput().getFluid().getHoverName(), tile.getTankOutput().getFluidAmount()));
        }
        if (tile.getProgress() > 0) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.progress",
                    tile.getProgress() * 100 / tile.getMaxProgress()));
        }
        JadeIntegratedDynamicsConfig.putTooltip(tag, tooltip);
    }

    @Override
    public Identifier getUid() {
        return ID;
    }

}
