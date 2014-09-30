package dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BankEnity  implements Serializable{

	@Column(name = "bank_bic")
	private String BIC;
	@Column(name = "bank_name")
	private String name;
	
	public BankEnity(){}
	
	public String getBIC() {
		return BIC;
	}
	public void setBIC(String bIC) {
		BIC = bIC;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
