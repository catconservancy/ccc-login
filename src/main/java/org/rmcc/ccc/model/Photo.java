package org.rmcc.ccc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dropbox.core.v2.files.Metadata;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The persistent class for the "Photos" database table.
 * 
 */
@Entity
@Table(name = "photos")
@NamedQuery(name = "Photo.findAll", query = "SELECT p FROM Photo p")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Photo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PHOTOS_IMAGEID_GENERATOR", sequenceName = "PHOTOS_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHOTOS_IMAGEID_GENERATOR")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "direction_of_travel", length = 255)
	private String directionOfTravel;

	@Column(name = "file_name", length = 255)
	private String fileName;

	@Column(name = "file_path", length = 255)
	private String filePath;

	@Column(name = "dropbox_path", length = 4000)
	private String dropboxPath;

	@Column(name = "highlight")
	private Boolean highlight;

	@Column(name = "image_date")
	private Timestamp imageDate;

	// @Column(name="species", length=255)
	// private String species;

	// bi-directional many-to-one association to Deployment
	@ManyToOne
	@JoinColumn(name = "deployment_id")
	private Deployment deployment;

	// bi-directional many-to-one association to Detection
	@OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
	private List<Detection> detections;

	@Transient
	private Metadata metadata;

	public Photo() {
	}

	public String getDirectionOfTravel() {
		return this.directionOfTravel;
	}

	public void setDirectionOfTravel(String directionOfTravel) {
		this.directionOfTravel = directionOfTravel;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDropboxPath() {
		return dropboxPath;
	}

	public void setDropboxPath(String dropboxPath) {
		this.dropboxPath = dropboxPath;
	}

	public Boolean getHighlight() {
		return this.highlight;
	}

	public void setHighlight(Boolean highlight) {
		this.highlight = highlight;
	}

	public Timestamp getImageDate() {
		return this.imageDate;
	}

	public void setImageDate(Timestamp imageDate) {
		this.imageDate = imageDate;
	}

	// public String getSpecies() {
	// return this.species;
	// }
	//
	// public void setSpecies(String species) {
	// this.species = species;
	// }

	public Deployment getDeployment() {
		return this.deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Detection> getDetections() {
		return detections;
	}

	public void setDetections(List<Detection> detections) {
		this.detections = detections;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (id == null || obj == null || getClass() != obj.getClass())
			return false;
		Photo that = (Photo) obj;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

}