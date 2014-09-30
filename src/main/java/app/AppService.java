package app;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapper.Mapper;
import concurrency.Consumer;
import concurrency.Drop;
import concurrency.Producer;
import dao.PaymentDAOImpl;

public class AppService {
	private final Mapper mapper;
	private final ExecutorService executorService;
	private final Drop drop;
	
	public AppService(Drop drop, Mapper mapper){
		executorService = Executors.newCachedThreadPool();
		this.drop = drop;
		this.mapper = mapper;
	}
	
	public void startService(File directory, int countOfProducers, int countOfConsumers){
		try {
			if(countOfProducers <=0 || countOfConsumers <=0){
				throw new Exception(" producers and consumers count must be greater then 0");
			}
			for(int i =0;i<countOfProducers;i++){
				executorService.execute(new Producer(drop, mapper, directory));
			}

			for(int i =0;i<countOfConsumers;i++){
				executorService.execute(new Consumer(drop, mapper, new PaymentDAOImpl()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopService(){
		executorService.shutdown();
	}
}
