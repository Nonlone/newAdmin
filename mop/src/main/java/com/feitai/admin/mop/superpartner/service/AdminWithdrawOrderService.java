package com.feitai.admin.mop.superpartner.service;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.feitai.admin.mop.base.BusinessException;
import com.feitai.admin.mop.base.RpcResult;
import com.feitai.admin.mop.base.ZipUtil;
import com.feitai.admin.mop.superpartner.dao.entity.OrderReceiverInfo;
import com.feitai.admin.mop.superpartner.dao.entity.WithdrawOrder;
import com.feitai.admin.mop.superpartner.dao.mapper.OrderReceiverInfoMapper;
import com.feitai.admin.mop.superpartner.dao.mapper.WithdrawOrderMapper;
import com.feitai.admin.mop.superpartner.enums.SuperPartnerOrderStatusEnum;
import com.feitai.admin.mop.superpartner.request.WithdrawOrderUpdateRequest;
import com.feitai.admin.mop.superpartner.vo.DownloadInfo;
import com.feitai.admin.mop.superpartner.vo.WithdrawOrderExcelModel;
import com.feitai.utils.http.OkHttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author qiuyunlong
 */
@Slf4j
@Service
public class AdminWithdrawOrderService {

    private static final String AUDIT_TYPE_NORMAL = "audit";

    private static final String AUDIT_TYPE_MODIFY = "modify";

    private static final int DOWNLOAD_WARNING_LIMIT = 5000;

    private final Map<String, DownloadInfo> downloadInfoMap = new ConcurrentHashMap<>();

    @Value("${TMP_FILE_PATH:withdrawOrder_tmp/}")
    private String TMP_FILE_PATH;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    @Value("${mop.server.orderUpdate.url}")
    private String orderUpdateUrl;

    //使用项目相对路径，创建临时文件夹 linux下可能会有权限问题
    @PostConstruct
    public void init() throws IOException {
        Path tmp = Paths.get(TMP_FILE_PATH);
        if (!tmp.toFile().exists()) {
            try {
                Files.createDirectory(tmp);
                log.info("tmp dir {} create successful", tmp.toAbsolutePath());
            } catch (IOException e) {
                log.info("tmp dir {} create fail", tmp.toAbsolutePath(), e);
                throw  e;
            }
        } else {
            log.info("tmp dir {} already exists", tmp.toAbsolutePath());
        }
    }

    @Autowired
    private WithdrawOrderMapper withdrawOrderMapper;
    @Autowired
    private OrderReceiverInfoMapper orderReceiverInfoMapper;


    public List<WithdrawOrder> listWithdrawOrder( Long userId, Integer status, Long orderId, Date startTime, Date endTime) {
        Example example = new Example(WithdrawOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andEqualTo("partnerUserId", userId);
        }
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        if (orderId != null) {
            criteria.andEqualTo("id", orderId);
        }
        if (startTime != null) {
            criteria.andGreaterThanOrEqualTo("applyTime", startTime);
        }
        if (endTime != null) {
            criteria.andLessThanOrEqualTo("applyTime", endTime);
        }
        example.orderBy("createdTime").desc();
        return withdrawOrderMapper.selectByExample(example);
    }

    public OrderReceiverInfo getOrderReceiverInfo(long orderId) {
        Example example = Example.builder(OrderReceiverInfo.class)
                .where(Sqls.custom()
                        .andEqualTo("orderId", orderId))
                .build();
        return orderReceiverInfoMapper.selectOneByExample(example);
    }

    public boolean updateWithdrawOrderStatus(long userId, long orderId, int status, String operator, String remark, String type) throws IOException {

        if (SuperPartnerOrderStatusEnum.AUDIT_PASS.getValue() != status && SuperPartnerOrderStatusEnum.AUDIT_REJECT.getValue() != status) {
            throw new BusinessException("提交了非法审核状态");
        }

        RpcResult result = doUpdateWithdrawOrderStatus(userId, orderId, status, operator, remark, type);
        if (result.isSuccess()) {
            return true;
        } else {
            throw new BusinessException(result.getMessage());
        }
    }

    private RpcResult doUpdateWithdrawOrderStatus(long userId, long orderId, int status, String operator, String remark, String type) throws IOException {
        WithdrawOrderUpdateRequest updateRequest = new WithdrawOrderUpdateRequest();
        updateRequest.setOperator(operator);
        updateRequest.setOrderId(orderId);
        updateRequest.setRemark(remark);
        updateRequest.setStatus(status);
        updateRequest.setUserId(userId);
        updateRequest.setType(type);
        String resultStr = OkHttpClientUtils.postReturnBody(orderUpdateUrl, JSON.toJSONString(updateRequest));
        return JSON.parseObject(resultStr, RpcResult.class);
    }

