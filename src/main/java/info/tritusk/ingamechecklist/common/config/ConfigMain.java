package info.tritusk.ingamechecklist.common.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.task.TaskEntryLoader;
import net.minecraftforge.common.config.Configuration;

public final class ConfigMain {
	
	public static File globalTasks;
	
	public static void initConfig(File file) {
		Configuration config = new Configuration(file);
		
		config.load();
		
		String urlString = config.getString("RemoteTaskList", "Remote", "", "Fill this with a url to let InGameChecklist download task file from other server.");
		
		if (urlString != null && !urlString.equals("")) {
			new Thread(() -> {
				try {
					URL url = new URL(urlString);
					InputStream rawFile = url.openConnection().getInputStream();
					if (!TaskEntryLoader.xmlHandlerInitialized)
						TaskEntryLoader.initXMLHandler();
					Document xmlDoc = TaskEntryLoader.reader.parse(rawFile);
					xmlDoc.normalize();
				} catch (MalformedURLException e) {
					IClProxy.log.error("The input url is badly formatted!");
					e.printStackTrace();
				} catch (IOException e) {
					IClProxy.log.error("The input url is either invalid or the remote server has issue!");
					e.printStackTrace();
				} catch (SAXException e) {
					IClProxy.log.error("Fail to parse XML file, please check if the file is broken");
					e.printStackTrace();
				}
			}).start();
		}
		
		if (config.hasChanged())
			config.save();
	}
	
	public static void initGlobalTasks(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		globalTasks = file;
	}

}
