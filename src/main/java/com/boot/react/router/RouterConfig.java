package com.boot.react.router;

import com.boot.react.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    private String CRICKETER_END_POINT = "/api/user/";

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return RouterFunctions.
                route(GET(CRICKETER_END_POINT)
                        .and(accept(MediaType.APPLICATION_JSON)), userHandler::getAllUsers)
                .andRoute(GET(CRICKETER_END_POINT + "id")
                        .and(accept(MediaType.APPLICATION_JSON)), userHandler::getUser)
                .andRoute(POST(CRICKETER_END_POINT)
                        .and(accept(MediaType.APPLICATION_JSON)), userHandler::addUser)
                .andRoute(PUT(CRICKETER_END_POINT + "id")
                        .and(accept(MediaType.APPLICATION_JSON)), userHandler::updateUser)
                .andRoute(DELETE(CRICKETER_END_POINT + "id")
                        .and(accept(MediaType.APPLICATION_JSON)), userHandler::deleteUser);
    }

    @Bean
    public RouterFunction<ServerResponse> errorRoute(UserHandler userHandler) {
        return RouterFunctions.route(GET("/runtimeexception").and(accept(MediaType.APPLICATION_JSON)), userHandler::exceptionExample);
    }
}
