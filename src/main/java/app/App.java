package app;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xml.JAXBParser;
import mapper.Mapper;
import mapper.MapperImpl;
import concurrency.Consumer;
import concurrency.Drop;
import concurrency.DropImpl;
import concurrency.FileStorage;
import concurrency.FileStorageImpl;
import concurrency.Producer;

public class App {

	final static Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		Drop drop = new DropImpl(10);
		FileStorage fileStorage = new FileStorageImpl(10);
		Mapper mapper = new MapperImpl();
		AppService app;
		try {
			app = new AppService(drop, mapper,fileStorage);
		} catch (ServiceException e) {
			logger.error("AppService initialize", e);
			return;
		}
		processDirect(app);
	}

	public static void processDirect(AppService app) {
		Scanner sc = new Scanner(System.in);
		String current = "";
		// TODO: tooooooooooooooo llooooooooooooooooooo IF statement
		while ((current = sc.next()) != null
				&& !current.equalsIgnoreCase("exit")) {
			String[] splittedCommand = current.split(" ");
			if(splittedCommand.length == 0){
				continue;
			}
			String currentCommand = splittedCommand[0].toLowerCase();
			switch (current) {
			case "start":
				if(splittedCommand.length<1){
					logger.info("Set path to directory with XML files: start <Path>");
					continue;
				}
				String filePath = splittedCommand[1];
				File f = new File(filePath);
				if(!f.canRead() || !f.isDirectory()){
					logger.info("{} is not directory or can't been readed", filePath);
				}
				app.startService(f,2,2);
				break;
			case "stop":
				app.stopService();
				break;
			default:
				break;
			}
		}
		System.out.println("Goodbye!");

	}
}
