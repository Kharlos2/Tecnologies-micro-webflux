package co.com.pragma.model.technology.api;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import reactor.core.publisher.Mono;

public interface ITechnologyCapacityServicePort {

    Mono<Void> saveTechnologyCapacities(CapacityWithTechnologies capacityWithTechnologies);

}
