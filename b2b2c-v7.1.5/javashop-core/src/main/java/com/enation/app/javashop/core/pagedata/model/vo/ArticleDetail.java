package com.enation.app.javashop.core.pagedata.model.vo;

import com.enation.app.javashop.core.pagedata.model.enums.ArticleShowPosition;
import com.enation.app.javashop.framework.database.annotation.Column;
import com.enation.app.javashop.framework.database.annotation.Id;
import com.enation.app.javashop.framework.database.annotation.Table;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 文章实体
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-12 10:43:18
 */
@Table(name = "es_article")
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArticleDetail implements Serializable {

    private static final long serialVersionUID = 5105404520203401L;

    /**
     * 主键
     */
    @Id(name = "article_id")
    @ApiModelProperty(hidden = true)
    private Integer articleId;
    /**
     * 文章名称
     */
    @Column(name = "article_name")
    @ApiModelProperty(name = "article_name", value = "文章名称")
    private String articleName;
    /**
     * 分类名称
     */
    @Column(name = "category_name")
    @ApiModelProperty(name = "category_name", value = "分类名称")
    private String categoryName;
    /**
     * 显示位置
     */
    @Column(name = "show_position")
    @ApiModelProperty(name = "show_position", value = "显示位置",hidden = true )
    private String showPosition;

    /**
     * 显示位置
     */
    @ApiModelProperty(name = "show_position_text", value = "显示位置，文字")
    private String showPositionText;

    /**
     * 是否允许删除  true 允许  false 不允许
     */
    @Column(name = "allow_delete")
    @ApiModelProperty(name = "allow_delete", value = "是否允许删除")
    private Boolean allowDelete;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getShowPosition() {
        return showPosition;
    }

    public void setShowPosition(String showPosition) {
        this.showPosition = showPosition;
    }

    public Boolean getAllowDelete() {

        if(this.showPosition!=null){
            return ArticleShowPosition.valueOf(this.showPosition).equals(ArticleShowPosition.OTHER);
        }

        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public String getShowPositionText() {
        if(this.showPosition!=null){
            return ArticleShowPosition.valueOf(this.showPosition).description();
        }
        return showPositionText;
    }

    public void setShowPositionText(String showPositionText) {
        this.showPositionText = showPositionText;
    }
}