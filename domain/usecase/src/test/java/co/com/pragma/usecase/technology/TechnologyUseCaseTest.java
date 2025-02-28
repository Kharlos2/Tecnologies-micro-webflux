package co.com.pragma.usecase.technology;

import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.PagedResponse;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.exceptions.CustomException;
import co.com.pragma.model.technology.exceptions.ExceptionsEnum;
import co.com.pragma.model.technology.models.ValidationResponse;
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


    private ITechnologyPersistencePort technologyService;
    private TechnologyUseCase technologyUseCase;


    @BeforeEach
    void setUp() {
        technologyService = mock(ITechnologyPersistencePort.class);
        technologyUseCase = new TechnologyUseCase(technologyService);
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

        when(technologyService.findByName(tech.getName())).thenReturn(Mono.just(tech));

        StepVerifier.create(technologyUseCase.save(tech))
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        ((CustomException) throwable).getStatus() == ExceptionsEnum.ALREADY_EXIST.getHttpStatus())
                .verify();

        verify(technologyService).findByName(tech.getName());
    }

    @Test
    void shouldSaveTechnologyWhenValid() {
        Technology tech = new Technology();
        tech.setName("NewTech");
        tech.setDescription("Valid description");

        when(technologyService.findByName(tech.getName())).thenReturn(Mono.empty());
        when(technologyService.save(any(Technology.class))).thenReturn(Mono.just(tech));

        StepVerifier.create(technologyUseCase.save(tech))
                .expectNext(tech)
                .verifyComplete();

        verify(technologyService).findByName(tech.getName());
        verify(technologyService).save(tech);
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
        when(technologyService.countTechnologies()).thenReturn(Mono.just(totalRecords));
        when(technologyService.findAllPaginated(page, size, sortDirection))
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
        verify(technologyService, times(1)).countTechnologies();
        verify(technologyService, times(1)).findAllPaginated(page, size, sortDirection);
    }

    @Test
    void checkTechnologies_AllExist_ShouldReturnValidResponse() {
        // Given
        List<Long> requestedIds = List.of(1L, 2L, 3L);
        CapacityWithTechnologies capacityWithTechnologies = new CapacityWithTechnologies(1L,requestedIds);

        when(technologyService.findById(1L)).thenReturn(Mono.just(new Technology(1L, "Tech1","a")));
        when(technologyService.findById(2L)).thenReturn(Mono.just(new Technology(2L, "Tech2","a")));
        when(technologyService.findById(3L)).thenReturn(Mono.just(new Technology(3L, "Tech3","a")));

        // When
        Mono<ValidationResponse> result = technologyUseCase.checkTechnologies(capacityWithTechnologies);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getValid() && response.getMessage().equals("All technologies exist"))
                .verifyComplete();

        verify(technologyService, times(1)).findById(1L);
        verify(technologyService, times(1)).findById(2L);
        verify(technologyService, times(1)).findById(3L);
    }

    @Test
    void checkTechnologies_SomeMissing_ShouldReturnInvalidResponse() {
        // Given
        List<Long> requestedIds = List.of(1L, 2L, 3L);
        CapacityWithTechnologies capacityWithTechnologies = new CapacityWithTechnologies(1L, requestedIds);

        when(technologyService.findById(1L)).thenReturn(Mono.just(new Technology(1L, "Tech1","a")));
        when(technologyService.findById(2L)).thenReturn(Mono.empty()); // No encontrado
        when(technologyService.findById(3L)).thenReturn(Mono.just(new Technology(3L, "Tech3","a")));

        // When
        Mono<ValidationResponse> result = technologyUseCase.checkTechnologies(capacityWithTechnologies);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        !response.getValid() && response.getMessage().contains("Missing technologies: [2]"))
                .verifyComplete();

        verify(technologyService, times(1)).findById(1L);
        verify(technologyService, times(1)).findById(2L);
        verify(technologyService, times(1)).findById(3L);
    }

    @Test
    void checkTechnologies_AllMissing_ShouldReturnInvalidResponse() {
        // Given
        List<Long> requestedIds = List.of(4L, 5L, 6L);
        CapacityWithTechnologies capacityWithTechnologies = new CapacityWithTechnologies(1L, requestedIds);

        when(technologyService.findById(anyLong())).thenReturn(Mono.empty()); // Ninguna tecnología encontrada

        // When
        Mono<ValidationResponse> result = technologyUseCase.checkTechnologies(capacityWithTechnologies);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        !response.getValid() && response.getMessage().contains("Missing technologies: [4, 5, 6]"))
                .verifyComplete();

        verify(technologyService, times(1)).findById(4L);
        verify(technologyService, times(1)).findById(5L);
        verify(technologyService, times(1)).findById(6L);
    }
}