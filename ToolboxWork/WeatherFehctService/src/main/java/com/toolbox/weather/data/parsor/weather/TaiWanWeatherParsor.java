package com.toolbox.weather.data.parsor.weather;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.framework.utils.DateUtility;
import com.toolbox.framework.utils.HttpUtility;
import com.toolbox.framework.utils.NumberUtility;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.weather.bean.CityWeatherBean;
import com.toolbox.weather.enums.WeatherSourceEnum;
import com.toolbox.weather.tools.YahooCodeFromChinaTool;
import com.toolbox.weather.tools.YahooCodeFromTaiWanTool;

public class TaiWanWeatherParsor {

    private CityWeatherBean cityWeather;
    private List<String>    exceptionList = new ArrayList<String>();

    public TaiWanWeatherParsor(CityWeatherBean cityWeather) {
        this.cityWeather = cityWeather;
    }

    private static Map<Integer, CityAreaMap> map           = new HashMap<Integer, CityAreaMap>();
    //台北
    private static String[]                  TaiBeiShi     = new String[] { "中正區、大同區、中山區、萬華區、信義區、松山區、大安區、南港區、北投區、內湖區、士林區、文山區" };
    //新北
    private static String[]                  XinBeiShi     = new String[] { "板橋區、新莊區、泰山區、林口區、淡水區、金山區、八里區、萬里區、石門區、三芝區、瑞芳區、汐止區、平溪區、貢寮區、雙溪區、深坑區、石碇區、新店區、坪林區、烏來區、中和區、永和區、土城區、三峽區、樹林區、鶯歌區、三重區、蘆洲區、五股區" };
    //台中
    private static String[]                  TaiZhongShi   = new String[] { "中區、東區、南區、西區、北區、北屯區、西屯區、南屯區、太平區、大里區、霧峰區、烏日區、豐原區、后里區、東勢區、石岡區、新社區、和平區、神岡區、潭子區、大雅區、大肚區、龍井區、沙鹿區、梧棲區、清水區、大甲區、外埔區、大安區" };
    //台南
    private static String[]                  TaiNanShi     = new String[] { "中西區、東區、 南區、北區、安平區、安南區、永康區、歸仁區、新化區、 左鎮區、玉井區、楠西區、南化區、仁德區、關廟區、龍崎區、官田區、麻豆區、 佳里區、西港區、七股區、將軍區、學甲區、北門區、新營區、後壁區、白河區、 東山區、六甲區、下營區、柳營區、鹽水區、善化區、大內區、山上區、新市區、安定區" };
    //高雄市
    private static String[]                  GaoXiongShi   = new String[] { "楠梓區、左營區、鼓山區、三民區、鹽埕區、前金區、新興區、苓雅區、前鎮區、小港區、旗津區、鳳山區、大寮區、鳥松區、林園區、仁武區、大樹區、大社區、岡山區、路竹區、橋頭區、梓官區、彌陀區、永安區、燕巢區、田寮區、阿蓮區、茄萣區、湖內區、旗山區、美濃區、內門區、杉林區、甲仙區、六龜區、茂林區、桃源區、那瑪夏區", "田寮" };
    //基隆市
    private static String[]                  JiLongShi     = new String[] { "仁愛區、中正區、信義區、中山區、安樂區、暖暖區、七堵區" };
    //新竹市
    private static String[]                  XinZhuShi     = new String[] { "東區、北區、香山區" };
    //嘉义市
    private static String[]                  JiaYiShi      = new String[] { "東區、北區、香山區" };
    //桃园县
    private static String[]                  TaoYuanXian   = new String[] { "桃園市、中壢市、平鎮市、八德市、楊梅市、大溪鎮、蘆竹鄉、龍潭鄉、龜山鄉、大園鄉、觀音鄉、新屋鄉、復興鄉" };
    //新竹县
    private static String[]                  XinZhuXian    = new String[] { "竹北市、竹東鎮、新埔鎮、關西鎮、新豐鄉、峨眉鄉、寶山鄉、五峰鄉、橫山鄉、北埔鄉、尖石鄉、芎林鄉、湖口鄉" };
    //苗栗县
    private static String[]                  MiaoLiXian    = new String[] { "苗栗市、通霄鎮、苑裡鎮、竹南鎮、頭份鎮、後龍鎮、卓蘭鎮、西湖鄉、頭屋鄉、公館鄉、銅鑼鄉、三義鄉、造橋鄉、三灣鄉、南庄鄉、大湖鄉、獅潭鄉、泰安鄉", "梧棲" };
    //彰化县
    private static String[]                  ZhangHuaXian  = new String[] { "彰化市、員林鎮、和美鎮、鹿港鎮、溪湖鎮、二林鎮、田中鎮、北斗鎮、花壇鄉、芬園鄉、大村鄉、永靖鄉、伸港鄉、線西鄉、福興鄉、秀水鄉、埔心鄉、埔鹽鄉、大城鄉、芳苑鄉、竹塘鄉、社頭鄉、二水鄉、田尾鄉、埤頭鄉、溪州鄉", "臺中" };
    //南投县
    private static String[]                  NanTouXian    = new String[] { "南投市、埔里鎮、草屯鎮、竹山鎮、集集鎮、名間鄉、鹿谷鄉、中寮鄉、魚池鄉、國姓鄉、水里鄉、信義鄉、仁愛鄉、、廬山、、合歡山頂、、日月潭、、玉山" };
    //云林县
    private static String[]                  YunLinXian    = new String[] { "斗六市、斗南鎮、虎尾鎮、西螺鎮、土庫鎮、北港鎮、莿桐鄉、林內鄉、古坑鄉、大埤鄉、崙背鄉、二崙鄉、麥寮鄉、臺西鄉、東勢鄉、褒忠鄉、四湖鄉、口湖鄉、水林鄉、元長鄉、、草嶺、、四湖、、", "嘉義" };
    //嘉义县
    private static String[]                  JiaYiXian     = new String[] { "太保市、朴子市、布袋鎮、大林鎮、民雄鄉、溪口鄉、新港鄉、六腳鄉、東石鄉、義竹鄉、鹿草鄉、水上鄉、中埔鄉、竹崎鄉、梅山鄉、番路鄉、大埔鄉、阿里山鄉" };
    //屏东县
    private static String[]                  PingDongXian  = new String[] { "屏東市、潮州鎮、東港鎮、恆春鎮、萬丹鄉、長治鄉、麟洛鄉、九如鄉、里港鄉、鹽埔鄉、高樹鄉、萬巒鄉、內埔鄉、竹田鄉、新埤鄉、枋寮鄉、新園鄉、崁頂鄉、林邊鄉、南州鄉、佳冬鄉、琉球鄉、車城鄉、滿州鄉、枋山鄉、霧台鄉、瑪家鄉、泰武鄉、來義鄉、春日鄉、獅子鄉、牡丹鄉、三地門鄉" };
    //宜兰县
    private static String[]                  YiLanXian     = new String[] { "宜蘭市、羅東鎮、蘇澳鎮、頭城鎮、礁溪鄉、壯圍鄉、員山鄉、冬山鄉、五結鄉、三星鄉、大同鄉、南澳鄉" };
    //花莲县
    private static String[]                  HuaLianXian   = new String[] { "花蓮市、鳳林鎮、玉里鎮、新城鄉、吉安鄉、壽豐鄉、秀林鄉、光復鄉、豐濱鄉、瑞穗鄉、萬榮鄉、富里鄉、卓溪鄉" };
    //台东县
    private static String[]                  TaiDongXian   = new String[] { "臺東市、成功鎮、關山鎮、長濱鄉、海端鄉、池上鄉、東河鄉、鹿野鄉、延平鄉、卑南鄉、金峰鄉、大武鄉、達仁鄉、綠島鄉、蘭嶼鄉、太麻里鄉" };
    //澎湖县
    private static String[]                  PengHuXian    = new String[] { "馬公市、湖西鄉、白沙鄉、西嶼鄉、望安鄉、七美鄉" };
    //金门县
    private static String[]                  JinMenXian    = new String[] { "金城鎮、金湖鎮、金沙鎮、金寧鄉、烈嶼鄉、烏坵鄉" };
    //连江县
    private static String[]                  LianJiangXian = new String[] { "馬祖" };

