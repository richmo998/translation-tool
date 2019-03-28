import com.spreada.utils.chinese.ZHConverter;
import com.wonhigh.utils.ChineseWordUtil;
import org.apache.log4j.Logger;
import org.junit.Test;

import javax.sound.midi.Soundbank;


/**
 * @author moyongfeng
 * @email: 605669690@qq.com
 * @date 2018/12/19 11:43
 * @copyright richmo998
 * @description:
 */
public class TestTranslation {

    private static Logger logger = Logger.getLogger(TestTranslation.class);

    @Test
    public void test(){
        String []str = {"发送验证码失败！","抱歉，当前","，請安裝"};

       for(String s : str){
           System.out.println("原:"+s);
           char []c = s.toCharArray();
           for(char a: c){
               System.out.println(a+"是否为中文符号："+ChineseWordUtil.isChinesePunctuation(a));
           }

       }



//        char n = '：';
//        System.out.println(ChineseWordUtil.isChinesePunctuation(n));
//
//        char c = ':';
//        System.out.println(ChineseWordUtil.isChinesePunctuation(c));
//
//        char a='我';
//        System.out.println(ChineseWordUtil.isChinesePunctuation(a));

//        String str="                          ";
//        System.out.println("changdu:"+str.length());
//        System.out.println("changdu:"+str.trim().length());

    }

}
