<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../../common/import-tags.jsp"%>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../../common/import-static.jsp"%>
</head>
<body>
<div style="background-color:#FFFFFF;">
<div style="background-color:#FFFFFF;width:1300px;position:relative;left: 10%" >
    <!--还款详情页 ================================================== -->
    <div>
        <h3 style="background-color:#ADADAD" ><span style="font-size:20px">补件基本信息</span></h3>
            <hr/>
            <table cellspacing="0" class="table table-bordered">
                <thead>
                <tr style="background-color: #F2F2F2">
                    <th width="150px">客户姓名</th>
                    <th width="155px">注册手机号</th>
                    <th width="100px">申请提现金额</th>
                    <th width="190px">客户提交补件时间</th>
                    <th width="80px">资金方</th>
                    <th width="150px">产品名称</th>
                    <th width="150px">客户ID</th>
                    <th width="60px">允许再补件次数</th>
                    <th width="60px">已发送总部次数</th>
                </tr>
                </thead>
                <tr>
                    <td><c:if test="${not empty supplyLog.idCardData}">${supplyLog.idCardData.name}</c:if></td>
                    <td><c:if test="${not empty supplyLog.user}">${supplyLog.user.phone}</c:if></td>
                    <td><c:if test="${not empty supplyLog.loanOrder}">${supplyLog.loanOrder.applyAmount}</c:if></td>
                    <td>${createdTime}</td>
                    <td><c:if test="${not empty fundName}">${fundName}</c:if></td>
                    <td><c:if test="${not empty supplyLog.loanOrder}"><c:if test="${not empty supplyLog.loanOrder.product}">${supplyLog.loanOrder.product.name}</c:if></c:if></td>
                    <td><c:if test="${not empty supplyLog.user}">${supplyLog.user.id}</c:if></td>
                    <td>${can2dashu}</td>
                    <td>${supply2dashu}</td>
                </tr>
            </table>
        <hr/>
        <div>
            <ul id="tabHeader" class="nav-tabs">
                <li id="li_detail" class="active"><a href="javascript:void(0)" onclick="load(this,'detail')">补件信息</a></li>
                <li id="li_log"><a href="javascript:void(0)" onclick="load(this,'log')">补件记录</a></li>
            </ul>
            <div id="tabContext" style="margin-bottom: 10px;">
                <div id="detail">
                    <h3 style="background-color:#ADADAD" ><span style="font-size:20px">补件信息</span></h3>
                    <hr/>
                    <c:forEach items="${history}" var="his" varStatus="">

                    <div style="font-size: 15px">客户提交补件时间：${his.createdTime}<button  name="sendDashu"  style="width: 60px;height: 30px;float: right" hidden="hidden" onclick="supply2dashu('${his.id}');"><span style="color: #ac2925;size: 30px">发送总部</span></button></div>
                    <br/>
                    <table cellspacing="0" class="table table-bordered">
                        <thead>
                            <tr style="background-color: #F2F2F2">
                                <th width="40px">补件类型</th>
                                <th width="50px">补件要素</th>
                                <th width="520px">补件内容</th>
                            </tr>
                        </thead>
                        <c:forEach items="${his.info}" var="info" varStatus="">
                            <tr>
                                <td><c:if test="${info.supplyType eq 1}">影像件</c:if><c:if test="${info.supplyType eq 2}">文本</c:if></td>
                                <td>${info.supplyName}</td>
                                <td>
                                    <c:if test="${info.ifPlural eq 0}">
                                        <c:if test="${info.supplyType eq 1}">
                                            <div style="float: left;margin: auto 20px;">
                                                <div>
                                                    <img class="dialog" style="max-height: 70px;max-width: 100px;"
                                                         src="${info.supplyInfo}">
                                                </div>
                                            </div>
                                        </c:if>
                                        <c:if test="${info.supplyType eq 2}">
                                            ${info.supplyInfo}
                                        </c:if>
                                    </c:if>
                                    <c:if test="${info.ifPlural eq 1}">
                                        <c:forEach items="${his.info}">
                                            <c:if test="${info.supplyType eq 1}">
                                                <c:forEach items="${info.supplyInfos}" var="supImg">
                                                    <div style="float: left;margin: auto 20px;margin-top: 20px">
                                                        <div>
                                                            <img class="dialog" style="max-height: 70px;max-width: 100px;"
                                                                 src="${supImg}">
                                                        </div>
                                                    </div>
                                                </c:forEach>

                                            </c:if>
                                            <c:if test="${info.supplyType eq 2}">
                                                <c:forEach items="${info.supplyInfos}" var="supText">
                                                    ${supText}
                                                </c:forEach>
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <hr/>
                </c:forEach>
                </div>
                <div id="log" style="display:none;">
                    <h3 style="background-color:#ADADAD" ><span style="font-size:20px">补件记录</span></h3>
                    <hr/>
                    <c:forEach items="${sendHistoryList}" var="his" varStatus="">

                        <div style="font-size: 15px">提交总部时间：${his.sendTime}</div>
                        <br/>
                        <table cellspacing="0" class="table table-bordered">
                            <thead>
                            <tr style="background-color: #F2F2F2">
                                <th width="40px">补件类型</th>
                                <th width="50px">补件要素</th>
                                <th width="520px">补件内容</th>
                            </tr>
                            </thead>
                            <c:forEach items="${his.info}" var="info" varStatus="">
                                <tr>
                                    <td><c:if test="${info.supplyType eq 1}">影像件</c:if><c:if test="${info.supplyType eq 2}">文本</c:if></td>
                                    <td>${info.supplyName}</td>
                                    <td>
                                        <c:if test="${info.ifPlural eq 0}">
                                            <c:if test="${info.supplyType eq 1}">
                                                <div style="float: left;margin: auto 20px;">
                                                    <div>
                                                        <img class="dialog" style="max-height: 70px;max-width: 100px;"
                                                             src="${info.supplyInfo}">
                                                    </div>
                                                </div>
                                            </c:if>
                                            <c:if test="${info.supplyType eq 2}">
                                                ${info.supplyInfo}
                                            </c:if>
                                        </c:if>
                                        <c:if test="${info.ifPlural eq 1}">
                                            <c:forEach items="${his.info}">
                                                <c:if test="${info.supplyType eq 1}">
                                                    <c:forEach items="${info.supplyInfos}" var="supImg">
                                                        <div style="float: left;margin: auto 20px; margin-top: 20px">
                                                            <div>
                                                                <img class="dialog" style="max-height: 70px;max-width: 100px;"
                                                                     src="${supImg}">
                                                            </div>
                                                        </div>
                                                    </c:forEach>

                                                </c:if>
                                                <c:if test="${info.supplyType eq 2}">
                                                    <c:forEach items="${info.supplyInfos}" var="supText">
                                                        ${supText}
                                                    </c:forEach>
                                                </c:if>
                                            </c:forEach>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <hr/>
                    </c:forEach>
                </div>
            </div>
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
    }

    <shiro:hasPermission name="/backend/supply/log:supply">
    debugger;
        var elementsByName = document.getElementsByName("sendDashu");
        for(var i = 0;i<elementsByName.length;i++){
            elementsByName[i].removeAttribute("hidden");
        }
    </shiro:hasPermission>

    function supply2dashu(id) {
        BUI.use('bui/overlay',function (Overlay){
            BUI.Message.Confirm('确认发送补件影像信息到总部吗？',function(){
                $.ajax({
                    url:'/backend/supply/log/supplyLog2Dashu/'+id,
                    dataType:'JSON',
                    headers: {'Content-type':'application/json'},
                    type:'POST',
                    async:true,
                    //contentType: 'application/json;charset=utf-8',
                    success:function(result){
                        if(result.code==0){
                            BUI.Message.Alert(result.message,function(){
                                window.location.reload();
                            },'success');
                        }else if(result.code==3){
                            BUI.Message.Alert(result.message,function(){
                            },'error');
                        }else {
                            BUI.Message.Alert(result.message,function(){
                            },'error');
                        }
                    }});

            },'question');
        });

    }

    $(function () {
        BUI.use('bui/overlay', function (Overlay) {
        //放大图片
        $('img.dialog').on('click', function () {
            var large_image = '<img class=\'closeImg\' style=\'max-height: 780px;max-width: 800px\' src= ' + $(this).attr("src") + '></img>';

                debugger;
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