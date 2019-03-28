package com.wonhigh.translation;

import com.wonhigh.common.Constants;
import com.wonhigh.i18nToolApplication;
import com.wonhigh.service.thread.MultiThreadCopyFile;
import com.wonhigh.service.thread.MultiThreadToFile;
import com.wonhigh.utils.ChineseWordUtil;
import com.wonhigh.utils.DateUtil;
import com.wonhigh.utils.LocalFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 翻译语言工具
 *
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/17 14:48
 * @copyright richmo998
 * @description:
 */
public class WordTranslation {

    private static Logger logger = Logger.getLogger(WordTranslation.class);
    private static Logger notInDbWordLogger = Logger.getLogger("notInDbWordLogger");
    private static Logger inDbWordLogger = Logger.getLogger("inDbWordLogger");

    private static String targetPath = "";


    /**
     * 翻译对应文件
     *
     * @param sources    源文件
     * @param language   语言种类
     * @param targetPath 目标文件
     */
    public static void translateFile(File sources, String language, String targetPath) {

        try {
            if (!sources.exists()) {
                //不存在，直接退出
                logger.error("源文件文件目录不存在，请检查");
                return;
            }
            //是目录，递归调用解析
            File[] filesList = sources.listFiles();
            if (null == filesList || filesList.length == 0) {
                //递归结束标志，为空文件
                return;
            }
            for (File file : filesList) {
                //递归判断
                if (file.isDirectory()) {
                    translateFile(file, language, targetPath);
                } else {
                    scanFileAndTranslate(file, language, targetPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描文件，逐行翻译
     *
     * @param file
     * @param language
     * @param targetPath
     */
    public static void scanFileAndTranslate(File file, String language, String targetPath) throws IOException {
        String sourcesPath = file.getAbsolutePath();
        String name = file.getName();
        //判断文件后缀是否在处理范围
//        String suffix = name.substring(name.indexOf(".") + 1);
        //2019-2-14 以文件最后的后缀为准，开放复制引用js到目标目录
        String suffix = name.substring(name.lastIndexOf(".") + 1);

        //此处需要将目标转化为最终文件路径targetPath
        targetPath = targetPath + sourcesPath.substring(sourcesPath.indexOf(Constants.LANGUAGE_CN) + Constants.LANGUAGE_CN.length());
        logger.info("最终目标文件：" + targetPath);

        if (!i18nToolApplication.includeFileSuffix.contains(suffix)) {
            logger.error("该文件类型不在翻译范围内,直接拷贝到目标路径下" + sourcesPath);
            //2019-2-14:非翻译文件直接copy到目标路径
            i18nToolApplication.executorService.execute(new MultiThreadCopyFile(sourcesPath,targetPath));
            return;
        }


        String operationTime = DateUtil.getDateTimeFormat(new Date());
        notInDbWordLogger.info("***操作时间：" + operationTime);
        notInDbWordLogger.info("***文件：" + sourcesPath);
        notInDbWordLogger.info("------------------------------------------------------------");
        inDbWordLogger.info("***操作时间：" + operationTime);
        inDbWordLogger.info("***文件：" + sourcesPath);
        inDbWordLogger.info("------------------------------------------------------------");
        //翻译后的list
        List<String> translationList = null;
        //加载对应文件
        List<String> strings = FileUtils.readLines(file, "UTF-8");
        if (null == strings || strings.isEmpty()) {
            logger.error("该文件为空");
            return;
        }

        BaseTranslation baseTranslation = new BaseTranslation();

        //不同文件进入不同的翻译类
        if (suffix.equalsIgnoreCase(Constants.SUFFIX_HTM) ||
                suffix.equalsIgnoreCase(Constants.SUFFIX_HTML) ||
                suffix.equalsIgnoreCase(Constants.SUFFIX_XML)) {
            //三者注释均为 <!--注释内容-->
            baseTranslation = new HtmlAndXMLTranslation();


        } else if (suffix.equalsIgnoreCase(Constants.SUFFIX_JS)) {
            //js两种注释  //注释的内容；/*注释的内容*/；
            baseTranslation = new JsTranslation();

        } else if (suffix.equalsIgnoreCase(Constants.SUFFIX_JSP)) {
            baseTranslation = new JspTranslation();
            // <!--注释内容-->;<%--注释内容--%>; <%  java代码都忽略掉 %>

        } else if (suffix.equalsIgnoreCase(Constants.SUFFIX_FTL)) {
            //忽略ftl相关注释  <!-- 注释 -->发布之后，客户端可以看到注释内容; <#-- 注释 -->发布之后，客户端看不到注释内容
            baseTranslation = new FtlTranslation();

        } else if (suffix.equalsIgnoreCase(Constants.SUFFIX_CSS)) {
            // css：/*注释的内容*/；

        } else if (suffix.equalsIgnoreCase(Constants.SUFFIX_RESX)) {

        }

        translationList = baseTranslation.translate(strings,language);
//        translationList = baseTranslation.getFlyInfomation(strings);

        //异步输出到目标文件
//        LocalFileUtils.writeToTranslationFile(translationList, targetPath);
        i18nToolApplication.executorService.execute(new MultiThreadToFile(translationList,targetPath));

        notInDbWordLogger.info("------------------------------------------------------------");
        notInDbWordLogger.info("***生成文件：" + targetPath);
        inDbWordLogger.info("------------------------------------------------------------");
        inDbWordLogger.info("***生成文件：" + targetPath);

    }

    /**
     * 翻译对应词条
     *
     * @param wordMap  :词条
     * @param language ：语言
     * @return
     */
    public static Map<String, String> translateWord(Map<String, String> wordMap, String language) {

        if (wordMap.isEmpty()) {
            logger.info("无翻译词条");
            return wordMap;
        }
        //notInDdWordList和inDbWordMap服务于日志输出
        List<String> notInDdWordList = new ArrayList<String>();
        Map<String, String> inDbWordMap = new HashMap<String, String>();

        Iterator<String> keys = wordMap.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = "";
            //1.从redis中获取
//            value = ChineseWordUtil.getTranslationFromRedis(key, language);
//            if (StringUtils.isNotBlank(value)) {
//                wordMap.put(key, value);
//                continue;
//            }
            //2.从本地词库获取
            value = ChineseWordUtil.getTranslationFromDB(key, language);
            if (StringUtils.isNotBlank(value)) {
                wordMap.put(key, value);
                inDbWordLogger.info(key + "=" + value);
                continue;
            }
            //3.api获取
            notInDbWordLogger.info(key);
            value = ChineseWordUtil.getTranslationFromApi(key, language);
            if (StringUtils.isNotBlank(value)) {
                wordMap.put(key, value);
                continue;
            }
        }
        return wordMap;
    }

    /**
     * 根据对照词条翻译对应语句
     *
     * @param sourceLine 原语句
     * @param wordMap    对照词条
     * @return
     */
    public static String replaceLineByTranslation(String sourceLine, Map<String, String> wordMap) {

        if (wordMap.isEmpty()) {
            return sourceLine;
        }

        Iterator<String> iterator = wordMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            String value = wordMap.get(next);
//            logger.info("词条：" + next + " 翻译为：" + value);
            sourceLine = sourceLine.replace(next, value);
        }
//        logger.info("翻译后整个语句为：" + sourceLine);
        return sourceLine;
    }


}
