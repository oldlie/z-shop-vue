package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import com.oldlie.zshop.zshopvue.model.db.repository.ArticleTagRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityTagRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.TagRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private ArticleTagRepository articleTagRepository;

    @Autowired
    public void setArticleTagRepository(ArticleTagRepository articleTagRepository) {
        this.articleTagRepository = articleTagRepository;
    }

    private CommodityTagRepository commodityTagRepository;

    @Autowired
    public void setCommodityTagRepository(CommodityTagRepository commodityTagRepository) {
        this.commodityTagRepository = commodityTagRepository;
    }

    @Transactional
    public SimpleResponse<Long> store(AppRequest<Tag> request) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Tag tag = request.getBody();
        Tag target = null;
        if (ZsTool.unboxLong(tag.getId()) > 0) {
            target = this.tagRepository.findById(tag.getId()).orElseGet(null);
            if (target == null) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("要修改的标签不存在了。");
                return response;
            }
        } else {
            target = new Tag();
        }
        target.setCategory(tag.getCategory());
        target.setTagOrder(tag.getTagOrder());
        target.setParentId(ZsTool.unboxLong(tag.getParentId()));
        target.setTitle(tag.getTitle());

        if (ZsTool.unboxLong(target.getParentId()) > 0 && ZsTool.unboxLong(tag.getId()) <= 0) {
            // 仅在新增时增加父节点的 child count
            // 且要求前端保证不能修改父节点 id
            Tag parent = this.tagRepository.findById(target.getParentId()).orElse(null);
            if (parent != null) {
                parent.setChildCount(parent.getChildCount() + 1);
                this.tagRepository.save(parent);
            } else {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("指定的上级标签不存在了，将设置本标签为默认标签。");
                target.setParentId(0L);
            }
        }

        target = this.tagRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    @Transactional
    public BaseResponse delete(Long id) {
        BaseResponse response = new BaseResponse();
        Tag tag = this.tagRepository.getOne(id);
        if (tag != null) {
            if (tag.getChildCount() > 0) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("标签拥有下级标签，还不能删除。");
                return response;
            }
            Long relatedArticles = this.articleTagRepository.countByTagId(id);
            if (relatedArticles > 0) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("标签存在相关文章，还不能删除。");
                return response;
            }
//            Long relatedCommodities = this.commodityTagRepository.countByTagId(id);
//            if (relatedCommodities > 0) {
//                response.setStatus(HTTP_CODE.FAILED);
//                response.setMessage("标签存在相关商品，还不能删除");
//                return response;
//            }

            if (tag.getParentId() != 0) {

                Tag parent = this.tagRepository.findById(tag.getParentId()).orElse(null);
                if (parent != null) {
                    int childCount = parent.getChildCount();
                    if (childCount <= 0) {
                        log.error("要删除的节点 {} 上级节点 {} 居然时0哦!!!", tag.getId(),
                                tag.getParentId());
                        parent.setChildCount(0);
                    } else {
                        parent.setChildCount(parent.getChildCount() - 1);
                    }
                }
            }
            this.tagRepository.delete(tag);
        }
        return response;
    }

    public ListResponse<Tag> list(Long parentId) {
        ListResponse<Tag> response = new ListResponse<>();
        List<Tag> list = this.tagRepository.findAllByParentIdOrderByIdAsc(parentId);
        response.setList(list);
        return response;
    }

    public PageResponse<Tag> tags(int page, int size, String orderBy, String order) {
        PageResponse<Tag> response = new PageResponse<>();
        Page<Tag> tags = this.tagRepository.findAll(ZsTool.pageable(page, size, orderBy, order));
        response.setTotal(tags.getTotalElements());
        response.setList(tags.getContent());
        return response;
    }

    public PageResponse<Tag> tags(String title, int page, int size, String orderBy, String order) {
        PageResponse<Tag> response = new PageResponse<>();
        Page<Tag> tags = this.tagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.like(root.get("title"), "%" + title + "%"),
                ZsTool.pageable(page, size, orderBy, order)
        );
        response.setTotal(tags.getTotalElements());
        response.setList(tags.getContent());
        return response;
    }

    public PageResponse<Tag> tags(final Long parentId, int page, int size, String orderBy, String order) {
        PageResponse<Tag> response = new PageResponse<>();
        Page<Tag> tagPage = this.tagRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("parentId"), parentId),
                ZsTool.pageable(page, size, orderBy, order)
        );
        response.setTotal(tagPage.getTotalElements());
        response.setList(tagPage.getContent());
        return response;
    }

    // region Home commodity tags
    public ListResponse<Tag> homeCommodityTags () {
        ListResponse<Tag> response = new ListResponse<>();
        List<Tag> list = this.tagRepository.findAllByHomeTag(0);
        response.setList(list);
        return response;
    }

    public ListResponse<Tag> homeArticleTags () {
        ListResponse<Tag> response = new ListResponse<>();
        List<Tag> list = this.tagRepository.findAllByHomeTag(1);
        response.setList(list);
        return response;
    }
    // endregion
}
