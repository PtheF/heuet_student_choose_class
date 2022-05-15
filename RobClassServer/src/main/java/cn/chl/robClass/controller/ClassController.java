package cn.chl.robClass.controller;

import cn.chl.robClass.controller.entity.ResponseData;
import cn.chl.robClass.entity.vo.*;
import cn.chl.robClass.service.IClassService;
import cn.chl.robClass.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private IClassService service;

    @GetMapping("/selectClass")
    public ResponseData<SelectClassPageVO> getSelectClassPage(){

        UserVO user = UserHolder.getUser();

        SelectClassPageVO classPage = service.getSelectClassPage(user.getClsNo());

        if(classPage == null){
            return ResponseData.fail(null);
        }

        return ResponseData.ok(classPage);
    }

    @GetMapping("/my")
    public ResponseData<List<ClassVO>> getMyClass(){

        UserVO user = UserHolder.getUser();

        List<ClassVO> myClass = service.getMyClass(user.getId(), user.getClsNo());

        if(myClass == null || myClass.isEmpty())
            return ResponseData.fail(null);

        return ResponseData.ok(myClass);
    }
}
