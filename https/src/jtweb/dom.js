function $() {
	var list;
	var nodes = document.childNodes;
	for ( var n = 0; n < arguments.length; n++) {
		list = [];
		var selector = arguments[n];
		for ( var m = 0; m < nodes.length; m++) {
			var node = nodes[m];
			if (typeof selector == 'string') {
				var c = selector.charAt(0);
				if (c == '#') {
					$._query(list, node, selector.substring(1), 1);
				} else if (c == '.') {
					$._query(list, node, selector.substring(1), 2);
				} else if (c == '<') {
					var name = selector.substring(1, selector.indexOf(">", 1));
					list.push(document.createElement(name));
				} else {
					$._query(list, node, selector.toLowerCase(), 3);
				}
			} else if (Array.isArray(selector)) {

			} else if (selector.nodeType != null && selector.nodeType == 1) {
				list = [ selector ];
			}
		}
		nodes = list;
	}
	for ( var k in $.fn) {
		list[k] = $.fn[k];
	}
	return list;
}

$.fn = {};

$._queryNode = function(result, node, key, type) {
	if (node.nodeType != null && node.nodeType == 1) {
		if (type == 1) {
			var classValue = node.getAttribute('id');
			if (classValue != null) {
				if (node.id == key) {
					result.push(node);
				}
			}
		} else if (type == 2) {
			var classValue = node.className;
			if (classValue != null) {
				var indexOf = classValue.indexOf(key);
				if (indexOf >= 0) {
					if (indexOf + key.length >= classValue.length
							|| classValue.charAt(indexOf + key.length) == ' ') {
						result.push(node);
					}
				}
			}
		} else if (type == 3) {
			if (node.nodeName.toLowerCase() == key) {
				result.push(node);
			}
		}
	}
}

$._query = function(result, node, key, type) {
	$._queryNode(result, node, key, type);
	var list = node.childNodes;
	for ( var n = 0; n < list.length; n++) {
		$._query(result, list[n], key, type);
	}
}

$._queryParent = function(result, node, key, type) {
	if (node == null) {
		return;
	}
	$._queryNode(result, node, key, type);
	return $._queryParent(result, node.parentNode, key, type);
};

$.title = function(value) {
	if (arguments.length > 0) {
		return document.title = value;
	} else {
		return document.title;
	}
}

$.fn.html = function(value) {
	if (arguments.length > 0) {
		for ( var n = 0; n < this.length; n++) {
			this[n].innerHTML = value;
		}
		return this;
	} else {
		if (this.length > 0) {
			return this[0].innerHTML;
		} else {
			return null;
		}
	}
}

$.fn.text = function(value) {
	if (arguments.length > 0) {
		for ( var n = 0; n < this.length; n++) {
			this[n].innerText = value;
		}
		return this;
	} else {
		if (this.length > 0) {
			var item = this[0];
			return item.innerText || item.textContent;
		} else {
			return null;
		}
	}
}

$.fn.addClass = function(value) {
	for ( var n = 0; n < this.length; n++) {
		var item = this[n];
		var classValue = item.className;
		if (classValue == null || classValue == undefined) {
			classValue = value;
		} else if (classValue.indexOf(value) < 0) {
			classValue += " " + value;
		}
		classValue = classValue.trim();
		item["class"] = classValue;
		item.attributes["class"] = classValue;
		item.setAttribute("class", classValue);
		item.className = classValue;
	}
	return this;
}

$.fn.removeClass = function(value) {
	for ( var n = 0; n < this.length; n++) {
		var item = this[n];
		var classValue = item.className;
		if (classValue != null) {
			var classes = classValue.split(" ");
			for ( var n = 0; n < classes.length; n++) {
				if (classes[n] == value) {
					classes.remove(n--);
				}
			}
			classValue = classes.join(" ");
		}
		item["class"] = classValue;
		item.attributes["class"] = classValue;
		item.setAttribute("class", classValue);
		item.className = classValue;
	}
	return this;
}

$.fn.hasClass = function(value) {
	for ( var n = 0; n < this.length; n++) {
		var item = this[n];
		var classValue = item.className;
		if (classValue != null) {
			var classes = classValue.split(" ");
			for ( var n = 0; n < classes.length; n++) {
				if (classes[n] == value) {
					return true;
				}
			}
		}
	}
	return false;
}

$.fn.attr = function(key, value) {
	if (arguments.length > 1) {
		for ( var n = 0; n < this.length; n++) {
			var item = this[n];
			if (value == null) {
				item[key] = null;
				item.attributes[key] = null;
				item.removeAttribute(key);
			} else {
				item[key] = value;
				item.attributes[key] = value;
				item.setAttribute(key, value);
			}
		}
		return this;
	} else {
		if (this.length > 0) {
			return this[0].getAttribute(key);
		} else {
			return this;
		}
	}
}

