package cn.game.admin.service.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.game.admin.service.Mobile;


/**
 * Created by zzc on 17/11/2016.
 * 这个类暂时没用
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

  @Override
  public void initialize(Mobile constraintAnnotation) {
    // 不需要执行初始化操作
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return false;
  }
}
