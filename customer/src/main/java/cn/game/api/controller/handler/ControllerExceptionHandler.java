
package cn.game.api.controller.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.game.api.controller.resp.BaseResponse;
import cn.game.api.util.GameApiUtils;



/**
 * 通用错误处理器.
 *
 * 
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ControllerExceptionHandler extends AbstractErrorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@Value("${server.error.path:${error.path:/error}}")
	private String errorPath;

	public ControllerExceptionHandler(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	/**
	 * 500错误.
	 *
	 * @param req
	 * @param rsp
	 * @param ex
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ModelAndView serverError(HttpServletRequest req, HttpServletResponse rsp, Exception ex) throws IOException {
		LOGGER.error("请求地址:{},客户端IP:{},发生500错误:{}", req.getRequestURI(), GameApiUtils.getIpAddress(req), ex.getMessage(),
				ex);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String msg = mapper.writeValueAsString(BaseResponse.newFail(BaseResponse.STATUS_ERROR, "系统繁忙,请稍候重试"));
		return handleJSONError(rsp, msg, HttpStatus.OK);
	}

	/**
	 * 404的拦截.
	 *
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView notFound(HttpServletRequest request, HttpServletResponse response, Exception ex)
			throws IOException {
		LOGGER.debug("请求地址:{},客户端IP:{},发生404错误:{}", request.getRequestURI(), GameApiUtils.getIpAddress(request),
				ex.getMessage());
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String msg = mapper.writeValueAsString(BaseResponse.newFail(BaseResponse.STATUS_BADREQUEST, "你访问的资源不存在"));
		handleJSONError(response, msg, HttpStatus.OK);
		return null;
	}

	/**
	 * 参数不完整错误.
	 *
	 * @param req
	 * @param rsp
	 * @param ex
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ModelAndView methodArgumentNotValidException(HttpServletRequest req, HttpServletResponse rsp,
			MethodArgumentNotValidException ex) throws IOException {
		BindingResult result = ex.getBindingResult();
		List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
		StringBuilder msg = new StringBuilder();
		fieldErrors.stream().forEach(
				fieldError -> msg.append("[" + fieldError.getField() + "," + fieldError.getDefaultMessage() + "]"));
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String json = mapper
				.writeValueAsString(BaseResponse.newFail(BaseResponse.STATUS_BADREQUEST, "参数不合法:" + msg.toString()));
		return handleJSONError(rsp, json, HttpStatus.OK);
	}

	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, false));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return modelAndView == null ? new ModelAndView("error", model) : modelAndView;
	}

	@RequestMapping
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("请求地址:{},客户端IP:{},发生未知错误:{}", request.getRequestURI(), GameApiUtils.getIpAddress(request),
				Collections.unmodifiableMap(getErrorAttributes(request, false)));
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		String msg;
		try {
			msg = mapper.writeValueAsString(BaseResponse.newFail(BaseResponse.STATUS_BADREQUEST, "您访问的资源暂时不可用"));
			handleJSONError(response, msg, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("error:{}", e.getMessage(), e);
		}
		return null;
	}

	protected ModelAndView handleViewError(String url, String errorStack, String errorMessage, String viewName) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", errorStack);
		mav.addObject("url", url);
		mav.addObject("msg", errorMessage);
		mav.addObject("timestamp", new Date());
		mav.setViewName(viewName);
		return mav;
	}

	protected ModelAndView handleJSONError(HttpServletResponse rsp, String errorMessage, HttpStatus status)
			throws IOException {
		rsp.setCharacterEncoding("UTF-8");
		rsp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		rsp.setStatus(status.value());
		PrintWriter writer = rsp.getWriter();
		writer.write(errorMessage);
		writer.flush();
		writer.close();
		return null;
	}

	@Override
	public String getErrorPath() {
		return errorPath;
	}
}
