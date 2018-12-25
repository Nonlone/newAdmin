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