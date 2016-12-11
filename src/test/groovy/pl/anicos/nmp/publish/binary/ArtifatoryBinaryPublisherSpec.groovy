package pl.anicos.nmp.publish.binary

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.HttpEntity
import pl.anicos.nmp.artifactory.ArtifactChecker
import pl.anicos.nmp.artifactory.ArtifactSender
import pl.anicos.nmp.artifactory.exceptions.ForbiddenException
import pl.anicos.nmp.artifactory.model.ArtifactoryModel
import pl.anicos.nmp.artifactory.model.ArtifactoryModelBuilder
import spock.lang.Specification

class ArtifatoryBinaryPublisherSpec extends Specification {

    private ArtifactChecker artifactChecker = Mock();
    private ArtifactSender artifactSender = Mock();
    private HttpEntity<String> stringHttpEntity = Mock()
    private ArtifactoryModelPackageCreator artifactoryModelPackageCreator = Mock()
    ArtifactoryModel artifactoryModel = new ArtifactoryModelBuilder().build()
    String artifactoryToken = "token";
    ObjectNode jsonNode = Mock()
    ArtifatoryBinaryPublisher testObj = new ArtifatoryBinaryPublisher(artifactChecker, artifactSender, artifactoryModelPackageCreator)

    def "should invoke validator and send file"() {
        given:

        artifactoryModelPackageCreator.create(artifactoryToken, jsonNode) >> artifactoryModel
        artifactChecker.fileExist(artifactoryModel, String) >> Optional.of(stringHttpEntity)

        when:
        testObj.publish(artifactoryToken, jsonNode)
        then:
        0 * artifactSender.publish(artifactoryModel)
        thrown ForbiddenException
    }

    def "should send file when not exist"() {
        given:
        artifactoryModelPackageCreator.create(artifactoryToken, jsonNode) >> artifactoryModel
        artifactChecker.fileExist(artifactoryModel, String) >> Optional.empty()

        when:
        testObj.publish(artifactoryToken, jsonNode)
        then:
        1 * artifactSender.publish(artifactoryModel)
    }
}
