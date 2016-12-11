package pl.anicos.nmp.publish.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import groovy.json.JsonSlurper
import spock.lang.Specification

class MetadataCreatorSpec extends Specification {
    ValuesGenerator valuesGenerator = Mock()
    ObjectMapper objectMapper = new ObjectMapper();
    MetadataCreator testObj = new MetadataCreator(new MetadataJsonUtil(valuesGenerator, new TarballLinkUpdater()));

    def "setup"() {
        valuesGenerator.getCurrentDate() >> '2016-09-06T20:50:49.037Z'
        valuesGenerator.generateUUID() >> '1-1cac3235e241ed775464a23a5f91cd58'
    }

    def "should create correct json before publish"() {
        given:
        def jsonAfterPublishing = getFileFromResources('jsonAfterFirstPublish.json');
        def jsonFromNpm = getFileFromResources('jsonFromNpmInFirstPublish.json');

        JsonNode afterPublishingNode = objectMapper.readTree(jsonAfterPublishing);
        JsonNode fromNpmNode = objectMapper.readTree(jsonFromNpm);

        when:
        JsonNode result = testObj.create(fromNpmNode);
        then:
        convertToJsonSlurper(result) == convertToJsonSlurper(afterPublishingNode)
    }

    def "should create correct json before publish with readme"() {
        given:
        def jsonAfterPublishing = getFileFromResources('jsonAfterFirstPublish_withReadme.json');
        def jsonFromNpm = getFileFromResources('jsonFromNpmInFirstPublish_withReadme.json');

        JsonNode afterPublishingNode = objectMapper.readTree(jsonAfterPublishing);
        JsonNode fromNpmNode = objectMapper.readTree(jsonFromNpm);

        when:
        JsonNode result = testObj.create(fromNpmNode);
        then:
        convertToJsonSlurper(result) == convertToJsonSlurper(afterPublishingNode)
    }

    def "should create correct json before publish with readme null"() {
        given:
        def jsonAfterPublishing = getFileFromResources('jsonAfterFirstPublish.json');
        def jsonFromNpm = getFileFromResources('jsonFromNpmInFirstPublish_withNullReadme.json');

        JsonNode afterPublishingNode = objectMapper.readTree(jsonAfterPublishing);
        JsonNode fromNpmNode = objectMapper.readTree(jsonFromNpm);

        when:
        JsonNode result = testObj.create(fromNpmNode);
        then:
        convertToJsonSlurper(result) == convertToJsonSlurper(afterPublishingNode)
    }

    private Object convertToJsonSlurper(ObjectNode result) {
        new JsonSlurper().parseText(objectMapper.writeValueAsString(result))
    }

    private File getFileFromResources(String fileName) {
        new File(getClass().getResource(fileName).toURI())
    }
}
