package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import org.cyclops.integrateddynamics.item.ItemVariable;
import org.cyclops.integrateddynamicscompat.Reference;

import mcjty.rftools.blocks.screens.ScreenHitTileEntity;
import mcjty.rftools.blocks.screens.ScreenTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RFToolsEventHandler {
	private static final ResourceLocation moduleProviderKey = new ResourceLocation(Reference.MOD_ID, "variable_module_provider");

	@SubscribeEvent
	public static void onAttachCapabilitiesToItemStack(AttachCapabilitiesEvent<ItemStack> event) {
		if(event.getObject().getItem() instanceof ItemVariable)
			event.addCapability(moduleProviderKey, VariableCardCapabilityProvider.INSTANCE);
	}

	private static final ResourceLocation cableKey = new ResourceLocation(Reference.MOD_ID, "cable");

	@SubscribeEvent
	public static void onAttachCapabilitiesToTileEntity(AttachCapabilitiesEvent<TileEntity> event) {
		TileEntity te = event.getObject();
		if((te instanceof ScreenTileEntity) || (te instanceof ScreenHitTileEntity))
			event.addCapability(cableKey, new CableNetworkCapabilityProvider(te));
	}
}
