package pl.anicos.nmp.publish.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetadataJsonUtil {

    public static final String ERROR_NO_README_DATA_FOUND = "ERROR: No README data found!";
    public static final String TIME = "time";
    public static final String MODIFIED = "modified";
    public static final String CREATED = "created";
    public static final String ATTACHMENTS = "_attachments";
    public static final String REV = "_rev";
    public static final String VERSIONS = "versions";
    public static final String README = "readme";
    public static final String DIRECTORIES = "directories";
    public static final String README_FILENAME = "readmeFilename";
    public static final String EMPTY_STRING = "";
    public static final String DIST_TAGS = "dist-tags";
    public static final String LATEST = "latest";
    public static final String ID = "_id";
    public static final String DIST = "dist";
    public static final String TARBALL = "tarball";

    private final ValuesGenerator valuesGenerator;
    private final TarballLinkUpdater tarballLinkUpdater;

    @Autowired
    public MetadataJsonUtil(ValuesGenerator valuesGenerator, TarballLinkUpdater tarballLinkUpdater) {
        this.valuesGenerator = valuesGenerator;
        this.tarballLinkUpdater = tarballLinkUpdater;
    }

    public ObjectNode initDefaultValues(ObjectNode objectNode) {
        objectNode.putObject(ATTACHMENTS);
        objectNode.put(REV, valuesGenerator.generateUUID());

        String latest = getLastVersion(objectNode);
        ObjectNode latestVersion = (ObjectNode) objectNode.get(VERSIONS).get(latest);
        if (latestVersion.get(README) != null && ERROR_NO_README_DATA_FOUND.equals(latestVersion.get(README).asText())) {
            latestVersion.remove(README);
        }
        if (!latestVersion.has(DIRECTORIES)) {
            latestVersion.putObject(DIRECTORIES);
        }

        if (!objectNode.has(README_FILENAME)) {
            objectNode.put(README_FILENAME, EMPTY_STRING);
        }
        return objectNode;
    }

    public String getLastVersion(ObjectNode objectNode) {
        return objectNode.get(DIST_TAGS).get(LATEST).asText();
    }

    public String getCurrentDate() {
        return valuesGenerator.getCurrentDate();
    }

    public ObjectNode addModifeidAndLastVersionTime(String latest, ObjectNode time, String now) {
        time.put(MetadataJsonUtil.MODIFIED, now);
        time.put(latest, now);
        return time;
    }

    public void updateLatestTarball(ObjectNode objectNode) {
        String lastVersion = getLastVersion(objectNode);
        ObjectNode distNode = (ObjectNode) objectNode.get(VERSIONS).get(lastVersion).get(DIST);
        String tarball = distNode.get(TARBALL).asText();
        String updatedTarball = tarballLinkUpdater.addVersionToLink(tarball, lastVersion);
        distNode.put(TARBALL, updatedTarball);
    }
}
