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
		<!-- 查询 -->
		<form id="searchForm" class="form-horizontal search-form">
			<div class="row">
				<div class="control-group span7">
					<label class="control-label">用户ID:</label>
					<div class="controls">
						<input type="text" maxlength="19" onkeyup="value=value.replace(/[^\d]/g,'')"  placeholder="请准确输入用户ID" class="input-normal control-text" name="userId">
					</div>
				</div>
				<div class="control-group span7">
					<label class="control-label">账号类型:</label>
					<div class="controls">
					<select name="type">
						<option value="" selected="selected">全部</option>
						<option value="1">个人账号</option>
						<option value="2">企业账号</option>
					</select>
						<!-- <input type="text" class="input-normal control-text" name="type"> -->
					</div>
				</div>
				<div class="control-group span7">
                    <label class="control-label">手机号:</label>
                    <div class="controls">
                        <input type="text" maxlength="11" onkeyup="value=value.replace(/[^\d]/g,'')"  placeholder="请准确输入用户手机" class="input-normal control-text" name="phone">
                    </div>
                </div>
                <div class="span1 offset2">
					<button type="button" id="btnSearch" class="button button-primary">搜索</button>
				</div>
				<div class="span1 offset2">
					<button class="button button-danger" onclick="flushall();">清空</button>
				</div>
				<div class="span1 offset2">
                    <button class="button" id="btnLogInfo">查看变更记录</button>
                </div>

			</div>
		</form>

		<div class="search-grid-container">
			<div id="grid"></div>
		</div>

		<div id="confirmContent" class="hide">
           <!--  <form id="searchInfoForm" class="form-inline"> -->
                 
         <!--    </form> -->
        </div>
        <input type="hidden" id="userId" value="">

		<div id="changeLog" class="hide">
            <span id="changeLogLink" class="page-action grid-command" data-href="${ctx}/admin/mop/partner/changeLog/list/index" title="变更记录">查看变更记录</span>
        </div>

        <div id="content" class="hide">
            <form id="rateForm" class="form-horizontal">
                <div class="row">
                    <label> 实时结算比例：</label>
                    <input name="im" id="imRate" type="text" data-rules="{fixNumber:true}"/> <span>%</span>
                </div>
                <br/>
                <div class="row" id="periodRate_start">
                    <label>定时结算比例 ：</label>
                    <br/>
                    <input id="p_st_start" name="start" readonly="readonly"  type="text" style="width:50px;" data-rules="{fixNumber:true}"/> <span>万元</span>
                    <span> ≤ </span>
                    <input id="p_st_end" name="end" type="text" style="width:50px;" data-rules="{fixNumber:true}"/> <span>万元</span>
                    &nbsp&nbsp&nbsp
                    <input id="p_st_rate" name="rate" type="text" style="width:50px;" data-rules="{fixNumber:true}"/> <span>%</span>
                </div>

                <div class="row" id="periodRate_mid">

                </div>

                <div class="row" id="periodRate_end">
                    <br/>
                    <span> ≥ </span>
                    <input id="p_end_start" name="start" type="text" style="width:50px;" data-rules="{fixNumber:true}"/> <span>万元</span>
                    &nbsp&nbsp&nbsp
                    <input id="p_end_rate" name="rate" type="text" style="width:50px;" data-rules="{fixNumber:true}"/> <span>%</span>
                </div>
                <br/>
                <a id="addRate" href="javascript:void(0)">+增加梯度</a>
            </form>
        </div>

	</div>
     <!-- 用于表格分组时 分组的列宽不生效的调整 -->
	  <table style="display: none;">
	      <thead id="gridWithTemplate">
	        <tr class="bui-grid-header-row">
				<td class=" grid-td-col1" width="20%" style="height: 0"></td>
				<td class=" grid-td-col2" width="20%" style="height: 0"></td>
				<td class=" grid-td-col3" width="15%" style="height: 0"></td>
				<td class=" grid-td-col4" width="25%" style="height: 0"></td>
				<td class=" grid-td-col5" width="20%" style="height: 0"></td>
				<td class="bui-grid-cell bui-grid-cell-empty">&nbsp;</td>
			</tr>
			</thead>
		</table>
	<script type="text/javascript">
		function flushall() {
			var elementsByTagName = document.getElementsByTagName("input");
			for (var i = 0; i < elementsByTagName.length; i++) {
				elementsByTagName[i].innerText = "";
			}
		}

		function del(obj) {
		    $(obj).parent().remove();
		}

		BUI.use([ 'bui/ux/crudgrid', 'bui/data', 'bui/grid','bui/overlay','bui/common/page','bui/form'], function(
				CrudGrid, Data, Grid,OverLay) {
			//定义页面权限
			var add = false, update = false, list = false, del = false;
			//"framwork:crudPermission"会根据用户的权限给add，update，del,list赋值
			<framwork:crudPermission resource="/admin/mop/partner"/>

            //查看变更记录
            $("#btnLogInfo").click(function(){
                 $("#changeLogLink").click();
            });

            var Form = BUI.Form;

            //添加校验规则
            BUI.Form.Rules.add({
                name : 'fixNumber',  //规则名称
                msg : '请填写非负,3位小数内的值。',//默认显示的错误信息
                validator : function(value,baseValue,formatMsg){ //验证函数，验证值、基准值、格式化后的错误信息
                  var regexp = new RegExp('^\\d+\\.?\\d{0,3}$');
                  if(value && value > 100){
                    return "不能超过100%";
                  }
                  if(value && !regexp.test(value)){
                    return formatMsg;
                  }
                }
            });

            var dyForm = new Form.Form({
                srcNode : '#rateForm',
                autoRender : true
            });

            dyForm.render();

            $("#addRate").click(function(){
                 var addStr = ' <div class="row" style="margin-left:5px;">' +
                              ' <br/>' +
                              ' <input name="start" type="text" placeholder="请输入" style="width:50px;" data-rules="{fixNumber:true}"/> <span>万元</span>' +
                              ' <span> ≤ </span>' +
                              ' <input name="end" type="text"  placeholder="请输入" style="width:50px;" data-rules="{fixNumber:true}"/> <span>万元</span>' +
                              ' &nbsp&nbsp&nbsp' +
                              ' <input name="rate" type="text" placeholder="请输入比率" style="width:60px;" data-rules="{fixNumber:true}"/> <span>%</span>' +
                              ' <a id="delRate" onclick="del(this)"> 删除 </a>' +
                              ' </div>'
                              ;
                 $("#periodRate_mid").append(addStr);

                 dyForm = new Form.Form({
                     srcNode : '#rateForm'
                 });

                 dyForm.render();
            });


			var Grid = Grid;
			Store = Data.Store;

			var columns = [ {
				title : '用户ID',
				dataIndex : 'userId',
				width : '20%'
			}, {
				title : '账号类型',
				dataIndex : 'type',
				width : '20%',
				renderer : function(value) {
					if (value == 1) {
						return "个人账号";
					} else if (value == 2) {
						return "企业账号";
					}
				}
			}, {
				title : '实时结算',
				dataIndex : 'im',
				width : '15%'
			}, {
				title : '定时结算',
				dataIndex : 'periods',
				width : '25%'
			}, {
				title : '操作',
				dataIndex : 'type',
				width : '20%',
				renderer : function(value) {
					if (value == 1) {
						return '<span class="grid-command btnType">切为企业账号</span> ';
					} else if (value == 2) {
						return '<span class="grid-command btnRate">修改结算比例</span>';
					}
				}
			}, {
			    title : '配置json',
                dataIndex : 'originalPeriod',
                visible : false
			}];

			var colGroup = new Grid.Plugins.ColumnGroup({
				groups : [ {
					title : '奖金结算比例',
					from : 2,
					to : 3
				} ]
			})

			var crudGrid = new CrudGrid({
				entityName : '账号配置信息',
				pkColumn : 'userId',//主键
				storeUrl : '${ctx}/admin/mop/partner/list',
				columns : columns,
				showAddBtn : false,
				showUpdateBtn : false,
				showRemoveBtn : false,
				gridPlugins : [ colGroup, Grid.Plugins.AutoFit ],
				storeCfg:{
                    proxy : {
                      pageStart : 1
                    }
                },
				gridCfg : {
					innerBorder : true
				}
			});
			var grid = crudGrid.get('grid');

            grid.on('cellclick',function (ev) {
                var record = ev.record;
                var target = $(ev.domTarget);
                $("#userId").val(record.userId);
                if (target.hasClass('btnType')) {
                    confirmDialog.show();
                } else if (target.hasClass('btnRate')) {
                    $('#periodRate_mid').children().remove();
                    var rateInfo = jQuery.parseJSON(record.originalPeriod);
                    $('#imRate').val(rateInfo.im * 100);
                    var len = rateInfo.period.length;
                    $('#p_end_start').val(rateInfo.period[len - 1].min / 10000);
                    $('#p_end_rate').val(rateInfo.period[len - 1].rate * 100);

                    $('#p_st_start').val(rateInfo.period[0].min / 10000);
                    $('#p_st_rate').val(rateInfo.period[0].rate * 100);
                    $('#p_st_end').val(rateInfo.period[0].max / 10000);

                    for(i = 1; i < len - 1; i++) {
                        $("#addRate").click();
                    }
                    var index = 1;
                    $('#periodRate_mid div').each(function (i) {
                         $(this).find("input:eq(0)").val(rateInfo.period[index].min / 10000);
                         $(this).find("input:eq(1)").val(rateInfo.period[index].max / 10000);
                         $(this).find("input:eq(2)").val(rateInfo.period[index].rate * 100);
                         index++;
                    });
                    rateDialog.show();
                }
            });
            //在表头内插入列的宽度模板使用分组内的列宽生效
			var tableHeaderHtml=$(".bui-grid-header table thead").html();
			$(".bui-grid-header table thead").html($("#gridWithTemplate").html()+tableHeaderHtml);
			
            var rateDialog = new OverLay.Dialog({
                title:'结算比例配置',
                width:600,
                height:500,
                contentId:'content',
                success:function () {
                  if(!dyForm.isValid()){
                        return false;
                  }
                  var data = {};
                  data.userId = $("#userId").val();
                  var settle = {};
                  settle.im = $('#imRate').val() / 100;
                  settle.period = [];
                  var period = {};
                  period.min =  $('#p_st_start').val() * 10000;
                  period.max =  $('#p_st_end').val() * 10000;
                  period.rate =$('#p_st_rate').val() / 100;
                  settle.period.push(period);

                  $('#periodRate_mid div').each(function (i) {
                     period = {};
                     period.min = $(this).find("input:eq(0)").val() * 10000;
                     period.max = $(this).find("input:eq(1)").val() * 10000;
                     period.rate = $(this).find("input:eq(2)").val() / 100;
                     settle.period.push(period);
                  });

                  period = {};
                  period.min = $('#p_end_start').val() * 10000;
                  period.max = -1;
                  period.rate = $('#p_end_rate').val() / 100;
                  settle.period.push(period);
                  data.settle = JSON.stringify(settle);
                  $.ajax({
                      url : '${ctx}/admin/mop/partner/rate/update',
                      async : false,
                      type : "POST",
                      data : data,
                      success : function(data) {
                          if (data.success) {
                              BUI.Message.Alert(data.msg,'success');
                              crudGrid.load(true);
                              rateDialog.close();
                          } else {
                              BUI.Message.Alert(data.msg,'warning');
                          }
                      },
                      failure : function() {
                          BUI.Message.Alert('系统异常，请稍后操作','warning');
                      }
                  });

                  $("#userId").val("");
                  $('#periodRate_mid').children().remove();

                  this.close();
                }
            });

            var confirmDialog = new OverLay.Dialog(
            {
                title : '确认定切为企业账号?',
                width : 250,
                height : 100, 
                 contentId : 'confirmContent',
                success : function() {
                    var data = {};
                    data.userId = $("#userId").val();
                    $.ajax({
                        url : '${ctx}/admin/mop/partner/type/update',
                        async : false,
                        type : "POST",
                        data : data,
                        success : function(data) {
                            $("#userId").val("");
                            if (data.success) {
                                BUI.Message.Alert(data.msg,'success');
                                crudGrid.load(true);
                                confirmDialog.close();
                            } else {
                                BUI.Message.Alert(data.msg,'warning');
                            }
                        },
                        failure : function() {
                            $("#userId").val("");
                            BUI.Message.Alert('系统异常，请稍后操作','warning');
                        }
                    });
                }
            });

			grid.render();
		});
	</script>

</body>
</html>

