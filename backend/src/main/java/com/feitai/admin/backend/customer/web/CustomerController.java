/**
 * @author 
 * @version 1.0
 * @since  2018-07-13 16:25:40
 * @desc Idcard
 */

package com.feitai.admin.backend.customer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.feitai.admin.backend.auth.service.AuthdataLinkfaceLivenessIdnumberVerificationService;
import com.feitai.admin.backend.auth.service.AuthdataLinkfaceLivenessSelfieVerificationService;
import com.feitai.admin.backend.config.service.AppConfigService;
import com.feitai.admin.backend.customer.entity.Idcard;
import com.feitai.admin.backend.customer.service.*;
import com.feitai.admin.backend.fund.service.FundService;
import com.feitai.admin.backend.fund.service.UserBankCardBindService;
import com.feitai.admin.backend.service.AttachPhotoService;
import com.feitai.admin.core.service.*;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import com.feitai.jieya.server.dao.bank.model.UserBankCard;
import com.feitai.jieya.server.dao.bank.model.UserBankCardBind;
import com.feitai.jieya.server.dao.callback.model.linkface.LinkfaceLivenessIdNumberVerifcation;
import com.feitai.jieya.server.dao.data.model.IdCardData;
import com.feitai.jieya.server.dao.data.model.PersonData;
import com.feitai.jieya.server.dao.data.model.WorkData;
import com.feitai.jieya.server.dao.fund.model.Fund;
import com.feitai.jieya.server.dao.user.model.User;
import com.feitai.utils.Desensitization;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/admin/customer/idcard")
@Slf4j
public class CustomerController extends BaseListableController<Idcard> {
	@Autowired
	private IdcardService idcardService;
	
	@Autowired
	private PersonService personService;

	@Autowired
	private AppConfigService appConfigService;

	@Autowired
	private UserInService userInService;
	
	@Autowired
	private WorkService workService;
	
	@Autowired
	private AttachPhotoService attachPhotoService;
	
	@Autowired
	private AuthdataLinkfaceLivenessIdnumberVerificationService authdataLinkfaceLivenessIdnumberVerificationService;
	
	@Autowired
	private AuthdataLinkfaceLivenessSelfieVerificationService authdataLinkfaceLivenessSelfieVerificationService;

	@Autowired
	private UserBankCardBindService userBankCardBindService;

	@Autowired
	private FundService fundService;

	@Autowired
	private UserBankCardService userBankCardService;

	@RequestMapping(value = "")
	public String index(Model model) {
		model.addAttribute("isOut",false);
		//model.addAttribute("userId", "2481652236288");
		return "/admin/customer/idcard/index";
	}


