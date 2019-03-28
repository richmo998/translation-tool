package com.wonhigh.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 加载系统properties配置文件
 *
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/17 20:38
 * @copyright richmo998
 * @description:
 */
public class SysPropertiesConf {

   private static Logger logger = Logger.getLogger(SysPropertiesConf.class);
    /**
     * 根据properties配置文件地址加载对应配置信息
     * @param propertyFile
     * @return
     */
    public static Properties loadProperties(String propertyFile) {

        Properties properties = new Properties();

        if(StringUtils.isBlank(propertyFile)){
            logger.error("propertyFile is null,check it!");
            return properties;
        }
        //load本地文件
        File file = new File(propertyFile);
        try {
            properties.load(new InputStreamReader(new FileInputStream(propertyFile),"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
           logger.error(e);
        }
        return properties;
    }


}
