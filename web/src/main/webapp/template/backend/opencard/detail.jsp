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
                        ${card.creditSum}
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
                        <div style="display: none" id="blackBox"> <div style="width: 450px;display:block;word-break: break-all;word-wrap: break-word;">${tongDunData.blackBox}</div></div>
                         <button  id="btnShow" class="button button-primary">显示</button>                         
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
                    <th width="150px">步骤</th>
                    <th width="150px">省份名</th>
                    <th width="150px">城市名</th>
                    <th width="150px">地区名</th>
                    <th width="150px">地址</th>
                    <th width="150px">ip</th>
                </thead>
                <tbody>
                <c:choose>
                <c:when test="${not empty areaList}">
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
            <li id="li_base" data-callback="" class="active"><a href="javascript:void(0)" onclick="load(this,'baseData')">基本资料</a></li>
            <li id="li_credit" data-callback="creditHtml" ><a href="javascript:void(0)" onclick="load(this,'credit')">征信报告</a></li>
            <li id="li_operator" data-callback="operatorHtml"><a href="javascript:void(0)" onclick="load(this,'operator')">运营商报告</a></li>
            <li id="li_bond" data-callback="bondHtml"><a href="javascript:void(0)" onclick="load(this,'bond')">贷后邦报告</a></li>
        </ul>
       </c:if>
        <div id="tabContext" style="margin-bottom: 10px;">
             <!-- 基本资料 -->
            <div  id="baseData" >
                <iframe  frameborder="no"  border="0"  src="${ctx}/backend/customer/detail/${user.id}" style="width: 1517px;min-height: 1000px;overflow-x: hidden;overflow-y: auto"></iframe>
            </div>
            
            <!-- 征信报告 -->
            <div id="credit" style="display:none;" >

                <script type="text/html" src="${ctx}/static/artTemplate/template/creditTemplate.tpl"></script>

            </div>

            <!-- 运营商报告 -->
            <div id="operator"  style="display:none;" >
                <iframe name="operatorIframe" id="operatorIframe" width="100%" height='100%' scrolling="yes" frameborder="no"
                        border="0"></iframe>
            </div>

            <!-- 贷后邦报告 -->
            <div id="bond"  style="display:none;"  >

                <script type="text/html" src="${ctx}/static/artTemplate/template/templateTest.tpl"></script>

            </div>

            
        </div>
    </div>



</div>

</body>
<script type="text/javascript">

    

      function load(ele,obj) {
    	// 样式控制
    	 var $ele = $(ele);
    	 $ele.parent().addClass("active").siblings().removeClass("active");
	     var $content = $("#"+obj);
	     $content.show().siblings().hide();
	   
	 	// 调用对应渲染方法
	   	var callbackFunName=$ele.parent().attr("data-callback");
	   	if(callbackFunName==null || callbackFunName=="")
	   		return;	
	    var callbackFun=eval(callbackFunName);
	   	new callbackFun();
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
    BUI.use('bui/overlay',function(Overlay){
        var dialog = new Overlay.Dialog({
          title:'同盾设备指纹',
          width:500,
          height:300,
          mask:false,
          buttons:[],
          bodyContent:$("#blackBox").html()//'<div style="width:500px;word-wrap:break-word;">'+$("#btnShow").attr("blackBox")+'</div>'//'<p></p>'
        });

      $('#btnShow').on('click',function () {
        dialog.show();
      });
    });

</script>
</html>