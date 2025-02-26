package co.com.pragma.usecase.technology;

import co.com.pragma.model.technology.Technology;
import co.com.pragma.model.technology.exceptions.CustomException;
import co.com.pragma.model.technology.exceptions.ExceptionsEnum;
import co.com.pragma.model.technology.spi.ITechnologyPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class TechnologyUseCaseTest {


    private ITechnologyPersistencePort technologyPersistencePort;
    private TechnologyUseCase technologyUseCase;

    @BeforeEach
    void setUp() {
        technologyPersistencePort = mock(ITechnologyPersistencePort.class);
        technologyUseCase = new TechnologyUseCase(technologyPersistencePort);
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
}