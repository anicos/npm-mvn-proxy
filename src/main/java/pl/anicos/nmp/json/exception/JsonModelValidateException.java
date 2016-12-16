package pl.anicos.nmp.json.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JsonModelValidateException extends RuntimeException {
    public JsonModelValidateException(String message) {
        super(message);
    }
}
