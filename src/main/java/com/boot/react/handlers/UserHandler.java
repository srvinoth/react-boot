package com.boot.react.handlers;

import com.boot.react.model.User;
import com.boot.react.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class UserHandler {

    @Autowired
    UserRepository userRepository;

    private static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(userRepository.findAll(), User.class);
    }

    public Mono<ServerResponse> getUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<User> userMono = userRepository.findById(id);
        return userMono.flatMap(user ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(user)))
                .switchIfEmpty(notFound);
    }


    public Mono<ServerResponse> addUser(ServerRequest serverRequest) {
        Mono<User> userWrapper = serverRequest.bodyToMono(User.class);
        return userWrapper.flatMap(
                user ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                                .body(userRepository.save(user), User.class)
        );

    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<User> userRequest = serverRequest.bodyToMono(User.class);
        Mono<User> userMono = userRepository.findById(id);
        Mono<User> updatedUser = userRequest.flatMap(user -> {
            userMono.flatMap(currentUser -> {
                currentUser.setCountry(user.getCountry());
                currentUser.setName(user.getName());
                currentUser.setSalary(user.getSalary());
                return userRepository.save(currentUser);
            });
            return userMono;
        });
        return updatedUser.flatMap(user ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(user))
        ).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Void> deleteUser = userRepository.deleteById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(deleteUser, Void.class);
    }

    public Mono<ServerResponse> exceptionExample(ServerRequest serverRequest) {
        throw new RuntimeException("RuntimeException occurred");
    }
}
