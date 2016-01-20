package cn.globalph.passport.service;

import java.util.List;

import javax.annotation.Resource;

import cn.globalph.passport.dao.CommunityDao;
import cn.globalph.passport.domain.Community;

import org.springframework.stereotype.Service;

@Service("nphCommunityService")
public class CommunityServiceImpl implements CommunityService {

	@Resource(name="nphCommunityDao")
	protected CommunityDao communityDao;
	
	@Override
	public List<Community> findFirstLevelComms() {
		return communityDao.findFirstLevelCommunities();
	}

	@Override
	public List<Community> findChildCommunities(Long id) {
		return communityDao.findChildCommunities(id);
	}

	@Override
	public Community readCommunityById(Long communityId) {
		return communityDao.readCommunityById(communityId);
	}

}
