package com.toolbox.weather.tools;

import java.util.HashMap;
import java.util.Map;

import com.toolbox.framework.utils.StringUtility;

public class LanguageTool {
    private static final String              defaultLanguage = "en";
    private static final Map<String, String> map             = new HashMap<String, String>();
    public static final Map<String, String>  globalMap       = new HashMap<String, String>();

    static {
        map.put("ko", "ko"); //koreanName;           //朝鲜语
        map.put("ja", "ja"); //koreanName;           //日本语
        map.put("pl", "pl"); //polishName;           //波兰文
        map.put("da", "da"); //danishName;           //丹麦文
        map.put("de", "de"); //germanName;           //德文
        map.put("ru", "ru"); //russianName;          //俄文
        map.put("fr", "fr"); // frenchName;           //法文
        map.put("zh", "zh_hans"); //简体中文
        map.put("zhhans", "zh_hans");//简体中文
        map.put("zhhant", "zh_hant"); //繁体中文
        map.put("fi", "fi"); //finnishName;          //芬兰文
        map.put("nl", "nl"); //dutchName;            //荷兰文
        map.put("cs", "cs");//czechName;            //捷克文   
        map.put("hr", "hr");//croatianName;         //克罗地亚文
        map.put("ro", "ro");//romanianName;         //罗马尼亚文
        map.put("ms", "ms");//malayName;            //马来文
        map.put("no", "no");//norwegianName;        //挪威文 (nb:书面挪威语,nn:新挪威语)
        map.put("pt", "pt");//portugueseName;       //葡萄牙文 （两列，什么意思？）
        map.put("sv", "sv");//swedishName;          //瑞典文
        map.put("sk", "sk");//slovakName;           //斯洛伐克文
        map.put("th", "th");//thaiName;             //泰文
        map.put("tr", "tr");//turkishName;          //土耳其文
        map.put("uk", "uk");//ukrainianName;        //乌克兰文
        map.put("es", "es");//spanishName;          //西班牙文
        map.put("el", "el");//greekName;            //希腊文
        map.put("hu", "hu");//hungarianName;        //匈牙利文
        map.put("it", "it");//italianName;          //意大利文
        map.put("id", "id");//indonesianName;       //印尼文
        map.put("en", "en");//englishName"); //英文
        map.put("enuk", "en_uk"); //英文-英国
        map.put("vi", "vi");//vietnameseName;       //越南文

        map.put("zhcn", "zh_hans"); // 中文（简体）  中国 
        map.put("zhtw", "zh_hant"); //  中文（繁体）  中国台湾
        map.put("zhhk", "zh_hant");//中文（繁体） 中国香港
        map.put("dadk", "da"); //丹麦语  丹麦  
        map.put("deat", "de"); //德语  奥地利 
        map.put("dech", "de"); //德语  瑞士
        map.put("dede", "de"); //德语  德国

        map.put("elgr", "el"); //希腊语  希腊  
        map.put("enca", "en"); //英语  加拿大
        map.put("engb", "en"); //  英语  联合王国
        map.put("enie", "en"); //英语  爱尔兰 
        map.put("enus", "en"); //英语  美国
        map.put("eses", "es"); //西班牙语  西班牙
        map.put("fifi", "fi"); //芬兰语  芬兰  
        map.put("frbe", "fr"); //法语  比利时  

        map.put("frca", "fr"); //法语  加拿大
        map.put("frch", "fr"); //法语  瑞士  
        map.put("frfr", "fr"); //法语  法国

        map.put("itch", "it"); //意大利语  瑞士 
        map.put("itit", "it"); // 意大利语  意大利
        //        map.put("jaJP", "ja"); //日语  日本  

        map.put("kokr", "ko"); // 韩国语  韩国
        map.put("nlbe", "nl"); //荷兰语  比利时  
        map.put("nlnl", "nl"); //  荷兰语  荷兰

        map.put("nono", "no"); // 挪威语 (Nynorsk)  挪威
        map.put("nonob", "no"); // 挪威语 (Bokm?l)  挪威  
        map.put("ptpt", "pt"); // 葡萄牙语  葡萄牙  
        map.put("svse", "sv"); // 瑞典语  瑞典    
        map.put("trtr", "tr"); // 土耳其语  土耳其  

        globalMap.put("ko", "KO");
        globalMap.put("ja", "JA");
        globalMap.put("pl", "PL");
        globalMap.put("da", "DA");
        globalMap.put("de", "DE");
        globalMap.put("ru", "RU");
        globalMap.put("fr", "FR");
        globalMap.put("zh_hans", "CN");
        globalMap.put("zh_hant", "CN");
        globalMap.put("fi", "FI");
        globalMap.put("nl", "NL");
        globalMap.put("cs", "CS");
        globalMap.put("hr", "HR");
        globalMap.put("ro", "RO");
        globalMap.put("ms", "MS");
        globalMap.put("no", "NO");
        globalMap.put("pt", "PT");
        globalMap.put("sv", "SV");
        globalMap.put("sk", "SK");
        globalMap.put("th", "TH");
        globalMap.put("tr", "TR");
        globalMap.put("uk", "UK");
        globalMap.put("es", "ES");
        globalMap.put("el", "EL");
        globalMap.put("hu", "HU");
        globalMap.put("it", "IT");
        globalMap.put("id", "ID");
        globalMap.put("en", "EN");
        globalMap.put("en_uk", "EN");
        globalMap.put("vi", "VI");

    }

    public static String get(String key) {
        if (StringUtility.isEmpty(key)) { return defaultLanguage; }
        key = key.toLowerCase();
        if (map.containsKey(key)) { return map.get(key); }
        String tmp = key;
        tmp = tmp.replace("_", "").replace("-", "");
        if (map.containsKey(tmp)) { return map.get(tmp); }

        if (key.contains("-")) {
            return get(key.split("-")[0]);
        } else if (key.contains("_")) { return get(key.split("_")[0]); }

        return defaultLanguage;

    }

    public static boolean isZhhans(String lang) {
        return get(lang).equals("zh_hans");
    }

    public static void main(String[] args) {
        System.out.println(get("en_US"));
    }
}
