package co.com.pragma.model.technology.api;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.PagedResponseTechnologies;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.models.ValidationResponse;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {

    Mono<Technology> save(Technology technology);
    Mono<PagedResponseTechnologies> getTechnologiesPaginated(int page, int size, String sortDirection);
    Mono<ValidationResponse> checkTechnologies(CapacityWithTechnologies capacityWithTechnologies);
}
