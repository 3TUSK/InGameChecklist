package info.tritusk.ingamechecklist.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import info.tritusk.ingamechecklist.api.ITask;
import info.tritusk.ingamechecklist.api.ITaskManager;
import info.tritusk.ingamechecklist.common.IClProxy;
import info.tritusk.ingamechecklist.common.task.TaskEntryManager;

public class RemoteChecklistManager implements Runnable, ITaskManager {
	
	private Document xml;
	
	private InputStream remoteTasks;
	private Socket socket;
	
	private volatile Map<String, ITask> tasks = new ConcurrentHashMap<>();
	
	public RemoteChecklistManager(String url) {
		try {
			socket = new Socket(url, 23333);
			socket.setSoTimeout(10000); //10 seconds time out
		} catch (UnknownHostException e) {
			throw new RuntimeException("The address of host is incorrect", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() { //Todo: thread-safe
		while (true) {
			try {
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				writer.println("InGameChecklist");
				writer.flush();
				writer.close();
				remoteTasks = socket.getInputStream();
			} catch (IOException e) {
				IClProxy.log.error("Fail to load tasks file from remote server.");
				e.printStackTrace();
			}
			if (!IClProxy.manager.isInitialized())
				IClProxy.manager.init();
			try {
				xml = TaskEntryManager.reader.parse(remoteTasks);
				Element e = xml.getDocumentElement();
				assert e.getTagName().equals("checklist");
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadFrom(InputStream input) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveTo(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized Collection<ITask> getTasks() {
		return tasks.values();
	}

	@Override
	public synchronized boolean addTask(ITask task) {
		return tasks.putIfAbsent(task.name(), task) == null;
	}

	@Override
	public synchronized boolean removeTask(ITask task) {
		return tasks.remove(task.name()) != null;
	}

	@Override
	public synchronized ITask getByName(String name) {
		return tasks.get(name);
	}

}
