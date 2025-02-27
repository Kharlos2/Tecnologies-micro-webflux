package co.com.pragma.r2dbc.service;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.spi.ITechnologyCapacityPersistencePort;
import co.com.pragma.r2dbc.entities.TechnologyCapacityEntity;
import co.com.pragma.r2dbc.repository.ITechnologyCapacityRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class TechnologyCapacityPersistenceAdapter implements ITechnologyCapacityPersistencePort {

    private final ITechnologyCapacityRepository technologyCapacityRepository;

    public TechnologyCapacityPersistenceAdapter(ITechnologyCapacityRepository technologyCapacityRepository) {
        this.technologyCapacityRepository = technologyCapacityRepository;
    }


    @Override
    public Mono<Void> saveTechnologyCapacity(CapacityWithTechnologies capacityWithTechnologies) {
        List<TechnologyCapacityEntity> entities = capacityWithTechnologies.getTechnologiesIds()
                .stream()
                .map(technologyId -> new TechnologyCapacityEntity(technologyId, capacityWithTechnologies.getCapacityId()))
                .collect(Collectors.toList());

        return technologyCapacityRepository.saveAll(entities).then();
    }
}
