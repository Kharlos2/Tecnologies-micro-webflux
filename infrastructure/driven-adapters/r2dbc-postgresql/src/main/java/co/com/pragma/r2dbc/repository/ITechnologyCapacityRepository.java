package co.com.pragma.r2dbc.repository;

import co.com.pragma.r2dbc.entities.TechnologyCapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ITechnologyCapacityRepository extends ReactiveCrudRepository<TechnologyCapacityEntity, Long> {
}
