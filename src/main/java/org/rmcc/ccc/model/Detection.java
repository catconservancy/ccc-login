package org.rmcc.ccc.model;

import java.io.Serializable;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * The persistent class for the "Detections" database table.
 * 
 */
@Entity
@Table(name="detections")
@NamedQuery(name="Detection.findAll", query="SELECT d FROM Detection d")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Detection implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DETECTIONS_DETECTIONID_GENERATOR", sequenceName="DETECTIONS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETECTIONS_DETECTIONID_GENERATOR")
	@Column(name="detection_id", unique=true, nullable=false)
	private Integer id;

	@Column(name="comments", length=255)
	private String comments;

	@Column(name="individuals")
	private Integer individuals;

	//bi-directional many-to-one association to IndividualID
	@OneToMany(mappedBy="detection")
	private List<IndividualID> IndividualIds;

	//bi-directional many-to-one association to Species
	@ManyToOne
	@JoinColumn(name="species_id")
	private Species species;

	//bi-directional many-to-one association to DetectionDetails
	@ManyToOne
	@JoinColumn(name="detail_id")
	private DetectionDetail detectionDetail;

	//bi-directional many-to-one association to Photo
	@ManyToOne
	@JoinColumn(name="image_id")
	private Photo photo;

	public Detection() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getIndividuals() {
		return this.individuals;
	}

	public void setIndividuals(Integer individuals) {
		this.individuals = individuals;
	}

	public Species getSpecies() {
		return this.species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public DetectionDetail getDetectionDetail() {
		return detectionDetail;
	}

	public void setDetectionDetail(DetectionDetail detectionDetail) {
		this.detectionDetail = detectionDetail;
	}

	public List<IndividualID> getIndividualIds() {
		return IndividualIds;
	}

	public void setIndividualIds(List<IndividualID> individualIds) {
		IndividualIds = individualIds;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

}