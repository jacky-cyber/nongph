package cn.globalph.common.crossapp.service;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * A service responsible for allowing secure authentication for a user between the admin and site applications.
 * 
 * This service generates a single use and time sensitive token for a user from the admin application. This token is sent
 * to the user and he must present it in a timely manner to the site application to associate his session as authenticated
 * from the admin applicaiton.
 * 
 * @see CrossAppAdminAuthService
 * @author Andre Azzolini (apazzolini)
 */
public interface CrossAppAuthService {

    public static String AUTH_FROM_ADMIN_URL_PARAM = "blAuthToken";
    public static String AUTH_FROM_ADMIN_SESSION_VAR = "blAuthedFromAdmin";

    /**
     * Consumes an authentication token for the given user id and token. This method will additionally register the
     * current session (acquired from the {@link RedirectAttributes} argument) as having an admin authentication for the
     * given adminUserId.
     * 
     * @param adminUserId
     * @param token
     * @param ra
     * @throws IllegalArgumentException
     */
    public void useSiteAuthToken(Long adminUserId, String token) throws IllegalArgumentException;

    /**
     * @return whether or not the user is currently authenticated from the admin
     */
    public boolean isAuthedFromAdmin();

    /**
     * @return the id of the currently authenticated admin user. Returns null if there is no currently authenticated user
     */
    public Long getCurrentAuthedAdminId();

    /**
     * @return whether or not the user is currently authenticated from the admin and also has the CSR role
     */
    public boolean hasCsrPermission();

}