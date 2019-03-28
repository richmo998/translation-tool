package com.wonhigh.translation;

import com.wonhigh.common.Constants;
import com.wonhigh.manager.WordListManager;
import com.wonhigh.utils.ChineseWordUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础翻译类
 * 全文件扫描，不过滤注释
 *
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/24 10:13
 * @copyright richmo998
 * @description:
 */
public class BaseTranslation {

    private static Logger logger = Logger.getLogger(BaseTranslation.class);
    private static Logger notInDbWordLogger = Logger.getLogger("notInDbWordLogger");
    private static Logger inDbWordLogger = Logger.getLogger("inDbWordLogger");


    /**
     * 根据txt中航班信息提取乘客信息
     * 提取固定格式类型信息
     *
     * @param fileList
     * @return
     */
    public List<String> getFlyInfomation(List<String> fileList) {

//        1.xxx 2.xxx NC7HNL/GS
//        3.  GS7551 T   FR25NOV  URCHTN RR2   0820 1010      E T1-- T1
//        4.NO
//        5.T
//        6.SSR FOID GS HK1 NI653221198901060450/P2
//        7.SSR FOID GS HK1 NI653221198704300435/P1
//        8.SSR TKNE GS HK1 URCHTN 7551 T25NOV 8262377052282/1/P2
//        9.SSR TKNE GS HK1 URCHTN 7551 T25NOV 8262377052281/1/P1
//        10.SSR CKIN GS GCZ HK1/P2
//        11.SSR CKIN GS GCZ HK1/P1
//        12.OSI GS GS CTCM18799381111/P1
//                这个是其中一段
//        也是一条航班信息
//        1.阿不都热扎克合力力 2.肉孜托合提吐孙托合提
//                这个是两个乘客的姓名
//        3.  GS7551
//                这个是航班号
//        FR25NOV
//                这个是起飞日期
//        王圣武  15:10:22
//        URCHTN
//                这个是航班三字码
//        0820 1010
//        这个是起飞时间跟落地时间
//        6.SSR FOID GS HK1 NI653221198901060450/P2
//        7.SSR FOID GS HK1 NI653221198704300435/P1
//        这个是身份证号码
//        12.OSI GS GS CTCM18799381111/P1
//                这个是手机号码

//        提取结果如下：
//        姓名：xx    身份证号：xxxx 航班号：GS7551  起飞时间:0820  达到时间：1010  联系电话：18799381111

        //只需要提取中文乘客，英文的直接跳过
        List<String> flyinfoList = new ArrayList<>();

        boolean isChinese = true;
        boolean secondContainsName = false;
        StringBuffer sb = new StringBuffer();
        int flag = 0;
        for (String str : fileList) {
            str = str.trim();
            //第一行为乘客姓名
            if (str.startsWith("1.") && !ChineseWordUtil.isContainsChineseWord(str)) {
                //说明非中文乘客
                isChinese = false;
                System.out.println("非中国乘客。"+str);
                continue;
            }else if (str.startsWith("1.") && ChineseWordUtil.isContainsChineseWord(str)) {
                //说明非中文乘客
                isChinese = true;
                System.out.println("中国乘客。"+str);
            }

            if (!isChinese) {
                //非中国乘客，忽略航班信息
                System.out.println("非中国乘客。其他信息："+str);
                continue;
            }

            try {

                if (str.startsWith("1.")) {
                    //将上一位乘客信息记录
                    if (sb.toString().length() > 0) {
                        flyinfoList.add(sb.toString());
                        System.out.println("最终提取结果：" + sb.toString());
                    }
                    //重新记录乘客信息
                    sb.setLength(0);
                    flag = 0;
                    secondContainsName = false;
                    isChinese = true;

                    sb.append("姓名：");
//                    getNameFromMessage(sb,str);
//
//                    continue;
                }

                if (ChineseWordUtil.isContainsChineseWord(str)) {
                    getNameFromMessage(sb,str);
                    secondContainsName = true;
                    continue;
                }

                flag++;
                //没有姓名的后面第一行就是航班信息
                if (1 == flag) {
                    String[] hangban = str.split(" ");
                    sb.append("航班：");
                    sb.append(hangban[2]);
                    sb.append(" ");
                    sb.append("日期：");
                    sb.append(hangban[6]);
                    sb.append(" ");
                    sb.append("起飞时间：");
                    sb.append(hangban[12]);
                    sb.append(" ");
                    sb.append("到达时间：");
                    sb.append(hangban[13]);
                    sb.append(" ");
                    continue;
                }

                //NIXXXXXX/P为身份证信息

                if (str.contains(" NI") && str.contains("/P")) {
                    if (!sb.toString().contains("身份证号")) {
                        sb.append("身份证号：");
                    }
                    System.out.println("身份证号:" + str.substring(str.indexOf(" NI") + 3, str.lastIndexOf("/P")));
                    sb.append(str.substring(str.indexOf(" NI") + 3, str.lastIndexOf("/P")));

                    sb.append(" ");
                    continue;
                }

                //CTC所在行的数字则表示为联系信息
                //遇到下一个1.开头的，则表示下一位乘客
                if (str.contains("CTC")) {
                    if (!sb.toString().contains("联系电话")) {
                        sb.append("联系电话：");
                    }
                    System.out.println("手机号码：" + ChineseWordUtil.getMobilePhoneNo(str));
                    sb.append(ChineseWordUtil.getMobilePhoneNo(str));
                    sb.append(" ");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("异常在哪");
                logger.error("出错行信息："+str);
                e.printStackTrace();
            }


        }

        return flyinfoList;

    }

    private void getNameFromMessage(StringBuffer sb,String str){

        String[] names = str.split(" ");
        for (int k = 0; k < names.length; k++) {
            String s = names[k];
            if (ChineseWordUtil.isContainsChineseWord(s)) {
                System.out.println("姓名" + k + ":" + s.substring(s.indexOf(".") + 1));
                sb.append(s.substring(s.indexOf(".") + 1));
                sb.append(" ");
            }
        }
    }
    /**
     * @param fileList 翻译文件
     * @param language 翻译语言
     * @return
     */
    public List<String> translate(List<String> fileList, String language) {
        //翻译后的list
        List<String> translationList = new ArrayList<String>();
        Map<String, String> wordMap = new HashMap<String, String>();
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> allWordMapForFile = new HashMap<String, String>(256);
        //逐行读取
        for (String line : fileList) {

            line = line.trim();
            if (StringUtils.isBlank(line)) {
                continue;
            }
            //如果整行语句不存在中文，则直接不需要翻译
            if (!ChineseWordUtil.isContainsChineseWord(line)) {
                translationList.add(line);
                continue;
            }

            //行中存在中文，依次读取每个字符，提取对应词条
            char[] chars = line.toCharArray();
            for (char c : chars) {
                if (ChineseWordUtil.isChineseByScript(c) && !ChineseWordUtil.isChinesePunctuation(c)) {
                    //为中文字符,且不是中文符号
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
            //TODO:记录整个文件的词条，后期异步批量进行词条入redis或db
            allWordMapForFile.putAll(wordMap);
            //清空map处理下一行数据，做准备
            wordMap.clear();
        }

        recordWordMap(allWordMapForFile);

        return translationList;
    }


    /**
     * 异步记录词条
     *
     * @param wordMap 词条对应关系
     */

    public void recordWordMap(Map<String, String> wordMap) {

        if (wordMap.isEmpty()) {
            logger.info("无记录词条");
            return;

        }

        notInDbWordLogger.info("***以上词条不存在，已使用词典翻译。");
        inDbWordLogger.info("***以上词条翻译完毕。");
        //异步批量进行词条入redis或db
        //2019-1-11 不需要记录静态翻译词条
//        new WordListManager().insertToDb(wordMap);

    }


    public static void main(String[] strs) {
        String str = "3.  GS7551 T   FR25NOV  URCHTN RR2   0820 1010      E T1-- T1";
        str.split(" ");
        System.out.println("len = " + str.length());
        System.out.println("航班= " + (str.substring(str.indexOf(".  "), str.indexOf(" T"))).replace(".  ", ""));
        System.out.println("len = " + str.length());
        System.out.println("len = " + str.length());


//        String names = "1.阿不都热扎克合力力";
//        System.out.println("name = "+names.substring(names.indexOf(".")+1));
//
//
//
//        String sfz = "6.SSR FOID GS HK1 NI653221198901060450/P2";
//        System.out.println("sfz = "+sfz.substring(sfz.indexOf("NI")+2,sfz.lastIndexOf("/")));


    }
}
