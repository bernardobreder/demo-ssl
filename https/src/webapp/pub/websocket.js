var wsUri = "ws://localhost:9090/";
var n = 0;
var websocket;
var $C = {};
var $WS = {
	websocket : websocket,
	session : guid(),
	send : function(msg) {
		$WS.websocket.send(msg);
	}
}

function guid() {
	var s4 = function() {
		return Math.floor((1 + Math.random()) * 0x10000).toString(36);
	};
	var result = "";
	for ( var n = 0; n < 128; n++) {
		result += s4();
	}
	return result;
};

function init() {
	freeze();
	var websocket = $WS.websocket = new WebSocket(wsUri);
	websocket.onopen = function(evt) {
		onOpen(evt)
	};
	websocket.onclose = function(evt) {
		onClose(evt)
	};
	websocket.onmessage = function(evt) {
		onMessage(evt)
	};
	websocket.onerror = function(evt) {
		onError(evt)
	};
}

function onOpen(evt) {
	unfreeze();
	$WS.websocket.send("session:" + $WS.session);
}

function onClose(evt) {
	freeze();
	setTimeout(function() {
		init();
	}, 1000);
}

function freeze() {
	if ($("#freeze").length == 0) {
		$("body").append(
				$("<div>").attr("id", "freeze").css("position", "fixed").css(
						"top", 0).css("left", 0).css("width", screen.width)
						.css("height", screen.height).css("z-index", Infinity)
						.css("background-color", "#eeeeee").css({
							opacity : 0.5
						}));
	}
}

function unfreeze() {
	$("#freeze").fadeOut("slow", function() {
		$("#freeze").remove();
	});
}

function onMessage(evt) {
	console.info(evt.data);
	eval(evt.data);
	$('body').tooltip({
		selector : "[title]",
		container : "body"
	});
}

function onError(evt) {
	// alert('<span style="color: red;">ERROR:</span> ' + evt.data);
}

$(function() {
	init();
});
