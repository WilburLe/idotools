package com.toolbox.weather.data.parsor.pm;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.toolbox.framework.utils.HttpUtility;

public class ChaPM25Parsor {

    //新增，用于单个城市获取数据时，更加直接。
    private static Map<Integer, String> cityMap  = new HashMap<Integer, String>();

    static {
        ////////////////////////城市能与数据库单个匹配的/////////////////////////////////////
        cityMap.put(10097, "http://www.chapm25.com/city/tangshan.html");//唐山
        cityMap.put(11378, "http://www.chapm25.com/city/dezhou.html");//德州
        cityMap.put(11397, "http://www.chapm25.com/city/binzhou.html");//滨州
        cityMap.put(10397, "http://www.chapm25.com/city/huhehaote.html");//呼和浩特
        cityMap.put(11414, "http://www.chapm25.com/city/lanzhou.html");//兰州
        cityMap.put(11311, "http://www.chapm25.com/city/qingdao.html");//青岛
        cityMap.put(10302, "http://www.chapm25.com/city/lianyungang.html");//连云港
        cityMap.put(11316, "http://www.chapm25.com/city/zibo.html");//淄博
        cityMap.put(10282, "http://www.chapm25.com/city/xuzhou.html");//徐州
        cityMap.put(10848, "http://www.chapm25.com/city/zhaoqing.html");//肇庆
        cityMap.put(10167, "http://www.chapm25.com/city/zhangjiakou.html");//张家口
        cityMap.put(11505, "http://www.chapm25.com/city/liuzhou.html");//柳州
        cityMap.put(10320, "http://www.chapm25.com/city/yangzhou.html");//扬州
        cityMap.put(12073, "http://www.chapm25.com/city/yinchuan.html");//银川
        cityMap.put(10610, "http://www.chapm25.com/city/wenzhou.html");//温州
        cityMap.put(10343, "http://www.chapm25.com/city/dalian.html");//大连
        cityMap.put(10604, "http://www.chapm25.com/city/ningbo.html");//宁波
        cityMap.put(11588, "http://www.chapm25.com/city/guiyang.html");//贵阳
        cityMap.put(11326, "http://www.chapm25.com/city/yantai.html");//烟台
        cityMap.put(10296, "http://www.chapm25.com/city/nantong.html");//南通
        cityMap.put(10276, "http://www.chapm25.com/city/nanjing.html");//南京
        cityMap.put(11698, "http://www.chapm25.com/city/tongchuan.html");//铜川
        cityMap.put(10503, "http://www.chapm25.com/city/yangquan.html");//阳泉
        cityMap.put(10651, "http://www.chapm25.com/city/taizhou2.html");//台州
        cityMap.put(10829, "http://www.chapm25.com/city/shantou.html");//汕头
        cityMap.put(10881, "http://www.chapm25.com/city/qingyuan.html");//清远
        cityMap.put(10105, "http://www.chapm25.com/city/qinhuangdao.html");//秦皇岛
        cityMap.put(10625, "http://www.chapm25.com/city/huzhou.html");//湖州
        cityMap.put(10770, "http://www.chapm25.com/city/quanzhou.html");//泉州
        cityMap.put(10598, "http://www.chapm25.com/city/hangzhou.html");//杭州
        cityMap.put(10871, "http://www.chapm25.com/city/heyuan.html");//河源
        cityMap.put(10359, "http://www.chapm25.com/city/dandong.html");//丹东
        cityMap.put(10307, "http://www.chapm25.com/city/huaian.html");//淮安
        cityMap.put(11498, "http://www.chapm25.com/city/nanning.html");//南宁
        cityMap.put(11322, "http://www.chapm25.com/city/dongying.html");//东营
        cityMap.put(11344, "http://www.chapm25.com/city/jining.html");//济宁
        cityMap.put(11031, "http://www.chapm25.com/city/wuhan.html");//武汉
        cityMap.put(10643, "http://www.chapm25.com/city/quzhou.html");//衢州
        cityMap.put(10079, "http://www.chapm25.com/city/shijiazhuang.html");//石家庄
        cityMap.put(10832, "http://www.chapm25.com/city/jiangmen.html");//江门
        cityMap.put(11938, "http://www.chapm25.com/city/kunming.html");//昆明
        cityMap.put(10855, "http://www.chapm25.com/city/huizhou.html");//惠州
        cityMap.put(10827, "http://www.chapm25.com/city/shenzhen.html");//深圳
        cityMap.put(10144, "http://www.chapm25.com/city/baoding.html");//保定
        cityMap.put(10658, "http://www.chapm25.com/city/lishui.html");//丽水
        cityMap.put(10831, "http://www.chapm25.com/city/foshan.html");//佛山
        cityMap.put(10619, "http://www.chapm25.com/city/jiaxing.html");//嘉兴
        cityMap.put(11700, "http://www.chapm25.com/city/baoji.html");//宝鸡
        cityMap.put(10904, "http://www.chapm25.com/city/zhengzhou.html");//郑州
        cityMap.put(11355, "http://www.chapm25.com/city/taian.html");//泰安
        cityMap.put(10747, "http://www.chapm25.com/city/fuzhou.html");//福州
        cityMap.put(10338, "http://www.chapm25.com/city/shenyang.html");//沈阳
        cityMap.put(10368, "http://www.chapm25.com/city/yingkou.html");//营口
        cityMap.put(11367, "http://www.chapm25.com/city/laiwu.html");//莱芜
        cityMap.put(10190, "http://www.chapm25.com/city/cangzhou.html");//沧州
        cityMap.put(11956, "http://www.chapm25.com/city/yuxi.html");//玉溪
        cityMap.put(10226, "http://www.chapm25.com/city/changchun.html");//长春
        cityMap.put(11360, "http://www.chapm25.com/city/weihai.html");//威海
        cityMap.put(10333, "http://www.chapm25.com/city/suqian.html");//宿迁
        cityMap.put(10756, "http://www.chapm25.com/city/xiamen.html");//厦门
        cityMap.put(10668, "http://www.chapm25.com/city/hefei.html");//合肥
        cityMap.put(10312, "http://www.chapm25.com/city/yancheng.html");//盐城
        cityMap.put(11530, "http://www.chapm25.com/city/beihai.html");//北海
        cityMap.put(10403, "http://www.chapm25.com/city/baotou.html");//包头
        cityMap.put(10001, "http://www.chapm25.com/city/haerbin.html");//哈尔滨
        cityMap.put(10205, "http://www.chapm25.com/city/langfang.html");//廊坊
        cityMap.put(10377, "http://www.chapm25.com/city/panjin.html");//盘锦
        cityMap.put(11787, "http://www.chapm25.com/city/chengdu.html");//成都
        cityMap.put(10328, "http://www.chapm25.com/city/taizhou.html");//泰州
        cityMap.put(10490, "http://www.chapm25.com/city/taiyuan.html");//太原
        cityMap.put(10828, "http://www.chapm25.com/city/zhuhai.html");//珠海
        cityMap.put(10214, "http://www.chapm25.com/city/hengshui.html");//衡水
        cityMap.put(10324, "http://www.chapm25.com/city/zhenjiang.html");//镇江
        cityMap.put(10291, "http://www.chapm25.com/city/suzhou.html");//苏州
        cityMap.put(10392, "http://www.chapm25.com/city/huludao.html");//葫芦岛
        cityMap.put(10567, "http://www.chapm25.com/city/linfen.html");//临汾
        cityMap.put(11368, "http://www.chapm25.com/city/linyi.html");//临沂
        cityMap.put(10635, "http://www.chapm25.com/city/jinhua.html");//金华
        cityMap.put(10279, "http://www.chapm25.com/city/wuxi.html");//无锡
        cityMap.put(11320, "http://www.chapm25.com/city/zaozhuang.html");//枣庄
        cityMap.put(10816, "http://www.chapm25.com/city/guangzhou.html");//广州
        cityMap.put(11722, "http://www.chapm25.com/city/weinan.html");//渭南
        cityMap.put(11858, "http://www.chapm25.com/city/lasa.html");//拉萨
        cityMap.put(11673, "http://www.chapm25.com/city/haikou.html");//海口
        cityMap.put(11733, "http://www.chapm25.com/city/yanan.html");//延安
        cityMap.put(10889, "http://www.chapm25.com/city/zhongshan.html");//中山
        cityMap.put(11710, "http://www.chapm25.com/city/xianyang.html");//咸阳
        cityMap.put(11404, "http://www.chapm25.com/city/heze.html");//菏泽
        cityMap.put(10426, "http://www.chapm25.com/city/eeds.html");//鄂尔多斯
        cityMap.put(11364, "http://www.chapm25.com/city/rizhao.html");//日照
        cityMap.put(10819, "http://www.chapm25.com/city/shaoguan.html");//韶关
        cityMap.put(11693, "http://www.chapm25.com/city/xian.html");//西安
        cityMap.put(11335, "http://www.chapm25.com/city/weifang.html");//潍坊
        cityMap.put(10288, "http://www.chapm25.com/city/changzhou.html");//常州
        cityMap.put(10888, "http://www.chapm25.com/city/dongguan.html");//东莞
        cityMap.put(11389, "http://www.chapm25.com/city/liaocheng.html");//聊城
        cityMap.put(12092, "http://www.chapm25.com/city/xining.html");//西宁
        cityMap.put(11306, "http://www.chapm25.com/city/jinan.html");//济南
        cityMap.put(10648, "http://www.chapm25.com/city/zhoushan.html");//舟山
        ////////////////////////////城市与数据库有两个以上匹配的/////////////////////////////////////
        cityMap.put(11213, "http://www.chapm25.com/city/nanchang.html");//南昌 > 南昌市
        cityMap.put(11214, "http://www.chapm25.com/city/nanchang.html");//南昌 > 南昌县
        cityMap.put(12196, "http://www.chapm25.com/city/wulumuqi.html");//乌鲁木齐 > 乌鲁木齐市
        cityMap.put(12197, "http://www.chapm25.com/city/wulumuqi.html");//乌鲁木齐 > 乌鲁木齐县
        cityMap.put(11114, "http://www.chapm25.com/city/zhuzhou.html");//株洲 > 株洲市
        cityMap.put(11116, "http://www.chapm25.com/city/zhuzhou.html");//株洲 > 株洲县
        cityMap.put(10629, "http://www.chapm25.com/city/shaoxing.html");//绍兴 > 绍兴市
        cityMap.put(10633, "http://www.chapm25.com/city/shaoxing.html");//绍兴 > 绍兴县
        cityMap.put(10126, "http://www.chapm25.com/city/xingtai.html");//邢台 > 邢台市　
        cityMap.put(10129, "http://www.chapm25.com/city/xingtai.html");//邢台 > 邢台县
        cityMap.put(10110, "http://www.chapm25.com/city/handan.html");//邯郸 > 邯郸市
        cityMap.put(10112, "http://www.chapm25.com/city/handan.html");//邯郸 > 邯郸县
        cityMap.put(10506, "http://www.chapm25.com/city/changzhi.html");//长治 > 长治市　
        cityMap.put(10508, "http://www.chapm25.com/city/changzhi.html");//长治 > 长治县
        cityMap.put(10181, "http://www.chapm25.com/city/chengde.html");//承德 > 承德市　
        cityMap.put(10182, "http://www.chapm25.com/city/chengde.html");//承德 > 承德县
        cityMap.put(11120, "http://www.chapm25.com/city/xiangtan.html");//湘潭 > 湘潭市
        cityMap.put(11123, "http://www.chapm25.com/city/xiangtan.html");//湘潭 > 湘潭县
        cityMap.put(11110, "http://www.chapm25.com/city/changsha.html");//长沙 > 长沙市
        cityMap.put(11112, "http://www.chapm25.com/city/changsha.html");//长沙 > 长沙县
        cityMap.put(10495, "http://www.chapm25.com/city/datong.html");//大同 > 大同市　
        cityMap.put(10496, "http://www.chapm25.com/city/datong.html");//大同 > 大同县
        //////////////////////////////////////////////////////////////
    }

    private static String               encoding = "UTF-8";

    public static int parsePM25Value(int cityId) {
        try {
            String url = cityMap.get(cityId);
            if (url == null) {
                return -1;
            }
            String htmlStr = HttpUtility.get(url, null, encoding);
            Document doc = Jsoup.parse(htmlStr);
            Elements es = doc.select("div .span8.pmtips");
            Element e = es.get(0).select("span").get(0);
            String pm25 = e.html().substring(0, e.html().indexOf(" "));
            return Integer.parseInt(pm25.trim());
        } catch (Exception e) {
//            System.out.println("cha PM25数据抓取异常:cityId=" + cityId + ", msg=" + e.getMessage());
        }
        return -1;

    }

}
