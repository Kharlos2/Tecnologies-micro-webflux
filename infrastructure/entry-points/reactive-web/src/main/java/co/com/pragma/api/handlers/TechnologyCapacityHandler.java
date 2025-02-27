package co.com.pragma.api.handlers;

import co.com.pragma.model.technology.api.ITechnologyCapacityServicePort;
import co.com.pragma.model.technology.models.CapacityWithTechnologies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TechnologyCapacityHandler{

    private final ITechnologyCapacityServicePort technologyCapacityServicePort;

    public TechnologyCapacityHandler(ITechnologyCapacityServicePort technologyCapacityServicePort) {
        this.technologyCapacityServicePort = technologyCapacityServicePort;
    }


    public Mono<ServerResponse> saveTechnologyCapacities(ServerRequest request) {
        System.out.println("1");
        Mono<CapacityWithTechnologies> capacityWithTechnologies = request.bodyToMono(CapacityWithTechnologies.class);
        return capacityWithTechnologies.flatMap(capacityTechnologies ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(technologyCapacityServicePort.saveTechnologyCapacities(capacityTechnologies), Void.class));

    }
}
