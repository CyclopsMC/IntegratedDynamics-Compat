package org.cyclops.integrateddynamicscompat.modcompat.jade;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.cyclops.integrateddynamics.api.part.IPartState;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.core.helper.PartHelpers;
import org.cyclops.integrateddynamicscompat.Reference;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

/**
 * Waila data provider for parts.
 * @author rubensworks
 *
 */
public class PartDataProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "part");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(config.get(PartDataProvider.ID)) {
            tooltip.addAll(NBTClassType.getClassType(List.class).readPersistedField("tooltip", accessor.getServerData()));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, ServerPlayer player, Level world, BlockEntity blockEntity, boolean showDetails) {
        PartHelpers.getPartContainer(world, blockEntity.getBlockPos(), null)
                .ifPresent(partContainer -> {
                    Direction side = partContainer.getWatchingSide(world, blockEntity.getBlockPos(), player);
                    if (side != null && partContainer.hasPart(side)) {
                        IPartType partType = partContainer.getPart(side);
                        IPartState partState = partContainer.getPartState(side);
                        List<Component> tooltip = Lists.newArrayList();
                        partType.loadTooltip(partState, tooltip);
                        NBTClassType.getClassType(List.class).writePersistedField("tooltip", tooltip, tag);
                    }
                });
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
