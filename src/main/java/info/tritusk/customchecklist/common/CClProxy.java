package info.tritusk.customchecklist.common;

import org.apache.logging.log4j.Logger;

import info.tritusk.customchecklist.common.config.ConfigMain;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CClProxy {
	
	public static Logger log;
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		ConfigMain.initConfig(event.getSuggestedConfigurationFile());
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
