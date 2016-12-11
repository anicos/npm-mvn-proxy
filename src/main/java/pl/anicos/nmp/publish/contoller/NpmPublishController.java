package pl.anicos.nmp.publish.contoller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.anicos.nmp.authentication.token.NpmTokenConverter;
import pl.anicos.nmp.publish.binary.ArtifatoryBinaryPublisher;
import pl.anicos.nmp.publish.json.ArtifactoryMetadataPublisher;
import pl.anicos.nmp.publish.model.PublishResponse;

@RestController
public class NpmPublishController {

    private final ArtifatoryBinaryPublisher artifatoryBinaryPublisher;
    private final NpmTokenConverter npmTokenConverter;
    private final ArtifactoryMetadataPublisher artifactoryMetadataPublisher;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NpmPublishController(ArtifatoryBinaryPublisher artifatoryBinaryPublisher, NpmTokenConverter npmTokenConverter, ArtifactoryMetadataPublisher artifactoryMetadataPublisher) {
        this.artifatoryBinaryPublisher = artifatoryBinaryPublisher;
        this.npmTokenConverter = npmTokenConverter;
        this.artifactoryMetadataPublisher = artifactoryMetadataPublisher;
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublishResponse publish(@PathVariable String name, @RequestBody ObjectNode jsonNodes, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Invoke publish with name " + name);
        String artifactoryToken = npmTokenConverter.toArtifactoryToken(authorizationHeader);

        artifatoryBinaryPublisher.publish(artifactoryToken, jsonNodes);
        artifactoryMetadataPublisher.publish(artifactoryToken, jsonNodes);

        logger.info("Artifact " + name + "publish with success");

        return new PublishResponse(Boolean.TRUE);
    }
}