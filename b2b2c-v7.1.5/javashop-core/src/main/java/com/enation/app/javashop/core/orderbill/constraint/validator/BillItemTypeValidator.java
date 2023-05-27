package com.enation.app.javashop.core.orderbill.constraint.validator;

import com.enation.app.javashop.core.orderbill.constraint.annotation.BillItemType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author fk
 * @version v1.0
 * @Description: BillItemType 验证
 * @date 2018/4/3 11:44
 * @since v7.0.0
 */
public class BillItemTypeValidator implements ConstraintValidator<BillItemType, String> {

    private final String[] ALL_STATUS = {"REFUND","PAYMENT"};

    @Override
    public void initialize(BillItemType status) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(ALL_STATUS).contains(value);
    }
}
