package pl.anicos.nmp.publish.json

import spock.lang.Specification

class TarballLinkUpdaterSpec extends Specification {

    TarballLinkUpdater testObj = new TarballLinkUpdater();


    def "should add version to link"() {
        when:
        def result = testObj.addVersionToLink('https://registry.npmjs.org/anicosnpm3/-/anicosnpm3-1.0.1.tgz', '1.0.1')
        then:
        result == 'https://registry.npmjs.org/anicosnpm3/1.0.1/anicosnpm3-1.0.1.tgz'
    }
}
