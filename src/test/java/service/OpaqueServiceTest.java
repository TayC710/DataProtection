package service;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.opaque.*;
import org.ssohub.crypto.ecc.ristretto255.R255Scalar;
import pojo.Files;
import pojo.OpaqueKeys;
import pojo.OpaqueUser;

import java.util.Map;

import static org.mockito.Mockito.*;

public class OpaqueServiceTest {
    @Mock
    Logger logger;
    @Mock
    SqlSessionFactory sqlSessionFactory;
    @InjectMocks
    OpaqueService opaqueService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetServerKeys() throws Exception {
        Map<String, Object> result = opaqueService.setServerKeys();
        Assert.assertEquals(Map.of("String", "replaceMeWithExpectedResult"), result);
    }

    @Test
    public void testStoreServerInfo() throws Exception {
        opaqueService.storeServerInfo(new ServerInputs(null, null, new Data(new byte[]{(byte) 0}, 0, 0), null, new Data(new byte[]{(byte) 0}, 0, 0), null), new RegistrationRecord(null, 0));
    }

    @Test
    public void testStoreClientInfo() throws Exception {
        opaqueService.storeClientInfo(new ClientInputs(new Data(new byte[]{(byte) 0}, 0, 0), new Data(new byte[]{(byte) 0}, 0, 0), new Data(new byte[]{(byte) 0}, 0, 0)), new R255Scalar(null, 0), new OpaqueSeed(null, 0), new GenerateAuthKeyPairResult(null, null), "fidoUserName");
    }

    @Test
    public void testGetOpaqueUser() throws Exception {
        OpaqueUser result = opaqueService.getOpaqueUser(0);
        Assert.assertEquals(new OpaqueUser(new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}), result);
    }

    @Test
    public void testGetFiles() throws Exception {
        Files result = opaqueService.getFiles(new Data(new byte[]{(byte) 0}, 0, 0));
        Assert.assertEquals(new Files(new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}), result);
    }

    @Test
    public void testGetOpaqueKeys() throws Exception {
        OpaqueKeys result = opaqueService.getOpaqueKeys();
        Assert.assertEquals(new OpaqueKeys(), result);
    }

    @Test
    public void testStoreFile() throws Exception {
        opaqueService.storeFile(new byte[]{(byte) 0}, new Data(new byte[]{(byte) 0}, 0, 0));
    }

    @Test
    public void testGetLocalFile() throws Exception {
        byte[] result = opaqueService.getLocalFile(new Data(new byte[]{(byte) 0}, 0, 0));
        Assert.assertArrayEquals(new byte[]{(byte) 0}, result);
    }

    @Test
    public void testUpdateOpaqueUser() throws Exception {
        opaqueService.updateOpaqueUser(new OpaqueUser(new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}, new byte[]{(byte) 0}));
    }

    @Test
    public void testEncrypt() throws Exception {
        Data result = opaqueService.encrypt(new byte[]{(byte) 0}, new byte[]{(byte) 0});
        Assert.assertEquals(new Data(new byte[]{(byte) 0}, 0, 0), result);
    }

    @Test
    public void testDecrypt() throws Exception {
        byte[] result = opaqueService.decrypt(new byte[]{(byte) 0}, new byte[]{(byte) 0});
        Assert.assertArrayEquals(new byte[]{(byte) 0}, result);
    }
}

// Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme