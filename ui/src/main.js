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

import AdminMenu from './components/AdminMenu'

Vue.component('admin-menu', AdminMenu);

import AdminTagComponent from './components/admin/AdminTag'
import AdminTagsComponent from './components/admin/AdminTags'
import AdminProductComponent from './components/admin/AdminProduct'
import AdminProductBasic from './components/admin/AdminProductBasic';

Vue.component('admin-tag', AdminTagComponent);
Vue.component('admin-tags', AdminTagsComponent);
Vue.component('admin-product', AdminProductComponent);
Vue.component('admin-product-basic', AdminProductBasic);


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
import AdminProductsPage from './pages/admin/AdminProductsPage'
import AdminTagsPage from './pages/admin/AdminTagsPage'

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
  { path: '/admin/dashboard', component: AdminDashboardPage},
  { path: '/admin/products', component: AdminProductsPage},
  { path: '/admin/tags', component: AdminTagsPage}
]

const router = new VueRouter({
  routes // (缩写) 相当于 routes: routes
})

Vue.use(Antd)
Vue.use(VueRouter)

import VueCookies from 'vue-cookie'

Vue.use(VueCookies)

Vue.prototype.apiUrl = 'http://localhost:8088';
Vue.prototype.bus = new Vue();

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')


