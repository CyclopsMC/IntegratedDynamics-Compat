package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.aspect.read;

import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeDouble;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.part.aspect.read.AspectReadBuilders;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.ThaumcraftModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueObjectTypeAspect;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueTypeListProxyPositionedAspectContainer;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aura.AuraHelper;

/**
 * Builders for thaumcraft aspects
 * @author rubensworks
 */
public class ThaumcraftAspects {

    public static final class Read {

        public static final class Aspect {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISASPECTCONTAINER =
                    AspectReadBuilders.BUILDER_BOOLEAN.appendKind("thaumcraft").handle(input -> {
                        DimPos dimPos = input.getLeft().getTarget().getPos();
                        return TileHelpers.getSafeTile(dimPos.getWorld(), dimPos.getBlockPos(), IAspectContainer.class) != null;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isaspectcontainer").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_ASPECTCONTAINER =
                    AspectReadBuilders.BUILDER_LIST.appendKind("thaumcraft")
                            .handle(input -> ValueTypeList.ValueList.ofFactory(new ValueTypeListProxyPositionedAspectContainer(
                                    input.getLeft().getTarget().getPos()))).appendKind("aspectcontainer").buildRead();

            public static final IAspectRead<ValueObjectTypeAspect.ValueAspect, ValueObjectTypeAspect> ASPECT =
                    AspectBuilder.forReadType(ThaumcraftModCompat.OBJECT_ASPECT).appendKind("thaumcraft").withProperties(AspectReadBuilders.LIST_PROPERTIES)
                            .handle(input -> {
                                int i = input.getRight().getValue(AspectReadBuilders.PROPERTY_LISTINDEX).getRawValue();
                                DimPos dimPos = input.getLeft().getTarget().getPos();
                                IAspectContainer aspectContainer = TileHelpers.getSafeTile(dimPos, IAspectContainer.class);
                                AspectList aspectList;
                                if (aspectContainer == null || i >= (aspectList = aspectContainer.getAspects()).size()) {
                                    return ValueObjectTypeAspect.ValueAspect.ofNull();
                                } else {
                                    thaumcraft.api.aspects.Aspect aspect = aspectList.getAspects()[i];
                                    return ValueObjectTypeAspect.ValueAspect.of(aspect, aspectList.getAmount(aspect));
                                }
                            }).appendKind("aspectcontainer").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_AURA_VIS =
                    AspectReadBuilders.BUILDER_DOUBLE.appendKind("thaumcraft").handle(input -> {
                        DimPos dimPos = input.getLeft().getTarget().getPos();
                        return (double) AuraHelper.getVis(dimPos.getWorld(), dimPos.getBlockPos());
                    }).handle(AspectReadBuilders.PROP_GET_DOUBLE, "auravis").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_AURA_FLUX =
                    AspectReadBuilders.BUILDER_DOUBLE.appendKind("thaumcraft").handle(input -> {
                        DimPos dimPos = input.getLeft().getTarget().getPos();
                        return (double) AuraHelper.getFlux(dimPos.getWorld(), dimPos.getBlockPos());
                    }).handle(AspectReadBuilders.PROP_GET_DOUBLE, "auraflux").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_AURA_BASE =
                    AspectReadBuilders.BUILDER_INTEGER.appendKind("thaumcraft").handle(input -> {
                        DimPos dimPos = input.getLeft().getTarget().getPos();
                        return AuraHelper.getAuraBase(dimPos.getWorld(), dimPos.getBlockPos());
                    }).handle(AspectReadBuilders.PROP_GET_INTEGER, "aurabase").buildRead();

        }

    }

}
