package cn.globalph.common.extensibility.jpa;

/**
 * Represents the columns that get weaved into entities that are marked as site-specific.
 * 
 * @author Jeff Fischer
 */
public interface SiteDiscriminator extends Discriminatable {

    /**
     * @return the site discriminator
     */
    public Long getSiteDiscriminator();

    /**
     * Sets the site discriminator
     * 
     * @param siteDiscriminator
     */
    public void setSiteDiscriminator(Long siteDiscriminator);
}
