package pl.anicos.nmp.publish.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anicos on 9/7/16.
 */
@Component
public class MetadataUpdater {

    private final MetadataJsonUtil metadataJsonUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MetadataUpdater(MetadataJsonUtil metadataJsonUtil) {
        this.metadataJsonUtil = metadataJsonUtil;
    }


    public ObjectNode update(ObjectNode fromNpm, ObjectNode fromArtifactory) {

        fromNpm = metadataJsonUtil.initDefaultValues(fromNpm);
        String lastVersion = metadataJsonUtil.getLastVersion(fromNpm);
        logger.info("Update existing metadata.json - adding new version " + lastVersion);
        metadataJsonUtil.updateLatestTarball(fromNpm);

        fillTimeSection(fromNpm, fromArtifactory, lastVersion);
        fillVersionSection(fromNpm, fromArtifactory);

        return fromNpm;
    }

    private void fillVersionSection(ObjectNode fromNpm, ObjectNode fromArtifactory) {
        JsonNode versionsFromArifatory = fromArtifactory.get(MetadataJsonUtil.VERSIONS);
        ObjectNode versionsFromNpm = (ObjectNode) fromNpm.get(MetadataJsonUtil.VERSIONS);

        versionsFromArifatory.fieldNames().forEachRemaining(versionName -> versionsFromNpm.set(versionName, versionsFromArifatory.get(versionName)));
    }

    private void fillTimeSection(ObjectNode fromNpm, ObjectNode fromArtifactory, String lastVersion) {
        String now = metadataJsonUtil.getCurrentDate();
        copyTimeObjectFormArtifactoryToNpm(fromNpm, fromArtifactory);
        ObjectNode time = (ObjectNode) fromNpm.get(MetadataJsonUtil.TIME);
        metadataJsonUtil.addModifeidAndLastVersionTime(lastVersion, time, now);
    }

    private void copyTimeObjectFormArtifactoryToNpm(ObjectNode fromNpm, ObjectNode fromArtifactory) {
        fromNpm.set(MetadataJsonUtil.TIME, fromArtifactory.get(MetadataJsonUtil.TIME));
    }

}
