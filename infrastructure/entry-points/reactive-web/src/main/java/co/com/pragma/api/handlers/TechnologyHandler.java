package co.com.pragma.api.handlers;

import co.com.pragma.api.dto.capacity.CheckTechnologiesRequestDTO;
import co.com.pragma.api.dto.save.SaveRequestDTO;
import co.com.pragma.api.dto.save.SaveResponseDTO;
import co.com.pragma.api.mapper.ITechnologiesMapper;
import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import co.com.pragma.model.technology.models.PagedResponseTechnologies;
import co.com.pragma.model.technology.models.Technology;
import co.com.pragma.model.technology.models.ValidationResponse;
import co.com.pragma.model.technology.api.ITechnologyServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TechnologyHandler {

    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologiesMapper saveMapper;

    public TechnologyHandler(ITechnologyServicePort technologyServicePort, ITechnologiesMapper saveMapper) {
        this.technologyServicePort = technologyServicePort;
        this.saveMapper = saveMapper;
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {

        Mono<Technology> technologyMono = serverRequest.bodyToMono(SaveRequestDTO.class).map(saveMapper::requestToModel);
        return technologyMono.flatMap(p -> ServerResponse.status(HttpStatusCode.valueOf(201)).contentType(MediaType.APPLICATION_JSON)
                .body((technologyServicePort.save(p)), SaveResponseDTO.class));
    }

    public Mono<ServerResponse> getAllTechnologies(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortDirection = request.queryParam("sort").orElse("asc");

        Mono<PagedResponseTechnologies> technologies = technologyServicePort.getTechnologiesPaginated(page, size, sortDirection);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(technologies, Technology.class);
    }

    public Mono<ServerResponse> checkTechnologies(ServerRequest request){

        Mono<CapacityWithTechnologies> technologyMono = request.bodyToMono(CheckTechnologiesRequestDTO.class).map((saveMapper::checkToModel));
        return technologyMono.flatMap(technologyCapacity ->
                ServerResponse.status(HttpStatusCode.valueOf(201))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(technologyServicePort.checkTechnologies(technologyCapacity), ValidationResponse.class));
    }
}
