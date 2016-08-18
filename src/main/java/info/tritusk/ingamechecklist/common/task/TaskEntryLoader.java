package info.tritusk.ingamechecklist.common.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import info.tritusk.ingamechecklist.api.ITaskTranslatable;
import info.tritusk.ingamechecklist.common.IClProxy;

public class TaskEntryLoader implements ITaskManager {
	
	public static DocumentBuilder reader;
	public static Transformer writer;
	
	private List<ITask> localEntryList = new ArrayList<>();
	
	private boolean xmlHandlerInitialized = false;
	
	@Override
	public boolean init() {
		try {
			if (!xmlHandlerInitialized) {
				reader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				writer = TransformerFactory.newInstance().newTransformer();
			}
			xmlHandlerInitialized = reader != null && writer != null;
			return xmlHandlerInitialized;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean isInitialized() {
		return xmlHandlerInitialized;
	}
	
	@Override
	public boolean loadFrom(InputStream input) {
		try {
			Document xmlDoc = reader.parse(input);
			Node mainNode = xmlDoc.getElementsByTagName("LocalChecklist").item(0);
			NodeList checklist = ((Element) mainNode).getElementsByTagName("task");
			for (int n = 0; n < checklist.getLength(); n++) {
				try {
					Node aTask = checklist.item(n);
					boolean enableI18n = Boolean.parseBoolean(((Element)aTask).getAttribute("enablei18n"));
					String taskName = ((Element)aTask).getAttribute("name");
					String taskDesc = ((Element)aTask).getTextContent();
					TaskEntry entry = new TaskEntry(taskName, taskDesc);
					if (enableI18n) {
						NodeList translations = ((Element)aTask).getElementsByTagName("i18n");
						for (int o = 0; o < translations.getLength(); o++) {
							Node aTranslation = translations.item(o);
							entry.setTranslation(((Element)aTranslation).getAttribute("code"), ((Element)aTranslation).getTextContent());
						}
					}
					
					localEntryList.add(entry);
				} catch (Exception e) {
					IClProxy.log.error("Error occured when append a new task.");
				}
			}
			return true;
		} catch (Exception e) {
			IClProxy.log.error("Error occured when loading checklist.");
			IClProxy.log.error("Please double check your checklist file, make sure that everything matches standard.");
			return false;
		}
	}
	
	@Override
	public boolean saveTo(final File file) {
		try {
			Document xmlDoc = reader.newDocument();
			Element main = xmlDoc.createElement("LocalChecklist");
			for (ITask task : localEntryList) {
				Element aTask = xmlDoc.createElement("task");
				aTask.setAttribute("name", task.name());
				aTask.setTextContent(task.description());
				if (aTask instanceof ITaskTranslatable) {
					aTask.setAttribute("enablei18n", "true");
					Map<String, String> i18nMap = ((ITaskTranslatable)aTask).getAllTranslations();
					for (Map.Entry<String, String> i18nEntry : i18nMap.entrySet()) {
						Element aTranslation = xmlDoc.createElement("i18n");
						aTranslation.setAttribute("code", i18nEntry.getKey());
						aTranslation.setTextContent(i18nEntry.getValue());
						aTask.appendChild(aTranslation);
					}
				}
				main.appendChild(aTask);
			}
			xmlDoc.appendChild(main);
			writer.setOutputProperty(OutputKeys.INDENT, "yes");
			writer.setOutputProperty(OutputKeys.METHOD, "xml");
			writer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			file.delete();
			file.createNewFile();
			writer.transform(new DOMSource(xmlDoc), new StreamResult(new FileOutputStream(file)));
			IClProxy.log.info("Successfully saved local checklist.");
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
