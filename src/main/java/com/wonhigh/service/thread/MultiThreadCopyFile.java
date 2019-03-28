package com.wonhigh.service.thread;

import com.wonhigh.utils.LocalFileUtils;

/**
 *
 * copy非翻译类文件
 */
public class MultiThreadCopyFile implements  Runnable {
    private String sourceFile ="";
    private String targetFile = "";


    public MultiThreadCopyFile(String sourceFile,String targertFile){
        this.sourceFile=sourceFile;
        this.targetFile = targertFile;
    }

    @Override
    public void run() {
        LocalFileUtils.copyFileToTarget(sourceFile,targetFile);
    }
}
