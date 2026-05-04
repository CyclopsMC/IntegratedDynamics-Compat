package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.refinedmods.refinedstorage.api.autocrafting.Pattern;
import com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken;
import com.refinedmods.refinedstorage.api.autocrafting.status.TaskStatus;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.core.evaluate.variable.*;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectProperties;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.part.aspect.read.AspectReadBuilders;
import org.cyclops.integrateddynamics.part.aspect.write.AspectWriteBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builders for Refined Storage aspects
 * @author rubensworks
 */
public class RefinedStorageAspects {

    private static Optional<Network> getNetwork(DimPos pos) {
        return IModHelpersNeoForge.get().getCapabilityHelpers()
                .getCapability(pos, RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability())
                .flatMap(provider -> provider.getContainers().stream()
                        .map(c -> c.getNode().getNetwork())
                        .filter(Objects::nonNull)
                        .findFirst());
    }

    public static final class Read {

        public static final IAspectValuePropagator<Pair<PartTarget, IAspectProperties>, Optional<Pair<DimPos, com.refinedmods.refinedstorage.api.network.Network>>> PROP_GET_NETWORK = input -> {
            DimPos pos = input.getLeft().getTarget().getPos();
            return getNetwork(pos).map(network -> Pair.of(pos, network));
        };

        public static final AspectBuilder<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean, Optional<Pair<DimPos, com.refinedmods.refinedstorage.api.network.Network>>>
                BUILDER_BOOLEAN = AspectReadBuilders.BUILDER_BOOLEAN.handle(PROP_GET_NETWORK, "refinedstorage");
        public static final AspectBuilder<ValueTypeList.ValueList, ValueTypeList, Optional<Pair<DimPos, com.refinedmods.refinedstorage.api.network.Network>>>
                BUILDER_LIST = AspectReadBuilders.BUILDER_LIST.handle(PROP_GET_NETWORK, "refinedstorage");

        public static final class Network {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_APPLICABLE =
                    BUILDER_BOOLEAN.appendKind("network")
                            .handle(Optional::isPresent)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable")
                            .buildRead();

        }

