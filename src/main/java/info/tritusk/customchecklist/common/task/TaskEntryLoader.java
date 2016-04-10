package info.tritusk.customchecklist.common.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

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

import info.tritusk.customchecklist.common.CClProxy;
import info.tritusk.customchecklist.common.config.ConfigMain;

public final class TaskEntryLoader {
	
	public static Collection<TaskEntry> globalEntryList = new ArrayList<TaskEntry>();
	
	public static Collection<TaskEntry> remoteEntryList = new ArrayList<TaskEntry>();
	
	public static boolean xmlHandlerInitialized = false;
	
	private static DocumentBuilder reader;
	private static Transformer writer;
	
	public static void initXMLHandler() {
		try {
			reader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			writer = TransformerFactory.newInstance().newTransformer();
			xmlHandlerInitialized = reader != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadGlobalChecklist(File source) {
		try {
			Document xmlDoc = reader.parse(source);
			NodeList checklist = xmlDoc.getElementsByTagName("task");
			for (int n = 0; n < checklist.getLength(); n++) {
				try {
					Node aTask = checklist.item(n);
					String taskName = ((Element)aTask).getAttribute("name");
					String taskDesc = ((Element)aTask).getTextContent();
					globalEntryList.add(new TaskEntry(taskName, taskDesc));
				} catch (Exception e) {
					CClProxy.log.error("Error occured when append a new task.");
				}
			}
			
		} catch (Exception e) {
			
		}
	}
	
	public static void saveGlobalChecklist(Collection<TaskEntry> tasks) {
		try {
			Document xmlDoc = reader.newDocument();
			for (TaskEntry task : tasks) {
				Element aTask = xmlDoc.createElement("task");
				aTask.setAttribute("name", task.getName());
				aTask.setTextContent(task.getDescription());
				xmlDoc.appendChild(aTask);
			}
			writer.setOutputProperty(OutputKeys.INDENT, "yes");
			writer.setOutputProperty(OutputKeys.METHOD, "xml");
			writer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			writer.transform(new DOMSource(xmlDoc), new StreamResult(new FileOutputStream(ConfigMain.globalTasksFilePath)));
		} catch (Exception e) {
			CClProxy.log.error("An error occured when trying to save the global config file.");
		}
	}
	
	public static void loadRemoteChecklistFromServer() {
		
	}
	
	public static void saveRemoteChecklistToLocal(Collection<TaskEntry> tasks) {
		
	}

}
