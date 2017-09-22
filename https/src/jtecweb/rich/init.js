$S = {};
$S.init = function() {
	var script = function(path, callback) {
		var tag = document.createElement('script');
		tag.type = 'text/javascript';
		tag.src = path;
		tag.onload = callback;
		document.head.appendChild(tag);
	}
	var css = function(path, callback) {
		var tag = document.createElement('link');
		tag.rel = 'stylesheet';
		tag.href = path;
		tag.onload = callback;
		document.head.appendChild(tag);
	}
	var count = 3;
	var check = function() {
		if (--count == 0) {
			$S.load();
		}
		console.info(count);
	}
	script("http://code.jquery.com/jquery-2.1.0.min.js", check);
	script("http://getbootstrap.com/dist/js/bootstrap.min.js", check);
	css("http://getbootstrap.com/dist/css/bootstrap.min.css", check);
}

$S.load = function() {
	alert(1);
}

$S.init();