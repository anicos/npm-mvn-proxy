package pl.anicos.nmp.artifactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class HttpEntityCreator {

    public static final String AUTHORIZATION = "Authorization";

    public HttpEntity<String> create(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AUTHORIZATION, token);
        return new HttpEntity<>(headers);
    }
}