    public List<WithdrawOrder> listWithReceiveInfo(Long userId, Integer status, Long orderId, Date startTime, Date endTime) {
        Map param = new HashMap();
        if (userId != null) {
            param.put("userId", userId);
        }
        if (status != null) {
            param.put("status", status);
        }
        if (orderId != null) {
            param.put("id", orderId);
        }
        if (startTime != null) {
            param.put("applyTime", startTime);
        }
        if (endTime != null) {
            param.put("applyTime", endTime);
        }
        return withdrawOrderMapper.listWithReceiveOrder(param);
    }

    public boolean isLargeDownloadData(Long userId, Integer status, Long orderId, Date startTime, Date endTime) {
        Map param = new HashMap();
        if (userId != null) {
            param.put("userId", userId);
        }
        if (status != null) {
            param.put("status", status);
        }
        if (orderId != null) {
            param.put("id", orderId);
        }
        if (startTime != null) {
            param.put("applyTime", startTime);
        }
        if (endTime != null) {
            param.put("applyTime", endTime);
        }
        return withdrawOrderMapper.countWithdrawOrder(param) > DOWNLOAD_WARNING_LIMIT;
    }

    public File createWithdrawOrderFile(Long userId, Integer status, Long orderId, Date startTime, Date endTime, String sessionId){
        List<WithdrawOrder> list = listWithReceiveInfo(userId, status, orderId, startTime, endTime);
        if (list.isEmpty()) {
            throw new BusinessException("没有可导出的数据");
        }
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setTotal(list.size() + 1);
        downloadInfoMap.put(sessionId, downloadInfo);
        List<WithdrawOrderExcelModel> excelModelList = new ArrayList<>(list.size());
        List<Path> pathList = new ArrayList<>(list.size() * 2);
        String currentPath = TMP_FILE_PATH + System.currentTimeMillis() + "/";
        String currentPicPath = currentPath + "image/";

        try {
            log.info(Paths.get(currentPath).toFile().getAbsolutePath());
            Files.createDirectory(Paths.get(currentPath));
            Files.createDirectory(Paths.get(currentPicPath));
        } catch (IOException e) {
            log.error("创建文件夹 {} , {} 出错", currentPath, currentPicPath, e);
            return null;
        }

        list.forEach( withdrawOrder -> {
            excelModelList.add(WithdrawOrderExcelModel.convertFrom(withdrawOrder));
            downloadInfo.increase();
            try {
                downloadOrderPic(pathList, withdrawOrder, currentPicPath);
            } catch (Exception e) {
                log.error("提现订单 ：{} , 下载图片出错 ", withdrawOrder.getId(), e);
            }
        });
        String nowTime = dateTimeFormatter.format(LocalDateTime.now());
        list = null;
        try(FileOutputStream out = new FileOutputStream(currentPath + "提现订单处理" + nowTime + ".xlsx")) {
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet1 = new Sheet(1, 0, WithdrawOrderExcelModel.class);
            sheet1.setSheetName("提现订单");
            writer.write(excelModelList, sheet1);
            writer.finish();
        } catch (Exception e) {
            log.error("生成EXCEL出错", e);
        }
        File zip = ZipUtil.compress(currentPath, TMP_FILE_PATH + nowTime + ".zip");
        try {
            FileUtils.deleteDirectory(Paths.get(currentPath).toFile());
        } catch (IOException e) {
            log.error("delete dir {} error", currentPath, e);
        }
        downloadInfo.increase();
        return zip;
    }

    private List<Path> downloadOrderPic(List<Path> fileList, WithdrawOrder withdrawOrder, String path) throws IOException{
        JSONObject jsonObject = JSON.parseObject(withdrawOrder.getOrderReceiverInfo().getIdCardDetail());
        String front = jsonObject.get("front").toString();
        String reverse = jsonObject.get("reverse").toString();

        Path frontPath = Paths.get(path + withdrawOrder.getId() + "-01.jpg");
        Response response = OkHttpClientUtils.get(front);
        if (response.isSuccessful()) {
            Files.copy(response.body().byteStream(), frontPath);
            fileList.add(frontPath);
        }

        Path reversePath = Paths.get(path + withdrawOrder.getId() + "-02.jpg");
        Response response2 = OkHttpClientUtils.get(reverse);
        if (response2.isSuccessful()) {
            Files.copy(response2.body().byteStream(), reversePath);
            fileList.add(reversePath);
        }
        return fileList;
    }

    public DownloadInfo getDownloadInfo(String sessionId) {
        return downloadInfoMap.get(sessionId);
    }

    public void removeDownloadInfo(String sessionId) {
        downloadInfoMap.remove(sessionId);
    }
}
