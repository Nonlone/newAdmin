<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../../common/import-tags.jsp" %>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../../common/import-static.jsp" %>
</head>
<body data-userId="${user.id}" style="width:1500px;max-width:1500px;">
<div style="background-color:#FFFFFF;margin: 10px auto;">
    <div>
        <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">烟草贷补充资料</span></h3>
        <div>
            <table cellspacing="0" class="table table-bordered" style="margin-top: 15px;">
                <tbody>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                        月收入：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:if test="${empty authdataTobacoo.monthlyIncome}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.monthlyIncome}
                    </td>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                        有无本地房产：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:if test="${authdataTobacoo.houseProperty eq 1}">
                        有
                        </c:if>
                        <c:if test="${authdataTobacoo.houseProperty eq 0}">
                        无
                        </c:if>
                        <c:if test="${empty authdataTobacoo.houseProperty}">
                            未填写
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        社交账号类型：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle; ">
                        <c:if test="${empty authdataTobacoo.socialTypeName}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.socialTypeName}
                    </td>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                        社交账号：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:if test="${empty authdataTobacoo.socialNo}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.socialNo}
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                        配偶证件类型：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:if test="${empty authdataTobacoo.spouseIdTypeName}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.spouseIdTypeName}
                    </td>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        配偶证件号码：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle; ">
                        <c:if test="${empty authdataTobacoo.spouseIdNo}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.spouseIdNo}
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        住宅邮编：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:if test="${empty authdataTobacoo.familyZip}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.familyZip}
                    </td>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        单位邮编：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:if test="${empty authdataTobacoo.workZip}">
                            未填写
                        </c:if>
                        ${authdataTobacoo.workZip}
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
    </div>

    </div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        //放大图片
        $('img.dialog').on('click', function () {
            var large_image = '<img style=\'max-height: 800px;max-width: 800px\' class=\'closeImg\' src= ' + $(this).attr("src") + '></img>';
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
            });
        });

    });
</script>
</html>