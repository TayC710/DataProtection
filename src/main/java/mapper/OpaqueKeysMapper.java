package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.OpaqueKeys;

public interface OpaqueKeysMapper {
    OpaqueKeys getOpaqueKeys();
    int insertOpaqueKeys(@Param("serverPublicKey") byte[] serverPublicKey, @Param("serverPrivateKey") byte[] serverPrivateKey, @Param("oprfSeed") byte[] oprfSeed);
}
