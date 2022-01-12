package org.cyclops.integrateddynamicscompat.modcompat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import org.cyclops.integrateddynamics.core.tileentity.BlockEntityMultipartTicking;
import org.cyclops.integrateddynamics.tileentity.BlockEntityDryingBasin;
import org.cyclops.integrateddynamics.tileentity.BlockEntityMechanicalDryingBasin;
import org.cyclops.integrateddynamics.tileentity.BlockEntityMechanicalSqueezer;
import org.cyclops.integrateddynamics.tileentity.BlockEntityProxy;
import org.cyclops.integrateddynamics.tileentity.BlockEntitySqueezer;

/**
 * Waila support class.
 * @author rubensworks
 *
 */
@WailaPlugin
public class Waila implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.addConfig(PartDataProvider.ID, true);
        registrar.addConfig(SqueezerDataProvider.ID, true);
        registrar.addConfig(DryingBasinDataProvider.ID, true);
        registrar.addConfig(MechanicalSqueezerDataProvider.ID, true);
        registrar.addConfig(MechanicalDryingBasinDataProvider.ID, true);
        registrar.addConfig(ProxyDataProvider.ID, true);

        PartDataProvider partDataProvider = new PartDataProvider();
        registrar.registerBlockDataProvider(partDataProvider, BlockEntityMultipartTicking.class);
        registrar.registerComponentProvider(partDataProvider, TooltipPosition.BODY, BlockEntityMultipartTicking.class);

        SqueezerDataProvider squeezerDataProvider = new SqueezerDataProvider();
        registrar.registerBlockDataProvider(squeezerDataProvider, BlockEntitySqueezer.class);
        registrar.registerComponentProvider(squeezerDataProvider, TooltipPosition.BODY, BlockEntitySqueezer.class);

        DryingBasinDataProvider dryingBasinDataProvider = new DryingBasinDataProvider();
        registrar.registerBlockDataProvider(dryingBasinDataProvider, BlockEntityDryingBasin.class);
        registrar.registerComponentProvider(dryingBasinDataProvider, TooltipPosition.BODY, BlockEntityDryingBasin.class);

        MechanicalSqueezerDataProvider mechanicalSqueezerDataProvider = new MechanicalSqueezerDataProvider();
        registrar.registerBlockDataProvider(mechanicalSqueezerDataProvider, BlockEntityMechanicalSqueezer.class);
        registrar.registerComponentProvider(mechanicalSqueezerDataProvider, TooltipPosition.BODY, BlockEntityMechanicalSqueezer.class);

        MechanicalDryingBasinDataProvider mechanicalDryingBasinDataProvider = new MechanicalDryingBasinDataProvider();
        registrar.registerBlockDataProvider(mechanicalDryingBasinDataProvider, BlockEntityMechanicalDryingBasin.class);
        registrar.registerComponentProvider(mechanicalDryingBasinDataProvider, TooltipPosition.BODY, BlockEntityMechanicalDryingBasin.class);

        ProxyDataProvider proxyDataProvider = new ProxyDataProvider();
        registrar.registerBlockDataProvider(proxyDataProvider, BlockEntityProxy.class);
        registrar.registerComponentProvider(proxyDataProvider, TooltipPosition.BODY, BlockEntityProxy.class);
    }

}
