package com.surekam.modules.api.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.web.BaseController;
import com.surekam.modules.api.entity.out.RebackData;
import com.surekam.modules.cms.entity.Article;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.cms.service.ArticleService;
import com.surekam.modules.cms.service.CategoryService;

@Controller
@RequestMapping(value = "api/cms/")
public class CmsApiController extends BaseController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping(value = "findCategoryList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RebackData findCategoryList(Category category, HttpServletRequest request, HttpServletResponse response, Model model) {
		RebackData rebackData = new RebackData();
		try {
			List<Category> categories = categoryService.findAll();
			rebackData.setErrcode("0000");
			rebackData.setData(categories);
			return rebackData;
		} catch (Exception e) {
			rebackData.setErrcode("0001");
			rebackData.setErrmsg("获取栏目列表失败！"+e.getMessage());
			return rebackData;
		}
	}
	
	@RequestMapping(value = "getCategory", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RebackData getCategory(Integer categoryId, HttpServletRequest request, HttpServletResponse response, Model model) {
		RebackData rebackData = new RebackData();
		try {
			Category category = categoryService.get(categoryId);
			rebackData.setErrcode("0000");
			rebackData.setData(category);
			return rebackData;
		} catch (Exception e) {
			rebackData.setErrcode("0001");
			rebackData.setErrmsg("获取栏目失败！"+e.getMessage());
			return rebackData;
		}
	}

	@RequestMapping(value = "findArticleList", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RebackData findArticleList(Integer categoryId, HttpServletRequest request, HttpServletResponse response, Model model) {
		RebackData rebackData = new RebackData();
		try {
			List<Article> articles = articleService.getArticle(categoryId);
			rebackData.setErrcode("0000");
			rebackData.setData(articles);
			return rebackData;
		} catch (Exception e) {
			rebackData.setErrcode("0001");
			rebackData.setErrmsg("获取内容列表失败！"+e.getMessage());
			return rebackData;
		}
	}
	
	@RequestMapping(value = "getArticle", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RebackData getArticle(Integer articleId, HttpServletRequest request, HttpServletResponse response, Model model) {
		RebackData rebackData = new RebackData();
		try {
			Article article = articleService.get(articleId);
			rebackData.setErrcode("0000");
			rebackData.setData(article);
			return rebackData;
		} catch (Exception e) {
			rebackData.setErrcode("0001");
			rebackData.setErrmsg("获取内容失败！"+e.getMessage());
			return rebackData;
		}
	}
}