	/***
	 * 智齿接口
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "wisdomTooth")
	public String wisdomTooth(Model model, ServletRequest request) {
		String phone = request.getParameter("uphone")==null?request.getParameter("tel"):request.getParameter("uphone");
		String email = request.getParameter("email");
		String sign = request.getParameter("sign");

//		if(!supportStaffService.checkHaveSupport(email,sign)){//查找是否有此客服人员
//			return "/admin/noSupport";
//		}
        try{
            Long id = userInService.findUserIdByPhone(phone);
            //身份证信息
            IdCardData idcard = idcardService.findOneBySql(getFindByUserIdSql(id));

            List<UserBankCard> cards = userBankCardService.findByUserId(id);
			//收款卡
			List<com.feitai.admin.backend.customer.vo.UserBankCard> payCardrea = new ArrayList<>();
            //还款卡
            List<com.feitai.admin.backend.customer.vo.UserBankCard> repayCardrea = new ArrayList<>();
            for (UserBankCard repay:cards) {

                String bankCardNo = Desensitization.bankCardNo(repay.getBankCardNo());
                com.feitai.admin.backend.customer.vo.UserBankCard userBankCard = new com.feitai.admin.backend.customer.vo.UserBankCard();
                userBankCard.setBankCardNo(bankCardNo);
                userBankCard.setBankName(repay.getBankName());
                userBankCard.setPayStatus(repay.getPayAble());
				//资金方
				String funds = getFunds(bankCardNo);
				userBankCard.setFund(funds);

				if(repay.getPayAble().equals("1")){
					payCardrea.add(userBankCard);
				}
				if(repay.getRepayAble().equals("1")){
					repayCardrea.add(userBankCard);
				}
            }
            //年龄
            if(idcard!=null){
                int year = new Date().getYear() - idcard.getBirthday().getYear();
                model.addAttribute("year",year);
            }
            //生日
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            PersonData person = personService.findByUserId(id);


            //用户基本信息
            User user = userInService.findOne(id);

            //脱敏处理
            if(idcard!=null){
                String hyIdcard = Desensitization.idCard(idcard.getIdCard());
                model.addAttribute("hyIdcard",hyIdcard);
                //idcard.setIdCard(hyIdcard);
            }
            if(user!=null){
                String hyPhone = Desensitization.phone(user.getPhone());
                model.addAttribute("hyPhone",hyPhone);
                //user.setPhone(hyPhone);
            }


            model.addAttribute("payCard",payCardrea);
            model.addAttribute("repayCard",repayCardrea);
            model.addAttribute("birthday",format.format(idcard.getBirthday()));
            model.addAttribute("person", person);
            model.addAttribute("idcard",idcard);
            model.addAttribute("user", user);

            //model.addAttribute("userId", "2481652236288");
            return "/admin/customer/idcard/out";
        }catch (Exception e){
			log.error("", e);
            return "/admin/noUser";
        }

	}



	/***
	 * 根据银行卡号获取资金方及其状态
	 * @param bankCardNo
	 * @return
	 */
	private String getFunds(String bankCardNo){
		List<UserBankCardBind> userBankCardBinds = userBankCardBindService.findByBankCardNo(bankCardNo);
		String funds = "";
		for (UserBankCardBind userBankCardBind:
				userBankCardBinds) {
			Fund fund = fundService.findOne(userBankCardBind.getSeqNo());
			String status = userBankCardBind.getStatus();
			switch (status){
				case "NONE":
					funds+=fund.getFundName()+"（未绑卡）,";
					break;
				case "AUDIT":
					funds+=fund.getFundName()+"（审核中）,";
					break;
				case "PASSED":
					funds+=fund.getFundName()+"（审核通过）,";
					break;
				case "BACK":
					funds+=fund.getFundName()+"（审核回退）,";
					break;
				case "REFUSED":
					funds+=fund.getFundName()+"（审核拒绝）,";
					break;
			}
		}
		if(funds.length()!=0){
			funds = funds.substring(0, funds.length() - 1);
		}
		return funds;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequiresPermissions("/admin/customer/idcard:list")
	@RequestMapping(value = "list")
	@ResponseBody
	public Object listPage(ServletRequest request) {
		Page<Idcard> listPage = super.list(request,getSql());
		String list = JSON.toJSONString(listPage);
		List newList = new ArrayList();
		Map mapList = JSON.parseObject(list, Map.class);
		List<JSONObject> content = (List<JSONObject>)mapList.get("content");
		//遍历page中内容，修改或添加非数据库的自定义字段
		for (JSONObject json:content) {
			Map<String, Object> map = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>(){});
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
			try{
				long birthday = (long)map.get("birthday");
				Date datey = new Date();
				datey.setTime(birthday);
				Date date = new Date();
				String now = dateFormat.format(date);
				String bir = dateFormat.format(datey);
				map.put("birthday", Integer.parseInt(now)-Integer.parseInt(bir));
			}catch (NullPointerException e){
				log.info("user's birthday is null");
				map.put("birthday","未填写");
			}

			Long createdTime = (Long)map.get("createdTime");
			if(createdTime!=null){
				Date date = new Date();
				date.setTime(createdTime);
				map.put("createdTime",date);
			}
			//map.put("idCard", (String)map.get("idCard"));
			newList.add(map);
		}
		mapList.put("content",newList);
		return mapList;
	}


