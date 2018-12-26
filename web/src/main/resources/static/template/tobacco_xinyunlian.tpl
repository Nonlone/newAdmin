<style type="text/css">
    ul{margin:0;padding:0;list-style:none;}
    .table{}
    .table-row-group{display:table-row-group;}
    .table-row{display:table-row;}
    .table-row-group .table-row:hover,.table-footer-group .table-row:hover{background:#f6f6f6}
    .table-cell{display:table-cell;padding:0 5px;border:1px solid #ccc; height: 30px;width:30px;text-align:center;vertical-align:middle;}
    .table-span{text-align:center;}
</style>
<div style="margin:10px auto;padding-left:20px;padding-top:10px;">
<div>

        <div>
            <h1 style="display:inline-block;margin-right:10px;">客户信息</h1>
        </div>
      <div style="margin-bottom: 40px">
        <div style="display:table;border-collapse:collapse;border:1px solid #ccc;width: 1200px;">
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">数据采集时间</li>
                    <li class="table-cell">{{createDateTime}}</li>
                    <li class="table-cell">客户登录时手机号</li>
                    <li class="table-cell">{{customerLoginPhone}}</li>
                    <li class="table-cell">客户名称</li>
                    <li class="table-cell">{{customerName}}</li>
                </ul>
            </div>
             <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">客户简称</li>
                    <li class="table-cell">{{abbreviationName}}</li>
                    <li class="table-cell">客户编码</li>
                    <li class="table-cell">{{customerID}}</li>
                    <li class="table-cell">客户简码</li>
                    <li class="table-cell">{{customerSimpleCode}}</li>
                </ul>
            </div>  
               <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">许可证号</li>
                    <li class="table-cell">{{licenseKey}}</li>
                    <li class="table-cell">客户电话</li>
                    <li class="table-cell">{{customerPhone}}</li>
                    <li class="table-cell">负责人</li>
                    <li class="table-cell">{{responsiblePerson}}</li>
                </ul>
            </div> 
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">负责人电话</li>
                    <li class="table-cell">{{responsiblePersonPhone}}</li>
                    <li class="table-cell">地址</li>
                    <li class="table-cell">{{customerAddress}}</li>
                    <li class="table-cell">营销中心</li>
                    <li class="table-cell">{{marketingCenter}}</li>
                </ul>
            </div>
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">营销部</li>
                    <li class="table-cell">{{marketingDepartment}}</li>
                    <li class="table-cell">客户经理</li>
                    <li class="table-cell">{{customerManager}}</li>
                    <li class="table-cell">客户经理联系电话</li>
                    <li class="table-cell">{{customerManagerPhone}}</li>
                </ul>
            </div> 
             <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">零售业态</li>
                    <li class="table-cell">{{retailFormat}}</li>
                    <li class="table-cell">市场类别</li>
                    <li class="table-cell">{{marketCategory}}</li>
                    <li class="table-cell">经营规模</li>
                    <li class="table-cell">{{customerScale}}</li>
                </ul>
            </div> 
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">档口</li>
                    <li class="table-cell">{{customerStall}}</li>
                    <li class="table-cell">开户银行</li>
                    <li class="table-cell">{{customerBank}}</li>
                    <li class="table-cell">银行卡号</li>
                    <li class="table-cell">{{customerBankCard}}</li>
                </ul>
            </div>
        </div>
     
        </div>
    </div>
    <div>

        <div>
            <h1 style="display:inline-block;margin-right:10px;">订单信息(显示时间最新的10条订单数据)</h1>
        </div>
        {{each orders as order i}}
        {{if i<10}}
      <div style="margin-bottom: 40px">
        <div style="display:table;border-collapse:collapse;border:1px solid #ccc;width: 1200px;">
            <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">订单编号</li>
                    <li class="table-cell">{{order.orderNo}}</li>
                    <li class="table-cell">订单日期</li>
                    <li class="table-cell">{{order.orderDate}}</li>
                    <li class="table-cell">订单状态</li>
                    <li class="table-cell">{{order.orderStatus}}</li>
                </ul>
            </div>
             <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">订单总数量</li>
                    <li class="table-cell">{{order.orderTotal}}</li>
                    <li class="table-cell">订单总金额</li>
                    <li class="table-cell">{{order.orderTotalAmount}}</li>
                    <li class="table-cell"></li>
                    <li class="table-cell"></li>
                </ul>
            </div>           
        </div>
       <!-- <div style="display:table;border-collapse:collapse;border:1px solid #ccc;width: 900px;">
           <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">商品编号</li>
                    <li class="table-cell">商品名称</li>
                    <li class="table-cell">批发价</li>
                    <li class="table-cell">指导价,零售价</li>
                    <li class="table-cell">需求量</li>
                    <li class="table-cell">订购量</li>
                    <li class="table-cell">香烟类型</li>
                </ul>
            </div>
            {{each order.orderDetail as orderDetail i}}
             <div class="table-row-group">
                <ul class="table-row">
                    <li class="table-cell">{{orderDetail.productID}}</li>
                    <li class="table-cell">{{orderDetail.productName}}</li>
                    <li class="table-cell">{{orderDetail.tradePrice}}</li>
                    <li class="table-cell">{{orderDetail.guidancePrice}}</li>
                    <li class="table-cell">{{orderDetail.requirement}}</li>
                    <li class="table-cell">{{orderDetail.orderQuantity}}</li>
                    <li class="table-cell">{{orderDetail.cigaretteType}}</li>
                </ul>
            </div>
            {{/each}}
        </div>-->
        </div>
        {{/if}}
        {{/each}}
    </div>

  

</div>