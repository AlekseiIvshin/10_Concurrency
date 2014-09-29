package dao.payment;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "payments")
public class PaymentEntityImpl implements PaymentEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Embedded
	private PayerEntiy payer;
	

	@Embedded
	private PayeeEntiy payee;
	
	@Embedded
	private BankEnity bank;
	
	@Column(name = "cash")
	private BigDecimal cash;

	@Column(name ="date_of_create")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfCreate;
	

	@Column(name ="date_of_execute")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfExecute;
	
	public PaymentEntityImpl(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PayerEntiy getPayer() {
		return payer;
	}

	public void setPayer(PayerEntiy payer) {
		this.payer = payer;
	}

	public PayeeEntiy getPayee() {
		return payee;
	}

	public void setPayee(PayeeEntiy payee) {
		this.payee = payee;
	}

	public BankEnity getBank() {
		return bank;
	}

	public void setBank(BankEnity bank) {
		this.bank = bank;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public Date getDateOfCreate() {
		return dateOfCreate;
	}

	public void setDateOfCreate(Date dateOfCreate) {
		this.dateOfCreate = dateOfCreate;
	}

	public Date getDateOfExecute() {
		return dateOfExecute;
	}

	public void setDateOfExecute(Date dateOfExecute) {
		this.dateOfExecute = dateOfExecute;
	}

}
