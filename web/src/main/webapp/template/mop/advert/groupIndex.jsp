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
		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
		    <div class="row">
                <div class="control-group span7">
                    <label class="control-label">状态:</label>
                    <div class="controls">
                        <select name="status">
                            <option value="" selected="selected">全部</option>
                            <option value="1">未启用</option>
                            <option value="2">使用中</option>
                            <option value="3">停用中</option>
                        </select>
                    </div>
                </div>
                <div class="control-group span10">
                    <label class="control-label">时间:</label>
                    <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                        <input type="text" class="calendar" name="startTime" data-tip="{text : '开始日期'}" >
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
            </div>
		</form>

		<!-- 修改新增 -->
		<div id="addOrUpdate" class="hide">
            <form class="form-inline" id="addOrUpdateForm">
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">模组编码：</label>
                        <div class="controls">
                            <input type="text" name="code" class="control-text" data-rules="{required : true}">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">模组名称：</label>
                        <div class="controls">
                          <input type="text" name="title" class="control-text" data-rules="{required : true}">
                        </div>
                    </div>
                </div>
                <div class="row">
                  <div class="control-group span16">
                      <label class="control-label">时间：</label>
                      <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                          <input type="text" name="beginTime" class="calendar calendar-time" data-tip="{text : '开始日期'}" >
                          <span>- </span>
                          <input type="text" name="endTime"  class="calendar calendar-time" data-tip="{text : '结束日期'}">
                      </div>
                  </div>
                </div>
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">展示限制数量:</label>
                        <div class="controls">
                            <input type="text" name="showLimit" class="control-text" data-rules="{number:true}">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">播放时间(秒):</label>
                        <div class="controls">
                            <input type="text" name="playTime" class="control-text" data-rules="{number:true}">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control-group">
                        <label class="control-label">备注:</label>
                        <div class="controls" style="margin-left: 20px;">
                            <textarea type="text" name="remark" class="control-text" style="height: 50px;width: 300px;"></textarea>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="id" value="">
            </form>
		</div>

		<div class="search-grid-container">
			<div id="grid"></div>
		</div>


		<div class="demo-content">
            <div class="row">
              <div class="span16">
                <div id="grid">

                </div>
              </div>
            </div>
	</div>

        <div id="confirmContent" class="hide">
            <!--  <form id="searchInfoForm" class="form-inline"> -->

            <!--    </form> -->
        </div>

<script type="text/javascript">

BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/overlay','bui/common/page'],function (CrudGrid,Form,ChargeDialog,OverLay,Page) {

	//定义页面权限
	var add=false,update=false,list=false, del = false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/mop/advert/block"/>

    //清空
    $("#btnClear").click(function(){
        $(':input','#searchForm')
               .not(':button,:submit,:reset,:hidden')
               .val('')
               .removeAttr('checked')
               .removeAttr('checked')
    });

    var columns = [
    		 {title:'模组编码',dataIndex:'code',width:'10%'},
    		 {title:'模组名称',dataIndex:'title',width:'10%'},
             {title:'展示限制',dataIndex:'showLimit',width:'5%',renderer:function(value, obj){
                 if (0 >= value) {
                     return '不限制';
                 }
                 return value;
             }},
             {title:'关联数量',dataIndex:'itemCount',width:'5%'},
             {title:'展示时间',dataIndex:'beginTime',width:'20%',renderer:function(value, obj){
                 var html = '';

                 if ( (null == value || '' == value)
                     && (null == obj.endTime || '' == obj.endTime) ) {
                     return "不限制";
                 }

                 if (null == value || '' == value) {
                     html += "~~";
                 }
                 else {
                     html += value;
                 }

                 html += "至";

                 if (null == obj.endTime || '' == obj.endTime) {
                     html += "~~";
                 }
                 else {
                     html += obj.endTime;
                 }

                 return html;
             }},
             {title:'播放时间(单位:秒)',dataIndex:'playTime',width:'10%',renderer:function(value, obj){
                     if (0 >= value) {
                         return '不限制';
                     }
                     return value;
             }},
    		 {title:'版本号',dataIndex:'version',width:'10%'},
    		 {title:'状态',dataIndex:'status',width:'5%',renderer:function(value, obj){
    		    var description = value;
                if (1 == value) {
                    description = '未启用';
                } else if (2 == value) {
                    description = '<span style="color:#00CC00">使用中</span>';
                } else if (3 == value) {
                    description = '<span style="color:#F00">停用中</span>';
                }
                return description;
             }}
            ];

	var crudGrid = new CrudGrid({
		entityName : '广告模组',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/mop/advert/group/list',
        addUrl : '${ctx}/mop/advert/group/add',
        updateUrl : '${ctx}/mop/advert/group/update',
        columns : columns,
		showAddBtn : true,
		showUpdateBtn : false,
		showRemoveBtn : false,
		addOrUpdateFormId : 'addOrUpdateForm',
		dialogContentId : 'addOrUpdate',
        operationColumnRenderer:function(value,obj){
            var rtv =  '';

            if (1 == obj.status) {
                rtv += '<span class="grid-command btnDel">删除</span>';
            }

            rtv +=  '<span class="grid-command btnCache">清除缓存</span>';

            if (1 == obj.status || 3 == obj.status) {
                rtv += '<span class="grid-command btnAct">启用</span>';
            }
            else if (2 == obj.status){
                rtv += '<span class="grid-command btnP" >暂停</span>';
            }
            if (update) {
                rtv += '<span class="grid-command btn-edit">编辑</span>';
            }

            return rtv;
        },
		gridCfg:{
    		innerBorder:true
    	},
		storeCfg:{//定义store的排序，如果是复合主键一定要修改
			sortInfo : {
				field : 'id',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
            }
        },
        operationColumnCfg:{
                    width:'18%'
        },

		});

	//点击获取列，获取主键id
    var grid = crudGrid.get('grid');

    grid.on('cellclick',function (ev) {
        console.info(ev);
        var sender = $(ev.domTarget); //点击的Dom
        var target = ev.record;
        var clickDom = sender.attr('id');
        //记录选中的id
        $("#selectId").val(target.id);
        if (sender.hasClass('btnCache')) {
            $.ajax({
              url:'${ctx}/mop/advert/group/cache',
              data:{id:target.id},
              async:false,
              success: function(data){
                   if (data.success){
                       showSuccess(data.msg);
                   }else {
                       showWarning(data.msg);
                   }
              },
              failure:function(){
                showWarning('系统异常，请稍后操作');
              }
            });
        }else if (sender.hasClass('btnDel')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认删除【"+target.code+"】该广告模组码?",'${ctx}/mop/advert/group/delete', confirmParams);
        }else if (sender.hasClass('btnAct')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认启用【"+target.code+"】该广告模组码?",'${ctx}/mop/advert/group/enable', confirmParams);
        }else if (sender.hasClass('btnP')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认暂停【"+target.code+"】该广告模组码?",'${ctx}/mop/advert/group/disable', confirmParams);
        }
    });

    var addOrUpdateFunction = function (event, id) {

        var title = '';
        var href = '${ctx}/mop/advert/group/detail/index';
        if (id) {
            title = '修改广告模组';
            href += '?id=' + id;
        }

        if (top.topManager) {
            //打开左侧菜单中配置过的页面
            top.topManager.openPage({
                id: 'main-menu',
                href: href,
                title: title
            });
        }
    }

    function confirmDialog(title, url, confirmParams) {
        var properties = {};
        properties.title = title;
        properties.contentId = "confirmContent";
        properties.url = url;

        var successFunc = function(data) {
            crudGrid.load(true);
        }

        var failureFunc = function() {

        }

        var confirmDialog = commonDialog(properties, confirmParams, successFunc, failureFunc);

        confirmDialog.show();
    }


    function commonDialog(properties, confirmParams, successFunc, failureFunc) {

        properties = properties || {};
        confirmParams = confirmParams || {};

        var title = properties.title || "";
        var width = properties.width || 350;
        var height = properties.height || 100;
        var contentId = properties.contentId || "";
        var formData = properties.formData || {};
        var url = properties.url || '#';

        successFunc = successFunc || function (data) {
        };

        failureFunc = failureFunc || function () {
        };


        var commonDialog = new OverLay.Dialog(
            {
                title : title,
                width : width,
                height : height,
                contentId : contentId,
                success : function() {
                    $.ajax({
                        url : url,
                        async : false,
                        type : "POST",
                        data : confirmParams,
                        success : function(data) {
                            successFunc(data);
                            if (data.success) {
                                BUI.Message.Alert(data.msg,'success');
                                commonDialog.close();
                            } else {
                                BUI.Message.Alert(data.msg,'warning');
                            }
                        },
                        failure : function() {
                            failureFunc();
                            BUI.Message.Alert('系统异常，请稍后操作','warning');
                        }
                    });
                }
            });

        return commonDialog;
    }



	/* 显示提示框 */
	function showWarning(str) {
        BUI.Message.Alert(str,'warning');
    }
    function showSuccess(str) {
        BUI.Message.Alert(str,'success');
    }

});
 
</script>
 
</body>
</html>


