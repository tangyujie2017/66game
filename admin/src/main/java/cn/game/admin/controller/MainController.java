package cn.game.admin.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
public class MainController extends WebMvcConfigurerAdapter {
	@RequestMapping("/")
	public String index(Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		return "index";

	}

	@RequestMapping("/roleManage")
	public String roleManage(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "redirect:/role/";

	}

	@RequestMapping("/releaseInfos")
	public String releaseInfos(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "release_infos";

	}

	@RequestMapping("/releaseNews")
	public String releaseNews(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "release_news";

	}

	@RequestMapping("/releaseSlide")
	public String releaseSlide(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}

		// response.getHeader("X-Frame-Options").
		// response.addHeader("X-Frame-Options", "SAMEORIGIN");
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "release_slide";

	}

	@RequestMapping("/usersManage")
	public String usersManage(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "redirect:/user/";

	}

	@RequestMapping("/usersAuthority")
	public String usersAuthority(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "redirect:/authority/";

	}

	@RequestMapping("/customerManage")
	public String customerManage(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "redirect:/customer/";

	}

	@RequestMapping("/customerRole")
	public String customerRole(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "redirect:/customerRole/";

	}

	@RequestMapping("/customerPermission")
	public String customerPermission(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "redirect:/customerAuthority/";

	}
	@RequestMapping("/releaseProducts")
	public String releaseProducts(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "release_products";

	}
	@RequestMapping("/releaseProduct")
	public String releaseProduct(Principal principal, HttpServletResponse response) {
		if (principal == null) {
			return "redirect:/login";
		}
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		return "release_product";

	}

}
