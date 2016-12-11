package pl.anicos.nmp.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.anicos.nmp.authentication.token.NpmTokenConverter;
import pl.anicos.nmp.authentication.token.TokenVerifier;

@RestController
public class JsmpAuthenticationController {
    private final TokenVerifier tokenVerifier;
    private final NpmTokenConverter npmTokenConverter;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public JsmpAuthenticationController(TokenVerifier tokenVerifier, NpmTokenConverter npmTokenConverter) {
        this.tokenVerifier = tokenVerifier;
        this.npmTokenConverter = npmTokenConverter;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String login1(@RequestHeader("Authorization") String authorizationHeader) {

        String token = npmTokenConverter.toArtifactoryToken(authorizationHeader);

        logger.info("Try to login to jspm with token " + token);

        tokenVerifier.tryLoginToArtifact(token);

        logger.info("Login successful to jspm with token" + token);

        return "{}";
    }
}
