package com.hss.service.impl;

import com.hss.config.MallConfig;
import com.hss.dao.MallContentDTO;
import com.hss.service.ReceiveEmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * 文件接收
 * </p>
 *
 * @author Hss
 * @date 2024-08-23
 */
@Service
@Slf4j
public class ReceiveEmailServiceImpl implements ReceiveEmailService {

    @Value(value = "${spring.servlet.multipart.location:#{null }}")
    private String tempFilePath;

    @Autowired
    private MallConfig mallConfig;

    @Override
    public Pair<Boolean,Object> receiveEmail() {
        //POP3主机名
        //String host = "pop3.163.com";
        String host = mallConfig.getPop3Host();
        //设置传输协议
        String protocol = "pop3";
        //用户账号
        String username = mallConfig.getUsername();
        //密码或者授权码
        String password = mallConfig.getPassword();
        //端口号
        Integer port = mallConfig.getPop3Port();

        /*
         * 获取Session
         */
        Properties props = new Properties();
        //协议
        props.setProperty("mail.store.protocol", protocol);
        //POP3主机名
        props.setProperty("mail.pop3.host", host);
        props.setProperty("mail.smtp.auth", "true");
        // 需要认证
        props.setProperty("mail.pop3.auth", "true");
        // SSL 连接
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        // 不允许使用非 SSL 连接
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        Session session = Session.getInstance(props);
        session.setDebug(true);

        /*
         * 获取Store，一个Store对象表示整个邮箱的存储
         *
         */
        URLName urlName = new URLName(protocol, host, port, null, username, password);
        try{
            Store store = session.getStore(urlName);
            //连接邮件服务器
            store.connect();
            //要收取邮件，我们需要通过Store访问指定的Folder（文件夹），通常是INBOX表示收件箱
            //获取邮箱的邮件夹，通过pop3协议获取某个邮件夹的名称只能为inbox，不区分大小写
            Folder folder = store.getFolder("INBOX");
            //打开邮箱方式（邮件访问权限），这里的只读权限
            folder.open(Folder.READ_ONLY);
            //打印邮件总数/新邮件数量/未读数量/已删除数量:
            log.info("Total messages: " + folder.getMessageCount());
            log.info("New messages: " + folder.getNewMessageCount());
            log.info("Unread messages: " + folder.getUnreadMessageCount());
            log.info("Deleted messages: " + folder.getDeletedMessageCount());

            // 获得邮件夹Folder内的所有邮件Message对象，一个Message代表一个邮件
            Message[] messages = folder.getMessages();

            List<MallContentDTO> dtoList = new ArrayList<>();
            for (Message message : messages) {
                /*解析邮件*/
                log.info("=====================");
                //获取主题
                String subject = message.getSubject();
                log.info("主题:" + subject);
                log.info("时间:" + message.getSentDate());
                //================== 获取邮件内容==================
                // 仅包含正文的简单邮件
                if(!StringUtils.isEmpty(mallConfig.getMallSubjectKey()) && subject.contains(mallConfig.getMallSubjectKey())){
                    if (message.isMimeType("TEXT/*")) {
                        log.info("邮件正文: " + message.getContent());
                    } else {
                        // 解析稍复杂邮件
                        MallContentDTO dto = new MallContentDTO();
                        parseMessage((MimeMultipart) message.getContent(),dto);
                        dtoList.add(dto);
                    }
                }
            }

            //传入true表示删除操作会同步到服务器上（即删除服务器收件箱的邮件），无参方法默认传递true
            folder.close(false);
            store.close();

            return Pair.of(true,dtoList);
        }catch (Exception e){
            log.error("接收失败！",e);
        }
        return Pair.of(false,"接收失败！");
    }


    /**
     * 解析邮件
     */
    public void parseMessage (MimeMultipart part,MallContentDTO dto) throws MessagingException, IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        for (int i = 0; i < part.getCount(); i++) {
            BodyPart body = part.getBodyPart(i);
            if (body.isMimeType("text/html")) {
                log.info("html格式正文: " + body.getContent());
            } else if (body.isMimeType("text/plain")) {
                log.info("纯文本格式正文: " +  body.getContent());
                dto.setTextContent(body.getContent().toString());
            } else if (body.isMimeType("multipart/*")) {
                MimeMultipart multipart = (MimeMultipart) body.getContent();
                parseMessage(multipart,dto);
            } else { // 附件
                InputStream inputStream = body.getDataHandler().getInputStream();
                int len = 0;
                while( (len = inputStream.read(bytes)) != -1 ){
                    outStream.write(bytes, 0, len);
                }
                inputStream.close();
                byte[] data = outStream.toByteArray();
                String fileName = body.getFileName();
                log.info("fileName={}",fileName);
                File tempFile = new File(tempFilePath + File.separator + System.currentTimeMillis() + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.write(data);
                fileOutputStream.close();
                log.info("邮件附件本地路径: " + tempFile.getAbsolutePath());
                dto.setZipFileContent(tempFile.getAbsolutePath());
            }
        }
    }
}
