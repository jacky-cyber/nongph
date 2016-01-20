package cn.globalph.common.sandbox;

import org.springframework.stereotype.Component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * @see cn.globalph.common.sandbox.SandBoxHelper
 * @author Jeff Fischer
 */
@Component("blSandBoxHelper")
public class DefaultSandBoxHelper implements SandBoxHelper {

    @Override
    public Long getSandBoxVersionId(EntityManager entityManager, Class<?> linkedObjectType, Long requestedParent) {
        return requestedParent;
    }

    @Override
    public Long getSandBoxVersionId(EntityManager entityManager, Class<?> linkedObjectType, Long requestedParent, Boolean includeSandBoxInheritance) {
        return requestedParent;
    }

    @Override
    public List<Long> mergeCloneIds(EntityManager em, Class<?> type, Long... originalIds) {
        return Arrays.asList(originalIds);
    }

    @Override
    public BiMap<Long, Long> getSandBoxToOriginalMap(EntityManager em, Class<?> type, Long... originalIds) {
        return HashBiMap.create();
    }

    @Override
    public OriginalIdResponse getOriginalId(EntityManager em, Class<?> type, Long id) {
        OriginalIdResponse response = new OriginalIdResponse();
        response.setOriginalId(id);
        return response;
    }

    @Override
    public void setupSandBoxState(Object clone, EntityManager em) {
        //do nothing
    }

    @Override
    public void archiveObject(Object start, EntityManager em) {
        //do nothing
    }

    @Override
    public String[] getSandBoxDiscriminatorFieldList() {
        return new String[]{};
    }

    @Override
    public boolean isSandBoxable(String className) {
        return false;
    }

    @Override
    public boolean isPromote() {
        return false;
    }

    @Override
    public boolean isReject() {
        return false;
    }

    @Override
    public void optionallyIncludeDeletedItemsInQueriesAndCollections(Runnable runnable, boolean includeDeleted) {
        runnable.run();
    }
}
