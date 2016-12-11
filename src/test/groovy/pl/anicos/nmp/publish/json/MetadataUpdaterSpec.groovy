package pl.anicos.nmp.publish.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import groovy.json.JsonSlurper
import spock.lang.Specification

class MetadataUpdaterSpec extends Specification {
    ValuesGenerator valuesGenerator = Mock()
    ObjectMapper objectMapper = new ObjectMapper();
    MetadataUpdater testObj = new MetadataUpdater(new MetadataJsonUtil(valuesGenerator, new TarballLinkUpdater()));

    def "setup"() {
        valuesGenerator.getCurrentDate() >> '2016-09-06T22:57:02.844Z'
        valuesGenerator.generateUUID() >> '2-693d3a82a0499ca4a168c691b321e05c'
    }

    def "should update correct json before publish"() {
        given:
        def jsonAfterFirstPublish = getFileFromResources('jsonAfterFirstPublish.json');
        def jsonFromNpmInSecondPublish = getFileFromResources('jsonFromNpmInSecondPublish.json');
        def jsonAfterSecondPublish = getFileFromResources('jsonAfterSecondPublish.json');

        JsonNode afterFirstPublishNode = objectMapper.readTree(jsonAfterFirstPublish);
        JsonNode afterSecondPublish = objectMapper.readTree(jsonAfterSecondPublish);
        JsonNode fromNpmInSecondPublish = objectMapper.readTree(jsonFromNpmInSecondPublish);

        when:
        JsonNode result = testObj.update(fromNpmInSecondPublish, afterFirstPublishNode);
        then:
        convertToJsonSlurper(result) == convertToJsonSlurper(afterSecondPublish)
    }

    private Object convertToJsonSlurper(ObjectNode result) {
        new JsonSlurper().parseText(objectMapper.writeValueAsString(result))
    }

    private File getFileFromResources(String fileName) {
        new File(getClass().getResource(fileName).toURI())
    }
}
