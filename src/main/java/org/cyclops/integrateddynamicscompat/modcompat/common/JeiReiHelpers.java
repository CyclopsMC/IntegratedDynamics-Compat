package org.cyclops.integrateddynamicscompat.modcompat.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rubensworks
 */
public class JeiReiHelpers {

    public static ResourceLocation itemsToTag(List<Item> items) {
        return ForgeRegistries.ITEMS.tags().stream()
                .map(tag -> {
                    if (tag.stream().collect(Collectors.toList()).equals(items)) {
                        return Optional.of(tag.getKey().location());
                    }
                    return Optional.<ResourceLocation>empty();
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(null);
    }

    public static void setStackInSlot(ContainerScreenLogicProgrammerBase<?> screen, int slot, ItemStack itemStack) {
        ContainerLogicProgrammerBase container = screen.getMenu();
        int slotPositionsCount = container.slots.size() - 36 - 4; /* subtract player inv, and 4 fixed slots in LP */
        int slotId = container.slots.size() - 36 - slotPositionsCount + slot;
        container.setItem(slotId, 0, itemStack.copy());
        IntegratedDynamicsCompat._instance.getPacketHandler().sendToServer(
                new CPacketSetSlot(container.containerId, slotId, itemStack));
    }

    public static MutableComponent getDurationSecondsTextComponent(int durationTicks) {
        String seconds = new DecimalFormat("#.##").format((double) durationTicks / MinecraftHelpers.SECOND_IN_TICKS);
        return Component.translatable("gui.integrateddynamics.jei.category.time.seconds", seconds);
    }

    public static MutableComponent getEnergyTextComponent(int durationTicks, int energyPerTick) {
        return Component.literal(String.format("%,d", durationTicks * energyPerTick))
                .append(Component.translatable(L10NValues.GENERAL_ENERGY_UNIT));
    }

}
