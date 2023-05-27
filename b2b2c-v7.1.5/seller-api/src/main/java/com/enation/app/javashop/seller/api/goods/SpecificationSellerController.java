package com.enation.app.javashop.seller.api.goods;

import com.enation.app.javashop.core.goods.GoodsErrorCode;
import com.enation.app.javashop.core.goods.model.dos.SpecValuesDO;
import com.enation.app.javashop.core.goods.model.dos.SpecificationDO;
import com.enation.app.javashop.core.goods.model.vo.SpecificationVO;
import com.enation.app.javashop.core.goods.service.SpecValuesManager;
import com.enation.app.javashop.core.goods.service.SpecificationManager;
import com.enation.app.javashop.framework.context.UserContext;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 规格项控制器
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0 2018-03-20 09:31:27
 */
@RestController
@RequestMapping("/seller/goods")
@Api(description = "规格项相关API")
@Validated
public class SpecificationSellerController {

    @Autowired
    private SpecificationManager specificationManager;

    @Autowired
    private SpecValuesManager specValuesManager;

    @ApiOperation(value = "根据分类id查询规格包括规格值", notes = "根据分类id查询规格")
    @ApiImplicitParam(name = "category_id", value = "分类id", required = true, paramType = "path", dataType = "int")
    @GetMapping("/categories/{category_id}/specs")
    public List<SpecificationVO> sellerQuerySpec(@PathVariable("category_id") Integer categoryId) {

        return this.specificationManager.querySellerSpec(categoryId);
    }

    @ApiOperation(value = "商家自定义某分类的规格项", response = SpecificationDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category_id", value = "分类id", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "spec_name", value = "规格项名称", required = true, paramType = "query", dataType = "string")})
    @PostMapping("/categories/{category_id}/specs")
    public SpecificationDO add(@PathVariable("category_id") Integer categoryId,
                               @ApiIgnore @NotEmpty(message = "规格名称不能为空") @Length(max = 50,message = "规格名称不能超过50个字符") String specName) {
        // 商家的规格
        SpecificationDO specification = this.specificationManager.addSellerSpec(categoryId, specName);

        return specification;
    }

    @ApiOperation(value = "商家自定义某规格的规格值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spec_id", value = "规格id", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "value_name", value = "规格值", required = true, paramType = "query", dataType = "string")})
    @PostMapping("/specs/{spec_id}/values")
    public SpecValuesDO addValue(@PathVariable("spec_id") Integer specId,
                                 @ApiIgnore @NotEmpty(message = "规格值名称不能为空") @Length(max = 50,message = "规格值名称不能超过50个字符") String valueName) {
        // 商家的规格
        Seller seller = UserContext.getSeller();
        SpecificationDO spec = specificationManager.getModel(specId);
        boolean flag = spec.getSellerId() == 0 || spec.getSellerId().equals(seller.getSellerId());
        if (spec == null || !flag) {
            throw new ServiceException(GoodsErrorCode.E306.code(), "无权操作");
        }
        SpecValuesDO value = new SpecValuesDO(specId, valueName, seller.getSellerId());

        value.setSpecName(spec.getSpecName());

        this.specValuesManager.add(value);

        return value;
    }

}
