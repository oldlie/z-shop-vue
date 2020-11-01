package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.Role;
import com.oldlie.zshop.zshopvue.model.db.UrlRoleMapping;
import com.oldlie.zshop.zshopvue.model.db.repository.RoleRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.UrlRoleMappingRepository;
import com.oldlie.zshop.zshopvue.model.db.User;
import com.oldlie.zshop.zshopvue.model.db.repository.UserRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository repository) {
        this.roleRepository = repository;
    }

    public UserService(UserRepository userRepository,
                       UrlRoleMappingRepository urlRoleMappingRepository) {
        this.userRepository = userRepository;
        this.urlRoleMappingRepository = urlRoleMappingRepository;
    }

    public User user(String username) {
        return this.userRepository.findFirstByUsername(username);
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails details = this.userRepository.findFirstByUsername(s);
        ((User) details).getRoles().forEach(x -> {
            log.debug(x.getName());
        });
        return details;
    }

    private Map<String, String> urlRoleMap = null;

    public Map<String, String> loadUrlMap() {
        urlRoleMap = new HashMap<>();
        List<UrlRoleMapping> list = this.urlRoleMappingRepository.findAll();
        list.forEach(x -> {
            if (urlRoleMap.keySet().contains(x.getUrl())) {
                String value = urlRoleMap.get(x.getUrl()) + "," + x.getRole();
                urlRoleMap.put(x.getUrl(), value);
            } else {
                urlRoleMap.put(x.getUrl(), x.getRole());
            }
        });
        return urlRoleMap;
    }

    public void loadUrlMap(final Map<String, String> map) {
        List<UrlRoleMapping> list = this.urlRoleMappingRepository.findAll();
        list.forEach(x -> {
            if (map.keySet().contains(x.getUrl())) {
                String value = map.get(x.getUrl()) + ",ROLE_" + x.getRole();
                map.put(x.getUrl(), value);
            } else {
                map.put(x.getUrl(), "ROLE_" + x.getRole());
            }
        });
    }

    public User initUser(final String username, final String password, final String role) {
        Role _role = this.roleRepository.findFirstByName(role);
        if (_role == null) {
            throw new RuntimeException("Role " + role + " does not exist.");
        }
        return this.initUser(username, password, _role);
    }

    public User initUser(final String username, final String password, final Role role) {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        User user = User
                .builder()
                .username(username)
                .password(this.bCryptPasswordEncoder.encode(password))
                .payPassword("888888")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .roles(roles)
                .build();
        user = this.userRepository.save(user);
        return user;
    }

    /**
     * 获取系统账号信息
     *
     * @param key     查询字段
     * @param value   查询的值
     * @param index   其实页面索引
     * @param size    页面记录数
     * @param orderBy 排序字段
     * @param direct  排序方向
     * @return Page of User
     */
    public PageResponse<User> accounts(String key, String value, int index, int size, String orderBy,
                                       String direct) {
        PageResponse<User> response = new PageResponse<>();
        Page<User> page;
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            page = this.userRepository.findAll(
                    ZsTool.pageable(index, size, orderBy, direct)
            );
        } else {
            page = this.userRepository.findAll(
                    (root, query, cb) -> cb.like(root.get(key), "%" + value + "%"),
                    ZsTool.pageable(index, size, orderBy, direct)
            );
        }
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    /**
     * Get account by user id
     *
     * @param id user id
     * @return User
     */
    public SimpleResponse<User> account(long id) {
        SimpleResponse<User> response = new SimpleResponse<>();
        Optional<User> optional = this.userRepository.findById(id);
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("该账号不存在了");
            return response;
        }
        User user = optional.get();
        user.setPassword("");
        user.setPayPassword("");
        response.setItem(user);
        return response;
    }

    /**
     * 更新用户昵称
     * @param uid user id
     * @param nickname user nickname
     * @return BaseResponse
     */
    public BaseResponse resetNickname(long uid, String nickname) {
        BaseResponse response = new BaseResponse();
        if (StringUtils.isEmpty(nickname)) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("昵称是空的");
            return response;
        }
        if (nickname.length() > 128) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("昵称最大字符不超过128");
            return response;
        }
        this.userRepository.findById(uid).ifPresent(x -> {
            x.setNickname(nickname);
            this.userRepository.save(x);
        } );
        return response;
    }

    /**
     * 重置密码
     * @param uid user id
     * @param oldPassword old password
     * @param newPassword new password
     * @return BaseResponse
     */
    public BaseResponse resetPassword(long uid, String oldPassword, String newPassword) {
        BaseResponse response = new BaseResponse();
        Optional<User> optionalUser = this.userRepository.findById(uid);
        if (!optionalUser.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("账户不存在了，请重新登录");
            return response;
        }
        User user = optionalUser.get();
        if (!this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("原密码不正确");
            return response;
        }
        user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
        this.userRepository.save(user);
        return response;
    }

    /**
     * Reset pay password
     *
     * @param uid      user id
     * @param password new pay password
     * @return BaseResponse
     */
    public BaseResponse resetPayPassword(long uid, String password) {
        BaseResponse response = new BaseResponse();
        String pattern = "^\\d{6}$";
        if (!Pattern.matches(pattern, password)) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("支付密码为6位数字");
            return response;
        }
        Optional<User> userOptional = this.userRepository.findById(uid);
        if (!userOptional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("账号不存在了，请刷新列表");
            return response;
        }
        User user = userOptional.get();
        user.setPayPassword(password);
        this.userRepository.save(user);
        return response;
    }

    /**
     * 重置支付密码
     * @param uid user id
     * @param password account password
     * @param payPwd pay password
     * @return BaseResponse
     */
    public BaseResponse resetPayPassword(long uid, String password, String payPwd) {
        BaseResponse response = new BaseResponse();
        String pattern = "^\\d{6}$";
        if (!Pattern.matches(pattern, payPwd)) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("支付密码为6位数字");
            return response;
        }
        Optional<User> userOptional = this.userRepository.findById(uid);
        if (!userOptional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("账号不存在了，请刷新列表");
            return response;
        }
        User user = userOptional.get();
        if (!this.bCryptPasswordEncoder.matches(password, user.getPassword())) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("密码不正确");
            return response;
        }
        user.setPayPassword(payPwd);
        this.userRepository.save(user);
        return response;
    }

    /**
     * Locked or unlock account
     *
     * @param uid    user id
     * @param locked if locked or not
     * @return BaseResponse
     */
    public BaseResponse resetAccountStatus(long uid, boolean locked) {
        BaseResponse response = new BaseResponse();
        Optional<User> userOptional = this.userRepository.findById(uid);
        if (!userOptional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("账号不存在了，请刷新列表");
            return response;
        }
        User user = userOptional.get();
        user.setAccountNonLocked(locked);
        this.userRepository.save(user);
        return response;
    }

    /**
     * 添加一个账号
     *
     * @param account  username
     * @param password password
     * @param role     role
     * @return User
     */
    public SimpleResponse<User> account(String account, String password, String nickname,
                                        String payPassword, String role) {
        SimpleResponse<User> response = new SimpleResponse<>();
        Role role1 = this.roleRepository.findFirstByName(role);
        if (role1 == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("指定的角色不存在了，请重新选择");
            return response;
        }
        List<Role> roles = new LinkedList<>();
        roles.add(role1);
        User user = User.builder()
                .username(account)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .payPassword(payPassword)
                .roles(roles)
                .isAccountNonExpired(true)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
        user = this.userRepository.save(user);
        response.setItem(user);
        return response;
    }
}
