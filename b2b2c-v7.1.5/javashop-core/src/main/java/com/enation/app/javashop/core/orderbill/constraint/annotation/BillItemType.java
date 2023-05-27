package com.enation.app.javashop.core.orderbill.constraint.annotation;

/**
 * @author fk
 * @version v2.0
 * @Description: 标签关键字验证
 * @date 2018/4/1110:27
 * @since v7.0.0
 */

import com.enation.app.javashop.core.orderbill.constraint.validator.BillItemTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {BillItemTypeValidator.class})
@Documented
@Target( {ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BillItemType {

    String message() default "类型参数不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
