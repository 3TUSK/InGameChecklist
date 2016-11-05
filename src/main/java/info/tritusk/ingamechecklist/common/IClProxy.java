package info.tritusk.ingamechecklist.common;

import java.io.File;
import java.io.FileInputStream;

import org.apache.logging.log4j.Logger;

import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.common.command.CommandTask;
import info.tritusk.ingamechecklist.common.task.TaskEntryManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

public class IClProxy {
	
	public static Logger log;

	public static ITaskManager manager = new TaskEntryManager();
	
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		manager.init();
		
		IClInterModManager.INSTANCE.retriveTaskManagerRequest(event.getAsmData());
	}
	
	public void init(FMLInitializationEvent event) {
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		ClientCommandHandler.instance.registerCommand(new CommandTask());
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		try {
			File file = new File(FMLClientHandler.instance().getSavesDirectory(), "InGameChecklist.xml");
			if (!file.exists())
				file.createNewFile();
			manager.loadFrom(new FileInputStream(file));
			log.info("Loaded local checklist.");
		} catch (Exception e) {
			log.error("Something went extremely wrong...");
			e.printStackTrace();
		}
	}
	
	public void onServerStopping(FMLServerStoppingEvent event) {
		//There is no check whether this file exists or not because it should be created on FMLServerStartingEvent is called
		manager.saveTo(new File(FMLClientHandler.instance().getSavesDirectory(), "InGameChecklist.xml"));
	}

}
