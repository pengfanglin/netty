let socket = null;
reconnect();
let heartCheck = {
  timeout: 10000, //10秒发一次心跳
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
        console.log('发送了心跳数据');
        let msg = {
          type: 'HEARTBEAT',
          data: ''
        };
        websocket.send(JSON.stringify(msg));
        //如果超过一定时间还没重置，说明后端主动断开了
        self.serverTimeoutObj = setTimeout(function () {
          websocket.close();
        }, self.timeout)
      }, this.timeout
    );
  }
};

//重新链接
function reconnect() {
  setTimeout(function () {
    console.log("重链了");
    if (window.WebSocket) {
      socket = new WebSocket('ws://127.0.0.1:9090/ws');
    } else {
      alert("浏览器不支持WebSocket");
    }
    //监听窗口关闭事件
    window.onbeforeunload = function () {
      console.log("窗口关闭");
      socket.close();
    };

    socket.onopen = function () {
      //重置心跳检测
      heartCheck.reset().start(socket);
      console.log("连接成功");
    };

    socket.onclose = function () {
      //链接断开后重连
      console.log("连接关闭");
      if (heartCheck.serverInterval == null) {
        heartCheck.serverInterval = setInterval(function () {
          console.log('当前链接状态' + socket.readyState);
          if (socket.readyState !== 1) {
            socket.close();
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
      console.log(msg.code);
      let ta = document.getElementById("responseText");
      ta.value = ta.value + "\n" + event.data;
    };
    socket.onerror = function () {
      console.log("websocket连接出错");
      reconnect();
    };
  }, 1000);
}


function send(message) {
  try {
    socket.send(message);
  } catch (err) {
    console.log('消息发送失败' + err);
  }
}

function close() {
  socket.close();
  reconnect();
}

window.onbeforeunload = function () {
  socket.close();
};