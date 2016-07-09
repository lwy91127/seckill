package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者角度设计接口
 * 三个方面：方法的粒度，参数，返回类型（return 类型/异常）
 * Created by lwy on 2016/5/25.
 */
public interface SeckillService {
    /**
     * 查询所有秒杀
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀
     * @param seckillId
     * @return
     */
    Seckill getSeckill(long seckillId);

    /**
     * 秒杀开启时暴露秒杀地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;

    /**
     * 执行秒杀  by 存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}
