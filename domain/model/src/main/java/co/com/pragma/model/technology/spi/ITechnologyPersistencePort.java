package co.com.pragma.model.technology.spi;

import co.com.pragma.model.technology.models.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {

    Mono<Technology> save(Technology technology);
    Mono<Technology> findByName(String name);
    Flux<Technology> findAllPaginated(int page, int size, String sortDirection);
    Mono<Long> countTechnologies();
    Mono<Technology> findById(Long id);
}
