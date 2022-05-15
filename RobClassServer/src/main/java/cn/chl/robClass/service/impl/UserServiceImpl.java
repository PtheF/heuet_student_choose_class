package cn.chl.robClass.service.impl;

import cn.chl.robClass.entity.po.UserPO;
import cn.chl.robClass.entity.vo.UserVO;
import cn.chl.robClass.mapper.IUserMapper;
import cn.chl.robClass.service.IUserService;
import cn.chl.robClass.utils.CacheClient;
import cn.chl.robClass.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static cn.chl.robClass.utils.RedisConstant.USER_PREFIX;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper mapper;

    @Resource
    private CacheClient cacheClient;

    public UserVO login(String id, String password) {

        UserPO user = cacheClient.getCacheNotSave(USER_PREFIX, id, UserPO.class, (i) -> mapper.queryUserById(i));

        if(user == null){
            return null;
        }

        boolean login = MD5Utils.verify(password, user.getPassword());

        if(!login){
            return null;
        }

        return new UserVO()
                .setId(user.getId())
                .setName(user.getName())
                .setClsNo(user.getClsNo())
                .setGender(user.getGender());
    }
}
