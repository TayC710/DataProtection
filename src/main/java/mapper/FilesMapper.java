package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.Files;

public interface FilesMapper {
    void insertFileRecord(Files file);

    Files selectFileByCredentialIdentifier(@Param("credentialIdentifier") byte[] credentialIdentifier);
    Files selectFileByClientIdentity(@Param("clientIdentity") byte[] clientIdentity);

    void updatePathByCredentialIdentifier(@Param("path") String path, @Param("credentialIdentifier") byte[] credentialIdentifier);

    void deleteByPath(@Param("path") String path);
}
