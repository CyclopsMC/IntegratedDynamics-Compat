package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable;

import com.google.common.base.Optional;
import lombok.ToString;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeNamed;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueOptionalBase;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeItemStackLPElement;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeLPElementBase;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.ThaumcraftModCompat;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;

import java.util.Objects;

/**
 * Value type with values that are thaumcraft aspects.
 * @author rubensworks
 */
public class ValueObjectTypeAspect extends ValueObjectTypeBase<ValueObjectTypeAspect.ValueAspect> implements IValueTypeNamed<ValueObjectTypeAspect.ValueAspect> {

    private static final String DELIMITER = ";";

    public ValueObjectTypeAspect() {
        super("thaumcraftaspect");
    }

    @Override
    public ValueAspect getDefault() {
        return ValueAspect.ofNull();
    }

    @Override
    public String toCompactString(ValueAspect value) {
        Optional<Pair<Aspect, Integer>> aspect = value.getRawValue();
        if(aspect.isPresent()) {
            return aspect.get().getKey().getName() + ":" + aspect.get().getValue();
        }
        return "";
    }

    @Override
    public String serialize(ValueAspect value) {
        Optional<Pair<Aspect, Integer>> aspect = value.getRawValue();
        if(aspect.isPresent()) {
            return aspect.get().getKey().getTag() + DELIMITER + aspect.get().getValue();
        }
        return "";
    }

    @Override
    public ValueAspect deserialize(String value) {
        String[] split = value.split(DELIMITER);
        if(split.length == 2) {
            try {
                return ValueAspect.of(Aspect.getAspect(split[0]), Integer.parseInt(split[1]));
            } catch (NumberFormatException e) {}
        }
        return ValueAspect.ofNull();
    }

    @Override
    public String getName(ValueAspect a) {
        Optional<Pair<Aspect, Integer>> aspect = a.getRawValue();
        if(aspect.isPresent()) {
            return aspect.get().getKey().getName();
        }
        return "";
    }

    @Override
    public ValueTypeLPElementBase createLogicProgrammerElement() {
        return new ValueTypeItemStackLPElement<>(this, new ValueTypeItemStackLPElement.IItemStackToValue<ValueObjectTypeAspect.ValueAspect>() {
            @Override
            public boolean isNullable() {
                return true;
            }

            @Override
            public L10NHelpers.UnlocalizedString validate(ItemStack itemStack) {
                return null;
            }

            @Override
            public ValueObjectTypeAspect.ValueAspect getValue(ItemStack itemStack) {
                AspectList aspectList = AspectHelper.getObjectAspects(itemStack);
                if (aspectList == null || aspectList.size() == 0) {
                    return ValueAspect.ofNull();
                }
                Aspect[] aspectArray = aspectList.getAspectsSortedByAmount();
                return ValueObjectTypeAspect.ValueAspect.of(aspectArray[0], aspectList.getAmount(aspectArray[0]));
            }
        });
    }

    @ToString
    public static class ValueAspect extends ValueOptionalBase<Pair<Aspect, Integer>> {

        private ValueAspect(Aspect aspect, int amount) {
            super(ThaumcraftModCompat.OBJECT_ASPECT, Pair.of(aspect, amount));
        }

        public ValueAspect() {
            super(ThaumcraftModCompat.OBJECT_ASPECT, null);
        }

        public static ValueAspect of(Aspect aspect, int amount) {
            return new ValueAspect(Objects.requireNonNull(aspect), amount);
        }

        public static ValueAspect ofNull() {
            return new ValueAspect();
        }

        @Override
        protected boolean isEqual(Pair<Aspect, Integer> a, Pair<Aspect, Integer> b) {
            return Objects.equals(a.getKey().getTag(), b.getKey().getTag()) && Objects.equals(a.getValue(), b.getValue());
        }
    }

}
