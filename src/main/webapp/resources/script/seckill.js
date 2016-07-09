//存放交互逻辑js代码
//javascript 模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登陆，计时交互
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //绑定phone
                //控制输出
                var killPhoneModel = $('#killPhoneModal');
                killPhoneModel.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            console.log("login with phone");
            //已经登陆
            //计时交互
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    console.log("get system time success");
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            })

        }
    },

    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone))
            return true;
        else
            return false;
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillbox = $('#seckill-box');
        //console.log(seckillbox);
        //时间判断
        if (nowTime > endTime) {
            console.log("秒杀结束");
            //秒杀结束
            seckillbox.html('秒杀结束！');
        } else if (nowTime < startTime) {
            console.log("秒杀未开始");
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);
            seckillbox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillbox.html(format);
                //时间完成后回调事件
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制实现逻辑，执行秒杀
                seckill.handleSeckillKill(seckillId, seckillbox);
            });
        } else {
            console.log("秒杀开始");
            //秒杀开始
            seckill.handleSeckillKill(seckillId, seckillbox);
        }
    },

    handleSeckillKill: function (seckillId, node) {
        //获取秒杀地址，控制实现逻辑，执行秒杀
        //console.log("handleSeckillKill");
        node.hide().html('<button class="btn btn-primary btg-lg" id="killBtn">开始秒杀</button>')

        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            //回调函数中执行交互流程
            console.log("result:" + JSON.stringify(result));
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:" + killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //绑定执行秒杀请求
                        //禁用按钮
                        $(this).addClass('disabled');
                        //发送请求
                        $.post(killUrl, {}, function (result) {
                            console.log("result:" + JSON.stringify(result));
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        })
                    });
                    node.show(300);
                } else {
                    //时间偏差，未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新进入倒计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('result:' + JSON.stringify(result));
            }
        });
    }

}