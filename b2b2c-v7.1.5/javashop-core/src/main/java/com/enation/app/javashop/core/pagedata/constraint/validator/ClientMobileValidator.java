package com.enation.app.javashop.core.pagedata.constraint.validator;

import com.enation.app.javashop.core.pagedata.constraint.annotation.ClientMobileType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * @author fk
 * @version v2.0
 * @Description: ClientMobileType 验证
 * @date 2018/4/3 11:44
 * @since v7.0.0
 */
public class ClientMobileValidator implements ConstraintValidator<ClientMobileType, String> {

    private final String[] ALL_STATUS = {"PC", "MOBILE"};

    @Override
    public void initialize(ClientMobileType status) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(ALL_STATUS).contains(value);
    }
}
