/*
 * Copyright (c) 2010-2013 www.pixshow.net All Rights Reserved
 *
 * File:QqCodeTool.java Project: LvWeatherService
 * 
 * Creator:4399-lvtu-8 
 * Date:Dec 3, 2013 11:26:17 AM
 * 
 */
package com.toolbox.weather.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * 
 * @author 4399-lvtu-8
 * @author $Author:$
 * @version $Revision:$ $Date:$ 
 * @since Dec 3, 2013
 * 
 */

public class QqCodeTool {
    //天气状况 wt
    public static String[]                             wt     = { "晴", "多云", "阴", "阵雨", "雷阵雨", "雷阵雨并伴有冰雹", "雨夹雪", "小雨", "中雨", "大雨", "暴雨", "大暴雪", "特大暴雪", "阵雪", "小雪", "中雪", "大雪", "暴雪", "雾", "冻雨", "沙尘暴", "小雨-中雨", "中雨-大雨", "大雨-暴雨", "暴雨-大暴雨", "大暴雨-特大暴雨",
            "小雪-中雪", "中雪-大雪", "大雪-暴雪", "浮尘", "扬沙", "强沙尘暴", "飑", "龙卷风", "弱高吹雪", "轻雾", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "霾" };
    //风向 wd
    public static String[]                             wd     = { "", "东北风", "东风", "东南风", "南风", "西南风", "西风", "西北风", "北风", "旋转不定" };
    //风力等级 wpl
    public static int[]                                wpl    = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    //风力等级 wp
    public static String[]                             wp     = { "微风", "3-4级", "4-5级", "5-6级", "6-7级", "7-8级", "8-9级", "9-10级", "10-11级", "11-12级" };
    //----------------------------------------------------------------------------------------------------------------------------------------------------------//    
    //晨练 cl
    public static String[][]                           cl     = { { "", "" }, { "适宜", "天气晴朗，空气清新，是您晨练的大好时机。" }, { "较适宜", "较适宜晨练，某些气象条件会对晨练产生一定影响，但影响不大。" }, { "较不宜", "某些气象因素对晨练造成不利影响，较不宜晨练。" }, { "不适宜", "气象因素非常不利于室外锻炼，请尽量避免户外晨练。" } };
    //穿衣 
    public static String[][]                           cy     = { { "", "" }, { "炎热", "薄型T恤衫。" }, { "热舒适", "短套装、T恤夏季服装。" }, { "舒适", "长袖服装。" }, { "凉舒适", "薄型套装等春秋过渡装。" }, { "温凉", "夹衣或西服套装加薄羊毛衫。" }, { "凉", "厚外套加毛衣等春秋服装。" }, { "冷", "棉衣加羊毛衫等冬季服装。" }, { "寒冷", "厚羽绒服等隆冬服装。" }                           };
    //感冒
    public static String[][]                           gm     = { { "", "" }, { "少发", "各项气象条件适宜，发生感冒机率较低。" }, { "较易发", "较易发生感冒，体质较弱的朋友请注意适当防护。" }, { "易发", "发生感冒机率较大，请加强自我防护避免感冒。" }, { "极易发", "极易发生感冒，请特别注意增加衣服保暖防寒避免感冒。" } };
    //洗车
    public static String[][]                           xc     = { { "", "" }, { "适宜", "适宜洗车，未来持续两天无雨天气较好，适合擦洗汽车。" }, { "较适宜", "较适宜洗车，未来一天无雨，风力较小，较适合擦洗汽车。" }, { "较不宜", "较不宜洗车，擦洗一新的汽车可能会蒙上污垢。" }, { "不宜", "不宜洗车，路上的泥水可能会再次弄脏您的爱车。" } };
    //中暑
    public static String[][]                           zs     = { { "无", "温度不高，其他各项气象条件适宜，中暑机率极低。" }, { "少发", "气温偏高，有可能中暑，体质较弱的朋友请注意防暑降温，避免长时间在日光下暴晒或在高温环境中工作。" }, { "较易", "气温较高，较易中暑，体弱者请避免长时间在日光下暴晒或在高温环境中工作。" }, { "容易", "气温很高，热气逼人，容易中暑，请注意防暑降温，避免长时间在日光下暴晒或在高温环境中工作。" }, { "极易", "气温极高，热浪滚滚，极易中暑，请注意防暑降温，避免在高温环境中工作。" } };
    //紫外线
    public static String[][]                           zwx    = { { "", "" }, { "最弱", "属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。" }, { "弱", "紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。" }, { "中等", "属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。" }, { "强", "紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。" }, { "很强", "紫外线辐射极强，建议涂擦SPF20以上、PA++的防晒护肤品，尽量避免暴露于日光下。" } };
    //晾晒
    public static String[][]                           ls     = { { "", "" }, { "极适宜", "极适宜晾晒，请抓紧时机晾晒。" }, { "适宜", "适宜晾晒，赶紧把久未见阳光的衣物搬出来吸收一下太阳的味道吧！" }, { "基本适宜", "午后温暖的阳光仍能满足你驱潮消霉杀菌的晾晒需求。" }, { "不太适宜", "气象要素对晾晒有影响，不太适宜晾晒。若非晾晒不可，请尽量选择通风的地点。" }, { "不适宜", "不适宜晾晒。如果非晾晒不可，请在室内进行并关牢门窗。" }          };
    //空气污染
    public static String[][]                           kqwr   = { { "", "" }, { "优", "非常有利于空气污染物稀释、扩散和清除，可在室外正常活动。" }, { "良", "有利于空气污染物稀释、扩散和清除，可在室外正常活动。" }, { "中", "对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。" }, { "较差", "较不利于空气污染物稀释、扩散和清除，请适当减少室外活动时间。" }, { "差", "不利于空气污染物稀释、扩散和清除，请尽量避免在室外长时间活动。" }       };
    //旅游
    public static String[][]                           ly     = { { "", "" }, { "适宜", "明媚阳和微风伴您一路同行，适宜旅游。" }, { "较适宜", "虽然会多云或有风，但仍是出行游玩的好时机，较适宜旅游。" }, { "一般", "可能出现阴天、大风、阵雨或沙尘等天气，将会给出行带来一些不便，旅游指数一般。" }, { "较不宜", "可能出现雨雪，强风，大雾等天气，人体在户外会感觉不舒适，将会给出行带来很多不便，较不适宜出游。" }, { "不适宜", "可能出现较大的雨雪，强风，冻雨，沙尘暴等天气，对出行造成一定困难，不适宜旅游。" } };
    //空调
    public static String[][]                           kt     = { { "", "" }, { "长时间开启", "闷热，您需长时间开启制冷空调来降温除湿。" }, { "部分时间开启", "天气热，建议您在适当的时候开启制冷空调来降低温度。" }, { "少部分时间", "中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。" }, { "较少开启", "您将感到很舒适，一般不需要开启空调。" }, { "开启制暖空调", "冷,适当开启制暖空调调节室内温度。" }                };
    //钓鱼
    public static String[][]                           dy     = { { "", "" }, { "适宜", "非常适宜垂钓，风和日丽的天气将陪伴你度过愉快的垂钓时光。" }, { "较适宜", "较适合垂钓，气象条件对垂钓会产生一定影响，但影响不大。" }, { "不适宜", "天气不好，不适合垂钓，请选择别的娱乐方式。" } };
    //辐射
    public static String[][]                           fs     = { { "", "" }, { "弱", "长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。" }, { "较弱", "建议涂擦SPF在12-15之间，PA+的防晒护肤品。" }, { "中等", "建议涂擦SPF指数高于15,PA+的防晒护肤品。" }, { "强", "外出时应加强防护，建议涂擦SPF在15—20之间PA++的防晒护肤品。" }, { "极强", "外出时应特别加强防护，建议涂擦SPF20以上，PA++的防晒护肤品，并随时补涂。" } };
    //逛街
    public static String[][]                           gj     = { { "适宜", "天气条件很适合逛街，可以尽情享受逛街的乐趣。" }, { "较适宜", "气象要素对逛街有一定影响，比较适宜逛街。" }, { "较不宜", "天气条件不适宜逛街，应尽量避免外出逛街。" }, { "不适宜", "气象条件不适合逛街，请尽量选择室内活动。" } };
    //划船
    public static String[][]                           hc     = { { "", "" }, { "适宜", "天气晴朗，温度适宜，非常适合划船或嬉玩各种水上运动。" }, { "较适宜", "较适宜划船，有些气象因素对划船会产生些影响，但影响不大。" }, { "不适宜", "不适宜划船，建议选择别的娱乐方式或采取必要措施。" } };
    //交通
    public static String[][]                           jt     = { { "", "" }, { "良好", "交通气象条件良好，车辆可以正常行驶。" }, { "较好", "交通气象条件较好，但不适宜高速行驶，司机应更加集中注意力，保持车距。" }, { "一般", "交通气象条件一般，刹车距离延长，事故易发期，注意车距，务必小心驾驶。" }, { "较差", "交通气象条件较差，事故高发期，车辆应低速行驶。" }, { "很差", "交通气象条件很差，车辆行驶缓慢，尽量减少出行。" }              };
    //雨伞
    public static String[][]                           ys     = { { "", "" }, { "不带伞", "降水概率很低，因此您在出门的时候无须带雨伞。" }, { "带伞", "将有阵雨或阵雪，如果您要短时间外出的话可不必带雨伞。" }, { "带伞", "有小雨或小雪或中雪，在短时间外出可收起雨伞，但最好还是带上雨伞。" }, { "带伞", "会有较大的雨雪天气，您在外出的时候一定要带雨伞，以免被雨水淋湿。" } };
    //美发
    public static String[][]                           mf     = { { "", "" }, { "极适宜", "各项气象条件都适宜美发，这为您的头发创造一个健康、洁净的生长环境。" }, { "适宜", "有某项气象因素会影响您的秀发生长，请注意保养。 " }, { "一般", "气象条件对美发有一定影响，注意保养您的美发，细心呵护！" } };
    //夜生活
    public static String[][]                           ysh    = { { "", "" }, { "适宜", "天气晴朗，你可以尽情外出享受夜生活的乐趣，不用担心天气会来捣乱。" }, { "较适宜", "虽然有风或有雨雪天气出现，但只要提前有所准备，您仍然可以享受夜生活的乐趣。" }, { "较不宜", "天气会使人体在户外感觉不舒适，建议夜生活最好在室内进行。" } };
    //放风筝
    public static String[][]                           ffz    = { { "", "" }, { "适宜", "这种天气去放风筝既可以舒展筋骨，又可放松身心。" }, { "较适宜", "选择合适的地点，还是较适宜放风筝的。" }, { "不宜", "气象条件不适宜放风筝。" } };
    //化妆
    public static String[][]                           hz     = { { "", "" }, { "保湿防龟裂", "天气寒冷，多补水，选用滋润保湿型化妆品，使用润唇膏。" }, { "保湿", "皮肤易缺水，用保湿型霜类化妆品，使用润唇膏。" }, { "控油", "建议用露质面霜打底，水质无油粉底霜，透明粉饼，粉质胭脂。" }, { "防晒", "天气炎热，易出汗，建议使用防脱水防晒指数高的化妆品，经常补粉。" } };
    //防寒
    public static String[][]                           fh     = { { "", "" }, { "凉", "室外活动注意适当增减衣物。" }, { "冷", "室外活动要穿厚实一点，年老体弱者要适当注意保暖。" }, { "寒冷", "室外活动要注意保暖，可戴手套与帽子。" }, { "非常寒冷", "室外活动注意保暖防寒，可戴厚手套和帽子，年老体弱者避免长时间外出。" }, { "严寒", "着羽绒服、皮大衣仍感到寒冷，室外活动须戴厚棉、皮手套和帽子。" }, { "冰冻严寒", "极易造成裸露皮肤冻伤，尽量避免野外作业和外出。" }, { "", "" }, { "微凉", "温度未达到风寒所需的低温，稍作防寒准备即可。" } };
    //心情
    public static String[][]                           xq     = { { "", "" }, { "好", "天气晴朗，阳光灿烂，空气温润，和风飘飘，美好的天气会带来一天接踵而来的好心情。" }, { "较好", "温度舒适，您会觉得精神振奋，意气风发，心情舒畅。" }, { "较差", "天气阴沉或有雾，会感觉莫名的压抑，情绪低落。" }, { "差", "天气阴沉有雨或有沙尘，闷热潮湿的空气会让人感到胸闷，心情糟糕。" } };
    //运动
    public static String[][]                           yd     = { { "", "" }, { "适宜", "天气较好，且紫外线辐射不强，适宜户外运动。" }, { "较适宜", "较适宜在户内低强度运动，户外运动需防晒避风。" }, { "较不宜", "受大风，气压，强紫外线，过高或过低的气温等天气影响，较不宜运动" } };
    //约会
    public static String[][]                           yh     = { { "", "" }, { "适宜", "天气晴朗，风和日丽，适宜与情人约会。" }, { "较适宜", "天气不会有太大的影响，你仍然可以有一个愉快的约会。" }, { "较不适宜", "会有影响人体舒适的不好天气出现，室外约会可能会让恋人们受些苦，最好在温暖的室内促膝谈心" }, { "不适宜", "天气会对人体有较大影响，外出约会还可能会败兴而归，男士请别约美眉逛街。" } };

