package org.rmcc.ccc.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


/**
 * The persistent class for the "DetectionDetails" database table.
 * 
 */
@Entity
@Table(name="detection_details")
@NamedQuery(name="DetectionDetail.findAll", query="SELECT d FROM DetectionDetail d")
public class DetectionDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DETECTIONDETAILS_DETAILID_GENERATOR", sequenceName="DETECTIONDETAILS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETECTIONDETAILS_DETAILID_GENERATOR")
	@Column(name="detail_id", unique=true, nullable=false)
	private Integer id;

	@Column(name="detail_text", nullable=false, length=255)
	private String detailText;

	@Column(name="shortcut_key", length=1)
	private String shortcutKey;

	//bi-directional many-to-one association to Species
	@ManyToOne
	@JoinColumn(name="species_id")
	private Species species;

	//bi-directional many-to-one association to Detection
	@OneToMany(mappedBy="detectionDetail")
	private List<Detection> detections;

	public DetectionDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetailText() {
		return this.detailText;
	}

	public void setDetailText(String detailText) {
		this.detailText = detailText;
	}

	public String getShortcutKey() {
		return this.shortcutKey;
	}

	public void setShortcutKey(String shortcutKey) {
		this.shortcutKey = shortcutKey;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}

	public List<Detection> getDetections() {
		return this.detections;
	}

	public void setDetections(List<Detection> detections) {
		this.detections = detections;
	}

	public Detection addDetection(Detection detection) {
		getDetections().add(detection);
		detection.setDetectionDetail(this);

		return detection;
	}

	public Detection removeDetection(Detection detection) {
		getDetections().remove(detection);
		detection.setDetectionDetail(null);

		return detection;
	}

}