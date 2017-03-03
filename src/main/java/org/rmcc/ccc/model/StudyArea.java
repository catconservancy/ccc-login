package org.rmcc.ccc.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.csv.CSVRecord;

/**
 * Entity implementation class for Entity: StudyArea
 *
 */
@Entity
@Table(name="study_area")
public class StudyArea implements Serializable, BaseModel {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="STUDY_AREA_ID_GENERATOR", sequenceName="STUDY_AREA_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUDY_AREA_ID_GENERATOR")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="name", length=255)
	private String name;

	@Column(name="dropbox_path")
	private String dropboxPath;

	//bi-directional many-to-one association to Deployment
	@OneToMany(mappedBy="studyArea")
	@JsonIgnore
	private List<Deployment> deployments;

	public StudyArea() {
		super();
	}

	public StudyArea(Integer studyAreaId, String studyAreaName) {
		this.id = studyAreaId;
		this.name = studyAreaName;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDropboxPath() {
		return dropboxPath;
	}

	public void setDropboxPath(String dropboxPath) {
		this.dropboxPath = dropboxPath;
	}

	public List<Deployment> getDeployments() {
		return deployments;
	}
	public void setDeployments(List<Deployment> deployments) {
		this.deployments = deployments;
	}

	@Override
	@JsonIgnore
	public String[] getFileHeaderMappings() {
		return new String[]{"StudyAreaID","StudyAreaName"};
	}

	@Override
	@JsonIgnore
	public String getFileName() {
		return "StudyAreas_lory.csv";
	}

	@Override
	@JsonIgnore
	public StudyArea getFromCsvRecord(CSVRecord record) {
		Integer studyAreaId = null;
		try { studyAreaId = Integer.parseInt(record.get("StudyAreaID")); } catch (NumberFormatException e) {}
		if (studyAreaId != null) {
			StudyArea studyArea = new StudyArea(studyAreaId, record.get("StudyAreaName"));
			return studyArea;
		}
		return null;
	}
}
