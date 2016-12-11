package pl.anicos.nmp.artifactory;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import pl.anicos.nmp.artifactory.model.ArtifactoryModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by anicos on 8/28/16.
 */
@Component
public class ArtifactSender {

    public static final String CONTENT_LENGTH = "Content-Length";
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ArtifactSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void addBinaryToRequest(ClientHttpRequest request, ArtifactoryModel artifactoryModel) throws IOException {
        request.getHeaders().add(HttpEntityCreator.AUTHORIZATION, artifactoryModel.getAuthorizationHeader());
        request.getHeaders().add(CONTENT_LENGTH, String.valueOf(artifactoryModel.getBinary().length));
        IOUtils.copy(new ByteArrayInputStream(artifactoryModel.getBinary()), request.getBody());
    }

    public void publish(ArtifactoryModel artifactoryModel) {
        final RequestCallback requestCallback = request -> addBinaryToRequest(request, artifactoryModel);

        final HttpMessageConverterExtractor<String> responseExtractor =
                new HttpMessageConverterExtractor<>(String.class, restTemplate.getMessageConverters());

        logger.info("PUT request with binary to " + artifactoryModel.getUrl());
        restTemplate.execute(artifactoryModel.getUrl(), HttpMethod.PUT, requestCallback, responseExtractor);
    }
}
