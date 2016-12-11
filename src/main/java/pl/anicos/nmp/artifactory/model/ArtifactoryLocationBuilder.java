package pl.anicos.nmp.artifactory.model;

public class ArtifactoryLocationBuilder {
    private String authorizationHeader;
    private String url;

    public ArtifactoryLocationBuilder setAuthorizationHeader(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
        return this;
    }

    public ArtifactoryLocationBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public ArtifactoryLocation build() {
        return new ArtifactoryLocation(authorizationHeader, url);
    }
}