let heartCheck = {
  //10秒发一次心跳
  checkTime: 10000,
  //5秒还没收到心跳响应，说明后端主动断开了
  timeout: 5000,
  timeoutObj: null,
  serverTimeoutObj: null,
  serverInterval: null,
  reset: function () {
    clearTimeout(this.timeoutObj);
    clearTimeout(this.serverTimeoutObj);
    return this;
  },
  start: function (websocket) {
    let self = this;
    this.timeoutObj = setTimeout(function () {
      let msg = {
        type: 'HEARTBEAT',
        data: ''
      };
      if (socket.readyState === WebSocket.OPEN) {
        try {
          websocket.send(JSON.stringify(msg));
        } catch (err) {
          console.log('消息发送失败' + err);
        }
      } else {
        console.log("连接尚未开启");
      }
      console.log('发送了心跳检测');
      //如果超过一定时间还没重置，说明后端主动断开了
      self.serverTimeoutObj = setTimeout(function () {
        console.log('后端主动断开连接，关闭链接');
        if (socket.readyState !== 3) {
          websocket.close();
        }
      }, self.timeout);
    }, this.checkTime);
  }
};

let socket = {
  send: function send(message) {
    if (socket.readyState === WebSocket.OPEN) {
      try {
        socket.send(message);
      } catch (err) {
        console.log('消息发送失败' + err);
      }
    } else {
      alert("连接尚未开启");
    }
  },
  close: function close() {
    if (socket.readyState !== 3) {
      socket.close();
    }
  },
  reconnect: function reconnect() {
    if (window.WebSocket) {
      socket = new WebSocket('ws://127.0.0.1:9090/ws');
    } else {
      alert("浏览器不支持WebSocket");
    }
    //监听窗口关闭事件
    window.onbeforeunload = function () {
      console.log("窗口关闭");
      if (socket.readyState !== 3) {
        socket.close();
      }
    };

    socket.onopen = function () {
      //重置心跳检测
      heartCheck.reset().start(socket);
      console.log("连接成功");
    };

    socket.onclose = function () {
      console.log("连接关闭,开始重连");
      if (heartCheck.serverInterval == null) {
        heartCheck.serverInterval = setInterval(function () {
          if (socket.readyState !== 1 && socket.readyState !== 0) {
            reconnect();
          } else {
            clearInterval(heartCheck.serverInterval);
            heartCheck.serverInterval = null;
            heartCheck.reset().start(socket);
          }
        }, 1000);
      }
    };

    socket.onmessage = function (event) {
      //重置心跳检测
      heartCheck.reset().start(socket);
      let msg = JSON.parse(event.data);
      let ta = document.getElementById("responseText");
      ta.value = ta.value + "\n" + event.data;
    };
    socket.onerror = function () {
      console.log("websocket连接出错");
      reconnect();
    };
  }
};
socket.reconnect();