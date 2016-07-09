-- 数据库初始化脚本

-- 创建数据库
CREATE database seckill;

use seckill;

-- 创建秒杀数据表

CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '商品数量',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)Engine=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';


-- 初始化数据
insert into seckill(name,number,start_time,end_time)
values ('1000元秒杀iphone 6s',100,'2016-5-23 00:00:00','2016-5-24 00:00:00'),
('1元秒杀iphone 6',10,'2016-5-23 00:00:00','2016-5-24 00:00:00'),
('100元秒杀小米5',100,'2016-5-23 00:00:00','2016-5-24 00:00:00'),
('100元秒杀魅族4',100,'2016-5-23 00:00:00','2016-5-24 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed(
  `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
  `user_phone` bigint NOT NULL COMMENT '用户手机号码',
  `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态表示：-1：无效，0：成功，1：已付款 。。。',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
  key idx_create_time(create_time)
)Engine=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';