<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../common/import-tags.jsp" %>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../common/import-static.jsp" %>
</head>
<style>
.form-horizontal .control-label {
    width: 110px;
}

.required-color {
    padding-right: 5px;
    color: red;
    text-decoration: none;
}

.addition-group .x-form-error {
	float:left;
}
</style>
<body>

<div id="formDiv">
<form id="detail" class="form-horizontal" action="${ctx}/mop/appVersion/saveOrUpdate" method="post">
    <input type="hidden" id="id" name="id" value="${id}">
    <input type="hidden" id="updateRule" name="updateRule" value="">
    <h2 style="font-weight: bold;margin-left: 10px;">通用配置</h2>
    
    <div id="baseInfo" class="row">
        
        <div class="control-group span10">
            <label class="control-label"><s>*</s>应用：</label>
            <div id="appCodeSelect" class="controls">
				<input type="hidden" id="appCode" name="appCode" data-rules="{required : true}">
            </div>
        </div>

        <div id="textContent" class="control-group span10">
            <label class="control-label"><s>*</s>客户端：</label>
            <div id="osTypeSelect" class="controls">
              <input type="hidden" id="osType" name="osType" data-rules="{required : true}">
            </div>
        </div>

    </div>

    <div class="row">
	    <div class="control-group span10">
	        <label class="control-label"><s>*</s>内部版本号：</label>
	        <div id="s1" class="controls">
	            <input id="insideVersion" name="insideVersion" type="text" class="control-text" style="width: 150px;" data-rules="{required : true, appVersion : true}" placeholder="例如:1.1.0">
	        </div>
	    </div>
	
	    <div class="control-group span10">
	        <label class="control-label"><s>*</s>外部版本号：</label>
	        <div class="controls">
	            <input id="appVersion" name="appVersion" type="text" class="control-text" style="width: 150px;" data-rules="{required : true, appVersion : true}" placeholder="例如:1.1.0">
	        </div>
	    </div>
    </div>
    
    <div class="row">
	    <div class="control-group span10">
	        <label class="control-label"><s>*</s>通用版本更新：</label>
	        <div id="commonRuleUpdateTypeSelect" class="controls">
				<input type="hidden" id="commonRuleUpdateType" name="commonRuleUpdateType" class="defaultUpdateType" data-rules="{required : true}" placeholder="请选择更新方式">
            </div>
	    </div>
    </div>
    
    <div class="row">
	    <div class="control-group">
	        <label class="control-label">指定版本更新：</label>
	        <div class="controls">
	        	<button type="button" id="addAdditionButton" class="button" style="margin-left:10px;">添加</button>
	        </div>
	        <div id="additionDiv" class="controls" style="margin-left:120px; width:100%; height:auto;">
	        </div>
	    </div>
    </div>
    
    <div class="row">
	    <div class="control-group">
            <label class="control-label"><s>*</s>更新说明：</label>
            <div class="controls" style="margin-left:20px;height: 80px;">
                <textarea id="updateNote" name="updateNote" type="text" class="control-text"
                          style="width: 300px;height: 60px;" data-rules="{required : true}"></textarea>
            </div>
        </div>
    </div>

	
	<div class="row">
		<hr>
    </div>
	
	
	<div class="row" style="padding-left: 20px;width:915px;">
    	<span style="font-weight: bold;font-size: 18px;">高级配置</span>
    	<button type="button" id="addAdvanceButton" class="button showAdvanceShow" style="margin-left:60px;margin-top: -5px;">添加高级配置</button>
    	<button type="button" class="button advanceToggleButton showAdvanceHide" style="margin-left:10px;margin-top: -5px;float:right;display:none;">显示高级配置↓</button>
    	<button type="button" class="button advanceToggleButton showAdvanceShow" style="margin-left:10px;margin-top: -5px;float:right;">收起高级配置↑</button>
    </div>
    
    <div id="customConfigDiv" style="padding:20px;">
    	
    </div>
    

    <div class="row actions-bar" style="margin-top: 50px;">
        <div class="form-actions span13 offset1">
            <button id="submit" type="submit" class="button button-primary">保存</button>
        </div>
    </div>
