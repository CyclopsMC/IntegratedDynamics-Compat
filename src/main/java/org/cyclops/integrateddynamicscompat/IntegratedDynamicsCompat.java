package org.cyclops.integrateddynamicscompat;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.integrateddynamics.tileentity.TileCoalGenerator;
import org.cyclops.integrateddynamics.tileentity.TileDryingBasin;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalDryingBasin;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalSqueezer;
import org.cyclops.integrateddynamics.tileentity.TileSqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerCoalGeneratorTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerDryingBasinTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerMechanicalMachineTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerSqueezerTileCompat;
import org.cyclops.integrateddynamicscompat.proxy.ClientProxy;
import org.cyclops.integrateddynamicscompat.proxy.CommonProxy;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(Reference.MOD_ID)
public class IntegratedDynamicsCompat extends ModBaseVersionable<IntegratedDynamicsCompat> {

    public static IntegratedDynamicsCompat _instance;

    public IntegratedDynamicsCompat() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);
        // TODO: temporarily disable some mod compats
        // Mod compats
//        modCompatLoader.addModCompat(new CharsetPipesModCompat());
//        modCompatLoader.addModCompat(new McMultiPartModCompat());
//        modCompatLoader.addModCompat(new WailaModCompat());
//        modCompatLoader.addModCompat(new ThaumcraftModCompat());
//        modCompatLoader.addModCompat(new TConstructModCompat());
//        modCompatLoader.addModCompat(new ForestryModCompat());
//        modCompatLoader.addModCompat(new Ic2ModCompat());
//        modCompatLoader.addModCompat(new TopModCompat());
//        modCompatLoader.addModCompat(new TeslaApiCompat());
//        modCompatLoader.addModCompat(new RefinedStorageModCompat());
//        modCompatLoader.addModCompat(new ImmersiveEngineeringModCompat());
//        modCompatLoader.addModCompat(new CraftTweakerModCompat());
//        modCompatLoader.addModCompat(new SignalsModCompat());
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        // Capabilities
        getCapabilityConstructorRegistry().registerTile(TileDryingBasin.class, new WorkerDryingBasinTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileSqueezer.class, new WorkerSqueezerTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileCoalGenerator.class, new WorkerCoalGeneratorTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileMechanicalDryingBasin.class, new WorkerMechanicalMachineTileCompat<>());
        getCapabilityConstructorRegistry().registerTile(TileMechanicalSqueezer.class, new WorkerMechanicalMachineTileCompat<>());

        super.setup(event);
    }

    @Override
    protected IClientProxy constructClientProxy() {
        return new ClientProxy();
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return new CommonProxy();
    }

    @Override
    protected ItemGroup constructDefaultItemGroup() {
        return null;
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig());
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        clog(Level.INFO, message);
    }
    
    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        IntegratedDynamicsCompat._instance.getLoggerHelper().log(level, message);
    }
    
}
