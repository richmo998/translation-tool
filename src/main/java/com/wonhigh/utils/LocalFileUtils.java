package com.wonhigh.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/17 21:00
 * @copyright richmo998
 * @description:
 */
public class LocalFileUtils {
    private static Logger logger = Logger.getLogger(LocalFileUtils.class);

    /**
     * 是否为目录
     *
     * @param file
     * @return
     */
    public static boolean isDirectory(File file) {
        if (null == file) {
            return false;
        }
        return file.isDirectory();
    }

    /**
     * 是否为文件
     *
     * @return
     */
    public static boolean isFile(File file) {
        if (null == file) {
            return false;
        }
        return file.isFile();
    }

    /**
     * 将翻译写入翻译目录下
     *
     * @param translationLineList 翻译后语句
     * @param targetFile          翻译目录文件
     */
    public static void writeToTranslationFile(List<String> translationLineList, String targetFile) {

        if (null == translationLineList) {
            logger.info("translationLineList 为空");
            return;

        }
        if (translationLineList.isEmpty()) {
            logger.info("无写入语句");
            return;
        }
        if (StringUtils.isBlank(targetFile)) {
            logger.error("目标路径为空，无法写入");
            return;

        }

        File target = FileUtils.getFile(targetFile);
        if (target.isDirectory()) {
            logger.error("目标一定为文件，目录无法写入。");
            return;
        }

        try {
            FileUtils.writeLines(target, "UTF-8", translationLineList, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("翻译文件已完毕，请检查。" + targetFile);
    }

    /**
     * 拷贝文件
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFileToTarget(String sourceFile,String targetFile){

        if(StringUtils.isBlank(sourceFile)){
            logger.error("copy文件：缺失源文件全路径");
            return ;
        }

        if(StringUtils.isBlank(targetFile)){
            logger.error("copy文件：缺失目标文件全路径");
            return ;
        }


        try {
            FileUtils.copyFile(FileUtils.getFile(sourceFile),FileUtils.getFile(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("拷贝文件已完毕，请检查。" + targetFile);

    }




}
