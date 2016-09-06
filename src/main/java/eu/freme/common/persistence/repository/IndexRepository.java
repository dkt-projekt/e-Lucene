package eu.freme.common.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import eu.freme.common.persistence.model.Index;

/**
 * @author Julian Moreno Schneider jumo04@dfki.de
 */
public interface IndexRepository extends CrudRepository<Index, Long> {

	public List<Index> findAll();

	public Index findOneByIndexId(String indexId);

//	public void deleteByIndexId(String indexId);
}
