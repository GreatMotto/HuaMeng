package com.xckevin.download.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by fengwh on 2015/6/24.
 */
public class RandomAccessFileUtil {

    private File file;
    byte[] buf;
    RandomAccessFile fp;
    public RandomAccessFileUtil(String filename){
        file=new File(filename);
        buf=new byte[(int)file.length()];
    }
    public RandomAccessFileUtil(File desFilename){
        file=desFilename;
        buf=new byte[(int)desFilename.length()];
    }
    public void openFile()throws FileNotFoundException {
        fp=new RandomAccessFile(file,"rw");
    }
    public void closeFile()throws IOException {
        fp.close();
    }
    public void coding()throws IOException{
        fp.read(buf);
        for(int i=0;i<buf.length;i++){
            buf[i]=(byte)(~buf[i]);
        }
        fp.seek(0);
        fp.write(buf);
    }

}
