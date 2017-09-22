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
	$WS.onWebSocketConnecting();
	$WS.websocket = new WebSocket($WS.url + $WS.path + "?session=" + $WS.session);
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
