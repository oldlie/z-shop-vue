package com.oldlie.zshop.zshopvue.model.front;

import com.oldlie.zshop.zshopvue.model.db.Article;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import lombok.*;

import java.util.List;

/**
 * @author oldlie
 * @date 2020/5/16
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class HomeArticles {
    private Tag firstTag;
    private List<Tag> tags;
    private List<Article> articles;
    private long total;
}
