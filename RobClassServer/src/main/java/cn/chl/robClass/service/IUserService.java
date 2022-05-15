package cn.chl.robClass.service;

import cn.chl.robClass.entity.vo.UserVO;

public interface IUserService {

    UserVO login(String id, String password);
}
