package co.com.pragma.model.technology.spi;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyCapacityPersistencePort {

    Mono<Void> saveTechnologyCapacity(CapacityWithTechnologies capacityWithTechnologies);

}
