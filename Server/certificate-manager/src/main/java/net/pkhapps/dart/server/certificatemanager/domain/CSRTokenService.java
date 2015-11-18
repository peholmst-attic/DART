package net.pkhapps.dart.server.certificatemanager.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface CSRTokenService {

    CSRToken generateNewToken();

    void validateToken(String tokenHash) throws NoSuchTokenException, TokenExpiredException;

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The token you tried to use does not exist")
    class NoSuchTokenException extends Exception {
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The token you tried to use has expired")
    class TokenExpiredException extends Exception {
    }
}
