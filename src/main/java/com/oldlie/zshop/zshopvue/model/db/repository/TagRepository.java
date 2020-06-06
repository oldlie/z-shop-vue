package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author oldlie
 */
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * Find all tags by parent id
     * @param parentId parent id
     * @return tags
     */
    List<Tag> findAllByParentIdOrderByIdAsc(Long parentId);

    /**
     * find all tags by category
     * @param category commodity or article
     * @return tags
     */
    @Query("FROM Tag as t join HomeTag as h on t.id=h.tagId where h.category=:category order by h.id asc")
    List<Tag> findAllByHomeTag(@Param("category") int category);

    /**
     * Find all tags by article id
     * @param articleId article id
     * @return list of tags
     */
    @Query("from Tag as t join ArticleTag a on t.id=a.tagId where a.articleId=articleId")
    List<Tag> findAllByArticleId(@Param("articleId") long articleId);

    /**
     * Find all tags by commodity id
     * @param commodityId commodity id
     * @return list of tags
     */
    @Query("FROM Tag as t join CommodityTag as c on t.id=c.tagId where c.commodityId=:commodityId")
    List<Tag> findAllByCommodityId(@Param("commodityId") long commodityId);
}
