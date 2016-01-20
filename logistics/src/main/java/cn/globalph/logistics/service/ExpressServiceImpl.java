package cn.globalph.logistics.service;

import cn.globalph.logistics.dao.ExpressDao;
import cn.globalph.logistics.ph.Express;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author steven
 * @since 6/19/15
 */
@Service("phExpressService")
public class ExpressServiceImpl implements ExpressService {

    private static Map<String, Integer> sequenceMap = new HashMap<>();

    @Resource(name = "phExpressDao")
    protected ExpressDao expressDao;

    @Override
    public Express save(Express express) {
        return expressDao.save(express);
    }

    @Override
    public Express readByExpressNo(String expressNo) {
        return expressDao.readByExpressNo(expressNo);
    }

    @Override
    public Express create() {
        return expressDao.create();
    }

    @Override
    public void delete(Express express) {
        expressDao.delete(express);
    }

    @Override
    public void deleteByExpressNo(String expressNo) {
        Express express = new Express();
        express.setExpressNo(expressNo);
        delete(express);
    }

    @Override
    public Express readByOrderNo(String orderNo) {
        return expressDao.readByOrderNo(orderNo);
    }

    @Override
    public String generateExpressNo() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss");
        String dateStr = simpleDateFormat.format(now);
        if (sequenceMap.containsKey(dateStr)) {
            Integer currentSequence = sequenceMap.get(dateStr);
            Integer newSequence = currentSequence + 1;
            sequenceMap.put(dateStr, newSequence);
            return "PH" + dateStr + newSequence;
        } else {
            for (String key : sequenceMap.keySet()) {
                sequenceMap.remove(key);
            }
            sequenceMap.put(dateStr, 1);
            return "PH" + dateStr + 1;
        }
    }
}
