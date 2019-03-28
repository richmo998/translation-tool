package com.wonhigh.translation;

import com.wonhigh.common.Constants;
import com.wonhigh.manager.WordListManager;
import com.wonhigh.utils.ChineseWordUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/19 15:49
 * @copyright richmo998
 * @description:
 */
public class JsTranslation extends BaseTranslation {
    private static Logger logger = Logger.getLogger(JsTranslation.class);

    /**
     * 翻译js文件
     *
     * @param fileList
     * @return
     */
    @Override
    public List<String> translate(List<String> fileList, String language) {
        //翻译后的list
        List<String> translationList = new ArrayList<String>();
        Map<String, String> wordMap = new HashMap<String, String>();
        Map<String, String> allWordMapForFile = new HashMap<String, String>(256);
        StringBuilder stringBuilder = new StringBuilder();
        boolean multiAnnotationIsFinish = true;

        //逐行读取
        for (String line : fileList) {

            line = line.trim();
            if (StringUtils.isBlank(line)) {
                continue;
            }
            //忽略js相关注释
            if (line.startsWith(Constants.ANNOTATION_JS_SINGLE)) {
                //TODO:行尾注释还未考虑
                translationList.add(line);
                continue;
            } else if (line.contains(Constants.ANNOTATION_JS_START)
                    && line.contains(Constants.ANNOTATION_JS_END)) {
                //单行全为注释
                translationList.add(line);
                continue;

            } else if (line.contains(Constants.ANNOTATION_JS_START)
                    && !line.contains(Constants.ANNOTATION_JS_END)) {
                translationList.add(line);
                multiAnnotationIsFinish = false;
                continue;
            } else if (!multiAnnotationIsFinish) {
                //多行注释还未结束
                translationList.add(line);
                if (line.contains(Constants.ANNOTATION_JS_END)) {
                    //多行注释结束
                    multiAnnotationIsFinish = true;
                }
                continue;
            }
            //如果整行语句不存在中文，则直接不需要翻译
            if (!ChineseWordUtil.isContainsChineseWord(line)) {
                translationList.add(line);
                continue;
            }

            //依次读取每个字符
            char[] chars = line.toCharArray();
            for (char c : chars) {
                if (ChineseWordUtil.isChineseByScript(c) && !ChineseWordUtil.isChinesePunctuation(c)) {
                    //为中文字符
                    stringBuilder.append(c);
                } else {
                    //不是中文字符，则确定该词条结束
                    if (StringUtils.isNotBlank(stringBuilder.toString())) {
                        //暂存一个空字符串，后期批量处理
                        wordMap.put(stringBuilder.toString(), "");
                        //清空StringBuilder
                        stringBuilder.setLength(0);
                    }
                }
            }
            //处理以中文结束的特殊情况
            if (stringBuilder.length() > 0) {
                wordMap.put(stringBuilder.toString(), "");
                //清空StringBuilder
                stringBuilder.setLength(0);
            }

            //处理完一行数据,对应词条翻译.替换词条
            translationList.add(WordTranslation.replaceLineByTranslation(line, WordTranslation.translateWord(wordMap, language)));

            allWordMapForFile.putAll(wordMap);
            //清空map处理下一行数据，做准备
            wordMap.clear();
        }

        super.recordWordMap(allWordMapForFile);
        return translationList;
    }
}
