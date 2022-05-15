package cn.chl.robClass.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用于恢复数据库，在项目启动时执行，将数据恢复为初始状态
 */
@Mapper
public interface InitialMapper {

    void initStock();

    void clearRecord();
}
