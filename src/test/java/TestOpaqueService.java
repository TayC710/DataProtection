import org.junit.Test;
import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.opaque.GenerateAuthKeyPairResult;
import service.OpaqueService;

import java.util.Arrays;
import java.util.Map;

public class TestOpaqueService {

    @Test
    public void testSetServerKeys() {
        OpaqueService opaqueService = new OpaqueService();
        Map<String, Object> result = opaqueService.setServerKeys();

        GenerateAuthKeyPairResult keyPairResult = (GenerateAuthKeyPairResult) result.get("GenerateAuthKeyPairResult");
        Data oprfSeed = (Data) result.get("Data");

        System.out.println("serverPublicKey=" + Arrays.toString(keyPairResult.getPublicKey().toBytes()));
        System.out.println("serverPrivateKey=" + Arrays.toString(keyPairResult.getPrivateKey().toBytes()));
        System.out.println("oprfSeed=" + Arrays.toString(oprfSeed.toBytes()));
    }
}
