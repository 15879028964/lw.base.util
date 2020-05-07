package com.yy.application.util;


import com.alibaba.fastjson.JSON;

/**
 * @Description: json格式化
 * @Author: David.liu
 * @Date: 2020/2/26 19:47
 */
public class JsonFactory {

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    /**
     * 传入一个object，最后以字符串的形式输出控制台
     *
     * @param object
     * @return
     */
    public static String jsonFormat(Object object) {
        {
            String jsonString = JSON.toJSONString(object);
            int level = 0;
            //存放格式化的json字符串,选用build，有速度优势
            StringBuilder jsonForMatStr = new StringBuilder();
            //将字符串中的字符逐个按行输出
            for (int index = 0; index < jsonString.length(); index++)
            {
                char c = jsonString.charAt(index);
                //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
                if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                    jsonForMatStr.append(getLevelStr(level));
                }
                //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
                switch (c) {
                    case '{':
                    case '[':
                        jsonForMatStr.append(c).append("\n");
                        level++;
                        break;
                    case ',':
                        jsonForMatStr.append(c).append("\n");
                        break;
                    case '}':
                    case ']':
                        jsonForMatStr.append("\n");
                        level--;
                        jsonForMatStr.append(getLevelStr(level));
                        jsonForMatStr.append(c);
                        break;
                    default:
                        jsonForMatStr.append(c);
                        break;
                }
            }
            return jsonForMatStr.toString();

        }
    }
}
