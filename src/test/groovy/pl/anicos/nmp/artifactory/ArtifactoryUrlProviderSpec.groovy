package pl.anicos.nmp.artifactory

import pl.anicos.nmp.configuration.ApplicationProperties
import pl.anicos.nmp.publish.model.Attachment
import pl.anicos.nmp.publish.model.DistTags
import pl.anicos.nmp.publish.model.NpmPublishBody
import spock.lang.Specification

class ArtifactoryUrlProviderSpec extends Specification {

    ApplicationProperties applicationProperties = Stub();
    ArtifactoryUrlProvider testObj = new ArtifactoryUrlProvider(applicationProperties)

    def "should provide url to artifact in artifactory"() {
        given:
        applicationProperties.getMavenRepoUrl() >> "mvn_url"
        applicationProperties.folderForNpmArtifacts >> "npm"

        Attachment attachment = new Attachment("data")
        Map<String, Attachment> attachmentMap = new HashMap<>()
        attachmentMap.put("name-1.1.gz", attachment)
        NpmPublishBody npmPublishBody = new NpmPublishBody("name", new DistTags("1.1"), attachmentMap)
        expect:
        testObj.getUrlToAtrtifact(npmPublishBody) == "mvn_url/npm/name/1.1/name-1.1.gz"
    }

    def "should provide url to json file in artifactory"() {
        given:
        applicationProperties.getMavenRepoUrl() >> "mvn_url"
        applicationProperties.folderForNpmArtifacts >> "npm"

        expect:
        testObj.getUrlToMetadataJson("id") == "mvn_url/npm/id/superPackage.json"
    }

    def "should provide url to binary"() {
        given:
        applicationProperties.getMavenRepoUrl() >> "mvn_url"
        applicationProperties.folderForNpmArtifacts >> "npm"
        expect:
        testObj.getUrlToAtrtifact('name', 'name-1.1.gz', 'version') == "mvn_url/npm/name/version/name-1.1.gz"
    }
}
