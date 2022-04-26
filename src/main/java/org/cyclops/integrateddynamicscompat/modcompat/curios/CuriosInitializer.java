package org.cyclops.integrateddynamicscompat.modcompat.curios;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamicscompat.modcompat.curios.operator.CuriosOperators;
import org.cyclops.integrateddynamicscompat.modcompat.curios.variable.CuriosValueTypeListProxyFactories;

/**
 * @author met4000
 */
public class CuriosInitializer implements ICompatInitializer {

    @Override
    public void initialize() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    protected void setup(FMLCommonSetupEvent event) {
    	CuriosValueTypeListProxyFactories.load();
    	
    	Operators.REGISTRY.register(CuriosOperators.OBJECT_ENTITY_CURIOSINVENTORY);
    	Operators.REGISTRY.register(CuriosOperators.OBJECT_ENTITY_CURIOSSLOTTYPES);
    }

}
