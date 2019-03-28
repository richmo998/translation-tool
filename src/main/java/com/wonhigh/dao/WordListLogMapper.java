package com.wonhigh.dao;

import com.wonhigh.module.WordListLog;

import java.util.List;
import java.util.Map;

public interface WordListLogMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WordListLog record);

    int insertSelective(WordListLog record);

    List<WordListLog> selectOperationLogByMap(Map<String,Object> params);

    List<WordListLog> selectOperationLogForList(WordListLog wordListLog);

    WordListLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WordListLog record);

    int updateByPrimaryKey(WordListLog record);
}