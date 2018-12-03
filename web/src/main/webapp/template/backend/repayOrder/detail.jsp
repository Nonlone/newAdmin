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
<div style="background-color:#FFFFFF;width:1517px;position:relative;left: 10%" >
    <!--还款详情页 ================================================== -->
    <div>
        <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">还款订单信息</span></h3>
        <hr/>
        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <span style="font-size:13px;padding: 5px;">基本信息：</span>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    客户姓名：
                </td>
                <td width="100px" height="50px">
                    ${idcard.name}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    身份证号：
                </td>
                <td width="100px" height="50px">
                    ${hyIdcard}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    客户Id：
                </td>
                <td width="100px" height="50px">
                    ${user.id}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    订单号：
                </td>
                <td width="100px" height="50px">
                    ${id}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    提现订单号：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.id}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    还款状态：
                </td>
                <td width="100px" height="50px">
                    ${status}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    产品名称：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.product.remark}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    资金方：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty fundName}">
                        无对应资金方
                    </c:if>
                    ${fundName}
                </td>

                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    申请渠道：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.applyChannelId}
                </td>
            </tr>
            <tr>

                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    支付方式：
                </td>
                <td width="100px" height="50px">
                    ${payType}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    支付金额：
                </td>
                <td width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">
                    ${repayOrder.amount}
                    </fmt:formatNumber>
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    存储信贷系统的出账编号:
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty putOutNo}">
                        暂无
                    </c:if>
                    ${putOutNo}
                </td>
            </tr>
            </tbody>
        </table>

        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <span style="font-size:13px;padding: 5px;">日期信息：</span>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    放款日期：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty payLoanTime}">
                        未放款
                    </c:if>
                    ${payLoanTime}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    最迟还款日：
                </td>
                <td width="100px" height="50px">
                    ${dueDate}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    逾期天数：
                </td>
                <td width="100px" height="50px">
                    ${overdueDays}
                </td>
            </tr>

            </tbody>
        </table>

        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <span style="font-size:13px;padding: 5px;">订单数据：</span>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    订单期数/总期数(月)：
                </td>
                <td width="100px" height="50px">
                    ${termPre}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    收款银行卡：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty payCard}">
                        客户未填写
                    </c:if>
                    ${payCard}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    还款银行卡：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty repayCard}">
                        客户未填写
                    </c:if>
                    ${repayCard}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    利率(%年）：
                </td>
                <td name="pres" width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="4" minFractionDigits="4">
                    ${productIdAndTerm.interestRate*100}
                    </fmt:formatNumber>
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    评审费率(%)：
                </td>
                <td name="pres" width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="4" minFractionDigits="4">
                    ${productIdAndTerm.approveFeeRate*100}
                    </fmt:formatNumber>
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    担保费率（%）：
                </td>
                <td name="pres" width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="4" minFractionDigits="4">
                    ${productIdAndTerm.guaranteeFeeRate*100}
                    </fmt:formatNumber>
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    提现金额：
                </td>
                <td width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">
                    ${loanOrder.loanAmount}
                    </fmt:formatNumber>
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    待还余额：
                </td>
                <td width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">
                    ${loanOrder.balanceAmount}
                    </fmt:formatNumber>
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    已还金额：
                </td>
                <td width="100px" height="50px">
                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2">
                    ${loanOrder.paidAmount}
                    </fmt:formatNumber>
                </td>
            </tr>

            </tbody>
        </table>

        <div>
            <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">地区信息</span></h3>
            <div style="margin-top:10px;margin-bottom: 10px;">
                <table cellspacing="0" class="table table-bordered">
                    <thead>
                    <th width="50px">步骤</th>
                    <th width="80px">省份名</th>
                    <th width="80px">城市名</th>
                    <th width="80px">地区名</th>
                    <th >地址</th>
                    <th width="100px">ip</th>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty areaList}">
                            <c:forEach items="${areaList}" var="area">
                                <tr>
                                    <td >${area.segmentName}</td>
                                    <td >${area.provinceName}</td>
                                    <td >${area.cityName}</td>
                                    <td >${area.districtName}</td>
                                    <td >${area.location}</td>
                                    <td >${area.ip}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td style="text-align: center;" colspan="6" width="900px">无</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <ul id="tabHeader" class="nav-tabs">
            <li id="li_bond"><a href="javascript:void(0)" onclick="load(this,'repayPlan')">还款计划</a></li>
        </ul>

        <div id="tabContext" style="margin-bottom: 10px;">
            <div id = "repayPlan" style="display:none;">
                <iframe frameborder="no" border="0" src="${ctx}/backend/loan/repayOrder/repayPlan/${loanOrder.id}" style="width: 1517px;min-height: 1000px;overflow-x: hidden;overflow-y: auto"></iframe>
            </div>
        </div>

        </div>

</div>
</div>
</body>
<script type="text/javascript">


    function load(ele, obj) {

        // 样式控制
        var $ele = $(ele);
        $ele.parent().addClass("active").siblings().removeClass("active");
        var $content = $("#" + obj);
        $content.show().siblings().hide();
        // 判断标记渲染
        if($ele.data("setted")==null){
            // 调用对应渲染方法
            var callbackFunName = $ele.parent().attr("data-callback");
            if (callbackFunName == null || callbackFunName == "")
                return;
            var callbackFun=eval(callbackFunName);
            new callbackFun();
            // 标记渲染
            $ele.data("setted",jQuery.noop);
        }
    }

    BUI.use('bui/overlay', function (Overlay) {

        $('#btnShow').on('click', function () {
            new Overlay.Dialog({
                title: '同盾设备指纹',
                width: 400,
                height: 200,
                mask: false,
                buttons: [],
                bodyContent: $("#blackBox").html()//'<div style="width:500px;word-wrap:break-word;">'+$("#btnShow").attr("blackBox")+'</div>'//'<p></p>'
            }).show();
        });

    });
</script>
</html>