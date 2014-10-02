package dao;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import javax.sql.DataSource;

import dao.entities.PaymentEntity;

import javax.naming.*;

import ch.qos.logback.core.db.BindDataSourceToJNDIAction;

public class PaymentDAOJDBC implements PaymentDAO {

	public PaymentDAOJDBC(){
		//Context ctx = new InitialContext()
		//DataSource dataSource =  new BasicDataSource();
		//dataSource.getC
	}
	
	@Override
	public boolean add(PaymentEntity payment) throws IllegalArgumentException,
			TransactionRequiredException, EntityExistsException {
		// TODO Auto-generated method stub
		return false;
	}

}