$.fn.attrs = function() {
	var list = [];
	for ( var n = 0; n < this.length; n++) {
		var node = this[n];
		for ( var m = 0; m < node.attributes.length; m++) {
			var attr = node.attributes[m];
			list.push(attr.name);
		}
	}
	return list;
}

$.fn.css = function(key, value) {
	if (arguments.length > 1) {
		for ( var n = 0; n < this.length; n++) {
			var item = this[n];
			var styleValue = item.getAttribute("style");
			if (styleValue == null) {
				styleValue = "";
			}
			var styles = styleValue.split(";");
			if (value == null) {
				for ( var n = 0; n < styles.length; n++) {
					if (styles[n].split(":")[0] == key) {
						styles.remove(n--);
					}
				}
			} else {
				styles.push(key + ":" + value);
			}
			styleValue = styles.join(";");
			item["style"] = styleValue;
			item.attributes["style"] = styleValue;
			item.setAttribute("style", styleValue);
		}
		return this;
	} else {
		if (this.length > 0) {
			var styleValue = this[0].getAttribute("style");
			if (styleValue == null) {
				return null;
			}
			var styles = styleValue.split(";");
			for ( var n = 0; n < styles.length; n++) {
				var tokens = styles[n].split(":");
				if (tokens[0] == key) {
					return tokens[1];
				}
			}
		}
		return null;
	}
}

$.fn.val = function(value) {
	if (arguments.length == 1) {
		for ( var n = 0; n < this.length; n++) {
			this[n].value = value;
		}
		return this;
	} else {
		if (this.length > 0) {
			return this[0].value;
		} else {
			return null;
		}
	}
};

$.fn.parent = function(selector) {
	if (selector != null) {
		var list = [];
		for ( var n = 0; n < this.length; n++) {
			var item = this[n];
			if (typeof selector == 'string') {
				var c = selector.charAt(0);
				if (c == '#') {
					$._queryParent(list, item, selector.substring(1), 1);
				} else if (c == '.') {
					$._queryParent(list, item, selector.substring(1), 2);
				} else {
					$._queryParent(list, item, selector.toLowerCase(), 3);
				}
			}
		}
		for ( var k in $.fn) {
			list[k] = $.fn[k];
		}
		return list;
	} else {
		if (this.length > 0) {
			return $(this[0].parentNode);
		} else {
			return null;
		}
	}
};

$.fn.bind = function(event, func) {
	if (func.name.trim() == "") {
		return this.attr("on" + event, func.toString().split("\n").join(""));
	} else {
		return this.attr("on" + event, func.name);
	}
}

$.fn.unbind = function(event, func) {
	return this.attr("on" + event, null);
}

$.fn.click = function() {
	for ( var n = 0; n < this.length; n++) {
		this[n].click();
	}
	return this;
}

$.fn.focus = function() {
	for ( var n = 0; n < this.length; n++) {
		this[n].focus();
	}
	return this;
}

$.fn.find = function(selector) {
	var list = [];
	for ( var n = 0; n < this.length; n++) {
		var item = this[n];
		if (typeof selector == 'string') {
			var c = selector.charAt(0);
			if (c == '#') {
				$._query(list, item, selector.substring(1), 1);
			} else if (c == '.') {
				$._query(list, item, selector.substring(1), 2);
			} else {
				$._query(list, item, selector.toLowerCase(), 3);
			}
		}
	}
	for ( var k in $.fn) {
		list[k] = $.fn[k];
	}
	return list;
};

$.fn.is = function(name) {
	for ( var n = 0; n < this.length; n++) {
		var item = this[n];
		var c = name.charAt(0);
		if (c == '#') {
			if (item.id == name.substring(1)) {
				return true;
			}
		} else if (c == '.') {
			var classValue = item.getAttribute("class");
			if (classValue != null) {
				var indexOf = classValue.indexOf(name);
				if (indexOf >= 0) {
					if (indexOf + name.length >= classValue.length
							|| classValue.charAt(indexOf + name.length) == ' ') {
						return true;
					}
				}
			}
		} else {
			if (item.nodeName.toLowerCase() == name) {
				return true;
			}
		}
	}
	return false;
}

$.fn.append = function(tag) {
	for ( var n = 0; n < this.length; n++) {
		for ( var m = 0; m < tag.length; m++) {
			this[n].appendChild(tag[m]);
		}
	}
};

$.fn.show = function() {
	return this.css('display', null);
}

$.fn.hide = function() {
	return this.css('display', 'none');
}

$.fn.enable = function() {
	return this.attr('disabled', null);
}

$.fn.disable = function() {
	return this.attr('disabled', 'disabled');
}

$.fn.isEnabled = function() {
	return this.attr('disabled') != 'disabled';
}
