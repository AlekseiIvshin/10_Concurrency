package dao.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "payments")
public class PaymentEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="payer_account")
	private PaymentMember payer;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="payee_account")
	private PaymentMember payee;

	@Embedded
	private BankEnity bank;

	@Column(name = "cash")
	private BigDecimal cash;

	@Column(name = "date_of_create")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfCreate;

	@Column(name = "date_of_execute")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfExecute;
	

	public PaymentEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PaymentMember getPayer() {
		return payer;
	}

	public void setPayer(PaymentMember payer) {
		this.payer = payer;
	}

	public PaymentMember getPayee() {
		return payee;
	}

	public void setPayee(PaymentMember payee) {
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
