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
public class MallConfig {

    @Value(value = "${com.hss.mall.subject.key:#{null }}")
    private String mallSubjectKey;

    @Value(value = "${com.hss.mall.context.begin:#{null }}")
    private String mallContextBegin;

    @Value(value = "${com.hss.mall.context.end:#{null }}")
    private String mallContextEnd;

    @Value(value = "${com.hss.mall.pop3.host:#{null }}")
    private String pop3Host;

    @Value(value = "${com.hss.mall.pop3.port:#{null }}")
    private Integer pop3Port;

    @Value(value = "${com.hss.mall.username:#{null }}")
    private String username;

    @Value(value = "${com.hss.mall.password:#{null }}")
    private String password;
}
