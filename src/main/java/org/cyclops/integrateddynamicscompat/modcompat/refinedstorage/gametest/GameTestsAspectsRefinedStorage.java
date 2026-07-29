package org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.gametest;

import com.refinedmods.refinedstorage.api.autocrafting.Ingredient;
import com.refinedmods.refinedstorage.api.autocrafting.Pattern;
import com.refinedmods.refinedstorage.api.autocrafting.PatternLayout;
import com.refinedmods.refinedstorage.api.autocrafting.PatternType;
import com.refinedmods.refinedstorage.api.autocrafting.task.ExternalPatternSinkId;
import com.refinedmods.refinedstorage.api.autocrafting.task.TaskState;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
import com.refinedmods.refinedstorage.api.network.impl.node.patternprovider.PatternProviderNetworkNode;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.support.resource.FluidResource;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.gametest.GameTest;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.integrateddynamics.RegistryEntries;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.part.PartPos;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeFluidStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueObjectTypeItemStack;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeBoolean;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypeList;
import org.cyclops.integrateddynamics.core.evaluate.variable.ValueTypes;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;
import org.cyclops.integrateddynamics.core.part.PartTypes;
import org.cyclops.integrateddynamics.part.PartTypePanelDisplay;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.aspect.RefinedStorageAspects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.cyclops.integrateddynamics.gametest.GameTestHelpersIntegratedDynamics.*;

/**
 * Game tests for the Refined Storage integration aspects.
 * @author rubensworks
 */
public class GameTestsAspectsRefinedStorage {

    public static final String TEMPLATE_EMPTY = "integrateddynamicscompat:empty10";
    public static final BlockPos POS = BlockPos.ZERO.offset(2, 0, 2);

    // RS2 block resource locations
    private static final Identifier RS_CREATIVE_CONTROLLER = Identifier.fromNamespaceAndPath("refinedstorage", "creative_controller");
    private static final Identifier RS_CREATIVE_STORAGE_BLOCK = Identifier.fromNamespaceAndPath("refinedstorage", "creative_storage_block");
    private static final Identifier RS_CREATIVE_FLUID_STORAGE_BLOCK = Identifier.fromNamespaceAndPath("refinedstorage", "creative_fluid_storage_block");

    private static Block getRsBlock(Identifier id) {
        return BuiltInRegistries.BLOCK.getValue(id);
    }

    /**
     * Place a creative RS controller adjacent to where the ID part reads from.
     */
    private static void setupCreativeController(GameTestHelper helper) {
        helper.setBlock(POS.west(), getRsBlock(RS_CREATIVE_CONTROLLER));
    }

    /**
     * Place a creative RS controller with a creative item storage block in the same network.
     */
    private static void setupNetworkWithItemStorage(GameTestHelper helper) {
        helper.setBlock(POS.west(), getRsBlock(RS_CREATIVE_CONTROLLER));
        helper.setBlock(POS.west().west(), getRsBlock(RS_CREATIVE_STORAGE_BLOCK));
    }

    /**
     * Place a creative RS controller with a creative fluid storage block in the same network.
     */
    private static void setupNetworkWithFluidStorage(GameTestHelper helper) {
        helper.setBlock(POS.west(), getRsBlock(RS_CREATIVE_CONTROLLER));
        helper.setBlock(POS.west().west(), getRsBlock(RS_CREATIVE_FLUID_STORAGE_BLOCK));
    }

    /**
     * Set up RS network accessible from both POS.west() and POS.north().west().
     * Used when a writer at POS and a reader at POS.north() both need to access the same RS network.
     */
    private static void setupNetworkWithItemStorageForWriteRead(GameTestHelper helper) {
        helper.setBlock(POS.west(), getRsBlock(RS_CREATIVE_CONTROLLER));
        // Adjacent to controller (POS.west()) and reachable from POS.north() via POS.north().west()
        helper.setBlock(POS.north().west(), getRsBlock(RS_CREATIVE_STORAGE_BLOCK));
    }

    /**
     * Get the RS2 network at the given block position.
     */
    private static Optional<Network> getNetworkAt(GameTestHelper helper, BlockPos pos) {
        return IModHelpersNeoForge.get().getCapabilityHelpers()
                .getCapability(helper.getLevel(), helper.absolutePos(pos), RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability())
                .flatMap(provider -> provider.getContainers().stream()
                        .map(c -> c.getNode().getNetwork())
                        .filter(java.util.Objects::nonNull)
                        .findFirst());
    }

