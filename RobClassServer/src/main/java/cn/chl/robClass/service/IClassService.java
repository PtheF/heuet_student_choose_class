package cn.chl.robClass.service;

import cn.chl.robClass.entity.po.ClassStockPO;
import cn.chl.robClass.entity.po.FullClassPO;
import cn.chl.robClass.entity.vo.ClassVO;
import cn.chl.robClass.entity.vo.EleClassVO;
import cn.chl.robClass.entity.vo.SelectClassPageVO;

import java.util.List;

public interface IClassService {

    List<ClassStockPO> getClassStock();

    SelectClassPageVO getSelectClassPage(String clsNo);

    List<ClassVO> getMyClass(String uid, String clsNo);
}