	/**
	 * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
	 * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
	 */
	@ModelAttribute
	public void getidcard(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
		if (id != -1) {
			model.addAttribute("idcard", this.idcardService.findOneBySql(getFindByIdSql(id)));
		}
	}


	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String auth(@PathVariable("id") String id, Model model) {
		//身份证信息
		Idcard idcard = this.idcardService.findOneBySql(getFindByIdSql(id));
		Long userId = idcard.getUserId();

		//年龄
		int year = new Date().getYear() - idcard.getBirthday().getYear();
		model.addAttribute("year",year);
		//生日
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		PersonData person = personService.findByUserId(userId);
		
		//婚姻状况最高学历
		String marital = "";
		String educationLevel = "";
		if(person!=null){
			marital = appConfigService.findByTypeCodeAndCode("maritalStatus",person.getMaritalStatus());
			educationLevel = appConfigService.findByTypeCodeAndCode("educationLevel", person.getEducationLevel());
		}
		//用户基本信息
		User user = userInService.findOne(userId);
		//单位信息
		WorkData work = workService.findByUserId(userId);
		//相片地址
		List<PhotoAttach> photos = attachPhotoService.findByUserId(userId);
		for (PhotoAttach attachPhoto : photos) {
			model.addAttribute("photo"+attachPhoto.getType(), attachPhoto.getPath());
			
		}
		//脱敏处理
		if(idcard!=null){
			String hyIdcard = Desensitization.idCard(idcard.getIdCard());
			model.addAttribute("hyIdcard",hyIdcard);
		}
		if(user!=null){
			String hyPhone = Desensitization.phone(user.getPhone());
			model.addAttribute("hyPhone",hyPhone);
		}
		//商汤
		LinkfaceLivenessIdNumberVerifcation livenessIdnumberVerification = authdataLinkfaceLivenessIdnumberVerificationService.findByUserId(userId);
		//AuthdataLinkfaceLivenessSelfieVerification livenessSelfieVerification =authdataLinkfaceLivenessSelfieVerificationService.findByUserId(userId);
		if(livenessIdnumberVerification==null){
			model.addAttribute("gongan", "未对比");
		}else{
			model.addAttribute("gongan", livenessIdnumberVerification.getVerifyScore()*100);
		}
		//model.addAttribute("huoti", livenessSelfieVerification.getVerifyScore());
		
		model.addAttribute("birthday",format.format(idcard.getBirthday()));
		model.addAttribute("person", person);		
		model.addAttribute("marital", marital);
		model.addAttribute("educationLevel",educationLevel);
		model.addAttribute("idcard",idcard);
		model.addAttribute("user", user);
		model.addAttribute("work", work);
		return "/admin/customer/idcard/detail";
	}


	@Override
	protected DynamitSupportService<Idcard> getService() {
		return this.idcardService;
	}

	protected String getSql() {
		String sql = SelectMultiTable.builder(IdCardData.class)
				.leftJoin(User.class,"user_in",new OnCondition[]{
						new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
				}).buildSqlString();
		return sql;
	}

	protected String getFindByIdSql(Object id){
		String sql = SelectMultiTable.builder(IdCardData.class)
				.leftJoin(User.class,"user_in",new OnCondition[]{
						new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
				}).buildSqlString()+" where maintable.id = '"+id+"'"+" GROUP BY maintable.id";
		return sql;
	}

	protected String getFindByUserIdSql(Object userId) {
		String sql = SelectMultiTable.builder(IdCardData.class)
				.leftJoin(User.class,"user_in",new OnCondition[]{
						new OnCondition(SelectMultiTable.ConnectType.AND, "userId", Operator.EQ, "id"),
				}).buildSqlString()+" where maintable.user_id = '"+userId+"'"+" GROUP BY maintable.id";
		return sql;
	}

	protected String getSingleSql(String typeCode){
		return null;
	}

	@InitBinder
	public void initDate(WebDataBinder webDataBinder){
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
		webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
	}
}
