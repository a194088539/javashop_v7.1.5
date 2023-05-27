package com.enation.app.javashop.buyer.api.member;

import com.enation.app.javashop.core.member.model.dos.ReceiptAddressDO;
import com.enation.app.javashop.core.member.service.ReceiptAddressManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 会员收票地址API
 *
 * @author duanmingyu
 * @version v1.0
 * @since v7.1.5
 * 2019-09-16
 */
@Api(description = "会员收票地址API")
@RestController
@RequestMapping("/members/receipt/address")
@Validated
public class ReceiptAddressBuyerController {

    @Autowired
    private ReceiptAddressManager receiptAddressManager;

    @ApiOperation(value = "新增会员收票地址", response = ReceiptAddressDO.class)
    @PostMapping
    public ReceiptAddressDO add(@Valid ReceiptAddressDO receiptAddressDO) {

        return receiptAddressManager.add(receiptAddressDO);
    }

    @ApiOperation(value = "修改会员收票地址", response = ReceiptAddressDO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "path")
    })
    @PutMapping(value = "/{id}")
    public ReceiptAddressDO edit(@Valid ReceiptAddressDO receiptAddressDO, @PathVariable Integer id) {

        return receiptAddressManager.edit(receiptAddressDO, id);
    }

    @ApiOperation(value = "查看会员收票地址详细")
    @GetMapping(value = "/detail")
    public ReceiptAddressDO get() {
        return this.receiptAddressManager.get();
    }
}
