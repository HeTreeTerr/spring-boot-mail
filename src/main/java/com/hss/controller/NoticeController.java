package com.hss.controller;

import com.hss.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 资讯 - Controller
 * </p>
 *
 * @author Hss
 * @date 2024-08-25
 */
@RestController
@Slf4j
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 载入资讯信息
     * @return
     */
    @RequestMapping(value = "/leadIntoNotices")
    public String leadIntoNotices(){
        noticeService.leadIntoNotices();
        return "success";
    }
}
