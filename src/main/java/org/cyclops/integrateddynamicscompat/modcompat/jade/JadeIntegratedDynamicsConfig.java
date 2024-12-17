package org.cyclops.integrateddynamicscompat.modcompat.jade;

import org.cyclops.integrateddynamics.block.*;
import org.cyclops.integrateddynamics.blockentity.*;
import org.cyclops.integrateddynamics.core.blockentity.BlockEntityMultipartTicking;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * Waila support class.
 * @author rubensworks
 *
 */
@WailaPlugin
public class JadeIntegratedDynamicsConfig implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registrar) {
        registrar.registerBlockDataProvider(new PartDataProvider(), BlockEntityMultipartTicking.class);
        registrar.registerBlockDataProvider(new SqueezerDataProvider(), BlockEntitySqueezer.class);
        registrar.registerBlockDataProvider(new DryingBasinDataProvider(), BlockEntityDryingBasin.class);
        registrar.registerBlockDataProvider(new MechanicalSqueezerDataProvider(), BlockEntityMechanicalSqueezer.class);
        registrar.registerBlockDataProvider(new MechanicalDryingBasinDataProvider(), BlockEntityMechanicalDryingBasin.class);
        registrar.registerBlockDataProvider(new ProxyDataProvider(), BlockEntityProxy.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registrar) {
        registrar.registerBlockComponent(new PartDataProvider(), BlockCable.class);
        registrar.registerBlockComponent(new SqueezerDataProvider(), BlockSqueezer.class);
        registrar.registerBlockComponent(new DryingBasinDataProvider(), BlockDryingBasin.class);
        registrar.registerBlockComponent(new MechanicalSqueezerDataProvider(), BlockMechanicalSqueezer.class);
        registrar.registerBlockComponent(new MechanicalDryingBasinDataProvider(), BlockMechanicalDryingBasin.class);
        registrar.registerBlockComponent(new ProxyDataProvider(), BlockProxy.class);
    }
}
