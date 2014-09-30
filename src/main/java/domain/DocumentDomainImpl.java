package domain;

import common.DocumentType;

public class DocumentDomainImpl implements DocumentDomain {
	private String series;
	private String number;
	private String type;
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
