package com.yy.application.annotation.validate;


import com.yy.application.annotation.PhoneCheck;
import com.yy.application.util.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author David.liu
 * @description 手机号自定义逻辑处理类
 * @date 2020/3/27 14:58
 */
public class MobileValidate implements ConstraintValidator<PhoneCheck, String> {
    /**
     * Initializes the validator in preparation for
     * {@link #(Object, ConstraintValidatorContext)} calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(PhoneCheck constraintAnnotation) {

    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Validator.isNullOrEmpty(value)) {
            return true;
        }
        return Pattern.matches("1[0-9][0-9]\\d{8}", value);
    }
}
