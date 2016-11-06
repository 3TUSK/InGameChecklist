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
			if (IClConfig.debug)
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
						IClProxy.log.error("Error occured when loading a new task.");
					}
				}
			}
			IClProxy.log.info("Loaded local checklist.");
			return true;
		} catch (Exception e) {
			IClProxy.log.error("Tasks file may be empty or broken. A back up will be created.");
			if (IClConfig.debug)
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
			Element main = xmlFile.createElement("checklist");
			tasks.values().forEach(task -> append(xmlFile, task, main));
			if (list == null) {
				xmlFile.appendChild(main);
			} else { // Replace will overwrite the previous data, which is actually a work around for now.
				xmlFile.replaceChild(main, list);
			}
			writer.transform(new DOMSource(xmlFile), new StreamResult(new FileOutputStream(file, false)));
			IClProxy.log.info("Successfully saved local checklist.");
			tasks.clear();
			xmlFile = null;
			return true;
		} catch (Exception e) {
			IClProxy.log.error("An error occured when trying to save the local checklist file.");
			if (IClConfig.debug)
				e.printStackTrace();
			return false;
		}
	}
	
	private void append(Document doc, ITask task, Element element) {
		Element toAppend = doc.createElement("task");
		toAppend.setAttribute("name", task.name());
		toAppend.setTextContent(task.description());
		element.appendChild(toAppend);
	}
	
	public Collection<ITask> getTasks() {
		return tasks.values();
	}

	@Override
	public boolean addTask(ITask task) {
		return tasks.putIfAbsent(task.name(), task) == null;
	}

	@Override
	public boolean removeTask(ITask task) {
		return tasks.remove(task.name()) != null;
	}

	@Override
	public ITask getByName(String name) {
		return tasks.get(name);
	}

}
