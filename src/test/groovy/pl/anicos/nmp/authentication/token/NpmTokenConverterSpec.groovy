package pl.anicos.nmp.authentication.token

import pl.anicos.nmp.TestCredentialsProvider
import pl.anicos.nmp.artifactory.exceptions.ForbiddenException
import spock.lang.Specification

class NpmTokenConverterSpec extends Specification {

    NpmTokenConverter testObj = new NpmTokenConverter();

    def "should convert npm token to artifatory"() {
        when:
        String result = testObj.toArtifactoryToken(TestCredentialsProvider.BEARER_TOKEN)
        then:
        result == TestCredentialsProvider.BASIC_TOKEN
    }

    def "should throw ForbiddenException"() {
        when:
        testObj.toArtifactoryToken('this is wrong token')
        then:
        ForbiddenException exception = thrown()
    }


}
