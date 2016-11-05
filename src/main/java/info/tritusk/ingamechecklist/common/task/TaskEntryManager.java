package info.tritusk.ingamechecklist.common.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.common.IClProxy;

public class TaskEntryManager implements ITaskManager {
	
	public static DocumentBuilder reader;
	public static Transformer writer;
	
	private Document xmlFile;
	
	private List<ITask> localEntryList = new ArrayList<>();
	
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
			Node mainNode = xmlFile.getElementsByTagName("checklist").item(0);
			NodeList checklist = ((Element) mainNode).getElementsByTagName("task");
			final int taskNumber = checklist.getLength();
			if (taskNumber > 0) {
				for (int n = 0; n < taskNumber; n++) {
					try {
						Node aTask = checklist.item(n);
						String taskName = ((Element)aTask).getAttribute("name");
						String taskDesc = ((Element)aTask).getTextContent();
						localEntryList.add(new TaskEntry(taskName, taskDesc));
					} catch (Exception e) {
						IClProxy.log.error("Error occured when loading new task.");
					}
				}
			}
			return true;
		} catch (Exception e) {
			IClProxy.log.error("Error occured when loading checklist.");
			IClProxy.log.error("Please double check your checklist file, make sure that everything matches standard.");
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean saveTo(final File file) {
		try {
			Element main = xmlFile.createElement("checklist");
			localEntryList.forEach(task -> {
				Element aTask = xmlFile.createElement("task");
				aTask.setAttribute("name", task.name());
				aTask.setTextContent(task.description());
				main.appendChild(aTask);
			});
			xmlFile.replaceChild(main, xmlFile.getElementsByTagName("checklist").item(0));
			writer.transform(new DOMSource(xmlFile), new StreamResult(new FileOutputStream(file, false)));
			IClProxy.log.info("Successfully saved local checklist.");
			localEntryList.clear();
			xmlFile = null;
			return true;
		} catch (Exception e) {
			IClProxy.log.error("An error occured when trying to save the local checklist file.");
			e.printStackTrace();
			return false;
		}
	}
	
	public Collection<ITask> getTasks() {
		return localEntryList;
	}

}
