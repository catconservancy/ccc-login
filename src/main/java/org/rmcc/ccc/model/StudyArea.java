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

/**
 * Entity implementation class for Entity: StudyArea
 *
 */
@Entity
@Table(name="study_area")
public class StudyArea implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="STUDY_AREA_ID_GENERATOR", sequenceName="STUDY_AREA_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUDY_AREA_ID_GENERATOR")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="name", length=255)
	private String name;

	//bi-directional many-to-one association to CameraMonitor
	@OneToMany(mappedBy="studyArea")
	private List<CameraMonitor> cameraMonitors;

	//bi-directional many-to-one association to Deployment
	@OneToMany(mappedBy="studyArea")
	private List<Deployment> deployments;

	public StudyArea() {
		super();
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
	public List<CameraMonitor> getCameraMonitors() {
		return cameraMonitors;
	}
	public void setCameraMonitors(List<CameraMonitor> cameraMonitors) {
		this.cameraMonitors = cameraMonitors;
	}
	public List<Deployment> getDeployments() {
		return deployments;
	}
	public void setDeployments(List<Deployment> deployments) {
		this.deployments = deployments;
	}
   
}
