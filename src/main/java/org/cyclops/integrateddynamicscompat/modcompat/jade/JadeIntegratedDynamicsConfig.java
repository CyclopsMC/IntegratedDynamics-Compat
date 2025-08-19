package org.cyclops.integrateddynamicscompat.modcompat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.cyclops.integrateddynamics.block.*;
import org.cyclops.integrateddynamics.blockentity.*;
import org.cyclops.integrateddynamics.core.blockentity.BlockEntityMultipartTicking;
import snownee.jade.api.*;

import java.util.List;

/**
 * Waila support class.
 * @author rubensworks
 *
 */
@WailaPlugin
public class JadeIntegratedDynamicsConfig implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registrar) {
        registrar.registerBlockDataProvider(new PartDataProviderServer(), BlockEntityMultipartTicking.class);
        registrar.registerBlockDataProvider(new SqueezerDataProviderServer(), BlockEntitySqueezer.class);
        registrar.registerBlockDataProvider(new DryingBasinDataProviderServer(), BlockEntityDryingBasin.class);
        registrar.registerBlockDataProvider(new MechanicalSqueezerDataProviderServer(), BlockEntityMechanicalSqueezer.class);
        registrar.registerBlockDataProvider(new MechanicalDryingBasinDataProviderServer(), BlockEntityMechanicalDryingBasin.class);
        registrar.registerBlockDataProvider(new ProxyDataProviderServer(), BlockEntityProxy.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registrar) {
        registrar.registerBlockComponent(new TooltipClientDataProviderClient(PartDataProviderServer.ID), BlockCable.class);
        registrar.registerBlockComponent(new TooltipClientDataProviderClient(SqueezerDataProviderServer.ID), BlockSqueezer.class);
        registrar.registerBlockComponent(new TooltipClientDataProviderClient(DryingBasinDataProviderServer.ID), BlockDryingBasin.class);
        registrar.registerBlockComponent(new TooltipClientDataProviderClient(MechanicalSqueezerDataProviderServer.ID), BlockMechanicalSqueezer.class);
        registrar.registerBlockComponent(new TooltipClientDataProviderClient(MechanicalDryingBasinDataProviderServer.ID), BlockMechanicalDryingBasin.class);
        registrar.registerBlockComponent(new ProxyDataProviderClient(), BlockProxy.class);
    }

    public static void putTooltip(CompoundTag tag, List<Component> tooltip) {
        tag.put("tooltip", IModHelpers.get().getMinecraftHelpers().valueOutputToNbt(o -> NBTClassType.getClassType(List.class).writePersistedField("v", tooltip, o)));
    }

    public static void appendTooltipClient(ITooltip tooltip, BlockAccessor accessor) {
        IModHelpers.get().getMinecraftHelpers().valueInputFromNbtVoid(accessor.getServerData().getCompoundOrEmpty("tooltip"), accessor.getLevel().registryAccess(), i -> tooltip.addAll(NBTClassType.getClassType(List.class).readPersistedField("v", i)));
    }
}
