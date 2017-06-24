package cn.game.admin.controller.cmd;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by zzc on 18/11/2016.
 */
public class UserAddCmdValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return UserAddCmd.class == clazz;
  }

  @Override
  public void validate(Object target, Errors errors) {

    UserAddCmd cmd = (UserAddCmd) target;
    if(cmd.getId()==null && !cmd.getPassword().equals(cmd.getRepeatPassword()) ){
      errors.rejectValue("password","notmatch");
    }

  }
}
