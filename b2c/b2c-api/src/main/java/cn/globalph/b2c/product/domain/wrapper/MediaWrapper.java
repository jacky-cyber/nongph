package cn.globalph.b2c.product.domain.wrapper;

import cn.globalph.b2c.product.domain.wrap.MediaWrap;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.common.media.domain.Media;

public class MediaWrapper implements APIWrapper<Media, MediaWrap> {
    
    @Override
    public MediaWrap wrapDetails(Media media) {
    	MediaWrap wrap = new MediaWrap();
    	wrap.setId( media.getId() );
    	wrap.setTitle( media.getTitle() );
    	wrap.setAltText( media.getAltText() );
    	wrap.setTags( media.getTags() );
    	wrap.setUrl( media.getUrl() );
    	return wrap;
    }

    @Override
    public MediaWrap wrapSummary(Media media) {
        return wrapDetails(media);
    }
}
