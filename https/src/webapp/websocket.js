var $C = {};
var $WS = {
	url : 'ws://localhost:9090',
	websocket : websocket,
	session : guid(),
	onWebSocketOpen : function() {
	},
	onWebSocketClose : function() {
	},
	onWebSocketConnecting : function() {
	},
	onWebSocketConnected : function() {
	},
	onWebSocketMessage : function() {
	},
	onWebSocketErro : function() {
	}
}

function guid() {
	var s4 = function() {
		return Math.floor((1 + Math.random()) * 0x10000).toString(36);
	};
	var result = '';
	for ( var n = 0; n < 128; n++) {
		result += s4();
	}
	return result;
};

$WS.websocketStart = function() {
	WS.onWebSocketConnecting();
	$WS.websocket = new WebSocket($WS.url);
	WS.websocket.onopen = function(evt) {
		onWebSocketConnected();
		onWebSocketOpen(evt)
	};
	WS.websocket.onclose = function(evt) {
		WS.onWebSocketConnecting();
		WS.onWebSocketClose(evt)
		setTimeout(function() {
			WS.websocketStart();
		}, 1000);
	};
	WS.websocket.onmessage = function(evt) {
		eval(evt.data);
		WS.onWebSocketMessage(evt)
	};
	WS.websocket.onerror = function(evt) {
		WS.onWebSocketError(evt)
	};
}

$WS.onWebSocketOpen = function(evt) {
	$WS.websocket.send('session:' + $WS.session);
}

$WS.onWebSocketClose = function(evt) {
}

$WS.onWebSocketConnecting = function() {
	if ($('#webSocketConnecting').length == 0) {
		var $logoTitle = $('<h1>').addClass('text-success').text('BandeiraBR');
		var $waiting = $('<h3>').addClass('text-warning').text('Carregando...');
		var $content = $('<div>').addClass('page-header').addClass(
				'text-center').append($logoTitle).append($('<hr>')).append(
				$waiting);
		$('body').append(
				$('<div>').attr('id', 'webSocketConnecting').css('position',
						'fixed').css('top', 0).css('left', 0).css('width',
						screen.width).css('height', screen.height).css(
						'z-index', Infinity).css('background-color', '#eeeeee')
						.append($content));
	}
}

$WS.onWebSocketConnected = function() {
	$('#webSocketConnecting').fadeOut('slow', function() {
		$('#webSocketConnecting').remove();
	});
}

$WS.onWebSocketMessage = function(evt) {
	$('body').tooltip({
		selector : '[title]',
		container : 'body'
	});
}

$WS.onWebSocketError = function(evt) {
}

$(function() { $WS.websocketStart(); });
