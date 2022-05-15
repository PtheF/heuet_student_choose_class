package cn.chl.robClass.mapper;

import cn.chl.robClass.entity.RobClassMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRobClassMapper {
    void saveRobRecord(RobClassMessage message);

    void increMember(int cid);
}
