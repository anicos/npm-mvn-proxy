package pl.anicos.nmp.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import spock.lang.Specification


class ObjectMapperWrapperSpec extends Specification {
    ObjectMapper objectMapper = Mock()

    ObjectMapperWrapper testObj = new ObjectMapperWrapper(objectMapper)

    ObjectNode jsonNodes = Mock()

    def "should rethrow runtime exception when treeToValue"() {
        given:
        objectMapper.treeToValue(jsonNodes, String) >> { throw new JsonProcessingException(new RuntimeException()) }

        when:
        testObj.treeToValue(jsonNodes, String)
        then:
        JsonProcessingRuntimeException exception = thrown();
        exception.cause instanceof JsonProcessingException
    }

    def "should rethrow runtime exception when writeValueAsBytes"() {
        given:
        objectMapper.writeValueAsBytes(jsonNodes) >> { throw new JsonProcessingException(new RuntimeException()) }

        when:
        testObj.writeValueAsBytes(jsonNodes,)
        then:
        JsonProcessingRuntimeException exception = thrown();
        exception.cause instanceof JsonProcessingException
    }
}
