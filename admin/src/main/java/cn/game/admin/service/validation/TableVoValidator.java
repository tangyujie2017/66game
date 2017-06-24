package cn.game.admin.service.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cn.game.admin.util.TableVo;


public class TableVoValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		
		return TableVo.class == clazz;
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}

}