    static {
        map.put(12140, new CityAreaMap("台北市", "臺北市", "北部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/North.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Taipei_City.htm", TaiBeiShi));
        map.put(12142, new CityAreaMap("基隆市", "基隆市", "北部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/North.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Keelung_City.htm", JiLongShi));
        map.put(12143, new CityAreaMap("台中市", "臺中市", "中部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Central.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Taichung_City.htm", TaiZhongShi));
        map.put(12144, new CityAreaMap("台南市", "臺南市", "雲嘉南空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Yun-Chia-Nan.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Tainan_City.htm", TaiNanShi));
        map.put(12145, new CityAreaMap("新竹市", "新竹市", "竹苗空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Chu-Miao.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Hsinchu_City.htm", XinZhuShi));
        map.put(12146, new CityAreaMap("嘉义市", "嘉義市", "雲嘉南空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Yun-Chia-Nan.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Chiayi_City.htm", JiaYiShi));
        map.put(12147, new CityAreaMap("新北市", "新北市", "北部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/North.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/New_Taipei_City.htm", XinBeiShi));
        map.put(12148, new CityAreaMap("宜兰县", "宜蘭縣", "宜蘭空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Yilan.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Yilan_County.htm", YiLanXian));
        map.put(12149, new CityAreaMap("桃园县", "桃園縣", "北部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/North.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Taoyuan_County.htm", TaoYuanXian));
        map.put(12150, new CityAreaMap("新竹县", "新竹縣", "竹苗空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Chu-Miao.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Hsinchu_County.htm", XinZhuXian));
        map.put(12151, new CityAreaMap("苗栗县", "苗栗縣", "雲嘉南空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Chu-Miao.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Miaoli_County.htm", MiaoLiXian));
        map.put(12153, new CityAreaMap("彰化县", "彰化縣", "中部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Central.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Changhua_County.htm", ZhangHuaXian));
        map.put(12154, new CityAreaMap("南投县", "南投縣", "中部", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Central.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Nantou_County.htm", NanTouXian));
        map.put(12155, new CityAreaMap("云林县", "雲林縣", "雲嘉南空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Yun-Chia-Nan.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Yunlin_County.htm", YunLinXian));
        map.put(12156, new CityAreaMap("嘉义县", "嘉義縣", "雲嘉南空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Yun-Chia-Nan.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Chiayi_County.htm", JiaYiXian));
        map.put(12158, new CityAreaMap("高雄市", "高雄市", "高屏空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/KaoPing.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Kaohsiung_City.htm", GaoXiongShi));
        map.put(12159, new CityAreaMap("屏东县", "屏東縣", "高屏空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/KaoPing.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Pingtung_County.htm", PingDongXian));
        map.put(12160, new CityAreaMap("澎湖县", "澎湖縣", "馬公", "http://taqm.epa.gov.tw/taqm/zh-tw/PsiIsland/Magong.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Penghu_County.htm", PengHuXian));
        map.put(12161, new CityAreaMap("台东县", "臺東縣", "花東空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Hua-Tung.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Taitung_County.htm", TaiDongXian));
        map.put(12162, new CityAreaMap("花莲县", "花蓮縣", "花東空品區", "http://taqm.epa.gov.tw/taqm/zh-tw/Psi/Hua-Tung.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Hualien_County.htm", HuaLianXian));
        map.put(12164, new CityAreaMap("金门县", "金門縣", "金門", "http://taqm.epa.gov.tw/taqm/zh-tw/PsiIsland/Kinmen.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Kinmen_County.htm", JinMenXian));
        map.put(12165, new CityAreaMap("连江县(马祖)", "连江县(马祖)", "馬祖", "http://taqm.epa.gov.tw/taqm/zh-tw/PsiIsland/Matzu.aspx", "http://www.cwb.gov.tw/V7/forecast/taiwan/inc/city/Lienchiang_County.htm", LianJiangXian));
    }

    public void parseCityWeather() {
        cityWeather.setUnixTime(DateUtility.currentUnixTime());
        cityWeather.setSource(WeatherSourceEnum.TaiWan.toString());
        cityWeather.setCreateDate(DateUtility.currentUnixTime());
        
        JSONObject hourForcast = new JSONObject();
        hourForcast.put("publicDate", "2013-01-1");
        hourForcast.put("hourForcast", new JSONArray());
        cityWeather.setHourForcast(hourForcast.toString());
        
        JSONObject liveInfo = new JSONObject();
        liveInfo.put("publicDate", "2013-01-1");
        cityWeather.setLiveInfo(liveInfo.toString());
        
        parseCurWeather(map.get(cityWeather.getCityId()));
        parseForcastWeather(map.get(cityWeather.getCityId()));

    }

    private void parseForcastWeather(CityAreaMap city) {
        if (city == null) { return; }
        String res = HttpUtility.get(city.weatherUrl, null, "utf-8");
        if (StringUtility.isEmpty(res)) { return; }

        JSONArray dayForcasts = new JSONArray();

        Document doc = Jsoup.parse(res);
        Elements es = doc.getElementsByAttributeValue("class", "FcstBoxTable01");

        Elements weekDayEs = es.select("thead tr th");
        Elements daytimeEs = es.select("tbody tr:lt(1) td");
        Elements nightEs = es.select("tbody tr:lt(2) td");
        for (int i = 1; i < weekDayEs.size(); i++) {
            String date = weekDayEs.get(i).textNodes().get(0).text(); //07/23 星期三
            String daytimeTemp = daytimeEs.get(i - 1).text(); //28 ~ 30
            String nightTemp = nightEs.get(i - 1).text(); //28 ~ 29

            String codeInfo = daytimeEs.get(i - 1).select("img").attr("title");
            int code = YahooCodeFromTaiWanTool.getYahooCode(codeInfo);
            if (code == -1) {
                code = YahooCodeFromChinaTool.getTempYahooCode4Title(codeInfo);
                exceptionList.add(codeInfo + " 预测天气状态  不能解析！");
            }

            String nightCodeInfo = nightEs.get(i - 1).select("img").attr("title");
            int nightCode = YahooCodeFromTaiWanTool.getYahooCode(nightCodeInfo);
            if (nightCode == -1) {
                nightCode = YahooCodeFromChinaTool.getTempYahooCode4Title(nightCodeInfo);
                exceptionList.add(nightCodeInfo + " 预测天气状态  不能解析！");
            }

            if (i == 1) {
                cityWeather.setHigh(Double.parseDouble(daytimeTemp.split("~")[1]));
                cityWeather.setLow(Double.parseDouble(nightTemp.split("~")[0]));
                cityWeather.setCode(code);
                cityWeather.setCodeInfo(codeInfo);
                cityWeather.setNightCode(nightCode);
                cityWeather.setNightCodeInfo(nightCodeInfo);
                cityWeather.setSunriseTime(0);
                cityWeather.setSunsetTime(0);
            }

            JSONObject newDay = new JSONObject();
            Calendar c = Calendar.getInstance();
            date = c.get(Calendar.YEAR) + "-" + date.replaceAll("/", "-");

            newDay.put("startDate", date);
            newDay.put("entDate", date);
            newDay.put("highTemp", Double.parseDouble(daytimeTemp.split("~")[1]));
            newDay.put("code", code);
            newDay.put("codeInfo", codeInfo);

            newDay.put("lowTemp", Double.parseDouble(nightTemp.split("~")[0]));
            newDay.put("codeNight", nightCode);
            newDay.put("codeInfoNight", nightCodeInfo);
            dayForcasts.add(newDay);
        }
        JSONObject result = new JSONObject();
        result.put("dayForcasts", dayForcasts);
        cityWeather.setDayForcast(result.toString());

    }

    private void parseCurWeather(CityAreaMap city) {
        if (city == null) { return; }
        JSONObject error = new JSONObject();

        String url = "http://www.cwb.gov.tw/V7/observe/real/ALL.htm";
        double temp = 0;
        int windLevel = 0;
        String windDirection = "";
        int humidity = 0;
        String curCodeInfo = "";
        String res = HttpUtility.get(url, null, "utf-8");
        Document doc = Jsoup.parse(res);
        Elements es = doc.getElementsByAttributeValue("class", "BoxTable");
        Elements trs = es.get(0).select("tr");
        for (int i = 1; i < trs.size(); i++) {
            String url_area = "http://www.cwb.gov.tw/V7/observe/24real/Data/";
            Elements tds = trs.get(i).select("td");
            Element th = trs.get(i).select("th").get(0);
            String areaName = th.text();
            try {
                if (city.cityName.contains(areaName) || city.areas[0].contains(areaName)) {
                    temp = Double.valueOf(tds.get(0).text());
                    windDirection = tds.get(3).text();
                    windLevel = Integer.parseInt(tds.get(4).text().split("|")[1]);
                    if (NumberUtility.isNumber(tds.get(7).text().replace("-", "-1"))) {
                        humidity = Integer.parseInt(tds.get(7).text().replace("-", "-1"));
                    }
                    curCodeInfo = tds.get(2).text();
                    if (curCodeInfo.equals("X") || curCodeInfo.equals("-")) {
                        url_area += th.select("a").attr("href");
                        String area_response = HttpUtility.get(url_area, null, "utf-8");

                        error.put("url_area1", url_area);
                        error.put("area_response1", area_response);

                        curCodeInfo = parseCurCodeInfo(area_response);
                        if (StringUtility.isNotEmpty(curCodeInfo)) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                error.put("url", url);
                error.put("res", res);
                exceptionList.add("台湾天气抓取 1：" + ExceptionUtils.getFullStackTrace(e) + "【" + error.toString() + "】");
            }
        }

        if (StringUtility.isEmpty(curCodeInfo) && city.areas.length > 1 && StringUtility.isNotEmpty(city.areas[1])) { //如果辖区内所有监测点都没有，实时天气描述，从附近的监测点取
            for (int i = 1; i < trs.size(); i++) {
                String url_area = "http://www.cwb.gov.tw/V7/observe/24real/Data/";
                Elements tds = trs.get(i).select("td");
                Element th = trs.get(i).select("th").get(0);
                String areaName = th.text();
                try {
                    if (city.areas[1].contains(areaName)) {
                        curCodeInfo = tds.get(2).text();
                        if (curCodeInfo.equals("X") || curCodeInfo.equals("-")) {
                            url_area += th.select("a").attr("href");
                            String area_response = HttpUtility.get(url_area, null, "utf-8");

                            error.put("url_area2", url_area);
                            error.put("area_response2", area_response);

                            curCodeInfo = parseCurCodeInfo(area_response);
                            if (StringUtility.isNotEmpty(curCodeInfo)) {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    exceptionList.add("台湾天气抓取2：" + ExceptionUtils.getFullStackTrace(e) + "【" + error.toString() + "】");
                }
            }
        }
        cityWeather.setCurTemp(temp);
        cityWeather.setCurCode(YahooCodeFromTaiWanTool.getYahooCode(curCodeInfo));
        cityWeather.setCurCodeInfo(curCodeInfo);
        cityWeather.setWindLevel(windLevel);
        cityWeather.setWindDirection(windDirection);
        cityWeather.setHumidity(humidity);

        if (cityWeather.getCurCode() == -1) {
            cityWeather.setCurCode(30);
            if (StringUtility.isNotEmpty(curCodeInfo) && !curCodeInfo.equals("X")) {
                exceptionList.add(curCodeInfo + " 当前天气状态 不能解析！");
            }
        }
    }

    private String parseCurCodeInfo(String areaResponse) {
        Document doc = Jsoup.parse(areaResponse);
        Elements es = doc.getElementsByAttributeValue("class", "BoxTable");
        Elements trs = es.get(0).select("tr");
        String curCodeInfo = "";
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            curCodeInfo = tds.get(2).text();
            if (curCodeInfo.equals("X") || curCodeInfo.equals("-")) {
                curCodeInfo = "";
                continue;
            } else {
                break;
            }
        }
        return curCodeInfo;
    }

    ////////////////////////////////////////////////////////////////////

    public void parsePM25Value() {
        cityWeather.setPm25(-1);
        CityAreaMap city = map.get(cityWeather.getCityId());
        if (city == null) { return; }
        try {
            //            StringBuffer sb = new StringBuffer();
            //            sb.append("http://").append(Config.getInstance().getString("proxy.server")).append("/transfers/?uu=");
            //            sb.append(city.pmUrl.substring("http://".length()));

            String url = "http://" + Config.getInstance().getString("proxy.server") + "/proxy/url.jsp?url=";
            url += CipherUtility.AES.encrypt(city.pmUrl, "q1w2e3");

            String res = HttpUtility.get(url);
            if (StringUtility.isEmpty(res)) { return; }
            List<Integer> vals = new ArrayList<Integer>();
            Document doc = Jsoup.parse(res);
            Elements es = doc.getElementsByAttributeValue("class", "TABLE_G");
            Elements trs = null;
            Elements tds = null;
            String areaName = null;
            String valStr = null;
            for (int j = 0; j < es.size(); j++) {
                trs = es.get(j).select("tr");
                for (int i = 2; i < trs.size(); i++) {
                    try {
                        tds = trs.get(i).select("td");
                        areaName = tds.get(0).select("a").get(0).text();
                        valStr = tds.get(1).select("span").get(0).text();
                        if (city.cityName.contains(areaName)) {
                            vals.add(Integer.parseInt(valStr));
                            continue;
                        }
                        if (city.areaName.contains(areaName)) {
                            vals.add(Integer.parseInt(valStr));
                            continue;
                        }
                        for (String area : city.areas) {
                            if (area.contains(areaName)) {
                                vals.add(Integer.parseInt(valStr));
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
            if (vals.size() == 0) return;
            int pm25 = 0;
            for (int p : vals) {
                pm25 += p;
            }
            cityWeather.setPm25(pm25 / vals.size());
        } catch (Exception e) {
        }
    }

    ////////////////////////////////////////////////////////////////////

    private static final class CityAreaMap {
        String   cityNameCh;
        String   cityName;
        String   areaName;
        String   weatherUrl;
        String   pmUrl;
        String[] areas;

        CityAreaMap(String cityNameCh, String cityName, String areaName, String pmUrl, String weatherUrl, String[] areas) {
            this.cityNameCh = cityNameCh;
            this.cityName = cityName;
            this.areaName = areaName;
            this.weatherUrl = weatherUrl;
            this.pmUrl = pmUrl;
            this.areas = areas;
        }

    }

    public List<String> getExceptionList() {
        return exceptionList;
    }
    
    public static void main(String[] args) {
        CityWeatherBean cityWeather = new CityWeatherBean();
        cityWeather.setCityId(12162);
        TaiWanWeatherParsor taiWanWeatherParsor = new TaiWanWeatherParsor(cityWeather);
        taiWanWeatherParsor.parsePM25Value();
        System.out.println(">>> "+cityWeather.getPm25());
    }
}
