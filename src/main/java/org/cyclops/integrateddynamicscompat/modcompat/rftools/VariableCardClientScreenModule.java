package org.cyclops.integrateddynamicscompat.modcompat.rftools;

import mcjty.rftools.api.screens.FormatStyle;
import mcjty.rftools.api.screens.IClientScreenModule;
import mcjty.rftools.api.screens.IModuleGuiBuilder;
import mcjty.rftools.api.screens.IModuleRenderHelper;
import mcjty.rftools.api.screens.ModuleRenderInfo;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VariableCardClientScreenModule implements IClientScreenModule<ModuleDataValue> {
    @Override
    public TransformMode getTransformMode() {
        return TransformMode.TEXT;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void render(IModuleRenderHelper renderHelper, FontRenderer fontRenderer, int currenty, ModuleDataValue screenData, ModuleRenderInfo renderInfo) {
        GlStateManager.disableLighting();
        String text;
        if(screenData == null) {
        	text = "screenData is null";
        }else if(screenData.materializedValue == null) {
        	text = screenData.errorMessage;
        } else {
        	text = screenData.valueType.toCompactString(screenData.materializedValue);
        }
        renderHelper.renderText(7, currenty, 0xffffff, renderInfo, renderHelper.format(text, FormatStyle.MODE_FULL));
    }

    @Override
    public void mouseClick(World world, int x, int y, boolean clicked) {
    	// do nothing
    }

    @Override
    public void createGui(IModuleGuiBuilder guiBuilder) {
        guiBuilder.label("TODO").nl();
    }

    @Override
    public void setupFromNBT(NBTTagCompound tagCompound, int dim, BlockPos pos) {
    	// do nothing
    }

    @Override
    public boolean needsServerData() {
        return true;
    }
}
