package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.OpaqueUser;

public interface OpaqueUserMapper {
    int insertNewUser(OpaqueUser user);

    OpaqueUser selectOpaqueUserByUserId(@Param("userId") int userId);

    OpaqueUser selectOpaqueUserByClientIdentity(@Param("clientIdentity") byte[] clientIdentity);

    void deleteByUserId(@Param("userId") int userId);
}
