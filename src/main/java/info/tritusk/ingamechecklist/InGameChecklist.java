package info.tritusk.ingamechecklist;

import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = "ingame_checklist", name = "In-Game Checklist", version = "@IGC_VERSION@", dependencies = "required-after:Forge@[12.18.2.2104,)", useMetadata = true, clientSideOnly = true)
public class InGameChecklist {
	
	@Mod.Instance("ingame_checklist")
	public static InGameChecklist instance;
	
	@SidedProxy(serverSide = "info.tritusk.ingamechecklist.common.IClProxy", clientSide = "info.tritusk.ingamechecklist.client.IClProxyClient")
	public static IClProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@Mod.EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}
	
	@Mod.EventHandler
	public void onServerStop(FMLServerStoppingEvent event) {
		proxy.onServerStopping(event);
	}

}
