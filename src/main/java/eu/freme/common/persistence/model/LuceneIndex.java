package eu.freme.common.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * @author Julian Moreno Schneider jumo04@dfki.de
 *
 */
@Entity
public class LuceneIndex {

	public enum Status {
		CREATED
	}

//	@GeneratedValue(strategy = GenerationType.AUTO)
//	Integer id;

//	@JoinColumn(name = "indexId")
	@Id
	String indexId;
	String fields;
	String analyzers;
	Status status;
	
	String language;

	Date creationTime;

	public LuceneIndex() {
	}

	public LuceneIndex(String indexId, String fields, String analyzers, Status status, String language,
			Date creationTime) {
		super();
		this.indexId = indexId;
		this.fields = fields;
		this.analyzers = analyzers;
		this.status = status;
		this.language = language;
		this.creationTime = creationTime;
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
