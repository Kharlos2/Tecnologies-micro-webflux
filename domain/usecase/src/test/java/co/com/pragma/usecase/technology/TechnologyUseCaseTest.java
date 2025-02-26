package co.com.pragma.usecase.technology;

import co.com.pragma.model.technology.PagedResponse;
import co.com.pragma.model.technology.Technology;
import co.com.pragma.model.technology.exceptions.CustomException;
import co.com.pragma.model.technology.exceptions.ExceptionsEnum;
import co.com.pragma.model.technology.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TechnologyUseCaseTest {


    private ITechnologyPersistencePort technologyPersistencePort;
    private TechnologyUseCase technologyUseCase;


    @BeforeEach
    void setUp() {
        technologyPersistencePort = mock(ITechnologyPersistencePort.class);
        technologyUseCase = new TechnologyUseCase(technologyPersistencePort);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnErrorWhenNameIsInvalid() {
        Technology tech = new Technology();
        tech.setName(""); // Nombre vacío
        tech.setDescription("Valid description");

        StepVerifier.create(technologyUseCase.save(tech))
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        ((CustomException) throwable).getStatus() == ExceptionsEnum.INVALID_NAME.getHttpStatus())
                .verify();
    }

    @Test
    void shouldReturnErrorWhenDescriptionIsInvalid() {
        Technology tech = new Technology();
        tech.setName("Valid Name");
        tech.setDescription(""); // Descripción vacía

        StepVerifier.create(technologyUseCase.save(tech))
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        ((CustomException) throwable).getStatus() == ExceptionsEnum.INVALID_DESCRIPTION.getHttpStatus())
                .verify();
    }

    @Test
    void shouldReturnErrorWhenTechnologyAlreadyExists() {
        Technology tech = new Technology();
        tech.setName("ExistingTech");
        tech.setDescription("Valid description");

        when(technologyPersistencePort.findByName(tech.getName())).thenReturn(Mono.just(tech));

        StepVerifier.create(technologyUseCase.save(tech))
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        ((CustomException) throwable).getStatus() == ExceptionsEnum.ALREADY_EXIST.getHttpStatus())
                .verify();

        verify(technologyPersistencePort).findByName(tech.getName());
    }

    @Test
    void shouldSaveTechnologyWhenValid() {
        Technology tech = new Technology();
        tech.setName("NewTech");
        tech.setDescription("Valid description");

        when(technologyPersistencePort.findByName(tech.getName())).thenReturn(Mono.empty());
        when(technologyPersistencePort.save(any(Technology.class))).thenReturn(Mono.just(tech));

        StepVerifier.create(technologyUseCase.save(tech))
                .expectNext(tech)
                .verifyComplete();

        verify(technologyPersistencePort).findByName(tech.getName());
        verify(technologyPersistencePort).save(tech);
    }



    @Test
    void getTechnologiesPaginated_ShouldReturnPagedResponse() {
        // 🔹 Datos de prueba
        int page = 0;
        int size = 2;
        String sortDirection = "asc";
        long totalRecords = 5;
        List<Technology> mockTechnologies = List.of(
                new Technology(Long.parseLong("1"),"Java", "Lenguaje de programación"),
                new Technology(Long.parseLong("2"),"Python", "Lenguaje de scripting")
        );

        // 🔹 Mock de los métodos
        when(technologyPersistencePort.countTechnologies()).thenReturn(Mono.just(totalRecords));
        when(technologyPersistencePort.findAllPaginated(page, size, sortDirection))
                .thenReturn(Flux.fromIterable(mockTechnologies));


        Mono<PagedResponse<Technology>> resultMono = technologyUseCase.getTechnologiesPaginated(page, size, sortDirection);

        // 🔹 Verificar con StepVerifier
        StepVerifier.create(resultMono)
                .assertNext(pagedResponse -> {
                    assertEquals(totalRecords, pagedResponse.getCount());
                    assertEquals(page, pagedResponse.getPage());
                    assertEquals(size, pagedResponse.getSize());
                    assertEquals(mockTechnologies.size(), pagedResponse.getItems().size());
                    assertEquals("Java", pagedResponse.getItems().get(0).getName());
                    assertEquals("Python", pagedResponse.getItems().get(1).getName());
                })
                .verifyComplete();

        // 🔹 Verificar llamadas a los mocks
        verify(technologyPersistencePort, times(1)).countTechnologies();
        verify(technologyPersistencePort, times(1)).findAllPaginated(page, size, sortDirection);
    }
}