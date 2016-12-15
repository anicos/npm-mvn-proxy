package pl.anicos.nmp.artifactory

import pl.anicos.nmp.artifactory.exceptions.VersionNumberNotExistException
import pl.anicos.nmp.publish.model.Attachment
import spock.lang.Specification

class VersionNumberExtractorSpec extends Specification {

    VersionNumberExtractor testObj = new VersionNumberExtractor();

    def "should return first number version"() {
        given:
        Map<String, Attachment> attachments = new HashMap<>();
        attachments.put("a", new Attachment())

        when:
        String result = testObj.getVersionNumber(attachments);

        then:
        result == "a"
    }

    def "should throw exception when version not exist"() {
        given:
        Map<String, Attachment> attachments = new HashMap<>();


        when:
        testObj.getVersionNumber(attachments);

        then:
        VersionNumberNotExistException exception = thrown();
        exception.message == "Can't find version number"

    }
}
