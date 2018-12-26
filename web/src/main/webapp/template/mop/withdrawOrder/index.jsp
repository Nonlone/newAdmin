<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<html>
<head>
	<title><spring:eval expression="@webConf['admin.title']" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/import-static.jsp"%>
</head>
<body>
	<div class="container">

	    <!-- 大图 -->
	    <div id="outerdiv" style="position:fixed;top:0;left:0;background:rgba(0,0,0,0.7);z-index:99999;width:100%;height:100%;display:none;">
            <div id="innerdiv" style="position:absolute;">
                <img id="bigimg" style="border:5px solid #fff;" src="" />
            </div>
        </div>

		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
		<div class="row">
			<div class="control-group span7">
				<label class="control-label" style="width: 94px">用户ID:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="userId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">提现单号:</label>
				<div class="controls">
					<input type="text" class="input-normal control-text" name="orderId">
				</div>
			</div>
			<div class="control-group span7">
				<label class="control-label">提现状态:</label>
				<div class="controls">
					<select name="status">
						<option value="" selected="selected">全部</option>
						<option value="10">处理中</option>
						<option value="20">提现成功</option>
						<option value="21">提现失败</option>
					</select>
				</div>
			</div>
			<div class="control-group span10">
				<label class="control-label">申请提现时间:</label>
				<div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
					<input type="text" class="calendar" name="startTime" data-tip="{text : '开始日期'}">
					<span>- </span>
					<input name="endTime" type="text" class="calendar" data-tip="{text : '结束日期'}">
				</div>
			</div>
			<div class="span1 offset1">
			    <button  type="button" id="btnSearch" class="button button-primary">查询</button>
			</div>
			<div class="span1 offset1">
                <button  type="button" id="btnClear" class="button button-primary">清空</button>
            </div>
            <div class="span1 offset1">
                <button  type="button" id="btnOutput" class="button button-primary">导出</button>
            </div>
		</div>
		</form>

        <!-- 详情 -->
		<div id="orderDetailInfoDiv" class="hide" >
            <div class="span6" id="charge_title"></div>
            <form class="form-horizontal well">
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">用户ID：</label>
                        <div class="controls">
                            <input type="text" id="_userId" class="control-text">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">收款人姓名：</label>
                        <div class="controls">
                          <input type="text" id="_name" class="control-text">
                        </div>
                    </div>
                </div>
                <div class="row">
                  <div class="control-group span8">
                      <label class="control-label">银行卡号：</label>
                      <div class="controls">
                        <input type="text" id="_bankCardNo" class="control-text">
                      </div>
                  </div>
                  <div class="control-group span8">
                    <label class="control-label">身份证号：</label>
                    <div class="controls">
                      <input type="text" id="_idCardNo" class="control-text">
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control-group span8">
                    <label class="control-label">开户银行：</label>
                    <div class="controls">
                      <input type="text" id="_bankName" class="control-text">
                    </div>
                  </div>
                  <div class="control-group span8">
                      <label class="control-label">分支行：</label>
                      <div class="controls">
                          <input type="text" id="_bankFullName" class="control-text">
                      </div>
                  </div>
                </div>
                <img width="300" class="pimg" height="150" id="_front" />
                <img width="300" class="pimg" height="150" id="_reverse" />
            </form>
		</div>

        <input type="hidden" id="audit_status" value="">
        <input type="hidden" id="audit_orderId" value="">
        <input type="hidden" id="audit_userId" value="">
        <input type="hidden" id="audit_type" value="">

        <div id="confirmContent" class="hide">
            <form id="searchInfoForm" class="form-inline">
                <p id="confirmTitle"></p>
                <div class="row">
                  <div class="control-group span15">
                    <label class="control-label">备注：</label>
                    <div class="controls control-row4">
                      <textarea id="audit_remark" class="input-large" type="text"></textarea>
                    </div>
                  </div>
                </div>
            </form>
        </div>

		<div class="search-grid-container">
			<div id="grid"></div>
		</div>
	</div>

<script type="text/javascript">

BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/overlay','bui/common/page'],function (CrudGrid,Form,Page,Overlay) {

    $(function(){
        $(".pimg").click(function(){
            var _this = $(this);//将当前的pimg元素作为_this传入函数
            imgShow("#outerdiv", "#innerdiv", "#bigimg", _this);
        });
    });

    function imgShow(outerdiv, innerdiv, bigimg, _this){
        var src = _this.attr("src");//获取当前点击的pimg元素中的src属性
        $(bigimg).attr("src", src);//设置#bigimg元素的src属性
        /*获取当前点击图片的真实大小，并显示弹出层及大图*/
        $("<img/>").attr("src", src).load(function(){
            var windowW = $(window).width();//获取当前窗口宽度
            var windowH = $(window).height();//获取当前窗口高度
            var realWidth = this.width;//获取图片真实宽度
            var realHeight = this.height;//获取图片真实高度
            var imgWidth, imgHeight;
            var scale = 0.8;//缩放尺寸，当图片真实宽度和高度大于窗口宽度和高度时进行缩放
            if(realHeight>windowH*scale) {//判断图片高度
                imgHeight = windowH*scale;//如大于窗口高度，图片高度进行缩放
                imgWidth = imgHeight/realHeight*realWidth;//等比例缩放宽度
                if(imgWidth>windowW*scale) {//如宽度扔大于窗口宽度
                    imgWidth = windowW*scale;//再对宽度进行缩放
                }
            } else if(realWidth>windowW*scale) {//如图片高度合适，判断图片宽度
                imgWidth = windowW*scale;//如大于窗口宽度，图片宽度进行缩放
                imgHeight = imgWidth/realWidth*realHeight;//等比例缩放高度
            } else {//如果图片真实高度和宽度都符合要求，高宽不变
                imgWidth = realWidth;
                imgHeight = realHeight;
            }

            $(bigimg).css("width",imgWidth);//以最终的宽度对图片缩放
            var w = (windowW-imgWidth)/2;//计算图片与窗口左边距
            var h = (windowH-imgHeight)/2;//计算图片与窗口上边距
            $(innerdiv).css({"top":h, "left":w});//设置#innerdiv的top和left属性
            $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg

        });

        $(outerdiv).click(function(){//再次点击淡出消失弹出层
            $(this).fadeOut("fast");
        });

    }
	
	//定义页面权限
	var add=false,update=false,list=false,del=false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/mop/partner/withdrawOrder"/>

    //清空
    $("#btnClear").click(function(){
        $(':input','#searchForm')
               .not(':button,:submit,:reset,:hidden')
               .val('')
               .removeAttr('checked')
               .removeAttr('checked')
    });


    function downloadData(){
        var url = "${ctx}/mop/partner/withdrawOrder/output"
        var form=$("#searchForm");
        var oldUrl = form.attr("action");
        form.attr("action",url);
        form.submit();
        form.attr("action",oldUrl);
        downDialog.show();
    }

    //下载
    $("#btnOutput").click(function(){
        var data = {};
        $(':input','#searchForm').not(':button,:submit,:reset,:hidden').each(function (i) {
            data[$(this).attr("name")] = $(this).val();
        })
        $.ajax({
            url:'${ctx}/mop/partner/withdrawOrder/output/count',
            async:false,
            type : "POST",
            data : data,
            success:function (data) {
                if (data.success) {
                    if (data.msg == 'yes') {
                         if (confirm('搜索数据超过5000条，确定要导出？')){
                            downloadData();
                         }
                    } else {
                        downloadData();
                    }
                } else {
                    showWarning(data.msg);
                }
            },
            failure:function () {
                showWarning('系统异常，请稍后操作');
            }
        });
        setTimeout(function(){ startGetDownloadInfo() }, 2000);
    });

    var timer;
    //轮训下载进度
    function startGetDownloadInfo() {
        timer = setInterval(function() {
            $.ajax({
                url:'${ctx}/mop/partner/withdrawOrder/output/info',
                type : "get",
                success:function (data) {
                    if (data) {
                        $("#dInfo").text(data.downloaded + '  /  ' + data.total);
                    } else {
                        stopGetDownloadInfo();
                    }
                },
                failure:function () {
                    stopGetDownloadInfo();
                }
            });
        },1000);
    }

    function stopGetDownloadInfo() {
        console.log("close Interval");
        clearInterval(timer);
        $("#dInfo").text("");
        downDialog.close();
    }

    var downDialog = new Overlay.Dialog({
                title:'等待下载',
                width:350,
                height:250,
                bodyContent:'<img src="${ctx}/loading.gif"  width="300" height="150" /> <br/> <span id="dInfo"> </span>',
                success:function () {
                  stopGetDownloadInfo();
                  this.close();
                },
                cancel:function() {
                    stopGetDownloadInfo();
                    return true;
                }
    });

    var columns = [
		 {title:'申请提现时间',dataIndex:'applyTime',width:'12%'},
		 {title:'提现单号',dataIndex:'id',width:'12%'},
         {title:'用户ID',dataIndex:'partnerUserId',width:'12%'},
		 {title:'提现金额/元',dataIndex:'amount',width:'10%',renderer:function(value, obj){
            return obj.amount.toFixed(2);
         }},
		 {title:'税后金额/元',dataIndex:'afterTaxAmount',width:'8%',renderer:function(value, obj){
		    return obj.afterTaxAmount.toFixed(2);
		 }},
		 {title:'状态',dataIndex:'status',width:'6%',renderer:function(value, obj){
		    var description = value;
            if (1 == value) {
                description = '新建';
            } else if (10 == value) {
                description = '处理中';
            } else if (20 == value) {
                description = '提现成功';
            } else if (21 == value) {
               description =  '提现失败';
            }
            return description;
         }},
		 {title:'操作',dataIndex:'op',width:'20%', renderer:function(value, obj){
		    var str = '<span class="grid-command" id="order_detail" > 查看信息 </span>';
		    if (obj.status == 10) {
		        str += '<span class="grid-command" id="audit_success" >通过放款 </span> <span class="grid-command" id="audit_fail">拒绝放款</span>';
		    }
		    if (obj.status == 20 || obj.status == 21) {
		        str += '<span class="grid-command" id="audit_modify">修改状态</span>';
		    }
		    return  str;
		 }},
        ];
	var crudGrid = new CrudGrid({
		entityName : '合伙人提现信息',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/mop/partner/withdrawOrder/list',
        columns : columns,
		showAddBtn : false,
		showUpdateBtn : false,
		showRemoveBtn : false,
		dialogContentId : 'orderDetailInfoId',
		gridCfg:{
    		innerBorder:true
    	}});

	//点击获取列，获取主键id
    var grid = crudGrid.get('grid');
    grid.on('cellclick',function (ev) {
        console.info(ev);
        var sender = $(ev.domTarget); //点击的Dom
        var target = ev.record;
        var clickDom = sender.attr('id');
		//记录选中的id
        $("#selectId").val(target.id);
        if (clickDom == 'order_detail') {
            dialog.show();
            var data = {};
            data.orderId = target.id;
            $.ajax({
                url:'${ctx}/mop/partner/withdrawOrder/detail',
                async:false,
                type : "POST",
                data : data,
                success:function (data) {
                    console.info(data);
                    $("#_userId").val(data.partnerUserId);
                    $("#_name").val(data.name);
                    $("#_idCardNo").val(data.idCardNo);
                    $("#_bankCardNo").val(data.bankCardNo);
                    $("#_bankFullName").val(data.bankFullName);
                    $("#_bankName").val(data.bankName);
                    $("#_front").attr('src',data.picFront);
                    $("#_reverse").attr('src',data.picReverse);
                },
                failure:function () {
                    showWarning('系统异常，请稍后操作');
                }
            });
        } else if (clickDom == 'audit_success') {
            $("#confirmTitle").text("确定通过放款?");
            $("#audit_status").val(20);
            $("#audit_orderId").val(target.id);
            $("#audit_userId").val(target.partnerUserId);
            $("#audit_remark").val("");
            $("#audit_type").val("audit");
            confirmDialog.show();
        } else if (clickDom == 'audit_fail') {
            $("#confirmTitle").text("确定拒绝放款?");
            $("#audit_status").val(21);
            $("#audit_orderId").val(target.id);
            $("#audit_userId").val(target.partnerUserId);
            $("#audit_remark").val("");
            $("#audit_type").val("audit");
            confirmDialog.show();
        } else if (clickDom == 'audit_modify') {
            var title = '确定要把放款状态从<font color="#FF0000">成功</font>改为<font color="#FF0000">失败</font>？'
            $("#audit_status").val(21);
            if (target.status == 21) {
                title = '确定要把放款状态从<font color="#FF0000">失败</font>改为<font color="#FF0000">成功</font>？';
                $("#audit_status").val(20);
            }
            $("#confirmTitle").html(title);
            $("#audit_orderId").val(target.id);
            $("#audit_userId").val(target.partnerUserId);
            $("#audit_remark").val("");
            $("#audit_type").val("modify");
            confirmDialog.show();
        }
		return true;
    });

	/* 显示提示框 */
	function showWarning(str) {
        BUI.Message.Alert(str,'warning');
    }
    function showSuccess(str) {
        BUI.Message.Alert(str,'success');
    }

    var dialog = new Overlay.Dialog({
                    title:'放款信息',
                    width:700,
                    height:450,
                    contentId:'orderDetailInfoDiv',
                    success:function () {
                      this.close();
                    }
    });

    var confirmDialog = new Overlay.Dialog({
                    title:'操作确认',
                    width:500,
                    height:230,
                    contentId:'confirmContent',
                    success:function () {
                      var data = {};
                      data.status = $("#audit_status").val();
                      data.userId = $("#audit_userId").val();
                      data.orderId = $("#audit_orderId").val();
                      data.remark = $("#audit_remark").val();
                      data.type = $("#audit_type").val();
                      console.log(data);
                      $.ajax({
                          url:'${ctx}/mop/partner/withdrawOrder/status/update',
                          async:false,
                          type : "POST",
                          data : data,
                          success:function (data) {
                            if (data.success){
                                showSuccess(data.msg);
                                crudGrid.load(true);
                                confirmDialog.close();
                            }else {
                                showWarning(data.msg);
                            }
                          },
                          failure:function () {
                              showWarning('系统异常，请稍后操作');
                          }
                      });
                    }
    });
});
</script>
 
</body>
</html>


