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

    @Value(value = "${com.hss.mall.subject.key}")
    private String mallSubjectKey;

    @Value(value = "${com.hss.mall.context.begin}")
    private String mallContextBegin;

    @Value(value = "${com.hss.mall.context.end}")
    private String getMallContextEnd;
}