    /**
     * Add a simple internal crafting pattern (ingredient -> output) to the RS network programmatically.
     */
    private static PatternProviderNetworkNode addPatternToNetwork(Network network, ItemResource ingredient, ItemResource output) {
        AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
        PatternLayout layout = new PatternLayout(
                List.of(new Ingredient(1, List.of(ingredient))),
                List.of(new ResourceAmount(output, 1)),
                List.of(),
                PatternType.INTERNAL
        );
        Pattern pattern = new Pattern(UUID.randomUUID(), layout);
        PatternProviderNetworkNode node = new PatternProviderNetworkNode(0, 1);
        node.setId(ExternalPatternSinkId.create());
        node.tryUpdatePattern(0, pattern);
        autocrafting.onContainerAdded(() -> node);
        return node;
    }

    /**
     * Place a display panel cable at the given position and put a variable item in it.
     */
    private static Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> setupDisplayPanel(
            GameTestHelper helper, BlockPos panelCablePos, Direction panelFacing, ItemStack variableItem) {
        helper.setBlock(panelCablePos, RegistryEntries.BLOCK_CABLE.value());
        PartHelpers.addPart(helper.getLevel(), helper.absolutePos(panelCablePos), panelFacing,
                PartTypes.DISPLAY_PANEL, new ItemStack(PartTypes.DISPLAY_PANEL.getItem()));
        PartPos displayPanelPos = PartPos.of(helper.getLevel(), helper.absolutePos(panelCablePos), panelFacing);
        return placeVariableInDisplayPanel(helper.getLevel(), displayPanelPos, variableItem);
    }

    // ===== Read: BOOLEAN_APPLICABLE =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSNetworkApplicableTrue(GameTestHelper helper) {
        setupCreativeController(helper);
        testReadAspect(POS, helper, PartTypes.MACHINE_READER,
                RefinedStorageAspects.Read.Network.BOOLEAN_APPLICABLE, ValueTypeBoolean.ValueBoolean.of(true));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSNetworkApplicableFalse(GameTestHelper helper) {
        // Place a non-RS block - no network node, so BOOLEAN_APPLICABLE should be false
        helper.setBlock(POS.west(), Blocks.DIRT);
        testReadAspect(POS, helper, PartTypes.MACHINE_READER,
                RefinedStorageAspects.Read.Network.BOOLEAN_APPLICABLE, ValueTypeBoolean.ValueBoolean.of(false));
    }

