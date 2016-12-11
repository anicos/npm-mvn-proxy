package pl.anicos.nmp.publish.binary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.nmp.publish.model.Attachment;
import pl.anicos.nmp.publish.model.NpmPublishBody;

import java.util.Base64;
import java.util.Map;
import java.util.function.Function;

@Component
public class NpmPublishBodyToByteArray implements Function<NpmPublishBody, byte[]> {


    private final Base64.Decoder decoder;

    @Autowired
    public NpmPublishBodyToByteArray(Base64.Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public byte[] apply(NpmPublishBody npmPublishBody) {
        Map<String, Attachment> attachments = npmPublishBody.getAttachments();
        String binaryName = attachments.keySet().stream().findFirst().get();
        String binaryBase64 = attachments.get(binaryName).getData();
        return decoder.decode(binaryBase64.getBytes());
    }
}
