<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/import-static.jsp"%>
</head>
<style type="text/css">
.photo-idcard{
	width:150px;
	height:150px
}
</style>
<body>
<div style="background-color:#FFFFFF">
<div style="background-color:#FFFFFF;width:1300px;position:relative;left: 10%">

    <div>
        <h3 style="background-color:#ADADAD" ><span style="font-size:20px">账号信息</span></h3>
        <hr/>
        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <tr>
                <td bgcolor="#F2F2F2" width="50px" height="30px">
                    姓名：
                </td>
                <td width="100px" height="30px">
                    <c:if test="${not empty idcard}">${idcard.name}</c:if>
                    <c:if test="${empty idcard}">未实名</c:if>
                </td>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    身份证号：
                </td>
                <td  width="100px" height="30px">
				${hyIdcard}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    民族：
                </td>
                <td width="100px" height="30px">
                ${idcard.nation}
                </td>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    性别：
                </td>
                <td width="100px" height="30px">
                ${idcard.sex}
                </td>
            </tr>
            <tr>
				<td bgcolor="#F2F2F2" width="50px"  height="30px">
					用户ID：
				</td>
				<td width="100px" height="30px">
					${idcard.userId}
				</td>

                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    注册时间：
                </td>
                <td width="100px" height="30px">
					${user.createdTime}
                </td>
            </tr>

            </tbody>
        </table>
		<hr/>

        <h3 style="background-color:#ADADAD" ><span style="font-size:20px">收款卡<c:if test="${empty payCard}">(未绑卡)</c:if></span></h3>
        <hr/>
        <div class="row">
            <div>
                <table class="table">
                    <thead>

                    <tr>
                        <th width="70px">银行名称</th>
                        <th width="70px">银行卡卡号</th>
                        <th width="150px">资金方</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${payCard}" var="pay" varStatus="">
                        <tr>
                            <td>${pay.bankName}</td>
                            <td>${pay.bankCardNo}</td>
                            <td>${pay.fund}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <hr/>

        <h3 style="background-color:#ADADAD" ><span style="font-size:20px">还款卡<c:if test="${empty repayCard}">(未绑卡)</c:if></span></h3>
        <hr/>
        <div class="row">
            <div>
                <table class="table">
                    <thead>

                    <tr>
                        <th width="70px">银行名称</th>
                        <th width="70px">银行卡卡号</th>
                        <th width="150px">资金方</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${repayCard}" var="repay" varStatus="">
                        <tr>
                            <td>${repay.bankName}</td>
                            <td>${repay.bankCardNo}</td>
                            <td>${repay.fund}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>
</div>
</body>
</html>