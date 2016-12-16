package pl.anicos.nmp.publish.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataCreator {

    private final MetadataJsonUtil metadataJsonUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MetadataCreator(MetadataJsonUtil metadataJsonUtil) {
        this.metadataJsonUtil = metadataJsonUtil;
    }

    public ObjectNode create(ObjectNode objectNode) {
        logger.info("Create existing metadata.json");
        ObjectNode newNode = metadataJsonUtil.initDefaultValues(objectNode);
        String latest = metadataJsonUtil.getLastVersion(newNode);
        metadataJsonUtil.updateLatestTarball(newNode);

        addTimeObject(newNode, latest);

        return newNode;
    }

    private void addTimeObject(ObjectNode objectNode, String latest) {
        ObjectNode time = objectNode.putObject(MetadataJsonUtil.TIME);
        String now = metadataJsonUtil.getCurrentDate();

        metadataJsonUtil.addModifeidAndLastVersionTime(latest, time, now);
        time.put(MetadataJsonUtil.CREATED, now);
    }

}
