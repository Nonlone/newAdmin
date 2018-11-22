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
<!-- Javascript goes in the document HEAD -->
<script type="text/javascript">


    function pres(name){
        var elementsByName = document.getElementsByName("name");
        for(i = 0; i < elementsByName.length; i++){
            elementsByName[i].value = elementsByName[i].value.toFixed(4);
        }
    }

    window.onload=function(){
        pres('pres');
    }
</script>


 <!-- CSS goes in the document HEAD or added to your external stylesheet -->
 <style type="text/css">
   
</style>
<body>
<div style="background-color:#FFFFFF">
<div style="background-color:#FFFFFF;width:1517px;position:relative;left: 10%">

    <!--提现详情页 ================================================== -->
    <div>

        <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">订单信息</span></h3>
        <hr/>
        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <span style="font-size:13px;padding: 5px;">基本信息：</span>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    订单号：
                </td>
                <td width="100px" height="50px">
                    ${id}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    放款状态：
                </td>
                <td width="100px" height="50px">
                    ${status}
                </td>

                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    借款用途：
                </td>
                <td width="100px" height="50px">
                    ${loanPurpose}
                </td>

            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    产品名称：
                </td>
                <td width="100px" height="50px">
                    ${product.name}
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
            </tbody>
        </table>

        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <span style="font-size:13px;padding: 5px;">日期信息：</span>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    订单创建日期：
                </td>
                <td width="100px" height="50px">
                    ${createdTime}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    借款日期：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty loanTime}">
                        未借款
                    </c:if>
                    ${loanTime}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    申请日期：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty applyTime}">
                        未申请
                    </c:if>
                    ${applyTime}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    审批时间：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty approveTime}">
                        未审批
                    </c:if>
                    ${approveTime}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    银行审批时间：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty bankApproveTime}">
                        未审批
                    </c:if>
                    ${bankApproveTime}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    放款日期：
                </td>
                <td width="100px" height="50px">
                    <c:if test="${empty payLoanTime}">
                        未放款
                    </c:if>
                    ${payLoanTime}
                </td>
            </tr>
            </tbody>
        </table>

        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <span style="font-size:13px;padding: 5px;">订单数据：</span>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    期数(月)：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.loanTerm}
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
                    ${productIdAndTerm.interestRate*100}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    评审费率(%)：
                </td>
                <td name="pres" width="100px" height="50px">
                    ${productIdAndTerm.approveFeeRate*100}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    担保费率（%）：
                </td>
                <td name="pres" width="100px" height="50px">
                    ${productIdAndTerm.guaranteeFeeRate*100}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    提现金额：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.loanAmount}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    授信金额：
                </td>
                <td width="100px" height="50px">
                    ${shouxin}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    净收金额(合同本金-首期各种费用)：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.netReceiveAmount}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    待还余额：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.balanceAmount}
                </td>
                <td bgcolor="#F2F2F2" width="100px" height="50px">
                    已还金额：
                </td>
                <td width="100px" height="50px">
                    ${loanOrder.paidAmount}
                </td>
                <c:if test="${not empty tongDunData }">
                    <td bgcolor="#F2F2F2" width="100px" height="50px">
                        同盾设备指纹
                    </td>
                    <td width="100px" height="50px">
                        <a id="btnShow" href="javascript:void(0);">显示</a>
                    </td>
                </c:if>
                <c:if test="${empty tongDunData }">
                    <td bgcolor="#F2F2F2" width="100px" height="50px">
                    </td>
                    <td width="100px" height="50px">
                    </td>
                </c:if>
            </tr>
            </tbody>
        </table>

        <div style="display: none" id="blackBox">
            <div style="display:block;word-break: break-all;word-wrap: break-word;">${tongDunData.blackBox}</div>
        </div>



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
            <li id="li_base" class="active"><a href="javascript:void(0)" onclick="load(this,'baseData')">基本资料</a></li>
            <li id="li_bond"><a href="javascript:void(0)" onclick="load(this,'repayPlan')">还款计划</a></li>
            <c:forEach items="${faddDetails}" var="fadd">
                <li id="li_credit"><a href="javascript:void(0)" onclick="load(this,'${fadd.id}')">${fadd.contractName}</a></li>
            </c:forEach>
        </ul>

        <div id="tabContext" style="margin-bottom: 10px;">
            <!-- 基本资料 -->
            <div id = "baseData">
                <iframe frameborder="no" border="0" src="${ctx}/backend/customer/detail/${user.id}" style="width: 1517px;min-height: 1000px;overflow-x: hidden;overflow-y: auto"></iframe>
            </div>
            <div id = "repayPlan" style="display:none;">
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
            <c:forEach items="${faddDetails}" var="fadd">
                <div id="${fadd.id}" style="display:none;"><iframe frameborder="no" border="0" src="${fadd.viewpdfUrl}" style="width: 1517px;min-height: 1000px;overflow-x: hidden;overflow-y: auto"></iframe></div>
            </c:forEach>
        </div>
    </div>

    <!-- script end -->
</div>
</div>
</body>
<script type="text/javascript">


    function load(ele, obj) {
        if(obj=='repayPlan'){
            var havePlan = ${havePlan};
            if(!havePlan){
                BUI.use('bui/overlay', function (Overlay) {
                    new Overlay.Dialog({
                        title: '提示窗口',
                        width: 300,
                        height: 150,
                        mask: false,
                        buttons: [],
                        bodyContent: '<p>该订单还没有对应的还款计划！</p>'
                    }).show();
                });
                return;
            }
        }

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