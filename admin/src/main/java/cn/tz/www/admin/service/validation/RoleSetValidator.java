package cn.tz.www.admin.service.validation;


import org.springframework.beans.factory.annotation.Autowired;

import cn.tz.www.admin.controller.service.UserService;
import cn.tz.www.admin.controller.service.detail.system.RoleDetails;
import cn.tz.www.admin.service.RoleSet;
import cn.tz.www.customer.entity.tools.Groups;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.List;

/** Created by zzc on 18/11/2016. */
public class RoleSetValidator implements ConstraintValidator<RoleSet, Collection<Long>> {

  @Autowired
  private UserService userService;
  @Override
  public void initialize(RoleSet constraintAnnotation) {
    // 不需要执行初始化操作
  }

  @Override
  public boolean isValid(Collection<Long> value, ConstraintValidatorContext context) {
    if (value == null) return false;
    if (value.isEmpty()) return false;
    Groups groups = new Groups();
   
	List<RoleDetails> roles = userService.findRoleByGroups(groups );
    
    if (roles == null || roles.isEmpty()) {
      return false;
    }
    if (!containsAll(value, roles)) {
      return false;
    }
    return true;
  }

  /**
   * Input 集合里面所有的值是否都包含在 Details 的集合中
   *
   * @param input 输入的集合
   * @param detailsSet 全部集合
   * @return
   */
  private boolean containsAll(Collection<Long> input, Collection<RoleDetails> detailsSet) {
    for (Long i : input) {
      if (!contains(i, detailsSet)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 判断输入的值是否包含在 Details ID 的集合中
   *
   * @param input
   * @param detailsSet
   * @return
   */
  private boolean contains(Long input, Collection<RoleDetails> detailsSet) {
    for (RoleDetails d : detailsSet) {
      if (d.getId() == input) {
        return true;
      }
    }
    return false;
  }
}
