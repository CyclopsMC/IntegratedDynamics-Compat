package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable;

import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxy;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeListProxyFactoryTypeRegistry;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyFactories;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyNBTFactorySimple;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyTail;
import org.cyclops.integrateddynamicscompat.Reference;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.ThaumcraftModCompat;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A list proxy for all aspects.
 */
public class ValueTypeListAllAspects extends ValueTypeListProxyBase<ValueObjectTypeAspect, ValueObjectTypeAspect.ValueAspect> implements INBTProvider {

    private static final List<ValueObjectTypeAspect.ValueAspect> ASPECTS = thaumcraft.api.aspects.Aspect.aspects
            .values().stream().map(aspect -> ValueObjectTypeAspect.ValueAspect.of(aspect, 1))
            .collect(Collectors.toList());

    public ValueTypeListAllAspects() {
        super(ThaumcraftModCompat.ALL_ASPECTS.getName(), ThaumcraftModCompat.OBJECT_ASPECT);
    }

    @Override
    public int getLength() {
        return ASPECTS.size();
    }

    @Override
    public ValueObjectTypeAspect.ValueAspect get(int index) {
        return ASPECTS.get(index);
    }

    @Override
    public void writeGeneratedFieldsToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(NBTTagCompound tag) {

    }

    public static class Factory extends ValueTypeListProxyNBTFactorySimple<ValueObjectTypeAspect, ValueObjectTypeAspect.ValueAspect, ValueTypeListAllAspects> {

        @Override
        public String getName() {
            return Reference.MOD_ID + ":thaumcraft:aspects";
        }

        @Override
        protected void serializeNbt(ValueTypeListAllAspects value, NBTTagCompound tag) {

        }

        @Override
        protected ValueTypeListAllAspects deserializeNbt(NBTTagCompound tag) {
            return new ValueTypeListAllAspects();
        }
    }
}
