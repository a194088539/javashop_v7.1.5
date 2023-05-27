package com.enation.app.javashop.manager.api.goodssearch;

import com.enation.app.javashop.core.base.SettingGroup;
import com.enation.app.javashop.core.base.service.SettingManager;
import com.enation.app.javashop.core.goodssearch.model.CustomWords;
import com.enation.app.javashop.core.goodssearch.model.EsSecretSetting;
import com.enation.app.javashop.core.goodssearch.service.CustomWordsManager;
import com.enation.app.javashop.framework.database.Page;
import com.enation.app.javashop.framework.util.JsonUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 自定义分词控制器
 *
 * @author fk
 * @version v1.0
 * @since v7.0.0
 * 2018-06-20 16:08:07
 *
 * update by liuyulei 2019-05-27
 */
@RestController
@RequestMapping("/admin/goodssearch/custom-words")
@Api(description = "自定义分词相关API")
public class CustomWordsManagerController {

    @Autowired
    private CustomWordsManager customWordsManager;

    @Autowired
    private SettingManager settingManager;


    @ApiOperation(value = "查询自定义分词列表", response = CustomWords.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page_no", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page_size", value = "每页显示数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "keywords", value = "关键字", required = false, dataType = "string", paramType = "query")
    })
    @GetMapping
    public Page list(@ApiIgnore Integer pageNo, @ApiIgnore Integer pageSize,@ApiIgnore String keywords) {

        return this.customWordsManager.list(pageNo, pageSize,keywords);
    }


    @ApiOperation(value = "添加自定义分词", response = CustomWords.class)
    @PostMapping
    public CustomWords add(@Valid CustomWords customWords) {

        this.customWordsManager.add(customWords);

        return customWords;
    }

    @PutMapping(value = "/{id}")
    @ApiOperation(value = "修改自定义分词", response = CustomWords.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    public CustomWords edit(@Valid CustomWords customWords, @PathVariable Integer id) {

        this.customWordsManager.edit(customWords, id);

        return customWords;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除自定义分词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的自定义分词主键", required = true, dataType = "int", paramType = "path")
    })
    public String delete(@PathVariable Integer id) {

        this.customWordsManager.delete(id);

        return "";
    }


    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询一个自定义分词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要查询的自定义分词主键", required = true, dataType = "int", paramType = "path")
    })
    public CustomWords get(@PathVariable Integer id) {

        CustomWords customWords = this.customWordsManager.getModel(id);

        return customWords;
    }

    /**
     * add by liuyulei 2019-05-26
     * @param password
     * @return
     */
    @ApiOperation(value = "设置秘钥",response = EsSecretSetting.class)
    @PutMapping("/secret-key")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password",value = "密码",required = true,dataType = "string",paramType = "query")
    })
    public EsSecretSetting setSecretKey(@ApiIgnore String password){
        EsSecretSetting secretSetting = new EsSecretSetting();

        secretSetting.setPassword(password);

        secretSetting.setSecretKey(password);
        settingManager.save(SettingGroup.ES_SIGN,secretSetting);

        return secretSetting;

    }

    @ApiOperation(value = "获取秘钥",response = EsSecretSetting.class)
    @GetMapping("/secret-key")
    public EsSecretSetting getSecretKey(){
        String value = settingManager.get(SettingGroup.ES_SIGN);
        if(StringUtil.isEmpty(value)){
            return new EsSecretSetting();
        }
        EsSecretSetting secretSetting = JsonUtil.jsonToObject(value,EsSecretSetting.class);
        return secretSetting;

    }


}
