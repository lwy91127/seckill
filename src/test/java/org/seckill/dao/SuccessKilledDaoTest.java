package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by lwy on 2016/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        int insertCount = successKilledDao.insertSuccessKilled(1000L,15000201503L);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L,15000201503L);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}