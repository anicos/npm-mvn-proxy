package pl.anicos.nmp.artifactory

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.artifactory.model.ArtifactoryModel
import pl.anicos.nmp.artifactory.model.ArtifactoryModelBuilder
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

class ArtifactSenderSpec extends Specification {

    RestTemplate restTemplate = new RestTemplate();
    ArtifactSender testObj = new ArtifactSender(restTemplate);
    String base64Example = 'YXNk'
    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)

    def 'should publish artifact to artifactory'() {
        given:
        String content = new File(getClass().getResource('publishResponse.json').toURI()).getText('UTF-8')

        ArtifactoryModel artifactoryModel = new ArtifactoryModelBuilder()
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .setAuthorizationHeader(TestCredentialsProvider.BASIC_TOKEN)
                .setBinary(base64Example.getBytes())
                .build();

        stubFor(put(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(201)
                .withBody(content)
                .withHeader('Content-Type', 'application/vnd.org.jfrog.json.storage.itemcreated+json;charset=ISO-8859-1')))

        when:
        testObj.publish(artifactoryModel);

        then:
        verify(putRequestedFor(urlEqualTo("/name")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }
}
