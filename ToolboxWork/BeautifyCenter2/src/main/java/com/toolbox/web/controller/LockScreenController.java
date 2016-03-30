package com.toolbox.web.controller;
/**
* @author E-mail:86yc@sina.com
* 
*/

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toolbox.common.AppEnum;
import com.toolbox.common.LanguageEnum;
import com.toolbox.framework.utils.StringUtility;
import com.toolbox.utils.UploadUtility;
import com.toolbox.web.entity.LockscreenEntity;
import com.toolbox.web.service.CommonJSONService;
import com.toolbox.web.service.LockScreenService;

@Controller
@RequestMapping("lockscreen")
public class LockScreenController {
    @Autowired
    private LockScreenService lockscreenService;
    @Autowired
    private CommonJSONService commonJSONService;

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public ModelAndView uploadpage() {
        return new ModelAndView("lock/upload");
    }

    @RequestMapping(value = "upload/apk", method = RequestMethod.POST)
    public ModelAndView uploadApk(HttpServletRequest request, String[] market) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile fileData = null;
        while (it.hasNext()) {
            String filenindex = it.next();
            fileData = multipartRequest.getFile(filenindex);
            if (!fileData.isEmpty()) {
                break;
            }
        }
        if (fileData == null || fileData.getSize() <= 0) {
            return new ModelAndView("redirect:/lockscreen/view/all/0");
        }
        LockscreenEntity lock = UploadUtility.parseAPK(fileData, market);
        lockscreenService.save(lock);
        return new ModelAndView("redirect:/lockscreen/view/all/0");
    }

    @RequestMapping(value = "view/{market}/{start}")
    public ModelAndView view(@PathVariable("market") String market, @PathVariable("start") int start) {
        List<LockscreenEntity> locks = lockscreenService.findByPage(market, start, -1);
        return new ModelAndView("lock/view").addObject("locks", locks).addObject("market", market);
    }

    @RequestMapping(value = "info/{elementId}")
    public ModelAndView info(@PathVariable("elementId") String elementId) {
        LockscreenEntity lock = lockscreenService.findByElementId(elementId);
        return new ModelAndView("lock/edit").addObject("lock", lock);
    }

    @RequestMapping(value = "delete/{elementId}", method = RequestMethod.GET)
    public @ResponseBody JSON delete(@PathVariable("elementId") String elementId) {
        //        lockscreenService.deleteByElementId(elementId);
        commonJSONService.delApp(AppEnum.lockscreen.getCollection(), elementId);
        return null;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request, String elementId, String cnName, String enName, String[] market) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> it = multipartRequest.getFileNames();
        MultipartFile fileData = null;
        while (it.hasNext()) {
            String filenindex = it.next();
            fileData = multipartRequest.getFile(filenindex);
            if (!fileData.isEmpty() && fileData.getSize() > 0) {
                break;
            }
        }

        LockscreenEntity lock = lockscreenService.findByElementId(elementId);
        Update update = new Update();
        JSONObject name = lock.getName();
        if (StringUtility.isNotEmpty(cnName)) {
            name.put(LanguageEnum.zh_CN.getCode(), cnName);
        }
        if (StringUtility.isNotEmpty(enName)) {
            name.put(LanguageEnum.en_US.getCode(), cnName);
        }
        lock.setName(name);
        if (market != null && market.length > 0) {
            JSONArray marketArr = new JSONArray();
            for (int i = 0; i < market.length; i++) {
                marketArr.add(market[i]);
            }
            update.set("market", marketArr);
        }
        if (!fileData.isEmpty() && fileData.getSize() > 0) {
            LockscreenEntity lock_new = UploadUtility.parseAPK(fileData, market);
            if (!lock.getPackageName().equals(lock_new.getPackageName())) {
                return new ModelAndView("lock/edit").addObject("lock", lock).addObject("msg", "包名与现有的不一致[" + lock_new.getPackageName() + "]");
            }
            update.set("fileSize", fileData.getSize());
            update.set("actionUrl", lock_new.getActionUrl());
            update.set("iconUrl", lock_new.getIconUrl());
            update.set("name", lock_new.getName());
            update.set("description", lock_new.getDescription());
            update.set("previewImageUrl", lock_new.getPreviewImageUrl());
            JSONArray detailUrl = new JSONArray();
            for (int i = 0; i < lock_new.getDetailUrl().length; i++) {
                detailUrl.add(lock_new.getDetailUrl()[i]);
            }
            update.set("detailUrl", detailUrl);

        }
        lockscreenService.updateFirst(new Query(Criteria.where("elementId").is(elementId)), update);
        LockscreenEntity result = lockscreenService.findByElementId(elementId);
        return new ModelAndView("lock/edit").addObject("lock", result).addObject("msg", "修改成功");
    }

}
