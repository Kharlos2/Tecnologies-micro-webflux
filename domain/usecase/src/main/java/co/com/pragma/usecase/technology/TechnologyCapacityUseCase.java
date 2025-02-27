package co.com.pragma.usecase.technology;

import co.com.pragma.model.technology.api.ITechnologyCapacityServicePort;
import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.spi.ITechnologyCapacityPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TechnologyCapacityUseCase implements ITechnologyCapacityServicePort {

    private final ITechnologyCapacityPersistencePort technologyCapacityPersistencePort;

    public TechnologyCapacityUseCase(ITechnologyCapacityPersistencePort technologyCapacityPersistencePort) {
        this.technologyCapacityPersistencePort = technologyCapacityPersistencePort;
    }

    @Override
    public Mono<Void> saveTechnologyCapacities(CapacityWithTechnologies capacityWithTechnologies) {
        return technologyCapacityPersistencePort.saveTechnologyCapacity(capacityWithTechnologies);
    }

    @Override
    public Flux<Technology> findTechnologiesByCapacity(Long capacityId) {
        return technologyCapacityPersistencePort.findTechnologiesByCapacity(capacityId);
    }


}
