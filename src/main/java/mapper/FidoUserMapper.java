package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.FidoUser;

public interface FidoUserMapper {
    FidoUser getUserByID(@Param("userId") int userId);

    FidoUser getUserByName(@Param("userName") String userName);

    FidoUser getUserByHandle(@Param("userHandle") byte[] userHandle);

    void insertNewUser(FidoUser fidoUser);

    void updateFilesByUserName(@Param("userName") String userName, @Param("files") String files);

    void updateOpaqueUsersByUserName(@Param("userName") String userName, @Param("opaqueUsers") String opaqueUsers);

}
