package pl.anicos.nmp.publish.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class DistTags {
    @NotEmpty
    private final String latest;

    @JsonCreator
    public DistTags(@JsonProperty("latest") String latest) {
        this.latest = latest;
    }

    public String getLatest() {
        return latest;
    }
}
