package com.hss.utils;

import com.hss.dao.ZipContentDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
@Slf4j
public class ZipFileUtil {

    /**
     * 解压zip文件
     * @param zipPath
     */
    public static List<ZipContentDTO> unzip(String zipPath){
        File file = new File(zipPath);
        List<ZipContentDTO> zipContentDTOS = new ArrayList<>();
        try {
            // 创建zipFile
            ZipFile zipFile = new ZipFile(file);
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry = null;
            // 读取压缩包内的文件为zipEntry
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                log.info("zipEntry:" + zipEntry);
                // 是否为文件夹
                if (zipEntry.isDirectory()) {
                    continue;
                }
                long size = zipEntry.getSize();
                log.info(zipEntry.getName() + "-" + size + "字节");
                if (size > 0) {
                    // 使用zipFile.getInputStream()，则需要关闭zipFile，即zipFile.close()才能删除临时文件
                    ZipContentDTO contentDTO = new ZipContentDTO();
                    contentDTO.setFileName(zipEntry.getName());
                    //contentDTO.setInputStream(zipFile.getInputStream(zipEntry));
                    contentDTO.setFileBase64(Base64.getEncoder().encodeToString(IOUtils.toByteArray(zipFile.getInputStream(zipEntry))));
                    zipContentDTOS.add(contentDTO);
                }
                log.info("--------------------");
            }
            zipInputStream.closeEntry();
            // 关闭zipFile  关闭对应的流
            zipInputStream.close();
            zipFile.close();
            return zipContentDTOS;
        }catch (Exception e){
            log.error("文件解压失败！",e);
        }finally {
            //删除zip文件
            file.deleteOnExit();
        }
        return null;
    }
}
