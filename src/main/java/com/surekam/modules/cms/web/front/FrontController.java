package com.surekam.modules.cms.web.front;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.CacheUtils;
import com.surekam.common.utils.CookieUtils;
import com.surekam.common.utils.DateUtils;
import com.surekam.common.utils.Encodes;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.cms.entity.Article;
import com.surekam.modules.cms.entity.Category;
import com.surekam.modules.cms.entity.Comment;
import com.surekam.modules.cms.entity.Link;
import com.surekam.modules.cms.service.ArticleService;
import com.surekam.modules.cms.service.CategoryService;
import com.surekam.modules.cms.service.CommentService;
import com.surekam.modules.cms.service.LinkService;
import com.surekam.modules.common.entity.FileInfo;
import com.surekam.modules.common.service.FileInfoService;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.SystemService;
import com.surekam.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value = "${frontPath}")
public class FrontController extends BaseController{
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private LinkService linkService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SystemService systemService;
    @Autowired
    private FileInfoService fileInfoService;
	
	/**
	 * 网站首页
	 */
	@RequestMapping
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		User user = UserUtils.getUser();
		// 未登录，则跳转到登录页
		if(user.getId() == null){
			return "redirect:"+Global.getAdminPath()+"/login";
		}
		//登录成功后，记录cookie值
		String loginCode = CookieUtils.getCookie(request, "loginCode");
		if(null == loginCode || (null != loginCode && !loginCode.equals(user.getLoginCode()))){
			String time = DateUtils.getDate("yyyyMMddHHmmss");
			String loginCodeUpdate = Encodes.encodeBase62((user.getLoginName() + time).getBytes());
			user.setLoginCode(loginCodeUpdate);
			CookieUtils.setCookie(response, "loginCode", loginCodeUpdate);
			systemService.saveUser(user);
		}
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(user.getLoginName(), false, true);
		
