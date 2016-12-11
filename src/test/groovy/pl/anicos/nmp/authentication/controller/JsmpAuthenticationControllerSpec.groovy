package pl.anicos.nmp.authentication.controller

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
import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.configuration.ApplicationProperties
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static pl.anicos.nmp.JsonObjectCreator.toJson

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JsmpAuthenticationControllerSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)
    @MockBean
    ApplicationProperties applicationProperties
    @LocalServerPort
    int randomServerPort;

    def 'should jsmp access to artifactiory'() {
        given:
        stubFor(get(urlEqualTo('/'))
                .willReturn(aResponse()
                .withStatus(200)
                .withBody('')
                .withHeader('Content-Type', 'application/json')))


        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())

        when:

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange('http://localhost:' + randomServerPort + '/', HttpMethod.GET, TestCredentialsProvider.getHttpEntityWithToken(), String)

        then:
        result.getStatusCodeValue() == 200
        toJson(result.getBody()) == toJson('{}')
        verify(getRequestedFor(urlEqualTo("/")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))

    }
}
