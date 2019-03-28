package com.wonhigh.utils;

import com.wonhigh.i18nToolApplication;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * mysql数据库连接工具
 *
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/20 17:45
 * @copyright richmo998
 * @description:
 */
public class DbConnectionUtil {

    private static Logger logger = Logger.getLogger(DbConnectionUtil.class);

    private static SqlSessionFactory sqlSessionFactory = null;
    private static SqlSession sqlSession = null;

    private static String mapperLocation = "mybatis-config.xml";

    static {
        logger.info("初始化数据库连接");
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(mapperLocation);

        } catch (IOException e) {
            logger.error("加载mybatis配置文件出错。" + e.getMessage());
            e.printStackTrace();
        }
        //打开SQL对话工厂
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        logger.info("获取sqlSessionFactory成功");
    }

    /**
     * 获取sqlSession
     */
    public static synchronized SqlSession getSingleSqlSession() {
        if (null == sqlSession) {
            sqlSession = sqlSessionFactory.openSession(true);
            logger.info("====创建自动提交事务的sqlSession===");
        }
//        logger.info("成功获取到自动提交事务的sqlSession");
        return sqlSession;
    }


}
