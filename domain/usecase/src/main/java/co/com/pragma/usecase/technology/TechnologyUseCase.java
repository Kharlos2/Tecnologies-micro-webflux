package co.com.pragma.usecase.technology;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.PagedResponse;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.models.ValidationResponse;
import co.com.pragma.model.technology.api.ITechnologyServicePort;
import co.com.pragma.model.technology.exceptions.ExceptionsEnum;
import co.com.pragma.model.technology.spi.ITechnologyPersistencePort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import co.com.pragma.model.technology.exceptions.CustomException;

import java.util.List;


public class TechnologyUseCase implements ITechnologyServicePort {

    private final ITechnologyPersistencePort technologyService;

    public TechnologyUseCase(ITechnologyPersistencePort technologyService) {
        this.technologyService = technologyService;
    }

    @Override
    public Mono<Technology> save(Technology technology) {
        return Mono.defer(() -> {
            if (
                    technology.getName() == null ||
                            technology.getName().isBlank() ||
                            technology.getName().length() > 50) {
                return Mono.error(new CustomException(ExceptionsEnum.INVALID_NAME));
            }
            if (
                    technology.getDescription() == null ||
                            technology.getDescription().isBlank() ||
                            technology.getDescription().length() > 90) {
                return Mono.error(new CustomException(ExceptionsEnum.INVALID_DESCRIPTION));
            }

            return technologyService.findByName(technology.getName())
                    .hasElement()
                    .flatMap(exist -> Boolean.TRUE.equals(exist)
                            ? Mono.error(new CustomException(ExceptionsEnum.ALREADY_EXIST))
                            : technologyService.save(technology));
        });
    }

    @Override
    public Mono<PagedResponse<Technology>> getTechnologiesPaginated(int page, int size, String sortDirection) {
        Mono<Long> countRecords = technologyService.countTechnologies();
        Flux<Technology> technologiesFlux = technologyService.findAllPaginated(page, size, sortDirection);

        return Mono.zip(countRecords, technologiesFlux.collectList())
                .map(tuple -> new PagedResponse<>(tuple.getT1(), page, size, tuple.getT2()));
    }

    @Override
    public Mono<ValidationResponse> checkTechnologies(CapacityWithTechnologies capacityWithTechnologies) {
        List<Long> requestedIds = capacityWithTechnologies.getTechnologiesIds();

        return Flux.fromIterable(requestedIds)
                .flatMap(technologyService::findById) // Buscar en la BD
                .map(Technology::getId) // Obtener solo los IDs encontrados
                .collectList()
                .map(foundIds -> {
                    List<Long> missingIds = requestedIds.stream()
                            .filter(id -> !foundIds.contains(id))
                            .toList();

                    boolean isValid = missingIds.isEmpty();
                    String message = isValid ? "All technologies exist" : "Missing technologies: " + missingIds;

                    return new ValidationResponse(message, isValid);
                });
    }
}