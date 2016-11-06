package info.tritusk.ingamechecklist.common.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.common.IClConfig;
import info.tritusk.ingamechecklist.common.IClProxy;
import net.minecraftforge.common.DimensionManager;

public class TaskEntryManager implements ITaskManager {
	
	public static DocumentBuilder reader;
	public static Transformer writer;
	
	private Document xmlFile;
	
	private Map<String, ITask> tasks = new HashMap<>();
	
	private boolean initialized = false;
	
	@Override
	public boolean init() {
		try {
			if (!initialized) {
				reader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				writer = TransformerFactory.newInstance().newTransformer();
				writer.setOutputProperty(OutputKeys.INDENT, "yes");
				writer.setOutputProperty(OutputKeys.METHOD, "xml");
				writer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			}
			initialized = reader != null && writer != null;
			return initialized;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}
	
	@Override
	public boolean loadFrom(InputStream input) {
		try {
			xmlFile = reader.parse(input);
			Element main = (Element)xmlFile.getElementsByTagName("checklist").item(0);
			NodeList checklist = main.getElementsByTagName("task");
			final int taskNumber = checklist.getLength();
			if (taskNumber > 0) {
				for (int n = 0; n < taskNumber; n++) {
					try {
						Node aTask = checklist.item(n);
						String taskName = ((Element)aTask).getAttribute("name");
						String taskDesc = ((Element)aTask).getTextContent();
						tasks.put(taskName, new TaskEntry(taskName, taskDesc));
					} catch (Exception e) {
						IClProxy.log.error("Error occured when loading new task.");
					}
				}
			}
			return true;
		} catch (Exception e) {
			IClProxy.log.error("Tasks file may be empty or broken. A back up will be created.");
			if (IClConfig.Options.debug)
				e.printStackTrace();
			try {
				OutputStream out = new FileOutputStream(new File(DimensionManager.getCurrentSaveRootDirectory(), "InGameChecklist_bak.xml"), false);
				IOUtils.copy(input, out);
				new OutputStreamWriter(out, "UTF-8").flush();
			} catch (FileNotFoundException impossible) {
				//Constructor of File(parent, child) only throw FileNotFoundException when child == null
			} catch (IOException _e) {
				IClProxy.log.error("Failed to create backup, existed task file may be overwritten soon or later. Please do manual backup immediately!");
			}
			xmlFile = reader.newDocument();
			return false;
		}
	}
	
	@Override
	public boolean saveTo(final File file) {
		try {
			Element list = xmlFile.getDocumentElement();
			if (list == null || !list.getAttribute("name").equals("checklist")) {
				Element main = xmlFile.createElement("checklist");
				tasks.entrySet().forEach(task -> {
					Element aTask = xmlFile.createElement("task");
					aTask.setAttribute("name", task.getKey());
					aTask.setTextContent(task.getValue().description());
					main.appendChild(aTask);
				});
				xmlFile.appendChild(main);
			} else {
				NodeList taskNodes = list.getElementsByTagName("task");
				final int length = taskNodes.getLength();
				for (int i = 0; i < length; i++) {
					
				}
			}			
			writer.transform(new DOMSource(xmlFile), new StreamResult(new FileOutputStream(file, false)));
			IClProxy.log.info("Successfully saved local checklist.");
			tasks.clear();
			xmlFile = null;
			return true;
		} catch (Exception e) {
			IClProxy.log.error("An error occured when trying to save the local checklist file.");
			if (IClConfig.Options.debug)
				e.printStackTrace();
			return false;
		}
	}
	
	public Collection<ITask> getTasks() {
		return tasks.values();
	}

}
