package com.feitai.admin.mop.withdraworder.vo;

import lombok.Data;

/**
 * @Author qiuyunlong
 */
@Data
public class DownloadInfo {
    private int total;
    private int downloaded;

    public void increase() {
        ++downloaded;
    }
}
