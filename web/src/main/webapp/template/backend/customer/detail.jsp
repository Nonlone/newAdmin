<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../common/import-tags.jsp" %>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../common/import-static.jsp" %>
</head>
<body data-userId="${user.id}">
<div style="background-color:#FFFFFF">
    <div style="background-color:#FFFFFF;width:1300px;position:relative;left: 1%;margin-top: 10px;">
        <div>
            <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">身份信息</span></h3>
            <div>
                <table cellspacing="0" class="table table-bordered" style="margin-top: 15px;">
                    <tbody>
                    <tr>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                            姓名：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle;">
                            ${idCardDataExtend.name}
                        </td>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                            身份证号：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle;">
                            ${hyIdcard}
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                            民族：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle; ">
                            ${idCardDataExtend.nation}
                        </td>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                            性别：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle;">
                            ${idCardDataExtend.sex}
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                            出生日期：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle;">
                            ${birthday}
                        </td>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                            年龄：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle; ">
                            ${age}
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                            身份证地址：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle;">
                            ${idCardDataExtend.address}
                        </td>
                        <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                            签发机关：
                        </td>
                        <td width="100px" height="30px" style="vertical-align: middle;">
                            ${idCardDataExtend.orgination}
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div>
            <h3 style="background-color:#ADADAD">
                <span style="font-size:20px;padding: 5px;">影像信息</span>
            </h3>
            <div style="margin-top: 10px; margin-bottom: 10px;">
                <div style="font-size: 14px;">
                    <table cellspacing="0" class="table table-bordered" style="margin-top: 15px;">
                        <tbody>
                        <tr>
                            <td bgcolor="#F2F2F2" width="150px" height="30px"
                                style="vertical-align: middle;text-align: center; ">活体对比公安4K
                            </td>
                            <td style="vertical-align: middle;text-align: center; ">${authVerify}</td>
                            <td bgcolor="#F2F2F2" width="150px" height="30px"
                                style="vertical-align: middle;text-align: center; ">活体对比身份证正面
                            </td>
                            <td style="vertical-align: middle;text-align: center; ">${livingVerify}</td>
                            <td bgcolor="#F2F2F2" width="150px" height="30px"
                                style="vertical-align: middle;text-align: center; ">活体防Hack值
                            </td>
                            <td style="vertical-align: middle;text-align: center; ">${hackScore}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div style="margin-top: 20px;">
                <c:if test="${not empty customerPhotos}">
                    <div>
                        <c:forEach items="${customerPhotos}" var="customerPhoto">
                            <div style="float: left;margin: auto 20px;">
                                <div>
                                    <img style="max-height: 200px;max-width: 200px;"
                                         class="photo-${customerPhoto.type} dialog" src="${customerPhoto.path}">
                                </div>
                                <div style="text-align: center;margin-top: 5px;margin-bottom: 5px;">
                                    <span>${customerPhoto.name}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div style="clear: both"></div>
                </c:if>
            </div>
        </div>

        <div>
            <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">基本信息</span></h3>
            <div style="margin-top:10px;margin-bottom: 10px;">
                <table cellspacing="0" class="table table-bordered">
                    <tbody>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">居住类型</td>
                        <td width="250px">${residentialType}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">婚姻状况</td>
                        <td width="250px">${maritalStatus}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">生育状况</td>
                        <td width="250px">${fertilityStatus}&nbsp;子女</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">最高学历</td>
                        <td width="250px">${educationLevel}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">手机号</td>
                        <td width="250px">${hyPhone}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">学历</td>
                        <td width="250px">${educationLevel}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">居住地</td>
                        <td width="250px">${person.provinceName}&nbsp;${person.cityName}&nbsp;${person.districtName}&nbsp;${person.address}</td>
                    </tr>
                    <c:if test="${ person.maritalStatus==2}">
                        <tr>
                            <td bgcolor="#F2F2F2" width="80px">配偶名字</td>
                            <td width="250px">${person.spouseName}</td>
                        </tr>
                        <tr>
                            <td bgcolor="#F2F2F2" width="80px">配偶联系方式</td>
                            <td width="250px">${person.spouseContact}</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div>
            <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">工作信息</span></h3>
            <div style="margin-top: 10px;margin-bottom: 10px;">
                <table cellspacing="0" class="table table-bordered">
                    <tbody>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">所属行业</td>
                        <td width="200px">${belongIndustry}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">工作类型</td>
                        <td width="200px">${jobsType}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">单位名称</td>
                        <td width="200px">${work.companyName}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">单位地址</td>
                        <td width="200px">${work.provinceName}&nbsp;${work.cityName}&nbsp;${districtName}&nbsp;${work.companyAddress}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">单位联系人手机</td>
                        <td width="200px">${hyWorkContactPhone}</td>
                    </tr>
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">单位联系人固话</td>
                        <c:if test="${not empty work.contactTelephoneZoneCode}">
                        <td width="200px">
                            (${work.contactTelephoneZoneCode})&nbsp;${work.contactTelephone} <c:if test="${not empty work.contactTelephoneExtension}">&nbsp;-&nbsp;${work.contactTelephoneExtension}</c:if>
                        </td>
                        </c:if>
                        <c:if test="${empty work.contactTelephoneZoneCode}">
                            <td width="200px">${work.contactTelephone}<c:if test="${not empty work.contactTelephoneExtension}">&nbsp;-&nbsp;${work.contactTelephoneExtension}</c:if></td>
                        </c:if>
                    </tr>
                    <c:if test="${not empty work.organizationCode}">
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">公司企业机构代码</td>
                        <td width="200px">${work.organizationCode}</td>
                    </tr>
                    </c:if>
                    <c:if test="${not empty work.businessLicenseUrl}">
                    <tr>
                        <td bgcolor="#F2F2F2" width="80px">公司营业执照</td>
                        <td width="200px"><img class="photo-businessLicenseUrl dialog" src="${work.businessLicenseUrl}" ${work.businessLicenseUrl} style="max-height: 200px;max-width: 200px;"/></td>
                    </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div style="height: 30px;"></div>
    </div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        //放大图片
        $('img.dialog').on('click', function () {
            var large_image = '<img class=\'closeImg\' src= ' + $(this).attr("src") + '></img>';
            BUI.use('bui/overlay', function (Overlay) {
                var width = this.width;
                var height = this.height;
                var dialog = new Overlay.Dialog({
                    title: '图片放大',
                    width: width,
                    height: height,
                    mask: false,
                    buttons: [],
                    bodyContent: large_image
                });
                dialog.show();
            });
        });

    });
</script>
</html>