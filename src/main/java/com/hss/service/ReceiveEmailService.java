package com.hss.service;

import org.apache.commons.lang3.tuple.Pair;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
public interface ReceiveEmailService {

    /**
     * 接受邮件
     */
    Pair<Boolean,Object> receiveEmail();
}
