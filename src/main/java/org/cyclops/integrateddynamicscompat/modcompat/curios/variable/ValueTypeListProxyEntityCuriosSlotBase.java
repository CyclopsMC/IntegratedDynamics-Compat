package org.cyclops.integrateddynamicscompat.modcompat.curios.variable;

import java.util.LinkedList;
import java.util.Optional;

import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeListProxyEntityBase;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeString;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamicscompat.modcompat.curios.slot.CuriosSlotProxy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

/**
 * @author met4000
 */
public abstract class ValueTypeListProxyEntityCuriosSlotBase<T extends IValueType<V>, V extends IValue> extends ValueTypeListProxyEntityBase<T, V> implements INBTProvider {
    
    public ValueTypeListProxyEntityCuriosSlotBase(ResourceLocation name, T valueType, World world, Entity entity) {
        super(name, valueType, world, entity);
    }
    
    protected Optional<ICuriosItemHandler> getEntityCuriosItemHandler() {
        // check entity exists
        Entity e = getEntity();
        if (e == null) return Optional.empty();
        
        // check entity is a living entity (required for Curios API)
        if (!(e instanceof LivingEntity)) return Optional.empty();
        
        return CuriosApi.getCuriosHelper().getCuriosHandler((LivingEntity) e).resolve(); // could maybe keep as lazy?
    }
    
    protected Optional<CuriosSlotProxy> getCuriosSlotProxy(int index) throws EvaluationException {
        // assuming that `index` is < `getLength()`; does this need to be explicitly checked?
        
        Optional<ICuriosItemHandler> itemHandlerOptional = getEntityCuriosItemHandler();
        if (!itemHandlerOptional.isPresent()) return Optional.empty();
        ICuriosItemHandler itemHandler = itemHandlerOptional.get();        
        
        // find the slot offset of the stack handler with the slot we want
        // potential race condition, if the curios map is mutated during iteration
        int currentGlobalSlot = 0, delta;
        ICurioStacksHandler curioStacksHandler = null;
        for (ICurioStacksHandler currentCurioStacksHandler : itemHandler.getCurios().values()) {
            delta = currentCurioStacksHandler.getSlots();
            if (currentGlobalSlot + delta > index) {
                curioStacksHandler = currentCurioStacksHandler;
                break;
            }
            currentGlobalSlot += delta;
        }
        if (curioStacksHandler == null)
            throw new EvaluationException(null); // TODO: throw actual error
//                    new TranslationTextComponent(L10NValues.VALUETYPE_ERROR_INVALIDLISTVALUETYPE,
//                new TranslationTextComponent(expectedValueType.getTranslationKey()),
//                new TranslationTextComponent(list.getRawValue().getValueType().getTranslationKey())));
        
        int slotIndex = index - currentGlobalSlot;
        // TODO: should probably error if `slotIndex` isn't a valid slot
        
        return Optional.of(new CuriosSlotProxy(curioStacksHandler, slotIndex));
    }
    
    @Override
    public int getLength() {
        Optional<ICuriosItemHandler> itemHandlerOptional = getEntityCuriosItemHandler();
        if (!itemHandlerOptional.isPresent()) return 0;
        
        return itemHandlerOptional.get().getSlots();
    }
    
}
