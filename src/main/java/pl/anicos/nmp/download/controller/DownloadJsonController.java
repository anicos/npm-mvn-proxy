package pl.anicos.nmp.download.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.anicos.nmp.artifactory.ArtifactChecker;
import pl.anicos.nmp.artifactory.ArtifactoryUrlProvider;
import pl.anicos.nmp.artifactory.exceptions.NotExistException;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation;
import pl.anicos.nmp.authentication.token.NpmTokenConverter;
import pl.anicos.nmp.configuration.ApplicationProperties;
import pl.anicos.nmp.download.NpmUrlProvider;
import pl.anicos.nmp.publish.json.ArtifactoryJsonLocationCreator;

import java.util.Optional;

@RestController
public class DownloadJsonController {

    private final ArtifactChecker artifactChecker;
    private final NpmUrlProvider npmUrlProvider;
    private final ArtifactoryJsonLocationCreator artifactoryJsonLocationCreator;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DownloadJsonController(ArtifactChecker artifactChecker, ArtifactoryUrlProvider artifactoryUrlProvider, NpmTokenConverter npmTokenConverter, ApplicationProperties applicationProperties, NpmUrlProvider npmUrlProvider, ArtifactoryJsonLocationCreator artifactoryJsonLocationCreator) {
        this.artifactChecker = artifactChecker;
        this.npmUrlProvider = npmUrlProvider;
        this.artifactoryJsonLocationCreator = artifactoryJsonLocationCreator;
    }

    @RequestMapping(value = "/{name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ObjectNode index(@PathVariable String name, @RequestHeader("Authorization") String authorizationHeader) {
        ArtifactoryLocation artifactoryLocation = artifactoryJsonLocationCreator.createArtifactoryLocation(name, authorizationHeader);

        logger.info("Request for downloading json" + artifactoryLocation.getUrl());

        Optional<ResponseEntity<ObjectNode>> tResponseEntity = artifactChecker.fileExist(artifactoryLocation, ObjectNode.class);
        if (tResponseEntity.isPresent()) {
            logger.info("Artifact exist in artifatory" + artifactoryLocation.getUrl());
            return tResponseEntity.get().getBody();
        }

        String urlToNpm = npmUrlProvider.getUrlToNpm(name);
        Optional<ResponseEntity<ObjectNode>> objectNodeResponseEntity = artifactChecker.fileExist(urlToNpm, ObjectNode.class);
        if (objectNodeResponseEntity.isPresent()) {
            logger.info("Artifact exist in npm " + urlToNpm);
            return objectNodeResponseEntity.get().getBody();
        }

        logger.info("Artifact does not exist in artifatory " + artifactoryLocation.getUrl() + "and npm " + urlToNpm);
        throw new NotExistException(artifactoryLocation.getUrl() + " Doesn't exist");
    }
}
