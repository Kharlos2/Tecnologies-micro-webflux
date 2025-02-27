package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entities.TechnologyCapacityEntity;
import co.com.pragma.r2dbc.entities.TechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ITechnologyCapacityRepository extends ReactiveCrudRepository<TechnologyCapacityEntity, Long> {

    @Query("""
        SELECT t.id, t.name, t.description
        FROM public.technologies t
        INNER JOIN public.capacity_technology ct ON t.id = ct.technology_id
        WHERE ct.capacity_id = :capacityId
    """)
    Flux<TechnologyEntity> findByCapacityId(Long capacityId);

}
