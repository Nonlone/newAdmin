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
<style type="text/css">
    .photo-idcard {
        width: 150px;
        height: 150px
    }
</style>
<body>
<div style="background-color:#FFFFFF;width:1500px;margin: 10px auto;">
    <div>
        <h3 style="background-color:#ADADAD">
            <span style="font-size:20px;padding: 5px;">订单信息</span></h3>
        <div style="margin-top: 10px;margin-bottom: 10px;">
            <table cellspacing="0" class="table table-bordered">
                <tbody>
                <tr>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        订单编号
                    </td>
                    <td width="70px" height="30px">
                        ${card.id}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        客户 ID
                    </td>
                    <td width="70px" height="30px">
                        <c:if test="${not empty user}">
                            ${user.id}
                        </c:if>
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        注册手机
                    </td>
                    <td width="70px" height="30px">
                        ${hyPhone}
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        实名状态
                    </td>
                    <td width="70px" height="30px">
                        ${userAuth}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        产品名称
                    </td>
                    <td width="70px" height="30px">
                        ${card.product.remark}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        产品状态
                    </td>
                    <td width="70px" height="30px">
                        ${cardStatus}
                        <c:if test="${card.status.value > 89 && rejectReason ne null}">
                            (&nbsp;${rejectReason}&nbsp;)
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2">
                        授权项
                    </td>
                    <td width="70px" height="30px">
                        ${auths}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        进件渠道
                    </td>
                    <td width="70px" height="30px">
                        <c:if test="${card.applyChannelId ne null}">${card.applyChannelId}</c:if>
                        <c:if test="${card.applyChannelId eq null}">无</c:if>
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        注册渠道
                    </td>
                    <td width="70px" height="30px">
                        <c:if test="${card.registChannelId ne null}">${card.registChannelId}</c:if>
                        <c:if test="${card.registChannelId eq null}">无</c:if>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        注册客户端类型：
                    </td>
                    <td width="70px" height="30px">
                        ${user.osType}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        授信类型
                    </td>
                    <td width="70px" height="30px">
                        <c:if test="${card.approvalFlag ne null}">${card.approvalFlag}</c:if>
                        <c:if test="${card.approvalFlag eq null}">无</c:if>
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        授信额度
                    </td>
                    <td width="70px" height="30px">
                      <fmt:formatNumber type="number" value="${card.creditSum}" pattern="0.00" maxFractionDigits="2"/>                       
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        订单创建时间
                    </td>
                    <td width="70px" height="30px">
                        ${createdTime}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        提交审批时间
                    </td>
                    <td width="70px" height="30px">
                        ${submitTime}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        收到审批时间
                    </td>
                    <td width="70px" height="30px">
                        ${creditTime}
                    </td>
                </tr>
                <tr>                   
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        拒绝原因
                    </td>
                    <td width="70px" height="30px">
                        ${rejectReason}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                    </td>
                    <td width="70px" height="30px">
                    </td>
                     <td bgcolor="#F2F2F2" width="30px" height="30px">
                        
                    </td>
                    <td width="70px" height="30px">

                    </td>
                </tr>
                <c:if test="${not empty tongDunData }">
                    <tr>
                        <td bgcolor="#F2F2F2" width="30px" height="30px">
                            同盾设备客户端
                        </td>
                        <td width="70px" height="30px">
                                ${tongDunData.osType}
                        </td>
                        <td bgcolor="#F2F2F2" width="30px" height="30px">
                            同盾设备指纹
                        </td>
                        <td width="70px" height="30px">
                            <a id="btnShow" href="javascript:void(0);">显示</a>
                            <div style="display: none" id="blackBox">
                                <div style="display:block;word-break: break-all;word-wrap: break-word;">${tongDunData.blackBox}</div>
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
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
    <div>
        <c:if test="${not empty authsList }">
            <ul id="tabHeader" class="nav-tabs">
                <li id="li_base" class="active"><a href="javascript:void(0)" onclick="load(this,'baseData')">基本资料</a></li>
                <c:forEach items="${authsList}" var="auth">
                    <li id="li_credit" data-callback="${auth.function}"><a href="javascript:void(0)" onclick="load(this,'${auth.function}')">${auth.name}</a></li>
                </c:forEach>
                <c:if test="${card.status.value!=-1&&card.status.value!=0}">
                    <li id="li_bond" data-callback="sauron_daihoubang"><a href="javascript:void(0)" onclick="load(this,'sauron_daihoubang')">贷后邦报告</a></li>
                </c:if>
            </ul>
        </c:if>
        <div id="tabContext" style="margin-bottom: 10px;">
            <!-- 基本资料 -->
            <div id="baseData">
                <iframe frameborder="no" border="0" src="${ctx}/backend/customer/detail/${user.id}" style="width: 1517px;min-height: 1000px;overflow-x: hidden;overflow-y: auto"></iframe>
            </div>
            <c:forEach items="${authsList}" var="auth">
                <div id="${auth.function}" style="display:none;"></div>
            </c:forEach>
            <!-- 贷后邦报告 -->
            <div id="sauron_daihoubang" style="display:none;"></div>
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


    /**
     * 获取模板渲染方法
     * */
    function getCreditData(templateUrl,callbackFunction){
        // 统一设置了 dataType 为 json，需要设置一下
        $.get(templateUrl,function(resp){
           callbackFunction(resp);
        },"text");
    }

    /**
     * 税务贷（金财）
     * */
    function tax_jchl(){
        getCreditData("/static/template/jincai_tax.tpl",function(resp){
            $.ajax({
                type: "post",
                url: "/backend/creditdata/jincaiTax",
                data:"userId=${card.userId}",
                dataType: "json",
                success: function (response) {
                    console.log(response)
                    if(response.code==0
                        && !jQuery.isEmptyObject(response.data)){
                        debugger;
                        var html = template.render(resp,response.data.report);
                        $("#tax_jchl").html("").html(html);
                    }else{
                        BUI.use('bui/overlay', function (Overlay) {
                            new Overlay.Dialog({
                                title: '提示窗口',
                                width: 300,
                                height: 150,
                                mask: false,
                                buttons: [],
                                bodyContent: '<p>该用户的此类征信信息错误</p>'
                            }).show();
                        });
                    }
                }
            });
        });
    }


    /**
     * 贷后帮（索伦-葫芦数据）
     * */
    function sauron_daihoubang(){
        getCreditData("/static/template/sauron_daihoubang.tpl",function(resp){
            $.ajax({
                type: "post",
                url: "/backend/creditdata/sauron",
                data:"userId=${card.userId}",
                dataType: "json",
                success: function (response) {
                    console.log(response)
                    if(response.code==0
                        && !jQuery.isEmptyObject(response.data)){
                        var html = template.render(resp,response.data.report);
                        $("#sauron_daihoubang").html("").html(html);
                    }else{
                        BUI.use('bui/overlay', function (Overlay) {
                            new Overlay.Dialog({
                                title: '提示窗口',
                                width: 300,
                                height: 150,
                                mask: false,
                                buttons: [],
                                bodyContent: '<p>该用户的此类征信信息错误</p>'
                            }).show();
                        });
                    }
                }
            });
        });
    }

    /**
     *  运营商——魔蝎
     * */
    function operator_moxie(){
        var url = 'https://tenant.51datakey.com/carrier/report_data?data=';
        $.post("/backend/creditdata/moxie","cardId=${card.id}&userId=${card.userId}",function(response){
            if(response.code==0
                && !jQuery.isEmptyObject(response.data)){
                var $iframe = $("<iframe></iframe>");
                $iframe.attr("width","100%");
                $iframe.attr("height","1000px");
                $iframe.attr("scrolling","yes");
                $iframe.attr("frameborder","no");
                $iframe.attr('src', url+response.data.reportMessage);
                $("#operator_moxie").html("").html($iframe);
            }else{
                BUI.use('bui/overlay', function (Overlay) {
                    new Overlay.Dialog({
                        title: '提示窗口',
                        width: 300,
                        height: 150,
                        mask: false,
                        buttons: [],
                        bodyContent: '<p>该用户的此类征信信息错误</p>'
                    }).show();
                });
            }
        });
    }


    /**
     * 人行简版——算话
     *
     * */
    function pbccrc_suanhua(){
        getCreditData("/static/template/pbccrc_suanhua.tpl",function(resp){
            $.ajax({
                type: "post",
                url: "/backend/creditdata/suanhua",
                data:"userId=${card.userId}",
                dataType: "json",
                success: function (response) {
                    console.log(response)
                    if(response.code==0
                        && !jQuery.isEmptyObject(response.data)){
                        var html = template.render(resp,response.data.report);
                        $("#pbccrc_suanhua").html("").html(html);
                    }else{
                        BUI.use('bui/overlay', function (Overlay) {
                            new Overlay.Dialog({
                                title: '提示窗口',
                                width: 300,
                                height: 150,
                                mask: false,
                                buttons: [],
                                bodyContent: '<p>该用户的此类征信信息错误</p>'
                            }).show();
                        });
                    }
                }
            });
        });
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