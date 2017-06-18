package cn.tz.www.admin.config;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRequestMessageConverter extends AbstractHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

	private final static Charset UTF8 = Charset.forName("UTF-8");
	private final static String JSONP_FUNC_NAME = "callback";
	private Charset charset = UTF8;
	private String jsonpFuncName = JSONP_FUNC_NAME;
	private ObjectMapper objectMapper;
	public JsonRequestMessageConverter() {
		super(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8));
		objectMapper = Jackson2ObjectMapperBuilder.json().build();
	}
	public void setJsonpFuncName(String jsonpFuncName) {
		this.jsonpFuncName = jsonpFuncName;
	}
	public void setCharset(Charset charset) {
		this.charset = charset;
	}
	private HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	private boolean requestJsonp(HttpServletRequest request) {
		return request.getRequestURI().endsWith(".jsonp");
	}
	private String getJsonpFunc(HttpServletRequest request) {
		String func = request.getParameter(jsonpFuncName);
		return StringUtils.isEmpty(func) ? "null" : func;
	}
	/**
	 * 判断前台请求提交的数据是否可以用此convert读
	 * type为control中标记为RequestBody的参数类型
	 * contextClass为对应请求的control类
	 * mediaType为自持的请求类型，如json、text等
	 * 
	 */
	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		JavaType javaType = getJavaType(type, contextClass);
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canDeserialize(javaType, causeRef) && canRead(mediaType)) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate deserialization for type " + javaType;
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			}
			else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}
	private JavaType getJavaType(Type type, Class<?> contextClass) {
		return this.objectMapper.getTypeFactory().constructType(type, contextClass);
	}
	/**
	 * 
	 * @Description:泛型读，将从前台传过来的json请求串映射为具体的参数对象
	 * @param type
	 * @param contextClass
	 * @param inputMessage
	 * @return
	 * @throws IOException
	 * @throws HttpMessageNotReadableException
	 * @see org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver.readWithMessageConverters(HttpInputMessage, MethodParameter, Type)
	 * @see org.springframework.http.converter.GenericHttpMessageConverter#read(java.lang.reflect.Type, java.lang.Class, org.springframework.http.HttpInputMessage)
	 * @update1: 
	 *
	 */
	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), this.getJavaType(type, contextClass));
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
	/**
	 * 
	 * @Description:如果泛型读canRead方法返回false，则会调用AbstractHttpMessageConverter中的read方法，此方法会调用readInternal转普通jsonObject
	 * @param clazz
	 * @param inputMessage
	 * @return
	 * @throws IOException
	 * @throws HttpMessageNotReadableException
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#readInternal(java.lang.Class, org.springframework.http.HttpInputMessage)
	 * @update1: 
	 *
	 */
	@Override
	protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		try {
			return this.objectMapper.readValue(inputMessage.getBody(), this.getJavaType(clazz, null));
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
	/**
	 * 
	 * @Description:定义此convert支持输出的对象类型，即control端返回的类型，此处支持json格式的字符串及JSONObject/JsonArray对象
	 * @param clazz
	 * @return
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#supports(java.lang.Class)
	 * @update1:
	 *
	 */
	@Override
	protected boolean supports(Class<?> clazz) {
		// 只处理control返回的String/JSONObject/JsonArray对象
		return String.class.isAssignableFrom(clazz) || JSON.class.isAssignableFrom(clazz);
	}
	/**
	 * 
	 * @Description:定义此convert可以输出的条件为json格式的字符串及JSONObject/JsonArray对象,且为json请求类型
	 * @param clazz
	 * @param mediaType
	 * @return
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#canWrite(java.lang.Class, org.springframework.http.MediaType)
	 * @update1:
	 *
	 */
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return this.supports(clazz) && canWrite(mediaType);
	}
	/**
	 * 
	 * @Description:
	 * @param t
	 * @param outputMessage
	 * @throws IOException
	 * @throws HttpMessageNotWritableException
	 * @see	org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(T, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse)
	 * @see org.springframework.http.converter.AbstractHttpMessageConverter#writeInternal(java.lang.Object, org.springframework.http.HttpOutputMessage)
	 * @update1:
	 *
	 */
	@Override
	protected void writeInternal(Object t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		// 进来的t值只能是字符串或JSONObject/JsonArray格式
		OutputStream out = outputMessage.getBody();
		HttpServletRequest request = getRequest();
		StringBuilder buffer = new StringBuilder();
		boolean requestJsonp = requestJsonp(request);
		if (requestJsonp) {
			buffer.append(getJsonpFunc(request)).append('(');
		}
		buffer.append(resolveJsonString(t));
		if (requestJsonp) {
			buffer.append(");");
		}
		System.out.println("jsonvonvert:"+buffer);
		byte[] bytes = buffer.toString().getBytes(charset);
		out.write(bytes);
		out.flush();
	}
	private String resolveJsonString(Object t) throws HttpMessageNotWritableException{
		if(t instanceof JSON){
			return ((JSON)t).toJSONString();
		}else if(t instanceof String){
			return (String)t;
		}
		throw new HttpMessageNotReadableException("Not Json Object");
	}
	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		return this.supports(clazz) && canWrite(mediaType);
	}
	@Override
	public void write(Object t, Type type, MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		
		
	}


}
