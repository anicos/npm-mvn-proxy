package pl.anicos.nmp.artifactory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VersionNumberNotExistException extends RuntimeException {

    public VersionNumberNotExistException(String message) {
        super(message);
    }
}
