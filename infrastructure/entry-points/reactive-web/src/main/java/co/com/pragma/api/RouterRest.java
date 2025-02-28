package co.com.pragma.api;

import co.com.pragma.api.dto.ErrorModel;
import co.com.pragma.api.dto.capacity.CheckTechnologiesRequestDTO;
import co.com.pragma.api.dto.save.SaveRequestDTO;
import co.com.pragma.api.dto.technologycapacity.TechnologyForCapacityDTO;
import co.com.pragma.api.handlers.TechnologyCapacityHandler;
import co.com.pragma.api.handlers.TechnologyHandler;
import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.PagedResponseTechnologies;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.models.ValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/technologies",
                    beanClass = TechnologyHandler.class,
                    beanMethod = "getAllTechnologies",
                    method = RequestMethod.GET,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    operation = @Operation(
                            operationId = "getAllTechnologies",
                            parameters = {
                                    @Parameter(name = "page", description = "Número de página", schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", description = "Tamaño de la página", schema = @Schema(type = "integer", defaultValue = "10")),
                                    @Parameter(name = "sortOrder", description = "Orden de clasificación (asc/desc)", schema = @Schema(type = "string", defaultValue = "asc"))
                            },
                            summary = "Listar tecnologias con paginación",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Respusta correcta",
                                            content = @Content(schema = @Schema(implementation = PagedResponseTechnologies.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/technologies",
                    beanClass = TechnologyHandler.class,
                    beanMethod = "create",
                    method = RequestMethod.POST,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    operation = @Operation(
                            operationId = "create",
                            summary = "Crear technologias",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SaveRequestDTO.class))),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Creacion exitosa.",
                                            content = @Content(schema = @Schema(implementation = Technology.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Solicitud incorrecta",
                                            content = @Content(schema = @Schema(implementation = ErrorModel.class))
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/validation-technologies",
                    beanClass = TechnologyHandler.class,
                    beanMethod = "checkTechnologies",
                    method = RequestMethod.POST,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    operation = @Operation(
                            operationId = "checkTechnologies",
                            summary = "Valida si existen todas las technologias del array",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CheckTechnologiesRequestDTO.class))),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Validacion exitosa",
                                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "No encontro alguno de los ids",
                                            content = @Content(schema = @Schema(implementation = ValidationResponse.class))
                                    )
                            }
                    )

            )
    })
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandler technologyHandler) {
        return route(
                GET("/api/technologies"), technologyHandler::getAllTechnologies)
                .andRoute(POST("/api/technologies"), technologyHandler::create)
                .andRoute(POST("/api/validation-technologies"), technologyHandler::checkTechnologies)
                ;
    }
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/technologies-capacity",
                    beanClass = TechnologyCapacityHandler.class,
                    beanMethod = "saveTechnologyCapacities",
                    method = RequestMethod.POST,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    operation = @Operation(
                            operationId = "saveTechnologyCapacities",
                            summary = "Guarda la relacion entre capacidad tecnologias",
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CapacityWithTechnologies.class))),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Guardado exitoso",
                                            content = @Content(schema = @Schema(implementation = Void.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/technologies-capacity",
                    beanClass = TechnologyCapacityHandler.class,
                    beanMethod = "findTechnologiesByCapacity",
                    method = RequestMethod.GET,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    operation = @Operation(
                            operationId = "findTechnologiesByCapacity",
                            summary = "Busca todas las tecnologias por el id de una capacidad",
                            parameters = {
                                    @Parameter(name = "capacityId", description = "Id capacidad", schema = @Schema(type = "integer", defaultValue = "0")),
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Busqueda exitosa",
                                            content = @Content(schema = @Schema(implementation = TechnologyForCapacityDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "No encuentra resultados",
                                            content = @Content(schema = @Schema(implementation = TechnologyForCapacityDTO.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerTechnologyCapacityFunction (TechnologyCapacityHandler technologyCapacityHandler){
        return route(
                POST("/api/technologies-capacity"), technologyCapacityHandler::saveTechnologyCapacities)
                .andRoute(GET("/api/technologies-capacity"), technologyCapacityHandler::findTechnologiesByCapacity)
        ;
    }
}
