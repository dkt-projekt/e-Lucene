package eu.freme.common.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import eu.freme.common.persistence.model.LuceneIndex;

/**
 * @author Julian Moreno Schneider jumo04@dfki.de
 */
@Repository
public interface IndexRepository extends CrudRepository<LuceneIndex, Long> {

	public List<LuceneIndex> findAll();

	public LuceneIndex findOneByIndexId(String indexId);

//	public void deleteByIndexId(String indexId);
}
