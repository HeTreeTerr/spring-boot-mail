package com.hss.service.impl;

import com.hss.bean.Notice;
import com.hss.config.MallConfig;
import com.hss.dao.MallContentDTO;
import com.hss.dao.ZipContentDTO;
import com.hss.service.NoticeService;
import com.hss.service.ReceiveEmailService;
import com.hss.utils.XmlUtil;
import com.hss.utils.ZipFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MallConfig mallConfig;

    @Autowired
    private ReceiveEmailService receiveEmailService;

    @Override
    public void leadIntoNotices() {
        //获取邮件内容
        Pair<Boolean,Object> pair = receiveEmailService.receiveEmail();
        if(pair.getKey()){
            List<MallContentDTO> contentDTOS = (List<MallContentDTO>) pair.getValue();
            List<Notice> noticeListRes = new ArrayList<>();
            for (MallContentDTO dto : contentDTOS){
                String context;
                List<Notice> noticeList = new ArrayList<>();
                //内容解析
                if(!StringUtils.isEmpty(dto.getTextContent())){
                    context = XmlUtil.analyzeXml(dto.getTextContent().replaceAll("\\s+|\\u00A0+",""))
                            .replaceAll("&nbsp;"," ");
                    context = StringUtils.substringBetween(context,mallConfig.getMallContextBegin(),mallConfig.getGetMallContextEnd());
                }else {
                    context = null;
                }
                log.info("context:{}",context);
                //内容转换成资讯集
                String[] noticeItemArr = context.split(";");
                for(Integer i=0; i < noticeItemArr.length; i++){
                    Notice notice = new Notice();
                    String[] noticeItem = noticeItemArr[i].split("\\|",-1);
                    try {
                        //公告时间
                        notice.setPublishDate(SDF.parse(noticeItem[0]));
                    }catch (Exception e){
                        log.error("时间转换失败！",e);
                    }
                    //产品编码
                    notice.setProdCode(noticeItem[1]);
                    //标题
                    notice.setTitle(noticeItem[2]);
                    //文件名
                    notice.setUrl(noticeItem[3]);
                    noticeList.add(notice);
                }
                //附件解析
                log.info("zip:{}",dto.getZipFileContent());
                //将zip中附件上传，同时和资讯进行绑定
                if(!StringUtils.isEmpty(dto.getZipFileContent())){
                    this.bindNoticeFiles(dto.getZipFileContent(),noticeList);
                }
                noticeListRes.addAll(noticeList);
            }
            log.info("============noticeListRes={}",noticeListRes);
        }else {
            log.info(pair.getValue().toString());
        }
    }

    /**
     * 绑定和上传资讯附件
     * @param zipPath
     * @param noticeList
     * @return
     */
    private List<Notice> bindNoticeFiles(String zipPath,List<Notice> noticeList){
        //解析和获取zip中的文件
        List<ZipContentDTO> zipDtos = ZipFileUtil.unzip(zipPath);
        log.info("zipDtos={}",zipDtos);
        //遍历资讯，逐个进行附件绑定
        for (Notice notice : noticeList){
            String fileName = notice.getUrl();
            for (int i = 0; i < zipDtos.size(); i++){
                ZipContentDTO zipContentDTO = zipDtos.get(i);
                //文件匹配成功
                if(zipContentDTO.getFileName().endsWith(fileName)){
                    //todo 上传第三方服务器
                    notice.setUrl(zipContentDTO.getFileName());
                    break;
                }
                //最后一次还没有匹配，置空
                if(i == zipDtos.size() -1){
                    notice.setUrl(null);
                    break;
                }
            }
        }
        return noticeList;
    }
}
