package cn.chl.robClass.utils;

import cn.chl.robClass.entity.vo.UserVO;

public class UserHolder {

    private static final ThreadLocal<UserVO> USER_HOLDER = new ThreadLocal<UserVO>();

    public static void holdUser(UserVO user){
        USER_HOLDER.set(user);
    }

    public static UserVO getUser(){
        return USER_HOLDER.get();
    }

    public static void freeUser(){
        USER_HOLDER.remove();
    }
}
