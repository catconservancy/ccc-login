package org.rmcc.ccc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="lookup_options")
@NamedQuery(name="LookupOption.findAll", query="SELECT d FROM LookupOption d")
public class LookupOption implements Serializable, BaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOOUPOPTION_OPTIONID_GENERATOR", sequenceName="LOOUPOPTION_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOOUPOPTION_OPTIONID_GENERATOR")
	@Column(name="option_id", unique=true, nullable=false)
	private Integer id;

	@Column(name="label", nullable=false, length=255)
	private String label;

	@Column(name="list_code", length=255)
	private String listCode;

	public LookupOption(Integer optionId, String label, String listCode) {
		this.id = optionId;
		this.label = label;
		this.listCode = listCode;
	}

	public LookupOption() { }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getListCode() {
		return listCode;
	}

	public void setListCode(String listCode) {
		this.listCode = listCode;
	}

	@Override
	@JsonIgnore
	public String[] getFileHeaderMappings() {
		return new String[]{"label","list_code"};
	}

	@Override
	@JsonIgnore
	public String getFileName() {
		return "LookupOptions.csv";
	}

	@Override
	@JsonIgnore
	public BaseModel getFromCsvRecord(CSVRecord record) {
		Integer optionId = null;
		LookupOption option = new LookupOption(optionId, record.get("label"), record.get("list_code"));
		return option;
	}
	
}
