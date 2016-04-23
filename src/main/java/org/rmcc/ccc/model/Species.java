package org.rmcc.ccc.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the "Species" database table.
 * 
 */
@Entity
@Table(name="species")
@NamedQuery(name="Species.findAll", query="SELECT s FROM Species s")
public class Species implements Serializable, BaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SPECIES_SPECIESID_GENERATOR", sequenceName="SPECIES_ID_SEQ", allocationSize=1, initialValue=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPECIES_SPECIESID_GENERATOR")
	@Column(name="species_id", unique=true, nullable=false)
	private Integer id;

	@Column(name="common_name", nullable=false, length=255)
	private String commonName;

	@Column(name="latin_name", length=255)
	private String latinName;

	@Column(name="shortcut_key", length=1)
	private String shortcutKey;

	//bi-directional many-to-one association to CameraMonitor
	@OneToMany(mappedBy="species", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<CameraMonitor> cameraMonitors;

	//bi-directional many-to-one association to Detection
	@OneToMany(mappedBy="species", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Detection> detections;

	//bi-directional many-to-one association to DetectionDetails
	@OneToMany(mappedBy="species", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<DetectionDetail> detectionDetails;

	public Species() {
	}

	public Species(Integer id, String commonName, String latinName, String shortcutKey) {
		this.id = id;
		this.commonName = commonName;
		this.latinName = latinName;
		this.shortcutKey = shortcutKey;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommonName() {
		return this.commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getLatinName() {
		return this.latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getShortcutKey() {
		return this.shortcutKey;
	}

	public void setShortcutKey(String shortcutKey) {
		this.shortcutKey = shortcutKey;
	}

	public List<CameraMonitor> getCameraMonitors() {
		return this.cameraMonitors;
	}

	public void setCameraMonitors(List<CameraMonitor> cameraMonitors) {
		this.cameraMonitors = cameraMonitors;
	}

	public CameraMonitor addCameraMonitor(CameraMonitor cameraMonitor) {
		getCameraMonitors().add(cameraMonitor);
		cameraMonitor.setSpecies(this);

		return cameraMonitor;
	}

	public CameraMonitor removeCameraMonitor(CameraMonitor cameraMonitor) {
		getCameraMonitors().remove(cameraMonitor);
		cameraMonitor.setSpecies(null);

		return cameraMonitor;
	}

	public List<Detection> getDetections() {
		return this.detections;
	}

	public void setDetections(List<Detection> detections) {
		this.detections = detections;
	}

	public Detection addDetection(Detection detection) {
		getDetections().add(detection);
		detection.setSpecies(this);

		return detection;
	}

	public Detection removeDetection(Detection detection) {
		getDetections().remove(detection);
		detection.setSpecies(null);

		return detection;
	}

	public List<DetectionDetail> getDetectionDetails() {
		return detectionDetails;
	}

	public void setDetectionDetails(List<DetectionDetail> detectionDetails) {
		this.detectionDetails = detectionDetails;
	}

	@Override
	@JsonIgnore
	public String[] getFileHeaderMappings() {
		return new String[]{"SpeciesID","CommonName","LatinName","ShortcutKey"};
	}
	
	@Override
	@JsonIgnore
	public String getFileName() {
		return "Species.csv";
	}

	@Override
	@JsonIgnore
	public Species getFromCsvRecord(CSVRecord record) {
		Integer speciesId = null;
		try { speciesId = Integer.parseInt(record.get("SpeciesID")); } catch (NumberFormatException e) {}
		if (speciesId != null) {
			Species species = new Species(speciesId, record.get("CommonName"), record.get("LatinName"), record.get("ShortcutKey"));
			return species;
		}
		return null;
	}
}