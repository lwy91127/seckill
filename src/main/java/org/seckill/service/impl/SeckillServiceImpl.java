package org.seckill.service.impl;

import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.SecretKeyResolver;
import org.apache.ibatis.annotations.Param;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by lwy on 2016/5/26.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入service依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    //用于混淆MD5
    private final String slat = "asdkjljasdkljl!##%^*#(asd*&^*$12";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getSeckill(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化:一致性建立在超时基础上
        //1.访问Redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2.访问数据库
            seckill = getSeckill(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3.放入redis
                redisDao.putSeckill(seckill);
            }
        }
        if (seckill == null)
            return new Exposer(false, seckillId);
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        logger.info(now.getTime() + "_" + startTime.getTime() + "_" + endTime.getTime());
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
            logger.info("秒杀未开始！！");
            return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Transactional
    /**
     * 使用注解控制方法的优点：
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行尽可能短，不穿插其他网络操作（RPC/HTTP）或者剥离到事务方法外
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务操作
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            logger.error("seckill data rewrite");
            throw new SeckillException("seckill data rewrite");
        }
        try {
            Date now = new Date();
            //成功，记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                logger.error("seckill repeated");
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存
                int updateCount = seckillDao.reduceNumber(seckillId, now);
                if (updateCount <= 0) {
                    //没有更新到记录，秒杀已经结束
                    logger.error("seckill is over");
                    throw new SeckillCloseException("seckill is over");
                } else {
                    logger.info("秒杀成功");
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    logger.info(successKilled.toString());
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (SeckillException e) {
            logger.error(e.getMessage());
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }

    }

    @Override
    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        return null;
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
