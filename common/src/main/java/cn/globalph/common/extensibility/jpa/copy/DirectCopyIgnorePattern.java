package cn.globalph.common.extensibility.jpa.copy;

/**
 * @author Jeff Fischer
 */
public class DirectCopyIgnorePattern {

    private String[] patterns;
    private String[] templateTokenPatterns;

    public String[] getPatterns() {
        return patterns;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    public String[] getTemplateTokenPatterns() {
        return templateTokenPatterns;
    }

    public void setTemplateTokenPatterns(String[] templateTokenPatterns) {
        this.templateTokenPatterns = templateTokenPatterns;
    }

}
