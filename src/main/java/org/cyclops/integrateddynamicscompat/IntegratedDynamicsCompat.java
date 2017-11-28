package org.cyclops.integrateddynamicscompat;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.integrateddynamics.core.item.ItemBlockEnergyContainer;
import org.cyclops.integrateddynamics.tileentity.TileCoalGenerator;
import org.cyclops.integrateddynamics.tileentity.TileDryingBasin;
import org.cyclops.integrateddynamics.tileentity.TileEnergyBattery;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalDryingBasin;
import org.cyclops.integrateddynamics.tileentity.TileMechanicalSqueezer;
import org.cyclops.integrateddynamics.tileentity.TileSqueezer;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerCoalGeneratorTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerDryingBasinTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerMechanicalMachineTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.capabilities.WorkerSqueezerTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.forestry.ForestryModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.ic2.Ic2ModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.immersiveengineering.ImmersiveEngineeringModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.jei.JEIModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.minetweaker.CraftTweakerModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.refinedstorage.RefinedStorageModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tconstruct.TConstructModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.TeslaApiCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaConsumerEnergyBatteryTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaConsumerEnergyContainerItemCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaHolderEnergyBatteryTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaHolderEnergyContainerItemCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaProducerCoalGeneratorTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaProducerEnergyBatteryTileCompat;
import org.cyclops.integrateddynamicscompat.modcompat.tesla.capabilities.TeslaProducerEnergyContainerItemCompat;
import org.cyclops.integrateddynamicscompat.modcompat.top.TopModCompat;
import org.cyclops.integrateddynamicscompat.modcompat.waila.WailaModCompat;

/**
 * The main mod class of this mod.
 * @author rubensworks (aka kroeserr)
 *
 */
@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        useMetadata = true,
        version = Reference.MOD_VERSION,
        dependencies = Reference.MOD_DEPENDENCIES,
        guiFactory = "org.cyclops.integrateddynamicscompat.GuiConfigOverview$ExtendedConfigGuiFactory"
)
public class IntegratedDynamicsCompat extends ModBaseVersionable {
    
    /**
     * The proxy of this mod, depending on 'side' a different proxy will be inside this field.
     * @see SidedProxy
     */
    @SidedProxy(clientSide = "org.cyclops.integrateddynamicscompat.proxy.ClientProxy", serverSide = "org.cyclops.integrateddynamicscompat.proxy.CommonProxy")
    public static ICommonProxy proxy;
    
    /**
     * The unique instance of this mod.
     */
    @Instance(value = Reference.MOD_ID)
    public static IntegratedDynamicsCompat _instance;

    public IntegratedDynamicsCompat() {
        super(Reference.MOD_ID, Reference.MOD_NAME, Reference.MOD_VERSION);
    }

    @Override
    protected RecipeHandler constructRecipeHandler() {
        return new RecipeHandler(this);
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);
        // TODO: temporarily disable some mod compats
        // Mod compats
        //modCompatLoader.addModCompat(new CharsetPipesModCompat());
        //modCompatLoader.addModCompat(new McMultiPartModCompat());
        modCompatLoader.addModCompat(new WailaModCompat());
        //modCompatLoader.addModCompat(new ThaumcraftModCompat());
        modCompatLoader.addModCompat(new JEIModCompat());
        modCompatLoader.addModCompat(new TConstructModCompat());
        modCompatLoader.addModCompat(new ForestryModCompat());
        modCompatLoader.addModCompat(new Ic2ModCompat());
        modCompatLoader.addModCompat(new TopModCompat());
        modCompatLoader.addModCompat(new TeslaApiCompat());
        modCompatLoader.addModCompat(new RefinedStorageModCompat());
        modCompatLoader.addModCompat(new ImmersiveEngineeringModCompat());
        modCompatLoader.addModCompat(new CraftTweakerModCompat());
    }

    /**
     * The pre-initialization, will register required configs.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        // Capabilities
        getCapabilityConstructorRegistry().registerTile(TileDryingBasin.class, new WorkerDryingBasinTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileSqueezer.class, new WorkerSqueezerTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileCoalGenerator.class, new WorkerCoalGeneratorTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileMechanicalDryingBasin.class, new WorkerMechanicalMachineTileCompat<>());
        getCapabilityConstructorRegistry().registerTile(TileMechanicalSqueezer.class, new WorkerMechanicalMachineTileCompat<>());
        getCapabilityConstructorRegistry().registerTile(TileCoalGenerator.class, new TeslaProducerCoalGeneratorTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileEnergyBattery.class, new TeslaConsumerEnergyBatteryTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileEnergyBattery.class, new TeslaProducerEnergyBatteryTileCompat());
        getCapabilityConstructorRegistry().registerTile(TileEnergyBattery.class, new TeslaHolderEnergyBatteryTileCompat());
        getCapabilityConstructorRegistry().registerItem(ItemBlockEnergyContainer.class, new TeslaConsumerEnergyContainerItemCompat());
        getCapabilityConstructorRegistry().registerItem(ItemBlockEnergyContainer.class, new TeslaProducerEnergyContainerItemCompat());
        getCapabilityConstructorRegistry().registerItem(ItemBlockEnergyContainer.class, new TeslaHolderEnergyContainerItemCompat());

        super.preInit(event);
    }
    
    /**
     * Register the config dependent things like world generation and proxy handlers.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
    
    /**
     * Register the event hooks.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
    /**
     * Register the things that are related to server starting, like commands.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStarting(FMLServerStartingEvent event) {
        super.onServerStarting(event);
    }

    /**
     * Register the things that are related to server starting.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        super.onServerStarted(event);
    }

    /**
     * Register the things that are related to server stopping, like persistent storage.
     * @param event The Forge event required for this.
     */
    @EventHandler
    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        // Uncomment the following line and specify an item config class to add a creative tab
        // return new ItemCreativeTab(this, new ItemConfigReference(ITEM CONFIG CLASS));
        return null;
    }

    @Override
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {
        configHandler.add(new GeneralConfig());
    }

    @Override
    public ICommonProxy getProxy() {
        return proxy;
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
