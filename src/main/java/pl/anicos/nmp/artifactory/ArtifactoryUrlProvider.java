package pl.anicos.nmp.artifactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.configuration.ApplicationProperties;
import pl.anicos.nmp.publish.model.Attachment;
import pl.anicos.nmp.publish.model.NpmPublishBody;

import java.util.Map;

@Component
public class ArtifactoryUrlProvider {

    public static final String SLASH = "/";
    public static final String SUPER_PACKAGE_JSON = "superPackage.json";
    private final ApplicationProperties applicationProperties;


    @Autowired
    public ArtifactoryUrlProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getUrlToAtrtifact(NpmPublishBody npmPublishBody) {
        Map<String, Attachment> attachments = npmPublishBody.getAttachments();
        String binaryName = attachments.keySet().stream().findFirst().get();
        return getUrlToAtrtifact(npmPublishBody.getId(), binaryName, npmPublishBody.getDistTags().getLatest());
    }

    public String getUrlToAtrtifact(String id, String binaryName, String vesion) {
        return applicationProperties.getMavenRepoUrl()
                + SLASH
                + applicationProperties.getFolderForNpmArtifacts()
                + SLASH
                + id
                + SLASH
                + vesion
                + SLASH
                + binaryName;
    }

    public String getUrlToMetadataJson(String id) {
        return applicationProperties.getMavenRepoUrl()
                + SLASH
                + applicationProperties.getFolderForNpmArtifacts()
                + SLASH
                + id
                + SLASH
                + SUPER_PACKAGE_JSON;
    }
}
