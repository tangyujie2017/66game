package cn.tz.www.admin.service;



import javax.validation.Constraint;

import cn.tz.www.admin.service.validation.MobileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zzc on 17/11/2016.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MobileValidator.class)
public @interface Mobile {
}
