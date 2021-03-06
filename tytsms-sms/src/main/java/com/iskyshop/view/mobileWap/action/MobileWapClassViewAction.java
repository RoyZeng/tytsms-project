package com.iskyshop.view.mobileWap.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.iskyshop.core.mv.JModelAndView;
import com.iskyshop.core.tools.CommUtil;
import com.iskyshop.foundation.domain.GoodsClass;
import com.iskyshop.foundation.service.IActivityGoodsService;
import com.iskyshop.foundation.service.IActivityService;
import com.iskyshop.foundation.service.IGoodsBrandService;
import com.iskyshop.foundation.service.IGoodsClassService;
import com.iskyshop.foundation.service.IGoodsService;
import com.iskyshop.foundation.service.IGroupGoodsService;
import com.iskyshop.foundation.service.ISysConfigService;
import com.iskyshop.foundation.service.IUserConfigService;
import com.iskyshop.foundation.service.IUserService;

/**
 * 
 * <p>
 * Title: MobileClassViewAction.java
 * </p>
 * 
 * <p>
 * Description:手机客户端商城前台分类请求
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 * 
 * @author hezeng
 * 
 * @date 2014-7-14
 * 
 * @version 1.0
 */
@Controller
public class MobileWapClassViewAction  extends MobileWapBaseAction{
	@Autowired
	private ISysConfigService configService;
	@Autowired
	private IUserConfigService userConfigService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IGoodsBrandService brandService;
	@Autowired
	private IGoodsClassService goodsClassService;
	@Autowired
	private IGroupGoodsService groupgoodsService;
	@Autowired
	private IGoodsBrandService goodsBrandService;
	@Autowired
	private IActivityGoodsService activityGoodsService;
	@Autowired
	private IActivityService activityService;

	/**
	 * 手机客户端商城首页分类请求
	 * 
	 * @param request
	 * @param response
	 * @param store_id
	 * @return
	 */
	@RequestMapping("/mobileWap/goodsclass.htm")
	public ModelAndView goodsclass(HttpServletRequest request,
			HttpServletResponse response, String id) {
		
		ModelAndView mv = new JModelAndView("mobileWap/view/class.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		Map<String,Object> map_list = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			if (id == null || id.equals("")) {// 查询顶级分类
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("display", true);
				List<GoodsClass> gcs = this.goodsClassService
						.query("select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc",
								params, 0, 13);

				mv.addObject("gcs", gcs);
				for (GoodsClass gc : gcs) {
					Map<String,Object> gc_map = new HashMap<String,Object>();
					gc_map.put("id", gc.getId());
					gc_map.put("className", gc.getClassName());
					String path = "";
					if (gc.getIcon_type() == 0) {
						path = CommUtil.getURL(request)
								+ "/resources/style/common/images/icon/icon_"
								+ gc.getIcon_sys() + ".png";
					} else {
						path = CommUtil.getURL(request) + "/"
								+ gc.getIcon_acc().getPath() + "/"
								+ gc.getIcon_acc().getName();
					}
					gc_map.put("icon_path", path);
					list.add(gc_map);
				}
			} else {// 查询子集分类
				GoodsClass parent = this.goodsClassService.getObjById(CommUtil
						.null2Long(id));
				mv.addObject("cgc", parent);
				for (GoodsClass gc : parent.getChilds()) {
					Map<String,Object> gc_map = new HashMap<String,Object>();
					gc_map.put("id", gc.getId());
					gc_map.put("className", gc.getClassName());
					list.add(gc_map);
				}
			}

			return mv;
//		map_list.put("goodsclass_list", list);
//		String json = Json.toJson(map_list, JsonFormat.compact());
//		response.setContentType("text/plain");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setCharacterEncoding("UTF-8");
//		PrintWriter writer;
//		try {
//			writer = response.getWriter();
//			writer.print(json);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	/**
	 * 手机客户端商城首页分类第二层请求
	 * 
	 * @param request
	 * @param response
	 * @param store_id
	 * @return
	 */
	@RequestMapping("/mobileWap/goodsclass2.htm")
	public ModelAndView goodsclass2(HttpServletRequest request,
			HttpServletResponse response, String id) {
		
		ModelAndView mv = new JModelAndView("mobileWap/view/class2.html",
				configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		if (id != null && !id.equals("")) {// 查询顶级分类
			GoodsClass parent = this.goodsClassService.getObjById(CommUtil
					.null2Long(id));
			mv.addObject("cgc", parent);
		}
		return mv;
	}
}
