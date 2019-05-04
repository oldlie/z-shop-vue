import Vue from 'vue'
import Antd from 'ant-design-vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import 'ant-design-vue/dist/antd.css'

import Address from './components/Address'
import ArticleList from './components/ArticleList'
import HomeCarousel from './components/HomeCarousel'
import ProductCard from './components/ProductCard'
import ProductComment from './components/ProductComment'
import UserPanelLinks from './components/UserPanelLinks'

Vue.component('address-com', Address);
Vue.component('article-list', ArticleList);
Vue.component('home-carousel', HomeCarousel);
Vue.component('product-card', ProductCard);
Vue.component('product-comment', ProductComment);
Vue.component('user-panel-links', UserPanelLinks);

Vue.config.productionTip = false

import AddressPage from './pages/AddressPage'
import AfterSale from './pages/AfterSale'
import ArticlePage from './pages/ArticlePage'
import ArticlesPage from './pages/ArticlesPage'
import BuyPage from './pages/BuyPage'
import CartPage from './pages/CartPage'
import CommentOrderPage from './pages/CommentOrderPage'
import HomePage from './pages/HomePage'
import LoginPage from './pages/LoginPage'
import ProductPage from './pages/ProductPage'
import ProductsPage from './pages/ProductsPage'
import OrderPage from './pages/OrderPage'
import UserProfilePage from './pages/UserProfilePage'

import AdminDashboardPage from './pages/admin/AdminDashboardPage'

const routes = [
  { path: '/', component: HomePage },
  { path: '/address', component: AddressPage },
  { path: '/after-sale', component: AfterSale },
  { path: '/article/:id', component: ArticlePage },
  { path: '/articles', component: ArticlesPage },
  { path: '/buy', component: BuyPage },
  { path: '/cart', component: CartPage },
  { path: '/home', component: HomePage },
  { path: '/login', component: LoginPage },
  { path: '/product/:id', component: ProductPage },
  { path: '/products', component: ProductsPage },
  { path: '/order/comment', component: CommentOrderPage },
  { path: '/order', component: OrderPage },
  { path: '/profile', component: UserProfilePage},
  { path: '/admin/dashboard', component: AdminDashboardPage}
]

const router = new VueRouter({
  routes // (缩写) 相当于 routes: routes
})

Vue.use(Antd)
Vue.use(VueRouter)

Vue.prototype.setCookie = function (name, value, day) {

  if (day !== 0) { //当设置的时间等于0时，不设置expires属性，cookie在浏览器关闭后删除

    var curDate = new Date();

    var curTamp = curDate.getTime();

    var curWeeHours = new Date(curDate.toLocaleDateString()).getTime() - 1;

    var passedTamp = curTamp - curWeeHours;

    var leftTamp = 24 * 60 * 60 * 1000 - passedTamp;

    var leftTime = new Date();

    leftTime.setTime(leftTamp + curTamp);

    document.cookie = name + "=" + escape(value) + ";expires=" + leftTime.toGMTString();

  } else {

    document.cookie = name + "=" + escape(value);

  }

}

Vue.prototype.getCookie = function (name) {

  var arr;

  var reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

  if (arr = document.cookie.match(reg))

    return unescape(arr[2]);

  else

    return null; ocument.cookie = name + "=" + escape(value);

}

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
