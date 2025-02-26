package co.com.pragma.model.technology.api;

import co.com.pragma.model.technology.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {

    Mono<Technology> save(Technology technology);
    Flux<Technology> getTechnologiesPaginated(int page, int size, String sortDirection);
}
