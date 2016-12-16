package pl.anicos.nmp.publish.contoller

import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import pl.anicos.nmp.artifactory.model.ArtifactoryModel
import pl.anicos.nmp.authentication.token.NpmTokenConverter
import pl.anicos.nmp.publish.binary.ArtifactoryModelPackageCreator
import pl.anicos.nmp.publish.binary.ArtifatoryBinaryPublisher
import pl.anicos.nmp.publish.json.ArtifactoryMetadataPublisher
import pl.anicos.nmp.publish.model.NpmPublishBody
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

class NpmPublishControllerSpec extends Specification {

    public static final String ARTIFATORY_TOKEN = "Basic YW5pY29zOmNvbXBhbGZsOTA="
    ArtifatoryBinaryPublisher artifatoryPublisher = Mock()
    NpmTokenConverter npmTokenConverter = Mock()
    ArtifactoryModelPackageCreator artifactoryModelCreator = Mock()
    NpmPublishBody npmPublishBody = Mock()
    ArtifactoryModel artifactoryModel = Mock()
    ArtifactoryMetadataPublisher artifactoryMetadataPublisher = Mock()


    NpmPublishController npmPublishController = new NpmPublishController(artifatoryPublisher, npmTokenConverter, artifactoryMetadataPublisher);
    MockMvc mvc = MockMvcBuilders.standaloneSetup(
            npmPublishController)
            .build()

    def 'should return 200 when body and auth is ok'() {
        given:
        def content = new File(getClass().getResource('correctPublish.json').toURI()).getText('UTF-8')
        npmTokenConverter.toArtifactoryToken("Bearer YW5pY29zOmNvbXBhbGZsOTA=") >> "Basic YW5pY29zOmNvbXBhbGZsOTA=";
        artifactoryModelCreator.create("Basic YW5pY29zOmNvbXBhbGZsOTA=", npmPublishBody) >> artifactoryModel
        when:
        def response = mvc.perform(put('/name')
                .contentType(MediaType.APPLICATION_JSON)
                .header('Authorization', 'Bearer YW5pY29zOmNvbXBhbGZsOTA=')
                .content(content)
        )
                .andReturn().response

        then:
        response.status == HttpStatus.OK.value()
        1 * artifatoryPublisher.publish(ARTIFATORY_TOKEN, _ as ObjectNode)
        1 * artifactoryMetadataPublisher.publish(ARTIFATORY_TOKEN, _ as ObjectNode);
    }

    def 'should return 400 when Authorization header not exist'() {
        given:
        def content = new File(getClass().getResource('correctPublish.json').toURI()).getText('UTF-8')

        when:
        def response = mvc.perform(put('/name')
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
        0 * artifatoryPublisher.publish(_ as ArtifactoryModel)
    }
}
