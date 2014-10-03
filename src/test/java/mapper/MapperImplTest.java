package mapper;

import static org.junit.Assert.*;

import org.jboss.logging.NDC;
import org.junit.Test;

import domain.BankDomain;
import domain.BankDomainImpl;
import xml.elements.BankXml;

public class MapperImplTest {

	@Test
	public void testMap() {
		Mapper mapper = new MapperImpl();
		BankXml bank = new BankXml();
		bank.setBIC("0000000");
		bank.setName("Testtest");
		BankDomain bankDomain = mapper.map(bank, BankDomainImpl.class);
		assertEquals(bank.getBIC(), bankDomain.getBIC());
		assertEquals(bank.getName(), bankDomain.getName());
	}

}
