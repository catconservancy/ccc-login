package org.rmcc.ccc.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the "Deployments" database table.
 * 
 */
@Entity
@Table(name="deployments")
@NamedQuery(name="Deployment.findAll", query="SELECT d FROM Deployment d")
public class Deployment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEPLOYMENTS_DEPLOYMENTID_GENERATOR", sequenceName="DEPLOYMENTS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DEPLOYMENTS_DEPLOYMENTID_GENERATOR")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="azimuth", length=255)
	private String azimuth;

	@Column(name="distance_to_human_habitat", length=255)
	private String distanceToHumanHabitat;

	@Column(name="distance_to_road", length=255)
	private String distanceToRoad;

	@Column(name="dominant_substrate", length=255)
	private String dominantSubstrate;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="habitat_ruggedness", length=255)
	private String habitatRuggedness;

	@Column(name="human_habitat_type", length=255)
	private String humanHabitatType;

	@Column(name="human_visitation", length=255)
	private String humanVisitation;

	@Column(name="location_id", length=255)
	private String locationID;

	@Column(name="notes", length=255)
	private String notes;

	@Column(name="ownership", length=255)
	private String ownership;

	@Column(name="position_on_slope", length=255)
	private String positionOnSlope;

	@Column(name="rangeland_use", length=255)
	private String rangelandUse;

	@Column(name="road_type", length=255)
	private String roadType;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="time_of_day", length=255)
	private String timeOfDay;

	@Column(name="topographic_feature", length=255)
	private String topographicFeature;

	@Column(name="trail_type", length=255)
	private String trailType;

	@Column(name="utm_datum")
	private Integer utmDatum;

	@Column(name="utm_e")
	private Integer utmE;

	@Column(name="utm_n")
	private Integer utmN;

	@Column(name="utm_zone")
	private Integer utmZone;

	@Column(name="vegetation_type", length=255)
	private String vegetationType;

	//bi-directional many-to-one association to CameraMonitor
	@OneToMany(mappedBy="deployment")
	private List<CameraMonitor> cameraMonitors;

	//bi-directional many-to-one association to StudyArea
	@ManyToOne
	@JoinColumn(name="study_area_id")
	private StudyArea studyArea;

	//bi-directional many-to-one association to Photo
	@OneToMany(mappedBy="deployment")
	private List<Photo> photos;

	public Deployment() {
	}

	public Integer getDeploymentID() {
		return this.id;
	}

	public void setDeploymentID(Integer deploymentID) {
		this.id = deploymentID;
	}

	public String getAzimuth() {
		return this.azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public String getDistanceToHumanHabitat() {
		return this.distanceToHumanHabitat;
	}

	public void setDistanceToHumanHabitat(String distanceToHumanHabitat) {
		this.distanceToHumanHabitat = distanceToHumanHabitat;
	}

	public String getDistanceToRoad() {
		return this.distanceToRoad;
	}

	public void setDistanceToRoad(String distanceToRoad) {
		this.distanceToRoad = distanceToRoad;
	}

	public String getDominantSubstrate() {
		return this.dominantSubstrate;
	}

	public void setDominantSubstrate(String dominantSubstrate) {
		this.dominantSubstrate = dominantSubstrate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getHabitatRuggedness() {
		return this.habitatRuggedness;
	}

	public void setHabitatRuggedness(String habitatRuggedness) {
		this.habitatRuggedness = habitatRuggedness;
	}

	public String getHumanHabitatType() {
		return this.humanHabitatType;
	}

	public void setHumanHabitatType(String humanHabitatType) {
		this.humanHabitatType = humanHabitatType;
	}

	public String getHumanVisitation() {
		return this.humanVisitation;
	}

	public void setHumanVisitation(String humanVisitation) {
		this.humanVisitation = humanVisitation;
	}

	public String getLocationID() {
		return this.locationID;
	}

	public void setLocationID(String locationID) {
		this.locationID = locationID;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOwnership() {
		return this.ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}

	public String getPositionOnSlope() {
		return this.positionOnSlope;
	}

	public void setPositionOnSlope(String positionOnSlope) {
		this.positionOnSlope = positionOnSlope;
	}

	public String getRangelandUse() {
		return this.rangelandUse;
	}

	public void setRangelandUse(String rangelandUse) {
		this.rangelandUse = rangelandUse;
	}

	public String getRoadType() {
		return this.roadType;
	}

	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTimeOfDay() {
		return this.timeOfDay;
	}

	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
	}

	public String getTopographicFeature() {
		return this.topographicFeature;
	}

	public void setTopographicFeature(String topographicFeature) {
		this.topographicFeature = topographicFeature;
	}

	public String getTrailType() {
		return this.trailType;
	}

	public void setTrailType(String trailType) {
		this.trailType = trailType;
	}

	public Integer getUtmE() {
		return this.utmE;
	}

	public void setUtmE(Integer utmE) {
		this.utmE = utmE;
	}

	public Integer getUtmN() {
		return this.utmN;
	}

	public void setUtmN(Integer utmN) {
		this.utmN = utmN;
	}

	public String getVegetationType() {
		return this.vegetationType;
	}

	public void setVegetationType(String vegetationType) {
		this.vegetationType = vegetationType;
	}

	public List<CameraMonitor> getCameraMonitors1() {
		return this.cameraMonitors;
	}

	public void setCameraMonitors1(List<CameraMonitor> cameraMonitors1) {
		this.cameraMonitors = cameraMonitors1;
	}

	public CameraMonitor addCameraMonitors1(CameraMonitor cameraMonitors1) {
		getCameraMonitors1().add(cameraMonitors1);
		cameraMonitors1.setDeployment(this);

		return cameraMonitors1;
	}

	public CameraMonitor removeCameraMonitors1(CameraMonitor cameraMonitors1) {
		getCameraMonitors1().remove(cameraMonitors1);
		cameraMonitors1.setDeployment(null);

		return cameraMonitors1;
	}

	public List<Photo> getPhotos() {
		return this.photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public Photo addPhoto(Photo photo) {
		getPhotos().add(photo);
		photo.setDeployment(this);

		return photo;
	}

	public Photo removePhoto(Photo photo) {
		getPhotos().remove(photo);
		photo.setDeployment(null);

		return photo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUtmDatum() {
		return utmDatum;
	}

	public void setUtmDatum(Integer utmDatum) {
		this.utmDatum = utmDatum;
	}

	public Integer getUtmZone() {
		return utmZone;
	}

	public void setUtmZone(Integer utmZone) {
		this.utmZone = utmZone;
	}

	public List<CameraMonitor> getCameraMonitors() {
		return cameraMonitors;
	}

	public void setCameraMonitors(List<CameraMonitor> cameraMonitors) {
		this.cameraMonitors = cameraMonitors;
	}

	public StudyArea getStudyArea() {
		return studyArea;
	}

	public void setStudyArea(StudyArea studyArea) {
		this.studyArea = studyArea;
	}

}