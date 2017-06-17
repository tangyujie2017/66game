package cn.tz.www.admin.service;


import javax.validation.Constraint;
import javax.validation.Payload;

import cn.tz.www.admin.service.validation.RoleSetValidator;

import java.lang.annotation.*;

/**
 * Created by zzc on 18/11/2016.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=RoleSetValidator.class)
@Documented
public @interface RoleSet {

  String message() default "{cn.gaiasys.retail.constraints.RoleSet.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
