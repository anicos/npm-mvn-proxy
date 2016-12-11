package pl.anicos.nmp.authentication.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.authentication.model.UserData;

import java.util.Base64;

@Component
public class TokenEncoder {
    final Base64.Encoder encoder;

    @Autowired
    public TokenEncoder(Base64.Encoder encoder) {
        this.encoder = encoder;
    }

    public String encode(UserData userData) {
        String token = userData.getName() + ":" + userData.getPassword();
        byte[] encode = encoder.encode(token.getBytes());
        return new String(encode);
    }
}
