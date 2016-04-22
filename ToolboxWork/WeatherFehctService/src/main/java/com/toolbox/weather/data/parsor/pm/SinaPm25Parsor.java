package com.toolbox.weather.data.parsor.pm;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.HttpUtility;

public class SinaPm25Parsor {

    private static final Map<Integer, String> cityMap  = new HashMap<Integer, String>();

    static {
        //////////////////////////城市能与数据库单个匹配的/////////////////////////////////////
        cityMap.put(11996, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0378");//临沧市
        cityMap.put(11441, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0270");//平凉市
        cityMap.put(10348, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0004");//鞍山市
        cityMap.put(10848, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1003");//肇庆市
        cityMap.put(12175, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0017");//重庆市
        cityMap.put(10408, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0286");//赤峰市
        cityMap.put(10828, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1000");//珠海市
        cityMap.put(11801, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1085");//攀枝花市
        cityMap.put(11142, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0411");//岳阳市
        cityMap.put(11673, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0502");//海口市
        cityMap.put(10034, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1051");//大庆市
        cityMap.put(11798, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1084");//自贡市
        cityMap.put(12166, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0008");//北京市
        cityMap.put(10770, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0114");//泉州市
        cityMap.put(10205, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1039");//廊坊市
        cityMap.put(11031, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0138");//武汉市
        cityMap.put(11213, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0097");//南昌市
        cityMap.put(12077, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1101");//石嘴山市
        cityMap.put(11307, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0161");//章丘市
        cityMap.put(10674, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0449");//芜湖市
        cityMap.put(10619, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0062");//嘉兴市
        cityMap.put(12092, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0236");//西宁市
        cityMap.put(11697, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2685");//高陵市
        cityMap.put(10888, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0123");//东莞市
        cityMap.put(10673, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1065");//巢湖市
        cityMap.put(10279, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1009");//无锡市
        cityMap.put(10707, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1064");//滁州市
        cityMap.put(12167, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0096");//密云市
        cityMap.put(10818, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1340");//从化市
        cityMap.put(10339, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1918");//新民市
        cityMap.put(10651, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1263");//台州市
        cityMap.put(10635, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1017");//金华市
        cityMap.put(10974, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1265");//三门峡市
        cityMap.put(10610, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0462");//温州市
        cityMap.put(11431, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1100");//武威市
        cityMap.put(10397, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0249");//呼和浩特市
        cityMap.put(10934, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0269");//安阳市
        cityMap.put(10689, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1059");//淮北市
        cityMap.put(12039, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0380");//景洪市
        cityMap.put(10855, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0053");//惠州市
        cityMap.put(11970, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0364");//昭通市
        cityMap.put(10827, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0120");//深圳市
        cityMap.put(11414, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0079");//兰州市
        cityMap.put(11530, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0499");//北海市
        cityMap.put(10282, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0437");//徐州市
        cityMap.put(11597, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0419");//遵义市
        cityMap.put(11809, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1086");//德阳市
        cityMap.put(10328, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1008");//泰州市
        cityMap.put(10226, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0010");//长春市
        cityMap.put(10720, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1058");//宿州市
        cityMap.put(10144, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0308");//保定市
        cityMap.put(11793, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1325");//双流市
        cityMap.put(10320, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1013");//扬州市
        cityMap.put(10816, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0037");//广州市
        cityMap.put(12377, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0362");//宜宾市
        cityMap.put(10296, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0101");//南通市
        cityMap.put(11815, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0351");//绵阳市
        cityMap.put(10759, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1005");//三明市
        cityMap.put(10363, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0067");//锦州市
        cityMap.put(10001, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0046");//哈尔滨市
        cityMap.put(11070, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0408");//荆州市
        cityMap.put(10302, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1012");//连云港市
        cityMap.put(10806, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1262");//宁德市
        cityMap.put(11120, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0142");//湘潭市
        cityMap.put(10625, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0056");//湖州市
        cityMap.put(10503, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0152");//阳泉市
        cityMap.put(10691, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1063");//铜陵市
        cityMap.put(10007, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1990");//宾县市
        cityMap.put(11114, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1079");//株洲市
        cityMap.put(10817, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1339");//增城市
        cityMap.put(10214, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1041");//衡水市
        cityMap.put(10685, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1060");//马鞍山市
        cityMap.put(10599, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1483");//建德市
        cityMap.put(12173, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0116");//上海市
        cityMap.put(10097, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0131");//唐山市
        cityMap.put(12169, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0133");//天津市
        cityMap.put(10307, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1011");//淮安市
        cityMap.put(10757, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0045");//莆田市
        cityMap.put(10167, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0300");//张家口市
        cityMap.put(10629, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0117");//绍兴市
        cityMap.put(10324, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0166");//镇江市
        cityMap.put(11710, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0143");//咸阳市
        cityMap.put(10693, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0452");//安庆市
        cityMap.put(11956, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1284");//玉溪市
        cityMap.put(10648, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0455");//舟山市
        cityMap.put(10927, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1031");//平顶山市
        cityMap.put(11789, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2365");//彭州市
        cityMap.put(10889, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1002");//中山市
        cityMap.put(10403, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0007");//包头市
        cityMap.put(11309, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1527");//济阳市
        cityMap.put(10679, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0444");//蚌埠市
        cityMap.put(11480, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1097");//临夏市
        cityMap.put(10725, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1061");//六安市
        cityMap.put(11788, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2364");//都江堰市
        cityMap.put(10012, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0112");//齐齐哈尔市
        cityMap.put(10506, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1043");//长治市
        cityMap.put(11674, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0507");//三亚市
        cityMap.put(10105, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1038");//秦皇岛市
        cityMap.put(12067, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2621");//香格里拉市
        cityMap.put(11696, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2684");//户县市
        cityMap.put(10495, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0251");//大同市
        cityMap.put(10333, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1010");//宿迁市
        cityMap.put(12196, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0135");//乌鲁木齐市
        cityMap.put(10181, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0302");//承德市
        cityMap.put(11938, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0076");//昆明市
        cityMap.put(12171, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0065");//静海市
        cityMap.put(11986, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0381");//普洱市
        cityMap.put(10074, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0023");//大兴市
        cityMap.put(10600, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1484");//富阳市
        cityMap.put(10911, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0072");//开封市
        cityMap.put(10490, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0129");//太原市
        cityMap.put(10356, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0296");//本溪市
        cityMap.put(11965, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0370");//保山市
        cityMap.put(11225, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0068");//九江市
        cityMap.put(11421, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0006");//白银市
        cityMap.put(11588, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0039");//贵阳市
        cityMap.put(11844, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0098");//南充市
        cityMap.put(11947, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1096");//曲靖市
        cityMap.put(11311, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0110");//青岛市
        cityMap.put(10668, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0448");//合肥市
        cityMap.put(10567, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1042");//临汾市
        cityMap.put(12005, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0373");//楚雄市
        cityMap.put(11435, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0228");//张掖市
        cityMap.put(12180, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0405");//梁平市
        cityMap.put(10832, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0058");//江门市
        cityMap.put(11313, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1326");//即墨市
        cityMap.put(11498, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0100");//南宁市
        cityMap.put(11505, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0479");//柳州市
        cityMap.put(11306, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0064");//济南市
        cityMap.put(10110, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1037");//邯郸市
        cityMap.put(11693, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0141");//西安市
        cityMap.put(12198, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0200");//克拉玛依市
        cityMap.put(10598, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0044");//杭州市
        cityMap.put(10702, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2058");//黄山市
        cityMap.put(11425, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0386");//天水市
        cityMap.put(11463, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0024");//定西市
        cityMap.put(10604, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1016");//宁波市
        cityMap.put(11448, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0226");//酒泉市
        cityMap.put(10352, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0029");//抚顺市
        cityMap.put(11787, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0016");//成都市
        cityMap.put(10789, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0471");//南平市
        cityMap.put(10756, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0140");//厦门市
        cityMap.put(10917, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0086");//洛阳市
        cityMap.put(10288, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0015");//常州市
        cityMap.put(10386, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2972");//朝阳市
        cityMap.put(10312, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1014");//盐城市
        cityMap.put(10952, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1032");//焦作市
        cityMap.put(10126, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0266");//邢台市
        cityMap.put(10658, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0461");//丽水市
        cityMap.put(12170, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2931");//宁河市
        cityMap.put(11722, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0137");//渭南市
        cityMap.put(10276, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0099");//南京市
        cityMap.put(11700, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0387");//宝鸡市
        cityMap.put(10343, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0019");//大连市
        cityMap.put(10799, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1006");//龙岩市
        cityMap.put(10340, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1919");//辽中市
        cityMap.put(11858, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0080");//拉萨市
        cityMap.put(10291, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1007");//苏州市
        cityMap.put(12073, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0259");//银川市
        cityMap.put(11733, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0267");//延安市
        cityMap.put(10079, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0122");//石家庄市
        cityMap.put(10739, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1066");//宣城市
        cityMap.put(10831, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0028");//佛山市
        cityMap.put(11694, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2682");//蓝田市
        cityMap.put(10683, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1062");//淮南市
        cityMap.put(10747, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0031");//福州市
        cityMap.put(12018, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0385");//蒙自市
        cityMap.put(10735, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1273");//池州市
        cityMap.put(10342, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1921");//法库市
        cityMap.put(11158, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1278");//张家界市
        cityMap.put(10003, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0124");//双城市
        cityMap.put(10714, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0442");//阜阳市
        cityMap.put(11418, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1098");//嘉峪关市
        cityMap.put(10338, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0119");//沈阳市
        cityMap.put(11419, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1099");//金昌市
        cityMap.put(11042, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0407");//宜昌市
        cityMap.put(11455, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0271");//庆阳市
        cityMap.put(11698, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1289");//铜川市
        cityMap.put(11981, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0365");//丽江市
        cityMap.put(12043, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0371");//大理市
        cityMap.put(10190, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1040");//沧州市
        cityMap.put(10051, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0278");//牡丹江市
        cityMap.put(10004, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1987");//五常市
        cityMap.put(10904, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0165");//郑州市
        cityMap.put(11150, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0416");//常德市
        cityMap.put(11110, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0013");//长沙市
        cityMap.put(11512, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0434");//桂林市
        cityMap.put(12168, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1308");//延庆市
        cityMap.put(10779, "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0162");//漳州市
        cityMap.put(12030, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1285");//文山市
        ////////////////////////////城市与数据库有两个以上匹配的/////////////////////////////////////
        cityMap.put(10017, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1997");//甘南市 > 甘南县
        cityMap.put(11488, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1997");//甘南市 > 甘南藏族自治州
        cityMap.put(10008, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1991");//巴彦市 > 巴彦县
        cityMap.put(10447, "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1991");//巴彦市 > 巴彦淖尔市
        ////////////////////////////城市与数据库没有匹配到的/////////////////////////////////////
        //cityMap.put("永川市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0157");//永川市
        //cityMap.put("温江市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2363");//温江市
        //cityMap.put("长清市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1526");//长清市
        //cityMap.put("大足市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1309");//大足市
        //cityMap.put("宝山市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1294");//宝山市
        //cityMap.put("徐家汇市",   "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2973");//徐家汇市
        //cityMap.put("胶南市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0060");//胶南市
        //cityMap.put("新都市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2362");//新都市
        //cityMap.put("金州市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1922");//金州市
        //cityMap.put("津南市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2928");//津南市
        //cityMap.put("通州市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1307");//通州市
        //cityMap.put("昌平市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0012");//昌平市
        //cityMap.put("石景山市",   "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2974");//石景山市
        //cityMap.put("渝北市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2935");//渝北市
        //cityMap.put("黄陂市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0050");//黄陂市
        //cityMap.put("江夏市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2169");//江夏市
        //cityMap.put("花都市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1338");//花都市
        //cityMap.put("余杭市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2950");//余杭市
        //cityMap.put("海淀市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2971");//海淀市
        //cityMap.put("丰台市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0027");//丰台市
        //cityMap.put("浦口市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0109");//浦口市
        //cityMap.put("松江市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1301");//松江市
        //cityMap.put("万盛市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2934");//万盛市
        //cityMap.put("怀柔市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1303");//怀柔市
        //cityMap.put("临潼市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0082");//临潼市
        //cityMap.put("塘沽市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0130");//塘沽市
        //cityMap.put("万州市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1105");//万州市
        //cityMap.put("北碚市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2933");//北碚市
        //cityMap.put("顺义市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1306");//顺义市
        //cityMap.put("蔡甸市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2168");//蔡甸市
        //cityMap.put("汉沽市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0043");//汉沽市
        //cityMap.put("萧山市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1482");//萧山市
        //cityMap.put("龙泉驿市",   "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2361");//龙泉驿市
        //cityMap.put("大港市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2925");//大港市
        //cityMap.put("西青市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2927");//西青市
        //cityMap.put("平谷市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1305");//平谷市
        //cityMap.put("潞西市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1121");//潞西市
        //cityMap.put("浦东市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1336");//浦东市
        //cityMap.put("青浦市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1300");//青浦市
        //cityMap.put("旅顺市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0087");//旅顺市
        //cityMap.put("东丽市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2926");//东丽市
        //cityMap.put("番禺市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1337");//番禺市
        //cityMap.put("巴南市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2936");//巴南市
        //cityMap.put("呼兰市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0054");//呼兰市
        //cityMap.put("北辰市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2929");//北辰市
        //cityMap.put("门头沟市",   "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1304");//门头沟市
        //cityMap.put("长安市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX2681");//长安市
        //cityMap.put("房山市",    "http://forecast.sina.cn/app/data/aqi.php?city=WMXX1302");//房山市
        //cityMap.put("阿城市",    "http://forecast.sina.cn/app/data/aqi.php?city=CHXX0001");//阿城市

    }
    private static String                     encoding = "UTF-8";

    public static int parsePM25Value(int cityId) {
        try {
            String url = cityMap.get(cityId);
            if (url == null) {
                return -1;
            }
            String res = HttpUtility.get(url, null, encoding);
            Document doc = Jsoup.parse(res);
            Elements es = doc.select("div .status");
            int pm25 = Integer.parseInt(es.get(0).select("strong").text());
            return pm25;
        } catch (Exception e) {
            //            System.out.println("sina PM25数据抓取异常:cityId=" + cityId + ", msg=" + e.getMessage());
        }
        return -1;
    }

    //    private static void initCityUrl() {
    //        String res = HttpUtility.get("http://forecast.sina.cn/app/data/aqi.php?city=CHXX0259", null, encoding);
    //        Document doc = Jsoup.parse(res);
    //        int count = 0;
    //        count += deal(doc, "A-G");
    //        count += deal(doc, "H-L");
    //        count += deal(doc, "M-T");
    //        count += deal(doc, "W-Z");
    //        System.out.println(count);
    //    }

    //    private static int deal(Document doc, String attrValue) {
    //        Elements es = doc.getElementsByAttributeValue("data-id", attrValue);
    //        Elements li = es.get(1).select("li");
    //
    //        for (int i = 0; i < li.size(); i++) {
    //            Element e = li.get(i).select("a").get(0);
    //            System.out.println("map.put(\"" + e.text() + "市\",\"http://forecast.sina.cn" + e.attr("href") + "\");");
    //        }
    //        return li.size();
    //    }

}
