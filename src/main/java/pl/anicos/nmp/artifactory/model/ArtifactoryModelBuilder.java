package pl.anicos.nmp.artifactory.model;

public class ArtifactoryModelBuilder {
    private String authorizationHeader;
    private byte[] binary;
    private String url;

    public ArtifactoryModelBuilder setAuthorizationHeader(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
        return this;
    }

    public ArtifactoryModelBuilder setBinary(byte[] binary) {
        this.binary = binary;
        return this;
    }

    public ArtifactoryModelBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public ArtifactoryModel build() {
        return new ArtifactoryModel(authorizationHeader, binary, url);
    }
}