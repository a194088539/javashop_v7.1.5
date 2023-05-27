package com.enation.app.javashop.core.member.model.dos;

import com.enation.app.javashop.core.base.context.Region;
import com.enation.app.javashop.core.base.context.RegionFormat;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * 会员收票地址实体
 *
 * @author duanmingyu
 * @version v7.1.4
 * @since v7.0.0
 * 2019-06-19
 */
@Table(name = "es_receipt_address")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReceiptAddressDO implements Serializable {

    private static final long serialVersionUID = -6723850607492854901L;

    /**
     * 主键ID
     */
    @Id(name = "id")
    @ApiModelProperty(hidden = true)
    private Integer id;

    /**
     * 会员ID
     */
    @Column(name = "member_id")
    @ApiModelProperty(name = "member_id", value = "会员ID", required = false, hidden = true)
    private Integer memberId;

    /**
     * 收票人姓名
     */
    @Column(name = "member_name")
    @ApiModelProperty(name = "member_name", value = "收票人姓名", required = false)
    private String memberName;

    /**
     * 收票人手机号
     */
    @Column(name = "member_mobile")
    @ApiModelProperty(name = "member_mobile", value = "收票人手机号", required = false)
    private String memberMobile;

    /**
     * 所属省份ID
     */
    @Column(name = "province_id")
    @ApiModelProperty(name = "province_id", value = "所属省份ID", required = false, hidden = true)
    private Integer provinceId;

    /**
     * 所属城市ID
     */
    @Column(name = "city_id")
    @ApiModelProperty(name = "city_id", value = "所属城市ID", required = false, hidden = true)
    private Integer cityId;

    /**
     * 所属区县ID
     */
    @Column(name = "county_id")
    @ApiModelProperty(name = "county_id", value = "所属区县ID", required = false, hidden = true)
    private Integer countyId;

    /**
     * 所属乡镇ID
     */
    @Column(name = "town_id")
    @ApiModelProperty(name = "town_id", value = "所属乡镇ID", required = false, hidden = true)
    private Integer townId;

    /**
     * 所属省份
     */
    @Column(name = "province")
    @ApiModelProperty(name = "province", value = "所属省份", required = false, hidden = true)
    private String province;

    /**
     * 所属城市
     */
    @Column(name = "city")
    @ApiModelProperty(name = "city", value = "所属城市", required = false, hidden = true)
    private String city;

    /**
     * 所属区县
     */
    @Column(name = "county")
    @ApiModelProperty(name = "county", value = "所属区县", required = false, hidden = true)
    private String county;

    /**
     * 所属乡镇
     */
    @Column(name = "town")
    @ApiModelProperty(name = "town", value = "所属乡镇", required = false, hidden = true)
    private String town;

    /**
     * 详细地址
     */
    @Column(name = "detail_addr")
    @ApiModelProperty(name = "detail_addr", value = "详细地址", required = false)
    private String detailAddr;

    @RegionFormat
    @ApiModelProperty(name = "region", value = "地区")
    private Region region;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public Integer getTownId() {
        return townId;
    }

    public void setTownId(Integer townId) {
        this.townId = townId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    @JsonIgnore
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReceiptAddressDO addressDO = (ReceiptAddressDO) o;
        return Objects.equals(id, addressDO.id) &&
                Objects.equals(memberId, addressDO.memberId) &&
                Objects.equals(memberName, addressDO.memberName) &&
                Objects.equals(memberMobile, addressDO.memberMobile) &&
                Objects.equals(provinceId, addressDO.provinceId) &&
                Objects.equals(cityId, addressDO.cityId) &&
                Objects.equals(countyId, addressDO.countyId) &&
                Objects.equals(townId, addressDO.townId) &&
                Objects.equals(province, addressDO.province) &&
                Objects.equals(city, addressDO.city) &&
                Objects.equals(county, addressDO.county) &&
                Objects.equals(town, addressDO.town) &&
                Objects.equals(detailAddr, addressDO.detailAddr) &&
                Objects.equals(region, addressDO.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, memberName, memberMobile, provinceId, cityId, countyId, townId, province, city, county, town, detailAddr, region);
    }

    @Override
    public String toString() {
        return "ReceiptAddressDO{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", memberMobile='" + memberMobile + '\'' +
                ", provinceId=" + provinceId +
                ", cityId=" + cityId +
                ", countyId=" + countyId +
                ", townId=" + townId +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", town='" + town + '\'' +
                ", detailAddr='" + detailAddr + '\'' +
                ", region=" + region +
                '}';
    }
}
