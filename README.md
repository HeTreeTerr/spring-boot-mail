# 一、sping-boot-mail
邮件收发基本实现

## 1.1 邮件收取和解析（正文和附件）
```text
1、标题：
需要包含“hss资讯信息同步”
2、正文：
日期|产品编码|标题|附件名;
<begin>
2024-08-23 00:00:00|BF0001|我是标题1|BF0001.txt;
2024-08-23 00:00:00|BF0002|我是标题2|BF0002.txt;
2024-08-23 00:00:00|BF0002|我是标题3|BF0003.txt;
<end>
3、附件：
file.zip，压缩包中附件和步骤2名称对应
4、验证：
1）发送邮件（按照上面模板来）
2）保证邮件pop3协议开启
3）修改项目配置
4）执行单元测试com.hss.SpringBootmailApplicationTest#contextLoads 进行调试，或者启动项目访问/notice/leadIntoNotices
```