package com.wonhigh.utils;

import com.spreada.utils.chinese.ZHConverter;
import com.wonhigh.common.Constants;
import com.wonhigh.module.WordList;
import com.wonhigh.service.WordListService;
import com.wonhigh.service.impl.WordListServiceImpl;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/17 22:26
 * @copyright richmo998
 * @description:
 */
public class ChineseWordUtil {
    private static Logger logger = Logger.getLogger(ChineseWordUtil.class);

    public static Pattern ChinesePattern = Pattern.compile("[\u4e00-\u9fa5]");

    public static Pattern mobilePhoneNoPattern = Pattern.compile("1[35789]\\d{9}");

    /**
     * 从中文中提取手机号码
     * @param message
     * @return
     */
    public static String getMobilePhoneNo(String message){

        Matcher m = mobilePhoneNoPattern.matcher(message);
        String mobile = "";
        while (m.find()) {
            //一定需要先查找再调用group获取电话号码
            mobile = m.group();
        }

        return mobile;
    }

    /**
     * 正则表达式是否包含中文
     * 或则区间判断
     * if(!(19968 <= n && n <40869)) {
     * return false;
     * }
     * 使用Unicode编码范围来判断汉字；这个方法不准确,因为还有很多汉字不在这个范围之内
     *
     * @param word
     * @return
     */
    public static boolean isContainsChineseWord(String word) {
        Matcher m = ChinesePattern.matcher(word);
        if (m.find()) {
            return true;
        }
        return false;

    }


    /**
     * 使用UnicodeScript方法判断
     * 此方法判断更准确，可包含生僻字
     *
     * @param c
     * @return
     */
    public static boolean isChineseByScript(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        if (sc == Character.UnicodeScript.HAN) {
            return true;
        }
        return false;
    }


    /**
     * 根据UnicodeBlock方法判断中文标点符号
     *
     * @param c
     * @return
     */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验一个字符是否是汉字
     * 此判断不能排除中文符号
     *
     * @param c 被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChineseChar(char c) {
        try {
            return String.valueOf(c).getBytes("UTF-8").length > 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从redis中获取翻译
     *
     * @param wordKey  键
     * @param language 语言
     * @return
     */
    public static String getTranslationFromRedis(String wordKey, String language) {

        return "";
    }

    /**
     * 从DB中获取翻译
     *
     * @param wordKey  键
     * @param language 语言
     * @return
     */
    public static String getTranslationFromDB(String wordKey, String language) {
        WordListService wordListService = new WordListServiceImpl();
//        WordList wordList = new WordList();
//        wordList.setWordType(wordKey);
//        wordList.setZhCn(wordKey);
//        wordListService.selectWordsForList(wordList);
        String value = "";
        try {
            WordList wordList = wordListService.selectBySimpleChineseKey(wordKey);
            if (null == wordList) {
                return "";
            }
            if (language.equalsIgnoreCase(Constants.LANGUAGE_HK)) {
                value = wordList.getZhHk();
            } else {
                value = wordList.getZhEn();
            }

        } catch (Exception e) {

            logger.error("查询翻译失败。" + e.getMessage());
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 从google中获取翻译
     *
     * @param wordKey  键
     * @param language 语言
     * @return
     */
    public static String getTranslationFromApi(String wordKey, String language) {
        String value = "";
        //默认为中文繁体
        int converterType = Constants.CONVERT_TYPE_HK;
        if (Constants.LANGUAGE_HK.equalsIgnoreCase(language)) {
            converterType = Constants.CONVERT_TYPE_HK;
        } else if (Constants.LANGUAGE_CN.equalsIgnoreCase(language)) {
            converterType = Constants.CONVERT_TYPE_CN;
        }
        //查询API翻译
        value = ZHConverter.convert(wordKey, converterType);
        return value;
    }
}
