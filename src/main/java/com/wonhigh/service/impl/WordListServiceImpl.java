package com.wonhigh.service.impl;

import com.wonhigh.dao.WordListMapper;
import com.wonhigh.module.WordList;
import com.wonhigh.service.WordListService;
import com.wonhigh.utils.DbConnectionUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;


/**
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/19 20:03
 * @copyright richmo998
 * @description:
 */
public class WordListServiceImpl implements WordListService {

    private Logger logger = Logger.getLogger(WordListServiceImpl.class);
    private WordListMapper wordListMapper = null;

    public WordListServiceImpl(){
        if(null == wordListMapper){
                wordListMapper = (WordListMapper)DbConnectionUtil.getSingleSqlSession().getMapper(WordListMapper.class);
//            logger.info("获取WordListMapper成功");
        }
    }

    @Override
    public int deleteByPrimaryKey(Integer id) throws Exception{
        return wordListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(WordList record)throws Exception {
        return wordListMapper.insert(record);
    }

    @Override
    public int insertSelective(WordList record)throws Exception {
        return wordListMapper.insertSelective(record);
    }

    @Override
    public List<WordList> selectWordByMap(Map<String, Object> params)throws Exception {
        return wordListMapper.selectWordByMap(params);
    }

    @Override
    public List<WordList> selectWordsForList(WordList wordList)throws Exception {
        return null;
    }

    @Override
    public WordList selectByPrimaryKey(Integer id)throws Exception {
        return wordListMapper.selectByPrimaryKey(id);
    }

    @Override
    public WordList selectBySimpleChineseKey(String key) throws Exception {
        return wordListMapper.selectBySimpleChineseKey(key);
    }

    @Override
    public int updateByPrimaryKeySelective(WordList record)throws Exception {
        return wordListMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(WordList record)throws Exception {
        return wordListMapper.updateByPrimaryKey(record);
    }
}
