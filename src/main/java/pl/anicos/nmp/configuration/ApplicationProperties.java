package pl.anicos.nmp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {


    private final String folderForNpmArtifacts;
    private final String mavenRepoUrl;
    private final String npmUrl;

    public ApplicationProperties(@Value("${folder.for.npm.artifacts:libs-release-local/npm}") String folderForNpmArtifacts, @Value("${maven.repo.url}") String mavenRepoUrl, @Value("${npm.url:http://registry.npmjs.org}") String npmUrl) {
        this.folderForNpmArtifacts = folderForNpmArtifacts;
        this.mavenRepoUrl = mavenRepoUrl;
        this.npmUrl = npmUrl;
    }

    public String getMavenRepoUrl() {
        return mavenRepoUrl;
    }

    public String getFolderForNpmArtifacts() {
        return folderForNpmArtifacts;
    }

    public String getNpmUrl() {
        return npmUrl;
    }
}
