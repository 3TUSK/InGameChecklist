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
			Node mainNode = xmlDoc.getElementsByTagName("checklist").item(0);
			NodeList checklist = ((Element) mainNode).getElementsByTagName("task");
			final int taskNumber = checklist.getLength();
			if (taskNumber > 0) {
				for (int n = 0; n < taskNumber; n++) {
					try {
						Node aTask = checklist.item(n);
						boolean enableI18n = Boolean.parseBoolean(((Element)aTask).getAttribute("enablei18n"));
						String taskName = ((Element)aTask).getAttribute("name");
						String taskDesc = ((Element)aTask).getTextContent();
						ITask entry;
						if (enableI18n) {
							entry = new TaskEntryTranslatable(taskName, taskDesc);
							NodeList translations = ((Element)aTask).getElementsByTagName("i18n");
							final int l = translations.getLength();
							if (l > 0) {
								for (int o = 0; o < l; o++) {
									Node aTranslation = translations.item(o);
									((TaskEntryTranslatable)entry).setTranslation(((Element)aTranslation).getAttribute("code"), ((Element)aTranslation).getTextContent());
								}
							}
						} else {
							entry = new TaskEntry(taskName, taskDesc);
						}
						
						localEntryList.add(entry);
					} catch (Exception e) {
						IClProxy.log.error("Error occured when append a new task.");
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
			Document xmlDoc = reader.newDocument();
			Element main = xmlDoc.createElement("checklist");
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
