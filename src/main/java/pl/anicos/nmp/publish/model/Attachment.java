package pl.anicos.nmp.publish.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {

    @NotEmpty
    private final String data;

    @JsonCreator
    public Attachment(@JsonProperty("data") String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
