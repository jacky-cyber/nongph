package cn.globalph.passport.service;

import java.util.List;

import cn.globalph.passport.domain.Community;

public interface CommunityService {

	public List<Community> findFirstLevelComms();
	
	public List<Community> findChildCommunities(Long id);
	
	public Community readCommunityById(Long communityId);
	
}
