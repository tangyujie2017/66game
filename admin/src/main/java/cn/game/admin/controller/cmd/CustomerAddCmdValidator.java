package cn.game.admin.controller.cmd;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CustomerAddCmdValidator implements Validator{
	 @Override
	  public boolean supports(Class<?> clazz) {
	    return CustomerAddCmd.class == clazz;
	  }

	  @Override
	  public void validate(Object target, Errors errors) {

		  CustomerAddCmd cmd = (CustomerAddCmd) target;
	    if(cmd.getId()==null && !cmd.getPassword().equals(cmd.getRepeatPassword()) ){
	      errors.rejectValue("password","notmatch");
	    }

	  }
}
