function connectWebViewJavascriptBridge(callback) {
            if(window.EcoJsBridge) {
                return callback(window.EcoJsBridge);
            }

            var WVJBIframe = document.createElement('iframe');
            WVJBIframe.style.display = 'none';

            // set iframe src delay because below Android 4.4
            // no work
            // this should be optimized !!!
            document.documentElement.appendChild(WVJBIframe);
            setTimeout(function() {
                WVJBIframe.src = 'jsb://__BRIDGE_LOADED__';
                setTimeout(function() {document.documentElement.removeChild(WVJBIframe);}, 500);
             }, 1000);

            document.addEventListener(
                'EcoJavascriptBridgeReady',
                function() {
                    callback(window.EcoJsBridge);
                },
                false);
        }

connectWebViewJavascriptBridge(function(bridge) {
            bridge.init(function(message, responseCallback) {
                console.log('xxx JS got a message', message);
                var data = {
                    'Javascript Responds': '测试中文!'
                };
                console.log('xxx JS responding with', data);
                responseCallback(data);
            });

            //just reserve for joke
            bridge.registerHandler("functionInJs", function(data, responseCallback) {
                document.getElementById("show").innerHTML = ("data from Java: = " + data);
                var responseData = "Javascript Says Right back aka!";
                responseCallback(responseData);
            });
   })

