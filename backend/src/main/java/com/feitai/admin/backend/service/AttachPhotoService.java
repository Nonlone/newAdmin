package com.feitai.admin.backend.service;

import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service
@Slf4j
public class AttachPhotoService extends DynamitSupportService<PhotoAttach> {

	public List<PhotoAttach> findByUserId(long userId) {
		// TODO Auto-generated method stub
		Example example = Example.builder(PhotoAttach.class).andWhere(Sqls.custom().andEqualTo("userId",userId)).build();
		return mapper.selectByExample(example);
	}
}
