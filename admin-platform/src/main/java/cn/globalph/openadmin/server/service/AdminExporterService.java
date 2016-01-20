package cn.globalph.openadmin.server.service;

import cn.globalph.openadmin.dto.AdminExporterDTO;

import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
public interface AdminExporterService {

    @Secured("PERMISSION_OTHER_DEFAULT")
    public List<AdminExporterDTO> getExporters(String type);

}
