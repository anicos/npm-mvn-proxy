package pl.anicos.nmp.publish.model;

/**
 * Created by anicos on 8/28/16.
 */
public class PublishResponse {
    private final Boolean success;

    public PublishResponse(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }
}
