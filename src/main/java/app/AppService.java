package app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;
import mapper.MypperImpl;
import concurrency.Consumer;
import concurrency.Drop;
import concurrency.DropImpl;
import concurrency.Producer;

public class AppService {
	private final Mapper mapper;
	private final ExecutorService executorService;
	private final Drop drop;
	
	public AppService(Drop drop, Mapper mapper){
		executorService = Executors.newCachedThreadPool();
		this.drop = drop;
		this.mapper = mapper;
	}
	
	
	public void startService(){
		executorService.execute(new Producer(drop, mapper));
		executorService.execute(new Consumer(drop, mapper));
	}
	
	public void stopService(){
		executorService.shutdown();
	}
}
