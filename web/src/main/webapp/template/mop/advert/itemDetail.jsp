<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../common/import-tags.jsp" %>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../common/import-static.jsp" %>
</head>
<body>

<form id="detail" class="form-horizontal" action="${ctx}/mop/advert/item/detail/saveOrUpdate" method="post">
    <input type="hidden" id="itemId" name="id" value="${id}">
    <h2 style="font-weight: bold;margin-left: 10px;">基本信息</h2>
    <div id="baseInfo" class="row">
        <div class="control-group span10">
            <label class="control-label"><s>*</s>内容名称：</label>
            <div class="controls">
                <input name="title" type="text" class="control-text" placeholder="请输入名称" data-rules="{required : true}">
            </div>
        </div>

        <div id="textContent" class="control-group span10">
            <label class="control-label"><s>*</s>文字内容：</label>
            <div class="controls">
                <input name="content" type="text" class="control-text">
            </div>
        </div>

        <!-- <div class="control-group span10">
            <label class="control-label">子标题：</label>
            <div class="controls">
                <input name="subTitle" type="text" class="control-text">
            </div>
        </div> -->

        <div class="control-group span10">
            <label class="control-label">备注：</label>
            <div class="controls">
                <input name="remark" type="text" class="control-text">
            </div>
        </div>
    </div>

    <div class="row">
            <div class="control-group span10">
                <label class="control-label"><s>*</s>关联模块:</label>
                <div id="s1" class="controls">
                    <input type="hidden" id="code" name="code" data-rules="{required : true}">
                </div>
            </div>

            <div class="control-group span10">
                <label class="control-label"><s>*</s>位置权重:</label>
                <div class="controls">
                    <input name="weight" type="text" class="control-text"  style="width: 200px;" placeholder="请输入数字,数字越小位置越靠前" data-rules="{number:true}">
                </div>
            </div>


            <div class="control-group span12">
                <label class="control-label"><s>*</s>展示日期：</label>
                <div class="controls bui-form-group" data-rules="{dateRange : true}">
                    <input type="text" name="beginTime" class="calendar calendar-time" data-tip="{text : '开始日期'}"><label>&nbsp;-&nbsp;</label>
                    <input type="text" name="endTime" class="calendar calendar-time" data-tip="{text : '结束日期'}">
                </div>
            </div>
    </div>

    <!-- <div class="row">
        <div class="control-group span10">
            <label class="control-label">播放时间(单位:秒)：</label>
            <div class="controls">
                <input name="playTime" type="text" class="control-text">
            </div>
        </div>

        <div class="control-group span10">
            <label class="control-label">总需播放次数：</label>
            <div class="controls">
                <input name="totalPlayCount" type="text" class="control-text">
            </div>
        </div>

        <div class="control-group span10">
            <label class="control-label">周期播放次数：</label>
            <div class="controls">
                <input name="perPlayCount" type="text" class="control-text">
            </div>
        </div>

        <div class="control-group span10">
            <label class="control-label">播放周期(单位:分钟)：</label>
            <div class="controls">
                <input name="perPlayUnit" type="text" class="control-text">
            </div>
        </div>
    </div> -->



    <h2 style="font-weight: bold;margin-left: 10px;">事件配置</h2>
    <input name="event" type="hidden" value="{}">
    <div class="row">
        <div class="control-group span10">
            <label class="control-label"><s>*</s>在APP上使用：</label>
            <div class="controls">
                <select name="appEventType">
                    <option value="">无</option>
                    <option value="link">跳转</option>
                </select>
            </div>
        </div>

        <div class="control-group span10">
            <label class="control-label">APP链接：</label>
            <div class="controls">
                <input name="appLink" type="text" class="control-text" style="width: 260px;">
            </div>
        </div>

        <!-- <div class="control-group span10">
            <label class="control-label">APP参数：</label>
            <div class="controls">
                <textarea name="appParams" type="text" class="control-text"
                          style="width: 260px;height: 60px;"></textarea>
            </div>
        </div> -->
    </div>

    <div class="row" style="margin-top: 30px;">
        <div class="control-group span10">
            <label class="control-label"><s>*</s>在H5上使用：</label>
            <div class="controls">
                <select name="h5EventType">
                    <option value="">无</option>
                    <option value="link">跳转</option>
                </select>
            </div>
        </div>

        <div class="control-group span10">
            <label class="control-label">H5链接：</label>
            <div class="controls">
                <input name="h5Link" type="text" class="control-text" style="width: 260px;">
            </div>
        </div>

        <!-- <div class="control-group span10">
            <label class="control-label">H5参数：</label>
            <div class="controls">
                <textarea name="h5Params" type="text" class="control-text"
                          style="width: 260px;height: 60px;"></textarea>
            </div>
        </div> -->
    </div>
    <div id="advance">
    <h2 style="font-weight: bold;margin-left: 10px;margin-top: 30px;">样式配置</h2>
    <input name="style" type="hidden" value="{}">
    <div class="row">
        <div class="control-group span11">
            <label class="control-label">APP样式配置：</label>
            <div class="controls">
                <textarea name="appStyle" type="text" class="input-large"></textarea>
            </div>
        </div>

        <div class="control-group span11">
            <label class="control-label">H5样式配置：</label>
            <div class="controls">
                <textarea name="h5Style" type="text" class="input-large"></textarea>
            </div>
        </div>
    </div>

    <h2 style="font-weight: bold;margin-left: 10px;margin-top: 30px;">扩展参数配置</h2>
    <input name="ext" type="hidden" value="{}">
    <div class="row">
        <div class="control-group span11">
            <label class="control-label">APP扩展参数：</label>
            <div class="controls">
                <textarea name="appExt" type="text" class="input-large"></textarea>
            </div>
        </div>
        <div class="control-group span11">
            <label class="control-label">H5扩展参数：</label>
            <div class="controls">
                <textarea name="h5Ext" type="text" class="input-large"></textarea>
            </div>
        </div>
    </div>

    <h2 style="font-weight: bold;margin-left: 10px;margin-top: 30px;">匹配条件配置</h2>
    <input name="matchConfig" type="hidden" value="{}">
    <div class="row">
        <div class="control-group span11">
            <label class="control-label">APP内部版本号：</label>
            <div class="controls">
                <textarea name="matchAppVersions" type="text" class="input-large"></textarea>
            </div>
        </div>
        <div class="control-group span11">
            <label class="control-label">系统类型：</label>
            <div class="controls">
                <textarea name="matchOsTypes" type="text" class="input-large"></textarea>
            </div>
        </div>
    </div>

    <div class="row" style="margin-left: 10px;margin-top: 30px;padding-left: 5px;">
        <p style="color: red">APP内部版本号(多个使用英文逗号分隔)：V1.1.1+代表V1.1.1及以上的版本；V1.1.1-代表V1.1.1及以下的版本；V1.1.1~V1.2.1代表V1.1.1到V1.2.1之间的版本(包含V1.1.1和V1.2.1版本)</p>
        <p style="color: red">系统类型取值范围(多个使用英文逗号分隔)：ANDROID/IOS/H5</p>
    </div>
    </div>

    <div class="row actions-bar" style="margin-top: 50px;">
        <div class="form-actions span13 offset1">
            <button id="submit" type="submit" class="button button-primary">保存</button>
            <button type="button" id="btnShow" class="button">显示高级配置</button>
        </div>
    </div>
