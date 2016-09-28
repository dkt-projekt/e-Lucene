package eu.freme.common.persistence.dao;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.freme.common.persistence.model.LuceneIndex;
import eu.freme.common.persistence.repository.IndexRepository;

/**
 * Complex database functionality for documents
 * 
 * @author Julian Moreno Schneider jumo04@dfki.de
 */
@Component
public class IndexDAO {

	@Autowired
	IndexRepository indexRepository;

	@Autowired
	EntityManager entityManager;

	@Transactional
	public void deleteByIndexId(String indexId) {
		LuceneIndex index = indexRepository.findOneByIndexId(indexId);
		entityManager.remove(index);
	}
}
