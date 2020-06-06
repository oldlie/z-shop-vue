package com.oldlie.zshop.zshopvue.model.front;

import com.oldlie.zshop.zshopvue.model.db.Commodity;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class CommoditiesWithTag {
    private Tag tag;
    private List<Commodity> commodities;
    private long total;
}
