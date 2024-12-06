package org.cyclops.integrateddynamicscompat.modcompat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.integrateddynamics.blockentity.BlockEntityProxy;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Waila data provider for proxies.
 * @author rubensworks
 *
 */
public class ProxyDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(org.cyclops.integrateddynamicscompat.Reference.MOD_ID, "proxy");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(config.get(ProxyDataProvider.ID)) {
            tooltip.add(Component.translatable(L10NValues.GENERAL_ITEM_ID, accessor.getServerData().getInt("id")));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        BlockEntityProxy tile = (BlockEntityProxy) accessor.getBlockEntity();
        tag.putInt("id", tile.getProxyId());
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

}
