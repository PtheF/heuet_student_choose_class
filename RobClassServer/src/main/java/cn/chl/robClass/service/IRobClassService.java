package cn.chl.robClass.service;

import cn.chl.robClass.controller.entity.ResponseData;

public interface IRobClassService {
    ResponseData<String> robClass(String id, String typ);
}
