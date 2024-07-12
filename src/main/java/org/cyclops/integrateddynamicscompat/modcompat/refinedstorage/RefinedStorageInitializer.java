package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeFluidStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyFactories;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyNBTFactory;
import org.cyclops.integrateddynamics.core.part.PartTypes;
import org.cyclops.integrateddynamics.part.aspect.Aspects;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect.RefinedStorageAspects;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect.ValueTypeListProxyPositionedNetworkMasterFluidInventory;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect.ValueTypeListProxyPositionedNetworkMasterItemInventory;

/**
 * @author rubensworks
 */
public class RefinedStorageInitializer implements ICompatInitializer {

    public static ValueTypeListProxyNBTFactory<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack,
            ValueTypeListProxyPositionedNetworkMasterItemInventory> POSITIONED_MASTERITEMINVENTORY;
    public static ValueTypeListProxyNBTFactory<ValueObjectTypeFluidStack, ValueObjectTypeFluidStack.ValueFluidStack,
            ValueTypeListProxyPositionedNetworkMasterFluidInventory> POSITIONED_MASTERFLUIDINVENTORY;

    @Override
    public void initialize() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    protected void setup(FMLCommonSetupEvent event) {
        Aspects.REGISTRY.register(PartTypes.MACHINE_READER, Lists.newArrayList(
                RefinedStorageAspects.Read.Network.BOOLEAN_APPLICABLE
        ));
        Aspects.REGISTRY.register(PartTypes.INVENTORY_READER, Lists.newArrayList(
                RefinedStorageAspects.Read.Inventory.LIST_ITEMSTACKS,
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTABLEITEMS,
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS
        ));
        Aspects.REGISTRY.register(PartTypes.FLUID_READER, Lists.newArrayList(
                RefinedStorageAspects.Read.Fluid.LIST_FLUIDSTACKS
        ));

        Aspects.REGISTRY.register(PartTypes.INVENTORY_WRITER, Lists.newArrayList(
                RefinedStorageAspects.Write.ITEMSTACK_CRAFT,
                RefinedStorageAspects.Write.LIST_CRAFT,
                RefinedStorageAspects.Write.BOOLEAN_CANCELCRAFT,
                RefinedStorageAspects.Write.ITEMSTACK_CANCELCRAFT,
                RefinedStorageAspects.Write.LIST_CANCELCRAFT
        ));

        POSITIONED_MASTERITEMINVENTORY = ValueTypeListProxyFactories.REGISTRY.register(
                new ValueTypeListProxyNBTFactory<>(ResourceLocation.parse(Reference.MOD_REFINEDSTORAGE, "positioned_item_inventory"),
                        ValueTypeListProxyPositionedNetworkMasterItemInventory.class));
        POSITIONED_MASTERFLUIDINVENTORY = ValueTypeListProxyFactories.REGISTRY.register(
                new ValueTypeListProxyNBTFactory<>(ResourceLocation.parse(Reference.MOD_REFINEDSTORAGE, "positioned_fluid_inventory"),
                        ValueTypeListProxyPositionedNetworkMasterFluidInventory.class));
    }

}
