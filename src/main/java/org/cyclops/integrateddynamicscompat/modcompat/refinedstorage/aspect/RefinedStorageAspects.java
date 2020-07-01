package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPattern;
import com.refinedmods.refinedstorage.api.autocrafting.task.ICraftingTask;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.node.INetworkNodeProxy;
import com.refinedmods.refinedstorage.api.util.IComparer;
import com.refinedmods.refinedstorage.api.util.StackListEntry;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeInteger;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectProperties;
import org.cyclops.integrateddynamics.core.part.aspect.property.AspectPropertyTypeInstance;
import org.cyclops.integrateddynamics.part.aspect.read.AspectReadBuilders;
import org.cyclops.integrateddynamics.part.aspect.write.AspectWriteBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Builders for Refined Storage aspects
 * @author rubensworks
 */
public class RefinedStorageAspects {

    @RSAPIInject
    public static IRSAPI RS;

    public static final class Read {

        public static final IAspectValuePropagator<Pair<PartTarget, IAspectProperties>, Optional<INetworkNode>> PROP_GET_NODE = input -> {
            DimPos pos = input.getLeft().getTarget().getPos();
            return TileHelpers.getSafeTile(pos, INetworkNodeProxy.class)
                    .map(INetworkNodeProxy::getNode);
        };
        public static final IAspectValuePropagator<Optional<INetworkNode>, Optional<INetwork>> PROP_GET_MASTER = input -> input.map(INetworkNode::getNetwork);

        public static final AspectBuilder<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean, Optional<INetwork>>
                BUILDER_BOOLEAN = AspectReadBuilders.BUILDER_BOOLEAN.handle(PROP_GET_NODE, "refinedstorage").handle(PROP_GET_MASTER);
        public static final AspectBuilder<ValueTypeList.ValueList, ValueTypeList, Optional<INetwork>>
                BUILDER_LIST = AspectReadBuilders.BUILDER_LIST.handle(PROP_GET_NODE, "refinedstorage").handle(PROP_GET_MASTER);

        public static final class Network {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_APPLICABLE =
                    BUILDER_BOOLEAN.appendKind("network")
                            .handle(Optional::isPresent)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable")
                            .buildRead();

        }

