package org.cyclops.integrateddynamicscompat.modcompat.jade;

import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/**
 * Jade data provider.
 * @author rubensworks
 *
 */
public class TooltipClientDataProviderClient implements IBlockComponentProvider {

    private final Identifier id;

    public TooltipClientDataProviderClient(Identifier id) {
        this.id = id;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(config.get(getUid())) {
            JadeIntegratedDynamicsConfig.appendTooltipClient(tooltip, accessor);
        }
    }

    @Override
    public Identifier getUid() {
        return this.id;
    }

}
