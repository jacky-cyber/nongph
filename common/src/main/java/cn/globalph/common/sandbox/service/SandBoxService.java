package cn.globalph.common.sandbox.service;

import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SandBoxService {

    public SandBox retrieveSandBoxById(Long id);
    
    public List<SandBox> retrieveAllSandBoxes();

    /**
     * Returns the sandbox currently associated with the passed in userId.
     * If one is not associated, it uses (or creates) a default user sandbox with the
     * name:   user:username.
     *
     * @param adminUser
     * @return
     */
    public SandBox retrieveUserSandBox(Long authorId, Long overrideSandBoxId, String sandBoxName);
    
    public SandBox retrieveUserSandBoxForParent(Long authorId, Long parentSandBoxId);

    public List<SandBox> retrievePreviewSandBoxes(Long authorId);
    
    public List<SandBox> retrieveSandBoxesByType(SandBoxType type);
    
    public Map<Long, String> retrieveAuthorNamesForSandBoxes(Set<Long> sandBoxIds);

    public SandBox createSandBox(String sandBoxName, SandBoxType sandBoxType) throws Exception;

    public SandBox createUserSandBox(Long authorId, SandBox approvalSandBox);
    
    public SandBox retrieveSandBox(String sandBoxName, SandBoxType sandBoxType);

    public SandBox createDefaultSandBox();

    /**
     * Reads all SandBoxes that are of type {@link SandBoxType.USER} and belong to the given
     * user.
     * 
     * @param authorId
     * @return a list of SandBox belonging to the user
     */
    public List<SandBox> retrieveAllUserSandBoxes(Long authorId);

}
