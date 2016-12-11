package pl.anicos.nmp.publish.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.ArtifactoryUrlProvider;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocationBuilder;
import pl.anicos.nmp.authentication.token.NpmTokenConverter;

@Component
public class ArtifactoryJsonLocationCreator {

    private final ArtifactoryUrlProvider artifactoryUrlProvider;
    private final NpmTokenConverter npmTokenConverter;

    @Autowired
    public ArtifactoryJsonLocationCreator(ArtifactoryUrlProvider artifactoryUrlProvider, NpmTokenConverter npmTokenConverter) {
        this.artifactoryUrlProvider = artifactoryUrlProvider;
        this.npmTokenConverter = npmTokenConverter;
    }

    public ArtifactoryLocation createArtifactoryLocation(String name, String authorizationHeader) {
        String urlToMetadataJson = artifactoryUrlProvider.getUrlToMetadataJson(name);
        String artifactoryToken = npmTokenConverter.toArtifactoryToken(authorizationHeader);
        return new ArtifactoryLocationBuilder()
                .setUrl(urlToMetadataJson)
                .setAuthorizationHeader(artifactoryToken)
                .build();
    }

}
