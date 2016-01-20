package cn.globalph.openadmin.security;

import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.service.SandBoxService;
import cn.globalph.common.web.SandBoxContext;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.security.remote.SecurityVerifier;
import cn.globalph.openadmin.server.service.SandBoxMode;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Jeff Fischer
 */
@Component("blAdminSandBoxFilter")
public class AdminSandBoxFilter extends OncePerRequestFilter {

    private static final String SANDBOX_ADMIN_ID_VAR = "blAdminCurrentSandboxId";
    private static String SANDBOX_ID_VAR = "blSandboxId";

    @Resource(name="blSandBoxService")
    protected SandBoxService sandBoxService;

    @Resource(name="blAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // this filter is not currently wired in
        HttpSession session = request.getSession();
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        if (adminUser == null) {
            //clear any sandbox
            session.removeAttribute(SANDBOX_ADMIN_ID_VAR);
            SandBoxContext.setSandBoxContext(null);
        } else {
            WebRequestContext brc = WebRequestContext.getWebRequestContext();
            if (brc != null) {
                brc.getAdditionalProperties().put("adminUser", adminUser);
            }

            Long overrideSandBoxId = adminUser.getOverrideSandBox() == null ? null : adminUser.getOverrideSandBox().getId();
            SandBox sandBox = sandBoxService.retrieveUserSandBox(adminUser.getId(), overrideSandBoxId, adminUser.getLogin());
            session.setAttribute(SANDBOX_ADMIN_ID_VAR, sandBox.getId());
            session.removeAttribute(SANDBOX_ID_VAR);
            AdminSandBoxContext context = new AdminSandBoxContext();
            context.setSandBoxId(sandBox.getId());
            context.setSandBoxMode(SandBoxMode.IMMEDIATE_COMMIT);
            context.setAdminUser(adminUser);
            SandBoxContext.setSandBoxContext(context);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            SandBoxContext.setSandBoxContext(null);
        }
    }
}
