package cn.globalph.passport.dao;

import java.util.List;

import cn.globalph.passport.domain.Community;

public interface CommunityDao {

	public List<Community> findFirstLevelCommunities();
	
	public List<Community> findChildCommunities(Long id);
	
	public Community readCommunityById(Long communityId);

}
