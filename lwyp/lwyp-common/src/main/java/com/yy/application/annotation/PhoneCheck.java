package com.yy.application.annotation;

import com.yy.application.annotation.validate.MobileValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Description: 验证手机号，空和正确的手机号都能验证通过<br/>
 * 正确的手机号由11位数字组成，第一位为1
 * 第二位为 3、4、5、6、7、8
 * @Author: David.liu
 * @Date: 2020/2/19 18:33
 */

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {MobileValidate.class})
public @interface PhoneCheck {
    String message() default "手机号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

