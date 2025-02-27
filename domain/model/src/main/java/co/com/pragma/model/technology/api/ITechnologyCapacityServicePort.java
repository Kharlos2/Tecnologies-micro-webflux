package co.com.pragma.model.technology.api;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyCapacityServicePort {

    Mono<Void> saveTechnologyCapacities(CapacityWithTechnologies capacityWithTechnologies);
    Flux<Technology> findTechnologiesByCapacity(Long capacityId);
}
