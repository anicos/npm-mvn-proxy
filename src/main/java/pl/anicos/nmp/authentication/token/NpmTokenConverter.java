package pl.anicos.nmp.authentication.token;

import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.exceptions.ForbiddenException;

@Component
public class NpmTokenConverter {
    public static final String WRONG_AUTH_TOKEN = "Wrong auth token";
    public static final String BASIC = "Basic ";
    public static final String REGEX = " ";
    public static final int TWO = 2;
    public static final int ONE = 1;


    public String toArtifactoryToken(String npmToken) {
        String[] split = npmToken.split(REGEX);
        if (split.length != TWO) {
            throw new ForbiddenException(WRONG_AUTH_TOKEN);
        }
        return BASIC + split[ONE];
    }
}
