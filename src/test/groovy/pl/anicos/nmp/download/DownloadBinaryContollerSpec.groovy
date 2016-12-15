package pl.anicos.nmp.download

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.mockito.Mockito
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.*
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.artifactory.HttpEntityCreator
import pl.anicos.nmp.configuration.ApplicationProperties
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DownloadBinaryContollerSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)
    @MockBean
    ApplicationProperties applicationProperties
    @LocalServerPort
    int randomServerPort;

    def "should return binary from artifactory"() {
        given:
        def array = [0, 0, 0, 0, 0] as byte[]
        stubFor(get(urlEqualTo("/npmfolder/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz"))
                .willReturn(aResponse()
                .withBody(array)));
        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn('npmfolder')

        when:
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpEntityCreator.AUTHORIZATION, TestCredentialsProvider.BASIC_TOKEN);
        ResponseEntity<byte[]> exchange = restTemplate.exchange('http://localhost:' + randomServerPort + '/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz', HttpMethod.GET, new HttpEntity<>(headers), byte[].class)

        then:
        exchange.getBody() == array
        exchange.getStatusCode() == HttpStatus.OK
        verify(getRequestedFor(urlEqualTo("/npmfolder/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }

    def "shouldn't return binary from npm"() {
        given:
        stubFor(get(urlEqualTo("/npmfolder/anicosnpm4/1.0.1/anicosnpm4-1.0.1.tgz"))
                .willReturn(aResponse()
                .withStatus(404)));
        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())
        Mockito.when(applicationProperties.getFolderForNpmArtifacts()).thenReturn('npmfolder')

        when:
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpEntityCreator.AUTHORIZATION, TestCredentialsProvider.BASIC_TOKEN);
        restTemplate.exchange('http://localhost:' + randomServerPort + '/anicosnpm4/1.0.1/anicosnpm4-1.0.1.tgz', HttpMethod.GET, new HttpEntity<>(headers), byte[].class)

        then:
        HttpClientErrorException exception = thrown()
        exception.statusCode == HttpStatus.NOT_FOUND

        verify(getRequestedFor(urlEqualTo("/npmfolder/anicosnpm4/1.0.1/anicosnpm4-1.0.1.tgz")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }

}
