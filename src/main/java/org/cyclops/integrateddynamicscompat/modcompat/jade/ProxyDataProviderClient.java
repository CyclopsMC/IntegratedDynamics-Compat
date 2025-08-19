package org.cyclops.integrateddynamicscompat.modcompat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Waila data provider for proxies.
 * @author rubensworks
 *
 */
public class ProxyDataProviderClient implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(config.get(getUid())) {
            tooltip.add(Component.translatable(L10NValues.GENERAL_ITEM_ID, accessor.getServerData().getInt("id")));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ProxyDataProviderServer.ID;
    }

}
