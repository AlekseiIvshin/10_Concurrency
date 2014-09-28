package concurrency;

import domain.PaymentDomain;
import ma.glasnost.orika.MapperFacade;
import mapper.Mapper;
import xml.elements.PaymentXml;
import xml.elements.PaymentsXml;

public class Producer implements Runnable{

	private final Drop drop;
	private final Mapper mapper;

	public Producer(Drop drop, Mapper mapper) {
		this.drop = drop;
		this.mapper = mapper;
	}

	public void setPayment() {
		PaymentXml payment = readPayment();
		if(payment!= null){
			PaymentDomain domainPayment = mapper.map(payment, PaymentDomain.class);
			try {
				while(!drop.setPayment(domainPayment)){
					try {
				        wait();
				    } catch (InterruptedException e) {}
				}
				notifyAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private PaymentXml readPayment(){
		// TODO read file and get payments. 
		// I think need use stax and jaxb.. 
		// 		GEt one payment -> to Drop -> next payment. 
		//		If no payment -> remove file
		return null;
	}

	@Override
	public void run() {
		setPayment();
	}
}
