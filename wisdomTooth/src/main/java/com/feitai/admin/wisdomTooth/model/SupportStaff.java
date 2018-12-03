/**
 * @author
 * @version 1.0
 * @desc SupportStaff
 * @since 2018-08-10 15:41:09
 */

package com.feitai.admin.wisdomTooth.model;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "sys_support_staff")
@Data
public class SupportStaff {
    private static final long serialVersionUID = -1L;

    //alias
    public static final String TABLE_ALIAS = "SupportStaff";
    public static final String ALIAS_ID = "id";
    public static final String ALIAS_NAME = "客服名字";
    public static final String ALIAS_EMAIL = "客服邮箱";
    public static final String ALIAS_ENABLE = "是否启用";


    //可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
    //columns START
    @Id
    protected Long id;

    private String name;

    @Email
    private String email;

    private Byte enable;
    //columns END

}

