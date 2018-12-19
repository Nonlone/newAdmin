package com.feitai.admin.mop.advert.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.feitai.admin.core.service.DynamitSupportService;
import com.feitai.admin.core.service.Page;
import com.feitai.admin.core.web.BaseListableController;
import com.feitai.admin.mop.advert.dao.entity.AdvertBlock;
import com.feitai.admin.mop.advert.dao.entity.AdvertItem;
import com.feitai.admin.mop.advert.enums.AdvertItemStatusEnum;
import com.feitai.admin.mop.advert.enums.AdvertItemTypeEnum;
import com.feitai.admin.mop.advert.service.AdvertBlockService;
import com.feitai.admin.mop.advert.service.AdvertItemService;
import com.feitai.admin.mop.advert.vo.AdvertItemQueryRequest;
import com.feitai.admin.mop.advert.vo.AdvertItemVo;
import com.feitai.admin.mop.advert.vo.SelectItem;
import com.feitai.admin.mop.base.AdaptDateEditor;
import com.feitai.admin.mop.base.BusinessException;
import com.feitai.admin.mop.base.CosClientUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springside.modules.utils.number.RandomUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author qiuyunlong
 */
@Slf4j
@Controller
@RequestMapping("/mop/advert/item")
public class AdvertItemController extends BaseListableController<AdvertItem>{

    /**
     * 文件上传到腾讯云的目录
     */
    private static final String IMG_FORMAT_PATTERN =
            "/mop/advert/${date}/${datetime}_${random}.jpg";

    @Value("${qcloud.cos.bucket.images}")
    private String bucket;

    @InitBinder
    public void initDate(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(Date.class, new AdaptDateEditor());
    }

    @Autowired
    private AdvertItemService advertItemService;
    @Autowired
    private AdvertBlockService advertBlockService;
    @Autowired
    private CosClientUtil cosClientUtil;

    @RequestMapping()
    public ModelAndView index(String blockId) {
        ModelAndView modelAndView = new ModelAndView("/mop/advert/itemIndex");
        modelAndView.addObject("blockId", blockId);
        return modelAndView;
    }

    @RequestMapping("/detail/index")
    public ModelAndView detail(@RequestParam(required = false) Long id) {
        ModelAndView modelAndView = new ModelAndView("/mop/advert/itemDetail");
        if (id != null) {
            modelAndView.addObject("id", id);
            modelAndView.addObject("type", advertItemService.getItemBlockType(id));
        }
        return modelAndView;
    }

    @RequestMapping("/blockItems")
    @ResponseBody
    public Object getBlockItems() {
        List<AdvertBlock> blockList = advertBlockService.list();
        List<SelectItem> result = new ArrayList<>(blockList.size());
        blockList.forEach(advertBlock -> {
            result.add(new SelectItem(advertBlock.getTitle(), advertBlock.getId()));
        });
        return result;
    }

    @RequestMapping("/titles")
    @ResponseBody
    public Object getTitles() {
        return advertItemService.listTitle();
    }

    @RequiresPermissions("/mop/advert/item:list")
    @RequestMapping("/list")
    @ResponseBody
    public Object list(@ModelAttribute AdvertItemQueryRequest queryRequest) {
        PageInfo pageInfo = advertItemService.listWithEditCopyByCode(
                queryRequest.getPageIndex(),
                queryRequest.getLimit(),
                queryRequest.getTitle(),
                queryRequest.getStatus(),
                queryRequest.getBlockId(),
                queryRequest.getStartTime(),
                queryRequest.getEndTime(),
                queryRequest.getField(),
                queryRequest.getDirection()
        );
        Page<AdvertItem> itemPage = buildPage(pageInfo, queryRequest.getPageIndex(), queryRequest.getLimit());
        return itemPage;
    }

