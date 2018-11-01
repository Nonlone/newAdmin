<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']" /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/import-static.jsp"%>
</head>
<style type="text/css">
.photo-idCardDataExtend{
	width:150px;
	height:150px
}
</style>
<body>
<div style="background-color:#FFFFFF">
<div style="background-color:#FFFFFF;width:1300px;position:relative;left: 10%">

    <div>
        <h3 style="background-color:#ADADAD" ><span style="font-size:20">身份信息</span></h3>
        <hr/>
        <table cellspacing="0" class="table table-bordered">
            <tbody>
            <tr>
                <td bgcolor="#F2F2F2" width="50px" height="30px">
                    姓名：
                </td>
                <td width="100px" height="30px">
                ${idCardDataExtend.name}
                </td>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    身份证号：
                </td>
                <td  width="100px" height="30px">
				${hyIdcard}
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    民族：
                </td>
                <td width="100px" height="30px">
                ${idCardDataExtend.nation}
                </td>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    性别：
                </td>
                <td width="100px" height="30px">
                ${idCardDataExtend.sex}
                </td>
            </tr>
            <tr>
				<td bgcolor="#F2F2F2" width="50px"  height="30px">
					用户ID：
				</td>
				<td width="100px" height="30px">
					<c:if test="${not empty user}">
						${user.id}
					</c:if>
				</td>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    年龄：
                </td>
                <td width="100px" height="30px">
           		${year}        
                </td>
            </tr>
            <tr>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    出生日期：
                </td>
                <td width="100px" height="30px">
                   ${birthday}
                </td>
                <td bgcolor="#F2F2F2" width="50px"  height="30px">
                    银行卡号：
                </td>
                <td width="100px" height="30px">
                    
                </td>
            </tr>
            
            </tbody>
        </table>
		<hr/>
    </div>
    <div>
		<h3 style="background-color:#ADADAD" ><span style="font-size:20">影像信息&nbsp; &nbsp; &nbsp; &nbsp;对比公安4K &nbsp; &nbsp;身份证相似度:${gongan}%  </span></h3>
        <hr/>
    	<table class="table">
            <tbody>
           		<tr>
    				<c:if test="${not empty photoIDCARD_EMBLEM}"><td><img class="photo-idCardDataExtend dialog" src="${photoIDCARD_EMBLEM}"></td></c:if>
    				<c:if test="${not empty photoIDCARD_HOLD}"><td><img class="photo-idCardDataExtend dialog" src="${photoIDCARD_HOLD}"></td></c:if>
    				<c:if test="${not empty photoIDCARD_EMBLEM}"><td><img class="photo-idCardDataExtend dialog" src="${photoIDCARD_EMBLEM}"></td></c:if>
    				<c:if test="${not empty photoIDCARD_POLICE}"><td><img class="photo-idCardDataExtend dialog" src="${photoIDCARD_POLICE}"></td></c:if>
    				<c:if test="${not empty photoBLINK}"><td><img class="photo-idCardDataExtend dialog" src="${photoBLINK}"></td></c:if>
    				<c:if test="${not empty photoMOUTH}"><td><img class="photo-idCardDataExtend dialog" src="${photoMOUTH}"></td></c:if>
    				<c:if test="${not empty photoSHAKE}"><td><img class="photo-idCardDataExtend dialog" src="${photo6}"></td></c:if>
    			</tr>
    			<tr>
    				<c:if test="${not empty photoIDCARD_PROTRAIT}"><td>身份证正面</td></c:if>
    				<c:if test="${not empty photoIDCARD_HOLD}"><td>手持照片</td></c:if>
    				<c:if test="${not empty photoIDCARD_EMBLEM}"><td>身份证反面</td></c:if>
    				<c:if test="${not empty photoIDCARD_POLICE}"><td>公安照片</td></c:if>
    				<c:if test="${not empty photoBLINK}"><td>活体照片1</td></c:if>
    				<c:if test="${not empty photoMOUTH}"><td>活体照片2</td></c:if>
    				<c:if test="${not empty photoSHAKE}"><td>活体照片3</td></c:if>
    			</tr>
    		</tbody>
    	</table>
    	<hr/>
    </div>
    
    <div>
    	<h3 style="background-color:#ADADAD" ><span style="font-size:20">基本信息</span></h3>
    	<hr/>
    	<table cellspacing="0" class="table table-bordered">
    	<tbody>
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">居住地</td>
    			<td width="250px">${person.address}</td>
    			<td width="120px"></td>
    		</tr>
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">婚姻状况</td>
    			<td width="250px">${marital}</td>
    			<td width="120px"></td>
    		</tr>
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">最高学历</td>
    			<td width="250px">${educationLevel}</td>
    			<td width="120px"></td>
    		</tr>
    		<!-- 
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">社会身份</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		 -->
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">手机号</td>
    			<td width="250px">${hyPhone}</td>
    			<td width="120px"></td>
    		</tr>
    		<!-- 
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">入网时长</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		 -->
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">下单IP所属地址</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">下单LBS定位地址</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		<!-- 
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">微信</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">邮箱</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">QQ</td>
    			<td width="250px"></td>
    			<td width="120px"></td>
    		</tr>
    		 -->
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">学历</td>
    			<td width="250px">${educationLevel}</td>
    			<td width="120px"></td>
    		</tr>
    		<!-- 
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">机器设备码</td>
    			<td width="250px"></td>
    			<td width="120px">历史关联账号:</td>
    		</tr>
    		 -->
    	</tbody>
    	</table>
    	<hr/>
    </div>
       	<!-- 
	<div>
	<h3 style="background-color:#ADADAD" ><span style="font-size:20">学历信息</span></h3>
		<hr/>
    	<table cellspacing="0" class="table table-bordered">
    	<tbody>
			<tr>
    			<td bgcolor="#F2F2F2"  width="80px">毕业院校</td>
    			<td width="150px"></td>
    			<td bgcolor="#F2F2F2"  width="80px">专业</td>
    			<td width="150px"></td>
    		</tr>  
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">学历类型</td>
    			<td width="150px"></td>
    			<td bgcolor="#F2F2F2"  width="80px">入学年份</td>
    			<td width="150px"></td>
    		</tr> 
    		<tr>
    			<td bgcolor="#F2F2F2"  width="80px">毕业时间</td>
    			<td width="150px"></td>
    			<td bgcolor="#F2F2F2"  width="80px">学历</td>
    			<td width="150px"></td>
    		</tr>  	
    	</tbody>
    	</table>
	<hr/> 
	</div>    
	 -->
	<div>
	<h3 style="background-color:#ADADAD" ><span style="font-size:20">单位信息</span></h3>
	<hr/>
    	<table cellspacing="0" class="table table-bordered">
    	<tbody>
			<tr>
    			<td bgcolor="#F2F2F2" width="100px">单位名称</td>
    			<td width="200px">${work.companyName}</td>
    		</tr>  
    		<tr>
    			<td bgcolor="#F2F2F2" width="100px">单位地址</td>
    			<td width="200px">${work.companyAddress}</td>
    		</tr>  
    		<tr>
    			<td bgcolor="#F2F2F2" width="100px">联系人手机</td>
    			<td width="200px">${work.contactPhone}</td>
    		</tr>  
    		<tr>
    			<td bgcolor="#F2F2F2" width="100px">联系人固话</td>
    			<td width="200px">(${contactTelephoneZoneCode})${work.contactTelephone}</td>
    		</tr>
    	</tbody>
    	</table>
	<hr/> 
	</div>   
</div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        //放大图片
        $('img.dialog').on('click',function () {
            var large_image = '<img class=\'closeImg\' src= ' + $(this).attr("src") + '></img>';
            BUI.use('bui/overlay',function(Overlay){
                var width = this.width;
                var height = this.height;
                var dialog = new Overlay.Dialog({
                    title:'图片放大',
                    width:width,
                    height:height,
                    mask:false,
                    buttons:[],
                    bodyContent:large_image
                });
                dialog.show();
            });
        });

    });
</script>
</html>