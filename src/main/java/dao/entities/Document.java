package dao.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class Document {
	@Column(name = "document_series")
	private String series;
	@Column(name = "document_number")
	private String number;
	@Column(name="document_type")
	private String type;
	
	public Document(){}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
