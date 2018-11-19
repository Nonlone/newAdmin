package com.feitai.admin.mop.base.enums;

/**
 * 借款订单状态
 */
public enum LoanOrderStatus {
    /**
     * 初始化状态
     */
    INIT(0),
    /**
     * 新网注册审批中
     */
    XINWANG_REGISTER_APPROVE_HANDLING(1),
    /**
     * 新网注册审批拒绝
     */
    XINWANG_REGISTER_APPROVE_REJECT(2),
    /**
     * 提现审批处理中
     */
    APPROVE_HANDLING(10),
    /**
     * 提现被拒
     */
    APPROVE_REJECT(11),
    /**
     * 降额待确认
     */
    WAITING_REDUCE_CONFIRM(20),
    /**
     * 降额取消
     */
    REDUCE_CANCELED(21),
    /**
     * 待签约
     */
    WAITING_SIGN(30),
    /**
     * 银行风险审批中
     */
    BANK_APPROVE_HANDLING(31),
    /**
     * 银行风险审批拒绝
     */
    BANK_APPROVE_REJECT(32),
    /**
     * 待银行补件
     */
    BANK_WAITING_SUPPLY(33),
    /**
     * 银行待同步补件
     */
    BANK_WAITING_SYNC_SUPPLY(34),
    /**
     * 银行降额待确认
     */
    BANK_WAITING_REDUCE_CONFIRM(35),
    /**
     * 银行降额取消
     */
    BANK_REDUCE_CANCELED(36),
    /**
     * 银行待同步降额
     */
    BANK_WAITING_SYNC_REDUCE(37),
    /**
     * 放款处理中（由订单中心异步通知）
     */
    PAY_LOAN_HANDLING(40),
    /**
     * 放款拒绝
     */
    PAY_LOAN_REJECT(41),
    /**
     * 已放款/待还款/还款中,逾期在数据库体现为还款中
     */
    WAITING_REPAY(50),

    /**
     * 逾期
     */
    OVERDUE(60),

    /**
     * 提现取消(后台人为取消)
     */
    CANCELED(99),
    /**
     * 已结清
     */
    PAY_OFF(100);

    private Integer value;

    LoanOrderStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
