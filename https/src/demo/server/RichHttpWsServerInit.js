$C = [];

$WS.onWebSocketMessage = function(e) {
	eval(e.data);
}

if ($WS.websocket.readyState == WebSocket.OPEN) {
	$WS.websocket.send('" + message + "');
}
