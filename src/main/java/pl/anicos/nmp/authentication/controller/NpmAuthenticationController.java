package pl.anicos.nmp.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.anicos.nmp.authentication.model.LoginResponse;
import pl.anicos.nmp.authentication.model.LoginResponseBuilder;
import pl.anicos.nmp.authentication.model.UserData;
import pl.anicos.nmp.authentication.token.NpmTokenConverter;
import pl.anicos.nmp.authentication.token.TokenEncoder;
import pl.anicos.nmp.authentication.token.TokenVerifier;

@RestController
public class NpmAuthenticationController {

    private final TokenEncoder tokenEncoder;
    private final TokenVerifier tokenVerifier;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public NpmAuthenticationController(TokenEncoder tokenEncoder, TokenVerifier tokenVerifier) {
        this.tokenEncoder = tokenEncoder;
        this.tokenVerifier = tokenVerifier;
    }

    @RequestMapping(value = "/-/user/org.couchdb.user:{userName}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@RequestBody UserData userData) {

        String token = tokenEncoder.encode(userData);

        logger.info("Try to login user " + userData.getName());

        tokenVerifier.tryLoginToArtifact(NpmTokenConverter.BASIC + token);

        logger.info("Login successful " + userData.getName());

        return new LoginResponseBuilder()
                .setOk(true)
                .setId("org.couchdb.user:undefined")
                .setRev("_we_dont_use_revs_any_more")
                .setToken(token)
                .build();
    }


}