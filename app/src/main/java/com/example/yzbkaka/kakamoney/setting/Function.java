package com.example.yzbkaka.kakamoney.setting;

/**
 * Created by yzbkaka on 20-4-25.
 */

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 功能全局方法
 */
public class Function {

    /**
     * 判断字符串是否是数字/小数
     */
    public static int isNumber(String string){
        String bigStr;
        try {
            bigStr = new BigDecimal(string).toString();
        } catch (Exception e) {
            return 0;//异常 说明包含非数字。
        }
        return 1;
    }
}