</form>

<div id="img" class='hide' style="margin-top: 20px;">
    <img id="pic" style="max-height: 100%;max-width: 100%;"/>
</div>

<div class="img-content">
    <div class="row">
        <div class="span13 offset1">
            <div id="J_Uploader">
            </div>
        </div>
    </div>
    <div class="row">
        <div class="span30">
            <div id="grid">
            </div>
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

    BUI.use(['bui/form', 'bui/ux/savedialog', 'bui/overlay', 'bui/grid', 'bui/uploader'], function (Form, Overlay, Grid) {
        var id = $("#itemId").val();
        var uploadParam = {};
        if (id != '') {
            uploadParam.itemId = id;
        }
        console.log(uploadParam)
        var setDefaultUrl = '${ctx}/mop/advert/item/detail/file/setDefault'
        var deleteUrl = '${ctx}/mop/advert/item/detail/file/delete'

        $('#btnShow').on('click',function () {
            if ($(this).text() == '显示高级配置') {
                $("#advance").show();
                $(this).text('收起高级配置')
            } else {
                $("#advance").hide();
                $(this).text('显示高级配置')
            }
        });

        $("#advance").hide();

        //动态显示组件
        var itemType = '${type}';

        console.log(itemType);
        if ('NOTICE' == itemType) {
           $("#textContent").show();
           $("#J_Uploader").hide();
           $("#grid").hide();
        } else {
           $("#textContent").hide();
        }

        var uploader = new BUI.Uploader.Uploader({
            render: '#J_Uploader',
            url: '${ctx}/mop/advert/item/upload',
            name: 'files',
            data: uploadParam,
            success: function (result) {
                showSuccess('上传成功');
                refreshForm();
                gridStore.load();
            },
            error: function (result) {
                showWarning('上传失败');
            },
            isSuccess: function (result) {
                console.log(result);
                return true;
            }
        }).render();

        $("#J_Uploader").click(function(){
          if ('' == $("#itemId").val()) {
            showWarning("上传图片前，请先保存基本信息");
            return false;
          }
        });

        function refreshForm() {
            $.ajax({
                url: '${ctx}/mop/advert/item/detail' + "?id=" + id,
                async: false,
                type: "POST",
                success: function (data) {
                    if (data) {
                        $('#detail :input').each(function (i) {
                            $(this).val(data[$(this).attr('name')])
                        });
                    } else {
                        //BUI.Message.Alert(data.msg, 'warning');
                    }
                },
                failure: function () {
                    BUI.Message.Alert('系统异常，请稍后操作', 'warning');
                }
            });
        }

        refreshForm();


        var form = new Form.HForm({
            srcNode: '#detail'
        });

        form.on('beforesubmit', function () {

            if (confirm('确定要更新吗，需对该模块操作“发布更新”才能将修改的内容生效到线上') != true) {
                return false;
            }

            collectMatchConfig();
            collectEvent();
            commonCollectJsonField('style');
            commonCollectJsonField('ext');
            if ('NOTICE' == itemType) {
                var data = $("#textContent").val();
                if ($.isEmptyObject(data)) {
                    showWarning("公告文字内容不能为空");
                    return false;
                }
            }
            return true;
        });



        form.render();

        var selectStore = new BUI.Data.Store({
            url: '${ctx}/mop/advert/item/blockItems',
            autoLoad: true
        });

        var select = new BUI.Select.Select({
            render: '#s1',
            width:'165',
            valueField: '#code',
            store: selectStore,
            multipleSelect: false
        });

        select.render();

        select.on('change', function(ev){
              if ("首页公告" == ev.text) {
                    $("#textContent").show();
              } else {
                    $("#textContent").hide();
              }
        });

        var editing = new BUI.Grid.Plugins.CellEditing({
                  triggerSelected : false //触发编辑的时候不选中行
        });

        var columns = [
            {title: '类型', dataIndex: 'type', width: '10%'},
            {title: '连接', dataIndex: 'url', width: '40%', renderer: function (value, obj) {
                var html = value;
                if ("image" == obj.type) {
                    html = "<img title='查看原图' src='"+value+"' class='btn2' style='max-width: 260px;max-height: 100px;cursor:pointer;'/>";
                }
                return html;
            }},
            {title: '比例', dataIndex: 'ratio', width: '10%'},
            {title: '分辨率', dataIndex: 'dpi', width: '10%', editor:{xtype : 'text'}},
            {title: '否是默认项', dataIndex: 'defaultItem', width: '10%'},
            {
                title: '操作', dataIndex: 'op', width: '10%', renderer: function (value, obj) {
                    var str = '<span class="grid-command btn3"> 删除 </span>'
                    if (!obj.defaultItem) {
                        str += '<span class="grid-command btn1"> 设为默认 </span>';
                    }
                    return str;
                }
            }
        ];


        var gridStore = new BUI.Data.Store({
            url: '${ctx}/mop/advert/item/detail/file?id=' + id,
            autoLoad: true
        });

        var grid = new BUI.Grid.Grid({
            render: '#grid',
            columns: columns,
            store: gridStore,
            plugins : [editing]
        });

        grid.on('cellclick', function (ev) {
            var sender = $(ev.domTarget); //点击的Dom
            var target = ev.record;
            var clickDom = sender.attr('id');
            console.info(target);
            if (sender.hasClass('btn1')) {
                var data = {};
                data.url = target.url;
                data.id = id;
                simpleHttp(setDefaultUrl, data);
                gridStore.load();
            } else if (sender.hasClass('btn2')) {
                $('#pic').attr('src', target.url);
                dialog.show();
            } else if (sender.hasClass('btn3')) {
                var data = {};
                data.url = target.url;
                data.id = id;
                simpleHttp(deleteUrl, data);
                gridStore.load();
            }
        });

        grid.render();


        function collectMatchConfig() {
            var defaultConfig = {};

            var matchAppVersions =  $("#detail :input[name='matchAppVersions']").val();
            var matchOsTypes =  $("#detail :input[name='matchOsTypes']").val();

            if ($.isNotBlank(matchAppVersions)) {
                matchAppVersions = $.trim(matchAppVersions)
                defaultConfig.appVersions = matchAppVersions;
            }

            if ($.isNotBlank(matchOsTypes)) {
                matchOsTypes = $.trim(matchOsTypes)
                defaultConfig.osTypes = matchOsTypes;
            }

            var matchConfig = {"defaultConfig":defaultConfig};
            $("#detail :input[name='matchConfig']").attr("value", JSON.stringify(matchConfig));
        }

        function collectEvent() {
            //事件配置
            var appEvent = {};

            appEvent.type = $("#detail :input[name='appEventType']").val();
            appEvent.link = $("#detail :input[name='appLink']").val();
            appEvent.params = {};

            var appParams =  $("#detail :input[name='appParams']").val();
            if ($.isNotBlank(appParams)) {
                appEvent.params = JSON.parse(appParams);
            }

            var h5Event = {};
            h5Event.type = $("#detail :input[name='h5EventType']").val();
            h5Event.link = $("#detail :input[name='h5Link']").val();
            h5Event.params = {};

            var h5Params =  $("#detail :input[name='h5Params']").val();
            if ($.isNotBlank(h5Params)) {
                h5Event.params = JSON.parse(h5Params);
            }

            var event = {"h5":h5Event, "app":appEvent};
            $("#detail :input[name='event']").attr("value", JSON.stringify(event));
        }

        function commonCollectJsonField(fieldName) {

            var newFieldName = upperFirstLetter(fieldName);

            var appJson = {};
            var appJsonValue =  $("#detail :input[name='app"+newFieldName+"']").val();
            if ($.isNotBlank(appJsonValue)) {
                appJson = JSON.parse(appJsonValue);
            }

            var h5Json = {};
            var h5JsonValue =  $("#detail :input[name='h5"+newFieldName+"']").val();
            if ($.isNotBlank(h5JsonValue)) {
                h5Json = JSON.parse(h5JsonValue);
            }

            var valueJson = {"h5":h5Json, "app":appJson};
            $("#detail :input[name='"+fieldName+"']").attr("value", JSON.stringify(valueJson));
        }

        function upperFirstLetter(str){
            return str.replace(/\b\w+\b/g, function(word) {
                return word.substring(0,1).toUpperCase( ) +  word.substring(1);
            });
        }

        /* 显示提示框 */
        function showWarning(str) {
            BUI.Message.Alert(str, 'warning');
        }

        function showSuccess(str) {
            BUI.Message.Alert(str, 'success');
        }

        function simpleHttp(url, data) {
            $.ajax({
                url: url,
                data: data,
                async: false,
                success: function (data) {
                    if (data.success) {
                        showSuccess(data.msg);
                    } else {
                        showWarning(data.msg);
                    }
                },
                failure: function () {
                    showWarning('系统异常，请稍后操作');
                }
            });
        };

        var dialog = new BUI.Overlay.Dialog({
            title: '',
            width: 900,
            height: 600,
            contentId: 'img',
            success: function () {
                this.hide();
            }
        });


    });
</script>

</body>
</html>


