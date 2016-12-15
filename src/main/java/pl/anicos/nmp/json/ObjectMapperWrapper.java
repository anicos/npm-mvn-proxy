package pl.anicos.nmp.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperWrapper {
    private final ObjectMapper objectMapper;

    @Autowired
    public ObjectMapperWrapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T treeToValue(ObjectNode jsonNodes, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(jsonNodes, clazz);
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
