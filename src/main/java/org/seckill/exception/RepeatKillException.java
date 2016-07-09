package org.seckill.exception;

/**
 * 重复秒杀异常(runtime exception)
 * Created by lwy on 2016/5/25.
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
