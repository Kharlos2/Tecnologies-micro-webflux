package co.com.pragma.r2dbc.service;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.spi.ITechnologyCapacityPersistencePort;
import co.com.pragma.r2dbc.entities.TechnologyCapacityEntity;
import co.com.pragma.r2dbc.mapper.ITechnologyMapper;
import co.com.pragma.r2dbc.repository.ITechnologyCapacityRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class TechnologyCapacityPersistenceAdapter implements ITechnologyCapacityPersistencePort {

    private final ITechnologyCapacityRepository technologyCapacityRepository;
    private final ITechnologyMapper technologyMapper;

    public TechnologyCapacityPersistenceAdapter(ITechnologyCapacityRepository technologyCapacityRepository, ITechnologyMapper technologyMapper) {
        this.technologyCapacityRepository = technologyCapacityRepository;
        this.technologyMapper = technologyMapper;
    }


    @Override
    public Mono<Void> saveTechnologyCapacity(CapacityWithTechnologies capacityWithTechnologies) {
        List<TechnologyCapacityEntity> entities = capacityWithTechnologies.getTechnologiesIds()
                .stream()
                .map(technologyId -> new TechnologyCapacityEntity(technologyId, capacityWithTechnologies.getCapacityId()))
                .toList();

        return technologyCapacityRepository.saveAll(entities).then();
    }

    @Override
    public Flux<Technology> findTechnologiesByCapacity(Long capacityId) {
        return technologyCapacityRepository.findByCapacityId(capacityId).map(technologyMapper::toModel);
    }
}
