package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable;

import net.minecraft.nbt.CompoundNBT;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyNBTFactorySimple;
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
    public void writeGeneratedFieldsToNBT(CompoundNBT tag) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(CompoundNBT tag) {

    }

    public static class Factory extends ValueTypeListProxyNBTFactorySimple<ValueObjectTypeAspect, ValueObjectTypeAspect.ValueAspect, ValueTypeListAllAspects> {

        @Override
        public String getName() {
            return Reference.MOD_ID + ":thaumcraft:aspects";
        }

        @Override
        protected void serializeNbt(ValueTypeListAllAspects value, CompoundNBT tag) {

        }

        @Override
        protected ValueTypeListAllAspects deserializeNbt(CompoundNBT tag) {
            return new ValueTypeListAllAspects();
        }
    }
}
