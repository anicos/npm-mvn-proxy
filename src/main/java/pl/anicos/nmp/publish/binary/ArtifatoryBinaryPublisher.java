package pl.anicos.nmp.publish.binary;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.ArtifactChecker;
import pl.anicos.nmp.artifactory.ArtifactSender;
import pl.anicos.nmp.artifactory.exceptions.ForbiddenException;
import pl.anicos.nmp.artifactory.model.ArtifactoryModel;

@Component
public class ArtifatoryBinaryPublisher {
    public static final String YOU_CANNOT_PUBLISH_OVER_THE_PREVIOUSLY_PUBLISHED_VERSION_S = "You cannot publish over the previously published version %s.";
    private final ArtifactChecker artifactChecker;
    private final ArtifactSender artifactSender;
    private final ArtifactoryModelPackageCreator artifactoryModelPackageCreator;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ArtifatoryBinaryPublisher(ArtifactChecker artifactChecker, ArtifactSender artifactSender, ArtifactoryModelPackageCreator artifactoryModelPackageCreator) {
        this.artifactChecker = artifactChecker;
        this.artifactSender = artifactSender;
        this.artifactoryModelPackageCreator = artifactoryModelPackageCreator;
    }

    public void publish(String artifactoryToken, ObjectNode jsonNodes) {
        ArtifactoryModel artifactoryModel = artifactoryModelPackageCreator.create(artifactoryToken, jsonNodes);
        if (artifactChecker.fileExist(artifactoryModel, String.class).isPresent()) {
            String message = String.format(YOU_CANNOT_PUBLISH_OVER_THE_PREVIOUSLY_PUBLISHED_VERSION_S, artifactoryModel.getUrl());
            logger.warn(message);
            throw new ForbiddenException(message);
        }
        artifactSender.publish(artifactoryModel);
    }
}
