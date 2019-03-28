package com.wonhigh.dao;

import com.wonhigh.module.WordList;

import java.util.List;
import java.util.Map;

public interface WordListMapper {

    int deleteByPrimaryKey(Integer id)throws Exception;

    int insert(WordList record)throws Exception;

    int insertSelective(WordList record)throws Exception;

    List<WordList> selectWordByMap(Map<String,Object> params)throws Exception;

    List<WordList> selectWordsForList(WordList wordList)throws Exception;

    WordList selectByPrimaryKey(Integer id)throws Exception;

    WordList selectBySimpleChineseKey(String key)throws Exception;


    int updateByPrimaryKeySelective(WordList record)throws Exception;

    int updateByPrimaryKey(WordList record)throws Exception;
}