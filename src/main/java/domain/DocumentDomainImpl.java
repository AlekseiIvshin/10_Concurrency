package domain;

import common.DocumentType;

public class DocumentDomainImpl implements DocumentDomain {
	private String series;
	private String number;
	private DocumentType type;
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
	public DocumentType getType() {
		return type;
	}
	public void setType(DocumentType type) {
		this.type = type;
	}
}
