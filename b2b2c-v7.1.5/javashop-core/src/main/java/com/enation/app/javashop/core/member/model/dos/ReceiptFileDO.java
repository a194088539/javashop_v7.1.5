package com.enation.app.javashop.core.member.model.dos;

import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会员电子发票附件实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-24
 */
@Table(name = "es_receipt_file")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReceiptFileDO implements Serializable {

    private static final long serialVersionUID = 5669929332569564985L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;

    /**
     * 会员开票历史记录ID
     */
    @Column(name = "history_id")
    @ApiModelProperty(name = "history_id", value = "会员开票历史记录ID", required = false)
    private Integer historyId;

    /**
     * 电子发票附件
     */
    @Column(name = "elec_file")
    @ApiModelProperty(name = "elec_file", value = "电子发票附件", required = false)
    private String elecFile;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public String getElecFile() {
        return elecFile;
    }

    public void setElecFile(String elecFile) {
        this.elecFile = elecFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReceiptFileDO that = (ReceiptFileDO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(historyId, that.historyId) &&
                Objects.equals(elecFile, that.elecFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, historyId, elecFile);
    }

    @Override
    public String toString() {
        return "ReceiptFileDO{" +
                "id=" + id +
                ", historyId=" + historyId +
                ", elecFile='" + elecFile + '\'' +
                '}';
    }
}
