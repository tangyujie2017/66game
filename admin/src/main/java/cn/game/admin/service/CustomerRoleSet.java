package cn.game.admin.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cn.game.admin.service.validation.CustomerRoleSetValidator;
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=CustomerRoleSetValidator.class)
@Documented
public @interface CustomerRoleSet {


	  String message() default "{cn.gaiasys.retail.constraints.CustomerRoleSet.message}";

	  Class<?>[] groups() default {};

	  Class<? extends Payload>[] payload() default {};

}
