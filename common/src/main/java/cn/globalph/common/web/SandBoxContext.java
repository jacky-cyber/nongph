package cn.globalph.common.web;


import cn.globalph.common.classloader.release.ThreadLocalManager;

public class SandBoxContext {
    
    private static final ThreadLocal<SandBoxContext> SANDBOXCONTEXT = ThreadLocalManager.createThreadLocal(SandBoxContext.class);
    
    public static SandBoxContext getSandBoxContext() {
        return SANDBOXCONTEXT.get();
    }
    
    public static void setSandBoxContext(SandBoxContext sandBoxContext) {
        SANDBOXCONTEXT.set(sandBoxContext);
    }

    protected Long sandBoxId;
    protected Boolean previewMode = false;

    /**
     * @return the sandBoxName
     */
    public Long getSandBoxId() {
        return sandBoxId;
    }
    
    /**
     * @param sandBoxId the sandBoxName to set
     */
    public void setSandBoxId(Long sandBoxId) {
        this.sandBoxId = sandBoxId;
    }

    public Boolean getPreviewMode() {
        return previewMode;
    }

    public void setPreviewMode(Boolean previewMode) {
        this.previewMode = previewMode;
    }

    public SandBoxContext clone() {
        SandBoxContext myContext = new SandBoxContext();
        myContext.setSandBoxId(getSandBoxId());
        myContext.setPreviewMode(getPreviewMode());

        return myContext;
    }
}