		model.addAttribute("isIndex", true);
		return "modules/cms/front/frontIndex";
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
	/**
	 * 内容列表
	 */
	@RequestMapping(value = "list-{categoryId}${urlSuffix}")
	public String list(@PathVariable Integer categoryId, @RequestParam(required=false, defaultValue="1") Integer pageNo,
			@RequestParam(required=false, defaultValue="5") Integer pageSize, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null){
			return "error/404";
		}
		if(category.getParent().getId() == 1){
			model.addAttribute("categoryFirst", category);
		}else{
			model.addAttribute("categoryFirst", category.getParent());
		}
		// 2：简介类栏目，栏目第一条内容
		if("2".equals(category.getShowModes()) && "article".equals(category.getModule())){
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")){
				categoryList.add(category);
			}else{
				categoryList = categoryService.findByParentId(category.getParent().getId());
			}
			model.addAttribute("category", category);
			model.addAttribute("categoryList", categoryList);
			// 获取文章内容
			Page<Article> page = new Page<Article>(1, 1, -1);
			Article article = new Article(category);
			page = articleService.find(page, article, false);
			if (page.getList().size()>0){
				article = page.getList().get(0);
				articleService.updateHitsAddOne(article.getId());
			}
			model.addAttribute("article", article);
            setTplModelAttribute(model, category);
            setTplModelAttribute(model, article.getViewConfig());
			return "modules/cms/front/"+getTpl(article);
		}else{
			List<Category> categoryList = categoryService.findByParentId(category.getId());
			// 展现方式为1 、无子栏目或公共模型，显示栏目内容列表
			if("1".equals(category.getShowModes())||categoryList.size()==0){
				// 有子栏目并展现方式为1，则获取第一个子栏目；无子栏目，则获取同级分类列表。
				if(categoryList.size()>0){
					category = categoryList.get(0);
				}else{
					// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
					if (category.getParent().getId().equals("1")){
						categoryList.add(category);
					}else{
						categoryList = categoryService.findByParentId(category.getParent().getId());
					}
				}
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				// 获取内容列表
				if ("article".equals(category.getModule())){
					Page<Article> page = new Page<Article>(pageNo, pageSize);
					page = articleService.find(page, new Article(category), false);
					model.addAttribute("page", page);
					// 如果第一个子栏目为简介类栏目，则获取该栏目第一篇文章
					if ("2".equals(category.getShowModes())){
						Article article = new Article(category);
						if (page.getList().size()>0){
							article = page.getList().get(0);
							articleService.updateHitsAddOne(article.getId());
						}
						model.addAttribute("article", article);
                        setTplModelAttribute(model, category);
                        setTplModelAttribute(model, article.getViewConfig());
						return "modules/cms/front/"+getTpl(article);
					}
				}else if ("link".equals(category.getModule())){
					Page<Link> page = new Page<Link>(1, -1);
					page = linkService.find(page, new Link(category), false);
					model.addAttribute("page", page);
				}
				String view = "/frontList";
				if (StringUtils.isNotBlank(category.getCustomListView())){
					view = "/"+category.getCustomListView();
				}
                setTplModelAttribute(model, category);
				return "modules/cms/front/"+view;
			}
			// 有子栏目：显示子栏目列表
			else{
				model.addAttribute("category", category);
				model.addAttribute("categoryList", categoryList);
				String view = "/frontList";
				if (StringUtils.isNotBlank(category.getCustomListView())){
					view = "/"+category.getCustomListView();
				}
                setTplModelAttribute(model, category);
                
                Page<Article> page = new Page<Article>(pageNo, pageSize);
				page = articleService.find(page, new Article(category), false);
				model.addAttribute("page", page);
				return "modules/cms/front/"+view;
			}
		}
	}

	/**
	 * 内容列表（通过url自定义视图）
	 */
	@RequestMapping(value = "listc-{categoryId}-{customView}${urlSuffix}")
	public String listCustom(@PathVariable Integer categoryId, @PathVariable String customView, @RequestParam(required=false, defaultValue="1") Integer pageNo,
			@RequestParam(required=false, defaultValue="15") Integer pageSize, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null){
			return "error/404";
		}
		List<Category> categoryList = categoryService.findByParentId(category.getId());
		model.addAttribute("category", category);
		model.addAttribute("categoryList", categoryList);
        setTplModelAttribute(model, category);
		return "modules/cms/front/frontListCategory"+customView;
	}

	/**
	 * 显示内容
	 */
	@RequestMapping(value = "view-{categoryId}-{contentId}${urlSuffix}")
	public String view(@PathVariable Integer categoryId, @PathVariable Integer contentId, Model model) {
		Category category = categoryService.get(categoryId);
		if (category==null){
			return "error/404";
		}
		if(category.getParent().getId() == 1){
			model.addAttribute("categoryFirst", category);
		}else{
			model.addAttribute("categoryFirst", category.getParent());
		}
		if ("article".equals(category.getModule())){
			// 如果没有子栏目，并父节点为跟节点的，栏目列表为当前栏目。
			List<Category> categoryList = Lists.newArrayList();
			if (category.getParent().getId().equals("1")){
				categoryList.add(category);
			}else{
				categoryList = categoryService.findByParentId(category.getParent().getId());
			}
			// 获取文章内容
			Article article = articleService.get(contentId);
			if (article==null || !Article.DEL_FLAG_NORMAL.equals(article.getDelFlag())){
				return "error/404";
			}
			// 文章阅读次数+1
			articleService.updateHitsAddOne(contentId);
			// 获取推荐文章列表
			//List<Object[]> relationList = articleService.findByIds(article.getRelation());
			// 将数据传递到视图
			model.addAttribute("category", article.getCategory());
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("article", article);
			//model.addAttribute("relationList", relationList); 
            setTplModelAttribute(model, article.getCategory());
            setTplModelAttribute(model, article.getViewConfig());
            
    		if(contentId !=null && contentId>0){
   			 	List<FileInfo> fileInfos= fileInfoService.findAllfilesByYwb(contentId+"", FileInfo.CMS_ACTICLE_TYPE,null);
   			 	model.addAttribute("fileInfos",fileInfos);
    		}
			return "modules/cms/front/"+getTpl(article);
		}
		return "error/404";
	}
	
	/**
	 * 内容评论
	 */
	@RequestMapping(value = "comment", method=RequestMethod.GET)
	public String comment(Comment comment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Comment> page = new Page<Comment>(request, response);
		Comment c = new Comment();
		c.setCategory(comment.getCategory());
		c.setContentId(comment.getContentId());
		c.setDelFlag(Comment.DEL_FLAG_NORMAL);
		page = commentService.find(page, c);
		model.addAttribute("page", page);
		model.addAttribute("comment", comment);
		return "modules/cms/front/frontComment";
	}
	
	/**
	 * 内容评论保存
	 */
	@ResponseBody
	@RequestMapping(value = "comment", method=RequestMethod.POST)
	public String commentSave(Comment comment,@RequestParam(required=false) Integer replyId, HttpServletRequest request) {
			if (replyId != null){
				Comment replyComment = commentService.get(replyId);
				if (replyComment != null){
					comment.setContent("<div class=\"reply\">"+replyComment.getCreateBy().getName()+":<br/>"
							+DateUtils.formatDateTime(replyComment.getCreateDate())+"<br/>"
							+replyComment.getContent()+"</div>"+comment.getContent());
				}
			}
			comment.setIp(request.getRemoteAddr());
			comment.setCreateDate(new Date());
			if(comment.getCategory()!=null && Comment.DEL_FLAG_AUDIT.equals(comment.getCategory().getIsCommentAudit())){
				comment.setDelFlag(Comment.DEL_FLAG_AUDIT);
			}else{
				comment.setDelFlag(Comment.DEL_FLAG_NORMAL);
			}
			commentService.save(comment);
			return "{result:1, message:'提交成功。'}";
	}

    private String getTpl(Article article){
        if(StringUtils.isBlank(article.getCustomContentView())){
            String view = null;
            Category c = article.getCategory();
            boolean goon = true;
            do{
                if(StringUtils.isNotBlank(c.getCustomContentView())){
                    view = c.getCustomContentView();
                    goon = false;
                }else if(c.getParent() == null || c.getParent().isRoot()){
                    goon = false;
                }else{
                    c = c.getParent();
                }
            }while(goon);
            return StringUtils.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
        }else{
            return article.getCustomContentView();
        }
    }

    private void setTplModelAttribute(Model model, String param){
        if(StringUtils.isNotBlank(param)){
            @SuppressWarnings("rawtypes")
			Map map = JsonMapper.getInstance().fromJson(param, Map.class);
            if(map != null){
                for(Object o : map.keySet()){
                    model.addAttribute("viewConfig_"+o.toString(), map.get(o));
                }
            }
        }
    }

    private void setTplModelAttribute(Model model, Category category){
        List<Category> categoryList = Lists.newArrayList();
        Category c = category;
        boolean goon = true;
        do{
            if(c.getParent() == null || c.getParent().isRoot()){
                goon = false;
            }
            categoryList.add(c);
            c = c.getParent();
        }while(goon);
        Collections.reverse(categoryList);
        for(Category ca : categoryList){
            setTplModelAttribute(model, ca.getViewConfig());
        }
    }
	
}
