package org.cyclops.integrateddynamicscompat.modcompat.common;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.integrateddynamics.client.gui.container.ContainerScreenLogicProgrammerBase;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.IntegratedDynamicsCompat;
import org.cyclops.integrateddynamicscompat.network.packet.CPacketSetSlot;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

/**
 * @author rubensworks
 */
public class JeiReiHelpers {

    public static ResourceLocation itemsToTag(List<Item> items) {
        return BuiltInRegistries.ITEM.listTagIds()
                .map(tag -> BuiltInRegistries.ITEM.get(tag)
                        .flatMap(t -> {
                            if (t.stream().map(Holder::value).toList().equals(items)) {
                                return Optional.of(tag.location());
                            }
                            return Optional.empty();
                        }))
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
        String seconds = new DecimalFormat("#.##").format((double) durationTicks / IModHelpers.get().getMinecraftHelpers().getSecondInTicks());
        return Component.translatable("gui.integrateddynamics.jei.category.time.seconds", seconds);
    }

    public static MutableComponent getEnergyTextComponent(int durationTicks, int energyPerTick) {
        return Component.literal(String.format("%,d", durationTicks * energyPerTick))
                .append(Component.translatable(L10NValues.GENERAL_ENERGY_UNIT));
    }

}
