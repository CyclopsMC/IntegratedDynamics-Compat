package org.cyclops.integrateddynamicscompat.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.integrateddynamics.core.ingredient.ItemMatchProperties;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeRecipeLPElement;
import org.cyclops.integrateddynamics.inventory.container.ContainerLogicProgrammerBase;
import org.cyclops.integrateddynamicscompat.Reference;

import java.util.List;

/**
 * @author rubensworks
 */
public class CPacketValueTypeRecipeLPElementSetRecipe extends PacketCodec<CPacketValueTypeRecipeLPElementSetRecipe> {

    public static final Type<CPacketValueTypeRecipeLPElementSetRecipe> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "value_type_recipe_lp_element_set_recipe"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CPacketValueTypeRecipeLPElementSetRecipe> CODEC = getCodec(CPacketValueTypeRecipeLPElementSetRecipe::new);

    @CodecField
    private int windowId;
    @CodecField
    private List<ItemMatchProperties> itemInputs;
    @CodecField
    private List<FluidStack> fluidInputs;
    @CodecField
    private List<ItemStack> itemOutputs;
    @CodecField
    private List<FluidStack> fluidOutputs;

    public CPacketValueTypeRecipeLPElementSetRecipe() {
        super(ID);
    }

    public CPacketValueTypeRecipeLPElementSetRecipe(int windowId, List<ItemMatchProperties> itemInputs, List<FluidStack> fluidInputs,
                                                    List<ItemStack> itemOutputs, List<FluidStack> fluidOutputs) {
        super(ID);
        this.windowId = windowId;
        this.itemInputs = itemInputs;
        this.fluidInputs = fluidInputs;
        this.itemOutputs = itemOutputs;
        this.fluidOutputs = fluidOutputs;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level world, Player player) {

    }

    @Override
    public void actionServer(Level world, ServerPlayer player) {
        if(player.containerMenu.containerId == windowId) {
            ContainerLogicProgrammerBase container = (ContainerLogicProgrammerBase) player.containerMenu;
            ValueTypeRecipeLPElement element = (ValueTypeRecipeLPElement) container.getActiveElement();
            element.setRecipeGrid(container, itemInputs, fluidInputs, itemOutputs, fluidOutputs);
        }
    }

}
