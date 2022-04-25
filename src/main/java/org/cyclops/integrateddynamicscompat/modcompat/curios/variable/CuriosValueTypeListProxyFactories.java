package org.cyclops.integrateddynamicscompat.modcompat.curios.variable;

import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyFactories;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyNBTFactory;

import net.minecraft.util.ResourceLocation;

public class CuriosValueTypeListProxyFactories {
	public static ValueTypeListProxyNBTFactory<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack, ValueTypeListProxyEntityCuriosInventory> ENTITY_CURIOSINVENTORY;
	
	public static void load() {
		if (ENTITY_CURIOSINVENTORY == null) {
			ENTITY_CURIOSINVENTORY = ValueTypeListProxyFactories.REGISTRY.register(new ValueTypeListProxyNBTFactory<>(new ResourceLocation(Reference.MOD_ID, "entity_curios_inventory"), ValueTypeListProxyEntityCuriosInventory.class));
		}
	}
}
