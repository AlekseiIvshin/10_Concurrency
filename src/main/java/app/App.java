package app;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;
import mapper.MypperImpl;
import concurrency.Consumer;
import concurrency.Drop;
import concurrency.DropImpl;
import concurrency.Producer;

public class App {

	public static void main(String[] args) {
		Drop drop = new DropImpl(10);
		Mapper mapper = new MypperImpl();
		AppService app = new AppService(drop, mapper);
	}

	public static void processDirect(AppService app) {
		Scanner sc = new Scanner(System.in);
		String current = "";
		// TODO: tooooooooooooooo llooooooooooooooooooo IF statement
		while ((current = sc.next()) != null && !current.equalsIgnoreCase("exit")) {
			current = current.toLowerCase();
			switch (current) {
			case "start":
				app.startService();
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
