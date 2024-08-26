package com.hss.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
@Configuration
@Data
@Slf4j
public class MailConfig {

    @Value(value = "${com.hss.mail.subject.key:#{null }}")
    private String mailSubjectKey;

    @Value(value = "${com.hss.mail.context.begin:#{null }}")
    private String mailContextBegin;

    @Value(value = "${com.hss.mail.context.end:#{null }}")
    private String mailContextEnd;

    @Value(value = "${com.hss.mail.pop3.host:#{null }}")
    private String pop3Host;

    @Value(value = "${com.hss.mail.pop3.port:#{null }}")
    private Integer pop3Port;

    @Value(value = "${com.hss.mail.username:#{null }}")
    private String username;

    @Value(value = "${com.hss.mail.password:#{null }}")
    private String password;
}
