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
	    <p>
          <div class="tips tips-small tips-info">
            <span class="x-icon x-icon-small x-icon-info"><i class="icon icon-white icon-info"></i></span>
            <div class="tips-content">权重数字越小前端展示位置越靠前。若模块中，多个内容的权重相同时，则以内容的创建时间判断；新创建的优先展示，早创建的排后。</div>
          </div>
        </p>
        <p>
          <div class="tips tips-small tips-info">
            <span class="x-icon x-icon-small x-icon-info"><i class="icon icon-white icon-info"></i></span>
            <div class="tips-content">模块下的内容编辑后，需要操作“发布更新”才能将修改的内容在线上生效。</div>
          </div>
        </p>
		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
		    <div class="row">
		        <div class="control-group span7">
		            <label class="control-label">内容名称:</label>
                    <div id="s3">

                    </div>
                    <input type="hidden" id="title" name="title" >
		        </div>
		        <div class="control-group span8">
		            <label class="control-label">关联模块:</label>
                    <div id="s1" class="controls">
                      <input type="hidden" id="blockId" name="blockId" value="${blockId}" >
                    </div>
                </div>
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
                <div class="control-group span12">
                    <label class="control-label">时间:</label>
                    <div class="controls bui-form-group height_auto" data-rules="{dateRange : true}">
                        <input type="text" class="calendar-time calendar" name="startTime" data-tip="{text : '开始日期'}" >
                        <span>- </span>
                        <input name="endTime" type="text" class="calendar-time calendar" data-tip="{text : '结束日期'}">
                    </div>
                </div>
                <div class="span1 offset1">
                    <button  type="button" id="btnSearch" class="button button-primary">查询</button>
                </div>
                <div class="span1 offset1">
                    <button  type="button" id="btnClear" class="button button-primary">清空</button>
                </div>
                <div class="span1 offset1">
                    <button  type="button" id="btnAdd" class="button button-primary">新增</button>
                </div>
            </div>
		</form>

		<!-- 新增 -->
		<div id="addOrUpdate" class="hide">
            <form class="form-inline" id="addOrUpdateForm">
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">模块编码：</label>
                        <div id="s2" class="controls" >
                          <input type="hidden" id="blockId2" name="blockIds" width="200px;">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">标题：</label>
                        <div class="controls">
                          <input type="text" name="title" class="control-text">
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
                <input type="hidden" name="id" value="">
            </form>
		</div>



		<div class="search-grid-container">
			<div id="grid"></div>
		</div>
	</div>

    <div id="confirmContent" class="hide">
        <!--  <form id="searchInfoForm" class="form-inline"> -->

        <!--    </form> -->
    </div>

<script type="text/javascript">

BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/overlay','bui/common/page','bui/select','bui/data','bui/uploader'],function (CrudGrid,Form,ChargeDialog,OverLay,Page) {

	//定义页面权限
	var add=false,update=false,list=false, del = false;
	//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
	<framwork:crudPermission resource="/mop/advert/item"/>

    //清空
    $("#btnClear").click(function(){
        $(':input','#searchForm')
               .not(':button,:submit,:reset,:hidden')
               .val('')
               .removeAttr('checked')
               .removeAttr('checked')
        $("#blockId").val("")
    });

    //模糊查询select
    var suggestData;
    var loadSuggestData = function() {
        $.ajax({
              url:'${ctx}/mop/advert/item/titles',
              async:false,
              success: function(data){
                   if (data){
                       suggestData = data;
                   }else {
                       showWarning(data.msg);
                   }
              },
              failure:function(){
                showWarning('系统异常，请稍后操作');
              }
        });
    }

    var suggest = new BUI.Select.Suggest({
           render:'#s3',
           data:suggestData,
    });


    suggest.render();
    suggest.on('change', function(ev){
        $("#title").val(ev.value);
    });

     var selectStore = new BUI.Data.Store({
           url : '${ctx}/mop/advert/item/blockItems',
           autoLoad : true
     });

     var select = new BUI.Select.Select({
           render:'#s1',
           valueField:'#blockId',
           width:'200',
           store : selectStore
     });

     var select2 = new BUI.Select.Select({
            render:'#s2',
            valueField:'#blockId2',
            store : selectStore,
            width:'200',
            multipleSelect:false
     });

     select.render();
     select2.render();
     
     function statusDesc(value){
         if (1 == value) {
             return '未启用';
         } else if (2 == value) {
         	return '使用中';
         } else if (3 == value) {
         	return '停用中';
         } else {
         	return '未知';
         }
     }

    var columns = [
             {title:'创建时间',dataIndex:'createdTime',width:'10%'},
             {title:'更新时间',dataIndex:'updateTime',width:'10%'},
             {title:'内容ID',dataIndex:'id',width:'12%'},
    		 {title:'内容名称',dataIndex:'title',width:'12%'},
    		 {title:'关联模块',dataIndex:'parents',width:'15%',renderer:function(value, obj){
    		     var html = '';

    		     if (null == value || 0 == value.length) {
                     return html;
                 }

    		     for (var i=0;i<value.length;i++) {
                     if ("" != html) {
                         html += ",";
                     }
                     html += value[i].title;
                 }

                 html = "<p style='white-space:nowrap;overflow:hidden;text-overflow: ellipsis;' title='"+html+"'>"+html+"</p>";

    		     return html;
    		 }},
             {title:'位置权重',dataIndex:'weight',width:'6%'},
             {title:'展示时间',dataIndex:'beginTime',width:'24%',renderer:function(value, obj){
                     if (typeof(value) == "undefined" ) {
                            value = "无 "
                     }
                     var end = obj.endTime;
                     if (typeof(end) == "undefined" ) {
                             end = " 无"
                     }
                     var html = value+'至'+end;

                     return html;
             }},

    		 {title:'修改状态',dataIndex:'editCopyId',width:'5%',renderer:function(value, obj){
                     var description = value;
                     if (0 == value) {
                         description = '无修改';
                     } else{
                         description = '未发布';
                     }
                     return description;
    		 }},
    		 {title:'线上状态',dataIndex:'originalStatus',width:'8%',renderer:function(value, obj){

                var description = value;
                 if (1 == value) {
                     description = '未启用';
                 } else if (2 == value) {
                     description = '<span style="color:#00CC00">使用中</span>';
                 } else if (3 == value) {
                     description = '<span style="color:#F00">停用中</span>';
                 }

                 return description;
             }},
             {title:'发布后状态',dataIndex:'status',width:'8%', renderer:function(value, obj){
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
		entityName : '广告内容',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/mop/advert/item/list',
        addUrl : '${ctx}/mop/advert/item/add',
        columns : columns,
		showAddBtn : false,
		showUpdateBtn : false,
		showRemoveBtn : false,
		addOrUpdateFormId : 'addOrUpdateForm',
		dialogContentId : 'addOrUpdate',
		operationColumnRenderer: function (value, obj) {
            var rtv =  '';

            if (1 == obj.status) {
                rtv += '<span class="grid-command btnDel">删除</span>';
            }

            rtv +=  '<span class="grid-command btnCache" style="display:none;">刷新</span>';

            if (1 == obj.status || 3 == obj.status) {
                rtv += '<span class="grid-command btnAct">启用</span>';
            }
            else if (2 == obj.status){
                rtv += '<span class="grid-command btnP" >停用</span>';
            }

            if (update) {
               rtv += '<span class="grid-command btnedit">编辑</span>';
            }

            if (0 < obj.editCopyId || 0 < obj.childEditCount) {
                rtv += '<span class="grid-command rollback" >清除修改</span> ';
            }

            return rtv;
        },

		gridCfg:{
    		innerBorder:true
    	},
    	operationColumnCfg:{
            width:'18%'
        },
		storeCfg:{//定义store的排序，如果是复合主键一定要修改
			sortInfo : {
				field : '',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
            }
        },

		});

	//点击获取列，获取主键id
    var grid = crudGrid.get('grid');

    //手动刷新
    var g_store = grid.get("store");
    g_store.on('load', function (ev) {
        loadSuggestData();
        suggest.set('data', suggestData);
    })

    var addOrUpdateFunction = function (event, id) {

        var title = '';
        var href = '${ctx}/mop/advert/item/detail/index';
        if (id) {
            title = '修改广告配置';
            href += '?id=' + id;
        } else {
            title = '新增广告配置';
        }

        if (top.topManager) {
            //打开左侧菜单中配置过的页面
            top.topManager.openPage({
                id: 'main-menu',
                href: href,
                title: title,
                reload: true
            });
        }
    }

    //新增
    $("#btnAdd").click(function(){
        addOrUpdateFunction("", "");
    });

    grid.on('cellclick',function (ev) {
        console.info(ev);
        var sender = $(ev.domTarget); //点击的Dom
        var target = ev.record;
        var clickDom = sender.attr('id');
        //记录选中的id
        $("#selectId").val(target.id);
        if (sender.hasClass('btnedit')) {
            addOrUpdateFunction('', target.id);
        } else if (sender.hasClass('btnCache')) {
            $.ajax({
              url:'${ctx}/mop/advert/item/cache',
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
            confirmDialog("确认删除【"+target.id+"】该广告吗?",'${ctx}/mop/advert/item/detail/delete', confirmParams);
        }else if (sender.hasClass('btnAct')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认启用【"+target.id+"】该广告吗?",'${ctx}/mop/advert/item/detail/enable', confirmParams);
        }else if (sender.hasClass('btnP')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认暂停【"+target.id+"】该广告吗?",'${ctx}/mop/advert/item/detail/disable', confirmParams);
        }else if (sender.hasClass('publish')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认发布【"+target.id+"】该广告吗?",'${ctx}/mop/advert/item/detail/publish', confirmParams);
        }else if (sender.hasClass('rollback')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认重置【"+target.id+"】该广告吗?",'${ctx}/mop/advert/item/detail/reset', confirmParams);
        }
    });


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
        var width = properties.width || 320;
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


