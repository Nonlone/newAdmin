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
	<style>
        /**内容超出 出现滚动条 **/
        .bui-stdmod-body{
          overflow-x : hidden;
          overflow-y : auto;
        }
    </style>
        <p>
          <div class="tips tips-small tips-info">
            <span class="x-icon x-icon-small x-icon-info"><i class="icon icon-white icon-info"></i></span>
            <div class="tips-content">若模块中，使用中的内容多于模块设置的前端展示内容数量限制，则以内容创建时间判断：新创建的优先展示，超过数量限制后前端直接不展示创建时间较早的内容。</div>
          </div>
        </p>
		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
		    <div class="row">
		        <div class="control-group span7">
		        <label class="control-label">关联模块:</label>
                    <div id="s1" class="controls">
                      <input type="hidden" id="blockId" name="blockId" >
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
                <input type="hidden" name="code" class="control-text">
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label"><s>*</s>标题：</label>
                        <div class="controls">
                          <input type="text" name="title" class="control-text" data-rules="{required : true}">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control-group span8">
                        <label class="control-label">关联模组：</label>
                        <div id="refGroupIdsDiv" class="controls" >
                            <input type="hidden" id="refGroupIdsInput" name="groupIds" width="200px;" data-rules="{required : true}">
                        </div>
                    </div>
                    <div class="control-group span8">
                        <label class="control-label">模块类型:</label>
                        <div id="blockTypeInputDiv" class="controls">
                            <select name="blockType" id="selectType">
                                <option value="1">横幅组</option>
                                <option value="2">轮播图</option>
                                <option value="3">公告</option>
                                <option value="4">闪屏</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div id="showLimitInput" class="control-group span8">
                        <label class="control-label"><s>*</s>展示内容数量限制:</label>
                        <div class="controls">
                            <input type="text" name="showLimit" class="control-text" data-rules="{number:true, required : true, max:50}" placeholder="在前端，模块每次最多可展示的内容数量">
                        </div>
                    </div>
                    <div id="showTimeInput" class="control-group span8">
                        <label class="control-label"><s>*</s>内容停留时长/s:</label>
                        <div class="controls">
                            <input type="text" name="playTime" class="control-text" data-rules="{number:true, required : true, max:999}" placeholder="控制模块下的内容每隔Ns自动切换、或内容停留Ns后会自动跳过">
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

        <div id="confirmContent" class="hide">
        </div>

<script type="text/javascript">

BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/overlay','bui/common/page','bui/grid','bui/data'],function (CrudGrid,Form,ChargeDialog,OverLay,Page,Grid,Data) {

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

    //
    $("#selectType").change(function(){
        var value = $("#selectType").val();
        if ('1' == value) {
            showLimitInput.removeAttr("disabled");
        } else if('2' == value) {
            showLimitInput.removeAttr("disabled");
        } else if('3' == value) {
            showLimitInput.removeAttr("disabled");
        } else if('4' == value) {
            showLimitInput.val(1);
            showLimitInput.attr("disabled","disabled");
        }
    });
    var suggestData;

    var loadSuggestData = function() {
        $.ajax({
              url:'${ctx}/mop/advert/item/blockItems',
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

    loadSuggestData();

    var suggest = new BUI.Select.Suggest({
       render:'#s1',
       data:suggestData,
    });

    suggest.render();
    suggest.on('change', function(ev){
        console.log(suggestData);
        $("#blockId").val(ev.value);
    });

    var groupIdSelectStore = new BUI.Data.Store({
        url : '${ctx}/mop/advert/block/groupItems',
        autoLoad : true
    });

    var groupIdSelect = new BUI.Select.Select({
        render:'#refGroupIdsDiv',
        valueField:'#refGroupIdsInput',
        store : groupIdSelectStore,
        width:'200',
        multipleSelect:true
    });

    groupIdSelect.render();

    var openContentLink = function (event, blockId) {
        var title = '';
        var href = '${ctx}/mop/advert/item';
        if (blockId) {
            title = '内容管理';
            href += '?blockId=' + blockId;
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

    //预览
    var Store = Data.Store;
    var preview_columns = [
            {title:'图片/文本',dataIndex:'content',width:'60%', renderer: function(value, obj) {
                                if ('image' == obj.type) {
                                    return '<img width="480"  src="' + value + '"/>';
                                }
                                return value;
                            }},

            {title:'点击事件',dataIndex:'event',width:'40%', renderer: function(value, obj){
                                if (obj.eventType) {
                                    return obj.eventType + "<br/>" + value;
                                }
                                return "无事件";
                            }}
            ];

    var preview_store = new Store({
         url: '${ctx}/mop/advert/block/preview',
         autoLoad:true
    });
    var preview_grid = new Grid.Grid({
           columns : preview_columns,
           store : preview_store,
           plugins : [Grid.Plugins.RowNumber]
    });

    var preview_dialog = new OverLay.Dialog({
            title:'',
            width:800,
            height:500,
            children : [preview_grid],
            childContainer : '.bui-stdmod-body',
            success:function () {
              this.close();
            }
    });
    
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
             {title:'模块ID',dataIndex:'id',width:'12%'},
    		 {title:'模块名称',dataIndex:'title',width:'15%'},
             {title:'模块类型',dataIndex:'blockType',width:'6%',renderer:function(value, obj){
                     if (1 == value) {
                         return '横幅组';
                     }
                     else if (2 == value) {
                    	 return '轮播图';
                     }
					 else if (3 == value) {
						 return '公告';
                     }
					 else if (4 == value) {
						 return '闪屏';
					 }
                     return "未知";
             }},
             {title:'展示限制',dataIndex:'showLimit',width:'6%',renderer:function(value, obj){
	                 if (0 >= value) {
	                     return '不限制';
	                 }
	                 return value;
	         }},
             {title:'关联数量',dataIndex:'itemCount',width:'6%'},
             {title:'创建时间',dataIndex:'createdTime',width:'10%'},
             {title:'发布时间',dataIndex:'publishTime',width:'10%'},
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
		entityName : '广告模块',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/mop/advert/block/list',
        addUrl : '${ctx}/mop/advert/block/add',
        updateUrl : '${ctx}/mop/advert/block/update',
        columns : columns,
		showAddBtn : add,
		showUpdateBtn : false,
		showRemoveBtn : false,
		addOrUpdateFormId : 'addOrUpdateForm',
		dialogContentId : 'addOrUpdate',
        operationColumnRenderer:function(value,obj){
            var rtv =  '';

            if (1 == obj.status) {
                rtv += '<span class="grid-command btnDel">删除</span>';
            }

            rtv +=  '<span class="grid-command btnCache">刷新</span>';

            rtv +=  '<span class="grid-command btnManage">内容管理</span>';

            if (1 == obj.status || 3 == obj.status) {
                rtv += '<span class="grid-command btnAct">启用</span>';
            }
            else if (2 == obj.status){
                rtv += '<span class="grid-command btnP" >停用</span>';
            }

            rtv += '<span class="grid-command btn-edit">编辑</span>';

            if (0 < obj.editCopyId || 0 < obj.childEditCount) {
                rtv += '<span class="grid-command publish" >发布</span> ';
                rtv += '<span class="grid-command rollback" >清除修改</span> ';
            }

            rtv += '<span class="grid-command btnPreview">预览</span>';
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
				field : 'id',//排序字段
				direction : 'DESC' //升序ASC，降序DESC
            }
        }

		});

	//点击获取列，获取主键id
    var grid = crudGrid.get('grid');

    //手动刷新
    var g_store = grid.get("store");
    g_store.on('load', function (ev) {
        loadSuggestData();
        suggest.set('data', suggestData);
    })

    grid.on('cellclick',function (ev) {
        console.info(ev);
        var sender = $(ev.domTarget); //点击的Dom
        var target = ev.record;
        var clickDom = sender.attr('id');
        //记录选中的id
        $("#selectId").val(target.id);
        if (sender.hasClass('btnCache')) {
            $.ajax({
              url:'${ctx}/mop/advert/block/cache',
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
            confirmDialog("确认删除【"+target.code+"】该广告模块码?",'${ctx}/mop/advert/block/delete', confirmParams);
        }else if (sender.hasClass('btnAct')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认启用【"+target.code+"】该广告模块码?",'${ctx}/mop/advert/block/enable', confirmParams);
        }else if (sender.hasClass('btnP')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认暂停【"+target.code+"】该广告模块码?",'${ctx}/mop/advert/block/disable', confirmParams);
        }else if (sender.hasClass('btnPreview')) {
            var param = {};
            param.blockId = target.id;
            preview_store.load(param);
            preview_dialog.set('title', "预览效果--" + target.title + "<br/>(以下为当前模块点击发布更新后，线上将会展示的内容。)");
            preview_dialog.show();
        }else if (sender.hasClass('publish')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认发布【"+target.id+"】该广告模块吗?",'${ctx}/mop/advert/block/publish', confirmParams);
        }else if (sender.hasClass('rollback')) {
            var confirmParams = {};
            confirmParams.id = target.id;
            confirmDialog("确认重置【"+target.id+"】该广告模块吗?",'${ctx}/mop/advert/block/reset', confirmParams);
        }else if (sender.hasClass('btnManage')) {
            openContentLink('', target.id);
        }
    });


    var beforeUpdateShow = function(dialog,form,record){
        $("#selectType").attr("disabled","disabled");

        groupIdSelect.setSelectedValue('');
        groupIdSelect.setSelectedValue(record.groupIds);
    };
    crudGrid.on('beforeAddShow', function(ev){
        $("#selectType").removeAttr("disabled");
    });
    crudGrid.on('beforeUpdateShow', beforeUpdateShow);


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


