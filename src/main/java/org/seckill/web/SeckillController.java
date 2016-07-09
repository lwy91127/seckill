package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by lwy on 2016/5/26.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null)
            return "redirect:/seckill/list";
        Seckill seckill = seckillService.getSeckill(seckillId);
        if(seckill == null)
            return "forward:/seckill/list";
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    //ajax 返回json
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try{
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable Long seckillId,@PathVariable String md5, @CookieValue(value = "killPhone",required = false)Long phone){
        logger.info("执行秒杀");
        if(phone == null)
            return new SeckillResult<SeckillExecution>(false,"未注册");
        SeckillResult<SeckillExecution> result;
        try{
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,phone,md5);
            result = new SeckillResult<>(true, seckillExecution);
        }catch (RepeatKillException e){
            logger.error(SeckillStateEnum.REPEAT_KILL.getStateInfo());
            return new SeckillResult<SeckillExecution>(true,new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL));
        }catch (SeckillCloseException e){
            logger.error(SeckillStateEnum.END.getStateInfo());
            return new SeckillResult<SeckillExecution>(true,new SeckillExecution(seckillId, SeckillStateEnum.END));
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillResult<SeckillExecution>(true,new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR));
        }
        logger.info(result.getData().toString());
        return result;
    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date date = new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }

}
