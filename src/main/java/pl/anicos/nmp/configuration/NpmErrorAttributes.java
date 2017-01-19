package pl.anicos.nmp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;


public class NpmErrorAttributes extends DefaultErrorAttributes {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<String, Object> getErrorAttributes(
            RequestAttributes requestAttributes,
            boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        Throwable error = getError(requestAttributes);
        if (error != null){
            String message = error.getMessage();
            errorAttributes.put("error", message);
        }

        logger.warn("Error " + errorAttributes.toString());
        return errorAttributes;
    }
}
