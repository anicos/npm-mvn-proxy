package pl.anicos.nmp.artifactory;

import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.exceptions.VersionNumberNotExistException;
import pl.anicos.nmp.publish.model.Attachment;

import java.util.Map;

@Component
public class VersionNumberExtractor {

    public static final String CAN_T_FIND_VERSION_NUMBER = "Can't find version number";

    public String getVersionNumber(Map<String, Attachment> attachments) {
        return attachments.keySet().stream().findFirst().map(a -> a).orElseThrow(() -> new VersionNumberNotExistException(CAN_T_FIND_VERSION_NUMBER));
    }
}
