package co.com.pragma.usecase.technology;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.spi.ITechnologyCapacityPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class TechnologyCapacityUseCaseTest {

    @Mock
    private ITechnologyCapacityPersistencePort technologyCapacityPersistencePort;

    @InjectMocks
    private TechnologyCapacityUseCase technologyCapacityUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTechnologyCapacities_ShouldCompleteSuccessfully() {
        // Given
        CapacityWithTechnologies capacityWithTechnologies = new CapacityWithTechnologies(1L,List.of(1L, 2L, 3L));

        when(technologyCapacityPersistencePort.saveTechnologyCapacity(capacityWithTechnologies)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = technologyCapacityUseCase.saveTechnologyCapacities(capacityWithTechnologies);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(technologyCapacityPersistencePort, times(1)).saveTechnologyCapacity(capacityWithTechnologies);
    }

    @Test
    void findTechnologiesByCapacity_ShouldReturnTechnologies() {
        // Given
        Long capacityId = 10L;
        Technology tech1 = new Technology(1L, "Java", "a");
        Technology tech2 = new Technology(2L, "Python", "a");

        when(technologyCapacityPersistencePort.findTechnologiesByCapacity(capacityId))
                .thenReturn(Flux.just(tech1, tech2));

        // When
        Flux<Technology> result = technologyCapacityUseCase.findTechnologiesByCapacity(capacityId);

        // Then
        StepVerifier.create(result)
                .expectNext(tech1)
                .expectNext(tech2)
                .verifyComplete();

        verify(technologyCapacityPersistencePort, times(1)).findTechnologiesByCapacity(capacityId);
    }

    @Test
    void findTechnologiesByCapacity_NoTechnologiesFound_ShouldReturnEmptyFlux() {
        // Given
        Long capacityId = 20L;

        when(technologyCapacityPersistencePort.findTechnologiesByCapacity(capacityId))
                .thenReturn(Flux.empty());

        // When
        Flux<Technology> result = technologyCapacityUseCase.findTechnologiesByCapacity(capacityId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(technologyCapacityPersistencePort, times(1)).findTechnologiesByCapacity(capacityId);
    }
}