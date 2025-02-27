package co.com.pragma.r2dbc.service;

import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.spi.ITechnologyPersistencePort;
import co.com.pragma.r2dbc.mapper.ITechnologyMapper;
import co.com.pragma.r2dbc.repository.ITechnologyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TechnologyPersistenceAdapter implements ITechnologyPersistencePort {

    private final ITechnologyRepository technologyRepository;
    private final ITechnologyMapper technologyMapper;

    public TechnologyPersistenceAdapter(ITechnologyRepository technologyRepository, ITechnologyMapper technologyMapper) {
        this.technologyRepository = technologyRepository;
        this.technologyMapper = technologyMapper;
    }

    @Override
    public Mono<Technology> save(Technology technology) {
        return technologyRepository.save(technologyMapper.toEntity(technology)).map(technologyMapper::toModel);
    }

    @Override
    public Mono<Technology> findByName(String name) {
        return technologyRepository.findByName(name).map(technologyMapper::toModel);
    }

    @Override
    public Flux<Technology> findAllPaginated(int page, int size, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, "name"));

        return technologyRepository.findAllBy(pageRequest)
                .map(technologyMapper::toModel);
    }

    @Override
    public Mono<Long> countTechnologies() {
        return technologyRepository.count();
    }

    @Override
    public Mono<Technology> findById(Long id) {
        return technologyRepository.findById(id).map(technologyMapper::toModel);
    }
}
