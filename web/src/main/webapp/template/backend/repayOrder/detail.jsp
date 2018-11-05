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
<body>
<div style="background-color:#FFFFFF;">
<div style="background-color:#FFFFFF;width:1300px;position:relative;left: 10%" >
    <!--还款详情页 ================================================== -->
    <div>
        <h3 style="background-color:#ADADAD" ><span style="font-size:20px">基本信息</span></h3>
        <hr/>
        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    还款单号：
                </td>
                <td  width="100px" height="50px">
                    ${repayOrder.id}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    姓名：
                </td>
                <td width="100px" height="50px">
                    ${idcard.name}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    产品名称：
                </td>
                <td width="100px" height="50px">
                    ${prodcutName}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    是否新客：
                </td>
                <td width="100px" height="50px">
                    是
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    还款卡号：
                </td>
                <td width="100px" height="50px">
                    ${phPayAccount}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    提现金额：
                </td>
                <td width="100px" height="50px">
                    ${repayOrder.loanOrder.loanAmount}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    借款期限：
                </td>
                <td width="100px" height="50px">
                    ${productIdAndTerm.term}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    授信金额：
                </td>
                <td width="100px" height="50px">
                    ${shouxin}
                </td>
                <td width="100px" height="50px">

                </td>
                <td width="100px" height="50px">

                </td>
            </tr>
            </tbody>
        </table>
        <hr/>


       <h3 style="background-color:#ADADAD" ><span style="font-size:20px">还款计划</span></h3>
        <hr/>
            <div class="row">
                <div>
                    <table class="table">
                        <thead>

                        <tr>
                            <th width="70px">期数</th>
                            <th width="70px">还款日期</th>
                            <th width="70px">实还日期</th>
                            <th width="70px">当期期供</th>
                            <th width="70px">应还本金</th>
                            <th width="70px">应还利息</th>
                            <th width="70px">应还评审费</th>
                            <th width="70px">应还担保费</th>
                            <th width="70px">应还违约金</th>
                            <th width="70px">实还本金</th>
                            <th width="70px">实还利息</th>
                            <th width="70px">实还评审费</th>
                            <th width="70px">实还担保费</th>
                            <th width="70px">实还违约金</th>
                            <th width="70px">还款状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${repayPlan}" var="plan" varStatus="">
                                <tr>
                                    <td>${plan.term}</td>
                                    <td>${plan.dueDate}</td>
                                    <td>${plan.realDate}</td>
                                    <td>${plan.amount}</td>
                                    <td>${plan.pincipalAmount}</td>
                                    <td>${plan.interestAmount}</td>
                                    <td>${plan.approveFeeAmount}</td>
                                    <td>${plan.guaranteeFeeAmount}</td>
                                    <td>${plan.overdueFineAmount}</td>
                                    <td>${plan.pincipalBalance}</td>
                                    <td>${plan.interestBalance}</td>
                                    <td>${plan.approveFeeBalance}</td>
                                    <td>${plan.guaranteeFeeBalance}</td>
                                    <td>${plan.overdueFineBalance}</td>
                                   <td></td>
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