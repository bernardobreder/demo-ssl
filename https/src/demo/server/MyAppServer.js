$C = [];

$WS.onWebSocketMessage = function(e) {
	eval(e.data);
}

$WS.onWebSocketPopState = function() {
	if ($WS.websocket.readyState == WebSocket.OPEN) {
		var request = {
			method : "page",
			url : window.location.pathname
		};
		if (window.location.search.length > 0) {
			var search = window.location.search.substring(1);
			var params = search.split("&");
			for ( var n = 0; n < params.length; n++) {
				var param = params[n];
				var item = param.split("=");
				request[item[0]] = item[1];
			}
		}
		var message = JSON.stringify(request);
		$WS.websocket.send(message);
	}
}
