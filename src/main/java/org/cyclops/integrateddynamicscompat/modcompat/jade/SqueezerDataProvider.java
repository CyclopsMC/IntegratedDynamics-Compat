package org.cyclops.integrateddynamicscompat.modcompat.jade;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.blockentity.BlockEntitySqueezer;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

/**
 * Waila data provider for the squeezer.
 * @author rubensworks
 *
 */
public class SqueezerDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "squeezer");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(config.get(SqueezerDataProvider.ID)) {
            tooltip.addAll(NBTClassType.getClassType(List.class).readPersistedField("tooltip", accessor.getServerData(), accessor.getLevel().registryAccess()));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        BlockEntitySqueezer tile = (BlockEntitySqueezer) accessor.getBlockEntity();
        List<Component> tooltip = Lists.newArrayList();
        if (!tile.getInventory().getItem(0).isEmpty()) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.item",
                    tile.getInventory().getItem(0).getDisplayName()));
        }
        if (!tile.getTank().isEmpty()) {
            tooltip.add(Component.translatable("gui." + Reference.MOD_ID + ".waila.fluid",
                    tile.getTank().getFluid().getDisplayName(), tile.getTank().getFluidAmount()));
        }
        NBTClassType.getClassType(List.class).writePersistedField("tooltip", tooltip, tag, accessor.getLevel().registryAccess());
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

}
