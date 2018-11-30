<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="../../../common/import-tags.jsp" %>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title><spring:eval expression="@webConf['admin.title']"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@include file="../../../common/import-static.jsp" %>
</head>
<body data-userId="${user.id}" style="width:1500px;max-width:1500px;">
<div style="background-color:#FFFFFF;margin: 10px auto;">
    <div>
        <h3 style="background-color:#ADADAD"><span style="font-size:20px;padding: 5px;">新网授信数据</span></h3>
        <div>
            <table cellspacing="0" class="table table-bordered" style="margin-top: 15px;">
                <tbody>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                       持股比例：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">                       
                       <c:choose>
                          <c:when test="${empty attachUserBusiInfor.shareholdingRatio}">
                          未填写
                          </c:when>
                          <c:otherwise>${attachUserBusiInfor.shareholdingRatio}</c:otherwise>
                       </c:choose>                       
                    </td>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                        年度毛利润：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                         <c:choose>
                          <c:when test="${empty attachUserBusiInfor.annualProfit}">
                          未填写
                          </c:when>
                          <c:otherwise>${attachUserBusiInfor.annualProfit}</c:otherwise>
                       </c:choose>  
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        经营主体年收入：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle; ">
                        <c:choose>
                          <c:when test="${empty attachUserBusiInfor.mainAnnualIncome}">
                          未填写
                          </c:when>
                          <c:otherwise>${attachUserBusiInfor.mainAnnualIncome}</c:otherwise>
                       </c:choose>  
                    </td>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle; ">
                       近六个月营业额：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:choose>
                          <c:when test="${empty attachUserBusiInfor.sixMonthsTotalTurnover}">
                          未填写
                          </c:when>
                          <c:otherwise>${attachUserBusiInfor.sixMonthsTotalTurnover}</c:otherwise>
                       </c:choose>  
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        近六个月订单量：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle; ">
                       <c:choose>
                          <c:when test="${empty attachUserBusiInfor.sixMonthsOrderQuantity}">
                          未填写
                          </c:when>
                          <c:otherwise>${attachUserBusiInfor.sixMonthsOrderQuantity}</c:otherwise>
                       </c:choose>  
                    </td>
                     <td bgcolor="#F2F2F2" width="50px" height="30px" style="vertical-align: middle;">
                        创建时间：
                    </td>
                    <td width="100px" height="30px" style="vertical-align: middle;">
                        <c:choose>
                          <c:when test="${empty attachUserBusiInfor.createdTime}">
                          未填写
                          </c:when>
                          <c:otherwise>${attachUserBusiInfor.createdTime}</c:otherwise>
                       </c:choose>  
                    </td>  
                </tr>

                </tbody>
            </table>
        </div>
         <c:if test="${not empty tobaccoPhoto}">
            <div style="margin-top: 20px;">
                <h3 style="background-color:#ADADAD">
                    <span style="font-size:20px;padding: 5px;">影像信息</span>
                </h3>
                <br/>
                    <div>
                        <c:forEach items="${tobaccoPhoto}" var="tobacco">
                            <div style="float: left;margin: auto 20px;">
                                <div>
                                    <img style="max-height: 200px;max-width: 200px;"
                                         class="photo-${tobacco.typeName} dialog" src="${tobacco.path}">
                                </div>
                                <div style="text-align: center;margin-top: 5px;margin-bottom: 5px;">
                                    <span>${tobacco.name}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div style="clear: both"></div>
            </div>
        </c:if>
    </div>
</div>
</body>
<script type="text/javascript">
    $(function () {
        //放大图片
        $('img.dialog').on('click', function () {
            var large_image = '<img style=\'max-height: 800px;max-width: 800px\' class=\'closeImg\' src= ' + $(this).attr("src") + '></img>';
            BUI.use('bui/overlay', function (Overlay) {
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