package com.oldlie.zshop.zshopvue.model.front;

import com.oldlie.zshop.zshopvue.model.db.Commodity;
import com.oldlie.zshop.zshopvue.model.db.CommodityFormula;
import com.oldlie.zshop.zshopvue.model.db.CommodityProfile;
import com.oldlie.zshop.zshopvue.model.db.Tag;
import lombok.*;

import java.util.List;

/**
 * @author oldlie
 * @date 2020/5/23
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class CommodityInfo {
    private Commodity commodity;
    private List<CommodityFormula> formulas;
    private CommodityProfile profile;
    private List<Tag> tags;
}
