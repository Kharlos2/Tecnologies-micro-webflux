package co.com.pragma.model.technology.spi;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyCapacityPersistencePort {

    Mono<Void> saveTechnologyCapacity(CapacityWithTechnologies capacityWithTechnologies);
    Flux<Technology> findTechnologiesByCapacity(Long capacityId);

}
