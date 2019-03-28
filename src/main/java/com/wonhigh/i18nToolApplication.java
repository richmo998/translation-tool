package com.wonhigh;

import com.wonhigh.common.Constants;
import com.wonhigh.translation.WordTranslation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 翻译工具入口
 *
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/19 10:04
 * @copyright richmo998
 * @description:
 */
public class i18nToolApplication {

    private static Logger logger = Logger.getLogger(i18nToolApplication.class);
    private static String sourcesPath = "E:\\zh_cn";
//    private static String sourcesPath = "/Users/richmo/work/paw-test/i18n-test-copy/zh_cn";
    private static String language = "zh_hk";
    private static String targetPath = "";

    public static String sysGroup = "BASE";
    public static ExecutorService executorService = Executors.newFixedThreadPool(2);

    public static String includeFileSuffix = ".html;.htm;.jsp;.ftl;.js;.resx;.xml;.css;.txt";

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        //如果有传参则根据传参个数进行取值
        if (args.length == 1) {
            sourcesPath = args[0];
        } else if (args.length == 2) {
            sourcesPath = args[0];
            language = args[1];
        } else if (args.length == 3) {
            sourcesPath = args[0];
            language = args[1];
            sysGroup = args[2];
        }

        if (StringUtils.isBlank(sourcesPath)) {
            logger.error("源文件不能为空");
            System.exit(0);
        }
        if (StringUtils.isBlank(language)) {
            logger.error("翻译语言种类不能为空");
            System.exit(0);

        }
        //此处处理好源文件目录和目标文件目录
        if (!sourcesPath.contains(Constants.LANGUAGE_CN)) {
            logger.error("源文件必须包含在zh_cn目录下。");
            System.exit(0);
        }

        //目标文件一定是在对应语言目录下
        targetPath = sourcesPath.replace(Constants.LANGUAGE_CN, language);
        File sources = FileUtils.getFile(sourcesPath);
        WordTranslation.translateFile(sources, language, targetPath);
        logger.info("当前工程所有静态资源已翻译完毕");

        //翻译完毕关闭线程池
        executorService.shutdown();
        Long endTime = System.currentTimeMillis();
        logger.info("翻译总耗时："+(endTime-startTime)/1000+" 秒");
        logger.info("所有线程任务执行完毕，关闭线程池。");

    }

}
