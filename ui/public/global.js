(function (win) {
    var G = function () {};

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
        for(var item in params) {
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

        if (typeof source ===  'number' ||
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

    G.prototype.request = function(url, method, params, callback, finalCallback) {
        var rx;
        method = !!method ? method : 'get';
        if (!!params) {
            if ('get' === method) {
                var encodeParams = {};
                for(var key in params) {
                    if (params.hasOwnProperty(key)) {
                        encodeParams[key] = encodeURI(params[key]);
                    }
                }
                rx = axios.get(url, {
                    params: encodeParams,
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    }
                });
            } else if ('post' === method){
                rx = axios.post(url, params);
            } else if ('delete' === method) {
                rx = axios.delete(url, {
                    data: params
                });
            } else if ('put' === method) {
                rx = axios.put(url, params);
            }
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
        }).catch(function (reason) { console.error(reason) }).finally(function () {
            if (!!finalCallback && typeof finalCallback === 'function') {
                finalCallback();
            }
        })
    };

    var Http =  function () {
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
        this.callback = function(callback) {
            obj['callback'] = callback;
            return this;
        };
        this.finalCallback = function(finalCallback) {
            obj['finalCallback'] = finalCallback;
            return this;
        };
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
            win.G.request(obj['url'], obj['method'], obj['params'], obj['callback'], obj['finalCallback']);
        };
        this.obj = obj;
        return this;
    };
    G.prototype.http = new Http();
    G.prototype.get = new Http().method('get');
    G.prototype.post = new Http().method('post');
    G.prototype.delete = new Http().method('delete');
    G.prototype.put = new Http().method('put');

    win.G = new G();
})(window);