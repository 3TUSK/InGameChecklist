package info.tritusk.customchecklist;

import info.tritusk.customchecklist.common.CClProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "CustomChecklist", name = "Custom Checklist", version = "@VERSION@", useMetadata = true)
public class CustomChecklist {
	
	@Instance("CustomChecklist")
	public CustomChecklist instance;
	
	@SidedProxy(serverSide = "info.tritusk.customchecklist.common.CClProxy", clientSide = "info.tritusk.customchecklist.client.CClProxyClient")
	public static CClProxy proxy;
	
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

}
