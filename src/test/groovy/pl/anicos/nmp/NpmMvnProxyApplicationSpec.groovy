package pl.anicos.nmp

import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.springframework.boot.SpringApplication

@RunWith(PowerMockRunner)
@PrepareForTest(SpringApplication)
class NpmMvnProxyApplicationSpec extends GroovyTestCase {

    public void testShouldInvokeSpringApplicationRunMethod() {
        //given
        String[] args = ["a", "b", "c"]
        PowerMockito.mockStatic(SpringApplication.class);

        //wehn
        NpmMvnProxyApplication.main(args);

        //then
        PowerMockito.verifyStatic();
        SpringApplication.run(NpmMvnProxyApplication, args);
    }
}
