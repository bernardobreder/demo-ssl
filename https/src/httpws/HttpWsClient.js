var $WS = {
	url : ${url},
	path : ${path},
	session : ${session},
	websocket : null,
	onWebSocketOpen : function(e) {
	},
	onWebSocketClose : function(e) {
	},
	onWebSocketMessage : function(e) {
	},
	onWebSocketError : function(e) {
	},
	onWebSocketConnecting : function() {
	},
	onWebSocketConnected : function() {
	},
};

$WS.websocketStart = function() {
	var url = $WS.url + window.location.pathname + window.location.search;
	if (url.indexOf("?") < 0) {
		url += "?session=" + $WS.session;
	} else {
		url += "&session=" + $WS.session;
	}
	$WS.onWebSocketConnecting();
	$WS.websocket = new WebSocket(url);
	$WS.websocket.onopen = function(evt) {
		$WS.onWebSocketConnected();
		$WS.onWebSocketOpen(evt)
	};
	$WS.websocket.onclose = function(evt) {
		$WS.onWebSocketClose(evt)
		setTimeout(function() {
			$WS.websocketStart();
		}, 1000);
	};
	$WS.websocket.onmessage = function(evt) {
		$WS.onWebSocketMessage(evt)
	};
	$WS.websocket.onerror = function(evt) {
		$WS.onWebSocketError(evt)
	};
};

$WS.websocketStart();
