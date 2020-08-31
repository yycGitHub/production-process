package com.surekam.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.surekam.common.persistence.IncrementDataEntity;
import com.surekam.modules.cms.utils.CmsUtils;

/**
 * 文章Entity
 */
@Entity
@Table(name = "CMS_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed 
@Analyzer(impl = IKAnalyzer.class)
public class Article extends IncrementDataEntity<Article> {

    public static final String DEFAULT_TEMPLATE = "frontViewArticle";
	
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;	// 标题
    private String link;	// 外部链接
	private String color;	// 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image;	// 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer weight;	// 权重，越大越靠前
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits;	// 点击数
	private String posid;	// 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
    private String customContentView;	// 自定义内容视图
   	private String viewConfig;	// 视图参数
   	private String article_author;//文章作者
	//private ArticleData articleData;	//文章副表
	

	private String content;	// 内容
	private String copyfrom;// 来源
	private String relation;// 相关文章
	private String allowComment;// 是否允许评论
    
	public Article() {
		super();
		this.weight = 0;
		this.hits = 0;
		this.posid = "";
		this.allowComment = YES;
	}

	public Article(Integer id){
		this();
		this.id = id;
	}
	
	public Article(Category category){
		this();
		this.category = category;
	}
	
	public String getArticle_author() {
		return article_author;
	}

	public void setArticle_author(String article_author) {
		this.article_author = article_author;
	}
	
	@PrePersist
	public void prePersist(){
		super.prePersist();
	}
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="category_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@IndexedEmbedded
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Length(min=1, max=255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    @Length(min=0, max=255)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

	@Length(min=0, max=50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min=0, max=255)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
        this.image = CmsUtils.formatImageSrcToDb(image);
	}

	@Length(min=0, max=255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min=0, max=255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	@Length(min=0, max=10)
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

    public String getCustomContentView() {
        return customContentView;
    }

    public void setCustomContentView(String customContentView) {
        this.customContentView = customContentView;
    }

    public String getViewConfig() {
        return viewConfig;
    }

    public void setViewConfig(String viewConfig) {
        this.viewConfig = viewConfig;
    }

	@Transient
	public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null){
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	@Transient
	public void setPosidList(List<Long> list) {
		posid = ","+StringUtils.join(list, ",")+",";
	}

    @Transient
   	public String getUrl() {
        return CmsUtils.getUrlDynamic(this);
   	}
	
    
    @NotBlank
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Length(min=0, max=255)
	public String getCopyfrom() {
		return copyfrom;
	}

	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	@Length(min=0, max=255)
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	@Length(min=1, max=1)
	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	@Override
	public String toString() {
		return "Article [category=" + category + ", title=" + title + ", link=" + link + ", color=" + color + ", image="
				+ image + ", keywords=" + keywords + ", description=" + description + ", weight=" + weight
				+ ", weightDate=" + weightDate + ", hits=" + hits + ", posid=" + posid + ", customContentView="
				+ customContentView + ", viewConfig=" + viewConfig + ", article_author=" + article_author + ", content="
				+ content + ", copyfrom=" + copyfrom + ", relation=" + relation + ", allowComment=" + allowComment
				+ "]";
	}
}


