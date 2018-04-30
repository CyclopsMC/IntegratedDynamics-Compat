package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.client.render.valuetype.ValueTypeWorldRenderers;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyFactories;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyNBTFactory;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.part.PartTypes;
import org.cyclops.integrateddynamics.part.aspect.Aspects;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.aspect.read.ThaumcraftAspects;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.client.render.valuetype.AspectValueTypeWorldRenderer;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.operator.OperatorBuilders;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueObjectTypeAspect;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueTypeListProxyPositionedAspectContainer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

import java.util.Collections;
import java.util.List;

/**
 * Compatibility plugin for Thaumcraft.
 * @author rubensworks
 *
 */
public class ThaumcraftModCompat implements IModCompat {

	public static ValueObjectTypeAspect OBJECT_ASPECT;
	public static ValueTypeListProxyNBTFactory<ValueObjectTypeAspect, ValueObjectTypeAspect.ValueAspect, ValueTypeListProxyPositionedAspectContainer> POSITIONED_ASPECTCONTAINER;

    @Override
    public String getModID() {
        return Reference.MOD_THAUMCRAFT;
    }

    @Override
    public void onInit(Step step) {
    	if(step == Step.PREINIT) {
			// Value types
			OBJECT_ASPECT = ValueTypes.REGISTRY.register(new ValueObjectTypeAspect());

			// Apects
			Aspects.REGISTRY.register(PartTypes.MACHINE_READER, Sets.<IAspect>newHashSet(
					ThaumcraftAspects.Read.Aspect.BOOLEAN_ISASPECTCONTAINER,
					ThaumcraftAspects.Read.Aspect.LIST_ASPECTCONTAINER,
					ThaumcraftAspects.Read.Aspect.ASPECT
			));

			// List proxy factories
			POSITIONED_ASPECTCONTAINER = ValueTypeListProxyFactories.REGISTRY.register(new ValueTypeListProxyNBTFactory<>("positionedAspectContainer", ValueTypeListProxyPositionedAspectContainer.class));

			// Operators
			/* Get aspects from item */
			Operators.REGISTRY.register(org.cyclops.integrateddynamics.core.evaluate.OperatorBuilders.ITEMSTACK_1_SUFFIX_LONG
					.output(ValueTypes.LIST).symbol("aspects").operatorName("getitemthaumcraftaspects")
					.function(variables -> {
                        ItemStack a = ((ValueObjectTypeItemStack.ValueItemStack) variables.getValue(0)).getRawValue();
                        if(!a.isEmpty()) {
                            AspectList aspectList = AspectHelper.getObjectAspects(a);
                            Aspect[] aspectArray = aspectList.getAspectsSortedByAmount();
                            List<ValueObjectTypeAspect.ValueAspect> list = Lists.newArrayListWithExpectedSize(aspectArray.length);
                            for(Aspect aspect : aspectArray) {
                                list.add(ValueObjectTypeAspect.ValueAspect.of(aspect, aspectList.getAmount(aspect)));
                            }
                            return ValueTypeList.ValueList.ofList(OBJECT_ASPECT, list);
                        } else {
                            return ValueTypeList.ValueList.ofList(OBJECT_ASPECT, Collections.emptyList());
                        }
                    }).build());
			/* Get amount of vis in aspect */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_1_SUFFIX_LONG
					.output(ValueTypes.INTEGER).symbolOperator("amount")
					.function(OperatorBuilders.FUNCTION_ASPECT_TO_INT
							.build(input -> input != null ? input.getRight() : 0)).build());
			/* Check if the raw aspect of two aspects are equal independent of amount */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_2
					.output(ValueTypes.BOOLEAN).symbol("=Aspect=").operatorName("israwaspectequal")
					.function(variables -> {
                        Optional<Pair<Aspect, Integer>> a = ((ValueObjectTypeAspect.ValueAspect) variables.getValue(0)).getRawValue();
                        Optional<Pair<Aspect, Integer>> b = ((ValueObjectTypeAspect.ValueAspect) variables.getValue(1)).getRawValue();
                        boolean equal = false;
                        if(a.isPresent() && b.isPresent()) {
                            equal = a.get().getKey().equals(b.get().getKey());
                        } else if(!a.isPresent() && !b.isPresent()) {
                            equal = true;
                        }
                        return ValueTypeBoolean.ValueBoolean.of(equal);
                    }).build());
			/* Get the aspects this aspect is made up of */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_1_SUFFIX_LONG
					.output(ValueTypes.LIST).symbol("compound aspects").operatorName("getcompoundaspects")
					.function(variables -> {
                        Optional<Pair<Aspect, Integer>> a = ((ValueObjectTypeAspect.ValueAspect) variables.getValue(0)).getRawValue();
                        if(a.isPresent()) {
                            Aspect[] aspectArray = a.get().getKey().getComponents();
                            List<ValueObjectTypeAspect.ValueAspect> list = Lists.newArrayListWithExpectedSize(aspectArray.length);
                            for(Aspect aspect : aspectArray) {
                                list.add(ValueObjectTypeAspect.ValueAspect.of(aspect, 1));
                            }
                            return ValueTypeList.ValueList.ofList(OBJECT_ASPECT, list);
                        }
                        return ValueTypeList.ValueList.ofList(OBJECT_ASPECT, Collections.emptyList());
                    }).build());
			/* Check if the given aspect is primal */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_1_SUFFIX_LONG
					.output(ValueTypes.BOOLEAN).symbolOperator("isprimal")
					.function(OperatorBuilders.FUNCTION_ASPECT_TO_BOOLEAN
							.build(input -> input != null && input.getLeft().isPrimal())).build());

			if(MinecraftHelpers.isClientSide()) {
				initClient();
			}
		}
    }

	@SideOnly(Side.CLIENT)
	protected void initClient() {
		ValueTypeWorldRenderers.REGISTRY.register(ThaumcraftModCompat.OBJECT_ASPECT, new AspectValueTypeWorldRenderer());
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Thaumcraft reader.";
	}

}
