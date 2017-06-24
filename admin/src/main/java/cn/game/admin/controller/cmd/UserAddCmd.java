package cn.game.admin.controller.cmd;


import cn.game.admin.service.RoleSet;
import cn.game.core.service.detail.system.RoleDetails;
import cn.game.core.service.detail.system.UserDetails;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

/** Created by zzc on 16/11/2016. */
public class UserAddCmd {

  private Long id;

  @Pattern(regexp = "1\\d{10}", message = "手机号码：格式不正确")
  private String mobile;

  @Size(min = 2, max = 10, message = "真实姓名：字符长度在2-10之间")
  private String realName;

  @RoleSet() private Collection<Long> roles;

  @Size(min = 6, max = 128)
  private String password;

  @Size(min = 6, max = 128)
  private String repeatPassword;

  private boolean locked = false;

  private String headPhoto;

  public UserAddCmd() {
    // 保留缺省构造
  }

  public UserAddCmd(
      String mobile, String realName, Set<Long> roles, String password, String repeatPassword) {
    this.mobile = mobile;
    this.realName = realName;
    this.roles = roles;
    this.password = password;
    this.repeatPassword = repeatPassword;
  }

  public UserDetails toDetails() {
    List<RoleDetails> tmpRoleDetailsSet = null;
    if (roles != null) {
      List<RoleDetails> roleDetailsSet = new ArrayList<>(roles.size());
      roles.stream().forEach(i -> roleDetailsSet.add(new RoleDetails(Long.valueOf(i))));
      tmpRoleDetailsSet = roleDetailsSet;
    }
    UserDetails details =
        new UserDetails(
            0, mobile, realName, mobile, password, locked, tmpRoleDetailsSet, headPhoto);
    return details;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public Collection<Long> getRoles() {
    return roles;
  }

  public void setRoles(Collection<Long> roles) {
    this.roles = roles;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRepeatPassword() {
    return repeatPassword;
  }

  public void setRepeatPassword(String repeatPassword) {
    this.repeatPassword = repeatPassword;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public String getHeadPhoto() {
    return headPhoto;
  }

  public void setHeadPhoto(String headPhoto) {
    this.headPhoto = headPhoto;
  }
}
