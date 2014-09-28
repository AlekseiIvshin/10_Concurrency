package mapper;

import xml.elements.BankXml;
import xml.elements.DocumentXml;
import xml.elements.PayeeXml;
import xml.elements.PayerXml;
import xml.elements.PaymentXml;
import dao.payment.BankEnity;
import dao.payment.Document;
import dao.payment.ParticipantEntity;
import dao.payment.PaymentEntity;
import domain.BankDomain;
import domain.DocumentDomain;
import domain.ParticipantDomain;
import domain.PaymentDomain;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class MypperImpl implements Mapper {

	private final static MapperFactory mapperFactory = initMapper();

	private static MapperFactory initMapper(){
		MapperFactory tmpFactory = new DefaultMapperFactory.Builder().build();
		// Participant: XML(Payee) -> domain
		tmpFactory.classMap( PayeeXml.class, ParticipantDomain.class)
		.byDefault().register();
		// Participant: XML(Payer) -> domain
		tmpFactory.classMap( PayerXml.class, ParticipantDomain.class)
		.byDefault().register();
		// Participant: domain -> entity
		tmpFactory.classMap(ParticipantDomain.class,ParticipantEntity.class)
		.byDefault().register();
		
		// Document: XML -> domain
		tmpFactory.classMap( DocumentXml.class, DocumentDomain.class)
		.byDefault().register();
		// Document: domain -> entity
		tmpFactory.classMap(DocumentDomain.class,Document.class)
		.byDefault().register();
		

		// Bank: XML -> domain
		tmpFactory.classMap( BankXml.class, BankDomain.class)
		.byDefault().register();
		// Bank: domain -> entity
		tmpFactory.classMap(BankDomain.class,BankEnity.class)
		.byDefault().register();
		

		// Payment: XML -> domain
		tmpFactory.classMap( PaymentXml.class, PaymentDomain.class)
		.byDefault().register();
		// Payment: domain -> entity
		tmpFactory.classMap(PaymentDomain.class,PaymentEntity.class)
		.byDefault().register();
		
		return tmpFactory;
	}
	
	@Override
	public <S, D> D map(S sourceObject, Class<D> destination) {

		MapperFacade mapper = mapperFactory.getMapperFacade();
		return mapper.map(sourceObject, destination);
	}

}
