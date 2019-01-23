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
		        <div class="control-group span8">
		            <label class="control-label">应用：</label>
                    <div id="appCodeSelect">
						<input type="hidden" id="appCode" name="appCode" >
                    </div>
		        </div>
		        <div class="control-group span8">
		            <label class="control-label">版本号：</label>
                    <div id="versionSelect" class="controls">
                      <input type="hidden" id="version" name="version">
                    </div>
                </div>
                <div class="control-group span8">
                    <label class="control-label">客户端：</label>
                    <div id="osTypeSelect" class="controls">
                      <input type="hidden" id="osType" name="osType">
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
	<framwork:crudPermission resource="/mop/appVersion"/>

    //清空
    $("#btnClear").click(function(){
        $(':input','#searchForm')
               .not(':button,:submit,:reset,:hidden')
               .val('')
               .removeAttr('checked')
               .removeAttr('checked');
        
        $("#appCode").val('');
        $("#version").val('');
        $("#osType").val('');
    });

    
    var appSelect = new BUI.Select.Select({
        render:'#appCodeSelect',
        valueField:'#appCode',
        width:'180',
        items:JSON.parse('${appInfoSelectItems}')
  	});
    
    var versionSelect = new BUI.Select.Select({
        render:'#versionSelect',
        valueField:'#version',
        width:'180',
        items:JSON.parse('${versionSelectItems}')
  	});
    
    var osTypeSelect = new BUI.Select.Select({
        render:'#osTypeSelect',
        valueField:'#osType',
        width:'180',
        items:JSON.parse('${osTypeSelectItems}')
  	});
    
    appSelect.render();
    versionSelect.render();
    osTypeSelect.render();

    var columns = [
        {title:'ID',dataIndex:'id',width:'12%'},
		{title:'应用',dataIndex:'appName',width:'15%'},
		{title:'版本号',dataIndex:'appVersion',width:'15%'},
        {title:'客户端',dataIndex:'osType',width:'6%'},
        {title:'更新说明',dataIndex:'updateNote',width:'12%'},
        {title:'通用版本更新',dataIndex:'commonRuleUpdateTypeDesc',width:'10%'},
        {title:'指定版本更新',dataIndex:'existAddition',width:'8%',
       	 renderer : function(value) {
				if (value) {
					return '有';
				} else {
					return '无';
				}
			}
        },
        {title:'高级配置',dataIndex:'existCustom',width:'8%',
       	 renderer : function(value) {
					if (value) {
						return '有';
					} else {
						return '无';
					}
				}
        },
        {title:'创建时间',dataIndex:'createdTime',width:'15%'}
        ];

    var crudGrid = new CrudGrid({
		entityName : 'APP版本管理',
    	pkColumn : 'id',//主键
      	storeUrl : '${ctx}/mop/appVersion/list',
        addUrl : '${ctx}/mop/appVersion/add',
        columns : columns,
		showAddBtn : add,
		showAddBtn : false,
		showUpdateBtn : false,
		showRemoveBtn : false,
		addOrUpdateFormId : 'addOrUpdateForm',
		dialogContentId : 'addOrUpdate',
        operationColumnRenderer:function(value,obj){
            var rtv =  '';
            rtv += '<span class="grid-command btnedit">查看&编辑</span>';
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
        //loadSuggestData();
        //suggest.set('data', suggestData);
    })

    var addOrUpdateFunction = function (event, id) {

        var title = '';
        var href = '${ctx}/mop/appVersion/detail/index';
        if (id) {
            title = '修改APP版本';
            href += '?id=' + id;
        } else {
            title = '新增APP版本';
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


