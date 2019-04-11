# URL 管理

## FRONTEND

```
HOME            GET     /                                                       首页
    CAROUSEL        GET     /carousel                                           轮播图
    TOP PRODUCT     GET     /top/products                                       置顶产品
    CAT PRODUCT     GET     /prodeucts?category={category}&page=0&size=10       根据分类信息获取该分类下的产品
    TOP ARTICLE     GET     /top/articles                                       置顶文章
    CAT ARTICLE     GET     /articles?category={category}&page=0&size=10        根据文章分类信息获取该分类下的文章
    
LOGIN           GET     /login                                                  展示登录页面
                POST    /login                                                  执行登录                               

LOGOUT          GET     /logout                                                 执行登出

ARTICLES        GET     /articles                                               按分类展示文章
    ARTICLE     GET     /artcile/{id}                                           根据ID展示文章
    
ARTICLE         GET     /article/{id}                                           根据ID展示文章

PRODUCT CAT     GET     /products/{category}                                    展示分类下所有的商品
                GET     /products?category={category}&page={page}&size={size}&by={by}&order={order}     翻页
    PRODUCT     GET     /product/{id}                                           点击产品跳转产品页
   
PRODUCT         GET     /product/{id}                                           展示产品页
    CART        POST    /cart                                                   添加产品到购物车
    BUY         POST    /buy                                                    从产品页跳转到结算页
    
CART            GET     /cart                                                   展示购物车页
    DELETE      DELETE  /cart                                                   删除购物车中的产品
    UPDATE      POST    /cart                                                   更新购物车中的产品数量
    BUY         GET     /buy                                                    从购物车界面跳转到结算页

BUY             GET     /buy                                                    结算页（实际到这里就要准备生成订单了）
    SUBMIT      POST    /buy                                                    执行结算，生成订单
    CANCEL      DELETE  /buy                                                    取消结算
    DEF ADDR    GET     /address                                                获取默认的地址                         
    GOTO ADDR   GET     /addresses                                              跳转到地址页
    POINTS      GET     /points                                                 获取当前账户的积分                        
    
ORDER           GET     /orders                                                 展示订单页面                               
    CANCEL      POST    /order/cancel                                           取消还未发货的订单

COMMENT         GET     /comment/{productId}                                    展示评价已购买的产品页面
                POST    /comment                                                执行评论

POINTS          GET     /points                                                 展示积分页面
                POST    /point                                                  兑换积分
    HISTORY     GET     /points/history                                         积分变更记录
    
ADDRESS         GET     /addresses                                              展示地址页面
                POST    /address                                                增加/修改地址
                DELETE  /address                                                删除地址
                
PROFILE         GET     /profile                                                展示个人信息页
                POST    /profile                                                修改个人信息
                POST    /profile/password                                       修改密码
                
               
```

## BACKEND

```
HOME SETTING    GET     /backend/home                                                   首页设置
    CAROUSEL    GET     /backend/home/carousel                                          获取轮播图
                POST    /backend/home/carousel                                          设置/修改轮播图
                DELETE  /backend/home/carousel                                          删除轮播图
    PRODUCTS    GET     /backend/home/top/products                                      获取置顶的产品
                DELETE  /backend/home/top/product                                       取消单件产品的置顶
                POST    /backend/home/top/product                                       调整置顶的顺序
    ARTICLES    GET     /backend/home/top/articles                                      获取置顶的文章
                POST    /backend/home/top/article                                       调整置顶的文章的顺序
                DELETE  /backend/home/top/article                                       取消置顶的文章
    P CAT       GET     /backend/home/product/categories                                获取产品分类列表
                POST    /backend/home/product/categories                                调整产品分类列表的顺序

PRODUCT         GET     /backend/product/categories                                     展示产品分类界面
                POST    /backend/product/categorie                                      增加/修改产品分类
                DELETE  /backend/product/categories                                     删除产品分类
                GET     /backend/products?category={category}&page={page}&size={size}&by={by}&order={order}    获取指定分类下的产品列表
                GET     /backend/product/{id}                                           获取单件产品
                POST    /backend/product/{id}                                           增加/编辑单件产品
                DELETE  /backend/product/{id}                                           删除单件产品
                POST    /backend/product/online                                         上线单件产品
                POST    /backend/product/offline                                        下线单件产品
                GET     /backend/product/comment/{id}                                   管理产品评价
                GET     /backend/product/specification                                  获取产品规格
                POST    /backend/product/specification                                  添加/修改产品规格
                DELETE  /backend/product/specification                                  删除产品规格
                
ARTICLE         GET     /backend/articles                                               展示所有的文章分类以及文章
                POST    /backend/article/category                                       增加/修改文章分类
                DELETE  /backend/article/category                                       删除文章分类
                GET     /backend/article/categories                                     获取所有的文章分类
                GET     /backend/articles/{category}                                    获取分类下的文章列表
                GET     /backend/article/{id}                                           获取文章
                POST    /backend/article                                                增加/编辑文章
                DELETE  /backend/article                                                删除文章
                
TAG             GET     /backend/tags                                                   展示tag页面
                GET     /backend/tag/categories                                         获取所有的tag的分类
                GET     /backend/tag/category/{id}                                      获取一个tag的分类
                POST    /backend/tag/category                                           增加/修改tag的分类
                DELETE  /backend/tag/category                                           删除一个tag
                GET     /backend/tags/{category}                                        获取一个分类下所有的tag
                POST    /backend/tag                                                    增加一个tag
                DELETE  /backend/tag                                                    删除一个tag                        

POINT CARD      GET     /backend/point-cards                                            展示积分卡界面
                POST    /backend/point-cards                                            批量增加积分卡
                POST    /backend/point-card                                             增加/修改单张积分卡
                DELETE  /backend/point-card                                             删除单张积分卡
                DELETE  /backend/point-cards                                            批量删除积分卡
                
USER            GET     /backend/users                                                  展示全站用户列表
                GET     /backend/users?page={page}&size={size}&by={by}&order={order}    翻页
                POST    /backend/user                                                   更新用户信息
                GET     /backend/points/{uid}                                           跳转到用户的积分信息
                GET     /backend/orders/{uid}                                           跳转到用户的订单信息

POINTS          GET     /backend/points                                                 获取全站积分变动情况
                GET     /backend/points/{uid}                                           展示单个用户的积分变动情况
                POST    /backend/points/{uid}                                           修改单个用户的积分
                
ORDERS          GET     /backend/orders                                                 展示全站订单列表
                GET     /backend/orders/{uid}                                           展示单个用户的订单列表
                GET     /backend/order/{id}                                             获取订单
                POST    /backend/order                                                  修改订单
                DELETE  /backend/order                                                  取消订单
           
```

    