package com.surekam.modules.cms.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surekam.common.config.Global;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.StringUtils;
import com.surekam.common.web.BaseController;
import com.surekam.modules.cms.entity.Comment;
import com.surekam.modules.cms.service.CommentService;
import com.surekam.modules.sys.utils.DictUtils;
import com.surekam.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value = "${adminPath}/cms/comment")
public class CommentController extends BaseController {

	@Autowired
	private CommentService commentService;
	
	@ModelAttribute
	public Comment get(@RequestParam(required=false) Integer id) {
		if (id != null){
			return commentService.get(id);
		}else{
			return new Comment();
		}
	}
	
	@RequiresPermissions("cms:comment:view")
	@RequestMapping(value = {"list", ""})
	public String list(Comment comment,  HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Comment> page = commentService.find(new Page<Comment>(request, response), comment); 
        model.addAttribute("page", page);
        model.addAttribute("categoryId", comment.getCategory()==null?"":comment.getCategory().getId());
		return "modules/cms/commentList";
	}

	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "save")
	public String save(Comment comment, RedirectAttributes redirectAttributes) {
		if (beanValidator(redirectAttributes, comment)){
			if (comment.getAuditUser() == null){
				comment.setAuditUser(UserUtils.getUser());
				comment.setAuditDate(new Date());
			}
			if(comment.getCategory()!=null && Comment.DEL_FLAG_AUDIT.equals(comment.getCategory().getIsCommentAudit())){
				comment.setDelFlag(Comment.DEL_FLAG_AUDIT);
			}else{
				comment.setDelFlag(Comment.DEL_FLAG_NORMAL);
			}
			commentService.save(comment);
			addMessage(redirectAttributes, DictUtils.getDictLabel(comment.getDelFlag(), "cms_del_flag", "保存")
					+"评论'" + StringUtils.abbr(StringUtils.replaceHtml(comment.getContent()),50) + "'成功");
		}
		String categoryId = comment.getCategory()==null?"":comment.getCategory().getId()+"";
		return "redirect:"+Global.getAdminPath()+"/cms/comment/?repage&delFlag=2&category.id="+categoryId;
	}
	
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "delete")
	public String delete(Integer[] ids,String categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		commentService.delete(ids, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"恢复审核":"删除")+"评论成功");
		return "redirect:"+Global.getAdminPath()+"/cms/comment/?repage&delFlag=2&category.id="+categoryId;
	}

}
