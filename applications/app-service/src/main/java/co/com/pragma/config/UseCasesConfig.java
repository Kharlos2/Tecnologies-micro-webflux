package co.com.pragma.config;

import co.com.pragma.model.technology.api.ITechnologyServicePort;
import co.com.pragma.model.technology.spi.ITechnologyPersistencePort;
import co.com.pragma.r2dbc.mapper.ITechnologyMapper;
import co.com.pragma.r2dbc.repository.ITechnologyRepository;
import co.com.pragma.r2dbc.service.TechnologyPersistenceAdapter;
import co.com.pragma.usecase.technology.TechnologyUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        private final ITechnologyRepository technologyRepository;
        private final ITechnologyMapper technologyMapper;

        public UseCasesConfig(ITechnologyMapper technologyMapper, ITechnologyRepository technologyRepository) {
                this.technologyMapper = technologyMapper;
                this.technologyRepository = technologyRepository;
        }

        @Bean
        public ITechnologyPersistencePort technologyPersistencePort(){
                return new TechnologyPersistenceAdapter(technologyRepository,technologyMapper);
        }

        @Bean
        public  ITechnologyServicePort technologyServicePort(ITechnologyPersistencePort technologyPersistencePort){
                return new TechnologyUseCase(technologyPersistencePort);
        }



}
