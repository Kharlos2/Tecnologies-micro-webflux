package co.com.pragma.api.handlers;

import co.com.pragma.api.dto.save.SaveRequestDTO;
import co.com.pragma.api.mapper.ISaveMapper;
import co.com.pragma.model.technology.PagedResponse;
import co.com.pragma.model.technology.Technology;
import co.com.pragma.model.technology.api.ITechnologyServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TechnologyHandler {

    private final ITechnologyServicePort technologyServicePort;
    private final ISaveMapper saveMapper;

    public TechnologyHandler(ITechnologyServicePort technologyServicePort, ISaveMapper saveMapper) {
        this.technologyServicePort = technologyServicePort;
        this.saveMapper = saveMapper;
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {

        Mono<Technology> technologyMono = serverRequest.bodyToMono(SaveRequestDTO.class).map(saveMapper::requestToModel);
        return technologyMono.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(technologyServicePort.save(p), Technology.class));
    }

    public Mono<ServerResponse> getAllTechnologies(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortDirection = request.queryParam("sort").orElse("asc");

        Mono<PagedResponse<Technology>> technologies = technologyServicePort.getTechnologiesPaginated(page, size, sortDirection);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(technologies, Technology.class);
    }
}
