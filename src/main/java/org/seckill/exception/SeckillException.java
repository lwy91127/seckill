package org.seckill.exception;

/**
 * 秒杀相关的业务异常
 * Created by lwy on 2016/5/25.
 */
public class SeckillException extends RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
