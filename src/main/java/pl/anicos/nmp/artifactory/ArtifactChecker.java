package pl.anicos.nmp.artifactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.anicos.nmp.artifactory.exceptions.ForbiddenException;
import pl.anicos.nmp.artifactory.exceptions.UnauthorizedException;
import pl.anicos.nmp.artifactory.exceptions.UnexpectedException;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation;

import java.util.Optional;


@Component
public class ArtifactChecker {

    public static final String YOU_DO_NOT_HAVE_PERMISSION_TO_PUBLISH_ARTIFACT_ARE_YOU_LOGGED_IN_AS_THE_CORRECT_USER = "you do not have permission to publish artifact. Are you logged in as the correct user?";
    public static final String UNRECOGNIZE_STATUS_CODE = "Unrecognize status code ";
    public static final String UNAUTHORIZED_MESSAGE = "{\"ok\":false,\"id\":\"org.couchdb.user:undefined\"}";

    private final RestTemplate restTemplate;
    private final HttpEntityCreator httpEntityCreator;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ArtifactChecker(RestTemplate restTemplate, HttpEntityCreator httpEntityCreator) {
        this.restTemplate = restTemplate;
        this.httpEntityCreator = httpEntityCreator;
    }

    public <T> Optional<ResponseEntity<T>> fileExist(ArtifactoryLocation artifactoryLocation, Class<T> clazz) {
        HttpEntity<String> stringHttpEntity = httpEntityCreator.create(artifactoryLocation.getAuthorizationHeader());

        return getResponseEntity(artifactoryLocation.getUrl(), clazz, stringHttpEntity);
    }

    public <T> Optional<ResponseEntity<T>> fileExist(String url, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("user-agent", "npm/3.10.8 node/v6.9.1 linux x64");
        return getResponseEntity(url, clazz, new HttpEntity<>(headers));
    }

    private <T> Optional<ResponseEntity<T>> getResponseEntity(String url, Class<T> clazz, HttpEntity<?> stringHttpEntity) {

        try {
            ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, stringHttpEntity, clazz);
            logger.info("GET request with status " + result.getStatusCodeValue() + " to " + url);
            return Optional.of(result);
        } catch (HttpClientErrorException httpClientErrorException) {
            HttpStatus statusCode = httpClientErrorException.getStatusCode();

            switch (statusCode) {
                case FORBIDDEN:
                    logger.warn("Forbidden exception. Code status " + statusCode + " " + url);
                    throw new ForbiddenException(YOU_DO_NOT_HAVE_PERMISSION_TO_PUBLISH_ARTIFACT_ARE_YOU_LOGGED_IN_AS_THE_CORRECT_USER, httpClientErrorException);
                case NOT_FOUND:
                    logger.info("Not found. Code status " + statusCode + " to " + url);
                    return Optional.empty();
                case UNAUTHORIZED:
                    logger.warn("Unauthorized exception. Code status " + statusCode + " to " + url);
                    throw new UnauthorizedException(UNAUTHORIZED_MESSAGE);
                default:
                    logger.warn("Unexpeced exception. Code status " + statusCode + " to " + url);
                    throw new UnexpectedException(UNRECOGNIZE_STATUS_CODE);
            }

        } catch (Exception e) {
            logger.warn("Unrecognize status exception. Code status " + e.getMessage() + " to " + url);
            throw new UnexpectedException(UNRECOGNIZE_STATUS_CODE, e);
        }
    }
}
