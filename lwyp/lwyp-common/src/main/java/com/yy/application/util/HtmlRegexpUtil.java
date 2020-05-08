package com.yy.application.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author admin
 */
public class HtmlRegexpUtil {
    private final static String REGXP_FOR_HTML = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

    private final static String REGXP_FOR_HTML_TO_STR = "<([^>]*)>|\\t|\\r|\\n|&nbsp;|&nbsp>"; // 过滤所有以<开头以>结尾以及去除字符串中的空格,回车,换行符,制表符的标签

    private final static String REGXP_FOR_IMG_TAG = "<\\s*img\\s+([^>]*)\\s*>"; // 找出IMG标签

    private final static String REGXP_FOR_IMA_TAG_SRC_ATTRIB = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

    /**
     *
     */
    public HtmlRegexpUtil() {
    }

    /**
     * 基本功能：判断标记是否存在
     * <p/>
     *
     * @param input
     * @return boolean
     */
    public static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '>':
                        flag = true;
                        break;
                    case '<':
                        flag = true;
                        break;
                    case '"':
                        flag = true;
                        break;
                    case '&':
                        flag = true;
                        break;
                    default:
                        flag = false;
                        break;
                }
            }
        }
        return flag;
    }

    /**
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     * <p/>
     *
     * @param str
     * @return String
     */
    public static String filterHtml(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        Pattern pattern = Pattern.compile(REGXP_FOR_HTML_TO_STR);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        String strContext;
        strContext = sb.toString().replace(" ","")
                .replace("&ldquo;","“")
                .replace("&rdquo;","”")
                .replace("&quot;","\"")
                .replace("&amp;","&")
                .replace("&lt;","<")
                .replace("&gt;",">");
        return strContext;
    }

    public static void main(String[] args) {
        String template = "<p>搏击操将拳击、空手道、跆拳道功夫和舞蹈动作结合在一起，在动感音乐中既宣泄情绪，又健身塑形，它让脂肪如临大敌，让压力瞬间遁形。跟着动感音乐，在教练的带领下，出拳起跳搏击，把烦恼与压力一一击垮，重拾生机与活力</p><p><img alt=\"\" src=\"https://image.dongfangfuli.com/2020/05/07/6dd8317d1119ba6e5f1362892773dbe16662bfc3c97b73109dd4486576d1ac0a.jpg\" style=\"height:400px; width:1200px\" /></p>";

        System.out.println(filterHtml(template));
    }

    /**
     * 基本功能：过滤指定标签
     * <p/>
     *
     * @param str
     * @param tag 指定标签
     * @return String
     */
    public static String fiterHtmlTag(String str, String tag) {
        String regxp = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 基本功能：替换指定的标签
     * <p/>
     *
     * @param str
     * @param beforeTag 要替换的标签
     * @param tagAttrib 要替换的标签属性值
     * @param startTag  新标签开始标记
     * @param endTag    新标签结束标记
     * @return String
     * @如：替换img标签的src属性值为[img]属性值[/img]
     */
    public static String replaceHtmlTag(String str, String beforeTag,
                                        String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag
                    .group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag
                        + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }
}
