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
                        ${card.product.name}
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
                        授信额度
                    </td>
                    <td width="70px" height="30px">
                        ${card.creditSum}
                    </td>
                    <td bgcolor="#F2F2F2" width="30px" height="30px">
                        客户端类型：
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
                </tbody>
            </table>
        </div>
    </div>
    <div>
        <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">地区信息</span></h3>
        <div style="margin-top:10px;margin-bottom: 10px;">



            <table cellspacing="0" class="table table-bordered">
                <thead>
                    <th width="150px">步骤</th>
                    <th width="150px">省份名</th>
                    <th width="150px">城市名</th>
                    <th width="150px">地区名</th>
                    <th width="150px">地址</th>
                    <th width="150px">ip</th>
                </thead>
                <tbody>
                <c:if test="${not empty areaList}">
                    <c:forEach items="${areaList}" var="area">
                        <tr>
                            <td  width="150px">${area.segmentName}</td>
                            <td  width="150px">${area.provinceName}</td>
                            <td  width="150px">${area.cityName}</td>
                            <td  width="150px">${area.districtName}</td>
                            <td  width="150px">${area.location}</td>
                            <td  width="150px">${area.ip}</td>
                        </tr>

                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
    <div>
        <ul class="nav-tabs">
            <li id="li_base" class="active"><a href="javascript:void(0)" onclick="loadAll('base')">基本资料</a></li>
            <li id="li_credit"><a href="javascript:void(0)" onclick="loadAll('credit')">征信报告</a></li>
            <li id="li_operator"><a href="javascript:void(0)" onclick="loadAll('operator')">运营商报告</a></li>
            <li id="li_bond"><a href="javascript:void(0)" onclick="loadAll('bond')">贷后邦报告</a></li>
        </ul>
        <div style="margin-bottom: 10px;">
            <!-- 征信报告 -->
            <div id="credit" hidden="true">

                <script type="text/html" src="${ctx}/static/artTemplate/template/creditTemplate.tpl"></script>

            </div>

            <!-- 运营商报告 -->
            <div id="operator" hidden="true">
                <iframe name="operatorIframe" id="operatorIframe" width="100%" height='100%' scrolling="yes" frameborder="no"
                        border="0"></iframe>
            </div>

            <!-- 贷后邦报告 -->
            <div id="bond" hidden="true">

                <script type="text/html" src="${ctx}/static/artTemplate/template/templateTest.tpl"></script>

            </div>

            <!-- 基本资料 -->
            <div id="baseData">
                <iframe  frameborder="no"  border="0"  src="${ctx}/backend/customer/detail/${user.id}" style="width: 1517px;min-height: 1000px;overflow-x: hidden;overflow-y: auto"></iframe>
            </div>
        </div>
    </div>



</div>

</body>
<script type="text/javascript">


    var baseData = document.getElementById("baseData");
    var credit = document.getElementById("credit");
    var operator = document.getElementById("operator");
    var bond = document.getElementById("bond");
    var li_base = document.getElementById("li_base");
    var li_credit = document.getElementById("li_credit");
    var li_operator = document.getElementById("li_operator");
    var li_bond = document.getElementById("li_bond");

    function removeClassAddHidden() {
        li_base.removeAttribute("class");
        li_credit.removeAttribute("class");
        li_operator.removeAttribute("class");
        li_bond.removeAttribute("class");
        baseData.setAttribute("hidden", "true");
        credit.setAttribute("hidden", "true");
        operator.setAttribute("hidden", "true");
        bond.setAttribute("hidden", "true");
    }

    function loadAll(type) {
        removeClassAddHidden();
        if (type == 'base') {//基本资料
            baseData.removeAttribute("hidden");
            li_base.setAttribute("class", "active");
        } else {
            if (type == 'credit') {//征信报告
                credit.removeAttribute("hidden");
                li_credit.setAttribute("class", "active");
                creditHtml();
            }
            if (type == 'operator') {//运营商报告
                operator.removeAttribute("hidden");
                li_operator.setAttribute("class", "active");
                operatorHtml();
            }
            if (type == 'bond') {//贷后邦报告
                bond.removeAttribute("hidden");
                li_bond.setAttribute("class", "active");
                bondHtml();
            }
        }

    }

    /***
     * 征信入口
     */
    function creditHtml() {
        //容器id
        var type = "#credit";
        //json
        var data = getData(type);
        //模板url
        var url = "${ctx}/static/artTemplate/template/creditTemplate.tpl";
        produceHtml(url, data, type);
    }

    /***
     * 运营商入口
     */
    function operatorHtml() {
        //容器id
        var type = "#operator";
        //json
        var data = getData(type);
        //模板url
        var url = "";
        produceHtml(url, data, type);
    }

    /***
     * 贷后邦入口
     */
    function bondHtml() {
        //容器id
        var type = "#bond";
        //模板url
        var url = "${ctx}/static/artTemplate/template/templateTest.tpl";
        //json
        var data = getData(type);
        produceHtml(url, data, type);
    }

    /***
     * 通用获取数据的方法
     */
    function getData(type) {
        var datajson = "";
        var source = "";
        if (type == "#bond") {
            source = "bond";
        } else if (type == "#operator") {
            source = "operator";
        } else if (type == "#credit") {
            source = "credit";
        }
        $.ajax({
            type: "post",
            url: "${ctx}/backend/data/acquire/getData",
            async: false,
            data: {cardId: "${card.id}", type: source},
            dataType: "json",
            success: function (data) {
                datajson = data;
            }
        });
        return datajson;
    }

    /***
     * 通用生产HTML方法
     */
    function produceHtml(url, jsonData, type) {
        if (jsonData == "nulldata" || jsonData == "" || jsonData == null) {
            BUI.use('bui/overlay', function (Overlay) {
                var dialog = new Overlay.Dialog({
                    title: '提示窗口',
                    width: 500,
                    height: 300,
                    mask: false,
                    buttons: [],
                    bodyContent: '<p>没有该用户的此类征信信息</p>'
                });
                dialog.show();
            });
        } else {
            if (type != "#operator") {
                $.ajax({
                    type: "GET",
                    async: false,
                    url: url,
                    dataType: "html",
                    success: function (data) {
                        var render = template.compile(data);
                        template.helper("getCtx", function (ctxStr) {
                            return ctx;
                        });
                        //人行时间格式化
                        template.helper('dateFormat', function (data) {
                            var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
                            var formatedDate = data.replace(pattern, '$1-$2-$3 $4:$5:$6');
                            return formatedDate;
                        });
                        var html = render(jsonData);
                        $(type).html("").html(html);
                    }
                });
            } else {
                //这里是默写获取pdf报告地址
                var url = 'https://tenant.51datakey.com/carrier/report_data?data=';
                var data = jsonData.message;
                url += data;
                $('#operatorIframe').attr('src', url);
                $('#operatorIframe').attr('height', $(window).height() - 50);

                // $('#operator').html('').html('<iframe src="'+url+'" width="100%" height="100%"></iframe>');

            }
        }
    }


    // function setIframeHeight(iframe) {
    //     if (iframe) {
    //         var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
    //         if (iframeWin.document) {
    //             iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
    //         }
    //     }
    // };

    // function iFrameHeight() {
    //     var ifm= document.getElementById("operatorIframe");
    //     var subWeb = document.frames ? document.frames["operatorIframe"].document : ifm.contentDocument;
    //     if(ifm != null && subWeb != null) {
    //         ifm.style.height = 'auto';//关键这一句，先取消掉之前iframe设置的高度
    //         ifm.style.height = subWeb.body.scrollHeight+'px';
    //     }
    // };

</script>
</html>