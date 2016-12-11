package pl.anicos.nmp.authentication.model

import spock.lang.Specification

class LoginResponseBuilderSpec extends Specification {
    LoginResponseBuilder testObj = new LoginResponseBuilder();

    def "should create respons result"() {
        when:
        LoginResponse result = testObj.setId("id").setOk(false).setRev("rev").setToken("token").build();
        then:
        result.getId() == 'id'
        result.getOk() == false
        result.getRev() == 'rev'
        result.getToken() == 'token'
    }
}
