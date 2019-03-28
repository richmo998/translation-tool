package com.wonhigh.manager;

import com.wonhigh.i18nToolApplication;
import com.wonhigh.module.WordList;
import com.wonhigh.service.WordListService;
import com.wonhigh.service.impl.WordListServiceImpl;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/20 22:12
 * @copyright richmo998
 * @description:
 */
public class WordListManager {
    private static Logger logger = Logger.getLogger(WordListManager.class);

    public void insertToDb(Map<String,String> allWordMap){

        if(null==allWordMap||allWordMap.isEmpty()){
            return ;
        }
        try {
            logger.info("同步进行词条入库===========》");
            Iterator<String> iterator = allWordMap.keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next();
                String value = allWordMap.get(key);
                WordList wordList = new WordList();
                wordList.setWordType(key);
                wordList.setZhCn(key);
                wordList.setZhHk(value);
                wordList.setSysGroup(i18nToolApplication.sysGroup);
                WordListService wordListService= new WordListServiceImpl();
                //检查是否已经存在
//                List<WordList> list = wordListService.selectWordsForList(wordList);
//                if(null != list && list.size()!=0){
//                    logger.info(key+":在词库中已经存在，无需重复插入");
//                    continue;
//                }
                WordList wl = wordListService.selectBySimpleChineseKey(key);
                if(null != wl){
                    logger.info(key+":在词库中已经存在，无需重复插入");
                    continue;
                }

                wordListService.insertSelective(wordList);
            }
            logger.info("《==============同步进行词条入库完成");
        } catch (Exception e) {
            logger.error("插入词库报错，请见检查。"+e.getMessage());
            e.printStackTrace();
        }
    }
}
