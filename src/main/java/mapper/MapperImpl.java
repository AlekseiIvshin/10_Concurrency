package mapper;

import xml.elements.BankXml;
import xml.elements.DocumentXml;
import xml.elements.PayeeXml;
import xml.elements.PayerXml;
import xml.elements.PaymentXml;
import dao.entities.BankEnity;
import dao.entities.Document;
import dao.entities.PaymentEntity;
import dao.entities.PaymentMemberInterface;
import domain.BankDomainImpl;
import domain.DocumentDomainImpl;
import domain.ParticipantDomainImpl;
import domain.PaymentDomainImpl;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class MapperImpl implements Mapper {

	private final static MapperFactory mapperFactory = initMapper();

	private static MapperFactory initMapper(){
		MapperFactory tmpFactory = new DefaultMapperFactory.Builder().build();
		// Participant: XML(Payee) -> domain
		tmpFactory.classMap( PayeeXml.class, ParticipantDomainImpl.class)
		.byDefault().register();
		// Participant: XML(Payer) -> domain
		tmpFactory.classMap( PayerXml.class, ParticipantDomainImpl.class)
		.byDefault().register();
		// Participant: domain -> entity
		tmpFactory.classMap(ParticipantDomainImpl.class,PaymentMemberInterface.class)
		.byDefault().register();
		
		// Document: XML -> domain
		tmpFactory.classMap( DocumentXml.class, DocumentDomainImpl.class)
		.byDefault().register();
		// Document: domain -> entity
		tmpFactory.classMap(DocumentDomainImpl.class,Document.class)
		.byDefault().register();
		

		// Bank: XML -> domain
		tmpFactory.classMap( BankXml.class, BankDomainImpl.class)
		.byDefault().register();
		// Bank: domain -> entity
		tmpFactory.classMap(BankDomainImpl.class,BankEnity.class)
		.byDefault().register();
		

		// Payment: XML -> domain
		tmpFactory.classMap( PaymentXml.class, PaymentDomainImpl.class)
		.byDefault().register();
		// Payment: domain -> entity
		tmpFactory.classMap(PaymentDomainImpl.class,PaymentEntity.class)
		.byDefault().register();
		
		return tmpFactory;
	}
	
	@Override
	public <S, D> D map(S sourceObject, Class<D> destination) {

		MapperFacade mapper = mapperFactory.getMapperFacade();
		return mapper.map(sourceObject, destination);
	}

}
