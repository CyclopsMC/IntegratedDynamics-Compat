package org.cyclops.integrateddynamicscompat.modcompat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import org.cyclops.integrateddynamics.core.tileentity.TileMultipartTicking;
import org.cyclops.integrateddynamics.tileentity.TileDryingBasin;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalDryingBasin;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalSqueezer;
import org.cyclops.integrateddynamics.tileentity.TileProxy;
import org.cyclops.integrateddynamics.tileentity.TileSqueezer;

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
        registrar.registerBlockDataProvider(partDataProvider, TileMultipartTicking.class);
        registrar.registerComponentProvider(partDataProvider, TooltipPosition.BODY, TileMultipartTicking.class);

        SqueezerDataProvider squeezerDataProvider = new SqueezerDataProvider();
        registrar.registerBlockDataProvider(squeezerDataProvider, TileSqueezer.class);
        registrar.registerComponentProvider(squeezerDataProvider, TooltipPosition.BODY, TileSqueezer.class);

        DryingBasinDataProvider dryingBasinDataProvider = new DryingBasinDataProvider();
        registrar.registerBlockDataProvider(dryingBasinDataProvider, TileDryingBasin.class);
        registrar.registerComponentProvider(dryingBasinDataProvider, TooltipPosition.BODY, TileDryingBasin.class);

        MechanicalSqueezerDataProvider mechanicalSqueezerDataProvider = new MechanicalSqueezerDataProvider();
        registrar.registerBlockDataProvider(mechanicalSqueezerDataProvider, TileMechanicalSqueezer.class);
        registrar.registerComponentProvider(mechanicalSqueezerDataProvider, TooltipPosition.BODY, TileMechanicalSqueezer.class);

        MechanicalDryingBasinDataProvider mechanicalDryingBasinDataProvider = new MechanicalDryingBasinDataProvider();
        registrar.registerBlockDataProvider(mechanicalDryingBasinDataProvider, TileMechanicalDryingBasin.class);
        registrar.registerComponentProvider(mechanicalDryingBasinDataProvider, TooltipPosition.BODY, TileMechanicalDryingBasin.class);

        ProxyDataProvider proxyDataProvider = new ProxyDataProvider();
        registrar.registerBlockDataProvider(proxyDataProvider, TileProxy.class);
        registrar.registerComponentProvider(proxyDataProvider, TooltipPosition.BODY, TileProxy.class);
    }

}
