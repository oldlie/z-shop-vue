package com.oldlie.zshop.zshopvue.model.front;

import com.oldlie.zshop.zshopvue.model.db.Role;
import lombok.*;

import java.util.List;

@Data
public class FrontUser {
    private Long id;
    private String username;
    private List<Role> roles;
}
