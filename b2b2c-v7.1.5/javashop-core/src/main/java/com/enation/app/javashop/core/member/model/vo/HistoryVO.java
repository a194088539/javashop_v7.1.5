package com.enation.app.javashop.core.member.model.vo;

import com.enation.app.javashop.core.member.model.dos.HistoryDO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


/**
 * 会员足迹VO
 *
 * @author zh
 * @version v7.1.4
 * @since vv7.1
 * 2019-06-18 15:18:56
 */
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HistoryVO {
    /**
     * 时间轴 以天为单位
     */
    @ApiModelProperty(name = "time", value = "时间，以天为单位")
    private long time;
    /**
     * 足迹
     */
    private List<HistoryDO> history;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<HistoryDO> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryDO> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        return "HistoryVO{" +
                "time=" + time +
                ", history=" + history +
                '}';
    }
}