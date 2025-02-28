package co.com.pragma.api.handlers;

import co.com.pragma.api.dto.technologycapacity.TechnologyForCapacityDTO;
import co.com.pragma.api.mapper.ITechnologiesMapper;
import co.com.pragma.model.technology.api.ITechnologyCapacityServicePort;
import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TechnologyCapacityHandler{

    private final ITechnologyCapacityServicePort technologyCapacityServicePort;
    private final ITechnologiesMapper technologiesMapper;

    public TechnologyCapacityHandler(ITechnologyCapacityServicePort technologyCapacityServicePort, ITechnologiesMapper technologiesMapper) {
        this.technologyCapacityServicePort = technologyCapacityServicePort;
        this.technologiesMapper = technologiesMapper;
    }


    public Mono<ServerResponse> saveTechnologyCapacities(ServerRequest request) {
        Mono<CapacityWithTechnologies> capacityWithTechnologies = request.bodyToMono(CapacityWithTechnologies.class);
        return capacityWithTechnologies.flatMap(capacityTechnologies ->
                ServerResponse.status(HttpStatusCode.valueOf(201))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(technologyCapacityServicePort.saveTechnologyCapacities(capacityTechnologies), Void.class));

    }
    public Mono<ServerResponse> findTechnologiesByCapacity(ServerRequest request){
        Long capacityId = Long.parseLong(request.queryParam("capacityId").orElse("0"));

        Flux<TechnologyForCapacityDTO> technologyFlux =
                technologyCapacityServicePort.findTechnologiesByCapacity(capacityId)
                .map(technologiesMapper::modelToTechnologyForCapacity);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(technologyFlux, TechnologyForCapacityDTO.class);
    }
}
