package de.dkt.eservices.elucene;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import eu.freme.common.persistence.dao.IndexDAO;
/**
 * @author Jan Nehring - jan.nehring@dfki.de
 */
@Configuration
@ComponentScan
public class LuceneConfig {

//	@Bean
//	public ELuceneService getELuceneService(){
//		return new ELuceneService();
//	}
	
	@Bean
	public IndexDAO indexDAO(){
		return new IndexDAO();
	}
}

