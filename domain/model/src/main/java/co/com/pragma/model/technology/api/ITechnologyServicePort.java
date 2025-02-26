package co.com.pragma.model.technology.api;

import co.com.pragma.model.technology.PagedResponse;
import co.com.pragma.model.technology.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {

    Mono<Technology> save(Technology technology);
    Mono<PagedResponse<Technology>> getTechnologiesPaginated(int page, int size, String sortDirection);
}
