package org.rmcc.ccc.model;

import com.dropbox.core.v2.files.Metadata;
import org.apache.commons.csv.CSVRecord;
import org.rmcc.ccc.controller.PhotoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the "Photos" database table.
 * 
 */
@Entity
@Table(name = "photos")
@NamedQuery(name = "Photo.findAll", query = "SELECT p FROM Photo p")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Photo implements Serializable, BaseModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(Photo.class);

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PHOTOS_IMAGEID_GENERATOR", sequenceName = "PHOTOS_ID_SEQ", allocationSize=1)
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

	@Column(name = "orig_dropbox_path", length = 4000)
	private String origDropboxPath;

	@Column(name = "highlight")
	private Boolean highlight;

	@Column(name = "image_date")
	private Timestamp imageDate;

	// @Column(name="species", length=255)
	// private String species;

	// bi-directional many-to-one association to Deployment
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deployment_id")
	private Deployment deployment;

	@Transient
	private String deploymentId;

	// bi-directional many-to-one association to Detection
	@OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Detection> detections;

	@Transient
	private Metadata metadata;

	public Photo() {
	}

	public Photo(Integer imageId, String imageDate, String fileName, String filePath, String speciesId, String directionOfTravel, String highlight, String deploymentID) {
		this.id = imageId;
		this.imageDate = imageDate != null && !"".equalsIgnoreCase(imageDate) ? convertToTimestamp(imageDate) : null;
		this.fileName = fileName;
		this.filePath = filePath;
		this.origDropboxPath = convertToDropboxPath(filePath) + fileName;
		this.dropboxPath = convertToDropboxPath(filePath) + fileName;
		this.directionOfTravel = directionOfTravel;
		this.highlight = highlight != null && !"".equalsIgnoreCase(highlight) ? Boolean.valueOf(highlight) : null;
		this.deploymentId = deploymentID;
	}

	private String convertToDropboxPath(String filePath) {
		String rootPath = PhotoController.ARCHIVED_ROOT.replace("/", "\\");
		Integer rootIndex = filePath.toLowerCase().indexOf(rootPath);
		String dropboxPath = null;
		if (rootIndex > -1) {
			dropboxPath = PhotoController.ARCHIVED_ROOT + filePath.substring(rootIndex + PhotoController.ARCHIVED_ROOT.length());
			dropboxPath = dropboxPath.replace("\\","/").toLowerCase();
		}
		return dropboxPath;
	}

	private Timestamp convertToTimestamp(String date) {
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			Date parsedDate = dateFormat.parse(date);
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
			return timestamp;
		} catch (Exception e) {
			LOGGER.error("Error converting date string to timestamp for date string: " + date, e);
		}
		return null;
	}

	public String getDirectionOfTravel() {
		return this.directionOfTravel;
	}

	public void setDirectionOfTravel(String directionOfTravel) {
		this.directionOfTravel = directionOfTravel;
	}

	public String getFileName() {
		return fileName;
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

	public String getOrigDropboxPath() {
		return origDropboxPath;
	}

	public void setOrigDropboxPath(String origDropboxPath) {
		this.origDropboxPath = origDropboxPath;
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

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
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

	@Override
	public String[] getFileHeaderMappings() {
		return new String[]{"ImageID","ImageDate","FileName","FilePath","Species","DirectionOfTravel","Highlight","DeploymentID"};
	}

	public String getDataImportFileName() {
		return "Photos.csv";
	}

	@Override
	public BaseModel getFromCsvRecord(CSVRecord record) {
		Integer imageId = null;
		try { imageId = Integer.parseInt(record.get("ImageID")); } catch (NumberFormatException e) {}
		if (imageId != null) {
			Photo photo = new Photo(imageId,
					record.get("ImageDate"),
					record.get("FileName"),
					record.get("FilePath"),
					record.get("Species"),
					record.get("DirectionOfTravel"),
					record.get("Highlight"),
					record.get("DeploymentID"));
			return photo;
		}
		return null;
	}

}