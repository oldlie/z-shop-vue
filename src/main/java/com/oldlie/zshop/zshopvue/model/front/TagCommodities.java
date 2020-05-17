package com.oldlie.zshop.zshopvue.model.front;

import com.oldlie.zshop.zshopvue.model.db.Commodity;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString
public class TagCommodities {
    private String title;
    private List<Commodity> list;
}