        public static final class Inventory {

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_ITEMSTACKS =
                    BUILDER_LIST.appendKind("inventory").handle(networkPairOpt -> networkPairOpt
                            .map(pair -> ValueTypeList.ValueList.ofFactory(
                                    new ValueTypeListProxyPositionedNetworkMasterItemInventory(pair.getLeft())))
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList())), "itemstacks")
                            .buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_CRAFTABLEITEMS =
                    BUILDER_LIST.appendKind("inventory").handle(networkPairOpt -> networkPairOpt
                            .map(pair -> {
                                com.refinedmods.refinedstorage.api.network.Network network = pair.getRight();
                                AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                                List<ValueObjectTypeItemStack.ValueItemStack> itemStacks = Lists.newArrayList();
                                for (Pattern pattern : autocrafting.getPatterns()) {
                                    for (ResourceAmount output : pattern.layout().outputs()) {
                                        if (output.resource() instanceof ItemResource itemResource) {
                                            itemStacks.add(ValueObjectTypeItemStack.ValueItemStack.of(itemResource.toItemStack(output.amount())));
                                        }
                                    }
                                }
                                return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, itemStacks);
                            })
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList())), "craftableitems").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_CRAFTINGITEMS =
                    BUILDER_LIST.appendKind("inventory").handle(networkPairOpt -> networkPairOpt
                            .map(pair -> {
                                com.refinedmods.refinedstorage.api.network.Network network = pair.getRight();
                                AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                                List<ValueObjectTypeItemStack.ValueItemStack> itemStacks = Lists.newArrayList();
                                for (TaskStatus status : autocrafting.getStatuses()) {
                                    if (status.info().resource() instanceof ItemResource itemResource) {
                                        itemStacks.add(ValueObjectTypeItemStack.ValueItemStack.of(itemResource.toItemStack(status.info().amount())));
                                    }
                                }
                                return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, itemStacks);
                            })
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList())), "craftingitems").buildRead();
        }

        public static final class Fluid {

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_FLUIDSTACKS =
                    BUILDER_LIST.appendKind("fluid").handle(networkPairOpt -> networkPairOpt
                            .map(pair -> ValueTypeList.ValueList.ofFactory(
                                    new ValueTypeListProxyPositionedNetworkMasterFluidInventory(pair.getLeft())))
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_FLUIDSTACK, Collections.emptyList())), "fluidstacks").buildRead();
        }

    }

    public static final class Write {

        public static final IAspectPropertyTypeInstance<ValueTypeBoolean, ValueTypeBoolean.ValueBoolean> PROPERTY_SKIPSTORAGE =
                new AspectPropertyTypeInstance<>(ValueTypes.BOOLEAN, "aspect.aspecttypes.integrateddynamics.boolean.refinedstorage.skipstorage");
        public static final IAspectPropertyTypeInstance<ValueTypeInteger, ValueTypeInteger.ValueInteger> PROPERTY_CRAFTCOUNT =
                new AspectPropertyTypeInstance<>(ValueTypes.INTEGER, "aspect.aspecttypes.integrateddynamics.integer.refinedstorage.craftcount");
        public static final IAspectProperties CRAFTING_PROPERTIES = new AspectProperties(ImmutableList.<IAspectPropertyTypeInstance>of(
                PROPERTY_SKIPSTORAGE,
                PROPERTY_CRAFTCOUNT
        ));
        static {
            CRAFTING_PROPERTIES.setValue(PROPERTY_SKIPSTORAGE, ValueTypeBoolean.ValueBoolean.of(false));
            CRAFTING_PROPERTIES.setValue(PROPERTY_CRAFTCOUNT, ValueTypeInteger.ValueInteger.of(1));
        }

        protected static void triggerItemStackCrafting(IAspectProperties aspectProperties, com.refinedmods.refinedstorage.api.network.Network network, ItemStack itemStack) {
            ItemResource itemResource = ItemResource.ofItemStack(itemStack);
            long craftCount = aspectProperties.getValue(PROPERTY_CRAFTCOUNT).getRawValue();

            if (aspectProperties.getValue(PROPERTY_SKIPSTORAGE).getRawValue()) {
                StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                if (storage.get(itemResource) >= itemStack.getCount()) {
                    return;
                }
            }

            AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
            try {
                autocrafting.ensureTask(itemResource, craftCount, Actor.EMPTY, CancellationToken.NONE);
            } catch (IllegalStateException e) {
                // No pattern found for this item - ignore silently
            }
        }

        public static final IAspectWrite<ValueObjectTypeItemStack.ValueItemStack, ValueObjectTypeItemStack>
                ITEMSTACK_CRAFT = AspectWriteBuilders.BUILDER_ITEMSTACK.appendKind("refinedstorage")
                .withProperties(CRAFTING_PROPERTIES).handle(
                        input -> {
                            if (!input.getRight().getRawValue().isEmpty()) {
                                DimPos pos = input.getLeft().getTarget().getPos();
                                getNetwork(pos).ifPresent(network -> {
                                    ItemStack itemStack = input.getRight().getRawValue();
                                    triggerItemStackCrafting(input.getMiddle(), network, itemStack);
                                });
                            }
                            return null;
                        }, "craft").buildWrite();

        public static final IAspectWrite<ValueTypeList.ValueList, ValueTypeList>
                LIST_CRAFT = AspectWriteBuilders.BUILDER_LIST.appendKind("refinedstorage")
                .withProperties(CRAFTING_PROPERTIES).handle(
                        (IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueTypeList.ValueList>, Void>) input -> {
                            DimPos pos = input.getLeft().getTarget().getPos();
                            getNetwork(pos).ifPresent(network -> {
                                if (input.getRight().getRawValue().getValueType() == ValueTypes.OBJECT_ITEMSTACK) {
                                    for (IValue value : (Iterable<IValue>) input.getRight().getRawValue()) {
                                        ValueObjectTypeItemStack.ValueItemStack valueItemStack = (ValueObjectTypeItemStack.ValueItemStack) value;
                                        if (!valueItemStack.getRawValue().isEmpty()) {
                                            triggerItemStackCrafting(input.getMiddle(), network, valueItemStack.getRawValue());
                                        }
                                    }
                                }
                            });
                            return null;
                        }, "craft").buildWrite();

        public static final IAspectWrite<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean>
                BOOLEAN_CANCELCRAFT = AspectWriteBuilders.BUILDER_BOOLEAN.appendKind("refinedstorage")
                .handle(
                        (IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueTypeBoolean.ValueBoolean>, Void>) input -> {
                            if (input.getRight().getRawValue()) {
                                DimPos pos = input.getLeft().getTarget().getPos();
                                getNetwork(pos).ifPresent(network -> {
                                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                                    autocrafting.cancelAll();
                                });
                            }
                            return null;
                        }, "cancelcraft").buildWrite();

        public static final IAspectWrite<ValueObjectTypeItemStack.ValueItemStack, ValueObjectTypeItemStack>
                ITEMSTACK_CANCELCRAFT = AspectWriteBuilders.BUILDER_ITEMSTACK.appendKind("refinedstorage")
                .handle(
                        (IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueObjectTypeItemStack.ValueItemStack>, Void>) input -> {
                            if (!input.getRight().getRawValue().isEmpty()) {
                                DimPos pos = input.getLeft().getTarget().getPos();
                                getNetwork(pos).ifPresent(network -> {
                                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                                    ItemResource itemResource = ItemResource.ofItemStack(input.getRight().getRawValue());
                                    for (TaskStatus status : autocrafting.getStatuses()) {
                                        if (itemResource.equals(status.info().resource())) {
                                            autocrafting.cancel(status.info().id());
                                        }
                                    }
                                });
                            }
                            return null;
                        }, "cancelcraft").buildWrite();

        public static final IAspectWrite<ValueTypeList.ValueList, ValueTypeList>
                LIST_CANCELCRAFT = AspectWriteBuilders.BUILDER_LIST.appendKind("refinedstorage")
                .handle(
                        (IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueTypeList.ValueList>, Void>) input -> {
                            DimPos pos = input.getLeft().getTarget().getPos();
                            getNetwork(pos).ifPresent(network -> {
                                if (input.getRight().getRawValue().getValueType() == ValueTypes.OBJECT_ITEMSTACK) {
                                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                                    List<TaskStatus> statuses = autocrafting.getStatuses();
                                    for (IValue value : (Iterable<IValue>) input.getRight().getRawValue()) {
                                        ValueObjectTypeItemStack.ValueItemStack valueItemStack = (ValueObjectTypeItemStack.ValueItemStack) value;
                                        if (!valueItemStack.getRawValue().isEmpty()) {
                                            ItemResource itemResource = ItemResource.ofItemStack(valueItemStack.getRawValue());
                                            for (TaskStatus status : statuses) {
                                                if (itemResource.equals(status.info().resource())) {
                                                    autocrafting.cancel(status.info().id());
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            return null;
                        }, "cancelcraft").buildWrite();

    }

}
