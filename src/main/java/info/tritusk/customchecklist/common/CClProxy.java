package info.tritusk.customchecklist.common;

import java.io.File;

import org.apache.logging.log4j.Logger;

import info.tritusk.customchecklist.common.config.ConfigMain;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CClProxy {
	
	public static Logger log;
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		File mainDir = event.getModConfigurationDirectory();
		ConfigMain.initConfig(new File(mainDir, "CustomChecklist.cfg"));
		ConfigMain.initGlobalTasks(new File(mainDir, "GlobalTasks.xml"));
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
