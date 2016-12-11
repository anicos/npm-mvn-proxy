package pl.anicos.nmp.publish.json

import spock.lang.Specification

class ValuesGeneratorSpec extends Specification {
    ValuesGenerator testObj = new ValuesGenerator();

    def "genrate uuid shouldn't be nul"() {
        when:
        def result = testObj.generateUUID()
        then:
        result
    }

    def "current date shouldn't be nul"() {
        when:
        def result = testObj.getCurrentDate()
        then:
        result
    }
}
