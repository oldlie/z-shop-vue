package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.CommodityComment;
import com.oldlie.zshop.zshopvue.model.db.repository.CommodityCommentRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

@Service
@Slf4j
public class CommodityCommentService {

    @Autowired
    private CommodityCommentRepository ccRepository;

    /**
     * 取最近的未处理的10条评论记录
     * @return
     */
    public ListResponse<CommodityComment> top10Comments(int status) {
        ListResponse<CommodityComment> response = new ListResponse<>();
        Page<CommodityComment> page = this.ccRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status),
                ZsTool.pageable(1, 10, "createDate","desc")
        );
        response.setList(page.getContent());
        return response;
    }

    public PageResponse<CommodityComment> page(long cid, int status, int index, int size) {
        PageResponse<CommodityComment> response = new PageResponse<>();
        Page<CommodityComment> page = this.ccRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get("cid"), cid);
                    Predicate predicate1 = criteriaBuilder.equal(root.get("status"), status);
                    return criteriaBuilder.and(predicate, predicate1);
                },
                ZsTool.pageable(index, size, "desc", "updateDate")
        );
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    /**
     * 管理员通过评论审核
     * @param id
     * @return
     */
    public BaseResponse passComment(long id) {
        BaseResponse response = new BaseResponse();
        Optional<CommodityComment> optional = this.ccRepository.findById(id);
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("这条评论不存在了，请刷新重试");
            return response;
        }
        CommodityComment comment = optional.get();
        comment.setStatus(1);
        this.ccRepository.save(comment);
        return response;
    }

    /**
     * 管理员不通过评论审核
     * @param id
     * @return
     */
    public BaseResponse failedComment(long id) {
        BaseResponse response = new BaseResponse();
        Optional<CommodityComment> optional = this.ccRepository.findById(id);
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("这条评论不存在了，请刷新重试");
            return response;
        }
        CommodityComment comment = optional.get();
        comment.setStatus(2);
        this.ccRepository.save(comment);
        return response;
    }
}
