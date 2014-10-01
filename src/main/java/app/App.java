package app;

import java.io.File;
import java.util.Scanner;

import mapper.Mapper;
import mapper.MapperImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.FactoryException;
import appservice.AppService;
import appservice.ServiceException;
import appservice.ServiceFactoryImpl;
import concurrency.quequestorages.drop.Drop;
import concurrency.quequestorages.drop.DropImpl;
import concurrency.quequestorages.files.FileStorage;
import concurrency.quequestorages.files.FileStorageImpl;

public class App {

	final static Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) {
		AppService app;
		try {
			app = new ServiceFactoryImpl().createService();
		} catch (FactoryException e) {
			logger.error("Service factory error",e);
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
			switch (currentCommand) {
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
				try {
					app.startService();
				} catch (ServiceException e) {
					logger.error("Service starting error",e);
				}
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
