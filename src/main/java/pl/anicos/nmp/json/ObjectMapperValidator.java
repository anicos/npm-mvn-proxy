package pl.anicos.nmp.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.json.exception.JsonModelValidateException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ObjectMapperValidator {

    private final Validator validator;

    @Autowired
    public ObjectMapperValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations.size() > 0) {
            StringBuilder message = new StringBuilder();
            violations.forEach(a -> message.append(a.toString()));

            throw new JsonModelValidateException(message.toString());
        }
    }
}
