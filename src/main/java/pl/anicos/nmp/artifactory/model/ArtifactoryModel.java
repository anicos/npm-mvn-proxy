package pl.anicos.nmp.artifactory.model;

/**
 * Created by anicos on 9/5/16.
 */
public class ArtifactoryModel extends ArtifactoryLocation {
    private final byte[] binary;

    public ArtifactoryModel(String authorizationHeader, byte[] binary, String url) {
        super(authorizationHeader, url);
        this.binary = binary;
    }

    public byte[] getBinary() {
        return binary;
    }
}
