package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entities.TechnologyEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyRepository extends ReactiveCrudRepository<TechnologyEntity, Long> {

    @Query("SELECT * FROM public.technologies WHERE name ILIKE :name")
    Mono<TechnologyEntity> findByName(String name);
    Flux<TechnologyEntity> findAllBy(PageRequest pageRequest);
}