    @RequiresPermissions("/mop/advert/item:add")
    @RequestMapping("/add")
    @ResponseBody
    public Object add(@ModelAttribute AdvertItem advertItem, String blockIds) {
        advertItemService.addOne(advertItem, blockIds);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/item:add")
    @RequestMapping("/detail/saveOrUpdate")
    public Object addDetail(@ModelAttribute AdvertItem advertItem, String code) {
        if (advertItem.getId() == null) {
            advertItemService.addOne(advertItem, code);

        }
        advertItemService.updateDetail(advertItem, code, getOperator());
        ModelAndView modelAndView = new ModelAndView("/mop/advert/itemDetail");
        modelAndView.addObject("id", advertItem.getId());
        modelAndView.addObject("type", advertItemService.getItemBlockType(advertItem.getId()));
        return modelAndView;
    }

    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/delete")
    @ResponseBody
    public Object delete(Long id) {
        advertItemService.delete(id);
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/enable")
    @ResponseBody
    public Object enable(Long id) {
        advertItemService.updateStatus(id, AdvertItemStatusEnum.ENABLE, getOperator());
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/disable")
    @ResponseBody
    public Object disable(Long id) {
        advertItemService.updateStatus(id, AdvertItemStatusEnum.DISABLE, getOperator());
        return BaseListableController.successResult;
    }
    
    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/publish")
    @ResponseBody
    public Object publish(Long id) {
        advertItemService.publishAdvertItemEditCopy(id, getOperator());
        return BaseListableController.successResult;
    }
    
    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/reset")
    @ResponseBody
    public Object reset(Long id) {
        advertItemService.resetAdvertItemEditCopy(id, getOperator());
        return BaseListableController.successResult;
    }
    

    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(MultipartFile files, Long itemId) throws IOException, ImageProcessingException {
        if (itemId == null) {
            return "error";
        }
        String pathName = "tmp" + System.currentTimeMillis();
        Files.copy(files.getInputStream(), Paths.get(pathName));
        File file = Paths.get(pathName).toFile();
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        Iterator<Directory> it = metadata.getDirectories().iterator();
        String width = null;
        String height = null;
        while (it.hasNext() && width == null && height == null) {
            Directory directory = it.next();
            for(Tag tag : directory.getTags()) {
                if ("Image Width".equals(tag.getTagName())) {
                    width = tag.getDescription().split(" ")[0];
                } else if ("Image Height".equals(tag.getTagName())) {
                    height = tag.getDescription().split(" ")[0];
                }
            }
        }
        log.info("Image Width is {}", width);
        log.info("Image Height is {}", height);

        String yyyyMMddHHmmss = FastDateFormat.getInstance("yyyyMMddHHmmss").format(new Date());
        String yyyyMMdd = yyyyMMddHHmmss.substring(0, 8);
        String key = IMG_FORMAT_PATTERN.replace("${date}", yyyyMMdd).replace("${datetime}", yyyyMMddHHmmss)
                .replace("${random}", RandomUtil.randomStringFixLength(8));

        log.info("file is" + file.getAbsolutePath());
        String url = null;
        try {
            url = cosClientUtil.picCOS(file, key, bucket);
        } catch (Exception e) {
            log.error("upload file error", e);
            throw new BusinessException("上传文件失败" + e.getMessage());
        } finally {
            Files.delete(file.toPath());
        }
        log.info("url:" + url);
        AdvertItem advertItem = advertItemService.selectWithEditCopyById(itemId);
        List<AdvertItem.AdvertItemShowConfigItem> list = JSON.parseObject(advertItem.getShowConfig(), new TypeReference<List<AdvertItem.AdvertItemShowConfigItem>>(){});
        AdvertItem.AdvertItemShowConfigItem advertItemShowConfigItem = new AdvertItem.AdvertItemShowConfigItem();
        advertItemShowConfigItem.setUrl(url.substring(0, url.lastIndexOf('?')));
        advertItemShowConfigItem.setDpi(width + "*" + height);
        advertItemShowConfigItem.setRatio(getRatio(Integer.valueOf(width), Integer.valueOf(height)));
        advertItemShowConfigItem.setRatioValue(new BigDecimal(width).divide(new BigDecimal(height),4,BigDecimal.ROUND_HALF_UP));
        advertItemShowConfigItem.setType(AdvertItemTypeEnum.IMAGE.getValue());
        advertItemShowConfigItem.setDefaultItem(false);
        list.add(advertItemShowConfigItem);

        advertItem.setShowConfig(JSON.toJSONString(list));
        advertItemService.update(advertItem, getOperator());
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/item:list")
    @RequestMapping("/detail")
    @ResponseBody
    public Object getDetailInfo(Long id) {
        if (id != null) {
            return AdvertItemVo.build(advertItemService.selectWithEditCopyById(id), advertItemService.selectBlockIdsWithEditCopyByItemId(id));
        }
        return null;
    }

    @RequiresPermissions("/mop/advert/item:list")
    @RequestMapping("/detail/file")
    @ResponseBody
    public Object getDetailFileInfo(Long id) {
        if (id != null) {
            return JSON.parseArray(advertItemService.selectWithEditCopyById(id).getShowConfig());
        }
        return null;
    }

    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/file/setDefault")
    @ResponseBody
    public Object setDefaultFile(Long id, String url) {
        advertItemService.setDefaultFile(id, url, SecurityUtils.getSubject().getPrincipal().toString());
        return BaseListableController.successResult;
    }

    @RequiresPermissions("/mop/advert/item:update")
    @RequestMapping("/detail/file/delete")
    @ResponseBody
    public Object deleteFile(Long id, String url) {
        advertItemService.deleteFile(id, url, SecurityUtils.getSubject().getPrincipal().toString());
        return BaseListableController.successResult;
    }

    @RequestMapping("cache")
    @ResponseBody
    public Object cache(Long id) {
        advertItemService.evictCache(id);
        return BaseListableController.successResult;
    }

    //计算比例
    public String getRatio(int width, int height) {
        int n = gcd(width, height);
        return width/n + ":" + height/n;
    }
    /*** 求最大公约数 ***/
    private int gcd(int a, int b) {
        if (a % b != 0) {
            return gcd(b, a % b);
        } else {
            return b;
        }
    }

    @Override
    protected DynamitSupportService<AdvertItem> getService() {
        return null;
    }

    /**
     * 获得操作人
     * @return
     */
    public String getOperator() {
        return SecurityUtils.getSubject().getPrincipal().toString();
    }
}
