package org.rmcc.ccc.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.csv.CSVRecord;

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
import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the "Detections" database table.
 * 
 */
@Entity
@Table(name="detections")
@NamedQuery(name="Detection.findAll", query="SELECT d FROM Detection d")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Detection implements Serializable, BaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DETECTIONS_DETECTIONID_GENERATOR", sequenceName="DETECTIONS_ID_SEQ", allocationSize=1)
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="species_id")
	private Species species;

	@Transient
	private Integer speciesId;

	//bi-directional many-to-one association to DetectionDetails
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="detail_id")
	private DetectionDetail detectionDetail;

	@Transient
	private Integer detectionDetailId;

	//bi-directional many-to-one association to Photo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="image_id")
	@JsonIgnore
	private Photo photo;

	@Transient
	private Integer photoId;

	public Detection() {
	}

	public Detection(Integer detectionId, String speciesId, String detectionDetailId, String individuals, String comments, String photoId) {
		this.id = detectionId;
		this.speciesId = speciesId != null && !"".equalsIgnoreCase(speciesId) ? Integer.parseInt(speciesId) : null;
		this.detectionDetailId = detectionDetailId != null && !"".equalsIgnoreCase(detectionDetailId) ? Integer.parseInt(detectionDetailId) : null;
		this.individuals = individuals != null && !"".equalsIgnoreCase(individuals) ? Integer.parseInt(individuals) : null;
		this.comments = comments;
		this.photoId = photoId != null && !"".equalsIgnoreCase(photoId) ? Integer.parseInt(photoId) : null;
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

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

	public Integer getDetectionDetailId() {
		return detectionDetailId;
	}

	public void setDetectionDetailId(Integer detectionDetailId) {
		this.detectionDetailId = detectionDetailId;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	@Override
	public String[] getFileHeaderMappings() {
		return new String[]{"DetectionID","SpeciesID","DetailID","Individuals","Comments","ImageID"};
	}

	@Override
	public String getFileName() {
		return "Detections.csv";
	}

	@Override
	public BaseModel getFromCsvRecord(CSVRecord record) {
		Integer detectionId = null;
		try { detectionId = Integer.parseInt(record.get("DetectionID")); } catch (NumberFormatException e) {}
		if (detectionId != null) {
			Detection detection = new Detection(detectionId,
					record.get("SpeciesID"),
					record.get("DetailID"),
					record.get("Individuals"),
					record.get("Comments"),
					record.get("ImageID"));
			return detection;
		}
		return null;
	}
}