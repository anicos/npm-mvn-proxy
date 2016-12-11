package pl.anicos.nmp.authentication.controller

import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.mockito.Mockito
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.*
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.client.RestTemplate
import pl.anicos.nmp.ResourceProvider
import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.configuration.ApplicationProperties
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static pl.anicos.nmp.JsonObjectCreator.toJson

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NpmAuthenticationControllerSpec extends Specification {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(Options.DYNAMIC_PORT)
    @MockBean
    ApplicationProperties applicationProperties
    @LocalServerPort
    int randomServerPort;

    String addUserJson = ResourceProvider.getText(getClass(), 'addUserBody.json')
    String addUserResponse = ResourceProvider.getText(getClass(), 'addUserResponse.json')

    def 'should return token correct for artifactory'() {
        given:
        stubFor(get(urlEqualTo('/'))
                .willReturn(aResponse()
                .withStatus(200)
                .withBody('')
                .withHeader('Content-Type', 'application/json')))

        Mockito.when(applicationProperties.getMavenRepoUrl()).thenReturn('http://localhost:' + wireMockRule.port())

        when:
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(addUserJson, headers)
        ResponseEntity<String> result = restTemplate.exchange('http://localhost:' + randomServerPort + '/-/user/org.couchdb.user:userName' + randomServerPort, HttpMethod.PUT, entity, String)

        then:
        result.getStatusCodeValue() == 201
        toJson(result.getBody()) == toJson(addUserResponse)
        verify(getRequestedFor(urlEqualTo("/")).withBasicAuth(TestCredentialsProvider.BASIC_CREDENTIALS))
    }
}
