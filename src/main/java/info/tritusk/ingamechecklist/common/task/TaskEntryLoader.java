package info.tritusk.ingamechecklist.common.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.config.ConfigMain;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class TaskEntryLoader {
	
	@SideOnly(Side.CLIENT)
	public static volatile List<TaskEntry> localEntryList = new ArrayList<TaskEntry>();
	
	public static volatile List<TaskEntry> remoteEntryList = new ArrayList<TaskEntry>();
	
	public static boolean xmlHandlerInitialized = false;
	
	public static DocumentBuilder reader;
	public static Transformer writer;
	
	public static void initXMLHandler() {
		try {
			reader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			writer = TransformerFactory.newInstance().newTransformer();
			xmlHandlerInitialized = reader != null && writer != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadGlobalChecklist(File source) {
		try {
			Document xmlDoc = reader.parse(source);
			Node mainNode = xmlDoc.getElementsByTagName("LocalChecklist").item(0);
			NodeList checklist = ((Element) mainNode).getElementsByTagName("task");
			for (int n = 0; n < checklist.getLength(); n++) {
				try {
					Node aTask = checklist.item(n);
					String taskName = ((Element)aTask).getAttribute("name");
					String taskDesc = ((Element)aTask).getTextContent();
					localEntryList.add(new TaskEntry(taskName, taskDesc));
				} catch (Exception e) {
					IClProxy.log.error("Error occured when append a new task.");
				}
			}
			
		} catch (Exception e) {
			IClProxy.log.error("Error occured when loading checklist.");
			IClProxy.log.error("Please double check your checklist file, make sure that everything matches standard.");
		}
	}
	
	public static void saveGlobalChecklist(Collection<TaskEntry> tasks) {
		try {
			Document xmlDoc = reader.newDocument();
			Element main = xmlDoc.createElement("LocalChecklist");
			for (TaskEntry task : tasks) {
				Element aTask = xmlDoc.createElement("task");
				aTask.setAttribute("name", task.getName());
				aTask.setTextContent(task.getDescription());
				main.appendChild(aTask);
			}
			xmlDoc.appendChild(main);
			writer.setOutputProperty(OutputKeys.INDENT, "yes");
			writer.setOutputProperty(OutputKeys.METHOD, "xml");
			writer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			ConfigMain.globalTasks.delete();
			ConfigMain.globalTasks.createNewFile();
			writer.transform(new DOMSource(xmlDoc), new StreamResult(new FileOutputStream(ConfigMain.globalTasks)));
			IClProxy.log.info("Successfully saved local checklist.");
		} catch (Exception e) {
			IClProxy.log.error("An error occured when trying to save the local checklist file.");
			e.printStackTrace();
		}
	}
	
	public static void loadRemoteChecklistFromServer() {
		
	}
	
	public static void saveRemoteChecklistToLocal(Collection<TaskEntry> tasks) {
		
	}

}
