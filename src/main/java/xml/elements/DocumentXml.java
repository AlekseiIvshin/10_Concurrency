package xml.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import common.DocumentType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "document")
public class DocumentXml{

	@XmlElement(required = true)
	private String series;

	@XmlElement(required = true)
	private String number;
	
	@XmlElement(required = true)
	private DocumentType type;

	public DocumentXml() {
	}

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
