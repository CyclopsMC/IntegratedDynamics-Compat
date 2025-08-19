package org.cyclops.integrateddynamicscompat.modcompat.jade;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;
import org.cyclops.integrateddynamicscompat.Reference;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import java.util.List;

/**
 * Waila data provider for parts.
 * @author rubensworks
 *
 */
public class PartDataProviderServer implements IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "part");

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        BlockEntity blockEntity = accessor.getBlockEntity();
        PartHelpers.getPartContainer(accessor.getLevel(), blockEntity.getBlockPos(), null)
                .ifPresent(partContainer -> {
                    Direction side = partContainer.getWatchingSide(accessor.getLevel(), blockEntity.getBlockPos(), accessor.getPlayer());
                    if (side != null && partContainer.hasPart(side)) {
                        IPartType partType = partContainer.getPart(side);
                        IPartState partState = partContainer.getPartState(side);
                        List<Component> tooltip = Lists.newArrayList();
                        tooltip.add(Component.translatable(partType.getTranslationKey()));
                        partType.loadTooltip(partState, tooltip);
                        JadeIntegratedDynamicsConfig.putTooltip(tag, tooltip);
                    }
                });
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
