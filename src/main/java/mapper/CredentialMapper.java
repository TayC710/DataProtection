package mapper;

import org.apache.ibatis.annotations.Param;
import pojo.Credential;

import java.util.List;

public interface CredentialMapper {
    Credential getCredentialById(int id);

    List<Credential> getCredentialByCredentialId(@Param("credentialId") byte[] credentialId);

    List<Credential> getCredentialByUserId(@Param("userId") int userId);

    List<Credential> getCredentialByUserHandle(@Param("userHandle") byte[] userHandle);

    Credential getCredentialByPublicKeyCose(@Param("publicKeyCose") byte[] publicKeyCose);

    List<Credential> getCredentialsByUserName(@Param("userName") String userName);

    Credential getCredentialByCredentialIdAndUserHandle(@Param("credentialId") byte[] credentialId, @Param("userHandle") byte[] userHandle);

    //    void insertNewCredential(@Param("credentialId") byte[] credentialId,
//                             @Param("userId") int userId,
//                             @Param("userHandle") byte[] userHandle,
//                             @Param("publicKeyCose") byte[] publicKeyCose,
//                             @Param("signatureCount") int signatureCount);
    void insertNewCredential(Credential credential);

    void updateAuthentication(@Param("signatureCount") int signatureCount,
                              @Param("userId") int userId,
                              @Param("credentialId") byte[] credentialId);

}
