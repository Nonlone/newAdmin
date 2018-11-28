<!DOCTYPE HTML>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../common/import-tags.jsp"%>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/dpl.css" rel="stylesheet"/>
<link href="http://g.alicdn.com/bui/bui/1.1.21/css/bs3/bui.css" rel="stylesheet"/>
<html>
<head>
    <title>资金方充值页面</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/import-static.jsp"%>
    <style type="text/css">
        .backgroundColor{
            background: #9ca0b1;
        }
    </style>
</head>
<body>
<div class="demo-content">
    <div class="row">
        <div class="row-fluid show-grid offset1">
            <div class="row" style="margin: 20px">
                <div class="control-group span9">
                    <label class="control-label span4"><h2>动账类型:</h2></label>
                    <div class="control-label span4" style="position:relative;left: -25px">
                        <select id="type" name="type">
                            <option value="" selected="selected">全部</option>
                            <option value="1" <c:if test="${type =='1'}">selected="selected"</c:if>>充值</option>
                            <option value="2" <c:if test="${type =='2'}">selected="selected"</c:if>>提现冻结</option>
                            <option value="3" <c:if test="${type =='3'}">selected="selected"</c:if>>提现拒绝解冻</option>
                            <option value="4" <c:if test="${type =='4'}">selected="selected"</c:if>>降额解冻</option>
                            <option value="5" <c:if test="${type =='5'}">selected="selected"</c:if>>提现取消解冻</option>
                            <option value="6" <c:if test="${type =='6'}">selected="selected"</c:if>>提现使用</option>
                        </select>
                    </div>
                    <div class="span3 offset1">
                        <button  type="button" id="btnSearch" class="button button-primary" onclick="searchDetail()">搜索</button>
                    </div>
                </div>
            </div>
            <table class="table table-head-bordered span20 span-first">
                <thead>
                <tr class="backgroundColor">
                    <th class="span1 span-first">序号</th>
                    <th class="span3">动账金额</th>
                    <th class="span3">动账类型</th>
                    <th class="span3">订单号</th>
                    <th class="span3">用户姓名</th>
                    <th class="span3">动账时间</th>
                    <th class="span3">动账后余额</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <%-- <c:when test="${fundAmountDetails.numberOfElements > 0}"> --%>
                    <c:when test="${fundAmountDetails.size > 0}">
                        <c:forEach var="fundAmountDetail" items="${fundAmountDetails.content}">
                            <tr>
                                <td>${fundAmountDetail.id}</td>
                                <c:choose>
                                    <c:when test="${fundAmountDetail.amount < 0}">
                                        <td style="color: #3c763d"><fmt:formatNumber type="number" value="${fundAmountDetail.amount}" pattern="0.00"></fmt:formatNumber></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><fmt:formatNumber type="number" value="${fundAmountDetail.amount}" pattern="0.00" minFractionDigits="2" /></td>
                                    </c:otherwise>
                                </c:choose>
                                <%--动账类型  ${fundAmountDetail.type}--%>
                                <td>
                                    <c:if test="${fundAmountDetail.type == '1'}">
                                        <c:out value="充值" />
                                    </c:if>
                                    <c:if test="${fundAmountDetail.type == '2'}">
                                        <c:out value="提现冻结" />
                                    </c:if>
                                    <c:if test="${fundAmountDetail.type == '3'}">
                                        <c:out value="提现拒绝解冻" />
                                    </c:if>
                                    <c:if test="${fundAmountDetail.type == '4'}">
                                        <c:out value="降额解冻" />
                                    </c:if>
                                    <c:if test="${fundAmountDetail.type == '5'}">
                                        <c:out value="提现取消解冻" />
                                    </c:if>
                                    <c:if test="${fundAmountDetail.type == '6'}">
                                        <c:out value="提现使用" />
                                    </c:if>
                                </td>
                                <td><c:out value="${fn:substring(fundAmountDetail.remark,5,fn:length(fundAmountDetail.remark) )}" /></td>
                                <td>${fundAmountDetail.operator}</td>
                                <td><fmt:formatDate value="${fundAmountDetail.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td><fmt:formatNumber type="number" value="${fundAmountDetail.balance}" pattern="0.00" minFractionDigits="2" /></td>
                                    <%--<td>${fundAmountDetail.remark}</td>--%>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4">没有相关充值记录</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <div class="pagination pull-left">
                <%-- 这里开始是页尾设置 --%>
                <%--总共页数：${fundCharges.totalPages} <br>--%>
                <%--记录总数：${fundCharges.totalElements} <br>--%>
                <%--当前页号：${fundCharges.number} <br>--%>
                <%--是否为首页：${fundCharges.first} <br>--%>
                <%--是否为尾页：${fundCharges.last} <br>--%>
                <%--每页显示的数量：${fundCharges.numberOfElements} <br>--%>
                <c:if test="${fundAmountDetails.size > 0}">
                    <ul>
                        <li>
                            <a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=0&type=${type}">首页 </a>
                        </li>
                        <c:choose>
                            <c:when test="${fundAmountDetails.first}">
                                <li><a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=0&type=${type}">&lt;&lt;上一页</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=${fundAmountDetails.number-1}&type=${type}">&lt;&lt;上一页</a></li>
                            </c:otherwise>
                        </c:choose>
                        <li class="disabled"><a href="#">第${fundAmountDetails.number + 1}页/共${fundAmountDetails.totalPages}页(共${fundAmountDetails.totalElements}条)</a></li>
                        <c:choose>
                            <c:when test="${fundAmountDetails.last}">
                                <li><a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=${fundAmountDetails.number}&type=${type}">下一页&gt;&gt;</a></li>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${(fundAmountDetails.number+1) eq fundAmountDetails.totalPages}">
                                        <li><a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=${fundAmountDetails.totalPages-1}&type=${type}">下一页&gt;&gt;</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=${fundAmountDetails.number+1}&type=${type}">下一页&gt;&gt;</a></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <li>
                            <a href="${ctx}/backend/fund/detail?fundId=${fundId}&page=${fundAmountDetails.totalPages-1}&type=${type}">尾页</a>
                        </li>
                    </ul>
                </c:if>
            </div>
        </div>
    </div>
    <!-- script end -->
</div>
</body>
</html>
<script>
  function searchDetail() {
      //1.获取动态类型值;
      var selectType =''+$("#type").find("option:selected").val();
      var url = '${ctx}/backend/fund/detail?fundId=${fundId}&type='+selectType;
      window.location.replace(url);
  }
</script>