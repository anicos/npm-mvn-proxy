package pl.anicos.nmp.authentication.token

import pl.anicos.nmp.authentication.model.UserData
import spock.lang.Specification

class TokenEncoderSpec extends Specification {

    def "should encode string"() {
        given:
        TokenEncoder tokenEncoder = new TokenEncoder(Base64.getEncoder())
        when:
        def result = tokenEncoder.encode(new UserData('name', 'pass'))
        then:
        result == 'bmFtZTpwYXNz'
    }
}
