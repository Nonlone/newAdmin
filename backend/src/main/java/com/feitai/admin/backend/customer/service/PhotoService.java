package com.feitai.admin.backend.customer.service;

import com.feitai.admin.core.service.ClassPrefixDynamicSupportService;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import com.feitai.jieya.server.dao.base.constant.PhotoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class PhotoService extends ClassPrefixDynamicSupportService<PhotoAttach> {

    public List<PhotoAttach> findUserPhotoByUserId(long userId) {
        Example example = Example.builder(PhotoAttach.class).andWhere(
                Sqls.custom().andEqualTo("userId", userId)
                        .andIn("type", Arrays.asList(new Integer[]{
                                PhotoType.IDCARD_PROTRAIT.getValue(),
                                PhotoType.IDCARD_EMBLEM.getValue(),
                                PhotoType.NOD.getValue(),
                                PhotoType.BLINK.getValue(),
                                PhotoType.SHAKE.getValue(),
                                PhotoType.MOUTH.getValue(),
                                PhotoType.QC_LIVING_IMG.getValue(),
                        }))
        ).orderByAsc("type").build();
        return mapper.selectByExample(example);
    }

    public List<PhotoAttach> findLoanVoucherPhotoByUserId(long userId) {
        Example example = Example.builder(PhotoAttach.class).andWhere(
                Sqls.custom().andEqualTo("userId", userId)
                        .andIn("type", Arrays.asList(new Integer[]{
                                PhotoType.LOAN_VOUCHER.getValue()
                        }))
        ).orderByAsc("type").build();
        return mapper.selectByExample(example);
    }


    public List<PhotoAttach> findTobaccoPhotoByUserId(long userId) {
        Example example = Example.builder(PhotoAttach.class).andWhere(
                Sqls.custom().andEqualTo("userId", userId)
                        .andIn("type", Arrays.asList(new Integer[]{
                                PhotoType.MARRIAGE_CERTIFICATE.getValue(),
                                PhotoType.FRANCHISE_PERMIT.getValue()
                        }))
        ).orderByAsc("type").build();
        return mapper.selectByExample(example);
    }
}
