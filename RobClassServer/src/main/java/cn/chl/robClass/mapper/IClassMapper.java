package cn.chl.robClass.mapper;

import cn.chl.robClass.entity.po.ClassPO;
import cn.chl.robClass.entity.po.ClassStockPO;
import cn.chl.robClass.entity.po.FullClassPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IClassMapper {

    void insertClass(ClassPO classPO);

    void insertClassStock(ClassStockPO classStockPO);

    List<ClassPO> getClassDes(String cno);

    List<ClassStockPO> getClassStock();
}