        public static final class Inventory {

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_ITEMSTACKS =
                    BUILDER_LIST.appendKind("inventory").handle(networkMaster -> networkMaster
                            .map(network -> ValueTypeList.ValueList.ofFactory(
                                    new ValueTypeListProxyPositionedNetworkMasterItemInventory(
                                            DimPos.of(network.getWorld(), network.getPosition()))))
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList())), "itemstacks")
                            .buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_CRAFTABLEITEMS =
                    BUILDER_LIST.appendKind("inventory").handle(networkMaster -> networkMaster
                            .map(network -> {
                                List<ValueObjectTypeItemStack.ValueItemStack> itemStacks = Lists.newArrayList();
                                for (ICraftingPattern craftingPattern : network.getCraftingManager().getPatterns()) {
                                    for (ItemStack itemStack : craftingPattern.getOutputs()) {
                                        itemStacks.add(ValueObjectTypeItemStack.ValueItemStack.of(itemStack));
                                    }
                                }
                                return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, itemStacks);
                            })
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList())), "craftableitems").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_CRAFTINGITEMS =
                    BUILDER_LIST.appendKind("inventory").handle(new IAspectValuePropagator<Optional<INetwork>, ValueTypeList.ValueList>() {

                        protected void addPatternItemStacks(List<ValueObjectTypeItemStack.ValueItemStack> itemStacks, ICraftingTask craftingTask) {
                            ICraftingPattern craftingPattern = craftingTask.getPattern();
                            for (ItemStack itemStack : craftingPattern.getOutputs()) {
                                itemStacks.add(ValueObjectTypeItemStack.ValueItemStack.of(itemStack));
                            }
                        }

                        @Override
                        public ValueTypeList.ValueList getOutput(Optional<INetwork> networkMaster) {
                            return networkMaster
                                    .map(network -> {
                                        List<ValueObjectTypeItemStack.ValueItemStack> itemStacks = Lists.newArrayList();
                                        for (ICraftingTask craftingTask : network.getCraftingManager().getTasks()) {
                                            addPatternItemStacks(itemStacks, craftingTask);
                                        }
                                        return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, itemStacks);
                                    })
                                    .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList()));
                        }
                    }, "craftingitems").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_MISSINGCRAFTINGITEMS =
                    BUILDER_LIST.appendKind("inventory").handle(new IAspectValuePropagator<Optional<INetwork>, ValueTypeList.ValueList>() {

                        protected void addPatternItemStacksMissing(List<ValueObjectTypeItemStack.ValueItemStack> itemStacks, ICraftingTask craftingTask) {
                            for (StackListEntry<ItemStack> itemStack : craftingTask.getMissing().getStacks()) {
                                itemStacks.add(ValueObjectTypeItemStack.ValueItemStack.of(itemStack.getStack()));
                            }
                        }

                        @Override
                        public ValueTypeList.ValueList getOutput(Optional<INetwork> networkMaster) {
                            return networkMaster
                                    .map(network -> {
                                        List<ValueObjectTypeItemStack.ValueItemStack> itemStacks = Lists.newArrayList();
                                        for (ICraftingTask craftingTask : network.getCraftingManager().getTasks()) {
                                            addPatternItemStacksMissing(itemStacks, craftingTask);
                                        }
                                        return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, itemStacks);
                                    })
                                    .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK, Collections.emptyList()));
                        }
                    }, "missingcraftingitems").buildRead();

        }

        public static final class Fluid {

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_FLUIDSTACKS =
                    BUILDER_LIST.appendKind("fluid").handle(networkMaster -> networkMaster
                            .map(network -> ValueTypeList.ValueList.ofFactory(
                                    new ValueTypeListProxyPositionedNetworkMasterFluidInventory(
                                            DimPos.of(network.getWorld(), network.getPosition()))))
                            .orElseGet(() -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_FLUIDSTACK, Collections.emptyList())), "fluidstacks").buildRead();
        }

    }

    public static final class Write {

        public static final IAspectPropertyTypeInstance<ValueTypeBoolean, ValueTypeBoolean.ValueBoolean> PROPERTY_SKIPCRAFTING =
                new AspectPropertyTypeInstance<>(ValueTypes.BOOLEAN, "aspect.aspecttypes.integrateddynamics.boolean.refinedstorage.skipcrafting");
        public static final IAspectPropertyTypeInstance<ValueTypeBoolean, ValueTypeBoolean.ValueBoolean> PROPERTY_SKIPSTORAGE =
                new AspectPropertyTypeInstance<>(ValueTypes.BOOLEAN, "aspect.aspecttypes.integrateddynamics.boolean.refinedstorage.skipstorage");
        public static final IAspectPropertyTypeInstance<ValueTypeInteger, ValueTypeInteger.ValueInteger> PROPERTY_CRAFTCOUNT =
                new AspectPropertyTypeInstance<>(ValueTypes.INTEGER, "aspect.aspecttypes.integrateddynamics.integer.refinedstorage.craftcount");
        public static final IAspectProperties CRAFTING_PROPERTIES = new AspectProperties(ImmutableList.<IAspectPropertyTypeInstance>of(
                PROPERTY_SKIPCRAFTING,
                PROPERTY_SKIPSTORAGE,
                PROPERTY_CRAFTCOUNT
        ));
        static {
            CRAFTING_PROPERTIES.setValue(PROPERTY_SKIPCRAFTING, ValueTypeBoolean.ValueBoolean.of(true));
            CRAFTING_PROPERTIES.setValue(PROPERTY_SKIPSTORAGE, ValueTypeBoolean.ValueBoolean.of(false));
            CRAFTING_PROPERTIES.setValue(PROPERTY_CRAFTCOUNT, ValueTypeInteger.ValueInteger.of(1));
        }

        protected static Void triggerItemStackCrafting(IAspectProperties aspectProperties, INetwork networkMaster, ItemStack itemStack) {
            int compareFlags = IComparer.COMPARE_NBT;
            ICraftingTask craftingTask = networkMaster.getCraftingManager().create(itemStack,
                    aspectProperties.getValue(PROPERTY_CRAFTCOUNT).getRawValue());
            if (craftingTask != null) {
                if (aspectProperties.getValue(PROPERTY_SKIPCRAFTING).getRawValue()) {
                    for (ICraftingTask task : networkMaster.getCraftingManager().getTasks()) {
                        for (ItemStack output : task.getPattern().getOutputs()) {
                            if (RS.getComparer().isEqual(output, itemStack, compareFlags)) {
                                // If there's already one crafting, stop.
                                return null;
                            }
                        }
                    }
                }

                if (aspectProperties.getValue(PROPERTY_SKIPSTORAGE).getRawValue()) {
                    ItemStack present = networkMaster.getItemStorageCache().getList().get(itemStack, compareFlags);
                    if (present != null && present.getCount() >= itemStack.getCount()) {
                        // If there's already one in the inventory, stop.
                        return null;
                    }
                }

                // Once we get here, we are certain that we want to shedule the task.
                craftingTask.calculate();
                if (!craftingTask.hasMissing()) {
                    networkMaster.getCraftingManager().start(craftingTask);
                }
            }
            return null;
        }

        public static final IAspectWrite<ValueObjectTypeItemStack.ValueItemStack, ValueObjectTypeItemStack>
                ITEMSTACK_CRAFT = AspectWriteBuilders.BUILDER_ITEMSTACK.appendKind("refinedstorage")
                .withProperties(CRAFTING_PROPERTIES).handle(
                        input -> {
                            if (!input.getRight().getRawValue().isEmpty()) {
                                DimPos pos = input.getLeft().getTarget().getPos();
                                return TileHelpers.getSafeTile(pos, INetworkNodeProxy.class)
                                        .map(networkNodeProxy -> {
                                            INetwork networkMaster = networkNodeProxy.getNode().getNetwork();
                                            if (networkMaster != null) {
                                                ItemStack itemStack = input.getRight().getRawValue();
                                                return triggerItemStackCrafting(input.getMiddle(), networkMaster, itemStack);
                                            }
                                            return null;
                                        })
                                        .orElse(null);
                            }
                            return null;
                        }, "craft").buildWrite();

        public static final IAspectWrite<ValueTypeList.ValueList, ValueTypeList>
                LIST_CRAFT = AspectWriteBuilders.BUILDER_LIST.appendKind("refinedstorage")
                .withProperties(CRAFTING_PROPERTIES).handle(
                        (IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueTypeList.ValueList>, Void>) input -> {
                            DimPos pos = input.getLeft().getTarget().getPos();
                            TileHelpers.getSafeTile(pos, INetworkNodeProxy.class).ifPresent(networkNodeProxy -> {
                                INetwork networkMaster = networkNodeProxy.getNode().getNetwork();
                                if (networkMaster != null) {
                                    if (input.getRight().getRawValue().getValueType() == ValueTypes.OBJECT_ITEMSTACK) {
                                        for (IValue value : (Iterable<IValue>) input.getRight().getRawValue()) {
                                            ValueObjectTypeItemStack.ValueItemStack valueItemStack = (ValueObjectTypeItemStack.ValueItemStack) value;
                                            if (!valueItemStack.getRawValue().isEmpty()) {
                                                ItemStack itemStack = valueItemStack.getRawValue();
                                                triggerItemStackCrafting(input.getMiddle(), networkMaster, itemStack);
                                            }
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
                                TileHelpers.getSafeTile(pos, INetworkNodeProxy.class).ifPresent(networkNodeProxy -> {
                                    INetwork networkMaster = networkNodeProxy.getNode().getNetwork();
                                    if (networkMaster != null) {
                                        List<ICraftingTask> craftingTasks = Lists.newArrayList(networkMaster.getCraftingManager().getTasks());
                                        for (ICraftingTask craftingTask : craftingTasks) {
                                            networkMaster.getCraftingManager().cancel(craftingTask.getId());
                                        }
                                    }
                                });
                            }
                            return null;
                        }, "cancelcraft").buildWrite();

        public static final IAspectWrite<ValueObjectTypeItemStack.ValueItemStack, ValueObjectTypeItemStack>
                ITEMSTACK_CANCELCRAFT = AspectWriteBuilders.BUILDER_ITEMSTACK.appendKind("refinedstorage")
                .handle(
                        new IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueObjectTypeItemStack.ValueItemStack>, Void>() {
                            @Override
                            public Void getOutput(Triple<PartTarget, IAspectProperties, ValueObjectTypeItemStack.ValueItemStack> input) {
                                if (!input.getRight().getRawValue().isEmpty()) {
                                    DimPos pos = input.getLeft().getTarget().getPos();
                                    TileHelpers.getSafeTile(pos, INetworkNodeProxy.class).ifPresent(networkNodeProxy -> {
                                        INetwork networkMaster = networkNodeProxy.getNode().getNetwork();
                                        if (networkMaster != null) {
                                            ItemStack itemStack = input.getRight().getRawValue();
                                            List<ICraftingTask> craftingTasks = Lists.newArrayList(networkMaster.getCraftingManager().getTasks());
                                            int compareFlags = IComparer.COMPARE_NBT;
                                            for (ICraftingTask craftingTask : craftingTasks) {
                                                for (ItemStack output : craftingTask.getPattern().getOutputs()) {
                                                    if (RS.getComparer().isEqual(output, itemStack, compareFlags)) {
                                                        networkMaster.getCraftingManager().cancel(craftingTask.getId());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                                return null;
                            }
                        }, "cancelcraft").buildWrite();

        public static final IAspectWrite<ValueTypeList.ValueList, ValueTypeList>
                LIST_CANCELCRAFT = AspectWriteBuilders.BUILDER_LIST.appendKind("refinedstorage")
                .handle(
                        new IAspectValuePropagator<Triple<PartTarget, IAspectProperties, ValueTypeList.ValueList>, Void>() {
                            @Override
                            public Void getOutput(Triple<PartTarget, IAspectProperties, ValueTypeList.ValueList> input) {
                                DimPos pos = input.getLeft().getTarget().getPos();
                                TileHelpers.getSafeTile(pos, INetworkNodeProxy.class).ifPresent(networkNodeProxy -> {
                                    INetwork networkMaster = networkNodeProxy.getNode().getNetwork();
                                    if (networkMaster != null) {
                                        if (input.getRight().getRawValue().getValueType() == ValueTypes.OBJECT_ITEMSTACK) {
                                            List<ICraftingTask> craftingTasks = Lists.newArrayList(networkMaster.getCraftingManager().getTasks());
                                            int compareFlags = IComparer.COMPARE_NBT;
                                            for (ICraftingTask craftingTask : craftingTasks) {
                                                for (ItemStack output : craftingTask.getPattern().getOutputs()) {
                                                    for (IValue value : (Iterable<IValue>) input.getRight().getRawValue()) {
                                                        ValueObjectTypeItemStack.ValueItemStack valueItemStack = (ValueObjectTypeItemStack.ValueItemStack) value;
                                                        if (!valueItemStack.getRawValue().isEmpty() &&
                                                                RS.getComparer().isEqual(output, valueItemStack.getRawValue(), compareFlags)) {
                                                            networkMaster.getCraftingManager().cancel(craftingTask.getId());
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                                return null;
                            }
                        }, "cancelcraft").buildWrite();

    }

}
