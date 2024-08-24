package com.hss.service.impl;

import com.hss.bean.Notice;
import com.hss.config.MallConfig;
import com.hss.dao.MallContentDTO;
import com.hss.service.NoticeService;
import com.hss.service.ReceiveEmailService;
import com.hss.utils.XmlUtil;
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
                String[] noticeItemArr = context.split(";");
                for(Integer i=0; i < noticeItemArr.length; i++){
                    Notice notice = new Notice();
                    String[] noticeItem = noticeItemArr[i].split("\\|",-1);
                    try {
                        notice.setPublishDate(SDF.parse(noticeItem[0]));
                    }catch (Exception e){
                        log.error("时间转换失败！",e);
                    }
                    notice.setProdCode(noticeItem[1]);
                    notice.setTitle(noticeItem[2]);
                    notice.setUrl(noticeItem[3]);
                    noticeList.add(notice);
                }
                //附件解析
                log.info("zip:{}",dto.getZipFileContent());

                noticeListRes.addAll(noticeList);
            }

            log.info("============noticeListRes={}",noticeListRes);
        }else {
            log.info(pair.getValue().toString());
        }
    }
}
