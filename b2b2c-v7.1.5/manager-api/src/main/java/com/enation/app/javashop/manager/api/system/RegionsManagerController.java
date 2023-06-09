package com.enation.app.javashop.manager.api.system;

import com.enation.app.javashop.core.system.model.dos.Regions;
import com.enation.app.javashop.core.system.model.vo.RegionsVO;
import com.enation.app.javashop.core.system.service.RegionsManager;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 地区控制器
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-05-18 11:45:03
 */
@RestController
@RequestMapping("/admin/systems/regions")
@Api(description = "地区相关API")
public class RegionsManagerController {

    @Autowired
    private RegionsManager regionsManager;

    @ApiOperation(value = "添加地区", response = Regions.class)
    @PostMapping
    public Regions add(@Valid RegionsVO regionsVO) {
        return this.regionsManager.add(regionsVO);

    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改地区", response = Regions.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public Regions edit(@Valid RegionsVO regionsVO, @PathVariable Integer id) {
        Regions regions = regionsManager.getModel(id);
        if (regions == null) {
            throw new ResourceNotFoundException("当前地区不存在");
        }
        BeanUtil.copyProperties(regionsVO, regions);
        this.regionsManager.edit(regions, id);
        return regions;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的地区主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {
        this.regionsManager.delete(id);
        return "";
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的地区主键", required = true, dataType = "int", paramType = "path")
    })
    public Regions get(@PathVariable Integer id) {

        Regions regions = this.regionsManager.getModel(id);

        return regions;
    }


    @GetMapping(value = "/{id}/children")
    @ApiOperation(value = "获取某地区的子地区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区id", required = true, dataType = "int", paramType = "path")
    })
    public List<Regions> getChildrenById(@PathVariable Integer id) {

        return regionsManager.getRegionsChildren(id);
    }

}
