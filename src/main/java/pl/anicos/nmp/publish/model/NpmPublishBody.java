package pl.anicos.nmp.publish.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NpmPublishBody {
    @NotEmpty
    private final String id;
    @NotNull
    @Valid
    private final DistTags distTags;
    @NotEmpty
    @Size(max = 1)
    @Valid
    private final Map<String, Attachment> attachments;

    @NotEmpty
    @Valid

    @JsonCreator
    public NpmPublishBody(@JsonProperty("_id") String id, @JsonProperty("dist-tags") DistTags distTags, @JsonProperty("_attachments") Map<String, Attachment> attachments) {
        this.id = id;
        this.distTags = distTags;
        this.attachments = attachments;
    }

    public String getId() {
        return id;
    }

    public DistTags getDistTags() {
        return distTags;
    }

    public Map<String, Attachment> getAttachments() {
        return attachments;
    }

}
