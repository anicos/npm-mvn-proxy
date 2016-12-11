package pl.anicos.nmp.download

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.mockito.Mockito
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.ResourceProvider
import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.configuration.ApplicationProperties
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static pl.anicos.nmp.JsonObjectCreator.toJson

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DownloadJsonControllerSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT);
    @MockBean
    ApplicationProperties applicationProperties
    @LocalServerPort
    int randomServerPort;
    String packageJson = ResourceProvider.getText(getClass(), 'packageJsonFile.json')

    def "Index json exist in atrifactory"() {
        given:
        stubFor(get(urlEqualTo('/npmfolder/sax.sa/superPackage.json'))
                .willReturn(aResponse()
                .withBody(packageJson)
                .withStatus(200)
                .withHeader('Content-Type', 'application/json')))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn('npmfolder')

        when:
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange('http://localhost:' + randomServerPort + '/sax.sa', HttpMethod.GET, TestCredentialsProvider.getHttpEntityWithToken(), String)

        then:
        result.getStatusCodeValue() == 200
        toJson(result.getBody()) == toJson(packageJson)

        verify(getRequestedFor(urlEqualTo("/npmfolder/sax.sa/superPackage.json")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }

    def "Index json not exist in atrifactory"() {
        given:
        String packageJson = ResourceProvider.getText(getClass(), 'packageJsonFile.json')

        stubFor(get(urlEqualTo('/npmUrl/jquery.js'))
                .willReturn(aResponse()
                .withBody(packageJson)
                .withStatus(200)
                .withHeader('Content-Type', 'application/json')))

        stubFor(get(urlEqualTo('/npmfolder/jquery.js/superPackage.json'))
                .willReturn(aResponse()
                .withStatus(404)))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getNpmUrl()).thenReturn('http://localhost:' + wireMockRule.port() + '/npmUrl')
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn('npmfolder')

        when:
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange('http://localhost:' + randomServerPort + '/jquery.js', HttpMethod.GET, TestCredentialsProvider.getHttpEntityWithToken(), String)

        then:
        result.getStatusCodeValue() == 200
        toJson(result.getBody()) == toJson(packageJson)
        verify(getRequestedFor(urlEqualTo("/npmfolder/jquery.js/superPackage.json")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }

    def "Index json not exist in atrifactory and npm"() {
        given:
        stubFor(get(urlEqualTo('/npmUrl/jquery'))
                .willReturn(aResponse()
                .withStatus(404)
                .withHeader('Content-Type', 'application/json')))

        stubFor(get(urlEqualTo('/npmfolder/jquery/superPackage.json'))
                .willReturn(aResponse()
                .withStatus(404)
                .withHeader('Content-Type', 'application/json')))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getNpmUrl()).thenReturn('http://localhost:' + wireMockRule.port() + '/npmUrl')
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn('npmfolder')

        when:
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange('http://localhost:' + randomServerPort + '/jquery', HttpMethod.GET, TestCredentialsProvider.getHttpEntityWithToken(), String)

        then:
        HttpClientErrorException exception = thrown()
        exception.statusCode == HttpStatus.NOT_FOUND
        verify(getRequestedFor(urlEqualTo("/npmfolder/jquery/superPackage.json")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }
}
