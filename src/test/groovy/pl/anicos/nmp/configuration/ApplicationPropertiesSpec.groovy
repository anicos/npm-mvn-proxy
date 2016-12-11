package pl.anicos.nmp.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@TestPropertySource(properties = ['maven.repo.url = http://mvnrepository.com'])
@ContextConfiguration
@SpringBootTest
class ApplicationPropertiesSpec extends Specification {

    @Autowired
    ApplicationProperties testObj;

    def "should provide maven url"() {
        when:
        String result = testObj.getMavenRepoUrl()
        then:
        result == 'http://mvnrepository.com'
    }

    def "should provide npm folder"() {
        when:
        String result = testObj.getFolderForNpmArtifacts()
        then:
        result == 'libs-release-local/npm'
    }

    def "should provide npm url"() {
        when:
        String result = testObj.getNpmUrl()
        then:
        result == 'http://registry.npmjs.org'
    }
}
