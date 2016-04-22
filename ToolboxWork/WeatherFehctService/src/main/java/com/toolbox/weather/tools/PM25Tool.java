package com.toolbox.weather.tools;

import org.springframework.stereotype.Service;

import com.toolbox.framework.i18n.api.TextManager;
import com.toolbox.weather.enums.PM25Level;

@Service
public class PM25Tool {

    public static int getPm25Level(int pm25) {
        if (pm25 >= 0 && pm25 <= 50) {
            return 1;
        } else if (pm25 > 50 && pm25 <= 100) {
            return 2;
        } else if (pm25 > 100 && pm25 <= 150) {
            return 3;
        } else if (pm25 > 150 && pm25 <= 200) {
            return 4;
        } else if (pm25 > 200 && pm25 <= 300) {
            return 5;
        } else
            return 6;
    }

    public static String getPm25LevelInfo(int level, String lang) {
        String file_prefix = "weather_";
        String level_prefix = "pm25.level.";
        lang = LanguageTool.get(lang);
        switch (level) {
            case 1:
                return TextManager.getString(file_prefix + lang, level_prefix + "one");
            case 2:
                return TextManager.getString(file_prefix + lang, level_prefix + "two");
            case 3:
                return TextManager.getString(file_prefix + lang, level_prefix + "three");
            case 4:
                return TextManager.getString(file_prefix + lang, level_prefix + "four");
            case 5:
                return TextManager.getString(file_prefix + lang, level_prefix + "five");
            case 6:
                return TextManager.getString(file_prefix + lang, level_prefix + "six");
        }
        return "";
    }

    /**
     * 获取空气质量等级
     * @return
     *
     */
    public static PM25Level getLevel(int pm25) {
        for (PM25Level level : PM25Level.values()) {
            if (level.getMinValue() <= pm25 && pm25 <= level.getMaxValue()) {
                return level;
            }
        }
        return null;
    }
}
