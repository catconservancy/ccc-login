package org.rmcc.ccc.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the "CameraMonitor" database table.
 * 
 */
@Entity
@Table(name="camera_monitor")
@NamedQuery(name="CameraMonitor.findAll", query="SELECT c FROM CameraMonitor c")
public class CameraMonitor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CAMERAMONITOR_LOGID_GENERATOR", sequenceName="CAMERAMONITOR_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CAMERAMONITOR_LOGID_GENERATOR")
	@Column(name="log_id", unique=true, nullable=false)
	private Integer id;

	@Column(name="camera_delay_setting", length=255)
	private String cameraDelaySetting;

	@Column(name="camera_date_time_correct")
	private Boolean cameraDateTimeCorrect;

	@Column(name="check_date")
	private Timestamp checkDate;

	@Column(name="check_time")
	private Timestamp checkTime;

	@Column(name="setLetter", length=1)
	private String setLetter;

	@Column(name="comments", length=255)
	private String comments;

	@Column(name="lure_info", length=255)
	private String lureInfo;

	@Column(name="new_batteries")
	private Boolean newBatteries;

	@Column(name="new_camera_id")
	private Boolean newCameraId;

	@Column(name="new_card")
	private Boolean newCard;

	@Column(name="num_pics")
	private Integer numPics;

	@Column(name="reasercher_names", length=255)
	private String reasercherNames;

	@Column(name="weather_temp_f")
	private Integer weatherTempF;

	@Column(name="wildlife_seen", length=255)
	private String wildlifeSeen;

	@Column(name="wildlife_sign", length=255)
	private String wildlifeSign;

	@Column(name="wildlife_sign_species", length=255)
	private String wildlifeSignSpecies;

	//bi-directional many-to-one association to Species
	@ManyToOne
	@JoinColumn(name="species_id")
	private Species species;

	//bi-directional many-to-one association to Deployments
	@ManyToOne
	@JoinColumn(name="deployment_id")
	private Deployment deployment;

	//bi-directional many-to-one association to StudyArea
	@ManyToOne
	@JoinColumn(name="study_area_id")
	private StudyArea studyArea;

	public CameraMonitor() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCameraDelaySetting() {
		return cameraDelaySetting;
	}

	public void setCameraDelaySetting(String cameraDelaySetting) {
		this.cameraDelaySetting = cameraDelaySetting;
	}

	public Boolean getCameraDateTimeCorrect() {
		return cameraDateTimeCorrect;
	}

	public void setCameraDateTimeCorrect(Boolean cameraDateTimeCorrect) {
		this.cameraDateTimeCorrect = cameraDateTimeCorrect;
	}

	public Timestamp getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Timestamp checkDate) {
		this.checkDate = checkDate;
	}

	public Timestamp getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Timestamp checkTime) {
		this.checkTime = checkTime;
	}

	public String getSetLetter() {
		return setLetter;
	}

	public void setSetLetter(String setLetter) {
		this.setLetter = setLetter;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getLureInfo() {
		return lureInfo;
	}

	public void setLureInfo(String lureInfo) {
		this.lureInfo = lureInfo;
	}

	public Boolean getNewBatteries() {
		return newBatteries;
	}

	public void setNewBatteries(Boolean newBatteries) {
		this.newBatteries = newBatteries;
	}

	public Boolean getNewCameraId() {
		return newCameraId;
	}

	public void setNewCameraId(Boolean newCameraId) {
		this.newCameraId = newCameraId;
	}

	public Boolean getNewCard() {
		return newCard;
	}

	public void setNewCard(Boolean newCard) {
		this.newCard = newCard;
	}

	public Integer getNumPics() {
		return numPics;
	}

	public void setNumPics(Integer numPics) {
		this.numPics = numPics;
	}

	public String getReasercherNames() {
		return reasercherNames;
	}

	public void setReasercherNames(String reasercherNames) {
		this.reasercherNames = reasercherNames;
	}

	public Integer getWeatherTempF() {
		return weatherTempF;
	}

	public void setWeatherTempF(Integer weatherTempF) {
		this.weatherTempF = weatherTempF;
	}

	public String getWildlifeSeen() {
		return wildlifeSeen;
	}

	public void setWildlifeSeen(String wildlifeSeen) {
		this.wildlifeSeen = wildlifeSeen;
	}

	public String getWildlifeSign() {
		return wildlifeSign;
	}

	public void setWildlifeSign(String wildlifeSign) {
		this.wildlifeSign = wildlifeSign;
	}

	public String getWildlifeSignSpecies() {
		return wildlifeSignSpecies;
	}

	public void setWildlifeSignSpecies(String wildlifeSignSpecies) {
		this.wildlifeSignSpecies = wildlifeSignSpecies;
	}

	public Species getSpecies() {
		return species;
	}

	public void setSpecies(Species species) {
		this.species = species;
	}
	public Deployment getDeployment() {
		return deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}

	public StudyArea getStudyArea() {
		return studyArea;
	}

	public void setStudyArea(StudyArea studyArea) {
		this.studyArea = studyArea;
	}

	@JsonIgnore
	public String[] getFileHeaderMappings() {
		return new String[]{
				"LogID",
				"DeploymentId",
				"CheckDate",
				"CheckTime",
				"StudyAreaID",
				"ResearchecNames",
				"WeatherTemp(F)",
				"No Of Pics", 
				"New Camera ID",
				"New Batteries",
				"New Card",
				"CameraID/TCorrect",
				"CameraDelaySetting",
				"LureInfo",
				"WildlifeSign",
				"WildlifeSignSpecies",
				"WildlifeSeen",
				"Comments",
				"SpeciesID"
						};
	}
	
	@JsonIgnore
	public String getFileName() {
		return "Species.csv";
	}

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