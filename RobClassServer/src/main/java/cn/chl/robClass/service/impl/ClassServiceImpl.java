package cn.chl.robClass.service.impl;

import cn.chl.robClass.entity.po.ClassPO;
import cn.chl.robClass.entity.po.ClassStockPO;
import cn.chl.robClass.entity.po.FullClassPO;
import cn.chl.robClass.entity.vo.ClassVO;
import cn.chl.robClass.entity.vo.EleClassVO;
import cn.chl.robClass.entity.vo.PEClassVO;
import cn.chl.robClass.entity.vo.SelectClassPageVO;
import cn.chl.robClass.mapper.IClassMapper;
import cn.chl.robClass.service.IClassService;
import cn.chl.robClass.utils.CacheClient;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.chl.robClass.utils.RedisConstant.*;

@Service
@Slf4j
public class ClassServiceImpl implements IClassService {

    @Autowired
    private IClassMapper mapper;

    @Autowired
    private StringRedisTemplate template;

    @Resource
    private CacheClient cacheClient;

    @Override
    public List<ClassStockPO> getClassStock() {
        return mapper.getClassStock();
    }

    @Override
    public SelectClassPageVO getSelectClassPage(String clsNo) {


        // 获取课程介绍信息
        List<ClassPO> cacheList = cacheClient.getCacheList(CLASS_PREFIX, clsNo, new TypeReference<List<ClassPO>>() {},
                (cno) -> mapper.getClassDes(cno),
                3, TimeUnit.HOURS);

        List<PEClassVO> pes = new ArrayList<>();
        List<EleClassVO> els = new ArrayList<>();

        // 从Redis中获取课程余量已选人数等信息
        cacheList.forEach(cls -> {

            int id = cls.getId();
            int remain;
            int selected;

            String selectedStr = template.opsForValue().get(CLASS_SELECT_PREFIX + id);

            String remainStr = template.opsForValue().get(CLASS_REMAIN_PREFIX + id);

            if(selectedStr == null || remainStr == null){
                throw new RuntimeException("未查到缓存，请进行缓存预热");
            }

            remain = Integer.parseInt(remainStr);
            selected = Integer.parseInt(selectedStr);

            log.info("class {} stock info=> remain:{}, selected:{}", id, remain, selected);

            if(cls.getTyp() == 1){
                EleClassVO elec = new EleClassVO();
                elec.setId(id);
                elec.setCapacity(remain + selected);
                elec.setSelected(selected);
                elec.setName(cls.getName());
                elec.setTeacher(cls.getTeacher());
                elec.setTime(cls.getcTime());

                els.add(elec);
            }else{
                PEClassVO pe = new PEClassVO();
                pe.setId(id);
                pe.setCapacity(remain + selected);
                pe.setRemain(remain);
                pe.setName(cls.getName());
                pe.setTeacher(cls.getTeacher());
                pe.setTime(cls.getcTime());

                pes.add(pe);
            }

        });

        // 整合成页面视图返回
        SelectClassPageVO page = new SelectClassPageVO();
        page.setPes(pes);
        page.setEles(els);

        return page;
    }

    @Override
    public List<ClassVO> getMyClass(String uid, String clsNo) {

        Set<String> clsIds = template.opsForSet().members(STU_SELECT_PREFIX + uid);

        if(clsIds == null || clsIds.isEmpty()){
            return null;
        }

        // 获取课程介绍信息
        List<ClassPO> cacheList = cacheClient.getCacheList(CLASS_PREFIX, clsNo, new TypeReference<List<ClassPO>>() {},
                (cno) -> mapper.getClassDes(cno),
                3, TimeUnit.HOURS);

        return cacheList.stream()
                .filter(cls -> clsIds.contains(cls.getId().toString()))
                .map(clsPO -> {
                    ClassVO vo = new ClassVO();
                    vo.setId(clsPO.getId());
                    vo.setName(clsPO.getName());
                    vo.setTeacher(clsPO.getTeacher());
                    vo.setTime(clsPO.getcTime());

                    return vo;
                }).collect(Collectors.toList());

    }
}
