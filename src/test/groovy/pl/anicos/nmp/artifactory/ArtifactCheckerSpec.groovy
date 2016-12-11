package pl.anicos.nmp.artifactory

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.artifactory.exceptions.ForbiddenException
import pl.anicos.nmp.artifactory.exceptions.UnauthorizedException
import pl.anicos.nmp.artifactory.exceptions.UnexpectedException
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation
import pl.anicos.nmp.artifactory.model.ArtifactoryLocationBuilder
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

class ArtifactCheckerSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)
    RestTemplate restTemplate = new RestTemplate();
    HttpEntityCreator httpEntityCreator = new HttpEntityCreator();
    ArtifactChecker testObj = new ArtifactChecker(restTemplate, httpEntityCreator)

    def 'should throw exception when pass is wrong'() {
        given:
        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader("token")
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .build()

        stubFor(get(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(403)
                .withBody('Forbidden')
                .withHeader('Content-Type', 'application/json')))

        when:
        testObj.fileExist(artifactoryLocation, String);

        then:
        ForbiddenException ex = thrown()
        ex.message == ArtifactChecker.YOU_DO_NOT_HAVE_PERMISSION_TO_PUBLISH_ARTIFACT_ARE_YOU_LOGGED_IN_AS_THE_CORRECT_USER
    }

    def 'should return empty optional when resource not exist'() {
        given:

        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader("token")
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .build()

        stubFor(get(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(404)
                .withBody('{\n' +
                '  "errors" : [ {\n' +
                '    "status" : 404,\n' +
                '    "message" : "Unable to find item"\n' +
                '  } ]\n' +
                '}')
                .withHeader('Content-Type', 'application/json')))

        when:
        def result = testObj.fileExist(artifactoryLocation, String);

        then:
        result.isPresent() == false
    }

    def 'should return not empty optional when resource exist'() {
        given:

        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader("token")
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .build()

        stubFor(get(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(200)
                .withBody('{}')
                .withHeader('Content-Type', 'application/json')))

        when:
        def result = testObj.fileExist(artifactoryLocation, String);

        then:
        result.isPresent() == true
    }

    def 'should throw unexpeced exception when server exception'() {
        given:
        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader("token")
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .build()

        stubFor(get(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(500)
                .withBody('{}')
                .withHeader('Content-Type', 'application/json')))

        when:

        testObj.fileExist(artifactoryLocation, String);

        then:
        UnexpectedException ex = thrown()
        ex.message == ArtifactChecker.UNRECOGNIZE_STATUS_CODE
        ex.cause.class == HttpServerErrorException.class
    }

    def 'should throw unauthorized response'() {
        given:
        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader("token")
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .build()

        stubFor(get(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(401)
                .withBody('{}')
                .withHeader('Content-Type', 'application/json')))

        when:

        testObj.fileExist(artifactoryLocation, String);

        then:
        UnauthorizedException ex = thrown()
        ex.message == ArtifactChecker.UNAUTHORIZED_MESSAGE
        ex.cause == null
    }

    def 'should throw unexpeced exception when unxepced response'() {
        given:

        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader("token")
                .setUrl('http://localhost:' + wireMockRule.port() + '/name')
                .build()

        stubFor(get(urlEqualTo('/name'))
                .willReturn(aResponse()
                .withStatus(400)
                .withBody('{}')
                .withHeader('Content-Type', 'application/json')))
        when:
        testObj.fileExist(artifactoryLocation, String);

        then:
        UnexpectedException ex = thrown()
        ex.message == ArtifactChecker.UNRECOGNIZE_STATUS_CODE
        ex.cause == null
    }
}