    public static Map<String, Map<String, Object>> zhishu = new HashMap<String, Map<String, Object>>();
    static {
        Map<String, Object> zs_cl = new HashMap<String, Object>();
        zs_cl.put("name", "晨练");
        zs_cl.put("content", cl);
        zhishu.put("zs_cl", zs_cl);

        Map<String, Object> zs_cy = new HashMap<String, Object>();
        zs_cy.put("name", "穿衣");
        zs_cy.put("content", cy);
        zhishu.put("zs_cy", zs_cy);

        Map<String, Object> zs_gm = new HashMap<String, Object>();
        zs_gm.put("name", "感冒");
        zs_gm.put("content", gm);
        zhishu.put("zs_gm", zs_gm);

        Map<String, Object> zs_xc = new HashMap<String, Object>();
        zs_xc.put("name", "洗车");
        zs_xc.put("content", xc);
        zhishu.put("zs_xc", zs_xc);

        Map<String, Object> zs_zs = new HashMap<String, Object>();
        zs_zs.put("name", "中暑");
        zs_zs.put("content", zs);
        zhishu.put("zs_zs", zs_zs);

        Map<String, Object> zs_zwx = new HashMap<String, Object>();
        zs_zwx.put("name", "紫外线");
        zs_zwx.put("content", zwx);
        zhishu.put("zs_zwx", zs_zwx);

        Map<String, Object> zs_ls = new HashMap<String, Object>();
        zs_ls.put("name", "晾晒");
        zs_ls.put("content", ls);
        zhishu.put("zs_ls", zs_ls);

        Map<String, Object> zs_kqwr = new HashMap<String, Object>();
        zs_kqwr.put("name", "污染扩散");
        zs_kqwr.put("content", kqwr);
        zhishu.put("zs_kqwr", zs_kqwr);

        Map<String, Object> zs_ly = new HashMap<String, Object>();
        zs_ly.put("name", "旅游");
        zs_ly.put("content", ly);
        zhishu.put("zs_ly", zs_ly);

        Map<String, Object> zs_kt = new HashMap<String, Object>();
        zs_kt.put("name", "空调");
        zs_kt.put("content", kt);
        zhishu.put("zs_kt", zs_kt);

        Map<String, Object> zs_dy = new HashMap<String, Object>();
        zs_dy.put("name", "钓鱼");
        zs_dy.put("content", dy);
        zhishu.put("zs_dy", zs_dy);

        Map<String, Object> zs_fs = new HashMap<String, Object>();
        zs_fs.put("name", "辐射");
        zs_fs.put("content", fs);
        zhishu.put("zs_fs", zs_fs);

        Map<String, Object> zs_gj = new HashMap<String, Object>();
        zs_gj.put("name", "逛街");
        zs_gj.put("content", gj);
        zhishu.put("zs_gj", zs_gj);

        Map<String, Object> zs_hc = new HashMap<String, Object>();
        zs_hc.put("name", "划船");
        zs_hc.put("content", hc);
        zhishu.put("zs_hc", zs_hc);

        Map<String, Object> zs_jt = new HashMap<String, Object>();
        zs_jt.put("name", "交通");
        zs_jt.put("content", jt);
        zhishu.put("zs_jt", zs_jt);

        Map<String, Object> zs_ys = new HashMap<String, Object>();
        zs_ys.put("name", "雨伞");
        zs_ys.put("content", ys);
        zhishu.put("zs_ys", zs_ys);

        Map<String, Object> zs_mf = new HashMap<String, Object>();
        zs_mf.put("name", "美发");
        zs_mf.put("content", mf);
        zhishu.put("zs_mf", zs_mf);

        Map<String, Object> zs_ysh = new HashMap<String, Object>();
        zs_ysh.put("name", "夜生活");
        zs_ysh.put("content", ysh);
        zhishu.put("zs_ysh", zs_ysh);

        Map<String, Object> zs_ffz = new HashMap<String, Object>();
        zs_ffz.put("name", "放风筝");
        zs_ffz.put("content", ffz);
        zhishu.put("zs_ffz", zs_ffz);

        Map<String, Object> zs_hz = new HashMap<String, Object>();
        zs_hz.put("name", "化妆");
        zs_hz.put("content", hz);
        zhishu.put("zs_hz", zs_hz);

        Map<String, Object> zs_fh = new HashMap<String, Object>();
        zs_fh.put("name", "防寒");
        zs_fh.put("content", fh);
        zhishu.put("zs_fh", zs_fh);

        Map<String, Object> zs_xq = new HashMap<String, Object>();
        zs_xq.put("name", "心情");
        zs_xq.put("content", xq);
        zhishu.put("zs_xq", zs_xq);

        Map<String, Object> zs_yd = new HashMap<String, Object>();
        zs_yd.put("name", "运动");
        zs_yd.put("content", yd);
        zhishu.put("zs_yd", zs_yd);

        Map<String, Object> zs_yh = new HashMap<String, Object>();
        zs_yh.put("name", "约会");
        zs_yh.put("content", yh);
        zhishu.put("zs_yh", zs_yh);

    }

}
