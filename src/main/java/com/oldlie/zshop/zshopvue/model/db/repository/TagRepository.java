package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    List<Tag> findAllByParentIdOrderByIdAsc(Long parentId);

    @Query("FROM Tag as t join HomeTag as h on t.id=h.tagId where h.category=:category order by h.id asc")
    List<Tag> findAllByHomeTag(@Param("category") int category);

    @Query("from Tag as t join ArticleTag a on t.id=a.tagId where a.articleId=articleId")
    List<Tag> findAllByArticleId(@Param("articleId") long articleId);

    @Query("FROM Tag as t join CommodityTag as c on t.id=c.tagId where c.commodityId=:commodityId")
    List<Tag> findAllByCommodityId(@Param("commodityId") long commodity);
}
