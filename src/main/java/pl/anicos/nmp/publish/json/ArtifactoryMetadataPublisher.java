package pl.anicos.nmp.publish.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.ArtifactSender;
import pl.anicos.nmp.artifactory.model.ArtifactoryModel;

@Component
public class ArtifactoryMetadataPublisher {
    private final ArtifactoryModelJsonCreator artifactoryModelJsonCreator;
    private final ArtifactSender artifactSender;

    @Autowired
    public ArtifactoryMetadataPublisher(ArtifactoryModelJsonCreator artifactoryModelJsonCreator, ArtifactSender artifactSender) {
        this.artifactoryModelJsonCreator = artifactoryModelJsonCreator;
        this.artifactSender = artifactSender;
    }

    public void publish(String artifactoryToken, ObjectNode jsonNodes) {
        ArtifactoryModel json = artifactoryModelJsonCreator.create(jsonNodes, artifactoryToken);
        artifactSender.publish(json);
    }
}
