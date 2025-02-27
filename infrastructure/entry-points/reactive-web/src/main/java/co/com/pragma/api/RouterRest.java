package co.com.pragma.api;

import co.com.pragma.api.handlers.TechnologyCapacityHandler;
import co.com.pragma.api.handlers.TechnologyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandler technologyHandler) {
        return route(
                GET("/api/technologies"), technologyHandler::getAllTechnologies)
                .andRoute(POST("/api/technologies"), technologyHandler::create)
                .andRoute(POST("/api/validation-technologies"), technologyHandler::checkTechnologies)
                ;
    }
    @Bean
    public RouterFunction<ServerResponse> routerTechnologyCapacityFunction (TechnologyCapacityHandler technologyCapacityHandler){
        return route(
                POST("/api/technologies-capacity"), technologyCapacityHandler::saveTechnologyCapacities
        );
    }
}
