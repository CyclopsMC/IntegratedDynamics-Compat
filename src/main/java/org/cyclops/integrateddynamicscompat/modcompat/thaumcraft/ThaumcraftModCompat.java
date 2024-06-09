package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.neoforged.fml.relauncher.Side;
import net.neoforged.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.client.render.valuetype.ValueTypeWorldRenderers;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeEntity;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyBase;
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
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueTypeListAllAspects;
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
	public static ValueTypeListProxyNBTFactory<ValueObjectTypeAspect, ValueObjectTypeAspect.ValueAspect, ValueTypeListProxyPositionedAspectContainer>
			POSITIONED_ASPECTCONTAINER;
	public static ValueTypeListAllAspects.Factory ALL_ASPECTS;

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
			Aspects.REGISTRY.register(PartTypes.MACHINE_READER, Lists.newArrayList(
					ThaumcraftAspects.Read.Aspect.BOOLEAN_ISASPECTCONTAINER,
					ThaumcraftAspects.Read.Aspect.LIST_ASPECTCONTAINER,
					ThaumcraftAspects.Read.Aspect.ASPECT,
					ThaumcraftAspects.Read.Aspect.BOOLEAN_ISESSENTIATRANSPORT,
					ThaumcraftAspects.Read.Aspect.BOOLEAN_ISESSENTIARECEIVER,
					ThaumcraftAspects.Read.Aspect.BOOLEAN_ISESSENTIAPROVIDER,
					ThaumcraftAspects.Read.Aspect.ASPECT_ESSENTIATRANSPORTSUCTION,
					ThaumcraftAspects.Read.Aspect.ASPECT_ESSENTIATRANSPORTSUCTIONASPECT,
					ThaumcraftAspects.Read.Aspect.ASPECT_ESSENTIATRANSPORTCONTENTS,
					ThaumcraftAspects.Read.Aspect.INTEGER_ESSENTIATRANSPORT_MINSUCTION
			));
			Aspects.REGISTRY.register(PartTypes.WORLD_READER, Lists.newArrayList(
					ThaumcraftAspects.Read.Aspect.DOUBLE_AURA_VIS,
					ThaumcraftAspects.Read.Aspect.DOUBLE_AURA_FLUX,
					ThaumcraftAspects.Read.Aspect.INTEGER_AURA_BASE
			));
			Aspects.REGISTRY.register(PartTypes.EXTRADIMENSIONAL_READER, Lists.newArrayList(
					ThaumcraftAspects.Read.Aspect.LIST_ALL_ASPECTS
			));

			// List proxy factories
			POSITIONED_ASPECTCONTAINER = ValueTypeListProxyFactories.REGISTRY.register(
					new ValueTypeListProxyNBTFactory<>(Reference.MOD_ID + ":thaumcraft:positionedAspectContainer",
							ValueTypeListProxyPositionedAspectContainer.class));
			ALL_ASPECTS = ValueTypeListProxyFactories.REGISTRY.register(new ValueTypeListAllAspects.Factory());

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
					.output(ValueTypes.LIST).symbol("compound_aspects").operatorName("getcompoundaspects")
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
			/* Get the combination aspects of the two given aspects */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_2
					.symbol("combination_aspect").operatorName("getcombinationaspect")
					.function(variables -> {
						Optional<Pair<Aspect, Integer>> a = ((ValueObjectTypeAspect.ValueAspect) variables.getValue(0)).getRawValue();
						Optional<Pair<Aspect, Integer>> b = ((ValueObjectTypeAspect.ValueAspect) variables.getValue(1)).getRawValue();
						if(a.isPresent() && b.isPresent()) {
							Aspect aspect = AspectHelper.getCombinationResult(a.get().getLeft(), b.get().getLeft());
							if (aspect != null) {
								return ValueObjectTypeAspect.ValueAspect.of(aspect, 1);
							}
						}
						return ValueObjectTypeAspect.ValueAspect.ofNull();
					}).build());
			/* Check if the given aspect is primal */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_1_SUFFIX_LONG
					.output(ValueTypes.BOOLEAN).symbolOperator("isprimal")
					.function(OperatorBuilders.FUNCTION_ASPECT_TO_BOOLEAN
							.build(input -> input != null && input.getLeft().isPrimal())).build());
			/* Get the list of aspects in the given entity */
			Operators.REGISTRY.register(OperatorBuilders.ASPECT_1_SUFFIX_LONG
					.inputType(ValueTypes.OBJECT_ENTITY).output(ValueTypes.LIST)
					.symbol("entity_aspects").operatorName("entityaspects")
					.function(variables -> {
						Optional<Entity> a = ((ValueObjectTypeEntity.ValueEntity) variables.getValue(0)).getRawValue();
						List<ValueObjectTypeAspect.ValueAspect> aspects = Lists.newArrayList();
						if(a.isPresent()) {
							AspectList aspectList = AspectHelper.getEntityAspects(a.get());
							if (aspectList != null) {
								for (Aspect aspect : aspectList.getAspects()) {
									aspects.add(ValueObjectTypeAspect.ValueAspect.of(aspect, aspectList.getAmount(aspect)));
								}
							}
						}
						return ValueTypeList.ValueList.ofList(ThaumcraftModCompat.OBJECT_ASPECT, aspects);
					}).build());

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
