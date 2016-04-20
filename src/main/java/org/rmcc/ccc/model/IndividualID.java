package org.rmcc.ccc.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;


/**
 * The persistent class for the "IndividualIDs" database table.
 * 
 */
@Entity
@Table(name="individual_ids")
@NamedQuery(name="IndividualID.findAll", query="SELECT i FROM IndividualID i")
public class IndividualID implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="INDIVIDUALIDS_ID_GENERATOR", sequenceName="INDIVIDUALIDS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INDIVIDUALIDS_ID_GENERATOR")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;

	@Column(name="animal_id", nullable=false, length=10)
	private String animalId;

	//bi-directional many-to-one association to Detection
	@ManyToOne
	@JoinColumn(name="detection_id")
	@JsonManagedReference(value="detection-indiv")
	private Detection detection;

	public IndividualID() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnimalId() {
		return this.animalId;
	}

	public void setAnimalId(String animalId) {
		this.animalId = animalId;
	}

	public Detection getDetection() {
		return this.detection;
	}

	public void setDetection(Detection detection) {
		this.detection = detection;
	}

}