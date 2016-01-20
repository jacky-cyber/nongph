package cn.globalph.openadmin.server.security.service;

import cn.globalph.common.security.util.PasswordChange;
import cn.globalph.common.service.GenericResponse;
import cn.globalph.openadmin.server.security.domain.AdminPermission;
import cn.globalph.openadmin.server.security.domain.AdminRole;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.security.service.type.PermissionType;

import org.springframework.security.authentication.dao.SaltSource;

import java.util.List;

/**
 * 
 * @felix.wu
 *
 */
public interface AdminSecurityService {

    public static final String[] DEFAULT_PERMISSIONS = { "PERMISSION_OTHER_DEFAULT", "PERMISSION_ALL_USER_SANDBOX" };

    List<AdminUser> readAllAdminUsers();
    AdminUser readAdminUserById(Long id);
    AdminUser readAdminUserByUserName(String userName);
    AdminUser saveAdminUser(AdminUser user);
    void deleteAdminUser(AdminUser user);

    List<AdminRole> readAllAdminRoles();
    AdminRole readAdminRoleById(Long id);
    AdminRole saveAdminRole(AdminRole role);
    void deleteAdminRole(AdminRole role);

    List<AdminPermission> readAllAdminPermissions();
    AdminPermission readAdminPermissionById(Long id);
    AdminPermission saveAdminPermission(AdminPermission permission);
    void deleteAdminPermission(AdminPermission permission);
    AdminUser changePassword(PasswordChange passwordChange);

    boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName);
    boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName);

    /**
     * Looks up the corresponding AdminUser and emails the address on file with
     * the associated username.
     *
     * @param emailAddress
     * @return Response can contain errors including (notFound)
     *
     */
    GenericResponse sendForgotUsernameNotification(String emailAddress);

    /**
     * Generates an access token and then emails the user.
     *
     * @param userName
     * @return Response can contain errors including (invalidEmail, invalidUsername, inactiveUser)
     * 
     */
    GenericResponse sendResetPasswordNotification(String userName);
    
    /**
     * Updates the password for the passed in user only if the passed
     * in token is valid for that user.
     *
     * @param username Name of the user
     * @param token Valid reset token
     * @param password new password
     *
     * @return Response can contain errors including (invalidUsername, inactiveUser, invalidToken, invalidPassword, tokenExpired, passwordMismatch)
     */
    GenericResponse resetPasswordUsingToken(String username, String token, String password, String confirmPassword);
    GenericResponse changePassword(String username, String oldPassword, String password, String confirmPassword);
    
    /**
     * @deprecated use {@link #getSaltSource()} instead
     */
    @Deprecated
    public String getSalt();
    
    /**
     * @deprecated use {@link #setSaltSource(SaltSource)} instead
     */
    @Deprecated
    public void setSalt(String salt);

    /**
     * Returns the {@link SaltSource} used with the blAdminPasswordEncoder to encrypt the user password. Usually configured in
     * applicationContext-admin-security.xml. This is not a required property and will return null if not configured
     */
    public SaltSource getSaltSource();
    
    /**
     * Sets the {@link SaltSource} used with blAdminPasswordencoder to encrypt the user password. Usually configured within
     * applicationContext-admin-security.xml
     * 
     * @param saltSource
     */
    public void setSaltSource(SaltSource saltSource);
    
    /**
     * Gets the salt object for the current admin user. By default this delegates to {@link #getSaltSource()}. If there is
     * not a {@link SaltSource} configured ({@link #getSaltSource()} returns null) then this also returns null.
     * 
     * @param user
     * @param unencodedPassword
     * @return the salt for the current admin user
     */
    public Object getSalt(AdminUser user, String unencodedPassword);

    /**
     * Returns a list of admin users that match the given email. This could potentially return more than one user if the
     * admin.user.requireUniqueEmailAddress property is set to false.
     * @param email
     * @return
     */
    public List<AdminUser> readAdminUsersByEmail(String email);

}
