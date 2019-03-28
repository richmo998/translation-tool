package com.wonhigh.service.thread;

import com.wonhigh.utils.LocalFileUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 异步多线程写文件
 *
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/21 11:04
 * @copyright richmo998
 * @description:
 */
public class MultiThreadToFile implements Runnable{

    private static Logger logger = Logger.getLogger(MultiThreadToFile.class);
    private List<String> allFileLine = null;
    private String targetFile = "";

    public MultiThreadToFile(List<String> allFileLine,String targetFile){
        this.allFileLine = allFileLine;
        this.targetFile = targetFile;

    }
    @Override
    public void run() {

        if(null == allFileLine){
            return ;
        }

        //异步输出到目标文件
        LocalFileUtils.writeToTranslationFile(allFileLine, targetFile);

    }
}
