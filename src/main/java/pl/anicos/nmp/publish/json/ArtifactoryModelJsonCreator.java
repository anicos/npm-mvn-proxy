package pl.anicos.nmp.publish.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.ArtifactChecker;
import pl.anicos.nmp.artifactory.ArtifactoryUrlProvider;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocationBuilder;
import pl.anicos.nmp.artifactory.model.ArtifactoryModel;
import pl.anicos.nmp.artifactory.model.ArtifactoryModelBuilder;
import pl.anicos.nmp.json.ObjectMapperWrapper;

import java.util.Optional;

@Component
public class ArtifactoryModelJsonCreator {


    private final ArtifactoryUrlProvider artifactoryUrlProvider;
    private final ArtifactChecker artifactChecker;
    private final MetadataCreator metadataCreator;
    private final MetadataUpdater metadataUpdater;
    private final ObjectMapperWrapper objectMapperWrapper;

    @Autowired
    public ArtifactoryModelJsonCreator(ArtifactoryUrlProvider artifactoryUrlProvider, ArtifactChecker artifactChecker, MetadataCreator metadataCreator, MetadataUpdater metadataUpdater, ObjectMapperWrapper objectMapperWrapper) {
        this.artifactoryUrlProvider = artifactoryUrlProvider;
        this.artifactChecker = artifactChecker;
        this.metadataCreator = metadataCreator;
        this.metadataUpdater = metadataUpdater;
        this.objectMapperWrapper = objectMapperWrapper;
    }

    public ArtifactoryModel create(ObjectNode jsonNodes, String artifactoryToken) {
        String urlToMetadataJson = getUrlToMetadataJson(jsonNodes);

        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setAuthorizationHeader(artifactoryToken)
                .setUrl(urlToMetadataJson)
                .build();

        Optional<ResponseEntity<ObjectNode>> objectNodeResponseEntity = artifactChecker.fileExist(artifactoryLocation, ObjectNode.class);

        ObjectNode newJson = objectNodeResponseEntity
                .map(response -> metadataUpdater.update(jsonNodes, response.getBody()))
                .orElseGet(() -> metadataCreator.create(jsonNodes));

        byte[] binaryFromJsonObject = objectMapperWrapper.writeValueAsBytes(newJson);

        return new ArtifactoryModelBuilder()
                .setAuthorizationHeader(artifactoryToken)
                .setBinary(binaryFromJsonObject)
                .setUrl(urlToMetadataJson)
                .build();
    }

    private String getUrlToMetadataJson(ObjectNode jsonNodes) {
        String id = jsonNodes.get(MetadataJsonUtil.ID).asText();
        return artifactoryUrlProvider.getUrlToMetadataJson(id);
    }


}
