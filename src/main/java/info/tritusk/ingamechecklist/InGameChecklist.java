package info.tritusk.ingamechecklist;

import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = "InGameChecklist", name = "In-Game Checklist", version = "@VERSION@", useMetadata = true)
public class InGameChecklist {
	
	@Instance("CustomChecklist")
	public InGameChecklist instance;
	
	@SidedProxy(serverSide = "info.tritusk.ingamechecklist.common.IClProxy", clientSide = "info.tritusk.ingamechecklist.client.IClProxyClient")
	public static IClProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.instance = this;
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		proxy.onServerStarting(event);
	}
	
	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event) {
		proxy.onServerStopping(event);
	}

}