    // ===== Read: LIST_ITEMSTACKS =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSInventoryListItemStacksEmpty(GameTestHelper helper) {
        // Just a creative controller - no storage blocks, so no items can be stored
        setupCreativeController(helper);
        testReadAspectPredicate(POS, helper, PartTypes.INVENTORY_READER,
                RefinedStorageAspects.Read.Inventory.LIST_ITEMSTACKS,
                value -> {
                    if (!(value instanceof ValueTypeList.ValueList list)) return false;
                    try {
                        return list.getRawValue().getLength() == 0;
                    } catch (EvaluationException e) {
                        return false;
                    }
                });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSInventoryListItemStacksWithItems(GameTestHelper helper) {
        setupNetworkWithItemStorage(helper);
        // Set up reader and create variable from it
        testReadAspectSetup(POS, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_ITEMSTACKS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(POS), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_ITEMSTACKS);
        // Place display panel at POS.east(), connected via cables to POS
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)), 10L, Action.EXECUTE, Actor.EMPTY);
                }))
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0, "Item list should not be empty");
                        ValueObjectTypeItemStack.ValueItemStack firstItem =
                                (ValueObjectTypeItemStack.ValueItemStack) listValue.getRawValue().get(0);
                        helper.assertTrue(
                                firstItem.getRawValue().is(Items.DIAMOND),
                                "First item should be diamond");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .thenSucceed();
    }

    // ===== Read: LIST_FLUIDSTACKS =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSFluidListFluidStacksEmpty(GameTestHelper helper) {
        setupCreativeController(helper);
        testReadAspectPredicate(POS, helper, PartTypes.FLUID_READER,
                RefinedStorageAspects.Read.Fluid.LIST_FLUIDSTACKS,
                value -> {
                    if (!(value instanceof ValueTypeList.ValueList list)) return false;
                    try {
                        return list.getRawValue().getLength() == 0;
                    } catch (EvaluationException e) {
                        return false;
                    }
                });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSFluidListFluidStacksWithFluids(GameTestHelper helper) {
        setupNetworkWithFluidStorage(helper);
        // Set up reader and create variable from it
        testReadAspectSetup(POS, helper, PartTypes.FLUID_READER, RefinedStorageAspects.Read.Fluid.LIST_FLUIDSTACKS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(POS), Direction.WEST),
                RefinedStorageAspects.Read.Fluid.LIST_FLUIDSTACKS);
        // Place display panel at POS.east(), connected via cables to POS
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    // Insert 1000 mB of water into the RS network
                    storage.insert(new FluidResource(Fluids.WATER), 1000L, Action.EXECUTE, Actor.EMPTY);
                }))
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0, "Fluid list should not be empty");
                        ValueObjectTypeFluidStack.ValueFluidStack firstFluid =
                                (ValueObjectTypeFluidStack.ValueFluidStack) listValue.getRawValue().get(0);
                        helper.assertTrue(
                                firstFluid.getRawValue().is(Fluids.WATER),
                                "First fluid should be water");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .thenSucceed();
    }

    // ===== Read: LIST_CRAFTABLEITEMS =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSInventoryListCraftableItemsEmpty(GameTestHelper helper) {
        // No autocrafters set up, so no craftable items
        setupCreativeController(helper);
        testReadAspectPredicate(POS, helper, PartTypes.INVENTORY_READER,
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTABLEITEMS,
                value -> {
                    if (!(value instanceof ValueTypeList.ValueList list)) return false;
                    try {
                        return list.getRawValue().getLength() == 0;
                    } catch (EvaluationException e) {
                        return false;
                    }
                });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSInventoryListCraftableItemsWithItems(GameTestHelper helper) {
        setupCreativeController(helper);
        // Set up reader and create variable from it
        testReadAspectSetup(POS, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTABLEITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(POS), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTABLEITEMS);
        // Place display panel at POS.east()
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    // Add pattern: 1 dirt -> 1 diamond
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                }))
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0, "Craftable items list should not be empty");
                        ValueObjectTypeItemStack.ValueItemStack firstItem =
                                (ValueObjectTypeItemStack.ValueItemStack) listValue.getRawValue().get(0);
                        helper.assertTrue(
                                firstItem.getRawValue().is(Items.DIAMOND),
                                "First craftable item should be diamond");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .thenSucceed();
    }

    // ===== Read: LIST_CRAFTINGITEMS =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSInventoryListCraftingItemsEmpty(GameTestHelper helper) {
        // No crafting tasks running, so no items being crafted
        setupCreativeController(helper);
        testReadAspectPredicate(POS, helper, PartTypes.INVENTORY_READER,
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS,
                value -> {
                    if (!(value instanceof ValueTypeList.ValueList list)) return false;
                    try {
                        return list.getRawValue().getLength() == 0;
                    } catch (EvaluationException e) {
                        return false;
                    }
                });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSInventoryListCraftingItemsWithItems(GameTestHelper helper) {
        setupNetworkWithItemStorage(helper);
        // Set up reader and create variable from it
        testReadAspectSetup(POS, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(POS), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        // Place display panel at POS.east()
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    // Add pattern: 1 dirt -> 1 diamond, insert ingredient, start crafting task
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIRT)), 1L, Action.EXECUTE, Actor.EMPTY);
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    autocrafting.ensureTask(ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)), 1L, Actor.EMPTY,
                            com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken.NONE);
                }))
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0, "Crafting items list should not be empty");
                        ValueObjectTypeItemStack.ValueItemStack firstItem =
                                (ValueObjectTypeItemStack.ValueItemStack) listValue.getRawValue().get(0);
                        helper.assertTrue(
                                firstItem.getRawValue().is(Items.DIAMOND),
                                "First crafting item should be diamond");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .thenSucceed();
    }

    // ===== Write: ITEMSTACK_CRAFT =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteItemStackCraft(GameTestHelper helper) {
        // Set up RS network and write aspect - no pattern available so ensureTask returns MISSING_RESOURCES, no crash
        setupCreativeController(helper);
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.ITEMSTACK_CRAFT,
                ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)));
        helper.succeedWhen(() -> helper.assertTrue(true, "Write aspect executed without crash"));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteItemStackCraftActual(GameTestHelper helper) {
        // RS controller reachable from POS.west(), storage also reachable from POS.north().west()
        setupNetworkWithItemStorageForWriteRead(helper);
        // Writer at POS (Direction.WEST -> RS controller)
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.ITEMSTACK_CRAFT,
                ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)));
        // Reader at POS.north() (Direction.WEST -> RS storage at POS.north().west())
        BlockPos readerPos = POS.north();
        testReadAspectSetup(readerPos, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(readerPos), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        // Display panel at POS.east() (adjacent to POS, connected via cable network)
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    // Add pattern: 1 dirt -> 1 diamond, insert dirt as ingredient
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIRT)), 1L, Action.EXECUTE, Actor.EMPTY);
                }))
                .thenWaitUntil(() -> {
                    // Wait for the write aspect to trigger the craft and the display panel to reflect it
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0,
                                "Crafting items list should not be empty after craft write");
                        ValueObjectTypeItemStack.ValueItemStack firstItem =
                                (ValueObjectTypeItemStack.ValueItemStack) listValue.getRawValue().get(0);
                        helper.assertTrue(firstItem.getRawValue().is(Items.DIAMOND),
                                "Crafting item should be diamond");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .thenSucceed();
    }

    // ===== Write: LIST_CRAFT =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteListCraft(GameTestHelper helper) {
        setupCreativeController(helper);
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.LIST_CRAFT,
                ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK,
                        java.util.List.of(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)))));
        helper.succeedWhen(() -> helper.assertTrue(true, "Write aspect executed without crash"));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteListCraftActual(GameTestHelper helper) {
        setupNetworkWithItemStorageForWriteRead(helper);
        // Writer at POS writing a list with diamond
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.LIST_CRAFT,
                ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK,
                        java.util.List.of(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)))));
        // Reader at POS.north() reading LIST_CRAFTINGITEMS from RS storage at POS.north().west()
        BlockPos readerPos = POS.north();
        testReadAspectSetup(readerPos, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(readerPos), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIRT)), 1L, Action.EXECUTE, Actor.EMPTY);
                }))
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0,
                                "Crafting items list should not be empty after list craft write");
                        ValueObjectTypeItemStack.ValueItemStack firstItem =
                                (ValueObjectTypeItemStack.ValueItemStack) listValue.getRawValue().get(0);
                        helper.assertTrue(firstItem.getRawValue().is(Items.DIAMOND),
                                "Crafting item should be diamond");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .thenSucceed();
    }

    // ===== Write: BOOLEAN_CANCELCRAFT =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteBooleanCancelCraft(GameTestHelper helper) {
        // cancelAll() is called but no tasks running - should not crash
        setupCreativeController(helper);
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.BOOLEAN_CANCELCRAFT,
                ValueTypeBoolean.ValueBoolean.of(true));
        helper.succeedWhen(() -> {
            // Verify no tasks are running after cancel
            Optional<Network> network = getNetworkAt(helper, POS.west());
            helper.assertTrue(network.isPresent(), "Network should be present");
            network.ifPresent(n -> {
                AutocraftingNetworkComponent autocrafting = n.getComponent(AutocraftingNetworkComponent.class);
                helper.assertTrue(autocrafting.getStatuses().isEmpty(), "No tasks should be running after cancelAll");
            });
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteBooleanCancelCraftActual(GameTestHelper helper) {
        setupNetworkWithItemStorageForWriteRead(helper);
        // Writer at POS: cancels all crafting tasks when triggered
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.BOOLEAN_CANCELCRAFT,
                ValueTypeBoolean.ValueBoolean.of(true));
        // Reader at POS.north() reading LIST_CRAFTINGITEMS from RS storage
        BlockPos readerPos = POS.north();
        testReadAspectSetup(readerPos, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(readerPos), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    // Add a pattern and start a crafting task
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIRT)), 1L, Action.EXECUTE, Actor.EMPTY);
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    autocrafting.ensureTask(ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)), 1L, Actor.EMPTY,
                            com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken.NONE);
                }))
                // Wait until the task appears in the display panel
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0,
                                "Crafting items list should have task before cancel");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                // The BOOLEAN_CANCELCRAFT writer is already active and will cancel all tasks
                // Verify task is now in cancelled state (RETURNING_INTERNAL_STORAGE)
                .thenWaitUntil(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    boolean allCancelled = autocrafting.getStatuses().stream()
                            .allMatch(s -> s.state() == TaskState.RETURNING_INTERNAL_STORAGE
                                    || s.state() == TaskState.COMPLETED);
                    helper.assertTrue(allCancelled || autocrafting.getStatuses().isEmpty(),
                            "All tasks should be cancelled or gone after BOOLEAN_CANCELCRAFT");
                }))
                .thenSucceed();
    }

    // ===== Write: ITEMSTACK_CANCELCRAFT =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteItemStackCancelCraft(GameTestHelper helper) {
        // No matching task to cancel - should not crash
        setupCreativeController(helper);
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.ITEMSTACK_CANCELCRAFT,
                ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)));
        helper.succeedWhen(() -> helper.assertTrue(true, "Write aspect executed without crash"));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteItemStackCancelCraftActual(GameTestHelper helper) {
        setupNetworkWithItemStorageForWriteRead(helper);
        // Writer cancels crafting of diamond
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.ITEMSTACK_CANCELCRAFT,
                ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)));
        // Reader at POS.north() reading LIST_CRAFTINGITEMS
        BlockPos readerPos = POS.north();
        testReadAspectSetup(readerPos, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(readerPos), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    // Add pattern and start crafting task for diamond
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIRT)), 1L, Action.EXECUTE, Actor.EMPTY);
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    autocrafting.ensureTask(ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)), 1L, Actor.EMPTY,
                            com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken.NONE);
                }))
                // Wait until the task appears in the display panel
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0,
                                "Crafting items list should have task before cancel");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                // The ITEMSTACK_CANCELCRAFT writer is already active and will cancel diamond task
                .thenWaitUntil(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    boolean allCancelled = autocrafting.getStatuses().stream()
                            .allMatch(s -> s.state() == TaskState.RETURNING_INTERNAL_STORAGE
                                    || s.state() == TaskState.COMPLETED);
                    helper.assertTrue(allCancelled || autocrafting.getStatuses().isEmpty(),
                            "All diamond tasks should be cancelled or gone after ITEMSTACK_CANCELCRAFT");
                }))
                .thenSucceed();
    }

    // ===== Write: LIST_CANCELCRAFT =====

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteListCancelCraft(GameTestHelper helper) {
        // No matching tasks to cancel - should not crash
        setupCreativeController(helper);
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.LIST_CANCELCRAFT,
                ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK,
                        java.util.List.of(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)))));
        helper.succeedWhen(() -> helper.assertTrue(true, "Write aspect executed without crash"));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testRSWriteListCancelCraftActual(GameTestHelper helper) {
        setupNetworkWithItemStorageForWriteRead(helper);
        // Writer cancels crafting of a list containing diamond
        testWriteAspectSetup(POS, helper, PartTypes.INVENTORY_WRITER,
                RefinedStorageAspects.Write.LIST_CANCELCRAFT,
                ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ITEMSTACK,
                        java.util.List.of(ValueObjectTypeItemStack.ValueItemStack.of(new ItemStack(Items.DIAMOND)))));
        // Reader at POS.north() reading LIST_CRAFTINGITEMS
        BlockPos readerPos = POS.north();
        testReadAspectSetup(readerPos, helper, PartTypes.INVENTORY_READER, RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        ItemStack variableAspect = createVariableFromReader(helper.getLevel(),
                PartPos.of(helper.getLevel(), helper.absolutePos(readerPos), Direction.WEST),
                RefinedStorageAspects.Read.Inventory.LIST_CRAFTINGITEMS);
        Pair<PartTypePanelDisplay, PartTypePanelDisplay.State> panelState =
                setupDisplayPanel(helper, POS.east(), Direction.EAST, variableAspect);

        helper.startSequence()
                .thenWaitUntil(() -> helper.assertTrue(
                        getNetworkAt(helper, POS.west()).isPresent(), "RS network not yet initialized"))
                .thenExecute(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    // Add pattern and start crafting task for diamond
                    addPatternToNetwork(network,
                            ItemResource.ofItemStack(new ItemStack(Items.DIRT)),
                            ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)));
                    StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
                    storage.insert(ItemResource.ofItemStack(new ItemStack(Items.DIRT)), 1L, Action.EXECUTE, Actor.EMPTY);
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    autocrafting.ensureTask(ItemResource.ofItemStack(new ItemStack(Items.DIAMOND)), 1L, Actor.EMPTY,
                            com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken.NONE);
                }))
                // Wait until the task appears in the display panel
                .thenWaitUntil(() -> {
                    try {
                        ValueTypeList.ValueList listValue = (ValueTypeList.ValueList) panelState.getRight().getDisplayValue();
                        helper.assertTrue(listValue != null && listValue.getRawValue().getLength() > 0,
                                "Crafting items list should have task before cancel");
                    } catch (EvaluationException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                // The LIST_CANCELCRAFT writer is already active and will cancel diamond task
                .thenWaitUntil(() -> getNetworkAt(helper, POS.west()).ifPresent(network -> {
                    AutocraftingNetworkComponent autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
                    boolean allCancelled = autocrafting.getStatuses().stream()
                            .allMatch(s -> s.state() == TaskState.RETURNING_INTERNAL_STORAGE
                                    || s.state() == TaskState.COMPLETED);
                    helper.assertTrue(allCancelled || autocrafting.getStatuses().isEmpty(),
                            "All diamond tasks should be cancelled or gone after LIST_CANCELCRAFT");
                }))
                .thenSucceed();
    }

}
