package org.rmcc.ccc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.csv.CSVRecord;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "registered_users")
@NamedQueries({ @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email = ?1") })
public class User implements Serializable, BaseModel {
	private static final long serialVersionUID = 1L;

	@Transient
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@NotNull
	@Column(name = "full_name", nullable = false, unique = false)
	private String fullName;

	@NotNull
	@Email
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column
	private boolean enabled;

	@NotNull
	@Size(min = 8, max = 256, message = "Password length must be at least 8 characters long.")
	@JsonIgnore
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	public User() {
	}

	public User(String fullName, String email, String password, String active) {
		this.fullName = fullName;
		this.email = email;
		this.passwordHash = password;
		this.enabled = active.equalsIgnoreCase("Y") ? true : false;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean active) {
		this.enabled = active;
	}

	@Override
	@JsonIgnore
	public String toString() {
		return "User{" + "id=" + id + ", email='" + email.replaceFirst("@.*", "@***") + ", passwordHash='"
				+ passwordHash.substring(0, 10) + ", role=" + role + '}';
	}

	@Override
	@JsonIgnore
	public String[] getFileHeaderMappings() {
		return new String[] { "FullName", "Email", "Password", "Active" };
	}

	@Override
	@JsonIgnore
	public String getFileName() {
		return "User.csv";
	}

	@Override
	@JsonIgnore
	public User getFromCsvRecord(CSVRecord record) {
		return new User(record.get("FullName"), record.get("Email"), record.get("Password"), record.get("Active"));
	}

}
