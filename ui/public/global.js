(function (win) {
    var G = function () { };

    G.prototype.copy = function (obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    G.prototype.decodeBase64Content = function (base64Content) {
        let commonContent = base64Content.replace(/\s/g, '+');
        commonContent = Buffer.from(commonContent, 'base64').toString();
        return commonContent;
    }

    G.prototype.encodeBase64Content = function (commonContent) {
        let base64Content = Buffer.from(commonContent).toString('base64');
        return base64Content;
    }

    G.prototype.postForm = function (url, params, target) {
        win.G.submitForm(url, 'post', params, target);
    };

    G.prototype.submitForm = function (url, method, params, target) {
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
            if (params.hasOwnProperty(item)) {
                var _input = document.createElement('input');
                _input.name = item;
                _input.value = params[item];
                _form.appendChild(_input);
            }
        }
        document.body.appendChild(_form);
        _form.submit();
    };

    G.prototype.deepCopy = function (source) {
        if (typeof source === 'function') {
            return source;
        }

        if (source === undefined || source === null) {
            // console.log('common.deepCopy:undefined or null', source);
            return source;
        }

        if (typeof source === 'number' ||
            typeof source === 'string' ||
            typeof source === 'boolean') {
            // console.log('common.deepCopy:basic type', source);
            return source;
        }

        var target, i = 0, length, key, _item, _temp;

        if (source instanceof Array) {
            target = [];
            if (source.length > 0) {
                // console.log('common.deepCopy:Array type', source);
                length = source.length;
                for (; i < length; i++) {
                    _item = source[i];
                    _temp = win.G.deepCopy(_item);
                    target[i] = _temp;
                }
            }
            return target;
        }

        // console.log('common.deepCopy:Object type', source);
        target = {};
        for (key in source) {
            if (source.hasOwnProperty(key)) {
                _item = source[key];
                _temp = win.G.deepCopy(_item);
                target[key] = _temp;
            }
        }
        return target;
    };

    G.prototype.request = function (url, method, params, callback, finalCallback, exception) {
        var rx;
        method = !!method ? method : 'get';
        var token = Cookie.getCookie('token');
        var auth = 'ZShop ' + token;
        if ('get' === method) {
            var encodeParams = {};

            for (var key in params) {
                if (params.hasOwnProperty(key)) {
                    encodeParams[key] = encodeURI(params[key]);
                }
            }
            rx = axios.get(url, {
                params: encodeParams,
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    'Authorization': auth
                }
            });

        } else if ('post' === method) {
            rx = axios.post(url, params, { headers: { 'Authorization': auth } });
        } else if ('delete' === method) {
            rx = axios.delete(url, {
                data: params,
                headers: { 'Authorization': auth }
            });
        } else if ('put' === method) {
            rx = axios.put(url, params, { headers: { 'Authorization': auth } });
        }

        rx.then(function (value) {
            if (value['status']) {
                if (!!callback && typeof callback === 'function') {
                    callback(value['data']);
                } else {
                    console.error('invalid callback', callback)
                }
            } else {
                console.error(value);
            }
        })
        .catch(function (reason) {
            if (!!exception && typeof exception === 'function') {
                exception(reason);
            }
        })
        .finally(function () {
            if (!!finalCallback && typeof finalCallback === 'function') {
                finalCallback();
            }
        })
    };

    var Http = function () {
        var obj = {};
        this.url = function (url) {
            obj['url'] = url;
            return this;
        };
        this.method = function (method) {
            obj['method'] = method;
            return this;
        };
        this.params = function (params) {
            obj['params'] = params;
            return this;
        };
        this.callback = function (callback) {
            obj['callback'] = callback;
            return this;
        };
        this.cb = function (cb) {
            obj['cb'] = cb;
            return this;
        }
        this.finalCallback = function (finalCallback) {
            obj['finalCallback'] = finalCallback;
            return this;
        };
        this.fcb = function (finalCallback) {
            obj['fcb'] = finalCallback;
            return this;
        }
        this.ex = function (cb) {
            obj['exception'] = cb;
            return this;
        }
        this.req = function () {
            this.request();
        },
            this.request = function () {
                // console.log('G request', obj);
                if (!obj['url']) {
                    console.error('invalid url', obj['url']);
                    return;
                }
                if (!obj['method']) {
                    console.error('invalid method', obj['method']);
                    return;
                }

                if (typeof obj['cb'] === 'function') {
                    obj['callback'] = obj['cb'];
                }
                if (typeof obj['fcb'] === 'function') {
                    obj['finalCallback'] = obj['fcb'];
                }

                win.G.request(obj['url'], obj['method'], obj['params'], obj['callback'],
                    obj['finalCallback'], obj['exception']);
            };
        this.obj = obj;
        return this;
    };

    G.prototype.get = function (url, params) {
        var http = new Http();
        http.method('get').url(url);
        if (!!params) {
            http.params(params);
        }
        return http;
    };
    G.prototype.post = function (url, params) {
        var http = new Http();
        http.method('post').url(url);
        if (!!params) {
            http.params(params);
        }
        return http;
    }
    G.prototype.delete = function (url, params) {
        var http = new Http();
        http.method('delete').url(url);
        if (!!params) {
            http.params(params);
        }
        return http;
    }

    win.G = new G();

})(window);

// 不能删哦，因为用着的
var Cookie = {
    setCookie: function (cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    },
    getCookie: function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
        }
        return "";
    },
    clearCookie: function (name) {
        Cookie.setCookie(name, "", -1);
    }
};