</form>
</div>

<!-- 渠道列表 -->
<div id="channelList" class="hide">
	<div class="search-grid-container">
		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
		    <div class="row">
		    	<div class="control-group span8" style="width:220px;">
		            <label class="control-label" style="width:80px;">渠道大类：</label>
                    <div id="channelSortSelect">
						<input type="hidden" id="channelSort" name="channelSort" >
                    </div>
		        </div>
		        <div class="control-group span8" style="width:260px;">
		            <label class="control-label" style="width:80px;">渠道标识：</label>
                    <div class="controls">
						<input type="text" class="input-normal control-text" name="channelCode">
					</div>
		        </div>
                <div class="span1 offset1" style="margin-left:30px;">
                    <button  type="button" id="btnSearch" class="button button-primary">查询</button>
                </div>
                <div class="span1 offset1">
                    <button  type="button" id="btnClear" class="button button-primary">清空</button>
                </div>
            </div>
		</form>
		<div id="grid" ></div>
		<input type="hidden" id="disableChannels" name="disableChannels" >
		<div style="font-size: 14px;margin-top: 35px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">
			已选<span id="channelListSelectSize">{}</span>个渠道：<span id="channelListSelectContent" >{}</span>
		</div>
	</div>
</div>

<script type="text/javascript">

    (function($){
        $.isBlank = function(obj){
            return(!obj || $.trim(obj) === "");
        };
        $.isNotBlank = function(obj){
            return !$.isBlank(obj);
        };
    })(jQuery);
    
    var editForm = null;
	var editFormContainer={};
	
    var appInfos = JSON.parse('${appInfoSelectItems}');
    var osTypes = JSON.parse('${osTypeSelectItems}');
    var updateTypes = JSON.parse('${updateTypeSelectItems}');
    var versions = JSON.parse('${versionSelectItems}');
    var channelSorts = JSON.parse('${channelSortSelectItems}');
    
    function removeCustomConfig(containerId, index) {
     	$("#"+containerId+" #custom_"+index).remove();
    }
    
    function removeAdditionRule(containerId, prefix, index) {
    	$("#"+containerId+" #"+prefix+"_addition_"+index).remove();
    	
		var form = editFormContainer['"addition_'+index+'"'];
    	
		if (null != form) {
			form.destroy();
			editFormContainer['"addition_'+index+'"'] = null;
    	}
    }
    
    BUI.use(['bui/ux/crudgrid','bui/form','bui/ux/savedialog','bui/overlay','bui/common/page','bui/select','bui/grid','bui/data'], function (CrudGrid,Form,ChargeDialog,OverLay,Page,Select,Grid,Data) {
    	
        BUI.Form.Rules.add({
        	  name : 'versionRange',
        	  msg : '结束版本号必须大于或等于开始版本号！',
        	  validator : function(value,baseValue,formatedMsg,group){
	              var beginValue = value.beginVersion;
	          	  var endValue = value.endVersion;
		          if (null == beginValue || "" == beginValue) {
		      	  	return undefined;
		      	  }
		          
		          if (null == endValue || "" == endValue) {
		      	  	return undefined;
		      	  }
        		  
		          return versionCmp(beginValue, endValue) <= 0 ? undefined : formatedMsg;
        	 }
        });
        
        Form.Rules.add({
            name : 'appVersion',  //规则名称
            msg : '请填写正确的版本格式,如:1.2.1',//默认显示的错误信息
            validator : function(value,baseValue,formatMsg){
              var regexp = new RegExp('^[0-9]+[\\.][0-9]+[\\.][0-9]+$');
              if(value && !regexp.test(value)){
                return formatMsg;
              }
            }
        }); 
    	
    	var id = $("#id").val();
    	
    	var appSelect = new BUI.Select.Select({
            render:'#appCodeSelect',
            valueField:'#appCode',
            width:'180',
            items: appInfos
      	});
        
        var osTypeSelect = new BUI.Select.Select({
            render:'#osTypeSelect',
            valueField:'#osType',
            width:'180',
            items: osTypes
      	});
        
        var commonRuleUpdateTypeSelect = new BUI.Select.Select({
            render:'#commonRuleUpdateTypeSelect',
            valueField:'#commonRuleUpdateType',
            width:'180',
            items: updateTypes
      	});
        
        var channelSortSelect = new BUI.Select.Select({
            render:'#channelSortSelect',
            valueField:'#channelSort',
            width:'130',
            items: channelSorts
      	});
        
        var customConfigIndex = 0;
        var additionConfigIndex = 0;
        
        function addCustomConfig(containerId, index, data) {
        	
        	var html = '';
        	html += '<div id="custom_'+index+'" class="customChannelDiv" index="'+index+'" style="padding: 10px 0px;border: 6px solid white;">';
        	//html += 	'<input type="hidden" id="custom_" name="osType">';
        	html += 	'<div class="row">';
        	html += 		'<div class="control-group" style="width:910px;">';
        	html += 			'<label class="control-label"><s>*</s>选择渠道：</label>';
        	html += 			'<div class="controls">';
        	html += 				'<input id="customChannel_'+index+'" name="selectChannels" type="text" class="control-text customChannelValue" readonly="readonly"  style="margin-left:10px;width: 325px;" data-rules="{required : true}" placeholder="请选择渠道">';
        	html += 				'<button type="button" id="selectChannelButton_'+index+'" class="button" style="margin-left:-5px;">选择</button>';
        	html += 			'</div>';
        	html += 			'<button type="button" class="button" style="float:right;" onclick="removeCustomConfig(\''+containerId+'\',\''+index+'\')">删除配置</button>';
        	html += 		'</div>';
        	html += 	'</div>';
        	html += 	'<div class="row">';
        	html += 		'<div class="control-group span10">';
        	html += 			'<label class="control-label"><s>*</s>通用版本更新：</label>';
        	html += 			'<div id="customRuleUpdateTypeSelect_'+index+'" class="controls">';
        	html += 				'<input type="hidden" id="customRuleUpdateType_'+index+'" name="defaultUpdateType" class="defaultUpdateType" data-rules="{required : true}" placeholder="请选择更新方式">';
        	html += 			'</div>';
        	html += 		'</div>';
        	html += 	'</div>';
        	html += 	'<div class="row">';
        	html += 		'<div class="control-group">';
        	html += 			'<label class="control-label">指定版本更新：</label>';
        	html += 			'<div class="controls">';
        	html += 				'<button type="button" id="customAddAdditionButton_'+index+'" class="button" style="margin-left:10px;">添加</button>';
        	html += 			'</div>';
        	html += 			'<div id="customAdditionDiv_'+index+'" class="controls" style="margin-left:120px; width:100%; height:auto;">';
        	html += 			'</div>';
        	html += 		'</div>';
        	html += 	'</div>';
        	html += '</div>';
        	
        	$("#"+containerId).append(html);
        	
        	if (null != data) {
        		data.selectChannels = data.channels.toString();
        		$("#custom_"+index+" :input").each(function (i) {
                    $(this).val(data[$(this).attr('name')])
                });
        		
        		if (null != data.additions && 0 < data.additions.length) {
        			for (var j=0;j < data.additions.length; j++) {
        				var addition = data.additions[j];
            			addAdditionRule("customAdditionDiv_"+index, "custom_"+index, additionConfigIndex++, addition);
        			}
        		}
        	}
        	
        	
            $("#customAddAdditionButton_"+index).click(function () {
            	addAdditionRule("customAdditionDiv_"+index, "custom_"+index, additionConfigIndex++);
            });
            
            $("#selectChannelButton_"+index).click(function () {
            	var disableChannels = [];
            	var thisChannels = $("#customChannel_"+index).val();
            	
            	$(".customChannelValue").each(function(){
            		var tmpChannels = $(this).val();
				    if (tmpChannels != thisChannels) {
				    	var tmpChannelArray = tmpChannels.split(",");
				    	
				    	if (null != tmpChannelArray && 0 < tmpChannelArray.length) {
			       			
			       			for (var j=0;j < tmpChannelArray.length; j++) {
			    				var channelId = tmpChannelArray[j];
			    				disableChannels.push(channelId);
			    			} 
			    		}
				    	
				    }
				});
        		showChannelListDialog(index, disableChannels, thisChannels.split(","));
            });
            
            
            var customRuleUpdateTypeSelect = new BUI.Select.Select({
                render:'#customRuleUpdateTypeSelect_'+index,
                valueField:'#customRuleUpdateType_'+index,
                width:'180',
                items: updateTypes
          	});
        	
            customRuleUpdateTypeSelect.render();
        }
        
        function addAdditionRule(containerId, prefix, index, data) {
        	
        	var updateVersionBegin = prefix+'_additionUpdateVersionBegin_'+index;
        	var updateVersionBeginSelect = prefix+'_additionUpdateVersionBeginSelect_'+index;
        	
        	var updateVersionEnd = prefix+'_additionUpdateVersionEnd_'+index;
        	var updateVersionEndSelect = prefix+'_additionUpdateVersionEndSelect_'+index;
        	
        	var updateType = prefix+'_additionUpdateType_'+index;
        	var updateTypeSelect = prefix+'_additionUpdateTypeSelect_'+index;
        	
        	var html = '';
        	html += '<div id="'+prefix+'_addition_'+index+'" class="additionRule" style="width:100%;float: left;margin-left: -10px">';
        	html += 	'<div id="'+prefix+'_addition_group_'+index+'" class="bui-form-group addition-group controls" data-rules="{required : true, versionRange:true}">';
        	html += 	'<div id="'+updateVersionBeginSelect+'" class="controls">';
        	html += 		'<s class="required-color">*</s><input type="hidden" id="'+updateVersionBegin+'" name="beginVersion" class="updateVersionBegin" placeholder="请选择版本">';
        	html += 	'</div>';
        	html += 	'<label class="control-label" style="width:30px;">&nbsp;&nbsp;至&nbsp;&nbsp;</label>';
        	html += 	'<div id="'+updateVersionEndSelect+'" class="controls">';
        	html += 		'<s class="required-color">*</s><input type="hidden" id="'+updateVersionEnd+'" name="endVersion" class="updateVersionEnd" placeholder="请选择版本">';
        	html += 	'</div>';
        	html += 	'</div>';
        	html += 	'<label class="control-label" style="width:100px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s>*</s>更新方式：</label>';
        	html += 	'<div id="'+updateTypeSelect+'" class="controls">';
        	html += 		'<input type="hidden" id="'+updateType+'" name="updateType" class="updateType" data-rules="{required : true}" placeholder="请选择更新方式">';
        	html += 	'</div>';
        	html += 	'<button type="button" id="'+prefix+'_removeAdditionButton_'+index+'" class="button" style="margin-left:15px;" onclick="removeAdditionRule(\''+containerId+'\',\''+prefix+'\',\''+index+'\')">删除</button>';
        	html += '</div>';
        	
        	$("#"+containerId).append(html);
        	
        	if (null != data) {
        		$("#"+prefix+"_addition_"+index+" :input").each(function (i) {
                    $(this).val(data[$(this).attr('name')])
                });
        	}
        	
            var versionBeginComponent = new BUI.Select.Select({
                render:'#'+updateVersionBeginSelect,
                valueField:'#'+updateVersionBegin,
                width:'180',
                items: versions
          	});
            
            var versionEndComponent = new BUI.Select.Select({
                render:'#'+updateVersionEndSelect,
                valueField:'#'+updateVersionEnd,
                width:'180',
                items: versions
          	});
            
            var typeComponent = new BUI.Select.Select({
                render:'#'+updateTypeSelect,
                valueField:'#'+updateType,
                width:'180',
                items: updateTypes
          	});
            
            versionBeginComponent.on('validchange change',function(ev){
            	versionCheckEvent(prefix, index);
            });
            
            versionEndComponent.on('validchange change',function(ev){
            	versionCheckEvent(prefix, index);
            });
            
            versionBeginComponent.render();
            versionEndComponent.render();
            typeComponent.render();
            
            if (null != editForm) {
            	extEditForm = new Form.HForm({
                    srcNode: '#'+prefix+'_addition_'+index
                });
            	extEditForm.render(); 
            	
            	editFormContainer['"addition_'+index+'"'] = extEditForm;
            }
        }
        
        function versionCheckEvent(prefix, index) {
        	var form = editFormContainer['"addition_'+index+'"'];
        	
    		if (null == form) {
    			form = editForm;
        	}
        	
        	var item = form.getChild(prefix+'_addition_group_'+index);
        	
        	var errors = [];
        	item.validControl(item.getRecord());
        	errors.push(item.get('error'));
        	item.showErrors(errors);
        }
        
        function toggleCustom(hide) {
        	
        	var flag = hide || $("#customConfigDiv").is(':visible');
        	
        	if (true == flag) {
        		$(".showAdvanceHide").show();
        		$(".showAdvanceShow").hide();
        		$("#customConfigDiv").hide("slow");
        	}
        	else {
        		$(".showAdvanceHide").hide();
        		$(".showAdvanceShow").show();
        		$("#customConfigDiv").show("slow");
        	}
        }
        
        $("#addAdditionButton").click(function () {
        	addAdditionRule("additionDiv", "common", additionConfigIndex++);
        });
        
        $("#addAdvanceButton").click(function () {
        	addCustomConfig("customConfigDiv", customConfigIndex++);
        });
        
        $(".advanceToggleButton").click(function () {
        	toggleCustom();
        });
    	
    	function refreshForm() {
            $.ajax({
                url: '${ctx}/mop/appVersion/detail' + "?id=" + id,
                async: false,
                type: "POST",
                dataType:'json',
                success: function (data) {
                    if (data) {
                        $('#detail :input').each(function (i) {
                            $(this).val(data[$(this).attr('name')])
                        });
                        
                        renderRuleProperties(JSON.parse(data.updateRule));
                    } else {
                        //BUI.Message.Alert(data.msg, 'warning');
                    }
                },
                failure: function () {
                    BUI.Message.Alert('系统异常，请稍后操作', 'warning');
                }
            });
        }
    	
    	function renderRuleProperties(obj) {
    		
    		//"additionDiv", "common", additionConfigIndex++
    		function renderAdditions(containerId, prefix, additions) {
    			if (null == additions || 0 == additions.length) {
           			return;
        		}
    			
    			for (var j=0;j < additions.length; j++) {
    				var addition = additions[j];
    				
    				var index = additionConfigIndex++;
        			addAdditionRule(containerId, prefix, index, addition);
    			} 
    		}
    		
    		
    		//"customConfigDiv", customConfigIndex++
    		function renderCustom(containerId, customRules) {
    			if (null == customRules || 0 == customRules.length) {
           			return;
        		}
    			
    			for (var j=0;j < customRules.length; j++) {
    				var customRule = customRules[j];
    				var index = customConfigIndex++;
    				
        			addCustomConfig("customConfigDiv", index, customRule);
    			} 
    		}
    		
    		if (null == obj) {
    			return
    		}
    		
    		var commonRule = obj.commonRule;
    		var customRules = obj.customRules;
    		
    		renderAdditions("additionDiv", "common", commonRule.additions);
    		renderCustom("customConfigDiv", customRules);
    	}
    	
    	var columns = [
    		{title:'应用名称',dataIndex:'appName',width:'20%'},
    		{title:'一级渠道名称',dataIndex:'mainPackgage',width:'20%'},
            {title:'二级渠道名称',dataIndex:'subPackage',width:'20%'},
            {title:'一级渠道标识',dataIndex:'mainPackageCode',width:'20%'},
            {title:'二级渠道标识',dataIndex:'channelId',width:'20%'}
            ];
		
        var store = new BUI.Data.Store({
        	autoLoad : false,
        	data : [],
        	});
          
       	var grid = new BUI.Grid.Grid({
           	render:'#grid',
           	columns : columns,
           	height: 390,
           	itemStatusFields : { //设置数据跟状态的对应关系
             selected : 'selected',
             disabled : 'disabled'
           	},
           	store : store,
			plugins : [BUI.Grid.Plugins.CheckSelection]	// 插件形式引入多选表格
       	});
       	
       	grid.on('selectedchange', function (e) {
       		refreshChannelBottom();
        });
       	
       	function refreshChannelBottom() {
			var selectValues = grid.getSelection();
       		
       		var selectSize = 0;
       		var selectContent = "";
       		dialogSelectChannels = [];
       		
       		if (null != selectValues && 0 < selectValues.length) {
       			
       			for (var j=0;j < selectValues.length; j++) {
    				var selectValue = selectValues[j];
    				
    				if (true == isDisableChannel(selectValue.channelId)) {
    					continue;
    				}
    				
    				if ("" != selectContent) {
    					selectContent += ",";
    				}
    				selectSize++;
    				selectContent += selectValue.channelId;
    				
    				dialogSelectChannels.push(selectValue.channelId);
    			} 
    		}
       		
       		$("#channelListSelectSize").html(selectSize);
       		$("#channelListSelectContent").html(selectContent);
       	}
       	
       	function isDisableChannel(channelId) {
   			if (null == dialogDisableChannels || 0 == dialogDisableChannels.length) {
    			return false;
    		}
   			
   			for (var j=0;j < dialogDisableChannels.length; j++) {
				var disableValue = dialogDisableChannels[j];
				if (channelId == disableValue) {
					return true;
				}
			}
   			
   			return false;
   		}
       	
   
        grid.render();
        
        var searchForm = new BUI.Form.Form({
        	  srcNode : '#searchForm'
        });
        
        searchForm.render();

        
        function refreshChannelList(disableChannels, checkChannels) {
        	//var data = [{"appName":"借呀","firstChannelName":"百度手机助手","secondChannelName":"百度钱包","firstChannelCode":"baidu_helper","secondChannelCode":"baidu_wallet",selected : true},{"appName":"借呀","firstChannelName":"腾讯手机助手","secondChannelName":"腾讯钱包","firstChannelCode":"qq_helper","secondChannelCode":"qq_wallet",disabled:true}];
        	
        	function setDataProperties(data, disableChannels, checkChannels) {
        		
        		for (var i=0;i < data.length; i++) {
        			
        			var record = data[i];
        			
        			if (null != disableChannels && 0 < disableChannels.length) {
            			for (var j=0;j < disableChannels.length; j++) {
            				var disableValue = disableChannels[j];
            				if (record.channelId == disableValue) {
            					record.disabled=true;
            					record.selected=true;
            				}
            			}
            		}
        			
        			if (null != checkChannels && 0 < checkChannels.length) {
            			for (var j=0;j < checkChannels.length; j++) {
            				var checkValue = checkChannels[j];
            				if (record.channelId == checkValue) {
            					record.selected=true;
            				}
            			}
            		}
        		}
        		
        		return data;
        	}
        	
        	$.ajax({
                url: '${ctx}/mop/appVersion/channel/list',
                async: false,
                type: "POST",
                data: searchForm.serializeToObject(),
                dataType:'json',
                success: function (data) {
                	store.setResult(setDataProperties(data, disableChannels, checkChannels));
                	store.load();
                	refreshChannelBottom();
                },
                failure: function () {
                    BUI.Message.Alert('系统异常，请稍后操作', 'warning');
                }
            });
        	
        }
    	
        $("#btnSearch").click(function () {
        	refreshChannelList(dialogDisableChannels, dialogSelectChannels);
        });
        
        $("#btnClear").click(function () {
        	clearForm();
        });
        
        function clearForm() {
        	$(':input','#searchForm')
        	.not(':button,:submit,:reset,:hidden')
        	.val('')
        	.removeAttr('checked')
        	.removeAttr('checked');
    		$("#channelSort").val('');
        }
        
        var dialogChannelIndex = "";
        var dialogDisableChannels = [];
        var dialogSelectChannels = [];
        
        var channelDialog = new OverLay.Dialog({
            title : "选择渠道",
            width : "720",
            height : "600",
            contentId : "channelList",
            success : function() {
                
                var selectContent = "";
                
                for (var j=0;j < dialogSelectChannels.length; j++) {
    				var selectValue = dialogSelectChannels[j];
    				
    				if (true == isDisableChannel(selectValue)) {
    					continue;
    				}
    				
    				if ("" != selectContent) {
    					selectContent += ",";
    				}
    				selectContent += selectValue;
    			}
                
            	$("#customChannel_"+dialogChannelIndex).val(selectContent);
            	
            	this.hide();
            }
        });
        
    	function showChannelListDialog(index, disableChannels, checkChannels){
    		clearForm();
    		dialogChannelIndex = index;
    		dialogDisableChannels = disableChannels || [];
    		dialogSelectChannels = [];
    		refreshChannelList(disableChannels, checkChannels);
    		channelDialog.show();
    	}
    	
		refreshForm();
        
        commonRuleUpdateTypeSelect.render();
        appSelect.render();
        osTypeSelect.render();
        channelSortSelect.render();
        
        
        
        
        editForm = new Form.HForm({
            srcNode: '#detail',
            submitType : 'ajax',
			method : 'post',
			callback : function(data) {
				if(data.success){
					$("#id").val(data.result);
					showSuccess(data.msg);
					//location.href = $ctx + '/mop/appVersion/detail/index?id='+data.result;
	            }else {
	            	showWarning(data.msg);
	            }
			}
        });
        
        editForm.on('beforesubmit', function () {
			try{
				if (confirm('确定要更新吗') != true) {
	                return false;
	            }
	            
	            var appCode = $("#appCode").val();
			    if (null == appCode || "" == appCode) {
			    	showWarning("应用不能为空");
			    	return false;
			    }
			    
			    var osType = $("#osType").val();
			    if (null == osType || "" == osType) {
			    	showWarning("客户端不能为空");
			    	return false;
			    }
			    
			    var osType = $("#osType").val();
			    if (null == osType || "" == osType) {
			    	showWarning("客户端不能为空");
			    	return false;
			    }
			    
			    var insideVersion = $("#insideVersion").val();
			    if (null == insideVersion || "" == insideVersion) {
			    	showWarning("内部版本号不能为空");
			    	return false;
			    }
			    
			    var appVersion = $("#appVersion").val();
			    if (null == appVersion || "" == appVersion) {
			    	showWarning("外部版本号不能为空");
			    	return false;
			    }
			    
			    var updateNote = $("#updateNote").val();
			    if (null == updateNote || "" == updateNote) {
			    	showWarning("更新说明不能为空");
			    	return false;
			    }
			    
			    
			    
			    var flag = true;
	            
	            $(".defaultUpdateType").each(function(){
	        		var tmpValue = $(this).val();
				    if (null == tmpValue || "" == tmpValue) {
				    	showWarning("通用更新方式不能为空");
				    	flag = false;
				    	return;
				    }
				});
	            
	            if (false == flag) {
	            	return false;
	            }
	            
	            $(".updateType").each(function(){
	        		var tmpValue = $(this).val();
				    if (null == tmpValue || "" == tmpValue) {
				    	showWarning("更新方式不能为空");
				    	flag = false;
				    	return;
				    }
				});
	            
	            if (false == flag) {
	            	return false;
	            }
	            
	            $(".customChannelValue").each(function(){
	        		var tmpValue = $(this).val();
				    if (null == tmpValue || "" == tmpValue) {
				    	showWarning("渠道不能为空");
				    	flag = false;
				    	return;
				    }
				});
	            
	            if (false == flag) {
	            	return false;
	            }
	            
	            var updateRule = collectUpdateRule();
	            
	            $("#updateRule").attr("value", JSON.stringify(updateRule));
			}catch(err){
				return false;
			}
            
            return true;
        });
        
        
        function collectUpdateRule() {
            
            function collectAdditionRule(containerId) {
            	var additions = [];
            	$("#"+containerId+" .additionRule").each(function(){
                	var addition = {};
                	addition.beginVersion = $(this).find(".updateVersionBegin").val();
                	addition.endVersion = $(this).find(".updateVersionEnd").val();
                	addition.updateType = $(this).find(".updateType").val();
                	additions.push(addition);
    			});
            	return additions;
            }
            
            function collectChannelRule(containerId) {
            	var customRules = [];
            	$("#"+containerId+" .customChannelDiv").each(function(){
            		
            		var customRule={};
            		customRule.defaultUpdateType = $(this).find(".defaultUpdateType").val();
            		customRule.channels = [];
            		
            		var tmpChannels = $(this).find(".customChannelValue");
            		if (null != tmpChannels && "" != tmpChannels.val()) {
            			customRule.channels = tmpChannels.val().split(",");
            		}
            		
            		customRule.additions = collectAdditionRule("customAdditionDiv_"+$(this).attr("index"));
            		
            		customRules.push(customRule);
    			});
            	return customRules;
            }
            
            var updateRule = {"commonRule":{},"customRules":[]};
            updateRule.commonRule.defaultUpdateType = $("#commonRuleUpdateType").val();
            updateRule.commonRule.channels = [];
            updateRule.commonRule.additions = collectAdditionRule("additionDiv");
            updateRule.customRules = collectChannelRule("customConfigDiv");
            
            return updateRule;
        }

        editForm.render();
        
        
        /* 显示提示框 */
        function showWarning(str) {
            BUI.Message.Alert(str, 'warning');
        }

        function showSuccess(str) {
            BUI.Message.Alert(str, 'success');
        }
        
        
     // 不考虑字母
        function s2i(s) {
            return s.split('').reduce(function(a, c) {
                var code = c.charCodeAt(0);
                if (48<=code && code < 58) {
                    a.push(code-48);
                }
                return a;
            }, []).reduce(function(a, c) {
                return 10*a + c;
            }, 0);
        }
         
        function versionCmp(s1, s2) {
            var a = s1.split('.').map(function(s) {
                return s2i(s);
            });
            var b = s2.split('.').map(function(s) {
                return s2i(s);
            });
            var n = a.length < b.length ? a.length : b.length;
            for (var i = 0; i < n; i++) {
                if (a[i] < b[i]) {
                    return -1;
                } else if (a[i] > b[i]) {
                    return 1;
                }
            }
            if (a.length < b.length) return -1;
            if (a.length > b.length) return 1;
            var last1 = s1.charCodeAt(s1.length-1) | 0x20,
                last2 = s2.charCodeAt(s2.length-1) | 0x20;
            return last1 > last2 ? 1 : last1 < last2 ? -1 : 0;
        }

    });
</script>

</body>
</html>


