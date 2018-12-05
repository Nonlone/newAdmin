package com.feitai.admin.backend.creditdata.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.backend.creditdata.service.AttachUserBusiInforService;
import com.feitai.admin.backend.creditdata.vo.PhotoAttachViewVo;
import com.feitai.admin.backend.customer.service.PhotoService;
import com.feitai.admin.backend.properties.MapProperties;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.jieya.server.dao.attach.model.AttachUserBusiInfor;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;

@Controller
@RequestMapping("/backend/xinwang")
public class AttachUserBusiInforController extends BaseListableController<AttachUserBusiInfor>{

	@Autowired
	private AttachUserBusiInforService attachUserBusiInforService;
	
	@Autowired
    private PhotoService photoService;

    @Autowired
    private MapProperties mapProperties;
	
	@Override
	protected DynamitSupportService<AttachUserBusiInfor> getService() {
		
		return attachUserBusiInforService;
	}
	
	 @GetMapping(value = "/detail/{userId}")
	 public ModelAndView detail(@PathVariable("userId") Long userId){
		 ModelAndView modelAndView = new ModelAndView("/backend/credit/xinwang/detail");
		 AttachUserBusiInfor attachUserBusiInfor =attachUserBusiInforService.getAttachUserBusiInforByUserId(userId);
		 
		 modelAndView.addObject("attachUserBusiInfor", attachUserBusiInfor);
		 //图片
	        List<PhotoAttach> tobaccoPhotoList = photoService.findTobaccoPhotoByUserId(userId);
	        List<PhotoAttachViewVo> tobaccoPhoto = new ArrayList<>();
	        for (PhotoAttach photoAttach:tobaccoPhotoList){
	            PhotoAttachViewVo photoAttachViewVo = new PhotoAttachViewVo();
	            BeanUtils.copyProperties(photoAttach,photoAttachViewVo);
	            photoAttachViewVo.setName(mapProperties.getPhotoType(photoAttach.getType()));
	            photoAttachViewVo.setTypeName(photoAttach.getType().toString().toUpperCase());
	            tobaccoPhoto.add(photoAttachViewVo);
	        }
	        modelAndView.addObject("tobaccoPhoto",tobaccoPhoto);
		 
		 return modelAndView;
	 }
     
}
