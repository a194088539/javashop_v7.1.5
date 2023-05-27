package com.enation.app.javashop.core.pagedata.constraint.annotation;

import com.enation.app.javashop.core.pagedata.constraint.validator.ClientMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author fk
 * @version v2.0
 * @Description: 客户端两端类型验证
 * @date 2018/4/311:42
 * @since v7.0.0
 */
@Constraint(validatedBy = {ClientMobileValidator.class})
@Documented
@Target( {ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientMobileType {

    String message() default "不正确的客户端类型";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
