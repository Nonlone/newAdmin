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
	<div style="background-color:#FFFFFF;width:1500px;position:relative;left: 10%">

		<div>
			<h3 style="background-color:#ADADAD" ><span style="font-size:20px">订单信息</span></h3>
			<hr/>
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
						客户姓名
					</td>
					<td width="70px" height="30px">
						${idcard.name}
					</td>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						客户ID
					</td>
					<td width="70px" height="30px">
						<c:if test="${not empty user}">
							${user.id}
						</c:if>
					</td>
				</tr>
				<tr>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						注册手机号
					</td>
					<td width="70px" height="30px">
						${hyPhone}
					</td>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						授信状态
					</td>
					<td width="70px" height="30px">
						<c:if test='${cardStatus eq "VALVE_REJECT"}'>
							阀门拒绝
						</c:if>
						<c:if test='${cardStatus eq "AUTHORIZED_REJECT"}'>
							授信拒绝
						</c:if>
						<c:if test='${cardStatus eq "CUSTOMER_REJECT"}'>
							主体拒绝
						</c:if>
						<c:if test='${cardStatus eq "WITHDRAW_REJECT"}'>
							提现拒绝
						</c:if>
						<c:if test='${cardStatus eq "QUOTACHANGE_REJECT"}'>
							提额拒绝
						</c:if>
						<c:if test='${cardStatus eq "RENEWAL_REJECT"}'>
							续卡拒绝
						</c:if>
						<c:if test='${cardStatus eq "RENEWAL_APPROVING"}'>
							续卡审核中
						</c:if>
						<c:if test='${cardStatus eq "QUOTACHAGNE_APPROVING"}'>
							提额审核中
						</c:if>
						<c:if test='${cardStatus eq "WITHDRAW_APPROVING"}'>
							提现审核中
						</c:if>
						<c:if test='${cardStatus eq "OPENCARD_APPROVING"}'>
							授信审核中
						</c:if>
						<c:if test='${cardStatus eq "FILLING"}'>
							资料填写中
						</c:if>
						<c:if test='${cardStatus eq "LOANING"}'>
							放款中
						</c:if>
						<c:if test='${cardStatus eq "UNACTIVE"}'>
							未激活
						</c:if>
						<c:if test='${cardStatus eq "NORMAL"}'>
							授信（未提现）
						</c:if>
						<c:if test='${cardStatus eq "DEBT"}'>
							授信（已提现）
						</c:if>
						<c:if test='${cardStatus eq "OVERDUE"}'>
							逾期
						</c:if>
						<c:if test='${cardStatus eq "ABANDONED"}'>
							卡废弃
						</c:if>
					</td>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						授信额度
					</td>
					<td width="70px" height="30px">
						${card.creditSum}
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
						<c:if test="${card.applyChannelId ne 'null'}">
							${card.applyChannelId}
						</c:if>
					</td>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						注册渠道
					</td>
					<td width="70px" height="30px">
						<c:if test="${card.registChannelId ne 'null'}">
							${card.registChannelId}
						</c:if>
					</td>
				</tr>
				<tr>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						产品名称
					</td>
					<td width="70px" height="30px">
						${card.product.name}
					</td>

					<td bgcolor="#F2F2F2" width="30px" height="30px">
						订单创建时间
					</td>
					<td width="70px" height="30px">
						${cardStartTm}
					</td>

					<td bgcolor="#F2F2F2" width="30px" height="30px">
						提交审批时间
					</td>
					<td width="70px" height="30px">
						${submitTime}
					</td>

				</tr>
				<tr>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						客户端类型：
					</td>
					<td width="70px" height="30px">
						${user.osType}
					</td>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						是否新客户
					</td>
					<td width="70px" height="30px">
						是
					</td>
					<td bgcolor="#F2F2F2" width="30px" height="30px">
						拒绝原因
					</td>
					<td width="70px" height="30px">
						<c:choose>
							<c:when test="${card.rejectReason eq 'valve_age'}">
								年龄拒绝
							</c:when>
							<c:when test="${card.rejectReason eq 'valve_area'}">
								地区拒绝
							</c:when>
							<c:when test="${card.rejectReason eq 'valve_contact_size'}">
								通讯录条目数拒绝
							</c:when>
							<c:when test="${card.rejectReason eq 'idcard_auth'}">
								实名拒绝
							</c:when>
							<c:when test="${card.rejectReason eq 'living'}">
								活体拒绝
							</c:when>
							<c:when test="${card.rejectReason eq 'DummyService'}">
								自动拒绝
							</c:when>
							<c:when test="${card.rejectReason eq 'value_not_exist'}">
								数据不存在拒绝
							</c:when>
							<c:otherwise>
								${card.rejectReason}
							</c:otherwise>
						</c:choose>

					</td>

				</tr>

				</tbody>
			</table>
			<hr/>
		</div>

		<ul class="nav-tabs">
			<li id="li_base" class="active"><a href="javascript:void(0)" onclick="loadAll('base')">基本资料</a></li>
			<li id="li_credit"><a href="javascript:void(0)" onclick="loadAll('credit')">征信报告</a></li>
			<li id="li_operator"><a href="javascript:void(0)" onclick="loadAll('operator')">运营商报告</a></li>
			<li id="li_bond"><a href="javascript:void(0)" onclick="loadAll('bond')">贷后邦报告</a></li>
		</ul>

		<!-- 征信报告 -->
		<div id="credit" hidden="true">

			<script type="text/html" src="${ctx}/static/artTemplate/template/creditTemplate.tpl"></script>

		</div>

		<!-- 运营商报告 -->
		<div id="operator" hidden="true">
			<iframe name="operatorIframe" id="operatorIframe" width="100%" height='100%' scrolling="yes" frameborder="no" border="0" ></iframe>
		</div>

		<!-- 贷后邦报告 -->
		<div id="bond" hidden="true">

			<script type="text/html" src="${ctx}/static/artTemplate/template/templateTest.tpl"></script>

		</div>


		<!-- 基本资料 -->
		<div id="baseData">
			<iframe frameborder="0" scrolling="auto" src="${ctx}/backend/customer/detail/${idcard.id}" style="width: 1500px;min-height: 1320px"></iframe>
		</div>


	</div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        //放大图片
        $('img.dialog').on('click',function () {
            var large_image = '<img class=\'closeImg\' src= ' + $(this).attr("src") + '></img>';
            BUI.use('bui/overlay',function(Overlay){
                var width = this.width;
                var height = this.height;
                var dialog = new Overlay.Dialog({
                    title:'图片放大',
                    width:width,
                    height:height,
                    mask:false,
                    buttons:[],
                    bodyContent:large_image
                });
                dialog.show();
            });
        });

    });

    var ctxStr = "${ctx}";
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
        baseData.setAttribute("hidden","true");
        credit.setAttribute("hidden","true");
        operator.setAttribute("hidden","true");
        bond.setAttribute("hidden","true");
    }

    function loadAll(type) {
        removeClassAddHidden();
        if(type=='base'){//基本资料
            baseData.removeAttribute("hidden");
            li_base.setAttribute("class","active");
        }else{
            if(type=='credit'){//征信报告
                credit.removeAttribute("hidden");
                li_credit.setAttribute("class","active");
                creditHtml();
            }
            if(type=='operator'){//运营商报告
                operator.removeAttribute("hidden");
                li_operator.setAttribute("class","active");
                operatorHtml();
            }
            if(type=='bond'){//贷后邦报告
                bond.removeAttribute("hidden");
                li_bond.setAttribute("class","active");
                bondHtml();
            }
        }

    }

    /***
     * 征信入口
     */
    function creditHtml(){
        //容器id
        var type="#credit";
        //json
        var data = getData(type);
        //模板url
        var url = "${ctx}/static/artTemplate/template/creditTemplate.tpl";
        produceHtml(url,data,type);
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
        produceHtml(url,data,type);
    }

    /***
     * 贷后邦入口
     */
    function bondHtml(){
        //容器id
        var type = "#bond";
        //模板url
        var url = "${ctx}/static/artTemplate/template/templateTest.tpl";
        //json
        var data = getData(type);
        produceHtml(url,data,type);
    }

    /***
     * 通用获取数据的方法
     */
    function getData(type){
        var datajson = "";
        var source = "";
        if(type=="#bond"){
            source = "bond";
        }else if(type=="#operator"){
            source = "operator";
        }else if(type=="#credit"){
            source = "credit";
        }
        $.ajax({
            type:"post",
            url:"${ctx}/backend/data/acquire/getData",
            async:false,
            data:{cardId:"${card.id}",type:source},
            dataType: "json",
            success:function(data){
                datajson =data;
            }
        });
        return datajson;
    }

    /***
     * 通用生产HTML方法
     */
    function produceHtml(url,jsonData,type) {
        if(jsonData=="nulldata"||jsonData==""||jsonData==null){
            BUI.use('bui/overlay',function(Overlay){
                var dialog = new Overlay.Dialog({
                    title:'提示窗口',
                    width:500,
                    height:300,
                    mask:false,
                    buttons:[],
                    bodyContent:'<p>没有该用户的此类征信信息</p>'
                });
                dialog.show();
            });
        }else{
            if (type!="#operator"){
                $.ajax({
                    type: "GET",
                    async:false,
                    url: url,
                    dataType: "html",
                    success: function(data){
                        var render = template.compile(data);
                        template.helper("getCtx",function (ctxStr) {
                            return ctx;
                        });
                        //人行时间格式化
                        template.helper('dateFormat',function (data) {
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
                $('#operatorIframe').attr('height', $(window).height()-50);

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