package org.rmcc.ccc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.commons.csv.CSVRecord;


/**
 * The persistent class for the "Deployments" database table.
 * 
 */
@Entity
@Table(name="deployments")
@NamedQuery(name="Deployment.findAll", query="SELECT d FROM Deployment d")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Deployment implements Serializable, BaseModel {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DEPLOYMENTS_DEPLOYMENTID_GENERATOR", sequenceName="DEPLOYMENTS_ID_SEQ", allocationSize=1)
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

	@Column(name="dropbox_path")
	private String dropboxPath;

	//bi-directional many-to-one association to CameraMonitor
	@OneToMany(mappedBy="deployment")
	@JsonIgnore
	private List<CameraMonitor> cameraMonitors;

	//bi-directional many-to-one association to StudyArea
	@ManyToOne
	@JoinColumn(name="study_area_id")
	private StudyArea studyArea;
	@Transient
	private String studyAreaId;

	//bi-directional many-to-one association to Photo
	@OneToMany(mappedBy="deployment")
	@JsonIgnore
	private List<Photo> photos;

	public Deployment() {
	}

	public Deployment(Integer deploymentId, String studyAreaID, String locationID, String ownership, String utm_e,
					  String utm_n, String utm_zone, String utm_datum, String startDate, String endDate, String timeOfDay,
					  String dominantSubstrate, String trailType, String positionOnSlope, String habitatRuggedness,
					  String topographicFeature, String vegetationType, String rangelandUse, String humanVisitation,
					  String distanceToHumanHabitat, String humanHabitatType, String distanceToRoad, String roadType,
					  String azimuth, String notes) {
		this.id = deploymentId;
		this.studyAreaId = studyAreaID;
		this.locationID = locationID;
		this.ownership = ownership;
		this.utmE = utm_e != null && !"".equalsIgnoreCase(utm_e) ? Integer.parseInt(utm_e) : null;
		this.utmN = utm_n != null && !"".equalsIgnoreCase(utm_n) ? Integer.parseInt(utm_n) : null;
		this.utmZone = utm_zone != null && !"".equalsIgnoreCase(utm_zone) ? Integer.parseInt(utm_zone) : null;
		this.utmDatum = utm_datum != null && !"".equalsIgnoreCase(utm_datum) ? Integer.parseInt(utm_datum) : null;
		this.startDate = startDate != null && !"".equalsIgnoreCase(startDate) ? convertToTimestamp(startDate) : null;
		this.endDate = endDate != null && !"".equalsIgnoreCase(endDate) ? convertToTimestamp(endDate) : null;
		this.timeOfDay = timeOfDay;
		this.dominantSubstrate = dominantSubstrate;
		this.trailType = trailType;
		this.positionOnSlope = positionOnSlope;
		this.habitatRuggedness = habitatRuggedness;
		this.topographicFeature = topographicFeature;
		this.vegetationType = vegetationType;
		this.rangelandUse = rangelandUse;
		this.humanVisitation = humanVisitation;
		this.distanceToHumanHabitat = distanceToHumanHabitat;
		this.humanHabitatType = humanHabitatType;
		this.distanceToRoad = distanceToRoad;
		this.roadType = roadType;
		this.azimuth = azimuth;
		this.notes = notes;
	}

	private Timestamp convertToTimestamp(String date) {
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			Date parsedDate = dateFormat.parse(date);
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			return timestamp;
		}catch(Exception e){//this generic but you can control another types of exception
			e.printStackTrace();
		}
		return null;
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

	public String getDropboxPath() {
		return dropboxPath;
	}

	public void setDropboxPath(String dropboxPath) {
		this.dropboxPath = dropboxPath;
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
	
	public String getStudayAreaName() {
		return this.studyArea != null ? this.studyArea.getName() : null;
	}

	public String getStudyAreaId() {
		return studyAreaId;
	}

	public void setStudyAreaId(String studyAreaId) {
		this.studyAreaId = studyAreaId;
	}

	@Override
	public String[] getFileHeaderMappings() {
		return new String[]{"DeploymentID","StudyAreaID","LocationID","Ownership","UTM_E","UTM_N","UTM_Zone","UTM_Datum",
				"StartDate","EndDate","TimeOfDay","DominantSubstrate","TrailType","PositionOnSlope","HabitatRuggedness",
				"TopographicFeature","VegetationType","RangelandUse","HumanVisitation","DistanceToHumanHabitat",
				"HumanHabitatType","DistanceToRoad","RoadType","Azimuth","Notes"};
	}

	@Override
	public String getFileName() {
		return "Deployments_lory.csv";
	}

	@Override
	public BaseModel getFromCsvRecord(CSVRecord record) {
		Integer deploymentId = null;
		try { deploymentId = Integer.parseInt(record.get("DeploymentID")); } catch (NumberFormatException e) {}
		if (deploymentId != null) {
			Deployment deployment = new Deployment(deploymentId,
					record.get("StudyAreaID"),
					record.get("LocationID"),
					record.get("Ownership"),
					record.get("UTM_E"),
					record.get("UTM_N"),
					record.get("UTM_Zone"),
					record.get("UTM_Datum"),
					record.get("StartDate"),
					record.get("EndDate"),
					record.get("TimeOfDay"),
					record.get("DominantSubstrate"),
					record.get("TrailType"),
					record.get("PositionOnSlope"),
					record.get("HabitatRuggedness"),
					record.get("TopographicFeature"),
					record.get("VegetationType"),
					record.get("RangelandUse"),
					record.get("HumanVisitation"),
					record.get("DistanceToHumanHabitat"),
					record.get("HumanHabitatType"),
					record.get("DistanceToRoad"),
					record.get("RoadType"),
					record.get("Azimuth"),
					record.get("Notes"));
			return deployment;
		}
		return null;
	}
}