package pl.anicos.nmp.publish.json;

import org.springframework.stereotype.Component;

/**
 * Created by anicos on 10/19/16.
 */
@Component
public class TarballLinkUpdater {
    public String addVersionToLink(String link, String version) {
        return link.replace("/-/", "/" + version + "/");
    }
}
