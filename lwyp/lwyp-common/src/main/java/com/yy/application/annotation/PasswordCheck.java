package com.yy.application.annotation;


import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 邮箱验证
 * @Author: sunwei01
 * @Date: 2020/3/10 9:50
 */

@ConstraintComposition(CompositionType.OR)
//密码允许字母、数字
@Pattern(regexp = "^[0-9A-Za-z]{6}$")
@Null
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface PasswordCheck {

    String message() default "请输入6位英文或数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
