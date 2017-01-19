package pl.anicos.nmp

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.mockito.Mockito
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.configuration.ApplicationProperties
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErrorHandlingSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)
    @MockBean
    ApplicationProperties applicationProperties
    @LocalServerPort
    int randomServerPort;

    def 'should return 404 when wrong url'() {
        given:
        stubFor(get(urlEqualTo('/'))
                .willReturn(aResponse()
                .withStatus(404)
                .withBody('')
                .withHeader('Content-Type', 'application/json')))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())

        when:
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{}", headers)
        restTemplate.exchange('http://localhost:' + randomServerPort + 'npm-mvn-proxy/-/user/org.couchdb.user:userName' + randomServerPort, HttpMethod.PUT, entity, String)

        then:
        HttpClientErrorException exception = thrown()
    }
}
