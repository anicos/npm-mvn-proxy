package pl.anicos.nmp.publish.binary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.artifactory.VersionNumberExtractor;
import pl.anicos.nmp.publish.model.Attachment;
import pl.anicos.nmp.publish.model.NpmPublishBody;

import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

@Component
public class NpmPublishBodyToByteArray implements Function<NpmPublishBody, byte[]> {


    private final Base64.Decoder decoder;
    private final VersionNumberExtractor versionNumberExtractor;

    @Autowired
    public NpmPublishBodyToByteArray(Base64.Decoder decoder, VersionNumberExtractor versionNumberExtractor) {
        this.decoder = decoder;
        this.versionNumberExtractor = versionNumberExtractor;
    }

    @Override
    public byte[] apply(NpmPublishBody npmPublishBody) {
        Map<String, Attachment> attachments = npmPublishBody.getAttachments();
        String binaryName = versionNumberExtractor.getVersionNumber(attachments);
        String binaryBase64 = attachments.get(binaryName).getData();
        return decoder.decode(binaryBase64.getBytes());
    }
}
