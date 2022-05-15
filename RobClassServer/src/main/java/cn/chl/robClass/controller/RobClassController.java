package cn.chl.robClass.controller;

import cn.chl.robClass.controller.entity.ResponseData;
import cn.chl.robClass.controller.entity.RobClassRequest;
import cn.chl.robClass.service.IRobClassService;
import cn.chl.robClass.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/robClass")
public class RobClassController {

    @Autowired
    private IRobClassService service;

    @PostMapping("/rob")
    public ResponseData<String> rob(@RequestBody RobClassRequest rob){

        return service.robClass(rob.getId(), rob.getTyp());
    }

}
