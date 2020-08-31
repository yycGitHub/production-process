package com.surekam.modules.api.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surekam.common.config.Global;
import com.surekam.common.mapper.JsonMapper;
import com.surekam.common.persistence.Page;
import com.surekam.common.utils.ResultBean;
import com.surekam.common.utils.ResultEnum;
import com.surekam.common.utils.ResultUtil;
import com.surekam.modules.agro.product.entity.ProductLibraryTree;
import com.surekam.modules.agro.product.entity.vo.ProductLibraryVo;
import com.surekam.modules.agro.product.service.ProductLibraryTreeService;
import com.surekam.modules.agro.productlibraryrelation.dao.ProductLibraryRelationDao;
import com.surekam.modules.agro.productlibraryrelation.entity.ProductLibraryRelation;
import com.surekam.modules.sys.dao.OfficeDao;
import com.surekam.modules.sys.entity.Office;
import com.surekam.modules.sys.entity.User;
import com.surekam.modules.sys.service.ApiSystemService;

/**
 * 品种
 * 
 * @author
 *
 */
@Api
@Controller
@RequestMapping(value = "api/productLibrary")
public class ProductLibraryController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProductLibraryTreeService productLibraryTreeService;
	
	@Autowired
	private ProductLibraryRelationDao productLibraryRelationDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private ApiSystemService apiSystemService;

	/**
	 * 基地种植养殖品种列表
	 * 
	 * @author
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "productLibraryList", produces = "application/json;charset=UTF-8", method = { RequestMethod.GET })
	@ResponseBody
	@ApiOperation(value = "基地种植养殖品种列表", httpMethod = "GET", notes = "基地种植养殖品种列表", consumes = "application/x-www-form-urlencoded")
	public String productLibraryList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		String token = request.getHeader("X-Token");
		if (token != null) {
			User currentUser = apiSystemService.findUserByToken(token);
			String officeId = currentUser.getOffice().getId();
			List<ProductLibraryTree> list = productLibraryTreeService.getofficeIdByList(officeId);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(list));
		} else {
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.TOKEN_IS_NULL.getCode(), ResultEnum.TOKEN_IS_NULL.getMessage()));
		}
	}

	@RequestMapping(value = "list", produces = "application/json;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "品种类型列表查询", notes = "品种类型列表查询", consumes = "application/json")
	public String list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String name, @RequestParam(required = false) String parentId,
			@RequestParam(required = false) String states, @RequestParam(required = false) Integer pageno,
			@RequestParam(required = false) Integer pagesize, @RequestParam(required = false) boolean getProduct) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		try {
			int pageNo = pageno == null ? Global.DEFAULT_PAGENO : pageno;
			int pageSize = pagesize == null ? Global.DEFAULT_PAGESIZE : pagesize;
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			Page<ProductLibraryTree> page = new Page<ProductLibraryTree>(pageNo, pageSize);
			page = productLibraryTreeService.findPageLibrarys_new(page, currentUser, name, parentId, states, false, getProduct);
			Page<ProductLibraryVo> pageVo = new Page<ProductLibraryVo>(pageNo, pageSize);
			pageVo.setCount(page.getCount());
			if (page.getList() != null && page.getList().size() > 0) {
				List<ProductLibraryTree> list = page.getList();
				List<ProductLibraryVo> listVo = new ArrayList<ProductLibraryVo>();
				for (int i = 0; i < list.size(); i++) {
					ProductLibraryVo tempVo = new ProductLibraryVo();
					ProductLibraryTree temp = list.get(i);
					BeanUtils.copyProperties(temp, tempVo, new String[] { "childList" });
					tempVo.setParentId(temp.getParent().getId());
					tempVo.setParentName(temp.getParent().getProductCategoryName());
					listVo.add(tempVo);
				}
				pageVo.setList(listVo);
			}
			List<ProductLibraryVo> list = pageVo.getList();
			for (ProductLibraryVo productLibraryVo : list) {
				String officeStr = "";
				List<ProductLibraryRelation> findByParentid = productLibraryRelationDao.findByParentid(productLibraryVo.getId());
				for (ProductLibraryRelation productLibraryRelation : findByParentid) {
					Office office = officeDao.get(productLibraryRelation.getOfficeId());
					if (null != office) {
						officeStr += office.getName() +"，";
					}
				}
				if(StringUtils.isNotBlank(officeStr)) {
					productLibraryVo.setOfficeStr(officeStr = officeStr.substring(0, officeStr.length() - 1));
				}
			}
			return jsonMapper.toJson(ResultUtil.success(pageVo));
		} catch (Exception e) {
			logger.error("品种类型列表查询错误：" + e);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "save", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "保存品种类型", notes = "品种类型", consumes = "application/x-www-form-urlencoded")
	public String save(ProductLibraryTree productLibraryTree, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			boolean falg = productLibraryTreeService.save(productLibraryTree);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(falg));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "delete", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "删除品种类型", httpMethod = "POST", notes = "品种类型", consumes = "application/x-www-form-urlencoded")
	public String delete(String id, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			boolean falg = productLibraryTreeService.delete(id);
			if(!falg){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(falg));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "enable", produces = "application/json;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "恢复品种类型", httpMethod = "POST", notes = "恢复品种类型", consumes = "application/x-www-form-urlencoded")
	public String enable(String id, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		try {
			boolean falg = productLibraryTreeService.enable(id);
			if(!falg){
				return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.OPERATION_FAILED.getCode(), ResultEnum.OPERATION_FAILED.getMessage()));
			}
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(falg));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.error(ResultEnum.PRODUCT_CATEGORY_ERROR.getCode(), ResultEnum.PRODUCT_CATEGORY_ERROR.getMessage()));
		}
	}

	@RequestMapping(value = "getElmentTreeLibrarys", produces = "application/json;charset=UTF-8", method = {
			RequestMethod.POST })
	@ResponseBody
	@ApiOperation(value = "获取品种树数据", httpMethod = "POST", notes = "获取品种树数据", consumes = "application/json")
	public String getElmentTreeLibrarys(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String itemId, 
			@RequestParam(required = false) boolean getCategory,
			@RequestParam(required = false) boolean onlyNext) {
		response.setContentType("application/json; charset=UTF-8");
		JsonMapper jsonMapper = JsonMapper.getInstance();
		ResultBean<Object> resultBean = new ResultBean<Object>();
		try {
			String token = request.getHeader("X-Token");
			User currentUser = apiSystemService.findUserByToken(token);
			if (StringUtils.isBlank(itemId) || currentUser.isAdmin()) {
				itemId = "1";
			}
			ProductLibraryTree productLibraryTree = productLibraryTreeService.get(itemId);
			if (productLibraryTree == null) {
				resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
				return jsonMapper.toJson(resultBean);
			}
			List<ProductLibraryTree> list = productLibraryTreeService.findLibrarys(currentUser, itemId, onlyNext, getCategory);
			ProductLibraryVo vo = treeList(list, productLibraryTree);
			List<ProductLibraryVo> vos = new ArrayList<ProductLibraryVo>();
			vos.add(vo);
			return JsonMapper.nonDefaultMapper().toJson(ResultUtil.success(vos));
		} catch (Exception e) {
			logger.error("获取树数据错误：" + e);
			e.printStackTrace();
			resultBean = ResultUtil.error(ResultEnum.SYSTEM_ERROR.getCode(), ResultEnum.SYSTEM_ERROR.getMessage());
			return jsonMapper.toJson(resultBean);
		}
	}

	/**
	 * 树对象组装
	 * 
	 * @param list
	 * @param parent
	 * @return
	 */
	public ProductLibraryVo treeList(List<ProductLibraryTree> list, ProductLibraryTree parent) {
		ProductLibraryVo productLibraryTreeVo = new ProductLibraryVo();
		BeanUtils.copyProperties(parent, productLibraryTreeVo, new String[] { "childList" });
		if (!"1".equals(parent.getId())) {
			productLibraryTreeVo.setParentId(parent.getParent().getId());
			productLibraryTreeVo.setParentName(parent.getParent().getProductCategoryName());
		}
		for (ProductLibraryTree temp : list) {
			if (parent.getId().equals(temp.getParent().getId())) {
				ProductLibraryVo productLibraryTreeVoTemp = new ProductLibraryVo();
				BeanUtils.copyProperties(temp, productLibraryTreeVoTemp, new String[] { "childList" });
				productLibraryTreeVoTemp.setParentId(parent.getId());
				productLibraryTreeVoTemp.setParentName(parent.getProductCategoryName());
				if (temp.getChildList().size() > 0) {
					productLibraryTreeVoTemp = treeList(list, temp);
				}
				productLibraryTreeVo.getChildList().add(productLibraryTreeVoTemp);
			}
		}
		return productLibraryTreeVo;
	}

}
