package com.feitai.admin.backend.creditdata.vo;

import com.feitai.jieya.server.dao.attach.model.PhotoAttach;
import lombok.Data;
import lombok.ToString;

/**
 * detail:
 * author:
 * date:2018/11/29
 */
@Data
@ToString
public class PhotoAttachViewVo extends PhotoAttach {

    private String name;

    private String typeName;
}
