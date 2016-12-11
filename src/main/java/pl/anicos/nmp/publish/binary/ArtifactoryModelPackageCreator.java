package pl.anicos.nmp.publish.binary;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.ArtifactoryUrlProvider;
import pl.anicos.nmp.artifactory.model.ArtifactoryModel;
import pl.anicos.nmp.artifactory.model.ArtifactoryModelBuilder;
import pl.anicos.nmp.json.ObjectMapperWrapper;
import pl.anicos.nmp.publish.model.NpmPublishBody;

@Component
public class ArtifactoryModelPackageCreator {

    private final NpmPublishBodyToByteArray npmPublishBodyToByteArray;
    private final ArtifactoryUrlProvider artifactoryUrlProvider;
    private final ObjectMapperWrapper objectMapperWrapper;

    @Autowired
    public ArtifactoryModelPackageCreator(NpmPublishBodyToByteArray npmPublishBodyToByteArray, ArtifactoryUrlProvider artifactoryUrlProvider, ObjectMapperWrapper objectMapperWrapper) {
        this.npmPublishBodyToByteArray = npmPublishBodyToByteArray;
        this.artifactoryUrlProvider = artifactoryUrlProvider;
        this.objectMapperWrapper = objectMapperWrapper;
    }

    public ArtifactoryModel create(String authorizationHeader, ObjectNode jsonNodes) {
        NpmPublishBody npmPublishBody = objectMapperWrapper.treeToValue(jsonNodes, NpmPublishBody.class);
        byte[] binary = npmPublishBodyToByteArray.apply(npmPublishBody);
        String urlToAtrtifact = artifactoryUrlProvider.getUrlToAtrtifact(npmPublishBody);
        return new ArtifactoryModelBuilder()
                .setAuthorizationHeader(authorizationHeader)
                .setBinary(binary)
                .setUrl(urlToAtrtifact)
                .build();
    }
}
