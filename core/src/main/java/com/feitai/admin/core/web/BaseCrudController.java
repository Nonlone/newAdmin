package com.feitai.admin.core.web;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;

/**
 * 增删改查 基类
 * @author linguocheng
 *
 * @param <T>
 */
public abstract class BaseCrudController<T> extends BaseListableController<T> {

	protected final static String ADD="add";
	protected final static String UPDATE="update";
	protected final static String DEL="del";
	protected final static String LIST="list";

	
	private String permissionPrefix;
	
	@RequestMapping(value = LIST)
	@ResponseBody
	public Object listPage(ServletRequest request) {	
		checkPermission(LIST);
		Page<T> listPage = super.list(request);
		return listPage;
	}

	@RequestMapping(value ="update/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object editFrom(@PathVariable("id") Long id) {
		checkPermission(UPDATE);
		T t = getService().findOne(id);
		return t;
	}

	@RequestMapping(value = ADD, method = RequestMethod.POST)
	@ResponseBody
	public Object add(@Valid T t) {
		checkPermission(ADD);
		getService().save(t);
		return successResult;
	}

	@RequestMapping(value = UPDATE, method = RequestMethod.POST)
	@ResponseBody
	public Object update(@Valid @ModelAttribute T t, Model model) {
		checkPermission(UPDATE);
		getService().updateByPrimaryKey(t);
		return successResult;
	}

	@RequestMapping(value = DEL)
	@ResponseBody
	public Object del(@RequestParam(value = "ids[]") Long[] ids) {
		checkPermission(DEL);
		getService().delete(ids);
		return successResult;
	}

	/**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出T对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
/*	@ModelAttribute
	public void getEntity(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("t", getService().findOne(id));
		}
	}*/

	/**
	 * 检查是否拥有权限
	 * @param permissionSuffix
	 * @throws AuthorizationException
	 */
	private void checkPermission(String permissionSuffix) throws AuthorizationException{
		Subject subject = SecurityUtils.getSubject();
		subject.checkPermission(getPermissionPrefix()+ ":" + permissionSuffix);
	}

	/**
	 * permission     permissionPrefix:permissionSuffix
	 * 如果 getCustomPermissionPrefix 方法中拿到的permissionPrefix为空，就从controllr 上拿RequestMapping中value[]的第一个值
	 * @return
	 * @throws AuthorizationException
	 */
	private String getPermissionPrefix() throws AuthorizationException {
		if (this.permissionPrefix != null) {
			return this.permissionPrefix;
		} 
		this.permissionPrefix=getCustomPermissionPrefix();
		if (this.permissionPrefix != null) {
			return this.permissionPrefix;
		} 
		if(this.permissionPrefix==null) {
			RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
			if (requestMapping != null && requestMapping.value().length > 0) {
                this.permissionPrefix = requestMapping.value()[0];
				return this.permissionPrefix;
			}
		}
		throw new AuthorizationException("permissionPrefix can not be null");
	}
	
	
	
	public  String getCustomPermissionPrefix(){
		return null;
	}
}
