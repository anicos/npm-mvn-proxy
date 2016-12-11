package pl.anicos.nmp.publish.contoller

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.mockito.Mockito
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.ResourceProvider
import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.configuration.ApplicationProperties
import pl.anicos.nmp.publish.json.ValuesGenerator
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NpmPublishControllerIntegrationSpec extends Specification {
    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)
    @MockBean
    ApplicationProperties applicationProperties;
    @MockBean
    ValuesGenerator valuesGenerator;
    @LocalServerPort
    int randomServerPort;

    String jsonFromNpm = ResourceProvider.getText(getClass(), 'jsonFromNpmInFirstPublish.json')
    String jsonSavedInArtifatory = ResourceProvider.getText(getClass(), 'jsonAfterFirstPublish.json')

    String jsonFromNpmInSecondPublish = ResourceProvider.getText(getClass(), 'jsonFromNpmInSecondPublish.json')
    String jsonAfterSecondPublish = ResourceProvider.getText(getClass(), 'jsonAfterSecondPublish.json')

    def 'should publish new artefact'() {
        given:

        stubFor(get(urlEqualTo('/npm/anicosnpm3/superPackage.json'))
                .willReturn(aResponse()
                .withStatus(404)))

        stubFor(get(urlEqualTo('/npm/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz'))
                .willReturn(aResponse()
                .withStatus(404)))

        stubFor(put(urlEqualTo('/npm/anicosnpm3/superPackage.json'))
                .willReturn(aResponse()
                .withStatus(201)))

        stubFor(put(urlEqualTo('/npm/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz'))
                .willReturn(aResponse()
                .withStatus(201)))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn("npm")
        Mockito.when(valuesGenerator.generateUUID()).thenReturn("1-1cac3235e241ed775464a23a5f91cd58");
        Mockito.when(valuesGenerator.getCurrentDate()).thenReturn("2016-09-06T20:50:49.037Z");

        when:
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange('http://localhost:' + randomServerPort + '/anicosnpm3', HttpMethod.PUT, TestCredentialsProvider.getHttpEntityWithToken(jsonFromNpm), String)

        then:
        result.getStatusCodeValue() == 200
        verify(getRequestedFor(urlEqualTo("/npm/anicosnpm3/superPackage.json")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
        verify(getRequestedFor(urlEqualTo("/npm/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
        verify(putRequestedFor(urlEqualTo("/npm/anicosnpm3/superPackage.json")).withRequestBody(equalToJson(jsonSavedInArtifatory)).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
        verify(putRequestedFor(urlEqualTo("/npm/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz")).withRequestBody(containing('package/package.json')).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }

    def 'should publish next version artefact'() {
        given:
        stubFor(get(urlEqualTo('/npm/anicosnpm3/superPackage.json'))
                .willReturn(aResponse()
                .withStatus(200)
                .withBody(jsonSavedInArtifatory)
                .withHeader('Content-Type', 'application/json')))

        stubFor(get(urlEqualTo('/npm/anicosnpm3/1.0.2/anicosnpm3-1.0.2.tgz'))
                .willReturn(aResponse()
                .withStatus(404)))

        stubFor(put(urlEqualTo('/npm/anicosnpm3/superPackage.json'))
                .willReturn(aResponse()
                .withStatus(201)))

        stubFor(put(urlEqualTo('/npm/anicosnpm3/1.0.2/anicosnpm3-1.0.2.tgz'))
                .willReturn(aResponse()
                .withStatus(201)))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn("npm")
        Mockito.when(valuesGenerator.generateUUID()).thenReturn("2-693d3a82a0499ca4a168c691b321e05c");
        Mockito.when(valuesGenerator.getCurrentDate()).thenReturn("2016-09-06T22:57:02.844Z");

        when:
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange('http://localhost:' + randomServerPort + '/anicosnpm3', HttpMethod.PUT, TestCredentialsProvider.getHttpEntityWithToken(jsonFromNpmInSecondPublish), String)

        then:
        result.getStatusCodeValue() == 200
        verify(getRequestedFor(urlEqualTo("/npm/anicosnpm3/superPackage.json")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
        verify(getRequestedFor(urlEqualTo("/npm/anicosnpm3/1.0.2/anicosnpm3-1.0.2.tgz")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
        verify(putRequestedFor(urlEqualTo("/npm/anicosnpm3/superPackage.json")).withRequestBody(equalToJson(jsonAfterSecondPublish)).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
        verify(putRequestedFor(urlEqualTo("/npm/anicosnpm3/1.0.2/anicosnpm3-1.0.2.tgz")).withRequestBody(containing('package/package.json')).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }
}