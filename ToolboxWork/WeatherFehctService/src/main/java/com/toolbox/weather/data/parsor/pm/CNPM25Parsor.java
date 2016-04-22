package com.toolbox.weather.data.parsor.pm;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.toolbox.framework.utils.HttpUtility;

public class CNPM25Parsor {

    private static Pattern              pattern = Pattern.compile("jin_value = \"(\\d{1,})\"");
    //新增，用于单个城市获取数据时，更加直接。
    private static Map<Integer, String> cityMap = new HashMap<Integer, String>();

    static {
        ////////////////////////////城市能与数据库单个匹配的/////////////////////////////////////
        cityMap.put(10725, "http://www.cnpm25.cn/city/liuan.html");//六安
        cityMap.put(10097, "http://www.cnpm25.cn/city/tangshan.html");//唐山
        cityMap.put(11965, "http://www.cnpm25.cn/city/baoshan.html");//保山
        cityMap.put(11158, "http://www.cnpm25.cn/city/zhangjiajie.html");//张家界
        cityMap.put(11397, "http://www.cnpm25.cn/city/binzhou.html");//滨州
        cityMap.put(10707, "http://www.cnpm25.cn/city/chuzhou.html");//滁州
        cityMap.put(12388, "http://www.cnpm25.cn/city/ziyang.html");//资阳
        cityMap.put(10302, "http://www.cnpm25.cn/city/lianyungang.html");//连云港
        cityMap.put(11316, "http://www.cnpm25.cn/city/zibo.html");//淄博
        cityMap.put(10282, "http://www.cnpm25.cn/city/xuzhou.html");//徐州
        cityMap.put(12073, "http://www.cnpm25.cn/city/yinchuan.html");//银川
        cityMap.put(10320, "http://www.cnpm25.cn/city/yangzhou.html");//扬州
        cityMap.put(11505, "http://www.cnpm25.cn/city/liuzhou.html");//柳州
        cityMap.put(10343, "http://www.cnpm25.cn/city/dalian.html");//大连
        cityMap.put(10610, "http://www.cnpm25.cn/city/wenzhou.html");//温州
        cityMap.put(11218, "http://www.cnpm25.cn/city/jingdezhen.html");//景德镇
        cityMap.put(10604, "http://www.cnpm25.cn/city/ningbo.html");//宁波
        cityMap.put(11063, "http://www.cnpm25.cn/city/xiaogan.html");//孝感
        cityMap.put(10720, "http://www.cnpm25.cn/city/suzhoushi.html");//宿州
        cityMap.put(11326, "http://www.cnpm25.cn/city/yantai.html");//烟台
        cityMap.put(10408, "http://www.cnpm25.cn/city/chifeng.html");//赤峰
        cityMap.put(11029, "http://www.cnpm25.cn/city/jiyuan.html");//济源
        cityMap.put(10952, "http://www.cnpm25.cn/city/jiaozuo.html");//焦作
        cityMap.put(10276, "http://www.cnpm25.cn/city/nanjing.html");//南京
        cityMap.put(10296, "http://www.cnpm25.cn/city/nantong.html");//南通
        cityMap.put(11282, "http://www.cnpm25.cn/city/fuzhoushi.html");//抚州
        cityMap.put(10685, "http://www.cnpm25.cn/city/maanshan.html");//马鞍山
        cityMap.put(10651, "http://www.cnpm25.cn/city/taizhou.html");//台州
        cityMap.put(10829, "http://www.cnpm25.cn/city/shantou.html");//汕头
        cityMap.put(11844, "http://www.cnpm25.cn/city/nanchong.html");//南充
        cityMap.put(10363, "http://www.cnpm25.cn/city/jinzhou.html");//锦州
        cityMap.put(12169, "http://www.cnpm25.cn/city/tianjin.html");//天津
        cityMap.put(10971, "http://www.cnpm25.cn/city/luohe.html");//漯河
        cityMap.put(10625, "http://www.cnpm25.cn/city/huzhou.html");//湖州
        cityMap.put(11019, "http://www.cnpm25.cn/city/zhumadian.html");//驻马店
        cityMap.put(10871, "http://www.cnpm25.cn/city/heyuan.html");//河源
        cityMap.put(11042, "http://www.cnpm25.cn/city/yichang.html");//宜昌
        cityMap.put(10359, "http://www.cnpm25.cn/city/dandong.html");//丹东
        cityMap.put(10992, "http://www.cnpm25.cn/city/shangqiu.html");//商丘
        cityMap.put(11970, "http://www.cnpm25.cn/city/shaotong.html");//昭通
        cityMap.put(10974, "http://www.cnpm25.cn/city/sanmenxia.html");//三门峡
        cityMap.put(10307, "http://www.cnpm25.cn/city/huaian.html");//淮安
        cityMap.put(10034, "http://www.cnpm25.cn/city/daqing.html");//大庆
        cityMap.put(11498, "http://www.cnpm25.cn/city/nanning.html");//南宁
        cityMap.put(10079, "http://www.cnpm25.cn/city/shijiazhuang.html");//石家庄
        cityMap.put(11000, "http://www.cnpm25.cn/city/xinyang.html");//信阳
        cityMap.put(11836, "http://www.cnpm25.cn/city/leshan.html");//乐山
        cityMap.put(10735, "http://www.cnpm25.cn/city/chizhou.html");//池州
        cityMap.put(10683, "http://www.cnpm25.cn/city/huainan.html");//淮南
        cityMap.put(10447, "http://www.cnpm25.cn/city/bayanchuoer.html");//巴彦淖尔
        cityMap.put(10827, "http://www.cnpm25.cn/city/shenzhen.html");//深圳
        cityMap.put(10731, "http://www.cnpm25.cn/city/bozhou.html");//亳州
        cityMap.put(11947, "http://www.cnpm25.cn/city/qujing.html");//曲靖
        cityMap.put(10658, "http://www.cnpm25.cn/city/lishui.html");//丽水
        cityMap.put(10940, "http://www.cnpm25.cn/city/hebi.html");//鹤壁
        cityMap.put(10831, "http://www.cnpm25.cn/city/foshan.html");//佛山
        cityMap.put(10619, "http://www.cnpm25.cn/city/jiaxing.html");//嘉兴
        cityMap.put(11700, "http://www.cnpm25.cn/city/baoji.html");//宝鸡
        cityMap.put(10980, "http://www.cnpm25.cn/city/nanyang.html");//南阳
        cityMap.put(10338, "http://www.cnpm25.cn/city/shenyang.html");//沈阳
        cityMap.put(10368, "http://www.cnpm25.cn/city/yingkou.html");//营口
        cityMap.put(10679, "http://www.cnpm25.cn/city/bengpu.html");//蚌埠
        cityMap.put(10837, "http://www.cnpm25.cn/city/zhanjiang.html");//湛江
        cityMap.put(12368, "http://www.cnpm25.cn/city/yaan.html");//雅安
        cityMap.put(12029, "http://www.cnpm25.cn/city/wenshan.html");//文山壮族苗族自治州
        cityMap.put(10927, "http://www.cnpm25.cn/city/pingdingshan.html");//平顶山
        cityMap.put(11956, "http://www.cnpm25.cn/city/yuxi.html");//玉溪
        cityMap.put(10226, "http://www.cnpm25.cn/city/changchun.html");//长春
        cityMap.put(11093, "http://www.cnpm25.cn/city/suizhou.html");//随州
        cityMap.put(11360, "http://www.cnpm25.cn/city/weihai.html");//威海
        cityMap.put(12039, "http://www.cnpm25.cn/city/jinghong.html");//景洪
        cityMap.put(10333, "http://www.cnpm25.cn/city/suqian.html");//宿迁
        cityMap.put(11105, "http://www.cnpm25.cn/city/xiantao.html");//仙桃
        cityMap.put(11674, "http://www.cnpm25.cn/city/sanya.html");//三亚
        cityMap.put(10668, "http://www.cnpm25.cn/city/hefei.html");//合肥
        cityMap.put(10312, "http://www.cnpm25.cn/city/yancheng.html");//盐城
        cityMap.put(11530, "http://www.cnpm25.cn/city/beihai.html");//北海
        cityMap.put(10403, "http://www.cnpm25.cn/city/baotou.html");//包头
        cityMap.put(11981, "http://www.cnpm25.cn/city/lijiang.html");//丽江
        cityMap.put(10490, "http://www.cnpm25.cn/city/taiyuan.html");//太原
        cityMap.put(11221, "http://www.cnpm25.cn/city/pingxiang.html");//萍乡
        cityMap.put(10828, "http://www.cnpm25.cn/city/zhuhai.html");//珠海
        cityMap.put(11804, "http://www.cnpm25.cn/city/luzhou.html");//泸州
        cityMap.put(10291, "http://www.cnpm25.cn/city/suzhou.html");//苏州
        cityMap.put(10392, "http://www.cnpm25.cn/city/huludao.html");//葫芦岛
        cityMap.put(10635, "http://www.cnpm25.cn/city/jinhua.html");//金华
        cityMap.put(11320, "http://www.cnpm25.cn/city/zaozhuang.html");//枣庄
        cityMap.put(10279, "http://www.cnpm25.cn/city/wuxi.html");//无锡
        cityMap.put(11828, "http://www.cnpm25.cn/city/suiing.html");//遂宁
        cityMap.put(10816, "http://www.cnpm25.cn/city/guangzhou.html");//广州
        cityMap.put(10789, "http://www.cnpm25.cn/city/nanping.html");//南平
        cityMap.put(11710, "http://www.cnpm25.cn/city/xianyang.html");//咸阳
        cityMap.put(11242, "http://www.cnpm25.cn/city/ganzhou.html");//赣州
        cityMap.put(11404, "http://www.cnpm25.cn/city/heze.html");//菏泽
        cityMap.put(11878, "http://www.cnpm25.cn/city/shannan.html");//山南
        cityMap.put(10426, "http://www.cnpm25.cn/city/eerduozi.html");//鄂尔多斯
        cityMap.put(11693, "http://www.cnpm25.cn/city/xian.html");//西安
        cityMap.put(11070, "http://www.cnpm25.cn/city/jingzhou.html");//荆州
        cityMap.put(12092, "http://www.cnpm25.cn/city/xining.html");//西宁
        cityMap.put(11032, "http://www.cnpm25.cn/city/huangshi.html");//黄石
        cityMap.put(10518, "http://www.cnpm25.cn/city/jincheng.html");//晋城
        cityMap.put(11306, "http://www.cnpm25.cn/city/jinan.html");//济南
        cityMap.put(10648, "http://www.cnpm25.cn/city/zhoushan.html");//舟山
        cityMap.put(10434, "http://www.cnpm25.cn/city/hulunbeier.html");//呼伦贝尔
        cityMap.put(12175, "http://www.cnpm25.cn/city/chongqing.html");//重庆
        cityMap.put(11239, "http://www.cnpm25.cn/city/yingtan.html");//鹰潭
        cityMap.put(11009, "http://www.cnpm25.cn/city/zhoukou.html");//周口
        cityMap.put(11378, "http://www.cnpm25.cn/city/dezhou.html");//德州
        cityMap.put(11106, "http://www.cnpm25.cn/city/qianjiang.html");//潜江
        cityMap.put(10693, "http://www.cnpm25.cn/city/anqing.html");//安庆
        cityMap.put(10397, "http://www.cnpm25.cn/city/huhehaote.html");//呼和浩特
        cityMap.put(10714, "http://www.cnpm25.cn/city/fuyang.html");//阜阳
        cityMap.put(11414, "http://www.cnpm25.cn/city/lanzhou.html");//兰州
        cityMap.put(11311, "http://www.cnpm25.cn/city/qingdao.html");//青岛
        cityMap.put(11058, "http://www.cnpm25.cn/city/ezhou.html");//鄂州
        cityMap.put(11809, "http://www.cnpm25.cn/city/deyang.html");//德阳
        cityMap.put(10848, "http://www.cnpm25.cn/city/zhaoqing.html");//肇庆
        cityMap.put(10167, "http://www.cnpm25.cn/city/zhangjiakou.html");//张家口
        cityMap.put(11588, "http://www.cnpm25.cn/city/guiyang.html");//贵阳
        cityMap.put(11059, "http://www.cnpm25.cn/city/jingmen.html");//荆门
        cityMap.put(11698, "http://www.cnpm25.cn/city/tongchuan.html");//铜川
        cityMap.put(10503, "http://www.cnpm25.cn/city/yangquan.html");//阳泉
        cityMap.put(10051, "http://www.cnpm25.cn/city/mudanjiang.html");//牡丹江
        cityMap.put(12317, "http://www.cnpm25.cn/city/dazhou.html");//达州
        cityMap.put(11512, "http://www.cnpm25.cn/city/guilin.html");//桂林
        cityMap.put(10012, "http://www.cnpm25.cn/city/qiqihaer.html");//齐齐哈尔
        cityMap.put(10881, "http://www.cnpm25.cn/city/qingyuan.html");//清远
        cityMap.put(10466, "http://www.cnpm25.cn/city/erlianhaote.html");//二连浩特
        cityMap.put(10702, "http://www.cnpm25.cn/city/huangshan.html");//黄山
        cityMap.put(10105, "http://www.cnpm25.cn/city/qinhuangdao.html");//秦皇岛
        cityMap.put(10673, "http://www.cnpm25.cn/city/chaohushi.html");//巢湖
        cityMap.put(11051, "http://www.cnpm25.cn/city/xiangyan.html");//襄阳
        cityMap.put(12198, "http://www.cnpm25.cn/city/kelamayi.html");//克拉玛依
        cityMap.put(10689, "http://www.cnpm25.cn/city/huaibei.html");//淮北
        cityMap.put(10770, "http://www.cnpm25.cn/city/quanzhou.html");//泉州
        cityMap.put(10598, "http://www.cnpm25.cn/city/hangzhou.html");//杭州
        cityMap.put(10407, "http://www.cnpm25.cn/city/wuhai.html");//乌海
        cityMap.put(10540, "http://www.cnpm25.cn/city/yuncheng.html");//运城
        cityMap.put(11322, "http://www.cnpm25.cn/city/dongying.html");//东营
        cityMap.put(11344, "http://www.cnpm25.cn/city/jining.html");//济宁
        cityMap.put(11798, "http://www.cnpm25.cn/city/zigong.html");//自贡
        cityMap.put(10799, "http://www.cnpm25.cn/city/longyan.html");//龙岩
        cityMap.put(11031, "http://www.cnpm25.cn/city/wuhan.html");//武汉
        cityMap.put(10643, "http://www.cnpm25.cn/city/quzhou.html");//衢州
        cityMap.put(12166, "http://www.cnpm25.cn/city/beijing.html");//北京
        cityMap.put(10832, "http://www.cnpm25.cn/city/jiangmen.html");//江门
        cityMap.put(11938, "http://www.cnpm25.cn/city/kunming.html");//昆明
        cityMap.put(11087, "http://www.cnpm25.cn/city/xianning.html");//咸宁
        cityMap.put(10855, "http://www.cnpm25.cn/city/huizhou.html");//惠州
        cityMap.put(10144, "http://www.cnpm25.cn/city/baoding.html");//保定
        cityMap.put(11851, "http://www.cnpm25.cn/city/menshan.html");//眉山
        cityMap.put(10759, "http://www.cnpm25.cn/city/sanming.html");//三明
        cityMap.put(11419, "http://www.cnpm25.cn/city/jinchang.html");//金昌
        cityMap.put(11815, "http://www.cnpm25.cn/city/mianyang.html");//绵阳
        cityMap.put(10904, "http://www.cnpm25.cn/city/zhengzhou.html");//郑州
        cityMap.put(10757, "http://www.cnpm25.cn/city/putian.html");//莆田
        cityMap.put(11355, "http://www.cnpm25.cn/city/taian.html");//泰安
        cityMap.put(10747, "http://www.cnpm25.cn/city/fuzhou.html");//福州
        cityMap.put(11367, "http://www.cnpm25.cn/city/laiwu.html");//莱芜
        cityMap.put(11272, "http://www.cnpm25.cn/city/yichun.html");//宜春
        cityMap.put(11237, "http://www.cnpm25.cn/city/xinyu.html");//新余
        cityMap.put(11035, "http://www.cnpm25.cn/city/shiyan.html");//十堰
        cityMap.put(10524, "http://www.cnpm25.cn/city/shuozhou.html");//朔州
        cityMap.put(10190, "http://www.cnpm25.cn/city/cangzhou.html");//沧州
        cityMap.put(10348, "http://www.cnpm25.cn/city/anshan.html");//鞍山
        cityMap.put(10756, "http://www.cnpm25.cn/city/xiamen.html");//厦门
        cityMap.put(11823, "http://www.cnpm25.cn/city/guangyuan.html");//广元
        cityMap.put(11107, "http://www.cnpm25.cn/city/tianmen.html");//天门
        cityMap.put(12312, "http://www.cnpm25.cn/city/bazhong.html");//巴中
        cityMap.put(10001, "http://www.cnpm25.cn/city/haerbin.html");//哈尔滨
        cityMap.put(11787, "http://www.cnpm25.cn/city/chengdu.html");//成都
        cityMap.put(10205, "http://www.cnpm25.cn/city/langfang.html");//廊坊
        cityMap.put(10377, "http://www.cnpm25.cn/city/panjin.html");//盘锦
        cityMap.put(10328, "http://www.cnpm25.cn/city/taizhoushi.html");//泰州
        cityMap.put(11801, "http://www.cnpm25.cn/city/panzhihua.html");//攀枝花
        cityMap.put(10324, "http://www.cnpm25.cn/city/zhenjiang.html");//镇江
        cityMap.put(11077, "http://www.cnpm25.cn/city/huanggan.html");//黄冈
        cityMap.put(10214, "http://www.cnpm25.cn/city/hengshui.html");//衡水
        cityMap.put(12077, "http://www.cnpm25.cn/city/shizuishan.html");//石嘴山
        cityMap.put(10553, "http://www.cnpm25.cn/city/qizhou.html");//忻州
        cityMap.put(10567, "http://www.cnpm25.cn/city/linfen.html");//临汾
        cityMap.put(11368, "http://www.cnpm25.cn/city/linyi.html");//临沂
        cityMap.put(11150, "http://www.cnpm25.cn/city/changde.html");//常德
        cityMap.put(10739, "http://www.cnpm25.cn/city/xuancheng.html");//宣城
        cityMap.put(11921, "http://www.cnpm25.cn/city/alidiqu.html");//阿里
        cityMap.put(11996, "http://www.cnpm25.cn/city/lincang.html");//临沧
        cityMap.put(11722, "http://www.cnpm25.cn/city/weinan.html");//渭南
        cityMap.put(11858, "http://www.cnpm25.cn/city/lasa.html");//拉萨
        cityMap.put(11733, "http://www.cnpm25.cn/city/yanan.html");//延安
        cityMap.put(11673, "http://www.cnpm25.cn/city/haikou.html");//海口
        cityMap.put(10889, "http://www.cnpm25.cn/city/zhongshan.html");//中山
        cityMap.put(12335, "http://www.cnpm25.cn/city/kangding.html");//康定
        cityMap.put(12173, "http://www.cnpm25.cn/city/shanghai.html");//上海
        cityMap.put(10584, "http://www.cnpm25.cn/city/luliang.html");//吕梁
        cityMap.put(11364, "http://www.cnpm25.cn/city/rizhao.html");//日照
        cityMap.put(10529, "http://www.cnpm25.cn/city/jinzhong.html");//晋中
        cityMap.put(11832, "http://www.cnpm25.cn/city/neijiang.html");//内江
        cityMap.put(10819, "http://www.cnpm25.cn/city/shaoguan.html");//韶关
        cityMap.put(11335, "http://www.cnpm25.cn/city/weifang.html");//潍坊
        cityMap.put(10288, "http://www.cnpm25.cn/city/changzhou.html");//常州
        cityMap.put(10888, "http://www.cnpm25.cn/city/dongguan.html");//东莞
        cityMap.put(10917, "http://www.cnpm25.cn/city/luoyang.html");//洛阳
        cityMap.put(11389, "http://www.cnpm25.cn/city/liaocheng.html");//聊城
        cityMap.put(10418, "http://www.cnpm25.cn/city/tongliao.html");//通辽
        ////////////////////////////城市与数据库有两个以上匹配的/////////////////////////////////////
        cityMap.put(11213, "http://www.cnpm25.cn/city/nanchang.html");//南昌 > 南昌市
        cityMap.put(11214, "http://www.cnpm25.cn/city/nanchang.html");//南昌 > 南昌县
        cityMap.put(12196, "http://www.cnpm25.cn/city/wulumuqi.html");//乌鲁木齐 > 乌鲁木齐市
        cityMap.put(12197, "http://www.cnpm25.cn/city/wulumuqi.html");//乌鲁木齐 > 乌鲁木齐县
        cityMap.put(11114, "http://www.cnpm25.cn/city/zhuzhou.html");//株洲 > 株洲市
        cityMap.put(11116, "http://www.cnpm25.cn/city/zhuzhou.html");//株洲 > 株洲县
        cityMap.put(11597, "http://www.cnpm25.cn/city/zunyi.html");//遵义 > 遵义市
        cityMap.put(11600, "http://www.cnpm25.cn/city/zunyi.html");//遵义 > 遵义县
        cityMap.put(11142, "http://www.cnpm25.cn/city/yueyang.html");//岳阳 > 岳阳市
        cityMap.put(11145, "http://www.cnpm25.cn/city/yueyang.html");//岳阳 > 岳阳县
        cityMap.put(10371, "http://www.cnpm25.cn/city/fuxin.html");//阜新 > 阜新市
        cityMap.put(10372, "http://www.cnpm25.cn/city/fuxin.html");//阜新 > 阜新蒙古族自治县
        cityMap.put(10126, "http://www.cnpm25.cn/city/xingtai.html");//邢台 > 邢台市　
        cityMap.put(10129, "http://www.cnpm25.cn/city/xingtai.html");//邢台 > 邢台县
        cityMap.put(10110, "http://www.cnpm25.cn/city/handan.html");//邯郸 > 邯郸市
        cityMap.put(10112, "http://www.cnpm25.cn/city/handan.html");//邯郸 > 邯郸县
        cityMap.put(10352, "http://www.cnpm25.cn/city/fushun.html");//抚顺 > 抚顺市
        cityMap.put(10353, "http://www.cnpm25.cn/city/fushun.html");//抚顺 > 抚顺县
        cityMap.put(10225, "http://www.cnpm25.cn/city/jilin.html");//吉林 > 吉林省
        cityMap.put(10231, "http://www.cnpm25.cn/city/jilin.html");//吉林 > 吉林市
        cityMap.put(11225, "http://www.cnpm25.cn/city/jiujiang.html");//九江 > 九江市
        cityMap.put(11228, "http://www.cnpm25.cn/city/jiujiang.html");//九江 > 九江县
        cityMap.put(12042, "http://www.cnpm25.cn/city/dali.html");//大理 > 大理白族自治州
        cityMap.put(12043, "http://www.cnpm25.cn/city/dali.html");//大理 > 大理市
        cityMap.put(11891, "http://www.cnpm25.cn/city/rikaze.html");//日喀则 > 日喀则地区
        cityMap.put(11892, "http://www.cnpm25.cn/city/rikaze.html");//日喀则 > 日喀则市
        cityMap.put(10934, "http://www.cnpm25.cn/city/anyang.html");//安阳 > 安阳市
        cityMap.put(10936, "http://www.cnpm25.cn/city/anyang.html");//安阳 > 安阳县
        cityMap.put(11110, "http://www.cnpm25.cn/city/changsha.html");//长沙 > 长沙市
        cityMap.put(11112, "http://www.cnpm25.cn/city/changsha.html");//长沙 > 长沙县
        cityMap.put(10943, "http://www.cnpm25.cn/city/xinxiang.html");//新乡 > 新乡市
        cityMap.put(10946, "http://www.cnpm25.cn/city/xinxiang.html");//新乡 > 新乡县
        cityMap.put(10495, "http://www.cnpm25.cn/city/datong.html");//大同 > 大同市　
        cityMap.put(10496, "http://www.cnpm25.cn/city/datong.html");//大同 > 大同县
        cityMap.put(12377, "http://www.cnpm25.cn/city/yibin.html");//宜宾 > 宜宾市
        cityMap.put(12385, "http://www.cnpm25.cn/city/yibin.html");//宜宾 > 宜宾县
        cityMap.put(10691, "http://www.cnpm25.cn/city/tongling.html");//铜陵 > 铜陵市
        cityMap.put(10692, "http://www.cnpm25.cn/city/tongling.html");//铜陵 > 铜陵县
        cityMap.put(11293, "http://www.cnpm25.cn/city/shangrao.html");//上饶 > 上饶市
        cityMap.put(11295, "http://www.cnpm25.cn/city/shangrao.html");//上饶 > 上饶县
        cityMap.put(11910, "http://www.cnpm25.cn/city/naqu.html");//那曲 > 那曲地区
        cityMap.put(11911, "http://www.cnpm25.cn/city/naqu.html");//那曲 > 那曲县
        cityMap.put(10959, "http://www.cnpm25.cn/city/puyang.html");//濮阳 > 濮阳市
        cityMap.put(10964, "http://www.cnpm25.cn/city/puyang.html");//濮阳 > 濮阳县
        cityMap.put(10374, "http://www.cnpm25.cn/city/liaoyang.html");//辽阳 > 辽阳市
        cityMap.put(10376, "http://www.cnpm25.cn/city/liaoyang.html");//辽阳 > 辽阳县
        cityMap.put(12344, "http://www.cnpm25.cn/city/guangan.html");//广安 > 广安市
        cityMap.put(12345, "http://www.cnpm25.cn/city/guangan.html");//广安 > 广安区
        cityMap.put(10356, "http://www.cnpm25.cn/city/benxi.html");//本溪 > 本溪市
        cityMap.put(10357, "http://www.cnpm25.cn/city/benxi.html");//本溪 > 本溪满族自治县
        cityMap.put(10629, "http://www.cnpm25.cn/city/shaoxing.html");//绍兴 > 绍兴市
        cityMap.put(10633, "http://www.cnpm25.cn/city/shaoxing.html");//绍兴 > 绍兴县
        cityMap.put(12004, "http://www.cnpm25.cn/city/chuxiong.html");//楚雄 > 楚雄彝族自治州
        cityMap.put(12005, "http://www.cnpm25.cn/city/chuxiong.html");//楚雄 > 楚雄市
        cityMap.put(10506, "http://www.cnpm25.cn/city/changzhi.html");//长治 > 长治市　
        cityMap.put(10508, "http://www.cnpm25.cn/city/changzhi.html");//长治 > 长治县
        cityMap.put(10386, "http://www.cnpm25.cn/city/chaoyang.html");//朝阳 > 朝阳市
        cityMap.put(10389, "http://www.cnpm25.cn/city/chaoyang.html");//朝阳 > 朝阳县
        cityMap.put(11260, "http://www.cnpm25.cn/city/jian.html");//吉安 > 吉安市
        cityMap.put(11262, "http://www.cnpm25.cn/city/jian.html");//吉安 > 吉安县
        cityMap.put(10181, "http://www.cnpm25.cn/city/chengde.html");//承德 > 承德市　
        cityMap.put(10182, "http://www.cnpm25.cn/city/chengde.html");//承德 > 承德县
        cityMap.put(11120, "http://www.cnpm25.cn/city/xiangtan.html");//湘潭 > 湘潭市
        cityMap.put(11123, "http://www.cnpm25.cn/city/xiangtan.html");//湘潭 > 湘潭县
        cityMap.put(10674, "http://www.cnpm25.cn/city/wuhu.html");//芜湖 > 芜湖市
        cityMap.put(10675, "http://www.cnpm25.cn/city/wuhu.html");//芜湖 > 芜湖县
        cityMap.put(11866, "http://www.cnpm25.cn/city/changdu.html");//昌都 > 昌都地区
        cityMap.put(11867, "http://www.cnpm25.cn/city/changdu.html");//昌都 > 昌都县
        cityMap.put(11096, "http://www.cnpm25.cn/city/enshi.html");//恩施 > 恩施土家族苗族自治州
        cityMap.put(11097, "http://www.cnpm25.cn/city/enshi.html");//恩施 > 恩施市
        cityMap.put(10911, "http://www.cnpm25.cn/city/kaifeng.html");//开封 > 开封市
        cityMap.put(10915, "http://www.cnpm25.cn/city/kaifeng.html");//开封 > 开封县
        cityMap.put(10965, "http://www.cnpm25.cn/city/xuchang.html");//许昌 > 许昌市
        cityMap.put(10968, "http://www.cnpm25.cn/city/xuchang.html");//许昌 > 许昌县
        cityMap.put(10380, "http://www.cnpm25.cn/city/tieling.html");//铁岭 > 铁岭市
        cityMap.put(10383, "http://www.cnpm25.cn/city/tieling.html");//铁岭 > 铁岭县
        cityMap.put(11929, "http://www.cnpm25.cn/city/linzhi.html");//林芝 > 林芝地区
        cityMap.put(11930, "http://www.cnpm25.cn/city/linzhi.html");//林芝 > 林芝县
        ////////////////////////////城市与数据库没有匹配到的/////////////////////////////////////
        //        cityMap.put("黄山风景区",    "http://www.cnpm25.cn/city/huangshangjq.html");//黄山风景区
        //        cityMap.put("九华山风景区",   "http://www.cnpm25.cn/city/jiuhuanshanjq.html");//九华山风景区

    }

    public static int parsePM25Value(int cityId) {
        try {
            String url = cityMap.get(cityId);
            if (url == null) {
                return -1;
            }
            String html = HttpUtility.get(url, null, "UTF-8");
            Matcher m = pattern.matcher(html);
            while (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
//            System.out.println("CN PM25数据抓取异常:cityId=" + cityId + ", msg=" + e.getMessage());
        }
        return -1;
    }

}
