var $C = {};

var $H = {};

var $WS = {
	url : ${wsHost},
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
$WS.onWebSocketOpen = function() {
	$WS.websocket.send('session:' + $WS.session + '\nurl: '
			+ window.location.pathname + window.location.search);
};
$WS.websocketStart = function() {
	$WS.onWebSocketConnecting();
	$WS.websocket = new WebSocket($WS.url);
	$WS.websocket.onopen = function(evt) {
		$WS.onWebSocketConnected();
		$WS.onWebSocketOpen(evt)
	};
	$WS.websocket.onclose = function(evt) {
		$WS.onWebSocketConnecting();
		$WS.onWebSocketClose(evt)
		setTimeout(function() {
			$WS.websocketStart();
		}, 1000);
	};
	$WS.websocket.onmessage = function(evt) {
		console.info(evt.data);
		eval(evt.data);
		$WS.onWebSocketMessage(evt)
	};
	$WS.websocket.onerror = function(evt) {
		$WS.onWebSocketError(evt)
	};
};
$WS.websocketStart();
