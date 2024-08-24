package com.hss.bean;

import lombok.Data;
import java.util.Date;

/**
 * <p>
 *  资讯信息
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
@Data
public class Notice {

    /** 记录id */
    private Integer id;
    /** 产品编号 */
    private String prodCode;
    /** 咨询标题 */
    private String title;
    /** 发布日期 */
    private Date publishDate;
    /** 保存url */
    private String url;
}
