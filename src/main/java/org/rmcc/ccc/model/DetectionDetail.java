package org.rmcc.ccc.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the "DetectionDetails" database table.
 * 
 */
@Entity
@Table(name="detection_details")
@NamedQuery(name="DetectionDetail.findAll", query="SELECT d FROM DetectionDetail d")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DetectionDetail implements Serializable, BaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DETECTIONDETAILS_DETAILID_GENERATOR", sequenceName="DETECTIONDETAILS_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DETECTIONDETAILS_DETAILID_GENERATOR")
	@Column(name="detail_id", unique=true, nullable=false)
	private Integer id;

	@Column(name="detail_text", nullable=false, length=255)
	private String detailText;

	@Column(name="shortcut_key", length=1)
	private String shortcutKey;

	//bi-directional many-to-one association to Species
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="species_id")
	private Species species;

	@Transient
	private Integer specId;

	//bi-directional many-to-one association to Detection
	@OneToMany(mappedBy="detectionDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Detection> detections;

	public DetectionDetail() {
	}

	public DetectionDetail(Integer detailId, String detailText, String speciesId, String shortcutKey) {
//		DetailID,DetailText,SpeciesID,ShortcutKey
		this.id = detailId;
		this.detailText = detailText;
		this.specId = speciesId != null && !"".equalsIgnoreCase(speciesId) ? Integer.parseInt(speciesId) : null;
		this.shortcutKey = shortcutKey;
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

	public Integer getSpecId() {
		return specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}

	public List<Detection> getDetections() {
		return this.detections != null ? this.detections : new ArrayList<>();
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

	@Override
	@JsonIgnore
	public String[] getFileHeaderMappings() {
		return new String[]{"DetailID","DetailText","SpeciesID","ShortcutKey"};
	}

	@Override
	@JsonIgnore
	public String getFileName() {
		return "DetectionDetails_lory.csv";
	}

	@Override
	@JsonIgnore
	public DetectionDetail getFromCsvRecord(CSVRecord record) {
		Integer detectionDetailId = null;
		try { detectionDetailId = Integer.parseInt(record.get("DetailID")); } catch (NumberFormatException e) {}
		if (detectionDetailId != null) {
			DetectionDetail detectionDetail = new DetectionDetail(detectionDetailId,
					record.get("DetailText"),
					record.get("SpeciesID"),
					record.get("ShortcutKey"));
			return detectionDetail;
		}
		return null;
	}

}