import Vue from 'vue'
import router from './route'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

import VueRouter from 'vue-router'
Vue.use(VueRouter)

import App from './App.vue'

import HomeArticleTagsComponent from './components/HomeArticleTagsComponent'
Vue.component('home-article-tags', HomeArticleTagsComponent)

import HomeCommodityTagsComponent from './components/HomeCommodityTagsComponent'
Vue.component('home-commodity-tags', HomeCommodityTagsComponent)

import SidebarComponent from './components/SidebarComponent'
Vue.component('sidebar', SidebarComponent);

import QuickNavTagsComponent from './components/QuickNavTagsComponent'
Vue.component('quick-nav-tags', QuickNavTagsComponent)

Vue.config.productionTip = false
Vue.use(Antd)

import VueCookie from 'vue-cookie'
Vue.use(VueCookie)

import axios from 'axios'
import VueAxios from 'vue-axios'

axios.defaults.baseURL = 'http://localhost:8090';
axios.defaults.withCredentials = true;
axios.defaults.headers = {
  'X-Requested-With': 'XMLHttpRequest',
  'Accept': 'application/json;charset=UTF-8',
  'Content-Type': 'application/json;charset=UTF-8'
};
axios.interceptors.request.use(function (config) {
  if (config.method === 'get' || config.method === 'delete') {
    //  给data赋值以绕过if判断
    config.data = true
  }
  config.headers['Content-Type'] = 'application/json'
  return config;
}, function (err) {
  console.error(err);
});

Vue.prototype.$bus = new Vue();

const Http = function () {
  this.request = function (url, method, params, callback, finalCallback, exception) {
    let rx;
    method = !method ? 'get' : method;
    let token = VueCookie.get('token');
    let auth = 'ZShop ' + token;
    if ('get' === method) {
      if (params !== null) {
        let _arr = [];
        for (let key in params) {
          _arr.push(key + '=' + encodeURIComponent(params[key]));
        }
        url = url + '?' + _arr.join('&');
      }
      rx = axios.get(url, {
        headers: {
          'Authorization': auth
        }
      })
    } else if ('post' === method) {
      rx = params instanceof FormData ?
        axios.post(url, params, {
          headers: {
            'Authorization': auth,
            'Content-Type': 'multipart/form-data',
            'X-Request-With': 'XMLHttpRequest'
          }
        }) :
        axios.post(url, params, {
          headers: {
            'Authorization': auth
          }
        })
    } else if ('delete' === method) {
      rx = axios.delete(url, {
        headers: {
          'Authorization': auth
        }
      })
    }

    rx.then(response => {
      if (response['status'] === 200) {
        if (callback && typeof callback === 'function') {
          callback(response['data'])
        } else {
          console.error(response);
        }
      } else if (response['status'] === 403) {
        router.push("/login");
      } else if (response['status'] === 404) {
        router.push("/404")
      }
    })
      .catch(reason => {
        if (exception && typeof exception === 'function') {
          exception(reason)
        }
        console.error(reason);
      })
      .finally(() => {
        if (finalCallback && typeof finalCallback === 'function') {
          finalCallback()
        }
      });
  };

  this.obj = {};

  this.url= function (url) {
    this.obj['url'] = url;
    return this;
  };

  this.method = function (method) {
    this.obj['method'] = method;
    return this;
  };

  this.params = function (params) {
    this.obj['params'] = params;
    return this;
  };
  this.callback = function (callback) {
    this.obj['callback'] = callback;
    return this;
  },
  this.cb = function (callback) {
    this.obj['callback'] = callback;
    return this;
  },
  this.exception = function (exception) {
    this.obj['exception'] = exception;
    return this;
  };

  this.excp = function (exception) {
    this.obj['exception'] = exception;
    return this;
  };
  this.finalCallback = function (finalCallback) {
    this.obj['finalCallback'] = finalCallback;
    return this;
  };
  this.fcb = function(finalCallback) {
    this.obj['finalCallback'] = finalCallback;
    return this;
  };
  this.req = function () {
    this.request(this.obj['url'], this.obj['method'], this.obj['params'], this.obj['callback'],
      this.obj['finalCallback'], this.obj['exception']);
  }
}

Vue.prototype.$h = {
  submitForm(url, method, params, target) {
    var _form = document.getElementById("postForm");
    if (_form) {
      document.body.removeChild(_form);
    }
    _form = document.createElement("form");
    _form['enctype'] = "multipart/form-data";
    _form.action = url;
    if (target) {
      _form.target = target;
    } else {
      // _self
      _form.target = '_blank';
    }

    _form.method = method;
    _form.style.display = 'none';
    for (var item in params) {
      // eslint-disable-next-line no-prototype-builtins
      if (params.hasOwnProperty(item)) {
        var _input = document.createElement('input');
        _input.name = item;
        _input.value = params[item];
        _form.appendChild(_input);
      }
    }
    document.body.appendChild(_form);
    _form.submit();
  },
  get(url) {
    const http = new Http();
    return http.method('get').url(url);
  },
  post(url, params) {
    const http = new Http();
    return !params ? http.method('post').url(url) : http.method('post').url(url).params(params);
  },
  delete(url) {
    const http = new Http();
    return http.method('delete').url(url);
  }
};

Vue.prototype.$input_status = {
  success: 'success',
  waring: 'waring',
  error: 'error',
  validating: 'validating'

};

Vue.use(VueAxios, axios);

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
