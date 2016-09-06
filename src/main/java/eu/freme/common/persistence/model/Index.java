package eu.freme.common.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 * @author Julian Moreno Schneider jumo04@dfki.de
 *
 */
@Entity
public class Index {

	public enum Status {
		CREATED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Integer id;

//	@JoinColumn(name = "indexId")
	String indexId;
	String fields;
	String analyzers;
	Status status;
	
	String language;

	Date creationTime;

	public Index() {
	}

	public Index(Integer id, String indexId, String fields, String analyzers, Status status, String language,
			Date creationTime) {
		super();
		this.id = id;
		this.indexId = indexId;
		this.fields = fields;
		this.analyzers = analyzers;
		this.status = status;
		this.language = language;
		this.creationTime = creationTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndexId() {
		return indexId;
	}

	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getAnalyzers() {
		return analyzers;
	}

	public void setAnalyzers(String analyzers) {
		this.analyzers = analyzers;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
