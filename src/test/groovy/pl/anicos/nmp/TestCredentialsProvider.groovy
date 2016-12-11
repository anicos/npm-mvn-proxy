package pl.anicos.nmp

import com.github.tomakehurst.wiremock.client.BasicCredentials
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class TestCredentialsProvider {

    public static final String BASIC_TOKEN = "Basic dXNlcjpwYXNz"
    public static final String BEARER_TOKEN = "Bearer dXNlcjpwYXNz"
    public static final BasicCredentials BASIC_CREDENTIALS = new BasicCredentials("user", "pass")
    public static final String AUTHORIZATION = "Authorization";

    public static HttpEntity<String> getHttpEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, BASIC_TOKEN);
        return new HttpEntity<>(headers);
    }

    public static HttpEntity<String> getHttpEntityWithToken(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, BASIC_TOKEN);
        return new HttpEntity<>(body, headers);
    }
}
