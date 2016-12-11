package pl.anicos.nmp.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.configuration.ApplicationProperties;

@Component
public class NpmUrlProvider {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public NpmUrlProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getUrlToNpm(String id) {
        return applicationProperties.getNpmUrl() + "/" + id;
    }
}
