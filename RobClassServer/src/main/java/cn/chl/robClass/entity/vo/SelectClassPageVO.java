package cn.chl.robClass.entity.vo;

import java.util.List;

/**
 * 选课页面
 */
public class SelectClassPageVO {

    private List<EleClassVO> eles;

    private List<PEClassVO> pes;

    public List<EleClassVO> getEles() {
        return eles;
    }

    public void setEles(List<EleClassVO> eles) {
        this.eles = eles;
    }

    public List<PEClassVO> getPes() {
        return pes;
    }

    public void setPes(List<PEClassVO> pes) {
        this.pes = pes;
    }
}
