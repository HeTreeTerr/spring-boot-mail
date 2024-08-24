package com.hss.dao;

import lombok.Data;

import java.io.InputStream;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
@Data
public class ZipContentDTO {

    /** 文件名 */
    private String fileName;
    /** 文件流 */
    private InputStream inputStream;
    /** base64 */
    private String fileBase64;
}
