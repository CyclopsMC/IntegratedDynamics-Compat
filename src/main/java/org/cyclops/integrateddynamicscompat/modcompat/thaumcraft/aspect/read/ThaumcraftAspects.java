package org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.aspect.read;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeDouble;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.helper.EnergyHelpers;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;
import org.cyclops.integrateddynamics.part.aspect.read.AspectReadBuilders;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.ThaumcraftModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueObjectTypeAspect;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueTypeListAllAspects;
import org.cyclops.integrateddynamicscompat.modcompat.thaumcraft.evaluate.variable.ValueTypeListProxyPositionedAspectContainer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.aura.AuraHelper;

import java.util.Objects;

/**
 * Builders for thaumcraft aspects
 * @author rubensworks
 */
public class ThaumcraftAspects {

    public static final class Read {

        public static final class Aspect {

            public static final AspectBuilder<ValueObjectTypeAspect.ValueAspect, ValueObjectTypeAspect, Pair<PartTarget, IAspectProperties>>
                    BUILDER_ASPECT = AspectBuilder.forReadType(ThaumcraftModCompat.OBJECT_ASPECT);

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
                    BUILDER_ASPECT.appendKind("thaumcraft").withProperties(AspectReadBuilders.LIST_PROPERTIES)
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

            public static final IAspectValuePropagator<Pair<PartTarget, IAspectProperties>, Pair<EnumFacing, IEssentiaTransport>>
                    PROP_GET_ESSENTIA_TRANSPORT = input -> Pair.of(input.getLeft().getTarget().getSide(),
                    TileHelpers.getSafeTile(input.getLeft().getTarget().getPos().getWorld(),
                            input.getLeft().getTarget().getPos().getBlockPos(),
                            IEssentiaTransport.class));

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISESSENTIATRANSPORT =
                    AspectReadBuilders.BUILDER_BOOLEAN.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle(input -> input.getRight() != null)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isessentiatransport").buildRead();

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISESSENTIARECEIVER =
                    AspectReadBuilders.BUILDER_BOOLEAN.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle(input -> input.getRight() != null && input.getRight().canInputFrom(input.getLeft()))
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isessentiareceiver").buildRead();

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISESSENTIAPROVIDER =
                    AspectReadBuilders.BUILDER_BOOLEAN.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle(input -> input.getRight() != null && input.getRight().canOutputTo(input.getLeft()))
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isessentiaprovider").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> ASPECT_ESSENTIATRANSPORTSUCTION =
                    AspectReadBuilders.BUILDER_INTEGER.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle((input) -> {
                                if (input.getRight() != null) {
                                    return input.getRight().getSuctionAmount(input.getLeft());
                                }
                                return 0;
                            }).handle(AspectReadBuilders.PROP_GET_INTEGER, "suction").buildRead();

            public static final IAspectRead<ValueObjectTypeAspect.ValueAspect, ValueObjectTypeAspect> ASPECT_ESSENTIATRANSPORTSUCTIONASPECT =
                    BUILDER_ASPECT.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle((input) -> {
                                if (input.getRight() != null) {
                                    int amount = input.getRight().getSuctionAmount(input.getLeft());
                                    thaumcraft.api.aspects.Aspect aspect = input.getRight().getSuctionType(input.getLeft());
                                    if (amount > 0 && aspect != null) {
                                        return ValueObjectTypeAspect.ValueAspect.of(aspect, amount);
                                    }
                                }
                                return ValueObjectTypeAspect.ValueAspect.ofNull();
                            }).appendKind("suction").buildRead();

            public static final IAspectRead<ValueObjectTypeAspect.ValueAspect, ValueObjectTypeAspect> ASPECT_ESSENTIATRANSPORTCONTENTS =
                    BUILDER_ASPECT.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle((input) -> {
                                if (input.getRight() != null) {
                                    int amount = input.getRight().getEssentiaAmount(input.getLeft());
                                    if (amount > 0) {
                                        return ValueObjectTypeAspect.ValueAspect.of(
                                                input.getRight().getEssentiaType(input.getLeft()), amount);
                                    }
                                }
                                return ValueObjectTypeAspect.ValueAspect.ofNull();
                            }).appendKind("essentia").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ESSENTIATRANSPORT_MINSUCTION =
                    AspectReadBuilders.BUILDER_INTEGER.handle(PROP_GET_ESSENTIA_TRANSPORT, "thaumcraft")
                            .handle(input -> input.getRight() != null ? input.getRight().getMinimumSuction() : 0)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "essentiaminsuction").buildRead();

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

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_ALL_ASPECTS =
                    AspectReadBuilders.BUILDER_LIST.appendKind("thaumcraft")
                            .handle(input -> ValueTypeList.ValueList.ofFactory(new ValueTypeListAllAspects()))
                            .appendKind("allaspects").buildRead();

        }

    }

}
