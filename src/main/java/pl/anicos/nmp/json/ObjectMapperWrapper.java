package pl.anicos.nmp.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperWrapper {
    private final ObjectMapper objectMapper;
    private final ObjectMapperValidator objectMapperValidator;

    @Autowired
    public ObjectMapperWrapper(ObjectMapper objectMapper, ObjectMapperValidator objectMapperValidator) {
        this.objectMapper = objectMapper;
        this.objectMapperValidator = objectMapperValidator;
    }

    public <T> T treeToValue(ObjectNode jsonNodes, Class<T> clazz) {
        T result = treeToValueWithoutValidation(jsonNodes, clazz);
        objectMapperValidator.validate(result);
        return result;
    }

    private <T> T treeToValueWithoutValidation(ObjectNode jsonNodes, Class<T> clazz) {
        try {
            T result = objectMapper.treeToValue(jsonNodes, clazz);
            return result;
        } catch (JsonProcessingException e) {
            throw new JsonProcessingRuntimeException("Can't parse bode of response", e);
        }
    }

    public byte[] writeValueAsBytes(ObjectNode newJson) {
        try {
            return objectMapper.writeValueAsBytes(newJson);
        } catch (JsonProcessingException e) {
            throw new JsonProcessingRuntimeException("Can't createArtifactoryLocation json binary from object", e);
        }
    }
}
