package pl.anicos.nmp.download.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.anicos.nmp.artifactory.ArtifactChecker;
import pl.anicos.nmp.artifactory.ArtifactoryUrlProvider;
import pl.anicos.nmp.artifactory.exceptions.NotExistException;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocationBuilder;
import pl.anicos.nmp.authentication.token.NpmTokenConverter;
import pl.anicos.nmp.configuration.ApplicationProperties;
import pl.anicos.nmp.download.NpmUrlProvider;
import pl.anicos.nmp.publish.json.ArtifactoryJsonLocationCreator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class DownloadBinaryController {

    public static final String INLINE_FILENAME = "inline; filename=";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String DOESN_T_EXIST = " doesn't exist";
    private final ArtifactChecker artifactChecker;
    private final ArtifactoryUrlProvider artifactoryUrlProvider;
    private final NpmTokenConverter npmTokenConverter;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DownloadBinaryController(ArtifactChecker artifactChecker, ArtifactoryUrlProvider artifactoryUrlProvider, NpmTokenConverter npmTokenConverter, ApplicationProperties applicationProperties, NpmUrlProvider npmUrlProvider, ArtifactoryJsonLocationCreator artifactoryJsonLocationCreator) {
        this.artifactChecker = artifactChecker;
        this.artifactoryUrlProvider = artifactoryUrlProvider;
        this.npmTokenConverter = npmTokenConverter;
    }

    @RequestMapping(value = "/{id}/{version}/{nameBinary:.+}", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadBinary(@PathVariable String id, @PathVariable String nameBinary, @PathVariable String version, @RequestHeader("Authorization") String authorizationHeader, HttpServletRequest request) {

        String urlToArtifact = artifactoryUrlProvider.getUrlToAtrtifact(id, nameBinary, version);

        logger.info("Request for download binary ", urlToArtifact);

        String artifactoryToken = npmTokenConverter.toArtifactoryToken(authorizationHeader);

        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder().setUrl(urlToArtifact).setAuthorizationHeader(artifactoryToken).build();

        Optional<ResponseEntity<byte[]>> responseEntity = artifactChecker.fileExist(artifactoryLocation, byte[].class);
        return responseEntity
                .map(a -> getHttpEntity(nameBinary, a.getBody()))
                .orElseThrow(() -> getNotExistException(urlToArtifact));
    }

    private NotExistException getNotExistException(String urlToArtifact) {
        String message = urlToArtifact + DOESN_T_EXIST;
        logger.warn(message);
        return new NotExistException(message);
    }

    private HttpEntity<byte[]> getHttpEntity(String nameBinary, byte[] responseEntity) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(CONTENT_DISPOSITION, INLINE_FILENAME + nameBinary);
        header.setContentLength(responseEntity.length);
        logger.info("Start downloading binary ", nameBinary);
        return new HttpEntity<>(responseEntity, header);
    }
}
