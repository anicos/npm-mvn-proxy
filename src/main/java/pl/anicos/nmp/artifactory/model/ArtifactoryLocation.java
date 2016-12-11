package pl.anicos.nmp.artifactory.model;

public class ArtifactoryLocation {
    private final String authorizationHeader;
    private final String url;

    public ArtifactoryLocation(String authorizationHeader, String url) {
        this.authorizationHeader = authorizationHeader;
        this.url = url;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public String getUrl() {
        return url;
    }
}
