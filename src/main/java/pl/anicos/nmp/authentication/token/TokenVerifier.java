package pl.anicos.nmp.authentication.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.ArtifactChecker;
import pl.anicos.nmp.artifactory.exceptions.UnauthorizedException;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocation;
import pl.anicos.nmp.artifactory.model.ArtifactoryLocationBuilder;
import pl.anicos.nmp.configuration.ApplicationProperties;

@Component
public class TokenVerifier {
    private final ArtifactChecker artifactChecker;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public TokenVerifier(ArtifactChecker artifactChecker, ApplicationProperties applicationProperties) {
        this.artifactChecker = artifactChecker;
        this.applicationProperties = applicationProperties;
    }

    public void tryLoginToArtifact(String token) throws UnauthorizedException {
        ArtifactoryLocation artifactoryLocation = new ArtifactoryLocationBuilder()
                .setUrl(applicationProperties.getMavenRepoUrl())
                .setAuthorizationHeader(token)
                .build();

        artifactChecker.fileExist(artifactoryLocation, String.class);
    }
}
