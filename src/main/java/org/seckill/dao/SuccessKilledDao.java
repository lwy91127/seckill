package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by lwy on 2016/5/23.
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复，通过联合主键
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
