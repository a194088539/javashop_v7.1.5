package com.enation.app.javashop.core.member.constraint.validator;

import com.enation.app.javashop.core.member.constraint.annotation.GradeType;
import com.enation.app.javashop.core.member.model.enums.CommentGrade;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author fk
 * @version v2.0
 * @Description: GradeType 验证
 * @date 2018/4/3 11:44
 * @since v7.0.0
 */
public class GradeTypeValidator implements ConstraintValidator<GradeType, String> {

    @Override
    public void initialize(GradeType status) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        try {
            CommentGrade s = CommentGrade.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }

}

