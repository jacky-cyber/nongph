/**
 * global.js v0.3
 * Creator : Leegorous
 */
var $win, $doc;
if (!$win) $win = window;
if (!$doc) $doc = document;

//-- global variable
var _$f;
var DEFAULT_DOMAIN=null;

var APPLICATION_PATH = (function () {
	var path = $win.location.pathname;
	var idx;
	if ((idx=path.indexOf('/',1))>1) {
		path = path.substring(0,idx);
	} else {
		path = '';
	}
	return path;
})();

if (typeof Node=="undefined") {
	var Node = {
		ELEMENT_NODE: 1,
		ATTRIBUTE_NODE: 2,
		TEXT_NODE: 3,
		CDATA_SECTION_NODE: 4,
		ENTITY_REFERENCE_NODE: 5,
		ENTITY_NODE: 6,
		PROCESSING_INSTRUCTION_NODE: 7,
		COMMENT_NODE: 8,
		DOCUMENT_NODE: 9,
		DOCUMENT_TYPE_NODE: 10,
		DOCUMENT_FRAGMENT_NODE: 11,
		NOTATION_NODE: 12
	}
}

/**
 * The $ function is a handy alias for document.getElementById.
 * Invoking it with the element id.
 */
function $(id) {
	return $doc.getElementById(id);
}

/**
 * Abstract object
 */
var Abstract = {
	empty:function() {throw Abstract.ExecuteAbstractMethodException;},
	ExecuteAbstractMethodException:new Error("Please implements this method before use it.")
};

var emptyFunc=function() {};

/**
 * Class object
 *
 * <p>This object is the important part of foundation stone of this framework.
 * In javascript, there is actually no class mechanism, but the prototype base
 * object mechanism. And sometimes we do need some "class" to describe some
 * kind of objects.<br/>
 * Use this class object, you can define and find defined classes easily.</p>
 *
 * <p>To create classes, use the create method. <br/>
 * The subclass will extend all fields and methods from the superClass and interfaces.
 * You can access the extended fields or methods directly if you haven't overrided them.</p>
 * <del>But once overrided, there is no way to get it back. So the best way is to use
 * the different name.</del>
 * 
 * <p>To invoke the method had been overrided, inside the method add
 * <code>this[arguments.callee.parent](args, ...);</code>
 * It likes <code>super.currentMethod(args, ...);</code> in Java.</p>
 *
 * example:
 *   Class.create("className","superClassName",["interface1",...,"interfaceN"],{
 *       // initialize method
 *       init:function(args) {...},
 *       // static field or method
 *       $tatic:{
 *           STATIC_FIELD:"example value",
 *           STATIC_METHOD:function(args) {...}
 *       },
 *       // different implements for different web browser.
 *       impl:{moz:{...},ie:{...}},
 *       // other field or method
 *       // I usually treat the field or method name with initial "_" as private.
 *       _field1:true,
 *       field1:9999,
 *       method1:function(args) {...}
 *   });
 *
 * To find created classes, use the get method.
 * example:
 *   var class1 = Class.get("className");
 *   var obj1 = new class1(args);
 *   //You can access the static field like this:
 *   var v = class1.STATIC_FIELD;
 *   v = class1.STATIC_METHOD(v);
 * 
 * @author Leegorous
 * @version 0.36
 *
 * @since version 0.36
 * The init method has been kept.
 * A new syntax 'this[arguments.callee.parent]()' (means 'super.currentMethod()' in Java) is supported.
 * So the old way using method alias is deprecated.
 *
 * @since version 0.35
 * Anonymity class define is supported.
 *
 * @since version 0.34
 * The "_super" method is deprecated.
 *
 * @since version 0.33
 * The Class object is enhanced to build class for different web browser (IE or Mozilla).
 * To define a class for different web browser, set the attribute "impl" with a specific object:
 *   {moz:{...},ie:{...}}
 * 
 * @since version 0.32
 * The "__class" attribute may not refer to the right class object when it is in the
 * inherited hierarchy. So this.__class is disabled.
 * 
 * @since version 0.31
 * In the instance object, the "__class" attribute refer to the class of this object.
 *   this.__class
 */
var Class = {
	TryWithoutThrowingException:false,
	_classId:1,
	__classdepository__ : {},
	__classdefinitions__ : {},

	/**
	 * Create a new class.
	 *
	 * @param _name (String) the class name, null for anonymity class
	 * @param _super (String) the super class name
	 * @param _interfaces (String[]) an array of interfaces' name
	 * @param defination (Object) the defination body object
	 */
	create:function(_name,_super,_interfaces,defination) {
		var classDefineObj = {
			className:_name,
			_superClass:_super,
			_interfaces:_interfaces,
			_define:defination || {}
		};

		//if (!_name) _name=this._classId++;
		if (!_name) {
			//_name=this._classId++;
			return Class.initClass(classDefineObj);
		}
		//this.__classdepository__[_name]={
		this.__classdefinitions__[_name] = classDefineObj;
		return _name;
	},

	/**
	 * Get the class object from the class repository
	 *
	 * @param name (String) the class name
	 */
	get:function(name) {
		var _o=this.__classdepository__[name];
		if (_o) return _o;
		else if (_o=this.__classdefinitions__[name]) return Class.initClass(_o);
		else if (this.TryWithoutThrowingException) return null;
		else throw this.createClassNotFoundException(name);
		/*
		if (_o) {
			if (_o.body) return _o.body;
			else return Class.initClass(_o);
		} else {
			if (this.TryWithoutThrowingException) return null;
			else throw this.createClassNotFoundException(name);
		}*/
	},

	initClass:function(obj) {
		var _define=obj._define,_init,_impl,_static;//,_super=''
		// give a blank constructor if no constructor defined.
		//if (!(_init=_define.init)) _init=function() {};
		_define.init = _define.init || function() {};
		_init = _define.init;
		// keep the initialize function/constructor since v0.36
		//delete _define.init;
		_impl=_define.impl;
		_static=_define.$tatic;
		delete _define.impl;
		delete _define.$tatic;
		
		// extend field/method from interfaces
		if (obj._interfaces) {
			var _interfaces = obj._interfaces;
			for (var i=0, j=_interfaces.length; i<j; i++) {
				Object.extend(_init.prototype, Class.get(_interfaces[i]).prototype);
			}
		}
		
		if (obj._superClass) {
			/*
			 * The method "_super" set in this way, cause it will induce the unexpected 
			 * recursion when it has been set in prototype.
			 *
			 * The method "_super" is deprecated.
			_init=""+_init;
			var _idx = _init.indexOf('{');
			_super="this._super=Class.get('"+obj._superClass+"');";//this._();
			_init=_init.substring(0,_idx+1)+_super+_init.substring(_idx+1);
			eval("_init="+_init);
			 */
			Object.extend(_init.prototype,Class.get(obj._superClass).prototype);
			//Class._extend(_init.prototype, Class.get(obj._superClass).prototype);
		}
		
		// extend fields/methods from current class defination
		Class._extend(_init.prototype, _define);
		
		// extend implements from the different browser
		if (_impl && DOM.browser) 
			Class._extend(_init.prototype, _impl[DOM.browser.isMoz?"moz":"ie"]);
			//Object.extend(_init.prototype,_impl[DOM.browser.isMoz?"moz":"ie"]);
		
		// extend the static fields/methods
		if (_static) {
			if (_impl=_static.impl) {
				delete _static.impl;
				Object.extend(_static,_impl[DOM.browser.isMoz?"moz":"ie"]);
			}
			Object.extend(_init,_static);
		}
		
		//Class._initInterfaces(_init,obj._interfaces,obj._define);
		/*
		 * For there is no any class casting, the this.__class will refer to the right class object
		 * only while it has been used in the subclass. But every class could be a super class, 
		 * then this.__class will not be used in any case. And I deleted it.
		 */
		//_init.prototype.__class=_init;
		//obj.body=_init;
		_define = _impl = _static = null;
		var className = obj.className;
		//var className = obj.className || "AnonymityClass";
		if (className) _init.className = className;
		else _init.className = (className = "AnonymityClass") +"$"+this._classId++;
		//if (obj.className) this.__classdepository__[obj.className]=_init;
		//else return _init;
		this.__classdepository__[className] = _init;
		_init=null;//return obj.body;
		return this.__classdepository__[className];
	},
	
	/**
	 * Extend field or method form source
	 */
	_extend : function(target, source) {
		var fm = "function";
		for (var i in source) {
			if (typeof source[i] == fm) {// extend method
				// if the target has a non-empty method with the same name
				if (target[i] && target[i] != Abstract.empty) {
					var c = 0, name;
					// generate a new name for the original method
					while (target[(name = i + "_$"+c++)]) {}
					// give the original method a new name
					target[name] = target[i];
					source[i].parent = name;
				}
			}
			target[i] = source[i];
		}
	},
	
	_initInterfaces:function(func,_interfaces,_define) {
		var fp=func.prototype, alias = [];
		if (_interfaces) {
			for (var i=0,j=_interfaces.length; i<j; i++) {
				Object.extend(fp,Class.get(_interfaces[i]).prototype);
			}
		}
		if (_define) {
			//Object.extend(fp,_define);
			for (var i in _define) {
				if (_define[i] && typeof _define[i]=="object" && _define[i].isMethodAlias) alias.push(i, _define[i].name);
					//fp[i] = fp[_define[i].name];
				else {
					if (fp[i] && typeof fp[i]=="function") {
						var c = 0;
						while(fp[i+"_"+c++]) {
						}
						var name = i+"_"+(c-1);
						fp[name] = fp[i];
						_define[i].parent = name;
					}
					fp[i] = _define[i];
				}
			}
		}
		for (var i=0,j=alias.length; i<j; i+=2) {
			fp[alias[i]]=fp[alias[i+1]];
		}
	},
	createClassNotFoundException:function(n) {return new Error('Class "'+n+'" Not Found.');}
};

/**
 * @deprecated
 *
 * @class MethodAlias
 */
Class.create("MethodAlias",null,null,{
	isMethodAlias : true,
	init : function(name) {
		this.name = name;
	}
});

/**
 * @deprecated
 */
var MethodAlias = Class.get("MethodAlias");

/**
 * Interface object
 */
var Interface={
	/**
	 * @param name (String) the interface name
	 * @param superInterfaces (String[]) an array of super classes' name
	 * @param defination (Object) the defination body object
	 */
	create:function(name,superInterfaces,defination) {
		return Class.create(name,null,superInterfaces,defination);
	}
};

/**
 * Object.extend() is the foundation stone of this framework.
 * It copies all properties from the source to the destination object.
 * @param destination (Object) the destination object
 * @param source (Object) the source object
 */
Object.extend = function(destination, source) {
	for (var property in source) 	{
		destination[property] = source[property];
	}
	return destination;
};

Object.extendPlus = function(destination, source) {
	for (var property in source) 	{
		if (typeof destination[property]=="undefined") destination[property] = source[property];
	}
	return destination;
};

Object.extend(Object,{
	clone : function(obj, deep) {
		if (deep) return Object.cloneDeep(obj);
		return Object.extend({},obj);
	},
	cloneDeep : function(obj) {
		var dest = {};
		for (var i in obj) {
			if (obj[i] && typeof obj[i]=="object") {
				if (obj[i].clone) dest[i] = obj[i].clone(true);
				else dest[i] = Object.cloneDeep(obj[i]);
			} else dest[i] = obj[i];
		}
		return dest;
	},
	toJSON:function(obj) {
		switch(typeof obj) {
			case "function":
			case "undefined":
			case "unknown": return;
			case "boolean": return ""+obj;
		}
		if (obj==null) return "null";
		if (obj.toJSON) return obj.toJSON();
		if (obj.ownerDocument==document) return;
		var _a=[],_v;
		for (var i in obj) {
			_v=Object.toJSON(obj[i]);
			if (_v!=undefined) _a.push('"'+i+'":'+_v);
		}
		return '{'+_a.join(',')+'}';
	}
});

/**
 * Array prototype extension
 */
var $break = new Object();
var $continue = new Object();
Object.extend(Array.prototype,{
	clone: function(deep) {
		if (deep) return this.cloneDeep();
		var _t = [];
		for(var i=0,n=this.length; i<n; i++) {
			_t.push(this[i]);
		}
		return _t;
	},
	cloneDeep : function() {
		var _t = [];
		for (var i=0,j=this.length; i<j; i++) {
			if (this[i] && typeof this[i]=="object") {
				if (this[i].clone) _t.push(this[i].clone(true));
				else _t.push(Object.cloneDeep(this[i]));
			} else _t.push(this[i]);
		}
		return _t;
	},
	each: function(iterator) {
		for (var i=0,j=this.length; i<j; i++) {
			try {
				iterator(this[i]);
			} catch (e) {
				if (e==$break) break;
				if (e==$continue) continue;
				throw e;
			}
		}
	},
	include: function(value) {
		for (var i=0,j=this.length; i<j; i++) {
			if (this[i] === value) return true;
		}
		return false;
	},

	/**
	 * Get the index of the item in array
	 *
	 * @param {Variable} item
	 * @returns {int} -1 if not found
	 */
	indexOf : function(item) {
		for (var i=0, j=this.length; i<j; i++) 
			if (this[i] === item) return i;
		return -1;
	},

	insert: function(idx,obj) {
		var len = this.length;
		if (idx>=0 && idx<=len) {
			this.splice(idx,0,obj);
			return true;
		} else if (idx>len) {
			this[idx]=obj;
		}
		return false;
	},
	remove: function(v) {
		var ol = this.length;
		for (var i=ol-1; i>=0; i--) {
			if (this[i]==v) {
				this.splice(i,1);
			}
		}
		if (this.length<ol) return true;
		return false;
	},
	removeById: function(id) {
		if (id>=0 && id<this.length) {
			this.splice(id,1);
			return true;
		}
		return false;
	},
	toJSON:function() {
		var _a=[],value;
		for (var i=0,j=this.length;i<j;i++) {
			value=Object.toJSON(this[i]);
			if (value !== undefined) _a.push(value);
		}
		return '[' + _a.join(',') + ']';
	},
	uniq: function() {
		var _arr = [];
		for (var i=0,j=this.length; i<j; i++) {
			if (!_arr.include(this[i])) _arr.push(this[i]);
		}
		return _arr;
	}
});

/*
 * Note: About the data conversion between Date and JSON
 * Here I use UTC string as the benchmark.
 */
Date.parseFromJSON=function(str) {
	var _s=Date.parse(str);
	if (isNaN(_s)) return null;
	_s=new Date(_s-(new Date().getTimezoneOffset())*60*1000);
	return _s;
}

Object.extend(Date.prototype,{
	toJSON:function() {
		return '"'+this.getUTCHours()+':'+this.getUTCMinutes()+':'+
			this.getUTCSeconds()+' '+(this.getUTCMonth()+1)+'/'+
			this.getUTCDate()+'/'+this.getUTCFullYear()+'"';
	}
});

Object.extend(Number.prototype,{
	toJSON:function() {
		return isFinite(this) ? this.toString() : 'null';
	}
});

String.interpret=function(value) {
	return value == null ? '' : String(value);
};
String.specialChar={
	'\b': '\\b',
	'\t': '\\t',
	'\n': '\\n',
	'\f': '\\f',
	'\r': '\\r',
	'\\': '\\\\'
};

/*
 * String prototype extension
 */
Object.extend(String.prototype,{
	gsub:function(pattern,replacement) {
		var _a=this,_b='',_m;
		while(_a.length>0) {
			if (_m=_a.match(pattern)) {
				_b+=_a.slice(0, _m.index);
				_b+=String.interpret(replacement(_m));
				_a=_a.slice(_m.index + _m[0].length);
			} else {
				_b+=_a;_a='';
			}
		}
		return _b;
	},
	trim: function() {
		return this.replace(/^\s+/,'').replace(/\s+$/,'');
	},
	toJSON:function() {
		var _a=this.gsub(/[\x00-\x1f\\]/,function(match) {
			var _c=String.specialChar[match[0]],_t;
			return _c?_c:'\\u00' + ((_t=match[0].charCodeAt().toString(16)).length==2?_t:"0"+_t);
		});
		return '"'+_a.replace(/"/g, '\\"')+'"';
	}
});

/*
 * utils
 */
var Try = {
	these: function() {
		var returnValue;
		for (var i = 0, length = arguments.length; i < length; i++) {
			var lambda = arguments[i];
			try {
				returnValue = lambda();
				break;
			} catch (e) {}
		}
		return returnValue;
	}
}﻿/**
 * DOM Object provide support for dealing with event listeners
 * Usage:
 *   If the element exist, you can use 
 *       DOM.addEventListener(elem,type,listener);
 *   or 
 *       DOM.removeEventListener(elem,type,listener);
 *   to add or remove the event listener.
 *   After attach the event listener, you should use
 *       DOM.sinkEvent(elem,bits);
 *
 *   If the element does not exist yet, and you are composing
 *   the HTML code, you can use
 *       DOM.addPreEventListener(evtId,type,listener);
 *   or
 *       DOM.removePreEventListener(evtId,type,listener);
 *   to add or remove the event listener to the event sink for
 *   specific evtId. And then get the event trigger string form
 *       DOM.getEventTrigger(evtId,bits);
 *   and add them to the HTML element code.
 *
 *   No matter the element exist or net, you can use
 *       DOM.addPreEventListener(evtId,type,listener);
 *   or
 *       DOM.removePreEventListener(evtId,type,listener);
 *   to add or remove the event listener to the event sink for
 *   specific evtId. And then use
 *       DOM.sinkPreEvent(elemId,evtId,bits);
 *   to sink the events for the element.
 *   After element initialized, you should use
 *       DOM.attachSunkEventListeners();

 */
var DOM={
	browser : {
		isIE : false,
		isMoz : false,
		isOpera : false,
		isSafari : false
		},
	_elems:{},
	_eventId:1,
	_sinks:{},
	//_preSinks:[],
	/*
	_eventTypeMap:{
		"click":"CLICK",
		"dblclick":"DBLCLICK",
		"mousedown":"MOUSEEVENT",
		"mouseup":"MOUSEEVENT",
		"mouseover":"MOUSEEVENT",
		"mouseout":"MOUSEEVENT",
		"mousemove":"MOUSEEVENT",
		"keydown":"KEYEVENT",
		"keypress":"KEYEVENT",
		"keyup":"KEYEVENT",
		"change":"CHANGE",
		"focus":"FOCUSEVENT",
		"blur":"FOCUSEVENT",
		"scroll":"SCROLL",
		"load":"LOAD",
		"error":"ERROR"
	},
	_eventTriggerMap:{
		"click":"onClick",
		"dblclick":"onDbClick",
		"mousedown":"_down",
		"mouseup":"_up",
		"mouseover":"_over",
		"mouseout":"_out",
		"mousemove":"_move",
		"keydown":"_down",
		"keypress":"_press",
		"keyup":"_up",
		"change":"onChange",
		"focus":"onFocus",
		"blur":"onLostFocus",
		"scroll":"onScroll",
		"load":"onLoad",
		"error":"onError"
	},*/
	//_addListenerImpl:null,
	//_removeListenerImpl:null,
	_attachListeners:function(elem) {
		if (this!=DOM) throw DOM.unexpectCaller;
		var evtId,bits,_sinks=this._sinks;
		if (!(evtId=elem.__evt)) {
			if (!(evtId=elem.getAttribute("__evt"))) return;
			elem.__evt=evtId;
			//elem.__events=bits;
		}
		if (_sinks[evtId]) {
			elem.__listener=_sinks[evtId];
			if (!this._elems[evtId]) this._elems[evtId]=[elem];
			else this._elems[evtId].push(elem);
			//delete _sinks[evtId];
			evtId=elem=null;return true;
		}
		evtId=elem=null;return false;
	},
	
	/**
	 * Get the event sink by the event id
	 * 
	 * @param {int} eventId the event id
	 * @returns {Object} the event sink or null if not exist.
	 */
	getEventSink : function(eventId) {
		return this._sinks[eventId] || null;
	},
	
	//-- The dispatch mechanism has been change.
	_getListeners : function(elem) {
		var evtId = this._getEventId(elem);
		if (evtId>0) return this._sinks[evtId] || null;
		else return null;
	},

	/**
	 * @private
	 * Get the event id from the element, generate a new one if not found.
	 *
	 * @param {Object} elem the element
	 * @returns int
	 */
	_getOrGenerateEventId : function(elem) {
		var evtId = this._getEventId(elem);
		// Generate a new event id if no event id has been assigned to the element or the element is null.
		if (!evtId) {
			evtId = this._eventId++;
			if (elem) {
				// Assign the event id to the element, and mark the element if necessary.
				elem.setAttribute("__evt",evtId);
				elem.__evt = evtId;
				if (!elem.__marked) this._markElement(elem);
			}
		}
		return evtId;
	},

	/**
	 * @private
	 * Get the event id from element
	 *
	 * @param {Object} elem the element
	 * 
	 * @returns int
	 */
	_getEventId : function(elem) {//debugger;
		// Returns 0 if the element is unexpected.
		if (!elem) return 0;
		
		var evtId = elem.__evt;
		
		// If the element does not contain the event id directly, check its attributes.
		if (!evtId) {
			evtId = elem.getAttribute("__evt");
			// Null if no attribute named "__evt" and returns 0.
			if (!isNaN(evtId = parseInt(evtId))) elem.__evt = evtId;
			else return 0;
		}

		// Mark the element if necessary.
		if (!elem.__marked) this._markElement(elem);
		return evtId;
	},

	_dispatchEvent:function(evt) {//debugger;
		if (!evt && !(evt=$win.event)) return;
		var E=this,type=evt.type,listeners;//debugger;
		//-- A weird thing, the next line should have the same effect as the second one, but in IE, it doesn't.
		//-- if (E==$win) E=DOM._getTarget(evt);
		if (E.nodeType!=1) {
			if ((E=DOM._getTarget(evt)).nodeType!=1) return;
		}
		//if ($win===E) E=DOM._getTarget(evt);
		//if (E.nodeType!=1) return; // && !E.__listener
		if (! (listeners=DOM._getListeners(E)) ) return;
		return DOM.impl["_dispatch_"+type](evt,E,listeners);
		/*
		if (!E.__listener && !DOM._attachListeners(E)) return;
		if (listeners=E.__listener[DOM._eventTypeMap[type]]) {
			var a=DOM._eventTriggerMap[type];//"on"+type.substring(0,1).toUpperCase()+type.substring(1);
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i][a]) listeners[i][a](evt,E);
			}
		}*/
	},
	
	/**
	 * @private
	 * Mark the element
	 * 
	 * @param {Object} elem the element
	 */
	_markElement : function(elem) {//debugger;
		var sinks, elems;
		// Find the event sink with the specific event id, create a new one if not found.
		if (!(sinks = this._sinks[elem.__evt])) this._sinks[elem.__evt] = sinks = {};
		// Find the marked element list, create a new one if not found.
		if (!(elems = sinks.elems)) sinks.elems = elems = [];
		// Mark the element.
		elems.push(elem);
		elem.__marked = true;
		sinks = elems = null;
	},
	
	/**
	 * Get the target from the event object
	 * 
	 * @param {Object} evt the event object
	 * @returns {Node} the target
	 */
	_getTarget:null,
	
	/**
	 * Add style name to element
	 *
	 * @param {Node} elem the element
	 * @param {String} name the style (class) name
	 */
	addStyleName : function(elem, name) {
		var classList = elem.className = elem.className.replace(/[ ]{2,}/g,' ');
		classList = classList.length>0?classList.split(' '):[];
		if (classList.include(name)) return;
		classList.push(name);
		elem.className = classList.join(' ');
	},

	/**
	 * Add click listener to the element.
	 *
	 * @param {Object} elem the element
	 * @param {Object} listener the click listener
	 * @returns int the event id
	 */
	addClickListener : function(elem, listener) {
		return DOM._addEventListener(elem, "click", listener);
	},
	
	/**
	 * Add mouse listener to the element.
	 *
	 * @param {Object} elem the element
	 * @param {Object} listener the mouse listener
	 * @returns int the event id
	 */
	addMouseListener : function(elem, listener) {
		var evtId = DOM._addEventListener(elem, "mousedown", listener);
		if (!elem) elem = evtId;
		DOM._addEventListener(elem, "mouseup", listener);
		DOM._addEventListener(elem, "mouseover", listener);
		DOM._addEventListener(elem, "mouseout", listener);
		return DOM._addEventListener(elem, "mousemove", listener);
	},
	
	/**
	 * Add keyboard listener to the element
	 *
	 * @param {Object} elem the element
	 * @param {Object} listener the keyboard listener
	 * @returns int the event id
	 */
	addKeyListener : function(elem, listener) {
		var evtId = DOM._addEventListener(elem, "keydown", listener);
		if (!elem) elem = evtId;
		DOM._addEventListener(elem, "keyup", listener);
		return DOM._addEventListener(elem, "keypress", listener);
	},
	
	/**
	 * Add focus listener to the element
	 *
	 * @param {Object} elem the element
	 * @param {Object} listener the focus listener
	 * @returns int the event id
	 */
	addFocusListener : function(elem, listener) {
		var evtId = DOM._addEventListener(elem, "focus", listener);
		if (!elem) elem = evtId;
		return DOM._addEventListener(elem, "blur", listener);
	},

	addChangeListener : function(elem, listener) {
		return DOM._addEventListener(elem, "change", listener);
	},

	addScrollListener : function(elem, listener) {
		return DOM._addEventListener(elem, "scroll", listener);
	},

	addLoadListener : function(elem, listener) {
		return DOM._addEventListener(elem, "load", listener);
	},

	addErrorListener : function(elem, listener) {
		return DOM._addEventListener(elem, "error", listener);
	},

	/**
	 * @private
	 * Add listener to the event sink.
	 *
	 * @param {Object} elem the element
	 * @param {String} type the event type
	 * @param {Object} listener the listener
	 *
	 * @returns int the event id
	 */
	_addEventListener : function(elem, type, listener) {
		// Check this object
		if (this!=DOM) throw DOM.unexpectCaller;
		// Check listener
		if (!type || !listener) return 0;
		
		// process event type string
		type = type.toLowerCase();
		var TYPE = type.toUpperCase();
		var bit = Event[TYPE];
		if (!bit) return 0;
		
		var evtId;
		// Get the event id
		switch(typeof elem) {
			case "number" :
				evtId = elem;
				elem = null;
				break;
			case "object" :
				/*if (elem) evtId = this._getEventId(elem);
				else evtId = this._eventId++;*/
				evtId = this._getOrGenerateEventId(elem);
				break;
			default :
				return 0;
		}
		
		var _sinks = this._sinks, _sink, _listeners;
		// Find the event sink with the specific id, create a new one if not found.
		if (!(_sink = _sinks[evtId])) _sinks[evtId] = _sink = {};
		// Make sure the bits property exist.
		_sink.bits = _sink.bits || 0;
		// Add event bit
		_sink.bits |= bit;
		
		// Find event listeners with the specific event type, create a new one if not found.
		if (!(_listeners = _sink[TYPE])) _sink[TYPE] = _listeners = [];
		_listeners.push(listener);
		
		// Bind the event trigger.
		if (elem) elem["on"+type] = this._dispatchEvent;
		return evtId;
	},

	/**
	 * Add event listener.
	 * @param {Object} elem the element
	 * @param {String} type the event types
	 * @param {Object} listener the event listener
	 */
	addEventListener:function(elem,type,listener) {
		if (this!=DOM) throw DOM.unexpectCaller;
		if (!type || !listener) return null;
		var evtId, bit;
		if (elem && typeof elem=="object") evtId=this._getEventId(elem);
		else evtId = elem;
		if (!evtId) evtId = this._eventId++;
		var types = type.replace(/\s/g,'').toUpperCase()
			.replace('MOUSEEVENT','MOUSEDOWN,MOUSEUP,MOUSEOVER,MOUSEOUT,MOUSEMOVE')
			.replace('KEYEVENT','KEYDOWN,KEYUP,KEYPRESS')
			.replace('FOCUSEVENT','FOCUS,BLUR')
			.split(',');
		var _sinks=this._sinks, _a ,_b;
		if (!(_a=_sinks[evtId])) _sinks[evtId]=_a={};
		_a.bits=_a.bits || 0;
		for (var i=0,j=types.length; i<j; i++) {
			if (!(bit=Event[type=types[i]])) return;
			if (!(_b=_a[type])) _a[type]=_b=[];
			_b.push(listener);
			_a.bits|=bit;
		}
		_a=_b=null;
		return evtId;
		/*
		var bit;
		if (!elem || !(bit=Event[type=type.toUpperCase()]) || !listener) return;
		var _listeners,_listener,_evt;
		if (!(_listeners=elem.__listener)) elem.__listener=_listeners={};
		if (!(_listener=_listeners[type])) _listener=_listeners[type]=[];
		_listener.push(listener);
		_listeners.bits=_listeners.bits || 0;
		_listeners.bits|=bit;
		if (!(_evt=elem.__evt)) _evt=elem.__evt=this._eventId++;
		this._sinks[_evt]=_listeners;
		elem=_listeners=_listener=null;
		return _evt;*/
	},
	/**
	 * Add listener to the specific event sink.
	 * @deprecated
	 *
	 * @param {int} evtId the event listeners locator
	 * @param {String} type the event types want to add
	 * @param {Object} listener the listener
	 */
	addPreEventListener:function(evtId,type,listener) {
		var bit;
		if (this!=DOM) throw DOM.unexpectCaller;
		var types = type.split(',');
		if (!evtId) evtId=this._eventId++;
		var _sinks=this._sinks;
		var _a,_b;
		if (!(_a=_sinks[evtId])) _sinks[evtId]=_a={};
		_a.bits=_a.bits || 0;
		for (var i=0,j=types.length; i<j; i++) {
			if (!(bit=Event[type=types[i].trim().toUpperCase()])) return;
			if (!(_b=_a[type])) _a[type]=_b=[];
			_b.push(listener);
			_a.bits|=bit;
		}
		_a=_b=null;
		/*
		if (!(bit=Event[type=type.toUpperCase()])) return;
		if (!evtId) evtId=this._eventId++;
		var _sinks=this._sinks;
		var _a,_b;
		if (!(_a=_sinks[evtId])) _sinks[evtId]=_a={};
		if (!(_b=_a[type])) _a[type]=_b=[];
		_b.push(listener);
		_a.bits=_a.bits || 0;
		_a.bits|=bit;
		_a=_b=null;*/
		return evtId;
	},
	/**
	 * @deprecated
	 */
	attachSunkEventListeners:function() {
		if (this!=DOM) throw DOM.unexpectCaller;
		var _preSinks=this._preSinks,_sinks=this._sinks,item;
		for (var i=0,j=_preSinks.length; i<j; i++) {
			item=_preSinks[i];//$doc.getElementById(item[0]);
			this.sinkEvent($doc.getElementById(item[0]),item[2],item[1]);
		}
		item=null;_preSinks=[];
	},

	/**
	 * Append node
	 *
	 * @param {Node} refNode the parent node
	 * @param {Node} node the child node
	 */
	append : function(refNode, node) {
		return refNode.appendChild(node);
	},

	clear:function() {
		if (this!=DOM) throw DOM.unexpectCaller;
		var _elems=this._elems,elem;
		for (var i in _elems) {
			if (elem=_elems[i]) {
				for (var j=0;j<elem.length;j++) {
					this.removeSunkEvent(elem[j]);
				}
				//this.removeSunkEvent(elem);
			}
			delete _elems[i];
		}
		_elems=elem=null;
	},

	/**
	 * Clean up the listeners and unbind the event dispatcher.
	 */
	cleanUp : function() {
		if (this!=DOM) throw DOM.unexpectCaller;
		var _sinks = this._sinks, _sink, elems;
		for (var i in _sinks) {
			_sink = _sinks[i];
			bits = _sink.bits;
			if (elems = _sink.elems) {
				for (var j=0; j<elems.length; j++) {
					if (elems[j]) {
						this._sinkEvent(elems[j], bits, null);
					}
					//this.removeSunkEvent(elems[j]);
					elems[j] = null;
				}
			}
			delete _sinks[i].elems;
			for (var j in _sink) {
				if (typeof _sink[j]=="object" && _sink[j].length>0) {
					var listeners = _sink[j];
					for (var m=0,n=listeners.length; m<n; m++) listeners[m]=null;
				}
				delete _sink[j];
			}
			delete _sinks[i];
		}
		_sinks = elems = null;
	},

	/**
	 * Remove all childNodes from the node
	 * 
	 * @param {Node} node the parent node
	 */
	detachChilds:function(node) {
		while(node.firstChild) {
			node.removeChild(node.firstChild);
		}
		node=null;
	},

	/**
	 * Remove the node from its parent node
	 *
	 * @param {Node} node the node want to be removed
	 */
	detachNode:function(node) {
		if (node.parentNode) return node.parentNode.removeChild(node);
	},
	
	/**
	 * Fire a blur event on element
	 *
	 * @param {Node} elem the element
	 */
	fireBlur : function(elem) {
		return elem.blur();
		//this._fireEvent(elem, this._createHTMLEvent("blur"));
	},
	
	/**
	 * Fire a click event on element
	 *
	 * @param {Node} elem the element
	 */
	fireClick : function(elem) {
		this._fireEvent(elem, this._createMouseEvent("click"));
	},
	
	/**
	 * Fire a focus event on element
	 *
	 * @param {Node} elem the element
	 */
	fireFocus : function(elem) {
		return elem.focus();
		//this._fireEvent(elem, this._createHTMLEvent("focus"));
	},
	
	/**
	 * Fire a key down event on element
	 *
	 * @param {Node} elem the element
	 */
	fireKeyDown : function(elem, keyCode, charCode) {
		this._fireEvent(elem, this._createKeyEvent("keydown", keyCode, charCode));
	},
	
	/**
	 * Fire a key press event on element
	 *
	 * @param {Node} elem the element
	 */
	fireKeyPress : function(elem, keyCode, charCode) {
		this._fireEvent(elem, this._createKeyEvent("keypress", keyCode, charCode));
	},
	
	/**
	 * Fire a key up event on element
	 *
	 * @param {Node} elem the element
	 */
	fireKeyUp : function(elem, keyCode, charCode) {
		this._fireEvent(elem, this._createKeyEvent("keyup", keyCode, charCode));
	},
	
	/**
	 * Fire a mouse down event on element
	 *
	 * @param {Node} elem the element
	 */
	fireMouseDown : function(elem) {
		this._fireEvent(elem, this._createMouseEvent("mousedown"));
	},
	
	/**
	 * Fire a mouse move event on element
	 *
	 * @param {Node} elem the element
	 */
	fireMouseMove : function(elem) {
		this._fireEvent(elem, this._createMouseEvent("mousemove"));
	},
	
	/**
	 * Fire a mouse out event on element
	 *
	 * @param {Node} elem the element
	 */
	fireMouseOut : function(elem) {
		this._fireEvent(elem, this._createMouseEvent("mouseout"));
	},
	
	/**
	 * Fire a mouse over event on element
	 *
	 * @param {Node} elem the element
	 */
	fireMouseOver : function(elem) {
		this._fireEvent(elem, this._createMouseEvent("mouseover"));
	},
	
	/**
	 * Fire a mouse up event on element
	 *
	 * @param {Node} elem the element
	 */
	fireMouseUp : function(elem) {
		this._fireEvent(elem, this._createMouseEvent("mouseup"));
	},
	
	/**
	 * @param bits
	 * The available value of bits are defined in Event object.
	 * If you want to sink the click and keyup event, the bits value
	 * should be
	 *     Event.ONCLICK & Event.ONKEYUP
	 */
	getEventTrigger:function(evtId,bits) {
		if (!evtId || !bits) return;
		var a=[],s="=_$f(event) ";
		if (bits & 0x000040) a.push("onclick");
		if (bits & 0x0000BE) {
			if (bits & 0x000001) a.push("onmousedown");
			if (bits & 0x000002) a.push("onmouseup");
			if (bits & 0x000004) a.push("onmouseover");
			if (bits & 0x000008) a.push("onmouseout");
			if (bits & 0x000010) a.push("onmousemove");
			if (bits & 0x000080) a.push("ondblclick");
		}
		if (bits & 0x000700) {
			if (bits & 0x000100) a.push("onkeydown");
			if (bits & 0x000200) a.push("onkeyup");
			if (bits & 0x000400) a.push("onkeypress");
		}
		if (bits & 0x8CB000) {
			if (bits & 0x001000) a.push("onfocus");
			if (bits & 0x002000) a.push("onblur");
			if (bits & 0x008000) a.push("onchange");
			//if (bits & 0x02000) a.push("onlosecapture");
			if (bits & 0x040000) a.push("onscroll");
			if (bits & 0x080000) a.push("onload");
			if (bits & 0x800000) a.push("onerror");
		}
		if (a.length>0) {
			return a.join(s)+s+'__evt="'+evtId+'" __events="'+bits+'" ';
		}
		return '';
	},
	/**
	 * Get the position of the element relative to document body.
	 */
	getPosition : function(elem) {
		var e = elem, E = e;
		var x = e.offsetLeft;
		var y = e.offsetTop;
		//debugger;
		while (e = e.offsetParent) {
			var P = e.parentNode;
			while (P != (E = E.parentNode)) {
				x -= E.scrollLeft;
				y -= E.scrollTop;
			}
			x += e.offsetLeft;
			y += e.offsetTop;
			E = e;
		}
		e=E=P=elem=null;
		return {"x":x, "y":y};
		//alert("top="+y+"nleft="+x);
	},
	
	/**
	 * Initialize DOM object.
	 * It will detect the browser type and assign the corresponding methods to DOM object.
	 */
	init:function() {
		var _impl;
		if ($doc.addEventListener) {
			_impl=this.impl.moz;
			this.browser.isMoz=true;

			var ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('opera')!= -1) {
				this.browser.isOpera = true;
				Object.extend(_impl, this.impl.opera);
			} else if(ua.indexOf('webkit')!= -1) {
				this.browser.isSafari = true;
				Object.extend(_impl, this.impl.safari);
			}
		} else if ($doc.attachEvent) {
			_impl=this.impl.ie;
			this.browser.isIE=true;
		}
		Object.extend(this, _impl);
		// add shortcut
		Object.extend(this, this.browser);
		//this._getTarget=_impl._getTarget;
		_impl=null;
	},

	/**
	 * Insert a node after the specific node
	 *
	 * @param {Node} node the node to be inserted
	 * @param {Node} referenceNode the reference node
	 * @returns {Node}
	 */
	insertAfter:function(node, referenceNode) {
		return referenceNode.parentNode.insertBefore(node, referenceNode.nextSibling);
	},

	prependChild:function(parent, node) {
		return parent.insertBefore(node, parent.firstChild);
	},
	removeAllEventListeners:function(elem) {
		delete elem.__listener;
		DOM.removeSunkEvent(elem);
	},
	removeAllPreEventListeners:function(evtId) {
		var _sinks=DOM._sinks;
		delete _sinks[evtId];
	},

	/**
	 * Remove a style name from element
	 *
	 * @param {Node} elem the element
	 * @param {String} name the style (class) name
	 */
	removeStyleName : function(elem, name) {
		var classList = elem.className.replace(/[ ]{2,}/g,' ');
		classList = classList.split(' ');
		classList.remove(name);
		elem.className = classList.join(' ');
	},

	removeSunkEvent:function(elem) {
		if (this!=DOM) throw DOM.unexpectCaller;
		var bits;
		//if (!(bits=elem.__events)) return;
		if (!(bits=this._sinks[elem.__evt].bits)) return;
		this._sinkEvent(elem,bits,null);
		this._elems[elem.__evt].remove(elem);
		//elem=elem.__listener=elem.__events=null;
		elem=elem.__listener=null;
	},
	removeEventListener:function(elem,type,listener) {
		if (!elem || !type || !listener) return;
		try {
			elem.__listener[type].remove(listener);
		} catch(e) {}
	},
	removePreEventListener:function(evtId,type,listener) {
		try {
			this._sinks[evtId][type].remove(listener);
		} catch(e) {}
	},
	sinkPreEvent:function(elemId,evtId,bits) {
		this._preSinks.push([elemId,evtId,bits]);
	},
	sinkEvent:function(elem,evtId) {
		if (this!=DOM) throw DOM.unexpectCaller;
		//elem.__events=bits;
		var bits=this._sinks[evtId].bits;
		this._sinkEvent(elem,bits,this._dispatchEvent);
		if (!evtId) evtId=this._eventId++;
		if (!this._elems[evtId]) this._elems[evtId]=[elem];
		else this._elems[evtId].push(elem);
		elem.__evt=evtId;
		elem=null;
	},
	_sinkEvent:function(elem,bits,handler) {
		if (bits & 0x000040) elem.onclick=handler;
		if (bits & 0x0000BE) {
			if (bits & 0x000001) elem.onmousedown=handler;
			if (bits & 0x000002) elem.onmouseup=handler;
			if (bits & 0x000004) elem.onmouseover=handler;
			if (bits & 0x000008) elem.onmouseout=handler;
			if (bits & 0x000010) elem.onmousemove=handler;
			if (bits & 0x000080) elem.ondblclick=handler;
		}
		if (bits & 0x000700) {
			if (bits & 0x000100) elem.onkeydown=handler;
			if (bits & 0x000200) elem.onkeyup=handler;
			if (bits & 0x000400) elem.onkeypress=handler;
		}
		if (bits & 0x8CB000) {
			if (bits & 0x001000) elem.onfocus=handler;
			if (bits & 0x002000) elem.onblur=handler;
			if (bits & 0x008000) elem.onchange=handler;
			//if (bits & 0x02000) elem.onlosecapture=handler;
			if (bits & 0x040000) elem.onscroll=handler;
			if (bits & 0x080000) elem.onload=handler;
			if (bits & 0x800000) elem.onerror=handler;
		}
		elem=null;
	},
	unexpectCaller:(new Error("This method can only be called by DOM object.")),
	impl:{
		moz:{
			_getTarget:function(evt) {
				return evt.target;
			},
			
			_fireEvent : function(elem, evtObj) {
				elem.dispatchEvent(evtObj);
			},
			
			_createHTMLEvent : function(type) {
				var evt = $doc.createEvent("HTMLEvents");
				evt.initEvent(type, true, true);
				return evt;
			},

			_createKeyEvent : function(type, keyCode, charCode) {
				var evt = $doc.createEvent("KeyboardEvent");
				evt.initKeyEvent(type, true, true, $win, false, false,false, false, keyCode, charCode);
				return evt;
			},
			
			_createMouseEvent : function(type) {
				var evt = $doc.createEvent("MouseEvents");
				evt.initMouseEvent(type, true, true, $win, 1, 0, 0, 0, 0, false, false,false, false, 0, null);
				return evt;
			}
		},

		ie:{
			_getTarget:function(evt) {
				return evt.srcElement;
			},

			_fireEvent : function(elem, evtObj) {
				elem.fireEvent('on'+evtObj.type, evtObj);
			},
			
			_createKeyEvent : function(type, _keyCode, _charCode) {
				var evt = $doc.createEventObject();
				evt.type = type;
				evt.ctrlKey = false;
				evt.altKey = false;
				evt.shiftKey = false;
				evt.metaKey = false;
				evt.keyCode = _keyCode;
				evt.charCode = _charCode;
				return evt;
			},

			_createMouseEvent : function(type) {
				var evt = $doc.createEventObject();
				evt.type = type;
				evt.detail = 1;
				evt.screenX = 0;
				evt.screenY = 0;
				evt.clientX = 0;
				evt.clientY = 0;
				evt.ctrlKey = false;
				evt.altKey = false;
				evt.shiftKey = false;
				evt.metaKey = false;
				evt.Button = 0;
				evt.relatedTarget = null;
				return evt;
			}
		},

		opera : {
			_createKeyEvent : function(type, _keyCode, _charCode) {
				var evt = $doc.createEvent("UIEvents");
				evt.initUIEvent(type, true, true, $win, 1);
				evt.keyCode = _keyCode;
				evt.charCode = _charCode;
				return evt;
			}
		},

		safari : {
			_createKeyEvent : function(type, _keyCode, _charCode) {
				var evt = $doc.createEvent("UIEvents");
				evt.initUIEvent(type, true, true, $win, 1);
				evt.keyCode = _keyCode;
				evt.charCode = _charCode;
				return evt;
			}
		},

		_dispatch_click : function(evt, elem, listeners) {
			if (! (listeners = listeners["CLICK"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onClick) listeners[i].onClick(evt, elem);
			}
		},
		_dispatch_dblclick : function(evt, elem, listeners) {
			if (! (listeners = listeners["DBLCLICK"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onDblClick) listeners[i].onDblClick(evt, elem);
			}
		},
		_dispatch_mousedown : function(evt, elem, listeners) {
			if (! (listeners = listeners["MOUSEDOWN"]) ) return;
			var _c = Event.getPageCoordinates(evt);
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onMouseDown) listeners[i].onMouseDown(evt, elem, _c.x, _c.y);
			}
		},
		_dispatch_mouseup : function(evt, elem, listeners) {
			if (! (listeners = listeners["MOUSEUP"]) ) return;
			var _c = Event.getPageCoordinates(evt);
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onMouseUp) listeners[i].onMouseUp(evt, elem, _c.x, _c.y);
			}
		},
		_dispatch_mousemove : function(evt, elem, listeners) {
			if (! (listeners = listeners["MOUSEMOVE"]) ) return;
			var _c = Event.getPageCoordinates(evt);
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onMouseMove) listeners[i].onMouseMove(evt, elem, _c.x, _c.y);
			}
		},
		_dispatch_mouseover : function(evt, elem, listeners) {
			if (! (listeners = listeners["MOUSEOVER"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onMouseOver) listeners[i].onMouseOver(evt, elem);
			}
		},
		_dispatch_mouseout : function(evt, elem, listeners) {
			if (! (listeners = listeners["MOUSEOUT"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onMouseOut) listeners[i].onMouseOut(evt, elem);
			}
		},
		_dispatch_keydown : function(evt, elem, listeners) {
			if (! (listeners = listeners["KEYDOWN"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onKeyDown) 
					listeners[i].onKeyDown(evt, elem, Event.getKeyCode(evt), Event.getModifiers(evt));
			}
		},
		_dispatch_keypress : function(evt, elem, listeners) {
			if (! (listeners = listeners["KEYPRESS"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onKeyPress) 
					listeners[i].onKeyPress(evt, elem, Event.getCharCode(evt), Event.getModifiers(evt));
			}
		},
		_dispatch_keyup : function(evt, elem, listeners) {
			if (! (listeners = listeners["KEYUP"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onKeyUp) 
					listeners[i].onKeyUp(evt, elem, Event.getKeyCode(evt), Event.getModifiers(evt));
			}
		},
		_dispatch_change : function(evt, elem, listeners) {
			if (! (listeners = listeners["CHANGE"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onChange) listeners[i].onChange(evt, elem);
			}
		},
		_dispatch_focus : function(evt, elem, listeners) {
			if (! (listeners = listeners["FOCUS"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onFocus) listeners[i].onFocus(evt, elem);
			}
		},
		_dispatch_blur : function(evt, elem, listeners) {
			if (! (listeners = listeners["BLUR"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onLostFocus) listeners[i].onLostFocus(evt, elem);
			}
		},
		_dispatch_scroll : function(evt, elem, listeners) {
			if (! (listeners = listeners["SCROLL"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onScroll) listeners[i].onScroll(evt, elem);
			}
		},
		_dispatch_load : function(evt, elem, listeners) {
			if (! (listeners = listeners["LOAD"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onLoad) listeners[i].onLoad(evt, elem);
			}
		},
		_dispatch_error : function(evt, elem, listeners) {
			if (! (listeners = listeners["ERROR"]) ) return;
			for (var i=0,j=listeners.length; i<j; i++) {
				if (listeners[i].onError) listeners[i].onError(evt, elem);
			}
		}
	}
};
DOM.init();﻿/**
 * Element is a base class.
 * @class Element
 *
 * @author Leegorous
 */
Class.create("Element" ,null ,null ,{
	$tatic : {
		/**
		 * Create an element.
		 *
		 * @param {String} elem
		 * @param {<Opt>Object} attrs The attributes of the element
		 * 
		 * @returns {HTMLElement}
		 */
		create : function(elem, attrs) {
			if (typeof elem!="string" || elem.length==0) 
				throw new Error("Element name is required.");
			var dom = $doc.createElement(elem);
			this._setAttributes(dom, attrs);
			return dom;
		},
		
		_setAttributes : function(elem, attrs) {
			if (!attrs) return;
			var styles, text;
			if (styles = attrs.style) delete attrs.style;
			if (text = attrs.innerText) {
				elem.appendChild($doc.createTextNode(text));
				delete attrs.innerText;
			}

			for (var i in attrs) {
				switch(i.toLowerCase()) {
					case "id" : 
						elem.id = attrs[i]; break;
					case "class" : 
					case "classname" :
						elem.className = attrs[i]; break;
					default : 
						elem.setAttribute(i, attrs[i]);
				}
			}

			if (styles) {
				for (var i in styles) elem.style[i] = styles[i];
			}
		}
	},
	/**
	 * @constructor 
	 * @param {HTMLElement/String} elem
	 * @param {<Opt>Object} attrs The attributes of the element
	 */
	init : function(elem, attrs) {
		//this._initElementImpl(elem, attrs);
		if (!elem) throw new Error("Element information or Node is required.");
		if (elem.nodeType>0) this._e = elem;
		else this._e = Element.create(elem, attrs);
	},

	appendChild : function(elem) {
	},

	getAttribute : function(name) {
		return this._e.getAttribute(name);
	},

	getClass : function() {
		return this._e.className;
	},

	getId : function() {
		return this._e.id;
	},

	getDom : function() {
		return this._e;
	}
	
	/**
	 * @deprecated
	_initElementImpl : function (elem, attrs) {
		if (!elem) throw new Error("Element information or Node is required.");
		if (elem.nodeType>0) this._e = elem;
		else this._e = Element.create(elem, attrs);
	}
	 */
});

var Element = Class.get("Element");﻿/**
 * Event object
 */
var Event;
if (!Event) Event={};
Object.extend(Event,{
	MOUSEDOWN : 0x000001,
	MOUSEUP   : 0x000002,
	MOUSEOVER : 0x000004,
	MOUSEOUT  : 0x000008,
	MOUSEMOVE : 0x000010,
	MOUSEDRAG : 0x000020,
	CLICK     : 0x000040,
	DBLCLICK  : 0x000080,
	KEYDOWN   : 0x000100,
	KEYUP     : 0x000200,
	KEYPRESS  : 0x000400,
	DRAGDROP  : 0x000800,
	FOCUS     : 0x001000,
	BLUR      : 0x002000,
	SELECT    : 0x004000,
	CHANGE    : 0x008000,
	RESET     : 0x010000,
	SUBMIT    : 0x020000,
	SCROLL    : 0x040000,
	LOAD      : 0x080000,
	UNLOAD    : 0x100000,
	ABORT     : 0x400000,
	ERROR     : 0x800000,
	RESIZE   : 0x4000000,
	
	ALT_MASK : 1,
	CONTROL_MASK : 2,
	SHIFT_MASK : 4,
	META_MASK : 8
});

Object.extend(Event,{
	MOUSEEVENT : Event.MOUSEDOWN|Event.MOUSEUP|Event.MOUSEOVER|Event.MOUSEOUT|Event.MOUSEMOVE,
	FOCUSEVENT : Event.FOCUS|Event.BLUR,
	KEYEVENT : Event.KEYDOWN|Event.KEYPRESS|Event.KEYUP,
	/**
	 * Get the keyCode from the event object.
	 */
	getKeyCode : function(event) {
		return event.keyCode;
	},
	getModifiers : function(event) {
		return (event.shiftKey?1:0) | (event.ctrlKey?2:0) | (event.altKey?4:0);
	},
	isRightClick : function(event) {
		return event.button==2;
	}
});

if (DOM.browser.isMoz) {
	Object.extend(Event, {
		getCharCode : function(event) {
			return event.charCode;
		},
		getPageCoordinates : function(event) {
			return {x:event.pageX, y: event.pageY};
		},
		isLeftClick : function(event) {
			return event.button==0;
		},
		isWheelClick : function(event) {
			return event.button==1;
		}
	});
} else if (DOM.browser.isIE) {
	Object.extend(Event, {
		getCharCode : function(event) {
			return event.keyCode;
		},
		getPageCoordinates : function(event) {
			return {
				x:event.clientX + $doc.body.scrollLeft - $doc.body.clientLeft,
				y:event.clientY + $doc.body.scrollTop  - $doc.body.clientTop
			};
		},
		isLeftClick : function(event) {
			return event.button==1;
		},
		isWheelClick : function(event) {
			return event.button==4;
		}
	});
}﻿/**
 * @class EventManager
 *
 * @author Leegorous
 */
Class.create("EventManager",null,null,{
	$tatic:{
		impl:{
			moz:{
				/**
				 * Prevent the default action of the event
				 * @param {Object} event the event object
				 */
				preventDefault:function(event) {
					event.preventDefault();
				}
			},
			ie:{
				preventDefault:function(event) {
					event.returnValue=false;
				}
			}
		}
	}
});

var EvtMgr=Class.get("EventManager");﻿/**
 * @import DOM;
 *
 * Window object
 * Usage:
 *   With this Window object (not window object), you can
 *   add onLoad listener with Window.addLoadListener(listener);, and
 *   add onClose listener with Window.addCloseListener(listener);, and
 *   add onResize listener with Window.addResizeListener(listener);,
 *   and get the visual size of browser with
 *       Window.innerWidth and Window.innerHeight
 */
var Window={
	__listener:{close:[],load:[],resize:[]},
	loaded:false,
	addCloseListener:function(listener) {
		this.__listener.close.push(listener);
	},
	addLoadListener:function(listener) {
		this.__listener.load.push(listener);
	},
	addResizeListener:function(listener) {
		this.__listener.resize.push(listener);
	},
	clear:function() {
		$win.onload=null;
		$win.onresize=null;
		$win.onunload=null;
	},
	getPageXOffset:null,
	getPageYOffset:null,
	init:function() {
		this._resizeTimeoutId=null;
		//$win.onload=this.onLoad;
		window.onload=this.onLoad;
		$win.onunload=this.onClose;
		var _impl,_dm;
		if (DOM.browser.isIE) {
			_impl=this._impl.ie;
			$win.onresize=_impl.onResize;
		} else {
			_impl=this._impl.moz;
			$win.onresize=this.onResize;
		}
		this._initDimension=_impl._initDimension;
		this.getPageXOffset=_impl.getPageXOffset;
		this.getPageYOffset=_impl.getPageYOffset;
		_dm=$doc.domain;
		if (DEFAULT_DOMAIN) {
			if (_dm.indexOf(DEFAULT_DOMAIN)>-1) _dm = DEFAULT_DOMAIN;
			else _dm=null;
		}
		if (_dm.indexOf('.')==-1) _dm=null;
		this.CURRENT_DOMAIN=_dm;
		this.addLoadListener({onLoad:function() {Window._initDimension();}});
		this.addResizeListener({onResize:function() {Window._initDimension();}});
		_impl=_dm=null;
	},
	onClose:function() {
		var listeners=Window.__listener.close;
		for (var i=0; i<listeners.length; i++) {
			listeners[i].onClose();
		}
	},
	onLoad:function() {
		Window.loaded=true;
		var listeners=Window.__listener.load;
		for (var i=0; i<listeners.length; i++) {
			listeners[i].onLoad();
		}
	},
	onResize:function() {
		var listeners=Window.__listener.resize;
		for (var i=0; i<listeners.length; i++) {
			listeners[i].onResize();
		}
		Window._resizeTimeoutId=null;
	},
	removeCloseListener:function(listener) {
		this.__listener.close.remove(listener);
	},
	removeLoadListener:function(listener) {
		this.__listener.load.remove(listener);
	},
	removeResizeListener:function(listener) {
		this.__listener.resize.remove(listener);
	},
	_impl:{
		ie:{
			_initDimension:function() {
				this.innerWidth=$doc.documentElement.clientWidth;
				this.innerHeight=$doc.documentElement.clientHeight;
			},
			getPageXOffset:function() {return $doc.documentElement.scrollLeft;},
			getPageYOffset:function() {return $doc.documentElement.scrollTop;},
			/*
			 * I don't know why IE will fire resize event more than it should be.
			 * In some situation, it will cause IE crash.
			 * I just set the timeout to fire the resize event to avoid the redundancy.
			 */
			onResize:function() {
				if (Window._resizeTimeoutId) return;
				Window._resizeTimeoutId=setTimeout(Window.onResize,50);
			}
		},
		moz:{
			_initDimension:function() {
				this.innerWidth=$win.innerWidth;
				this.innerHeight=$win.innerHeight;
			},
			getPageXOffset:function() {return $doc.body.scrollLeft;},
			getPageYOffset:function() {return $doc.body.scrollTop;}
		}
	}
}
Window.init();
Window.addLoadListener({
	onLoad:function() {
		$win._$f=_$f=DOM._dispatchEvent;
	}
});
Window.addCloseListener({
	onClose:function() {
		DOM.clear();
		DOM.cleanUp();
		Window.clear();
	}
});﻿/**
 * Cookies object
 */
var Cookies={
	_cks:{},
	create:function(key,value,minutes,path,domain,secure) {
		var __dm,expires;
		if (path) path = '; path=' + (typeof path=="string" ? path : '/');
		else path='';
		if (domain) domain = (typeof (__dm=domain)=="string" || ((__dm=Window.CURRENT_DOMAIN)!=null)) ? '; domain=' + __dm : '';
		else domain='';
		if (secure) secure = (secure==true) ? '; secure' : '';
		else secure='';
		if (minutes) {
			var date = new Date();
			date.setTime(date.getTime()+(minutes*60*1000));
			expires = '; expires='+date.toGMTString();
		} else expires='';
		$doc.cookie = key+'='+value+expires+path+domain+secure;
		_cks[key] = value;
	},
	erase:function(key) {
		this.create(key,'',-1);
		var _cks=this._cks;
		delete _cks[key];
	},
	get:function(key) {
		return this._cks[key];
	},
	init:function() {
		var _cs=$doc.cookie.split('; '),_ckv;
		for (var i=_cs.length-1;i>=0;i--) {
			_ckv = _cs[i].split('=');
			this._cks[_ckv[0]]=_ckv[1];
		}
		_ckv=null;
	}
};﻿/**
 * Format object
 */
var Format={
	CompactDate:1,
	StandardDate:2,
	CompactTime:3,
	StandardTime:4,
	GMTDate:5,
	_getStandard:function(a,b,c,d) {
		return (a<10?'0':'')+a+d+(b<10?'0':'')+b+d+(c<10?'0':'')+c;
	},
	_defaultDateDescription:{
		Week:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],
		week:["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],
		Month:["January","February","March","April","May","June","July","August","September","October","November","December"],
		month:["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],
		AMPM:{"AM":"AM","PM":"PM"},
		ampm:{"AM":"am","PM":"pm"}
	},
	/**
	 * @param date (Date) the date object want to format
	 * @param format (String) the format template, available notations list below.
	 *   YY : full year, 4 digit.                    yy : short year, 2 digit.
	 *   MM : numeric month with 0, 2 digit.         mm : numeric month.
	 *   M  : month name. (*)                        m  : short month name. (*)
	 *   DD : numeric day with 0, 2 digit.           dd : numeric day.
	 *   W  : weekday name. (*)                      w  : short weekday name. (*)
	 *   A  : capitalization AM/PM. (*)              a  : lower case am/pm. (*)
	 *   HH : 24 hour format with 0, 2 digit.        hh : 24 hour format.
	 *   H  : 12 hour format with 0, 2 digit.        h  : 12 hour format.
	 *   II : numeric minute with 0, 2 digit.        ii : numeric minute.
	 *   SS : numeric second with 0, 2 digit.        ss : numeric second.
	 *   Z  : full timezone offset.                  z  : short timezone offset.
	 * @param langPack (Object) the language pack object
	 *   To know about the pattern of the language pack object,
	 *   please Format._defaultDateDescription
	 */
	formatDate:function(date,format,langPack) {
		if (!date || date.constructor!=Date) return '';
		var _Y = date.getFullYear();
		var _M = date.getMonth()+1;
		var _D = date.getDate();
		var _W = date.getDay();
		var _H = date.getHours();
		var _H12 = _H>12?_H-12:_H;
		var _AP=_H>12?'AM':'PM';
		var _I = date.getMinutes();
		var _S = date.getSeconds();
		var _Z = date.getTimezoneOffset();
		if (!format) return '';
		switch(format) {
			case 1:return _Y+'-'+_M+'-'+_D+' '+_H+':'+_I+':'+_S;
			case 2:return this._getStandard(_Y,_M,_D,'-')+':'+this._getStandard(_H,_I,_S,':');
			case 3:return _H+':'+_I+':'+_S;
			case 4:return this._getStandard(_H,_I,_S,':');
			case 5:return date.toGMTString().slice(0,-3);
			default:
				if (!langPack) langPack=Format._defaultDateDescription;
				var s=format,ms,re=/YY|yy|MM|mm|M|m|DD|dd|W|w|A|a|HH|hh|H|h|II|ii|SS|ss|Z|z/g;
				ms = format.match(re);
				for (var i=0;i<ms.length;i++) {
					switch(ms[i]) {
						case 'YY':
							s=s.replace(/([^\\]|^)YY/g, '$1'+_Y); break;
						case 'yy':
							s=s.replace(/([^\\]|^)yy/g, '$1'+_Y.toString().substr(2,2)); break;
						case 'MM':
							s=s.replace(/([^\\]|^)MM/g,'$1'+(_M<10?'0':'')+_M); break;
						case 'mm':
							s=s.replace(/([^\\]|^)mm/g,'$1'+_M); break;
						case 'M':
							
							s=s.replace(/([^\w]|^)M([^\w]|$)/g,"$1"+langPack.Month[_M-1]+"$2"); break;
						case 'm':
							s=s.replace(/([^\w]|^)m([^\w]|$)/g,"$1"+langPack.month[_M-1]+"$2"); break;
						case 'DD':
							s=s.replace(/([^\\]|^)DD/g,'$1'+(_D<10?'0':'')+_D); break;
						case 'dd':
							s=s.replace(/([^\\]|^)dd/g,'$1'+_D); break;
						case 'W':
							s=s.replace(/([^\w]|^)W([^\w]|$)/g,"$1"+langPack.Week[_W]+"$2"); break;
						case 'w':
							s=s.replace(/([^\w]|^)w([^\w]|$)/g,"$1"+langPack.week[_W]+"$2"); break;
						case 'A':
							s=s.replace(/([^\\\w]|^)A([^\w]|$)/g,'$1'+langPack.AMPM[_AP]+"$2"); break;
						case 'a': 
							s=s.replace(/([^\\\w]|^)a([^\w]|$)/g,'$1'+langPack,ampm[_AP]+"$2"); break;
						case 'HH':
							s=s.replace(/([^\\]|^)HH/g,'$1'+(_H<10?'0':'')+_H); break;
						case 'hh':
							s=s.replace(/([^\\]|^)hh/g,'$1'+_H); break;
						case 'H':
							s=s.replace(/([^\\]|^)H/g,'$1'+(_H12<10?'0':'')+_H12); break;
						case 'h':
							s=s.replace(/([^\\]|^)h/g,'$1'+_H12); break;
						case 'II':
							s=s.replace(/([^\\]|^)II/g,'$1'+(_I<10?'0':'')+_I); break;
						case 'ii':
							s=s.replace(/([^\\]|^)ii/g,'$1'+_I); break;
						case 'SS':
							s=s.replace(/([^\\]|^)SS/g,'$1'+(_S<10?'0':'')+_S); break;
						case 'ss':
							s=s.replace(/([^\\]|^)ss/g,'$1'+_S); break;
						case 'Z':
							var Z;
							s=s.replace(/([^\\]|^)Z/g,'$1'+( _Z==0?'00':( (_Z<0?'+':'-') + ((Z=Math.abs(Math.round(_Z/60)))<10?'0':'') + Z + '00') ) ); break;
						case 'z':
							var Z;
							s=s.replace(/([^\\]|^)z/g,'$1'+( _Z==0?'0000':( (_Z<0?'+':'-') + ((Z=Math.abs(Math.round(_Z/60)))<10?'0':'') + Z) ) ); break;
					}
				}
				return s;
		}
	}
};﻿/**
 * @import Format;
 * @import Window;
 * 
 * Console object
 * Task:
 *   1, putError : for display the error message
 *   2, scroll automatically <finished>
 */
var Console={
	on:false,
	_console:null,
	_board:null,
	init:function() {
		var _console=$doc.createElement("div");
		_console.id="consoleWrapper";
		this._board=$doc.body.appendChild(_console);
		var _a = $doc.createDocumentFragment();
		_a.appendChild($doc.createElement("br"));
		this._MsgItem = _a;
		_a = _a.cloneNode(true);
		var _b = $doc.createElement("span");
		_b.style["color"]="#FF0000";
		_a.insertBefore(_b, _a.firstChild);
		this._ErrMsgItem = _a;
		_console=null;
		this.on=true;
	},
	error : function(e) {
		var _a = this._ErrMsgItem.cloneNode(true);
		if (typeof e!="object") _a.firstChild.appendChild($doc.createTextNode(Format.formatDate(new Date(),Format.StandardTime)+" "+e));
		else {
			_a.firstChild.innerHTML = "Err Name: "+e.name+"<br/>Err Msg: "+e.message;
		}
		this._board.appendChild(_a);
		this._board.scrollTop = this._board.scrollHeight - this._board.clientHeight;
	},
	put:function(str) {
		var _board=this._board;
		var _a = this._MsgItem.cloneNode(true);
		_a.insertBefore($doc.createTextNode(Format.formatDate(new Date(),Format.StandardTime)+" "+str), _a.firstChild);
		_board.appendChild(_a);/*
		_board.appendChild($doc.createTextNode(Format.formatDate(new Date(),Format.StandardTime)+" "+str));
		_board.appendChild($doc.createElement("br"));*/
		_board.scrollTop = _board.scrollHeight - _board.clientHeight;
		_board=null;
	},
	clear:function() {
		delete this._board;
	}
};

Window.addLoadListener({
	onLoad:function() {
		Console.init();
		Console.put(new Date());
	}
});
Window.addCloseListener({
	onClose:function() {
		Console.clear();
	}
});﻿/**
 * @class Entry
 *
 * @author Leegorous
 */
Class.create("Entry", null, null, {
	init:function(name,value) {
		this.name=name || "";
		this.value=value || "";
	}
});

var Entry=Class.get("Entry");﻿/*
 * @import Console;
 * 
 * ErrorStack object
 */
var ErrorStack={
	_stack:[],
	sendMessageToConsole:true,
	push:function(e) {
		this._stack.push(e);
		if (this.sendMessageToConsole && Console.on) {
			Console.error(e);
		}
	}
};﻿/**
 * @class JSONParser
 */
var JSONParser={
	parse:function(str) {
		if (str==null) throw new Error("Null Point Exception");
		if (str=="") throw new Error("Illegal Argument Exception");
		try {
			return eval('('+str+')');
		} catch(e) {
			throw e;
		}
	}
};﻿/**
 * @import Window;
 *
 * @class Timer
 *
 * @author Leegorous
 */
Class.create("Timer",null,null,{
	_run:Abstract.empty,
	isRepeat:false,
	cancel:function(){
		if (this.id==null) return;
		if (this.isRepeat) clearInterval(this.id);
		else clearTimeout(this.id);
		Timer._timers.remove(this);
		this.id=null;
		this.isRunning=false;
	},
	destroy : function() {
		this.cancel();
		this._run=null;
	},
	fire:function() {
		if (!this.isRepeat) {
			Timer._timers.remove(this);
			this.isRunning=false;
			this.id=null;
		}
		this._run();
	},
	init:function(operation,operator) {
		if (operator && operation) {
			this._run=(typeof operation=="string"?function() {
				operator[operation]();
			}:function() {
				operator._tmpOperation=operation;
				operator._tmpOperation();
				delete operator._tmpOperation;
			});
		} else this._run=operation || function() {};
		this.id=null;
		this.isRunning=false;
	},
	schedule:function(delayMillis){
		if (delayMillis>=0) {
			this.cancel();
			this.id=this._createFn(setTimeout,this,delayMillis);
			Timer._timers.push(this);
			this.isRunning=true;
		}
	},
	scheduleRepeat:function(intervalMillis){
		if (intervalMillis>=0) {
			this.isRepeat=true;
			this.cancel();
			this.id=this._createFn(setInterval,this,intervalMillis);
			Timer._timers.push(this);
			this.isRunning=true;
		}
	},
	$tatic:{
		_timers:[],
		clear:function() {
			var _a;
			while(_a=this._timers.shift()) {
				_a.destroy();
			}
			delete this._timers;
			/*
			var _timers=this._timers;
			while(_timers.length>0) {
				_timers[0].destroy();
				_timers.removeById(0);
			}
			this._timers=_timers=null;*/
		}
	},
	_createFn:function(fn,oTimer,millis) {
		return fn(function(){oTimer.fire();},millis);
	}
});
var Timer=Class.get("Timer");

Window.addCloseListener({
	onClose:function() {
		Timer.clear();
	}
});/*
 * XML object
 */
var XML={
	_nameSpaces:{
		"html":"http://www.w3.org/1999/xhtml",
		"mathml":"http://www.w3.org/1998/Math/MathML",
		"xs":"http://www.w3.org/2001/XMLSchema",
		"json":"http://json.org/"
	},
	init:function() {
		var _impl;
		if (DOM.browser.isIE) {
			_impl=this._impl.ie;
			var ns=this._nameSpaces,nss=[];
			for (var prefix in ns) {
				nss.push("xmlns:"+prefix+'="'+ns[prefix]+'"');
			}
			this._nameSpacesString=nss.join(' ');
			this.initDocument=function(node) {
				if (!node) return false;
				var doc=node.ownerDocument;
				doc.setProperty("SelectionLanguage", "XPath");
				doc.setProperty("SelectionNamespaces",this._nameSpacesString);
			}
			ns=nss=null;
		} else {
			_impl=this._impl.moz;
			this._Serializer = new XMLSerializer();
			this._DOMParser=new DOMParser();
			this._xpathEvaluator=new XPathEvaluator();
			this._defaultResolver=function(prefix) {return XML._nameSpaces[prefix];};
		}
		this.createDocument=_impl.createDocument;
		this.load=_impl.load;
		this.getLocalName = _impl.getLocalName;
		this.parse=_impl.parse;
		this.selectSingleNode=_impl.selectSingleNode;
		this.selectNodes=_impl.selectNodes;
		this.toXml = _impl.toXml;
		_impl=null;
	},
	removeWhiteSpace:function(elem,deep,cleanComment) {
		if (!elem) return false;
		var node=elem.firstChild,nextNode;
		while(node) {
			nextNode=node.nextSibling;
			switch(node.nodeType) {
				case 1:
					if (deep && node.hasChildNodes) this.removeWhiteSpace(node,deep,cleanComment);
					break;
				case 3:
					if (!/\S/.test(node.nodeValue)) elem.removeChild(node);
					break;
				case 8:
					if (cleanComment) elem.removeChild(node);
					break;
			}
			node=nextNode;
		}
		node=nextNode=null;
		return elem;
	},

	_impl:{
		ie:{
			createDocument:function(rootTagName, namespaceURL) {
				rootTagName=rootTagName||"";
				namespaceURL=namespaceURL||"";
				var _x = Try.these(
					function() { return new ActiveXObject("MSXML2.DOMDocument"); },
					function() { return new ActiveXObject("Microsoft.XMLDOM"); }
				);
				if (rootTagName.length>0) {
					var _prefix = "";
					var _tagname = rootTagName;
					var _a=rootTagName.split(':');
					if (_a.length==2) {
						_prefix=_a[0];
						_tagname=_a[1];
					}
					if (namespaceURL.length>0 && _prefix.length==0) _prefix="gw";
					var _t="<"+(_prefix.length>0?(_prefix+":"):"") + _tagname + (namespaceURL.length>0?(" xmlns:"+_prefix+'="'+namespaceURL+'"'):"")+"/>";
					_x.loadXML(_t);
				}
				return _x;
			},
			
			getLocalName : function(node) {
				return node.baseName;
			},

			load:function(xml,url,callback) {
				xml.onreadystatechange = callback;
				xml.load(url);
			},
			parse:function(text) {
				var _x=this.createDocument();
				_x.async=false;
				_x.loadXML(text);
				return _x;
			},
			selectSingleNode:function(contextNode,expression) {
				if (!contextNode) return null;
				return contextNode.selectSingleNode(expression);
			},
			selectNodes:function(contextNode,expression) {
				if (!contextNode) return null;
				var _r=[];
				if (contextNode==contextNode.ownerDocument) contextNode=contextNode.documentElement;
				var _ns=contextNode.selectNodes(expression);
				for (var i=0,m=_ns.length; i<m; i++) _r.push(_ns.item(i));
				return _r;
			},
			
			toXml : function(node) {
				return node.xml;
			}
		},
		moz:{
			createDocument:function(rootTagName, namespaceURL) {
				rootTagName=rootTagName||"";
				namespaceURL=namespaceURL||"";
				return $doc.implementation.createDocument(namespaceURL, rootTagName, null);
			},

			getLocalName : function(node) {
				return node.localName;
			},

			load:function(xml,url,callback) {
				if (!callback) return;
				xml.onload = callback;
				xml.load(url);
			},
			parse:function(text) {
				var parser = new DOMParser();
				return parser.parseFromString(text,"application/xml");
				//return this._DOMParser.parseFromString(text,"application/xml");
			},
			selectSingleNode:function(contextNode,expression,resolver) {
				if (!contextNode) return null;
				if (!resolver) resolver=this._defaultResolver;
				var _ns=this._xpathEvaluator.evaluate(expression,contextNode,resolver,XPathResult.ANY_TYPE,null);
				return _ns.iterateNext();
			},
			selectNodes:function(contextNode,expression,resolver) {
				if (!contextNode) return null;
				if (!resolver) resolver=this._defaultResolver;
				var _ns=this._xpathEvaluator.evaluate(expression,contextNode,resolver,XPathResult.ANY_TYPE,null);
				var r,_r=[];
				while(r=_ns.iterateNext()) {
					_r.push(r);
				}
				return _r;
			},
			
			toXml : function(node) {
				return this._Serializer.serializeToString(node, "application/xml");
			}
		}
	}
};
XML.init();﻿/**
 * @class util.GenericLocker
 *
 * @author Leegorous
 */
Class.create("util.GenericLocker",null,null,{
	allKeysReady : Abstract.empty,
	//_initGenericLockerImpl : function(obj,method) {
	init : function(obj,method) {
		this._obj=obj;
		this._method=method;
		this._keys={};
	},
	insertKey:function(key,value) {
		this._keys[key]=value;
		if (this.allKeysReady()) this.unlock();
	},
	
	/**
	 * Unlock this locker when all keys are ready.
	 * It will invoke the (object's) method passed on initializing this locker.
	 */
	unlock:function() {
		var obj=this._obj;
		var method=this._method;
		if (obj && method) {
			obj[method]();
		} else if (method) method();
		this._obj=this._method=this._keys=null;
		return;
	}
});﻿/**
 * @import util.GenericLocker;
 *
 * @class util.SimpleLocker
 * @extends util.GenericLocker
 *
 * @author Leegorous
 */
Class.create("util.SimpleLocker","util.GenericLocker",null,{
	allKeysReady : function() {
		return this._k1==this._k2;
	},
	getKey : function() {
		var _key = this._key;
		this._key = _key*2;
		this._k1 |= _key;
		return _key;
	},
	init:function(obj,method) {
		this._key=1;
		this._k1 = 0;
		this._k2 = 0;
		//this._initGenericLockerImpl(obj,method);
		this[arguments.callee.parent](obj,method);
	},
	insertKey:function(value) {
		//this._keys[key]=value;
		this._k2 |= value;
		if (this.allKeysReady()) this.unlock();
	}
});﻿/**
 * @import Timer;
 *
 * @class http.Connection
 * It need the support of DOM object
 */
Class.create('http.Connection',null,null,{
	$tatic:{
		UNITIALIZED:0,
		OPEN: 1,
		SENT: 2,
		RECEIVING: 3,
		LOADED: 4
	},
	init:function() {
		this._conn=this._creatConnection();
		this._timer=null;
		this.busy=false;
	},
	abort:function() {
		this._conn.onreadystatechange=emptyFunc;
		this._conn.abort();
		this.busy=false;
	},
	open:function(method,url) {
		try {
			this._conn.open(method,url,true);
			return this.busy=true;
		} catch (e) {ErrorStack.push(e);}
		return false;
	},
	send:function(request) {
		var _this=this;
		var _conn=this._conn;
		var Connection=Class.get("http.Connection");
		_conn.onreadystatechange=function() {
			if (_conn.readyState==Connection.LOADED) {
				// When the response document is loaded, 
				// cancel the associated timer if it exist.
				if (request._timer) {
					request._timer.cancel();
					request._timer=request._timer.request=null;
				}
				// Create the response object
				var rp = _this._createResponse(_conn);
				
				try {
					// Call the callback method
					if (_conn.status==200) request.callback.onResponseReceived(request,rp);
					else request.callback.onError(request,rp,{message:_conn.status});
				} catch(e) {
					// force to show the error message cause
					// sometimes the exception will annihilate
					$win.alert(e.message);
				}
				// release the connection
				_this.busy=false;
				_this=request=request._conn=_conn=null;
			}
		}
		request._conn=this;
		try {
			_conn.send(request.getPostData());
			if (request.timeout>0) this._createTimer(request);
			return true;
		} catch (e) {ErrorStack.push(e);}
		return false;
	},
	setHeader:function(headers){
		if (headers) {
			for (var i=headers.length-1; i>=0; i--) {
				this._conn.setRequestHeader(headers[i].name,headers[i].value);
			}
		}
	},
	_createResponse:function(_conn) {
		var rp=new (Class.get("http.Response"))(), _ct;
		rp._headers=_conn.getAllResponseHeaders();
		rp.statusCode=_conn.status;
		rp.statusText=_conn.statusText;
		rp.text=_conn.responseText;
		_ct = _conn.getResponseHeader('Content-Type');
		if (_ct && _ct.indexOf('xml')!=-1) rp.xml=_conn.responseXML;
		return rp;
	},
	_createTimer:function(request) {
		var _timer = new (Class.get("Timer"))(function() {
			var request=this.request;
			request._conn.abort();
			request._conn=null;
			request._timer=null;
			request.callback.onError(request,"Request timeout.");
			this.request=request=null;
		});
		_timer.request=request;
		_timer.schedule(request.timeout);
		request._timer=_timer;
		_timer=null;
	},
	impl:{
		ie:{
			_creatConnection:function(){
				return Try.these(
					function(){return new ActiveXObject("Msxml2.XMLHTTP");},
					function(){return new ActiveXObject("Microsoft.XMLHTTP");}
				);
			}
		},
		moz:{
			_creatConnection:function(){return new XMLHttpRequest();}
		}
	}
});

/**
 * @class http.RequestPool
 */
Class.create("http.RequestPool",null,null,{
$tatic:{
	/**
	 * Default interval of reposting requests
	 * @type int
	 */
	DEFAULT_INTERVAL : 500,
	/**
	 * True if this can repost requests automatically
	 * @type boolean
	 * @deprecated
	 */
	//auto:true,
	/**
	 * True if the RequestPool has been blocked.
	 * @type boolean
	 * @deprecated
	 */
	//block:false,
	/**
	 * The interval of reposting requests
	 * @type int
	 * @deprecated
	 */
	//interval:500,
	/**
	 * The maximum of connection
	 * @type int
	 */
	MaxConnCount:5,
	/**
	 * Connections stack
	 * @type Connection[]
	 */
	_connPool:[],
	/**
	 * Requests stack
	 * @type Request[]
	 */
	_requests:[],
	/**
	 * The timer for reposting requests
	 * @type Timer
	 */
	_timer:null,
	/**
	 * Add a request to the pool, and let it handle the else.
	 * @param {Request} request the request object
	 */
	add:function(request) {
		if (request) this._requests.push(request);
		//this.block=false;
		this._post();
	},
	/**
	 * @deprecated
	 * Create a Request Pool
	 *//*
	create:function() {
		var pool=this;
		this._timer=new (Class.get("Timer"))(
			function() {
				if (pool.auto && !pool.block) pool._post();
			}
		);
		if (pool.auto) this._timer.scheduleRepeat(this.interval);
	},
	setAutoRun:function(millis) {
		if (this._timer) {
			if (millis>0 && this._timer) {
				this.auto=true;
				var interval=millis;
				if (millis<100) interval=100;
				this._timer.scheduleRepeat(interval);
			} else {
				this.auto=false;
				this._timer.cancel();
			}
		}
	},*/
	_getConn:function() {
		var conn=null,pool=this._connPool;
		for (var i=pool.length-1; i>=0; i--) {
			if (!pool[i].busy) {
				conn=pool[i];
				break;
			}
		}
		if (conn==null && pool.length<this.MaxConnCount) {
			conn=new (Class.get("http.Connection"))();
			pool.push(conn);
		}
		return conn;
	},
	_getRequest:function() {
		return this._requests.shift();
		/*
		var r=null,requests=this._requests;
		if (requests.length>0) {
			r=requests[0];
			requests.removeById(0);
		}
		return r;*/
	},
	_post:function() {
		var conn=null;
		while (conn=this._getConn()) {
			var request=this._getRequest();
			if (!request) {break;/*this.block=true;*/}
			conn.abort();
			var query='',headers=null;
			var url=request.url;
			var method=request.method;
			if (method=="GET") url+=((query=request.getPostData()).length>0?("?"+query):"");
			if (method=="POST") {
				headers=request.headers.clone();
				headers.push(new Entry("Content-Type","application/x-www-form-urlencoded"));
			}
			if (!conn.open(method,url,true)) continue;
			conn.setHeader(headers);
			if (!conn.send(request)) continue;
		}
		if (this._requests.length>0) {
			if (!this._timer) this._timer=new Timer("_post",this);
			if (!this._timer.isRunning) this._timer.scheduleRepeat(this.DEFAULT_INTERVAL);
		} else if (this._timer) this._timer.cancel();
		return true;
	}
}});

/**
 * @class http.Request
 */
Class.create('http.Request',null,null,{
	method:"GET",
	addParam:function(name,value) {
		this._addEntry(this.params,name,value);
	},
	addHeader:function(name,value) {
		this._addEntry(this.headers,name,value);
	},
	cancel:function() {
		if (this._timer) {
			this._timer.cancel();
			this._timer=this._timer.request=null;
		}
		if (this._conn) {
			this._conn.abort();
			this._conn=null;
		}
	},
	editParam:function(name,value){
		return this._editEntry(this.params,name,value);
	},
	editHeader:function(name,value){
		return this._editEntry(this.headers,name,value);
	},
	getPostData:function() {
		var s=[],e;
		for (var i=0,j=this.params.length; i<j; i++) {
			e=this.params[i];
			s.push(this._encodeComponent(e.name)+"="+this._encodeComponent(e.value));
		}
		if (s.length==0) return (s='');
		return s.join("&");
	},
	init:function(url,callback,method,timeout) {
		this.url=url;
		this.params=[];
		this.headers=[];
		this._timer=null;
		if (callback) this.callback=callback;
		if (method && method.toLowerCase()=="post") this.method="POST";
		if (timeout>=0) this.timeout=timeout;
	},
	removeAllParams:function() {this.params=[];},
	removeAllHeaders:function() {this.headers=[];},
	removeParam:function(name) {
		this._removeEntry(this.params,name);
	},
	removeHeader:function(name) {
		this._removeEntry(this.headers,name);
	},
	_addEntry:function(entrys,name,value) {
		entrys.push(new Entry(name,value));
	},
	_editEntry:function(entrys,name,value) {
			for (var i=entrys.length-1; i>=0; i--) {
				if (entrys[i].name==name) {
					entrys[i].value=value;
					return true;
				}
			}
			return false;
	},
	_encodeComponent:function(str) {
		return encodeURIComponent(str).replace(/%20/g, "+");
	},
	_removeEntry:function(entrys,name) {
		for (var i=0,j=entrys.length; i<j; i++) {
			if (entrys[i].name==name) return entrys.removeById(i);
		}
		return false;
	}
});

/**
 * @interface http.RequestCallback
 */
Interface.create("http.RequestCallback",null,null,{
	onError: Abstract.empty,
	onResponseReceived: Abstract.empty
});

/**
 * @class http.Response
 */
Class.create('http.Response',null,null,{
	allHeaders:null,
	statusCode:null,
	statusText:'',
	text:'',
	xml:null,
	getHeader:function(name){
		if (!this.allHeaders && !this._initHeaders()) return null;
		var v=this.allHeaders[name];
		if (v) return v;
		else return null;
	},
	_initHeaders:function() {
		if (!this._headers) return false;
		var hs=this._headers.split('\n');
		var _hs={},_h,_idx;
		for (var i=hs.length-1; i>=0; i--) {
			_h=hs[i];
			if (_h.length==0) continue;
			if ((_idx=_h.indexOf(':'))<0) continue;
			_hs[_h.substring(0,_idx).trim()]=_h.substring(p+1).trim();
		}
		this.allHeaders=_hs;
		return true;
	}
});

var RequestPool=Class.get("http.RequestPool");
var Request=Class.get("http.Request");

/*RequestPool.create();*//**
 * @class aop.Aspects
 *
 * @author Leegorous
 */
(function() {

var InvalidAspect = new Error("Valid <Function> aspect expected.");
var InvalidMethod = new Error("Method expected.");

Class.create("aop.Aspects" ,null ,null ,{
	$tatic : {
		addBefore : function() {},

		addAround : function(aspect, obj, methods) {
			// check aspect
			if (typeof aspect != "function") throw InvalidAspect;
			
			// find the target object, an object or the prototype of a class
			if (typeof obj == "function") obj = obj.prototype;

			// wrap the methods in array
			if (typeof methods == "string") methods = [methods];

			var method;
			var aspectFn = ("" + aspect).replace(/proceed\(\)/g, "arguments.callee._invocation.apply(this, arguments)");

			for (var i = 0, j = methods.length; i < j; i++) {
				if (!(method = obj[methods[i]]) || typeof method != "function") throw InvalidMethod;
				//$win.alert(aspectFn);
				var newAspect;
				eval("newAspect = (" + aspectFn + ")");
				newAspect._invocation = method;
				obj[methods[i]] = newAspect;
			}
		}
	}
});

})();

var Aspects = Class.get("aop.Aspects");﻿/**
 * Creator : Leegorous
 *
 * @agent true;
 * @classpath client/;
 *
 * @import global;
 * @import DOM;
 * @import Element;
 * @import Event;
 * @import EventManager;
 * @import Window;
 * @import Cookies;
 * @import Console;
 * @import Entry;
 * @import ErrorStack;
 * @import Format;
 * @import JSONParser;
 * @import Timer;
 * @import XML;
 * @import util.SimpleLocker;
 * @import http;
 * @import aop.*;
 *
 * @jsUnit ../jsunit/app/;
 * @test tests/;
 * @test tests/util/;
 */﻿/**
 * @class ui.Calendar
 */
Class.create("ui.Calendar",null,null,{
	$tatic : {
		SOURCE : "/powerx/std/module/eni/Widgets/Calendar/gw_calendar.htm",
		_sink : [],
		destroy :function() {
			var _a;
			while(_a=this._sink.shift()) _a.destroy();
			//for (var i=0,j=this._sink.length; i<j; i++) this._sink[i].destroy();
			delete this._sink;
		}
	},
	init : function(elem) {
		this._e = elem;
		if (elem.value.length>0) this.defaultDate = elem.value;
		this.format=elem.getAttribute("_format") || "YYYY-MM-DD";
		var _w = $doc.createElement("div");
		this._w=$doc.body.appendChild(_w);
		_w.className = "calendarWrapper";
		_w.innerHTML = "<iframe frameborder='0'></iframe>";
		var _f = _w.firstChild, Calendar=Class.get("ui.Calendar");
		_f.className = "calendarFrame";
		_f.src = Calendar.SOURCE;
		this.container = _f;
		this.inUsing = false;
		this.loaded = false;
		this._timer=new Timer("_check",this);
		this._repeater=new Timer("show",this);
		Calendar._sink.push(this);
		_f=_w=null;
	},
	addChangeListener : function(listener) {
		this._changeListener = listener || function() {};
	},
	changeRemind : function() {
		$win.alert("This date is invalid, it will be override with the last valid date or default value.");
	},
	checkLater : function() {
		this._timer.schedule(200);
	},
	destroy : function() {
		$doc.body.removeChild(this._w);
		this._timer.destroy();
		this._repeater.destroy();
		this._e._calendar=null;
		delete this._e;
		delete this._w;
		delete this._timer;
		delete this._repeater;
		this._changeListener=this.validate=null;
	},
	hide : function() {
		this._w.style["display"] = "none";
		$win.$currentCalendar = null;
		this.stopListener();
	},
	setDate : function(v) {
		this._e.value = v;
		this.validate();
	},
	show : function() {
		var pos = DOM.getPosition(this._e);
		var _w = this._w;
		if (pos.y!=this._y) _w.style["top"] = ((this._y=pos.y) + this._e.clientHeight) + "px";
		if (pos.x!=this._x) _w.style["left"] = (this._x=pos.x) + "px";
		_w.style["display"] = "";
	},
	startListener : function() {
		this._repeater.scheduleRepeat(500);
	},
	stopListener : function() {
		this._timer.cancel();
		this._repeater.cancel();
	},
	validate : function() {},
	_check : function() {
		//if (!$win.$currentCalendar) return;
		if (!this.loaded) {
			this.checkLater();
		}
		if (!this.inUsing) this.hide();
	}
});
Window.addCloseListener({onClose:function() {
	Class.get("ui.Calendar").destroy();
}});﻿/**
 * @interface ui.ChangeListener
 *
 * @author Leegorous
 */
Interface.create("ui.ChangeListener",null,{
	onChange:/* function(event,sender) {} */Abstract.empty
});﻿/**
 * @interface ui.ClickListener
 *
 * @author Leegorous
 */
Interface.create("ui.ClickListener",null,{
	onClick:/* function(event,sender) {} */Abstract.empty
});﻿/**
 * @interface ui.FocusListener
 *
 * @author Leegorous
 */
Interface.create("ui.FocusListener",null,{
	onFocus:/* function(event,sender) {} */Abstract.empty,
	onLostFocus:/* function(event,sender) {} */Abstract.empty
});﻿/**
 * @interface ui.KeyboardListener
 * <del>It is an interface to user, but actually it is an abstract class to me.
 * I define some private method to help to handle the keyboard event object, like getting
 * the keycode and modifiers.</del>
 *
 * @author Leegorous
 */
Interface.create("ui.KeyboardListener",null,{
	/*
	_down:function(event,sender) {
		this.onKeyDown(event,sender,this._getKeyCode(event),this._getModifiers(event));
	},
	_press:function(event,sender) {
		this.onKeyPress(event,sender,this._getCharCode(event),this._getModifiers(event));
	},
	_up:function(event,sender) {
		this.onKeyUp(event,sender,this._getKeyCode(event),this._getModifiers(event));
	},
	_getKeyCode:function(event) {return event.keyCode;},
	_getModifiers:function(event) {return (event.shiftKey?1:0) | (event.ctrlKey?2:0) | (event.altKey?4:0);},
	*/
	onKeyDown:/* function(event,sender,keycode,modifiers) {} */Abstract.empty, 
	onKeyPress:/* function(event,sender,keycode,modifiers) {} */Abstract.empty, 
	onKeyUp:/* function(event,sender,keycode,modifiers) {} */Abstract.empty/*,
	impl:{
		moz:{
			_getCharCode:function(event) {return event.charCode;}
		},
		ie:{
			_getCharCode:function(event) {return event.keyCode;}
		}
	}*/
});
/*
Object.extend(Class.get("ui.KeyboardListener"),{
	KEY_ALT : 18,
	KEY_BACKSPACE : 8,
	KEY_CTRL : 17,
	KEY_DELETE : 46,
	KEY_DOWN : 40,
	KEY_END : 35,
	KEY_ENTER : 13,
	KEY_ESCAPE : 27,
	KEY_HOME : 36,
	KEY_LEFT : 37,
	KEY_PAGEDOWN : 34,
	KEY_PAGEUP : 33,
	KEY_RIGHT : 39,
	KEY_SHIFT : 16,
	KEY_TAB : 9,
	KEY_UP : 38,
	MODIFIER_ALT : 4,
	MODIFIER_CTRL : 2,
	MODIFIER_SHIFT : 1
});*/﻿/**
 * @class ui.ListBox
 */
Class.create("ui.ListBox",null,null,{
	init : function(element) {
		this._e = element;
	},
	getValue : function() {
		return this._e.value;
	},
	hide : function() {
		if (this._e.style["display"]!="none") {
			this._display = this._e.style["display"];
			this._e.style["display"] = "none";
		}
	},
	setAttribute : function(name, value) {
		this._e.setAttribute(name, value);
	},
	/**
	 * Set this textbox readOnly
	 * @param {Boolean} readonly true if this textbox should be readonly.
	 */
	setReadOnly: function(/* Boolean */readonly) {
		this._e.disabled = !!readonly;
	},
	setValue : function(value) {
		var _selectedIndex = this._e.selectedIndex;
		this._e.value=value;
		if (this._e.value==value) return true;
		else {
			this._e.selectedIndex = _selectedIndex;
			return false;
		}
	},
	show : function() {
		if (this._e.style["display"]!="none") this._display = this._e.style["display"];
		else {
			if (this._display) this._e.style["display"] = this._display;
			else this._e.style["display"] = "";
		}
	},
	impl : {
		moz : {
			addOption : function(name, value) {
				this._e.add(new Option(name,value),null);
			}
		},
		ie : {
			addOption : function(name, value) {
				this._e.add(new Option(name,value));
			}
		}
	}
});﻿/**
 * @interface ui.MouseListener
 *
 * @author Leegorous
 */
Interface.create("ui.MouseListener",null,{
	/*
	_over : function(event, sender) {
		this.onMouseOver(event, sender);
	},
	_out : function(event, sender) {
		this.onMouseOut(event, sender);
	},
	*/
	onMouseDown : /* function(event,sender,x,y) {} */Abstract.empty,
	onMouseUp : /* function(event,sender,x,y) {} */Abstract.empty,
	onMouseOver : /* function(event,sender) {} */Abstract.empty,
	onMouseOut : /* function(event,sender) {} */Abstract.empty,
	onMouseMove : /* function(event,sender,x,y) {} */Abstract.empty/*,
	impl : {
		moz : {
			_down : function(event, sender) {
				this.onMouseDown(event, sender, event.clientX, event.clientY);
			},
			_up : function(event, sender) {
				this.onMouseUp(event, sender, event.clientX, event.clientY);
			},
			_move : function(event, sender) {
				this.onMouseMove(event, sender, event.clientX, event.clientY);
			}
		},
		ie : {
			_down : function(event, sender) {
				var _c = this._getCoordinates(event);
				this.onMouseDown(event, sender, _c.x, _c.y);
			},
			_up : function(event, sender) {
				var _c = this._getCoordinates(event);
				this.onMouseUp(event, sender, _c.x, _c.y);
			},
			_move : function(event, sender) {
				var _c = this._getCoordinates(event);
				this.onMouseMove(event, sender, _c.x, _c.y);
			},
			_getCoordinates : function(event) {
				return {
					x:ev.clientX + $doc.body.scrollLeft - $doc.body.clientLeft,
					y:ev.clientY + $doc.body.scrollTop  - $doc.body.clientTop
				};
			}
		}
	}*/
});/**
 * @import ui.MouseListener;
 *
 * @class ui.MouseListenerAdapter
 * @implements ui.MouseListener
 *
 * @author Leegorous
 */
Class.create("ui.MouseListenerAdapter",null,["ui.MouseListener"],{
	/*
	isRightClick : function(event) {
		return event.button==2;
	},*/
	onMouseDown : function(event, sender, x, y) {},
	onMouseUp : function(event,sender,x,y) {},
	onMouseOver : function(event,sender) {},
	onMouseOut : function(event,sender) {},
	onMouseMove : function(event,sender,x,y) {}
	/*,
	impl : {
		moz : {
			isLeftClick : function(event) {
				return event.button==0;
			},
			isWheelClick : function(event) {
				return event.button==1;
			}
		},
		ie : {
			isLeftClick : function(event) {
				return event.button==1;
			},
			isWheelClick : function(event) {
				return event.button==4;
			}
		}
	}*/
});﻿/**
 * @interface ui.TabListener
 *
 * @author Leegorous
 */
Interface.create("ui.TabListener", null, {
	/**
	 * Fired before a tab has been selected
	 * 
	 * @param {Widget} panel
	 * @param {int} index the index of the tab about to be selected
	 * @returns <code>false</code> to disallow the selection. If any listener returns false, then the selection will be disallowed.
	 */
	onBeforeTabSelected : /*function(panel, index)*/Abstract.empty,

	/**
	 * Fired when a tab is selected
	 * 
	 * @param {Widget} panel
	 * @param {int} index the index of the tab about to be selected
	 */
	onTabSelected : /*function(panel, index)*/Abstract.empty
});﻿/**
 * @import ui.ClickListener;
 *
 * TabPanel is a panel contains a set of tabs. Each tab contains other widgets.
 * @class ui.TabPanel
 *
 * @author Leegorous
 */
Class.create("ui.TabPanel" ,null ,null ,{
	_selectedIndex : -1,
	_maxTabWidth:0,
	_curTabWidth:0,
	/**
	 * @constructor
	 */
	init : function (argObj) {
		this._tabs = [];
		this._bodys = [];
		this._tabListeners = [];

		var panel = Element.create("div",{"class":"w-tabPanel"});
		this._e = panel;
		
		var tabBar = Element.create("div",{"class":"w-tabBar"});
		panel.appendChild(this._tabBar = tabBar);

		var scrollLeftWrap = Element.create("div", {
			"class" : "w-tabBarScrollLeft",
			"style":{"display":"none"}
		});

		var scrollLeft = Element.create("A",{
			"href" : "javascript:void(0);",
			"title" : "Scroll left",
			"innerText" : "Scroll left"
		});

		var scrollRightWrap = Element.create("div", {
			"class" : "w-tabBarScrollRight",
			"style":{"display":"none"}
		});
		
		var scrollRight = Element.create("A",{
			"href" : "javascript:void(0);",
			"title" : "Scroll right",
			"innerText" : "Scroll right"
		});

		scrollLeftWrap.appendChild(scrollLeft);
		scrollRightWrap.appendChild(scrollRight);

		tabBar.appendChild(this._scrollLeftBtn = scrollLeftWrap);
		tabBar.appendChild(this._scrollRightBtn = scrollRightWrap);

		var tabBarGroup = Element.create("div",{"class":"w-tabBarGroup"});// w-tabBarGroupScroll
		tabBar.appendChild(this._tabBarGroup = tabBarGroup);

		var tabBarInnerGroup = Element.create("div",{"class":"w-tabBarInnerGroup"});
		tabBarGroup.appendChild(this._tabBarInnerGroup = tabBarInnerGroup);

		var tabBody = Element.create("div",{"class":"w-tabBody"});
		panel.appendChild(this._tabBody = tabBody);

		if (argObj) {
			if (argObj.width>0) {
				this._maxTabWidth = argObj.width;
				tabBarGroup.style["width"] = argObj.width+"px";
			}
		}

		panel = tabBar = tabBarGroup = tabBody = scrollLeftWrap = scrollRightWrap = scrollLeft = scrollRight = null;
	},

	/**
	 * @private
	 * The implement of adding a tab
	 *
	 * @param {Widget} widget
	 * @param {String} text
	 * @param {Boolean} closeable
	 */
	_add : function (widget, text, closeable) {
		if (!widget || !text) return;
		if (typeof widget == "function") this._bodys.push(widget);
		else {
			var tabContent = Element.create("div",{"class":"w-tabContent","style":{"display":"none"}});
			this._tabBody.appendChild(tabContent);
			tabContent.appendChild(widget);
			this._bodys.push(tabContent);
		}

		var tabBarItemWrap = Element.create("div",{
			"class":"w-tabBarItemWrap"
		});

		var tabBarItem = Element.create("a",{
			"class":"w-tabBarItem",
			"href":"javascript:void(0);"
		});

		tabBarItem._tabPanel = this;

		DOM.addClickListener(tabBarItem,new (Class.get("ui.TabPanel$TabClickListener"))());

		tabBarItem.appendChild(Element.create("span",{
			"class":"w-tabBarItemInner",
			"innerText":text
		}));

		tabBarItemWrap.appendChild(tabBarItem);

		if (closeable) {
			var closeBtn = Element.create("a",{
					"class":"w-tabBarCloseBtn",
					"title":"Close this tab",
					"href":"javascript:void(0);",
					"innerText":"Close"
				});
			
			DOM.addClickListener(closeBtn, new (Class.get("ui.TabPanel$CloseTabListener"))());

			tabBarItemWrap.appendChild(closeBtn);
			tabBarItem.className+=" w-tabBarItemCloseable";
		}

		this._tabBarInnerGroup.appendChild(tabBarItemWrap);

		this._tabs.push(tabBarItem);

		if (tabBarItemWrap.clientWidth>0) this._checkTabWidth();
	},

	_checkTabWidth : function() {
		var lastTab = this._tabs[this._tabs.length-1].parentNode;
		var width = lastTab.offsetLeft + lastTab.clientWidth+3;
		if (width>this._maxTabWidth) {
			this._scrollLeftBtn.style.display = "block";
			this._scrollRightBtn.style.display = "block";
			this._tabBarGroup.style.width = (this._maxTabWidth-32)+"px";
			DOM.addStyleName(this._tabBarGroup, "w-tabBarGroupScroll");
		}
	},

	/**
	 * Add a tab
	 * @param {Widget} widget
	 * @param {String} text
	 * @param {Boolean} closeable
	 */
	add : function (widget, text, closeable) {
		// this method can only be invoked once.
		this.add = this._add;
		this.add(widget, text, closeable);

		this._selectTab(0);
	},

	/**
	 * Add a tab listener
	 *
	 * @param {TabListener} listener the tab listener
	 */
	addTabListener : function(listener) {
		if (listener) this._tabListeners.push(listener);
	},

	/**
	 * Remove a tab with the specific index.
	 *
	 * @param {int} index the tab index
	 */
	remove : function(index) {
		if (index<this.getWidgetCount()) {
			// detach the tab
			DOM.detachNode(this._tabs[index].parentNode);
			DOM.detachNode(this._bodys[index]);
			this._tabs.removeById(index);
			this._bodys.removeById(index);
			if (index<this._selectedIndex) this._selectedIndex--;
			else if (index == this._selectedIndex) {
				if (this.getWidgetCount()>0) {
					this.selectTab(index-1);
				}
			}
		}
	},

	/**
	 * Remove a tab listener
	 *
	 * @param {TabListener} listener the tab listener
	 */
	removeTabListener : function(listener) {
		if (listener) this._tabListeners.remove(listener);
	},

	getWidgetCount : function() {
		return this._tabs.length;
	},

	selectTab : function(index) {
		var listeners = this._tabListeners;
		if (listeners.length==0) return;
		for (var i=0, j=this._tabListeners.length; i<j; i++) {
			if (listeners[i].onBeforeTabSelected(this, index)===false) return;
		}

		this._selectTab(index);

		for (var i=0, j=this._tabListeners.length; i<j; i++) {
			listeners[i].onTabSelected(this, index);
		}
	},

	_getTabIndex : function(TabElem) {
		return this._tabs.indexOf(TabElem);
	},

	_fireOnTabClick : function(TabElem) {
		this.selectTab(this._tabs.indexOf(TabElem));
	},
	
	_selectTab : function(index) {
		var previousTab;
		if (index==this._selectedIndex) return;
		if (previousTab = this._tabs[this._selectedIndex]) {
			DOM.removeStyleName(previousTab, "w-tabBarSelected");
			var _body = this._bodys[this._selectedIndex];
			if (typeof _body=="object") _body.style.display = "none";
		}
		previousTab = null;
		
		DOM.addStyleName(this._tabs[index], "w-tabBarSelected");
		var curBody = this._bodys[index];
		if (typeof curBody == "function") {
			curBody = curBody();
			var tabContent = Element.create("div",{"class":"w-tabContent","style":{"display":"none"}});
			this._tabBody.appendChild(tabContent);
			tabContent.appendChild(curBody);
			curBody = this._bodys[index] = tabContent;
			tabContent = null;
		}
		curBody.style.display = "block";
		curBody = null;

		this._selectedIndex = index;
	}
});

Class.create("ui.TabPanel$TabClickListener" ,null ,["ui.ClickListener"] ,{
	onClick : function(event, elem) {
		if (elem && elem._tabPanel) {
			elem._tabPanel._fireOnTabClick(elem);
			elem.blur();
		}
	}
});

Class.create("ui.TabPanel$CloseTabListener", null, ["ui.ClickListener"], {
	onClick : function(event, elem) {
		var tabPanel;
		if (elem && elem.previousSibling && (tabPanel = elem.previousSibling._tabPanel)) {
			tabPanel.remove(tabPanel._getTabIndex(elem.previousSibling));
		}
		tabPanel = null;
	}
});﻿/**
 * @class ui.TextBox
 * 
 * @author Leegorous
 */
Class.create("ui.TextBox",null,null,{
	destroy : function() {
		DOM.detachNode(this._e);
		delete this._e;
	},
	getText : function() {
		return this._e.value;
	},
	/**
	 * Get the text value of this textbox
	 * @returns {String} text value of this textbox
	 */
	getValue : function() {
		return this._e.value;
	},
	hide : function() {
		if (this._e.style["display"]!="none") {
			this._display = this._e.style["display"];
			this._e.style["display"] = "none";
		}
	},
	init:function(element) {
		if (!element) {
			element = $doc.createElement("input");
			element.type = "text";
		}
		this._e=element;
	},
	setAttribute : function(name,value) {
		this._e.setAttribute(name,value);
	},
	setMaxLength:function(i) {
		this._e["maxLength"]=i;
	},
	/**
	 * Set this textbox readOnly
	 * @param {Boolean} readonly true if this textbox should be readonly.
	 */
	setReadOnly: function(/* Boolean */readonly) {
		this._e.readOnly = !!readonly;
	},
	setText : function(v) {
		this._e.value=v;
	},
	/**
	 * Set the text value of this textbox
	 * @param {String} v the text value of this textbox
	 */
	setValue : function(v) {
		this._e.value=v;
	},
	show : function() {
		if (this._e.style["display"]!="none") this._display = this._e.style["display"];
		else {
			if (this._display) this._e.style["display"] = this._display;
			else this._e.style["display"] = "";
		}
	},
	impl:{
		moz:{
			getCursorPos:function() {
				try {
					return this._e.selectionStart;
				} catch(e) {return 0;}
			},
			getSelectionLength:function() {
				try{
					return this._e.selectionEnd - this._e.selectionStart;
				} catch (e) { return 0;}
			},
			setSelectionRange:function(pos,length) {
				this._e.setSelectionRange(pos, pos+length);
			}
		},
		ie:{
			getCursorPos:function() {
				try {
					var _a=this._e.document.selection.createRange();
					return -_a.move("character", -65535);
				} catch(e) {return 0;}
			},
			getSelectionLength:function() {
				try {
					var _a = this._e.document.selection.createRange();
					return _a.text.length;
				} catch (e) {return 0;}
			},
			setSelectionRange:function(pos,length) {
				try {
					var _a=this._e.createTextRange();
					_a.collapse(true);
					_a.moveStart('character', pos);
					_a.moveEnd('character', length);
					_a.select();
					_a=null;
				} catch (e) {}
			}
		}
	}
});﻿/**
 * Creator : Leegorous
 *
 * @agent true;
 * @classpath client/;
 *
 * @import ui.*;
 *
 * @jsUnit ../jsunit/app/;
 * @test tests/ui/;
 */﻿/**
 * @class msg.ui.CommonContainerListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.CommonContainerListener",null,null,{
	findContainer:function(elem) {
		if (!elem) return null;
		var _elem=elem,_c;
		// find the container with the container id of the node
		if (!(_c = CtnMgr.get(_elem._ctnId))) {
			// find container id from the node hierarchy
			while(_elem = _elem.parentNode) {
				if (_elem._ctnId && (_c = CtnMgr.get(_elem._ctnId))) {
					elem._ctnId = _elem._ctnId;
					break;
				}
			}
		}
		return _c;
	}
});/**
 * @class msg.ui.SimpleContainer
 * @extends Object
 * This is an abstract class, and the base class for all container.
 */
Class.create("msg.ui.SimpleContainer",null,null,{
	/**
	 * @private
	 * The document fragment of this contaienr body.
	 */
	_body : null,
	/**
	 * @private
	 * The document fragment of this contaienr.
	 */
	_container : null,
	/**
	 * The 'bit' property indicate what should this container look like.
	 * It helps to locate the exactly template for this container.
	 */
	bit : 0,
	/**
	 * @private
	 * The dictionary object
	 */
	dict : null,
	/**
	 * The 'readOnly' property indicate the edit status.
	 * True if this container is read only.
	 */
	readOnly : false,
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:Abstract.empty,
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:Abstract.empty,
	/**
	 * The container description.
	 */
	description : "",
	/**
	 * Destroy this container.
	 */
	//destroy : new MethodAlias("_destroySimpleImpl"),
	//_destroySimpleImpl:function() {
	destroy : function() {
		delete this.node;
		//delete this._ownerMessage; @2008-4-21
		delete this.ownerDocument;
		delete this._container;
		delete this._body;
		delete this._parent;
		CtnMgr.remove(this.id);
	},
	
	/**
	 * @constructor
	 *
	 * @param {Object} _node
	 * @param {Container} _parentContainer
	 */
	init : function(_node, _parentContainer) {
		//this._initSimpleImpl(_node,_parentContainer);
	//},
	//_initSimpleImpl : function(_node,_parentContainer) {
		//Console.put("Creating "+_node.name+" of "+_parentContainer.name);
		if (!_parentContainer) throw new Error('Parent container requested.');
		//-- get the name from current node, and set it as the name of this container
		this.name=_node.name || '';
		//this.bit=0;
		//this._body=null;
		//-- set the data node
		this.node=_node;
		//-- set parent container
		this._parent=_parentContainer;
		//-- set the owner message
		//this._ownerMessage=_parentContainer._ownerMessage;

		// set the owner document
		this.ownerDocument = _parentContainer.ownerDocument;

		//-- checkup whether this pattern container is optional
		this.bit|=((this.enabled=(_node.minOccurs!=0))?0:ContainerTemplates.isOptional);
		
		var _dict;
		// get the dictionary from the parent container
		//-- get the longName, shortName and description(expansion) from corresponding dictionary
		/*if (_dict=CurrentDictionary[_node.type]) {
		//-- checkup whether the container has description
			if (_dict.expansion) {
				this.description=_dict.expansion;
				this.bit|=ContainerTemplates.hasDescription;
			}
			//-- get the long name from dictionary
			this.longName=_dict.longName;
			//-- get the short name from dictionary, 
			//-- if it is not exist, set the container name as its short name
			this.shortName = this.name;
		}

		if (!(_dict = _node.dict) && _parentContainer.node.dict) {
			if (_dict=_parentContainer.node.dict[this.name]) _node.dict = _dict;
			if (this.isVirtual()) _node.dict = _parentContainer.node.dict;
		}*/

		if (_parentContainer && (_dict=_parentContainer.dict)) {
			this.dict = _dict = this.isVirtual() ? _dict : _dict[this.name];
			//this.dict = _dict;
			if (_dict) {
				if (_dict.expansion) {
					this.description=_dict.expansion;
					this.bit|=ContainerTemplates.hasDescription;
				}
				//-- get the long name from dictionary
				this.longName=_dict.longName;
			}
		}
		//_dict=null;
		//-- set the container id
		this.id = CtnMgr.add(this);
		//delete this._super;
	},

	isOptional : function() {
		return this.bit & ContainerTemplates.isOptional;
	},
	
	isVirtual : function() {
		return !!this.node.virtual;
	},
	/**
	 * Compare search string with the container infomation, return true if this container
	 * is matched.
	 * @param {String} v
	 * @returns {Boolean} true if this container matched.
	 */
	//match : new MethodAlias("_matchSimpleImpl"),
	//_matchSimpleImpl : function(v) {
	match : function(v) {
		if (this.name.indexOf(v)>=0) return true;
		if (this.description.indexOf(v)>=0) return true;
		if (this.longName && this.longName.indexOf(v)>=0) return true;
		if (this.shortName && this.shortName.indexOf(v)>=0) return true;
	},

	/**
	 * Notify the observer
	 *
	 * @param {String} type
	 */
	notify : function(type) {
		if (this.observer) this.observer.update(type);
	},
	
	/**
	 * Add a listener to the container observer
	 *
	 * @param {string} type
	 * @param {Object} listener
	 */
	addListener : function(type, listener) {
		this._dispatchListener("add", type, listener);
	},
	
	/**
	 * Remove a listener from the container observer
	 *
	 * @param {string} type
	 * @param {Object} listener
	 */
	removeListener : function(type, listener) {
		this._dispatchListener("remove", type, listener);
	},
	
	_dispatchListener : function(action, type, listener) {
		if (!this.observer) throw new Error("Observer does not exist.");

		if (!type || type.length == 0 || !listener) throw new Error("Invalid parameter on adding listener.");

		var methodName = action + type.charAt(0).toUpperCase() + type.substring(1) + "Listener";
		if (this.observer[methodName]) this.observer[methodName](listener);
	},
	
	/**
	 * select containers
	 *
	 * @param {String} path
	 * @returns null
	 */
	select : function(path) {
		//if (!this.enabled) return null;
		if (path == "." || this.name == path) return [this];
		return null;
	},

	/**
	 * Set the observer
	 * 
	 * @param {Object} observer
	 */
	setObserver : function(observer) {
		this.observer = observer;
	}
	/*
	$tatic:{
		// deprecated.
		_valueSequence:[],
		initContainer:function() {
			var _a=this._valueSequence;
			for (var i=0,j=_a.length;i<j;i++) {
				_a[i].innerHTML=_a[++i];
			}
		}
	}*/
});/**
 * @import msg.ui.SimpleContainer;
 *
 * @class msg.ui.CompositeContainer
 * @extends msg.ui.SimpleContainer
 */
Class.create("msg.ui.CompositeContainer","msg.ui.SimpleContainer",null,{
	add : function(child) {
		if (!this._body) throw new Error("body does not exist.");
		var name = child.name || this.ownerDocument.generateContainerName();
		this.childs[name]=child;
		this._body.appendChild(child._container);
	},
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody : function() {
		var _a = $doc.createElement("div");
		_a.className = "compositeBody";
		this._container.appendChild(_a);
		_a = null;
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer : function() {
		//Console.put(this.bit);
		return ContainerTemplates.get("CompositeContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyCompositeImpl"),
	//_destroyCompositeImpl : function() {
	destroy : function() {
		this.eachChild(function(child) {
			child.destroy();
		});
		delete this.childs;
		//this._destroySimpleImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Iterate over all childs in this container
	 * @param {Method} iterator The action
	 */
	eachChild : function(iterator) {
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && (i==child.name || child.isVirtual())) iterator(child);
		}
		child = null;
	},
	
	/**
	 * Fill value to the container
	 *
	 * @param {Node} node the xml node
	 */
	fill : function(node) {
		var nodes, child, result = false;
		//if (!this.enabled) this.setEnabled(true);
		if (!this.enabled) {
			//this._container.firstChild.firstChild.firstChild.checked=true;
			this.setEnabled(true);
		}
		if (!(nodes = node.childNodes)) return;
		for (var i=0,j=nodes.length; i<j; i++) {
			this._fill(nodes[i]);
			/*
			child = this.childs[nodes[i].nodeName];
			if (child) {
				child.fill(nodes[i]);
				result = true;
			} else {
				if (!this._fillVirtualChilds(nodes[i])) 
					Console.error("Error occur in filling "+nodes[i].nodeName);
			}
			*/
		}
		node=nodes=child=null;
		return result;
	},
	
	/**
	 * @protected
	 * Fill the xml data to the specific child
	 *
	 * @param {Node} node the xml data node
	 * @returns {Boolean} true if there is an match filling
	 */
	_fill : function(node) {
		var result = false;
		var nodeName = XML.getLocalName(node);
		var child = this.childs[nodeName];
		if (child) {
			child.fill(node);
			result = true;
		} else {
			if (!this._fillVirtualChilds(node))
				Console.error("Err: Container for [" + nodeName + "] is not found");
		}
		node = null;
		return result;
	},

	/**
	 * @private
	 * Try to fill the value in virtual childs
	 *
	 * @param {Node} node the xml node
	 * @returns {Boolean}
	 */
	_fillVirtualChilds : function(node) {
		var childs = this.childs, result = false;
		for (var i in childs) {
			var child = childs[i];
			if (child.isVirtual()) {
				if (child.childs) {
					if (child.includeChild(XML.getLocalName(node))) {
						if (!child.enabled) child.setEnabled(true);
						if (child._fill(node)) result = true;
					}
				} else {
					if (child.node.type == "Any") {
						child.fill(node);
						result = true;
					}
				}
			}
			/*
			if (child.isVirtual() && child.includeChild(node.nodeName)) {
				if (!child.enabled) child.setEnabled(true);
				if (child._fill(node)) result = true;
			}*/
		}
		node = null;
		return result;
	},

	/**
	 * Check out if there is a child (maybe not exist yet) by name
	 */
	includeChild : function(name) {
		var childs = this.node.childs;
		for (var i = 0, j = childs.length; i < j; i++) {
			if (childs[i].name == name) return true;
		}
		return false;
	},

	/**
	 * @constructor
	  *
	 * @param {Object} _node
	 * @param {Container} _parentContainer
	 */
	init : function(_node, _parentContainer) {
		/*this._initCompositeImpl(_node,_parentContainer);
		
	},
	_initCompositeImpl : function(_node,_parentContainer) {*/
		//this._super(_node,_parentContainer);
		//this._initSimpleImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this.childs = {};
		this._container = this.createContainer();
		//this._container._container = this;
		this._container._ctnId = this.id;
		this.setLabel();
		//if (this.bit & ContainerTemplates.hasDescription) this.setInfo();
		if (!(this.bit & ContainerTemplates.isOptional)) this.initBody();
		//if (_parentContainer) _parentContainer.add(this);
	},
	
	initBody : function() {
		this._body=this._container.lastChild;
		//UIObjectTree.walk(this.node,this);
		//CtnBuilder.buildChild(this.node, this);
		this.ownerDocument.containerBuilder.buildChild(this.node, this);
	},

	setEnabled : function(enabled) {
		if (this.enabled=!!enabled) {
			if (!this._body) {
				this.createBody();
				this.initBody();
			}
			this._body.style["display"]="";
		} else if (this._body) this._body.style["display"]="none";

		var checkbox = this._container.firstChild.firstChild.firstChild;
		var checked = checkbox.checked;
		if (this.enabled != checked) {
			checkbox.checked = this.enabled;
		}
		checkbox = null;
		
		// notify observer
		this.notify("enabled");
	},
	
	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		if (!path || path.length==0) return null;
		var idx, child;
		if (path.indexOf(".") == 0) {
			if ((path = path.substring(1)).length == 0) return [this];
		} else {
			if (!this.enabled) return null;
		}
		
		if ((idx = path.indexOf("/")) > 0) {
			if (child = this.childs[path.substring(0,idx)]) 
				return child.select(path.substring(idx + 1));
		} else {
			if (child = this.childs[path]) return child.select(".");
		}
		return null;
	},
	
	/**
	 * @deprecated
	 */
	setInfo : function() {
		this._container.firstChild.lastChild.lastChild.innerHTML=this.description;
	},
	setLabel : function() {
		// set the label when this is not virtual
		if (!this.isVirtual()) {
			//var displayName = this.isVirtual() ? "" : (this.longName || this.name);
			var displayName = this.longName || this.name;
			this._container.firstChild.lastChild.firstChild.innerHTML = displayName;
			if (this.bit & ContainerTemplates.hasDescription) 
				this._container.firstChild.lastChild.lastChild.innerHTML=this.description;
			this._container.title = displayName + " " + this.description;
		}
	},
	showMatch : function(v) {
		var result = false;
		if (v.length==0) result = true;
		if (this.match(v)) {
			result = true;
			v = "";
		}
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.showMatch(v)) result = true;
			});
		}
		if (result) this._container.style["display"]="";
		else this._container.style["display"]="none";
		return result;
	},
	showOptional : function(/* Boolean: */show) {
		if (!this.enabled) {
			if (show) {
				if (this._container.style["display"]=="none") return this._container.style["display"]="";
			} else return this._container.style["display"]="none";
		}
		this.eachChild(function(child) {
			child.showOptional(show);
		});
		/*
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.showOptional(show);
		}
		child = null;*/
	},
	/**
	 * Set this container and all childs writable or read only.
	 * @param {Boolean} readonly true if this container and all childs should be readonly.
	 */
	setReadOnly : function(/* Boolean: */readonly) {
		//var child;
		this.readOnly = readonly = !!readonly;
		if (this.bit & ContainerTemplates.isOptional) this._setOptionalCheckboxReadOnly(readonly);
		this.eachChild(function(child) {
			child.setReadOnly(readonly);
		});
		/*
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.setReadOnly(readonly);
		}
		child = null;*/
	},
	/**
	 * Get the XML string
	 */
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitComposite(this);
		/*
		var xml=[], childsXml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				this.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				xml.push('<'+this.name);//+' type="SwiftFieldElement" typeName="'+this.node.type+'"'
				//if (this.node.Separator) xml.push(' separator="'+this.node.Separator.replace(/\n/g,"&#10;")+'"');
				xml.push('>');
				xml.push(childsXml);
				xml.push('</'+this.name+'>');
			}
		}
		return xml.join("");
		*/
	},

	validate : function() {
		//var child;
		var result=true;
		this.eachChild(function(child) {
			if (child.enabled && !child.validate()) result = false;
		});
		/*
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name && child.enabled) {
				if (!child.validate()) result = false;
			}
		}*/
		return result;
	},

	_setOptionalCheckboxReadOnly : function(/* Boolean: */readonly) {
		this._container.firstChild.firstChild.firstChild.disabled = readonly;
	},

	lazyAddingListener : function(path, type, listener) {
		if (!path || path.length == 0 || !type || type.length == 0 || !listener) return;
		var fields;
		if (fields = this.select(path)) {
			for (var i=0,j=fields.length; i<j; i++) {
				fields[i].addListener(type, listener);
			}
		} else {
			var parentPath = path;
			while((parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"))).length > 0) {
				if (fields = this.select(parentPath)) {
					fields.each(function(field) {
						field._hangUpAddingListener(path.substring(parentPath.length + 1), type, listener);
					});
					break;
				}
			}
		}
	},
	
	/**
	 * Hang up the process of add listener
	 *
	 * @param {String} path
	 * @param {String} type
	 * @param {Object} listener
	 */
	_hangUpAddingListener : function(path, type, listener) {
		if (!this.enabled) {
			var _this = this;
			var _eListener = {
				onEnabled : function(enabled) {
					if (!enabled) return;
					_this._hangUpAddingListener(path, type, listener);
					_this.removeListener("enabled", _eListener);
					_eListener = _this = null;
				}};
			this.addListener("enabled", _eListener);
		} else {
			this.lazyAddingListener(path, type, listener);
		}
	}
});/**
 * @class msg.ui.ContainerBuilder
 *
 * @author Leegorous
 */
Class.create("msg.ui.ContainerBuilder" ,null ,null ,{

	/**
	 * build the container by the node
	 *
	 * @param {Object} node the schema object
	 * @param {Container} parentContainer the parent container
	 */
	build : function(node, parentContainer) {
		var name = this.getContainerClassName(node);
		//if (!node.base) name = "ecp.ui."+node._ui+"PatternContainer";
		//else name = this.conf[node.base];
		var container = new (Class.get(name))(node, parentContainer);

		var observerName = this.getObserverClassName(node);
		if (observerName) container.setObserver(new (Class.get(observerName))(container));

		if (parentContainer) parentContainer.add(container);

		if (!container.isOptional()) container.notify("enabled");

		return container;
	},
	
	/**
	 * Build the containers for the child nodes
	 *
	 * @param {Object} node the schema object
	 * @param {Container} parentContainer the parent container
	 */
	buildChild:function(node, parentContainer) {
		var childs;
		if (!(childs=node.childs)) return;
		for (var i=0,j=childs.length;i<j;i++) {
			this.build(childs[i], parentContainer);
		}
		childs = null;
	},
	
	/**
	 * @abstract
	 * Get the class name of the container by schema node
	 *
	 * @returns {String} the class name
	 */
	getContainerClassName : Abstract.empty,

	/**
	 * @abstract
	 * Get the class name of the observer by schema node
	 * 
	 * @returns {String} the class name
	 */
	getObserverClassName : Abstract.empty
});/**
 * @import msg.ui.CommonContainerListener;
 * @import ui.ClickListener;
 *
 * @class msg.ui.ContainerEnableSwitcher
 * @extend msg.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.ContainerEnableSwitcher" ,
	"msg.ui.CommonContainerListener" ,
	["ui.ClickListener"] ,{
	
	onClick:function(event,sender) {
		var _container;
		if (_container = this.findContainer(sender)) {
			_container.setEnabled(sender.checked);
		}
		_container=null;
	}
});﻿/**
 * @import DOM;
 * @import Event;
 *
 * @class msg.ui.ContainerEvent;
 *
 * @author Leegorous
 */
Class.create("msg.ui.ContainerEvent" ,null ,null ,{
	$tatic : {
		
		_sink : {},

		bind : function(/* String */ name, /* Object */ listeners) {
			if (!name || name.length==0 || !listeners) return;
			var _objStack = {}, evtId = null;
			for (var i in listeners) {
				var itemName, _obj;
				if (typeof (itemName = listeners[i]) != "string") continue;
				if (!(_obj = _objStack[itemName])) {
					_objStack[itemName] = _obj = new (Class.get(itemName))();
				}

				var methodName = "add" + i.charAt(0).toUpperCase() + i.substring(1) + "Listener";
				if (DOM[methodName]) evtId = DOM[methodName](evtId, _obj);
			}
			if (evtId) return this._sink[name] = evtId;
			else throw new Error("Expected to bind event listeners.");
		},

		getEventId : function(/* String */ name) {
			return this._sink[name] || null;
		}
	}
});

var ContainerEvent = Class.get("msg.ui.ContainerEvent");/**
 * @import msg.ui.CommonContainerListener;
 * @import ui.ClickListener;
 *
 * @class msg.ui.ContainerExpandListener
 * @extend msg.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.ContainerExpandListener" ,
	"msg.ui.CommonContainerListener" ,
	["ui.ClickListener"] ,{

	onClick:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.expand(!_container.expanded);
		_container=null;
	}
});/**
 * @import DOM;
 * @import Element;
 *
 * @class msg.ui.ContainerTemplate
 *
 * @author Leegorous
 */
Class.create("msg.ui.ContainerTemplate" ,null ,null ,{
	$tatic : {
		isOptional : 1,
		hasFormat : 2,
		hasDescription : 4,
		increasable : 8,
		removable : 16,
		
		// templates are unavailable while they are not ready.
		ready : false,
		
		// template sink
		_sink : {},

		bind : function(name, classObject) {
			this._sink[name] = classObject;
		},
		
		bindEventTrigger : function(/* Element */ elem, /* int */ evtId) {//debugger;
			var sink = DOM.getEventSink(evtId);
			if (sink && sink.bits) {
				var bits = sink.bits;
				if (bits & 0x000040) elem.setAttribute("onclick", "_$f(event)");
				if (bits & 0x0000BE) {
					if (bits & 0x000001) elem.setAttribute("onmousedown", "_$f(event)");
					if (bits & 0x000002) elem.setAttribute("onmouseup", "_$f(event)");
					if (bits & 0x000004) elem.setAttribute("onmouseover", "_$f(event)");
					if (bits & 0x000008) elem.setAttribute("onmouseout", "_$f(event)");
					if (bits & 0x000010) elem.setAttribute("onmousemove", "_$f(event)");
					if (bits & 0x000080) elem.setAttribute("ondblclick", "_$f(event)");
				}
				if (bits & 0x000700) {
					if (bits & 0x000100) elem.setAttribute("onkeydown", "_$f(event)");
					if (bits & 0x000200) elem.setAttribute("onkeyup", "_$f(event)");
					if (bits & 0x000400) elem.setAttribute("onkeypress", "_$f(event)");
				}
				if (bits & 0x8CB000) {
					if (bits & 0x001000) elem.setAttribute("onfocus", "_$f(event)");
					if (bits & 0x002000) elem.setAttribute("onblur", "_$f(event)");
					if (bits & 0x008000) elem.setAttribute("onchange", "_$f(event)");
					//if (bits & 0x02000) elem.setAttribute("onlosecapture","_$f(event)");
					if (bits & 0x040000) elem.setAttribute("onscroll", "_$f(event)");
					if (bits & 0x080000) elem.setAttribute("onload", "_$f(event)");
					if (bits & 0x800000) elem.setAttribute("onerror", "_$f(event)");
				}
				elem.setAttribute("__evt", evtId);
				
				// make the trigger real,
				// the process above does not really bind the event trigger in IE,
				// so it need to be re-instantiation
				if (DOM.isIE) {
					var parentElem
					if (parentElem = elem.parentNode) {
						parentElem.innerHTML = parentElem.innerHTML;
						elem = parentElem.firstChild;
					} else {
						parentElem = Element.create('div',{style:{"display":"none"}});
						$doc.body.appendChild(parentElem);
						parentElem.appendChild(elem);
						parentElem.innerHTML = parentElem.innerHTML;
						elem = parentElem.firstChild;
						DOM.detachNode(elem);
						DOM.detachNode(parentElem);
						parentElem = null;
					}
				}
				
				return elem;
			}
		},

		get : function(name, bit) {
			if (this._sink[name]) return new this._sink[name](bit);
			return null;
		},
		
		_tmpStack : {},
		
		/**
		 * Prepare the templates
		 *
		 * @param {String} source the template container id
		 */
		prepare : function(source) {
			var sink = $(source);
			if (!sink) return;
			XML.removeWhiteSpace(sink, true);
			if (sink.hasChildNodes()) {
				var j=sink.childNodes.length;
				for (var i=0; i<j; i++) {
					var name;
					if ((name = sink.childNodes[i].getAttribute("name")) && name.length>0)
						this._tmpStack[name] = sink.childNodes[i];
				}
				
				for (var i in this._tmpStack) {
					var node = this._tmpStack[i], name, type;
					if (!node || node.nodeType != Node.ELEMENT_NODE) continue;
					this.build(node);
				}
			}
			
			this.ready = true;
		},
		
		/**
		 * Build template
		 *
		 * @param {Element} node the template node
		 */
		build : function(/* Element */ node) {
			var name, type;
			if (typeof node == "string") node = this._tmpStack[node];
			if (!node) return;
			if ((name = node.getAttribute("name")) && name.length>0 && (type = node.getAttribute("type")) && type.length>0) {
				Class.get(type).bindTemplate(node);
				delete this._tmpStack[name];
			}
		}
	}
});

var ContainerTemplate = Class.get("msg.ui.ContainerTemplate");/**
 * Document container
 *
 * @class msg.ui.DocumentContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.DocumentContainer",null,null,{
	
	$tatic : {
		// View mode
		VIEW : 1,
		// Edit mode
		EDIT : 2,
		// Create template mode
		CREATE_TEMPLATE : 4
	},

	_containerNameSerial : 1,
	
	// default is edit mode
	status : 2,

	language : "en",

	add : function(container) {
		//if (this.message) throw new Error("This document can only contains one message.");
		this.childs[container.name] = container;
		this._wrapper.appendChild(container._container);
	},

	lazyAddingListener : function(path, type, listener) {
		if (!path || path.length == 0 || !type || type.length == 0 || !listener) return;
		var fields;
		if (fields = this.select(path)) {
			for (var i=0,j=fields.length; i<j; i++) {
				fields[i].addListener(type, listener);
			}
		} else {
			var parentPath = path;
			while((parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"))).length > 0) {
				if (fields = this.select(parentPath)) {
					fields.each(function(field) {
						field._hangUpAddingListener(path.substring(parentPath.length + 1), type, listener);
					});
					break;
				}
			}
		}
	},

	/**
	 * apply rule
	 *
	 * @param {Object} rules object
	 */
	applyRule : function(rule) {//debugger;
		if (!rule.rules || !rule.rules.main) return;
		rule.rules.main(this);
	},

	/**
	 * Build the message
	 */
	buildMessage : function() {//debugger;
		// get dictionary
		this.dict = this.messageManager.getDictionary(this.messageType, this.language);
		if (this.dict && this.dict.Document) this.dict = this.dict.Document;
		//CtnBuilder.buildChild(this.node,this);
		this.containerBuilder.buildChild(this.node, this);
	},
	/**
	 * Destroy this document
	 */
	destroy : function() {
		/*
		if (this.message) {
			this._wrapper.removeChild(this.message._container);
			this.message.destroy();
			delete this.message;
		}*/
		this.eachChild(function(child) {
			child.destroy();
		});
		delete this.childs;
		delete this.messageManager;
		delete this._wrapper;
		delete this.node;
		delete this.ownerDocument;
	},

	/**
	 * Iterate over all childs in this container
	 * @param {Method} iterator The action
	 */
	eachChild : function(iterator) {//debugger;
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) iterator(child);
		}
		child = null;
	},
	
	/**
	 * Get the message container of this document
	 *
	 * @returns {MessageContainer}
	 */
	getMessage : function() {
		return this.childs[this.messageType] || null;
	},

	generateContainerName : function() {
		return "Ctn" + this._containerNameSerial++;
	},

	/**
	 * @constructor
	 *
	 * @param {Object} node
	 */
	init : function(node, parentContainer) {
	//init : function(_node, _dict, _wrapper/*, lang*/) {
		/*this._initDocumentImpl(_node, _wrapper, lang);
	},
	_initDocumentImpl : function(_node, _wrapper, lang) {*/
		if (!node) // || !info.node || !info.wrapper || !info.generateXmlVisitor || !info.containerBuilder
			throw new Error("DocumentContainer: infomation expected.");
		//-- Document root node
		this.node = node;

		this._getMessageType(node);

		this.childs = {};
		
		//this.generateXmlVisitor = new (Class.get(info.generateXmlVisitor))();
		
		//this.containerBuilder = new (Class.get(info.containerBuilder))();

		//var _dict = info.dict;

		//var _dict = MsgMgr.getDictionary(_node.childs[0].name, lang);
		//if (_dict) info.node.dict = _dict[info.node.name];

		//this.dict = info.dict ? info.dict.Document : null;

		//-- Target container
		//var _wrapper = info.wrapper;
		//if (typeof info.wrapper=="string") _wrapper = $(info.wrapper);
		//if (!_wrapper) throw new Error("Container wrapper required.");
		//this._wrapper = _wrapper;
		//-- Set the ownerDocument
		this.ownerDocument = this;

		this.id = CtnMgr.add(this);

		if (parentContainer) {
			this._getInfoFromParentDocument(parentContainer);
		}
		//-- Start to build the message
		//this.buildMessage();
	},

	/**
	 * @private
	 *
	 * Get the information from the parent document
	 *
	 * @param {Container} parentContainer
	 */
	_getInfoFromParentDocument : function(parentContainer) {
		var parentDoc = parentContainer.ownerDocument;
		// set the parent document
		this.parentDocument = parentDoc;

		// inherit language from parent document
		this.setLanguage(parentDoc.language);

		// get the wrapper from parent container
		this.setContainerWrapper(parentContainer._body);

		// inherit message manager from parent document
		this.setMessageManager(parentDoc.messageManager);

		// inherit generateXmlVisitor from parent document
		this.setGenerateXmlVisitor(parentDoc.generateXmlVisitor);

		// inherit container builder from parent document
		this.setContainerBuilder(parentDoc.containerBuilder);

		this.buildMessage();
	},

	_getMessageType : function(node) {
		this.targetNamespace = node.targetNamespace || "";
		this.messageType = node.childs[0].name;
	},

	/**
	 * Set the container builder
	 *
	 * @param {String|Object} builder
	 */
	setContainerBuilder : function(builder) {
		switch(typeof builder) {
			case "string" : 
				this.containerBuilder = new (Class.get(builder))();break;
			case "object" :
				this.containerBuilder = builder;break;
			default : 
				throw new Error("DocumentContainer: Invalid container builder");
		}
	},

	/**
	 * Set the container wrapper
	 *
	 * @param {String|Object} wrapper
	 */
	setContainerWrapper : function(wrapper) {
		wrapper = (typeof wrapper == "string") ? $(wrapper) : wrapper;
		if (wrapper) this._wrapper = wrapper;
		else throw new Error("DocumentContainer: Invalid container wrapper");
	},

	/**
	 * Set visitor for generating xml
	 *
	 * @param {String|Object} visitor
	 */
	setGenerateXmlVisitor : function(visitor) {
		switch(typeof visitor) {
			case "string" : 
				this.generateXmlVisitor = new (Class.get(visitor))();
				break;
			case "object" : 
				this.generateXmlVisitor = visitor;
				break;
			default : 
				throw new Error("DocumentContainer: Invalid visitor");
		}
	},

	/**
	 * Set the language
	 *
	 * @param {String} lang
	 */
	setLanguage : function(lang) {
		if (lang) this.language = lang;
	},

	/**
	 * Set the message manager
	 *
	 * @param {Object} manager
	 */
	setMessageManager : function(manager) {
		if (manager) this.messageManager = manager;
	},
	
	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		// check parameter
		if (!path || path.length==0) return null;// || !this.message
		if (path.indexOf("/") == 0) path = path.substring(1);
		var idx, child;
		/*
		if ((idx = path.indexOf("/")) > 0) {
			if (this.message.name == path.substring(0,idx)) {
				return this.message.select(path.substring(idx + 1));
			}
		} else {
			if (this.message.name == path) return [this.message];
		}*/

		if ((idx = path.indexOf("/")) > 0) {
			if (child = this.childs[path.substring(0,idx)]) 
				return child.select(path.substring(idx + 1));
		} else {
			if (child = this.childs[path]) return child.select(".");
		}
		return null;
		//return this.message.select(path);
	},
	
	toXML : function() {
		return this.generateXmlVisitor.visitDocument(this);
		/*
		var xml = [];
		xml.push('<Document xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="'+this.node.xmlns+'">');
		xml.push(this.message.toXML());
		xml.push('</Document>');
		return xml.join('');*/
	}
});/**
 * 
 * @class msg.ui.MessageManager
 *
 * PATH.getSchemaPath() and PATH.getDictionaryPath() are requested.
 * @author Leegosous
 */
Class.create("msg.ui.MessageManager",null,null,{//$tatic:{
	//_schemas : {},
	//_dicts : {},

	/**
	 * @constructor
	 *
	 * @param {Object} impl
	 */
	init : function(impl) {
		// create a schema stack
		this._schemas = {};

		// create a dictionary stack
		this._dicts = {};

		// create a rule stack
		this._rules = {};

		// deprecated
		// create a schedule queue
		this._schedule = [];

		// set the implements
		Object.extend(this, impl);
	},

	/**
	 * @deprecated
	 */
	_addTask : function(builder ,type , name) {
		var item,found = false,_schedule = this._schedule;
		// As the element name, "." is not allowed. All "." will be replaced with "_".
		var _name = name.replace(".","_");
		find:
		for (var i=0,j=_schedule.length; i<j; i++) {
			for (var m=1,n=_schedule[i].length; m<n; m++) {
				if (_schedule[i][m]==_name) {
					found = true;
					break find;
				}
			}
		}
		if (!found) this._createRequest(type, name);
		for (i=0; i<j; i++) {
			if (_schedule[i][0]==builder) break;
		}
		if (i<j) item = _schedule[i];
		else _schedule.push(item=[builder]);
		item.push(_name);
		_schedule = null;
	},

	/**
	 * @deprecated
	 */
	_completeTask : function(name) {
		var _schedule = this._schedule;
		for (var i=0; i<_schedule.length; i++) {
			var item = _schedule[i];
			for (var m=1; m<item.length; m++) {
				if (item[m]==name) item.removeById(m--);
			}
			if (item.length==1) {
				var builder = item[0],msg = builder.message;
				builder.setSchema(this.getSchema(msg.schemaType));
				builder.setDictionary(this.getDictionary(msg.schemaType, msg.language));
				_schedule.removeById(i--);
			}
		}
	},

	/**
	 * @deprecated
	 */
	_createRequest : function(type, name) {
		if (type=="schema") RequestPool.add(
			new Request(PATH.getSchemaPath(name),new (Class.get("msg.ui.GetSchemaCallback"))(name)));
		if (type=="dict") RequestPool.add(
			new Request(PATH.getDictionaryPath(name),new (Class.get("msg.ui.GetDictionaryCallback"))(name)));
	},

	/**
	 * @protected
	 * 
	 * Add the schema to the manager
	 * @param {String} schema
	 */
	_addSchema : function(schema) {
		var str = schema;
		var err = new Error("MessageManager: Valid schema expected.");
		if (!str && str.length == 0) throw err;
		schema = eval('(' + str + ')');
		if (!schema || typeof schema!="object") throw err;
		if (!schema.childs) throw err;

		var name;
		if (schema.name == "Document") {
			// get common message name
			name = schema.childs[0].name;
		} else {
			// get header name
			name = schema.name;
		}

		this._schemas[name] = str;
	},

	/**
	 * @deprecated
	 *
	 * Add schema to manager
	 * @param {Object} schemaString the schema object.
	 */
	addSchema : function(schemaString) {
		var schema = eval('('+schemaString+')');
		if (!schema || typeof schema!="object" || schema.name!="Document") return;
		if (!schema.childs || schema.childs.length!=1) return;
		var name = schema.childs[0].name;
		this._schemas[name] = schemaString;
		this._completeTask(name);
		//return name;
	},

	/**
	 * @protected
	 *
	 * Add the dictionary to the manager
	 * @param {String} dict
	 */
	_addDictionary : function(dict) {
		if (!dict || dict.length == 0) throw new Error("MessageManager: Valid dictionary expected.");
		var dict = eval('(' + dict + ')');
		var name = dict.name;
		this._dicts[name] = dict;
	},

	/**
	 * @deprecated
	 *
	 * Add dictionary to the manager.
	 * @param {Object} dict the dictionary object.
	 */
	addDictionary : function(dict) {
		if (!dict || typeof dict!="object") return;
		var name = dict.name;
		this._dicts[name] = dict;
		this._completeTask(name);
		//return name;
	},
	
	/**
	 * @protected
	 * 
	 * Add a rule to the manager
	 * @param {String} rule
	 */
	_addRule : function(rule) {
		var err = new Error("MessageManager: Valid rule expected.");
		if (!rule || rule.length == 0) throw err;
		var rule = eval('(' + rule + ')');
		if (!rule.rules || typeof rule.rules != "object") throw err;
		var name = rule.name;
		this._rules[name] = rule;
	},
	
	/**
	 * Get the specific rules
	 * @param {String} name
	 * @returns {Object} the rules object
	 */
	getRule : function(name) {
		return this._rules[name] || null;
	},

	/**
	 * Get the specific dictionary
	 * @param {String} name
	 * @param {String} lang
	 * @returns {Object} dictionary object
	 */
	getDictionary : function(name, lang) {
		// As the element name, "." is not allowed. All "." will be replaced with "_".
		//return this._dicts[name + "_" + lang] || this._dicts[name.replace(".","_")+"_"+lang] || null;
		return this._dicts[name + "_dict_" + lang] || null;
	},

	/**
	 * Get the specific schema
	 * @param {String} name
	 * @returns {Object} schema object
	 */
	getSchema : function(name) {//debugger;
		var schema = this._schemas[name];
		// As the document name in FIN message, "." are replaced with "_".
		//if (!schema) schema = this._schemas[name.replace(".","_")];
		if (schema) schema = eval('('+schema+')');
		else schema = null;
		return schema;
	},

	requestSchema : function(builder) {
		var msg,schema,dict;
		if (msg = builder.message) {
			if (schema = this.getSchema(msg.schemaType)) {
				builder.setSchema(schema);
			} else {
				this._addTask(builder ,"schema" ,msg.schemaType);
			}
			if (dict = this.getDictionary(msg.schemaType, msg.language)) {
				builder.setDictionary(dict);
			} else {
				this._addTask(builder ,"dict", msg.schemaType+"_"+msg.language);
			}
		}
	},
	
	sendRequest : function(url, builder, callbackFnName) {
		RequestPool.add(
			new Request(url, new (Class.get("msg.ui.GetMessageCallback"))(builder, this, callbackFnName)));
	}
//}
});﻿/**
 * @import msg.ui.MessageManager;
 *
 * @class msg.ui.GetDictionaryCallback
 *
 * @author Leegorous
 */
Class.create("msg.ui.GetDictionaryCallback" ,null ,["http.RequestCallback"], {
	init : function(name) {
		this.name = name;
	},
	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
	},
	onResponseReceived:function(request,response) {
		var text = response.text;
		//if (DOM.browser.isIE) text = text.replace(/\\n/g,"\\\\r\\\\n");
		try {
			var obj = eval('('+text+')');
			MsgMgr.addDictionary(obj);
		} catch(e) {
			Console.error("Error occur in parsing dictionary '"+this.name+"'");
			throw e;
		}
		return;
	}
});/**
 * @class msg.ui.GetMessageCallback
 *
 * @author Leegorous
 */
Class.create("msg.ui.GetMessageCallback" ,null ,["http.RequestCallback"], {

	/**
	 * @constructor
	 *
	 * @param {MessageBuilder} builder
	 * @param {MessageManager} manager
	 * @param {String} fnName
	 */
	init : function(builder, manager, fnName) {
		this._callback = function(response) {
			manager[fnName](builder, response);
			this._callback = builder = manager = fnName = null;
		}
	},

	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
		this._callback(null);
	},

	onResponseReceived:function(request,response) {
		this._callback(response);
		/*
		var text = response.text;
		if (DOM.browser.isIE) {
			if (text.indexOf("\\r")<0) text = text.replace(/\\n/g,"\\\\r\\\\n");
		} else text = text.replace(/\\r\\n/g,"\\\\n");
		try {
			//var obj = eval('('+text+')');
			MsgMgr.addSchema(text);
		} catch(e) {
			Console.error("Error occur in parsing schema '"+this.name+"'");
			throw e;
		}
		return;
		*/
	}
});﻿/**
 * @author Leegorous
 */
Class.create("msg.ui.GetMessageXmlCallback" ,null ,["http.RequestCallback"] ,{
	init : function(builder) {
		this.builder = builder;
	},
	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
		this.builder.setXml(this.builder.XML_NOT_EXIST);
		delete this.builder;
	},
	onResponseReceived:function(request,response) {
		var xml = response.xml;
		this.builder.setXml(xml);
		delete this.builder;
	}
});﻿/**
 * @import msg.ui.MessageManager;
 *
 * @class msg.ui.GetSchemaCallback
 *
 * @author Leegorous
 */
Class.create("msg.ui.GetSchemaCallback" ,null ,["http.RequestCallback"], {
	init : function(name) {
		this.name = name;
	},
	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
	},
	onResponseReceived:function(request,response) {
		var text = response.text;
		if (DOM.browser.isIE) {
			if (text.indexOf("\\r")<0) text = text.replace(/\\n/g,"\\\\r\\\\n");
		} else text = text.replace(/\\r\\n/g,"\\\\n");
		try {
			//var obj = eval('('+text+')');
			MsgMgr.addSchema(text);
		} catch(e) {
			Console.error("Error occur in parsing schema '"+this.name+"'");
			throw e;
		}
		return;
	}
});﻿/**
 * @import ui.ChangeListener;
 * @import msg.ui.CommonContainerListener;
 *
 * @class msg.ui.ListPatternChangeListener
 * @extends msg.ui.CommonContainerListener
 * @implements ui.ChangeListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.ListPatternChangeListener", 
	"msg.ui.CommonContainerListener", 
	["ui.ChangeListener"],{
	onChange:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) {
			_container.setAlternativeFieldEnabled(sender.value==-1);
			_container.notify("change");
		}
		_container = null;
	}
});/**
 * @import msg.ui.SimpleContainer;
 *
 * @abstract
 * @class msg.ui.PatternContainer
 * @extends msg.ui.SimpleContainer
 *
 * It is the super class of all PatternContainers.
 *
 * @author Leegorous
 */
Class.create("msg.ui.PatternContainer","msg.ui.SimpleContainer",null,{
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyPatternImpl"),
	//_destroyPatternImpl:function() {
	destroy : function() {
		delete this._field;
		//this._destroySimpleImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Fill the value.
	 * This is the abstract method.
	 */
	fill : function(node) {
		if (node = node.firstChild) {
			if (this.bit & ContainerTemplates.isOptional) {
				this._container.firstChild.firstChild.checked=true;
				//defaultChecked=true;
				this.setEnabled(true);
			}
			this.setValue(node.nodeValue);
			//this.validate();
		}
	},
	/**
	 * Get the value of this container;
	 */
	getValue:function() {
		return this._field.value;
	},
	init:function(_node,_parentContainer) {
		//this._initPatternImpl(_node,_parentContainer);
	//},
	//_initPatternImpl : function(_node,_parentContainer) {
		//this._initSimpleImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//-- checkup whether this pattern container is optional
		//this.bit|=((this.enabled=(_node.minOccurs!=0))?0:ContainerTemplates.isOptional);
		//this.bit|=(~(_node.minOccurs?parseInt(_node.minOccurs):1) & ContainerTemplates.isOptional);
		this.bit|=(this.node.FinFormat?ContainerTemplates.hasFormat:0);
		//this.bit|=((this.format=Class.get("msg.ui.PatternContainer").findFormat(_node)).length>0)?ContainerTemplates.hasFormat:0;
		this._container=this.createContainer();
		if (!this._container) Console.put('name:'+this.name+' bit:'+this.bit);
		//this._container._container=this;
		this._container._ctnId=this.id;
		//var _a=Class.get("msg.ui.SimpleContainer")._valueSequence;_a.push(this._container.childNodes[1]);_a.push(this.longName||this.name);
		this.setLabel();
		//this._container.childNodes[1].appendChild($doc.createTextNode(this.longName||this.name));
		if (!(this.bit & ContainerTemplates.isOptional)) this.initBody();
		//if (_parentContainer) _parentContainer.add(this);
	},
	/**
	 * Associate the container object with the specific Element object.
	 * And fill the format info and description if necessary.
	 */
	//initBody : new MethodAlias("_initBodyPatternImpl"),
	//_initBodyPatternImpl:function() {
	initBody : function() {
		this._body=this._container.lastChild;
		this._field=this._body.firstChild;
		this._field._ctnId = this.id;
		this.setInfo();
	},
	/**
	 * Enable this container
	 */
	setEnabled:function(enabled) {
		if (this.enabled=!!enabled) {
			if (!this._body) {
				this.createBody();
				this.initBody();
			}
			this._body.style["display"]="";
		} else if (this._body) this._body.style["display"]="none";

		var checked = this._container.firstChild.firstChild.checked;
		if (this.enabled != checked) this._container.firstChild.firstChild.checked = this.enabled;

		this.notify("enabled");
	},
	/**
	 * Set the format info and description if necessary.
	 */
	setInfo:function() {
		if (this.bit & ContainerTemplates.hasFormat) this._body.childNodes[1].title=this.node.FinFormat;
		// the innerHTML has performance problem
		if (this.bit & ContainerTemplates.hasDescription) this._body.lastChild.innerHTML=this.description;
	},
	/**
	 * Set the extra infomation.
	 * @param {Object} info the infomation want to show
	 * @param {Boolean} isError true if there is an error infomation
	 */
	setExtraInfo:function(info,isError) {
		if (info) {
			if (info==="") this.setExtraInfo(false);
			if (typeof info=="string") info=$doc.createTextNode(info);
			if (this._extraInfo) {
				this._extraInfo.style["display"]="";
				if (this._extraInfo.firstChild) DOM.detachChilds(this._extraInfo);
			} else this._body.appendChild(this._extraInfo=ContainerTemplates.get("FieldExtraInfo"));
			if (isError) this._extraInfo.className="fieldErrorInfo";
			else this._extraInfo.className="fieldExtraInfo";
			this._extraInfo.appendChild(info);
		} else {
			if (this._extraInfo) {
				if (this._extraInfo.style["display"]!="none") this._extraInfo.style["display"]="none";
				DOM.detachChilds(this._extraInfo);
			}
		}
	},
	/**
	 * Set the label of this container.
	 */
	setLabel:function() {
		this._container.childNodes[1].innerHTML = this.longName||this.name;
		this._container.title = (this.longName||this.name)+" "+this.description;
	},
	/**
	 * Set the value of this container.
	 */
	setValue:function(v) {
		//-- if the body of this container has not been initialized, initialize it first.
		//if ((this.bit & ContainerTemplates.isOptional) && !this._field) this.initBody();
		if (this._field) this._field.value=v;
	},
	showMatch : function(v) {
		var result = false;
		if (v.length==0) result = true;
		if (this.match(v)) result = true;
		if (result) this._container.style["display"]="";
		else this._container.style["display"]="none";
		return result;
	},
	showOptional : function(show) {
		if (!this.enabled) {
			if (show) this._container.style["display"]="";
			else this._container.style["display"]="none";
		}
	},
	/**
	 * Set this container writable or read only.
	 * @param {Boolean} readonly true if this container should be readonly.
	 */
	//setReadOnly : new MethodAlias("_setReadOnlyPatternImpl"),
	//_setReadOnlyPatternImpl : function(/* Boolean: */readonly) {
	setReadOnly : function(/* Boolean: */readonly) {
		this.readOnly = readonly = !!readonly;
		if (this.node.readOnly) this.readOnly = readonly = true;
		if (this.bit & ContainerTemplates.isOptional) this._setOptionalCheckboxReadOnly(readonly);
		if (this.enabled) this._setFieldReadOnly(readonly);
	},
	//toXML : new MethodAlias("_toXMLPatternImpl"),
	//_toXMLPatternImpl : function() {
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitPattern(this);
		/*
		var xml = [];
		if (this.enabled) {
			// I don't know why it have to do such replace in original script.
			// But I keep it.
			var nodeName = this.name.replace(/_\d+/,'');
			xml.push('<'+nodeName);//+' type="SwiftSubFieldElement" typeName="'+this.node.type+'"'
			//-- output FinFormat
			//if (this.node.FinFormat) xml.push(' FinFormat="'+this.node.FinFormat+'"');
			//-- output SepPrefix
			//if (this.node.SepPrefix) xml.push(' prefix="'+this.node.SepPrefix.replace(/\n/g,"&#10;")+'"');
			//-- output SepSuffix
			//if (this.node.SepSuffix) xml.push(' suffix="'+this.node.SepSuffix.replace(/\n/g,"&#10;")+'"');
			xml.push(">"+this.getValue()+"</"+nodeName+">");
		}
		return xml.join("");*/
	},
	
	/**
	 * An interface method. It will be invoked while doing validation.
	 * It can be overrided.
	 */
	//validate : new MethodAlias("_validatePatternImpl"),
	//_validatePatternImpl:function() {
	validate : function() {
		if (!this._field) return false;
		var v=this.getValue(),_a,_b=null,_c=false;
		if (_a=this.node.restriction) {
			if (typeof _a.minLength =="number" && v.length<_a.minLength) {
				_b="Minimal length is "+_a.minLength;_c=true;
			}
			if (!_c && (_a=_a.pattern)) {
				var re = new RegExp("^"+_a+"$");
				if (_c=!re.test(v)) _b="Invalid input.";
			}
		}
		/*
		if ((_a=this.node.restriction) && (_a=_a.pattern)) {
			var re = new RegExp("^"+_a+"$");
			if (_c=!re.test(v)) _b="Invalid input.";
		}*/
		this.setExtraInfo(_b,_c);
		return !_c;
	},
	
	_setOptionalCheckboxReadOnly : function(/* Boolean: */readonly) {
		this._container.firstChild.firstChild.disabled = readonly;
	},
	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		this._field.readOnly = readonly;
	}
	
	//$tatic:{
		// deprecated
		/*
		findFormat:function(_node) {
			if (_node.format) return _node.format;
			return this._initFormat(_node);
		},
		_initFormat:function(_node) {
			var format='',restriction;
			if (_node.annotation) {
				format=_node.annotation.FinFormat || '';
			} else if ((restriction=_node.restriction) && restriction.annotation) {
				format=restriction.annotation.FinFormat || '';
			}
			restriction=null;
			return _node.format=format;
		}*/
	//}
});﻿/**
 * @import msg.ui.PatternContainer;
 *
 * @class msg.ui.ListPatternContainer
 * @extends msg.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.ListPatternContainer","msg.ui.PatternContainer",null,{
	createAlternativeField:function() {
		var _a=$doc.createElement("input");
		_a.type="text";
		_a.className="field";
		DOM.sinkEvent(_a,ContainerEvents.getEventId("CommonTextPattern"));
		if (this.node.restriction.maxLength) _a.maxLength=this.node.restriction.maxLength;
		this._field.parentNode.insertBefore(_a,this._field.nextSibling);
	},
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("ListPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("ListPatternContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyListPatternImpl"),
	//_destroyListPatternImpl:function() {
	destroy : function() {
		delete this._choiceField;
		//this._destroyPatternImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Filtrate the input
	 * @param {Object} event the event object
	 * @param {int} charCode the charCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateInput:function(event,charCode,modifiers) {},
	/**
	 * Filtrate the text
	 * @param {Object} event the event object
	 * @param {int} keyCode the keyCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateText:function(event,keycode,modifiers) {},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initListPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//_initListPatternImpl : new MethodAlias("_initPatternImpl"),
	initBody:function() {
		this[arguments.callee.parent]();
		this._choiceField=this._field;
		var enums=this.node.restriction.enumeration;
		var dictEnums=null,name;
		if (this.dict) dictEnums = this.dict.enumeration;
		for (var i=0,j=enums.length; i<j; i++) {
			//this.addOption(enums[i],enums[i]);
			if (dictEnums) name = dictEnums[enums[i]];
			else name = enums[i];
			this.addOption(name,enums[i]);
		}
		//this.addOption("Other...",-1);
		enums=null;
	},
	setAlternativeFieldEnabled:function(enabled) {
		var skipValidate = false;
		if (enabled) {
			if (!this._choiceField.nextSibling || this._choiceField.nextSibling.nodeName!="INPUT") {
				this.createAlternativeField();
				skipValidate=true;
			}
			this._field=this._choiceField.nextSibling;
			this._field.style["display"]="";
		} else if (this._field!=this._choiceField) {
			this._field.style["display"]="none";
			this._field=this._choiceField;
		}
		if (!skipValidate) this.validate();
	},
	setValue : function(v) {
		if (this._field) {
			this._field.value=v;
			if (this._field.value!=v) {
				this._field.value=-1;
				this.setAlternativeFieldEnabled(true);
				this._field.value=v;
			}
			this.notify("change");
		}
	},
	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		this._choiceField.disabled = readonly;
		if (this._field!=this._choiceField) this._field.readOnly = readonly;
	},
	impl:{
		moz:{
			addOption:function(name,value) {
				this._field.add(new Option(name,value),null);
			}
		},
		ie:{
			addOption:function(name,value) {
				this._field.add(new Option(name,value));
			}
		}
	}
});/**
 * @import msg.ui.SimpleContainer;
 *
 * @class msg.ui.MainContainer
 * @extends msg.ui.SimpleContainer
 *
 * It is the entry of the message container.
 * @author Leegorous
 */
Class.create("msg.ui.MainContainer","msg.ui.SimpleContainer",null,{
	$tatic : {
		VIEW : 1,
		EDIT : 2,
		CREATE_TEMPLATE : 4
	},
	status : 2,// EDIT
	add : function(child) {
		var name = child.name || this.ownerDocument.generateContainerName();
		this.childs[name] = child;
		this._body.appendChild(child._container);
	},
	createContainer : function() {
		var _a = $doc.createElement("div");
		_a.className="MainContainer";
		return _a;
		//return ContainerTemplates.get("SequenceItemContainer"+this.bit);
	},
	createBody : function() {},
	
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyMainImpl"),
	//_destroyMainImpl : function() {
	destroy : function() {
		this.eachChild(function(child) {
			child.destroy();
		});
		delete this.childs;
		//this._destroySimpleImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Iterate over all childs in this container
	 * @param {Method} iterator The action
	 */
	eachChild : function(iterator) {
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) iterator(child);
		}
		child = null;
	},
	
	/**
	 * Fill value to the container
	 *
	 * @param {Node} node the xml node
	 */
	fill : function(node) {
		var nodes, child, result = false;

		if (XML.getLocalName(node) != this.node.name) {
			throw new Error("Node ["+this.node.name+"] expected.");
		}

		// exit if no child nodes
		if (!(nodes = node.childNodes)) return;

		this.ownerDocument.nsPrefix = node.prefix;

		for (var i=0,j=nodes.length; i<j; i++) {
			// try to find the container as the nodeName
			var nodeName = XML.getLocalName(nodes[i]);
			child = this.childs[nodeName];
			if (child) {
				child.fill(nodes[i]);
				result = true;
			} else {
				if (!this._fillVirtualChilds(nodes[i])) 
					Console.error("Error occur in filling " + nodeName);
			}
		}
		node=nodes=child=null;
		return result;
	},
	
	/**
	 * @private
	 * Try to fill the value in virtual childs
	 *
	 * @param {Node} node the xml node
	 * @returns {Boolean}
	 */
	_fillVirtualChilds : function(node) {
		var childs = this.childs, result = false;
		for (var i in childs) {
			if (childs[i].isVirtual()) {
				if (childs[i]._fill(node)) result = true;
			}
		}
		node = null;
		return result;
	},
	
	/**
	 * @constructor
	 */
	init : function(_node,_parentContainer) {
	//	this._initMainImpl(_node,_parentContainer);
	//},
	//_initMainImpl : function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initSimpleImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//this._ownerMessage=this;
		this.childs = {};
		this._container = this.createContainer();
		//this._container._container = this;
		this._container._ctnId = this.id;
		this.initBody();
		//if (_parentContainer) _parentContainer.add(this);
	},
	
	initBody : function() {
		this._body = this._container;
		//UIObjectTree.walk(this.node,this);
		//CtnBuilder.buildChild(this.node, this);
		this.ownerDocument.containerBuilder.buildChild(this.node, this);
	},
	showMatch : function(v) {
		v = v || "";
		var result = false;
		this.eachChild(function(child) {
			if (child.showMatch(v)) result=true;
		});
		return result;
	},
	showOptional : function(/* Boolean: */show) {
		show = !!show;
		this.setReadOnly(!show);
		this.eachChild(function(child) {
			child.showOptional(show);
		});
		/*
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.showOptional(show);
		}
		child = null;*/
	},
	
	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		if (!path || path.length==0 || !this.enabled) return null;
		var idx, child;
		if (path.indexOf(".") == 0) {
			if ((path = path.substring(1)).length == 0) return [this];
		}
		
		if ((idx = path.indexOf("/")) > 0) {
			if (child = this.childs[path.substring(0,idx)]) 
				return child.select(path.substring(idx + 1));
		} else {
			if (child = this.childs[path]) return child.select(".");
		}
	},

	/**
	 * Set main container and all childs writable or read only.
	 * @param {Boolean} readonly true if main container and all childs should be readonly.
	 */
	setReadOnly : function(/* Boolean: */readonly) {
		//var child;
		this.readOnly = !!readonly;
		this.eachChild(function(child) {
			child.setReadOnly(readonly);
		});
		/*
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.setReadOnly(readonly);
		}
		child = null;*/
	},
	/**
	 * Get the XML string
	 */
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitMessage(this);
		/*
		var xml=[], childsXml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0) {
				xml.push('<'+this.name);//+' type="SwiftMessageElement" typeName="'+this.node.type+'"'
				xml.push('>');
				xml.push(childsXml);
				xml.push('</'+this.name+'>');
			}
		}
		return xml.join("");*/
	},
	validate : function() {
		//var child,
		var result=true;
		this.eachChild(function(child) {
			if (child.enabled && !child.validate()) result = false;
		});
		/*
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name && child.enabled) {
				if (!child.validate()) result = false;
			}
		}*/
		return result;
	}
});/**
 * @class msg.ui.MessageBuilder
 * 
 * @author Leegorous
 */
Class.create("msg.ui.MessageBuilder",null ,null, {

	XML_NOT_EXIST : {},
	
	bit : 3,

	_bit : 0,

	message : null,
	
	/**
	 * @constructor
	 *
	 * @param {Object} message the message info
	 * @param {Object} impl the implements
	 */
	init : function(message, impl) {
		if (impl) Object.extend(this, impl);
		//this.bit = 7;
		this.message = message;
		this.requestSchema();
		this.requestMessageXml();

	},

	prepareWrapper : function() {
		var msg = this.message;
		if (msg.wrapper) {
			if (typeof msg.wrapper=="string") msg.wrapper=$(msg.wrapper);
			DOM.detachChilds(msg.wrapper);
		} else {
			msg.wrapper = $doc.createElement('div');
			$doc.body.appendChild(msg.wrapper);
		}
		msg = null;
	},
	
	createMessage : Abstract.empty,
	
	getXmlRoot : function(xml) {
		return xml.documentElement;
	},
	
	fillMessage : function() {//debugger;
		if (!this.message.xmlId) return;
		var info = this.message;
		var msg;

		if (info.document) {
			if (info.xml) {//debugger;
				msg = info.document.getMessage();
				if (info.xml == this.XML_NOT_EXIST) {
					msg.setReadOnly(info.readOnly);
				} else {
					var root = this.getXmlRoot(info.xml);
					//msg.nsPrefix = root.prefix;
					var nodes = root.childNodes;
					for (var i=0,j=nodes.length; i<j; i++) {
						if (nodes[i].nodeType!=1) continue;
						msg.fill(nodes[i]);
					}
					msg.validate();
					msg.setReadOnly(info.readOnly);
				}
			}
		}
		// if the document container and xml data are ready, then filling process start
		/*
		if (msg.document && msg.xml) {
			var nodes = msg.xml.childNodes;
			for (var i=0,j=nodes.length; i<j; i++) {
				if (nodes[i].nodeType!=1) continue;
				msg.document.message.fill(nodes[i]);
			}
			msg.document.message.validate();
			msg.document.message.setReadOnly(msg.readOnly);
		}*/
	},
	
	requestSchema : function() {
		this.message.messageManager.requestSchema(this);
	},
	
	requestMessageXml : Abstract.empty,/*function() {
		if (this.message.xmlId) {
			RequestPool.add(new Request(PATH.getMessageXmlPath(this.message.xmlId), new (Class.get("msg.ui.GetMessageXmlCallback"))(this)));
		}
	},*/
	
	build : function() {
		this.prepareWrapper();
		var begin=(new Date()).getTime();
		this.createMessage();
		var time = (new Date()).getTime() - begin;
		Console.put("Build (and fill) Document in "+ time + " ms.");
	},
	
	getDictionary : function() {
		return this.message.dict;
	},

	getSchema : function() {
		return this.message.schema;
	},
	
	getRule : function() {
		return this.message.rule;
	},
	
	setDictionary : function(dict) {
		this.message.dict = dict;
		this._bit |= 1;
		if (this.bit == this._bit) this.build();
	},
	
	setSchema : function(schema) {
		this.message.schema = schema;
		this._bit |= 2;
		if (this.bit == this._bit) this.build();
	},

	setRule : function(rule) {
		this.message.rule = rule;
		this._bit |= 4;
		if (this.bit == this._bit) this.build();
	},
	
	setXml : function(xml) {
		this.message.xml = xml;
		try {
			this.fillMessage();
		} catch(e) {
			$win.alert("MessageBuilder:fillXml" + e.message);
		}
	}
});/**
 * @import msg.ui.CommonContainerListener;
 * @import ui.ClickListener;
 *
 * @class msg.ui.NewOccurrenceListener
 * @extend msg.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.NewOccurrenceListener" ,
	"msg.ui.CommonContainerListener" ,
	["ui.ClickListener"] ,{
	
	onClick:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.newOccur();
		_container=null;
	}
});/**
 * @import msg.ui.SimpleContainer;
 *
 * @class msg.ui.OptionContainer
 * @extends msg.ui.SimpleContainer
 */
Class.create("msg.ui.OptionContainer","msg.ui.SimpleContainer",null,{
	/**
	 * Add a child to this container.
	 * @param {SimpleContainer} child The child container want to be added.
	 */
	add : function(child) {
		var name = child.name || this.ownerDocument.generateContainerName();
		this.childs[name]=child;
		this._setChild(child);
	},

	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody : function() {
		var _a = $doc.createElement("div");
		_a.className = "compositeBody";
		this._container.appendChild(_a);
		_a = null;
	},
	/**
	 * Create a new child with the specific name.
	 * @param {String} name The child name
	 */
	createChild : function(name) {
		var _a = this.node.childs,_b;
		for (var i=0,j=_a.length; i<j; i++) {
			if ((_b=_a[i]).name==name) break;
		}
		_a=null;
		//UIObjectTree.createContainer(_b,this);
		//CtnBuilder.build(_b, this);
		this.ownerDocument.containerBuilder.build(_b, this);
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer : function() {
		return ContainerTemplates.get("OptionContainer"+this.bit);
	},
	/**
	 * Create a selector for this OptionContainer.
	 */
	createSelector : function() {
		var _a = ContainerTemplates.get("OptionContainerSelector");
		_a.firstChild.setAttribute("__evt",ContainerEvents.getEventId("OptionContainerSwitcher"));
		DOM.insertAfter(_a, this._container.firstChild.lastChild.firstChild);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyOptionImpl"),
	//_destroyOptionImpl : function() {
	destroy : function() {
		this.eachChild(function(child) {
			child.destroy();
		});
		delete this._selector;
		delete this.curChild;
		delete this.childs;
		//this._destroySimpleImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Iterate over all childs in this container
	 * @param {Method} iterator The action
	 */
	eachChild : function(iterator) {
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) iterator(child);
		}
		child = null;
	},

	/**
	 * Fill the xml data to this container
	 * @param {Object} node The xml data node
	 */
	fill : function(node) {
		var nodes,name;
		if (!(nodes=node.childNodes)) return;
		for (var i=0,j=nodes.length; i<j; i++) {
			if (nodes[i].nodeType!=1) continue;
			if (!this.enabled) {
				this._container.firstChild.firstChild.firstChild.checked=true;
				this.setEnabled(true);
			}

			this._fill(nodes[i]);
			/*
			//this.setEnabled(true);
			name=nodes[i].nodeName;
			if (this._selector.setValue(name)) {
				this.setChild(name);
				this.childs[name].fill(nodes[i]);
				//return true;
			} else {
				// if this container is virtual, the filling action maybe just a try
				// it will not be an error.
				if (!this.isVirtual()) Console.error("Err: container for [" + name + "] not found.");
				//return false;
			}*/
		}
	},
	
	/**
	 * @protected
	 * Fill the xml data to the specific child
	 *
	 * @param {Node} node the xml data node
	 * @returns {Boolean} true if there is an match filling
	 */
	_fill : function(node) {//debugger;
		// get the nodeName
		var name = XML.getLocalName(node);

		if (this.includeChild(name)) {
			this._selector.setValue(name);
			this.setChild(name);
			this.childs[name].fill(node);
			return true;
		} else {//!this._fillVirtualChilds(node) && 
			// if this container is virtual, the filling action maybe just a try
			// it will not be an error.
			if (!this.isVirtual()) Console.error("Err: container for [" + name + "] not found.");
			return false;
		}
	},
	
	/**
	 * Test whether this container contains a child with the specific name
	 * 
	 * @param {String} name
	 * @returns {Boolean}
	 */
	includeChild : function(name) {
		var childs = this.node.childs;
		for (var i = 0, j = childs.length; i < j; i++) {
			if (childs[i].name == name) return true;
		}
		return false;
	},

	init : function(_node,_parentContainer) {
		//this._initOptionImpl(_node,_parentContainer);
	//},
	//_initOptionImpl : function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initSimpleImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this.childs = {};
		this._container = this.createContainer();
		//this._container._container = this;
		this._container._ctnId = this.id;
		this.setLabel();
		//if (this.bit & ContainerTemplates.hasDescription) this.setInfo();
		if (!(this.bit & ContainerTemplates.isOptional)) {
			this.initSelector();
			this.initBody();
		}
		//if (_parentContainer) _parentContainer.add(this);
	},
	
	initBody : function() {
		this._body=this._container.lastChild;
		//UIObjectTree.walk(this.node,this);
	},
	initSelector : function() {
		this._selector = new (Class.get("ui.ListBox"))(this._container.firstChild.lastChild.childNodes[1].firstChild);
		var _b = this.node.childs, _n,_v;
		this._selector.addOption("","-1");
		for (var i=0,j=_b.length; i<j; i++) {
			_v = _b[i].name;
			_n = this.dict ? (this.dict[_v].longName || _v) : _v;// && this.dict[_v]
			this._selector.addOption(_n, _v);
		}//debugger;
		//this._selector.setValue(-1);
		_b = null;
	},
	
	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		if (!path || path.length==0 || !this.enabled) return null;
		var idx, child;
		if (path.indexOf(".") == 0) {
			if ((path = path.substring(1)).length == 0) return [this];
		}
		
		if ((idx = path.indexOf("/")) > 0) {
			if (child = this.childs[path.substring(0,idx)]) 
				return child.select(path.substring(idx + 1));
		} else {
			if (child = this.childs[path]) return child.select(".");
		}
	},
	
	/**
	 * Set the child with the specific name as the current child
	 * 
	 * @param {String} name
	 */
	setChild : function(name) {
		if (!this._body) {
			this.createBody();
			this.initBody();
		}
		var selectedValue = this._selector.getValue();
		if (selectedValue != name) this._selector.setValue(name);
		if ("-1"==name && this._body) {
			return this._setChild(null);
			//this.curChild = null;
			//return DOM.detachChilds(this._body);//this._body.style["display"]="none";
		}
		if (!this.childs[name]) return this.createChild(name);
		this._setChild(this.childs[name]);
	},
	setEnabled : function(enabled) {
		if (this.enabled=!!enabled) {
			if (!this._selector) {
				this.createSelector();
				this.initSelector();
			}
			if (this._body && this._body.hasChildNodes()) {
				//this.createBody();
				//this.initBody();
				this._body.style["display"]="";
			}
			this._selector.show();//.style["display"]="";
		} else {
			if (this._selector) this._selector.hide();//.style["display"]="none";
			if (this._body) this._body.style["display"]="none";
		}
		this.notify("enabled");
	},
	/**
	 * @deprecated
	 */
	setInfo : function() {
		this._container.firstChild.lastChild.lastChild.innerHTML=this.description;
	},
	setLabel : function() {
		// set the label only this is not virtual
		if (!this.isVirtual()) {
			//var displayName = this.isVirtual() ? "" : (this.longName || this.name);
			var displayName = this.longName || this.name;
			this._container.firstChild.lastChild.firstChild.innerHTML = displayName;
			if (this.bit & ContainerTemplates.hasDescription) 
				this._container.firstChild.lastChild.lastChild.innerHTML=this.description;
			this._container.title = displayName + " " + this.description;
		}
	},
	showMatch : function(v) {
		var result = false;
		if (v.length==0) result = true;
		if (this.match(v)) {
			result = true;
			v = "";
		}
		if (this.enabled && this.curChild && this.curChild.showMatch(v)) result = true;
		if (result) this._container.style["display"]="";
		else this._container.style["display"]="none";
		return result;
	},
	showOptional : function(show) {
		if (this.enabled) {
			if (show) this._container.firstChild.style["display"]="";
			else this._container.firstChild.style["display"]="none";
		} else {
			if (show) {
				if (this._container.style["display"]=="none") return this._container.style["display"]="";
			} else return this._container.style["display"]="none";
		}
		if (this.curChild) this.curChild.showOptional(show);
	},
	/**
	 * Set this container and its current childs writable or read only.
	 * @param {Boolean} readonly true if this container and its current childs should be readonly.
	 */
	setReadOnly : function(/* Boolean: */readonly) {
		this.readOnly = readonly = !!readonly;
		if (this.bit & ContainerTemplates.isOptional) this._setOptionalCheckboxReadOnly(readonly);
		if (this._selector) this._selector.setReadOnly(readonly);
		if (this.curChild) this.curChild.setReadOnly(readonly);
	},
	/**
	 * Get the XML string
	 */
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitOption(this);
		/*
		var xml=[], childsXml;
		if (this.enabled && this.curChild) {
			childsXml = this.curChild.toXML();
			if (childsXml.length>0 || 
				this.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				if (this.node.virtual) xml.push(childsXml);
				else {
					xml.push('<'+this.name);//+' type="SwiftChoiceElement" typeName="'+this.node.type+'"'
					xml.push('>');
					xml.push(childsXml);
					xml.push('</'+this.name+'>');
				}
			}
		}
		return xml.join("");*/
	},
	validate : function() {//debugger;
		var i = ("" + this._selector.getValue()),result=true;
		if (i.length == 0 || i=="-1") {//selectedIndex
			Console.error("This OptionField '"+this.name+"' should be filled.");
		} else {
			if (this.childs[i].enabled) {
				if (!this.childs[i].validate()) result=false;
			}
		}
		return result;
	},

	/**
	 * @private
	 *
	 * Set the current child
	 *
	 * @param {Container} child
	 */
	_setChild : function(child) {
		if (this.curChild === child) return;

		this.curChild = child;
		// clear all child while current child is null
		if (child == null) {
			this.notify("change");
			return DOM.detachChilds(this._body);
		}

		if (this._body.firstChild) this._body.replaceChild(child._container,this._body.firstChild);
		else this._body.appendChild(child._container);
		this._body.style["display"]="";

		this.notify("change");
		/*
		DOM.detachChilds(this._body);
		this._body.appendChild(child._container);*/
	},
	
	/**
	 * @private
	 */
	_setOptionalCheckboxReadOnly : function(/* Boolean: */readonly) {
		this._container.firstChild.firstChild.firstChild.disabled = readonly;
	},

	lazyAddingListener : function(path, type, listener) {
		if (!path || path.length == 0 || !type || type.length == 0 || !listener) return;
		var fields;
		if (fields = this.select(path)) {
			for (var i=0,j=fields.length; i<j; i++) {
				fields[i].addListener(type, listener);
			}
		} else {
			var parentPath = path;
			while((parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"))).length > 0) {
				if (fields = this.select(parentPath)) {
					fields.each(function(field) {
						field._hangUpAddingListener(path.substring(parentPath.length + 1), type, listener);
					});
					break;
				}
			}
		}
	},

	/**
	 * Hang up the process of add listener
	 *
	 * @param {String} path
	 * @param {String} type
	 * @param {Object} listener
	 */
	_hangUpAddingListener : function(path, type, listener) {
		var _this;
		// when disabled
		if (!this.enabled) {
			_this = this;
			var _eListener = {
				onEnabled : function(enabled) {
					if (!enabled) return;
					_this._hangUpAddingListener(path, type, listener);
					_this.removeListener("enabled", _eListener);
					_eListener = _this = null;
				}};
			this.addListener("enabled", _eListener);
		} else {
			var targetChild, idx;
			if ((idx = path.indexOf("/")) != -1) targetChild = path.substring(0, idx);
			else targetChild = path;
			if (this.curChild && this.curChild.name == targetChild) {
				this.lazyAddingListener(path, type, listener);
			} else {
				_this = this;
				var _cListener = {
					onChange : function(container) {
						if (container.curChild && container.curChild.name == targetChild) {
							_this._hangUpAddingListener(path, type, listener);
							_this.removeListener("change", _cListener);
							_cListener = _this = null;
						}
					}
				};
				this.addListener("change", _cListener);
			}
		}
	}
});﻿/**
 * @import ui.ChangeListener;
 * @import msg.ui.CommonContainerListener;
 *
 * @class msg.ui.OptionContainerChangeListener
 * @extends msg.ui.CommonContainerListener
 * @implements ui.ChangeListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.OptionContainerChangeListener", 
	"msg.ui.CommonContainerListener", 
	["ui.ChangeListener"],{
	
	onChange:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.setChild(sender.value);
		_container = null;
	}
});/**
 * @import msg.ui.CommonContainerListener;
 * @import ui.ClickListener;
 *
 * @class msg.ui.RemoveSequenceItemListener
 * @extend msg.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.RemoveSequenceItemListener" ,
	"msg.ui.CommonContainerListener" ,
	["ui.ClickListener"],{
	
	onClick:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.remove();
		_container=null;
	}
});/**
 * @import msg.ui.SimpleContainer;
 *
 * @class msg.ui.SequenceContainer
 * @extends msg.ui.SimpleContainer
 */
Class.create("msg.ui.SequenceContainer","msg.ui.SimpleContainer",null,{
	/**
	 * Total created childs.
	 */
	created : 0,
	/**
	 * Add a child to this container.
	 * @param {SimpleContainer} child The child container want to be added.
	 */
	add : function(child) {
		this.childs.push(child);
		this._body.appendChild(child._container);
	},
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody : function() {
		this._container.appendChild(ContainerTemplates.get("SequenceBody"));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer : function() {
		return ContainerTemplates.get("SequenceContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroySequenceImpl"),
	//_destroySequenceImpl : function() {
	destroy : function() {
		this.eachChild(function(child) {
			child.destroy();
		});
		delete this._expand;
		delete this.childs;
		//this._destroySimpleImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Iterate over all childs in this container
	 * @param {Method} iterator The action
	 */
	eachChild : function(iterator) {
		this.childs.each(iterator);
	},
	/**
	 * Expand or collapse this container.
	 * If this container contains nothing, this method has no effect.
	 *
	 * @param {Boolean} bln true to expand, false to collapse
	 */
	expand : function(bln) {
		if (!this._body || this.created==0) return;
		if (this.expanded=!!bln) {
			this._expand.className="expandBtn";
			this._expand.title="Collapse";
			if (this._body.hasChildNodes()) {
				this._body.style["display"]="";
			}
		} else {
			this._expand.className="collapseBtn";
			this._expand.title="Expand";
			this._body.style["display"]="none";
		}
	},
	/**
	 * Fill the xml data to this container
	 * @param {Object} node The xml data node
	 */
	fill : function(node) {
		this.filled = this.filled || 0;
		if (!this.enabled) {
			if (!this.increasable && (this.bit & ContainerTemplates.isOptional))
				this._container.firstChild.firstChild.firstChild.checked=true;
			this.setEnabled(true);
		}
		if (this.filled >= this.created) this.newOccur();
		var _a;
		if (_a = this.childs[this.filled]) {
			_a.fill(node);
			this.filled++;
		} else ErrorStack.push(this.name+" can not have over "+this.maxOccurs+" childs.");
	},
	init : function(_node,_parentContainer) {
		//this._initSequenceImpl(_node,_parentContainer);
	//},
	//_initSequenceImpl : function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initSimpleImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//this.created = 0;
		this.minOccurs = (_node.minOccurs>-1)?_node.minOccurs:1;
		this.maxOccurs = _node.maxOccurs || 1;
		this.bit|=((this.increasable = ((this.maxOccurs>1 && this.maxOccurs>this.minOccurs) || this.maxOccurs=="unbounded"))?ContainerTemplates.increasable:0);
		if (this.maxOccurs!=1) this.bit|=ContainerTemplates.hasDescription;
		this.childs = [];
		this._container = this.createContainer();
		//this._container._container = this;
		this._container._ctnId = this.id;
		this._expand=this._container.firstChild.lastChild.firstChild;
		this.setLabel();
		if (!(this.bit & ContainerTemplates.isOptional)) this.initBody();
		//if (_parentContainer) _parentContainer.add(this);
	},
	
	initBody : function() {
		this.createBody();
		this._body = this._container.lastChild;
		var i=0;
		do {
			this.newOccur();
			i++;
		} while(i<this.minOccurs);
	},

	/**
	 * Generate a new instance of this sequence container
	 */
	newOccur : function() {
		// do nothing when this container is read only
		if (this.readOnly) return false;

		if (!this._body) {
			this.createBody();
			this._body = this._container.lastChild;
		}
		if (this.created<this.maxOccurs) {
			this.created++;

			/*
			 * A trick here to make the container builder to build the proper container
			 * by modifying the base of the node
			 */
			var oldBase = this.node.base;
			this.node.base += "Item";
			this.ownerDocument.containerBuilder.build(this.node, this);
			// recovery the base
			this.node.base = oldBase;

			//new (Class.get("msg.ui.SequenceItemContainer"))(this.node,this);
			this.updateCreated();
		}
		this.setEnabled(true);
		//if (!this.expanded) this.expand(true);
	},
	/**
	 * Remove the specific child if created childs are more than its minimum.<br/>
	 * If this container is readonly, it has no effect.
	 *
	 * @param {SimpleContainer} child the specific child want to be removed.
	 */
	remove : function(child) {
		if (this.readOnly) return false;
		if (this.created>=this.minOccurs) {
			this.childs.remove(child);
			this.created--;
			this._body.removeChild(child._container);
			for (var i=1; i<=this.childs.length; i++) {
				this.childs[i-1].setLabel(i);
			}
			this.updateCreated();
			if (this.created<this.minOccurs) this.newOccur();
			if (this.created==0) this.setEnabled(false);
			return true;
		}
		return false;
	},
	
	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		if (!path || path.length==0 || !this.enabled) return null;
		var idx, result = [], child;

		for (var i=0, j=this.childs.length; i<j; i++) {
			if (child = this.childs[i].select(path)) result = result.concat(child);
		}

		if (result.length > 0) return result;
		else return null;
		/*
		if ((idx = path.indexOf("/")) > 0) {
			var path0 = path.substring(0, idx);
			var path1 = path.substring(idx + 1);
			for (var i=0, j=this.childs.length; i<j; i++) {
				if (child = this.childs[i][path0]) {
					if (child = child.select(path1)) result = result.concat(child);
				}
			}
			
			if (result.length > 0) return result;
			else return null;
			//if (child = this.childs[path.substring(0,idx)]) 
			//	return child.select(path.substring(idx + 1));
		} else {
			if (child = this.childs[path.substring(0,idx)]) return [child];
		}*/
	},
	
	setEnabled : function(enabled) {
		if (this.enabled=!!enabled) {
			if (!this._body) this.initBody();
			this.expand(true);
		} else {
			this.expand(false);
		}
	},
	setLabel : function() {
		/**
		 * _a is the label node prepare to contain the infomation of sequenceContainer.
		 * _b is the infomation will be presented.
		 */
		var _a = this._container.firstChild.childNodes[1],_b;

		var displayName = this.isVirtual() ? "" : (this.longName || this.name);
		_a.firstChild.innerHTML = displayName;
		if (this.maxOccurs!=1) {
			_b = "Min :"+this.minOccurs+" Max : "+this.maxOccurs+" created : <span>"+this.minOccurs+"</span>  ";
			/*_b = _b.replace(/\{\$min}/g,this.minOccurs);
			_b = _b.replace("{$max}",this.maxOccurs);*/
			_a.lastChild.innerHTML = _b;
			if (this.maxOccurs=="unbounded") this.maxOccurs = Number.MAX_VALUE;
		}
		if (this.bit & ContainerTemplates.hasDescription) {
			_a.lastChild.appendChild($doc.createTextNode(this.description));
		}
		this._container.title = displayName + " " + this.description;
	},
	showMatch : function(v) {
		var result = false;
		if (v.length==0) result = true;
		if (this.match(v)) {
			result = true;
			v = "";
		}
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.showMatch(v)) result = true;
			});
		}
		if (result) this._container.style["display"]="";
		else this._container.style["display"]="none";
		return result;
	},
	showOptional : function(show) {
		if (this.enabled) {
			if (show) this._container.firstChild.style["display"]="";
			else this._container.firstChild.style["display"]="none";
		} else {
			if (show) {
				if (this._container.style["display"]=="none") return this._container.style["display"]="";
			} else return this._container.style["display"]="none";
		}
		this.eachChild(function(child) {
			child.showOptional(show);
		});
		/*
		for (var i=0,j=this.childs.length; i<j; i++) {
			this.childs[i].showOptional(show);
		}*/
	},
	/**
	 * Set this container and all childs writable or read only.
	 * @param {Boolean} readonly true if this container and all childs should be readonly.
	 */
	setReadOnly : function(/* Boolean: */readonly) {
		this.readOnly = !!readonly;
		if (!this.increasable && (this.bit & ContainerTemplates.isOptional)) this._setOptionalCheckboxReadOnly(readonly);
		this.eachChild(function(child) {
			child.setReadOnly(readonly);
		});
		/*
		for (var i=0,j=this.childs.length; i<j; i++) {
			this.childs[i].setReadOnly(readonly);
		}*/
	},
	/**
	 * Get the XML string
	 */
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitSequence(this);
		/*
		var xml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				xml.push(child.toXML());
			});
		}
		return xml.join("");*/
	},
	updateCreated : function() {
		if (!this._created) {
			var _a = this._container.firstChild.childNodes[1].lastChild;
			var _b = _a.getElementsByTagName("span");
			if (_b.length==1) this._created = _b[0];
		}
		if (this._created) this._created.innerHTML = this.created;
	},
	validate : function() {
		var result=true;
		this.eachChild(function(child) {
			if (!child.validate()) result = false;
		});
		/*
		for (var i=0,j=this.childs.length; i<j; i++) {
			if (!this.childs[i].validate()) result = false;
		}*/
		return result;
	},
	
	_setOptionalCheckboxReadOnly : function(/* Boolean: */readonly) {
		this._container.firstChild.firstChild.firstChild.disabled = readonly;
	}
});/**
 * @import msg.ui.SimpleContainer;
 *
 * @class msg.ui.SequenceItemContainer
 * @extends msg.ui.SimpleContainer
 * 
 * @author Leegorous
 */
Class.create("msg.ui.SequenceItemContainer","msg.ui.SimpleContainer",null,{
	/**
	 * Add a child to this container.
	 * @param {SimpleContainer} child The child container want to be added.
	 */
	add : function(child) {
		var name = child.name || this.ownerDocument.generateContainerName();
		this.childs[name] = child;
		this._body.appendChild(child._container);
	},
	/**
	 * Create a document fragment node as the body of this container.
	 * In here it doing nothing, and it should not be used.
	 */
	createBody : function() {},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer : function() {
		return ContainerTemplates.get("SequenceItemContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroySequenceItemImpl"),
	//_destroySequenceItemImpl : function() {
	destroy : function() {
		this.eachChild(function(child) {
			child.destroy();
		});
		delete this.childs;
		//this._destroySimpleImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Iterate over all childs in this container
	 * @param {Method} iterator The action
	 */
	eachChild : function(iterator) {
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && child.ownerDocument) iterator(child);
		}
		child = null;
	},
	/**
	 * Fill the xml data to this container
	 * @param {Object} node The xml data node
	 */
	fill : function(node) {
		var nodes,child;
		if (!(nodes = node.childNodes)) return;
		for (var i=0,j=nodes.length; i<j; i++) {
			var nodeName = XML.getLocalName(nodes[i]);
			child = this.childs[nodeName];
			if (child) {
				child.fill(nodes[i]);
			} else {
				Console.error("Error occur in filling " + nodeName);
			}
		}
		node=nodes=child=null;
	},
	init : function(_node,_parentContainer) {
		//this._initSequenceItemImpl(_node,_parentContainer);
	//},
	//_initSequenceItemImpl : function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initSimpleImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this.bit=((this.removable = _parentContainer.increasable)?ContainerTemplates.removable:0);
		this.enabled=true;
		this.childs = {};
		this._container = this.createContainer();
		//this._container._container = this;
		this._container._ctnId = this.id;

		// inherit dict from parent container
		this.dict = _parentContainer?_parentContainer.dict : null;

		this.setLabel();
		this.initBody();
		//if (_parentContainer) _parentContainer.add(this);
	},
	
	initBody : function() {
		this._body = this._container.lastChild;
		//UIObjectTree.walk(this.node,this);
		this.ownerDocument.containerBuilder.buildChild(this.node, this);
	},
	remove : function() {
		if (this._parent.remove(this)) this.destroy();
	},
	
	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		if (!path || path.length==0 || !this.enabled) return null;
		var idx, child;
		if (path.indexOf(".") == 0) {
			if ((path = path.substring(1)).length == 0) return [this];
		}
		
		if ((idx = path.indexOf("/")) > 0) {
			if (child = this.childs[path.substring(0,idx)]) 
				return child.select(path.substring(idx + 1));
		} else {
			if (child = this.childs[path]) return child.select(".");
		}
	},
	
	setLabel : function(i) {
		if (typeof i=="undefined") i = this._parent.created;
		this._container.firstChild.firstChild.innerHTML = "Occurrence "+i;
	},
	showMatch : function(v) {
		var result = false;
		if (v.lenght==0) result = true;
		this.eachChild(function(child) {
			if (child.showMatch(v)) result = true;
		});
		if (result) this._container.style["display"]="";
		else this._container.style["display"]="none";
		return result;
	},
	showOptional : function(show) {
		this.eachChild(function(child) {
			child.showOptional(show);
		});
		/*
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.showOptional(show);
		}
		child = null;*/
	},
	/**
	 * Set this container and all childs writable or read only.
	 * @param {Boolean} readonly true if this container and all childs should be readonly.
	 */
	setReadOnly : function(/* Boolean: */readonly) {
		//var child;
		this.readOnly = !!readonly;
		this.eachChild(function(child) {
			child.setReadOnly(readonly);
		});
		/*
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.setReadOnly(readonly);
		}
		child = null;*/
	},
	/**
	 * Get the XML string
	 */
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitSequenceItem(this);
		/*
		var xml=[], childsXml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			/*
			var child;
			for (var i in this.childs) {
				child = this.childs[i];
				if (child && i==child.name && child.enabled) childsXml.push(child.toXML());
			}* /
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				this.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				xml.push('<'+this.name);//+' type="Swift'+this.node.base+'Element" typeName="'+this.node.type+'"'
				xml.push('>');
				xml.push(childsXml);
				xml.push('</'+this.name+'>');
			}
		}
		return xml.join("");*/
	},
	validate : function() {
		//var child;
		var result=true;
		this.eachChild(function(child) {
			if (child.enabled && !child.validate()) result = false;
		});
		/*
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name && child.enabled) {
				if (!child.validate()) result = false;
			}
		}
		child = null;*/
		return result;
	}
});﻿/**
 * @import msg.ui.PatternContainer;
 *
 * @class msg.ui.TextAreaPatternContainer
 * @extends msg.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.TextAreaPatternContainer","msg.ui.PatternContainer",null,{
	createContainer:function() {
		return ContainerTemplates.get("TextAreaPatternContainer"+this.bit);
	},
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("TextAreaPatternField"+this.bit));
	},
	/**
	 * when focus on this field, the timer of this container should be canceled.
	 */
	cancelTimer:function() {
		if (this._timer) {
			this._timer.cancel();
			this._timer.schedule(10);
		}
		//this._timer=null;
	},
	initBody:function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this.setDimension();
		/**
		 * the setAttribute method and _getColumnNumber method have performance problems.
		 */
		/*
		var _format=this.format;
		var _i=_format.indexOf('*');
		this._field.setAttribute("rows",parseInt(_format.substring(0,_i))||1);
		//this._getColumnNumber(_format.substring(_i+1));
		this._field.setAttribute("cols",this._getColumnNumber(_format.substring(_i+1)));*/
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._timer=null;
		//this._initTextAreaPatternImpl(_node,_parentContainer);
	//},
	//_initTextAreaPatternImpl : function(_node,_parentContainer) {
		//this._initPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this._timer=null;
	},
	/**
	 * Refine the text of the textarea field.
	 * The text could not include "\r", "\f", "\v".
	 * Hyphen "-" could not lead a line.
	 */
	refineText:function() {
		var elem=this._field;
		var v = elem.value, original = v;
		//var originalLength=v.length;
		v = v.replace(/[\r\f\v]/g,'').replace(/\n{2,}|\n[-]/g,'\n').replace(/\t/g,' ').trim();//[ ]{2,}|
		//var end;
		//if ((end=v.length)==0) return elem=null;
		if (v.length==0) return elem=null;
		//end--;
		//var row=elem.rows;
		//var col=elem.cols-1;
		elem.value = this._textFilter(elem.rows, elem.cols-1, v);
		/*
		var result=[];
		//var idx0=0,idx1=v.indexOf('\n'),idx3;
		var idx0=0,idx1=0,idx2=0;
		//-- re0 : represent characters that can not lead a line;
		var re0=/[-\n]/;
		//-- idx0 : always current position
		while(idx0<end) {
			while (re0.test(v.charAt(idx0))) idx0++;
			idx1=v.indexOf('\n',idx0);
			if (idx1==-1) idx1=end;
			if (idx1-idx0>col) {
				//-- long line;
				//-- idx1 : exact position
				//-- idx2 : predicted position
				idx2=idx0+col;
				if (v.charAt(idx2)==' ') {
					idx1=idx2+1;
				} else {
					idx1=v.lastIndexOf(' ',idx2);
					if (idx1<idx0) idx1=idx2;
					else idx1++;
				}
			}
			result.push(v.slice(idx0,idx1));
			idx0=idx1;
		}
		if (result.length>row) result.length=row;
		elem.value=result.join('\n');*/
		//var resultLength=elem.value.length;
		//if (resultLength<originalLength) {
		var _r=true,_info="";
		if (elem.value!=original) {
			_info = "Text refined ";
			/*
			if (this._timer) this._timer.cancel();
			else {
				var o=this;
				this._timer=new Timer(function() {o.setExtraInfo('');o=o._timer=null;});
			}
			this._timer.schedule(3000);*/
		}
		if (!(_r=this.validate())) _info+=(_info.length>0?"And ":"")+"Invalid";
		this.setExtraInfo(_info,!_r);
		if (_r) this._hideTipsLater();
		original=elem=v=null;
	},
	_hideTipsLater : function() {
		if (this._timer) this._timer.cancel();
		else {
			var o=this;
			this._timer=new Timer(function() {o.setExtraInfo('');o=o._timer=null;});
		}
		this._timer.schedule(3000);
	},
	setDimension:function() {
		this._field.setAttribute("rows",this.node.rows);
		this._field.setAttribute("cols",this.node.cols+1);
	},
	
	validate : function() {
		return this[arguments.callee.parent]();
	},
	
	_textFilter : function(row, col, v) {
		var result=[],end = v.length;
		//var idx0=0,idx1=v.indexOf('\n'),idx3;
		var idx0=0,idx1=0,idx2=0;
		//-- re0 : represent characters that can not lead a line;
		var re0=/[-\n]/;
		//-- idx0 : always current position
		while(idx0<end) {
			while (re0.test(v.charAt(idx0))) idx0++;
			idx1=v.indexOf('\n',idx0);
			if (idx1==-1) idx1=end;
			if (idx1-idx0>col) {
				//-- long line;
				//-- idx1 : exact position
				//-- idx2 : predicted position
				idx2=idx0+col;
				if (v.charAt(idx2)==' ') {
					idx1=idx2+1;
				} else {
					idx1=v.lastIndexOf(' ',idx2);
					if (idx1<idx0) idx1=idx2;
					else idx1++;
				}
			}
			result.push(v.slice(idx0,idx1));
			idx0=idx1;
		}
		if (result.length>row) result.length=row;
		return result.join('\n');
	}
	//_validateTextAreaPatternImpl : new MethodAlias("_validatePatternImpl")
	/*function() {
		//Console.put(this.node.restriction.pattern.toJSON());
		this._validateTextAreaPatternImpl = this._validatePatternImpl;
		return this._validateTextAreaPatternImpl();
	}*/
	/**
	 * deprecated
	_getColumnNumber:function(str) {
		var value=0;
		var str=str.gsub(/\d+!?(n|a|c|x)/,function(match) {value+=parseInt(match,10);return '';});
		return value+=str.length;
	}*/
});﻿/**
 * @import ui.FocusListener;
 * @import msg.ui.CommonContainerListener;
 *
 * @class msg.ui.TextAreaPatternValidateListener
 * @extends msg.ui.CommonContainerListener
 * @implements ui.FocusListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.TextAreaPatternValidateListener",
	"msg.ui.CommonContainerListener", 
	["ui.FocusListener"],{
	
	onFocus:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.cancelTimer();
		_container = null;
	},
	
	onLostFocus:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.refineText();
		_container = null;
	}
});﻿/**
 * @import msg.ui.PatternContainer;
 *
 * @class msg.ui.TextPatternContainer
 * @extends msg.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.TextPatternContainer","msg.ui.PatternContainer",null,{
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("TextPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("TextPatternContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyTextPatternImpl"),
	//_destroyTextPatternImpl : function() {
	destroy : function() {
		if (this._field) this._field.destroy();
		//this._destroyPatternImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Filtrate the input
	 * @param {Object} event the event object
	 * @param {int} charCode the charCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateInput:function(event,charCode,modifiers) {},
	/**
	 * Filtrate the text
	 * @param {Object} event the event object
	 * @param {int} keyCode the keyCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateText:function(event,keycode,modifiers) {},
	/**
	 * Get the value of this container.
	 * @returns {String} the value of this container.
	 */
	getValue:function() {
		return this._field.getText();
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//_initTextPatternImpl : new MethodAlias("_initPatternImpl"),
	/*function(_node,_parentContainer) {
		this._initPatternImpl(_node,_parentContainer);
	},*/
	//initBody : new MethodAlias("_initBodyTextPatternImpl"),
	//_initBodyTextPatternImpl:function() {
	initBody : function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this._field=new (Class.get("ui.TextBox"))(this._field);
		this.setMaxLength();
	},
	
	preventInput:function(event) {
		switch(event.keyCode) {
			case 8:
			case 9:
			case 37:
			case 38:
			case 39:
			case 40:break;
			//case 46:
			default:EvtMgr.preventDefault(event);
		}
	},
	setMaxLength:function() {
		var _max
		if (this.node.restriction && (_max = this.node.restriction.maxLength)>-1) {
			this._field.setMaxLength(_max);
			if (_max>10) this._field._e.style["width"]=Math.ceil(_max/5)*5*9+"px";
		}
	},
	setValue:function(v) {
		//-- if the body of this container has not been initialized, initialize it first.
		//if ((this.bit & ContainerTemplates.isOptional) && !this._field) this.initBody();
		if (this._field) this._field.setText(v);
	},
	//validate : new MethodAlias("_validateTextPatternImpl"),
	//_validateTextPatternImpl:function() {
	validate : function() {
		var valid;
		var value = this.getValue();
		if (valid=this[arguments.callee.parent]()) {
			//-- check if the initial character is hyphen.
			if (!(valid=(value.charAt(0)!='-'))) {
				this.setExtraInfo("cannotStartLineWithHyphen", true);
				return valid;
			}

			var res,base;
			if ( (res=this.node.restriction) && (base = res.base) ) {
				switch(base) {
					case "xs:int":break;
					/*
					 * check the decimal value
					 */
					case "xs:decimal":
						var _v;
						if (res.totalDigits>0 && res.fractionDigits>=0) {
							var intLength = res.totalDigits - res.fractionDigits - 1;
							var re = new RegExp("^\\d{0,"+intLength+"}\\.\\d{0,"+res.fractionDigits+"}$","g");
							if (!(valid = re.test(value))) {
								this.setExtraInfo("Invalid decimal.",true);
							}
						}
						break;
				}
			}
		}
		return valid;
	},
	
	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		this._field.setReadOnly(readonly);
	}
	// deprecated
	/*
	_getMaxLength:function() {
		var _a;
		if ((_a=this.node.restriction) && (_a=_a.restriction)) {
			if (_a.maxLength) return parseInt(_a.maxLength);
			else if (_a.maxInclusive) return _a.maxInclusive.length;
		}
		return -1;
	}*/
});﻿/**
 * @import ui.FocusListener;
 * @import ui.KeyboardListener;
 * @import msg.ui.CommonContainerListener;
 *
 * @class msg.ui.TextPatternValidateListener
 * @extends msg.ui.CommonContainerListener
 * @implements ui.FocusListener, ui.KeyboardListener
 *
 * @author Leegorous
 */
Class.create("msg.ui.TextPatternValidateListener",
	"msg.ui.CommonContainerListener", 
	["ui.FocusListener","ui.KeyboardListener"],{
	ignoreKeys:{
		"8":true,// BackSpace
		"9":true,// Tab
		"37":true,// Left
		"38":true,// Up
		"39":true,// Right
		"40":true// Down
		//"46":false// Delete
	},
	onFocus:function(event,sender) {},
	
	onLostFocus:function(event,sender) {
		var _container = this.findContainer(sender);
		if (_container) _container.validate();
	},
	
	onKeyDown:function(event,sender,keycode,modifiers) {
		//alert(keycode);
	},
	
	onKeyPress:function(event,sender,charcode,modifiers) {
		if (this.ignoreKeys[event.keyCode]) return;
		var _container = this.findContainer(sender);
		if (_container) _container.filtrateInput(event,charcode,modifiers);
	},
	
	onKeyUp:function(event,sender,keycode,modifiers) {
		if (this.ignoreKeys[event.keyCode]) return;
		var _container = this.findContainer(sender);
		if (_container) _container.filtrateText(event,keycode,modifiers);
	}
});/**
 * @class msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.SimpleContainer" ,null ,null ,{
	_e : null,
	/**
	 * Destroy this template object
	 */
	destroy : function() {
		DOM.detachNode(this._e);
		this._release();
	},
	
	getElem : function() {
		return this._e;
	},
	
	/**
	 * release the binded node
	 */
	_release : function() {
		delete this._e;
		this.getElem = null;
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 *
 * @class msg.ui.tmpl.OptionContainerSelector
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.OptionContainerSelector" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [0],
		
		_sink : {},
		
		bindEvents : function(/* Element */ elem) {//debugger;
			ContainerTemplate.bindEventTrigger(
				elem.firstChild,
				ContainerEvent.getEventId("OptionContainerSwitcher"));
			elem = null;
		},
		
		bindTemplate : function(/* Element */node) {
			var name = node.getAttribute("name");

			this.bindEvents(node);

			// remove the additional information
			node.removeAttribute("name");
			node.removeAttribute("type");

			var bit, _node;
			for (var i=0, j=this.bits.length; i<j; i++) {
				bit = this.bits[i];
				_node = node.cloneNode(true);

				this._sink[bit] = _node;
			}

			// release the temporal nodes
			_node = null;

			// register this template class
			ContainerTemplate.bind(name, this);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 * @import msg.ui.tmpl.OptionContainerSelector;
 *
 * @class msg.ui.tmpl.OptionContainer
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.OptionContainer" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [
			0,
			ContainerTemplate.isOptional,
			ContainerTemplate.hasDescription,
			ContainerTemplate.isOptional | ContainerTemplate.hasDescription
			],
		
		_sink : {},
		
		bindTemplate : function(/* Element */node) {
			var name = "OptionContainer";

			ContainerTemplate.build("OptionContainerSelector");
			
			var selector = ContainerTemplate.get("OptionContainerSelector");
			DOM.insertAfter(selector.getElem(), node.firstChild.lastChild.firstChild);
			selector._release();
			selector = null;
			
			for (var i=0, j=this.bits.length; i<j; i++) {
				bit = this.bits[i];
				_node = node.cloneNode(true);
				var checkbox = _node.firstChild.firstChild.firstChild;
				if (bit & ContainerTemplate.isOptional) {
					//DOM.detachNode(_node.lastChild);
					_node.lastChild.style["display"] = "none";
					DOM.detachNode(_node.firstChild.lastChild.childNodes[1]);
				} else {
					DOM.detachNode(checkbox);
				}
				this._sink[bit] = _node;
			}

			// release the temporal nodes
			node = checkbox = null;
			// register this template class
			ContainerTemplate.bind(name, this);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	},

	getHeader : function() {
		return this.getElem().firstChild;
	},

	getBody : function() {
		return this.getElem().lastChild;
		/*
		if (this._body) return this._body;
		else {
			return this._body = new Element(this._e.lastChild);
		}*/
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 * @import msg.ui.tmpl.OptionContainer;
 *
 * @class msg.ui.tmpl.CompositeContainer
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.CompositeContainer" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [
			0,
			ContainerTemplate.isOptional,
			ContainerTemplate.hasDescription,
			ContainerTemplate.isOptional | ContainerTemplate.hasDescription
			],
		
		_sink : {},

		bindEvents : function(/* Element */ elem) {//debugger;
			ContainerTemplate.bindEventTrigger(
				elem.firstChild.firstChild.firstChild,
				ContainerEvent.getEventId("ContainerEnableSwitcher"));
			elem = null;
		},
		
		bindTemplate : function(/* Element */node) {
			var _node, bit, _node2;
			var name = node.getAttribute("name");
			// bind events
			this.bindEvents(node);

			// remove the additional information
			node.removeAttribute("name");
			node.removeAttribute("type");

			_node2 = node.cloneNode(true);
			
			for (var i=0, j=this.bits.length; i<j; i++) {
				bit = this.bits[i];
				_node = node.cloneNode(true);
				var checkbox = _node.firstChild.firstChild.firstChild;
				if (bit & ContainerTemplate.isOptional) {
					//DOM.detachNode(_node.lastChild);
					_node.lastChild.style["display"] = "none";
					//checkbox.setAttribute("__evt", ContainerEvent.getEventId("ComplexContainerSwitcher"));
				} else {
					DOM.detachNode(checkbox);
				}
				this._sink[bit] = _node;
			}
			
			Class.get("msg.ui.tmpl.OptionContainer").bindTemplate(_node2);
			
			// release the temporal nodes
			node = _node2 = checkbox = null;
			// register this template class
			ContainerTemplate.bind(name, this);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	},

	getHeader : function() {
		return this.getElem().firstChild;
	},

	getBody : function() {
		return this.getElem().lastChild;
		/*
		if (this._body) return this._body;
		else {
			return this._body = new Element(this._e.lastChild);
		}*/
	}
});/**
 * @class msg.ui.tmpl.SimplePatternField
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.SimplePatternField" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [
			0,
			ContainerTemplate.isOptional,
			ContainerTemplate.hasFormat,
			ContainerTemplate.isOptional | ContainerTemplate.hasFormat,
			ContainerTemplate.hasDescription,
			ContainerTemplate.isOptional | ContainerTemplate.hasDescription,
			ContainerTemplate.hasFormat | ContainerTemplate.hasDescription,
			ContainerTemplate.isOptional | ContainerTemplate.hasFormat | ContainerTemplate.hasDescription
			],
		
		bindTemplate : function(templateClass, node) {
			var name = node.getAttribute("name");

			// remove the additional information
			node.removeAttribute("name");
			node.removeAttribute("type");

			var _node, bit;
			for (var i=0, j=this.bits.length; i<j; i++) {
				bit = this.bits[i];
				_node = node.cloneNode(true);
				$3=_node.lastChild,$2=$3.previousSibling;

				if (!(bit & ContainerTemplate.isOptional)) _node.className += " mandatoryField";

				if (!(bit & ContainerTemplate.hasFormat)) _node.removeChild($2);

				if (!(bit & ContainerTemplate.hasDescription)) _node.removeChild($3);

				templateClass._sink[bit] = _node;
			}
			// release the node
			node = _node = $2 = $3 = null;

			// register this template class
			ContainerTemplate.bind(name, templateClass);
		}
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 * @import msg.ui.tmpl.SimplePatternField;
 *
 * @class msg.ui.tmpl.ListPatternField
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.ListPatternField" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		_sink : {},
		
		bindEvents : function(/* Element */ elem) {//debugger;
			ContainerTemplate.bindEventTrigger(
				elem.firstChild,
				ContainerEvent.getEventId("CommonListPattern"));
			elem = null;
		},
		
		bindTemplate : function(/* Element */node) {
			// bind the events
			this.bindEvents(node);
			
			Class.get("msg.ui.tmpl.SimplePatternField").bindTemplate(this, node);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.tmpl.SimpleContainer;
 *
 * @class msg.ui.tmpl.SequenceBody
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.SequenceBody" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [0],
		
		_sink : {},
		
		bindTemplate : function(/* Element */node) {
			var name = node.getAttribute("name");

			// remove the additional information
			node.removeAttribute("name");
			node.removeAttribute("type");

			var bit, _node;
			for (var i=0, j=this.bits.length; i<j; i++) {
				bit = this.bits[i];
				_node = node.cloneNode(true);

				this._sink[bit] = _node;
			}

			// release the temporal nodes
			_node = null;

			// register this template class
			ContainerTemplate.bind(name, this);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 *
 * @class msg.ui.tmpl.SequenceContainer
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.SequenceContainer" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [
			0,
			ContainerTemplate.isOptional,
			ContainerTemplate.hasDescription,
			ContainerTemplate.isOptional | ContainerTemplate.hasDescription,
			ContainerTemplate.increasable,
			ContainerTemplate.isOptional | ContainerTemplate.increasable,
			ContainerTemplate.hasDescription | ContainerTemplate.increasable,
			ContainerTemplate.isOptional | ContainerTemplate.hasDescription | ContainerTemplate.increasable
			],

		_sink : {},
		
		bindEvents : function(/* Element */ elem) {
			var header = elem.firstChild;

			ContainerTemplate.bindEventTrigger(
				header.firstChild.firstChild,
				ContainerEvent.getEventId("ContainerEnableSwitcher"));

			ContainerTemplate.bindEventTrigger(
				header.lastChild.firstChild,
				ContainerEvent.getEventId("ExpandContainer"));

			ContainerTemplate.bindEventTrigger(
				header.lastChild.lastChild,
				ContainerEvent.getEventId("NewOccurrence"));

			elem = header = null;
		},
		
		bindTemplate : function(/* Element */node) {
			var name = node.getAttribute("name");
			// bind events
			this.bindEvents(node);
			
			// remove the additional information
			node.removeAttribute("name");
			node.removeAttribute("type");

			for (var i=0, j=this.bits.length; i<j; i++) {
				var bit = this.bits[i];
				var _node = node.cloneNode(true);
				var header = _node.firstChild;
				var checkbox = header.firstChild.firstChild;
				var newOccur = header.lastChild.lastChild;

				// child increasable
				if (bit & ContainerTemplate.increasable) {
					DOM.detachNode(checkbox);
				} else {
					DOM.detachNode(newOccur);
					if (bit & ContainerTemplate.isOptional) {
					} else {
						DOM.detachNode(checkbox);
					}
				}

				// no description
				if (!(bit & ContainerTemplate.hasDescription)) {
					DOM.detachNode(header.childNodes[1].lastChild);
				}

				this._sink[bit] = _node;
			}

			// release the temporal nodes
			node = header = checkbox = newOccur = null;
			// register this template class
			ContainerTemplate.bind(name, this);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 *
 * @class msg.ui.tmpl.SequenceItemContainer
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.SequenceItemContainer" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		bits : [0, ContainerTemplate.removable],
		
		_sink : {},
		
		bindEvents : function(/* Element */ elem) {
			var header = elem.firstChild;

			ContainerTemplate.bindEventTrigger(
				header.lastChild.firstChild,
				ContainerEvent.getEventId("RemoveSequenceItem"));

			elem = header = null;
		},
		
		bindTemplate : function(/* Element */node) {
			var name = node.getAttribute("name");
			// bind events
			this.bindEvents(node);

			// remove the additional information
			node.removeAttribute("name");
			node.removeAttribute("type");

			var bit, _node, header;
			for (var i=0, j=this.bits.length; i<j; i++) {
				bit = this.bits[i];
				_node = node.cloneNode(true);
 				header = _node.firstChild;

				if (!(bit & ContainerTemplate.removable)) {
					DOM.detachNode(header.lastChild.firstChild);
				}

				this._sink[bit] = _node;
			}
			
			// release the temporal nodes
			_node = header = null;

			// register this template class
			ContainerTemplate.bind(name, this);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 * @import msg.ui.tmpl.SimplePatternField;
 *
 * @class msg.ui.tmpl.TextAreaPatternField
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.TextAreaPatternField" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		_sink : {},
		
		bindEvents : function(/* Element */ elem) {//debugger;
			ContainerTemplate.bindEventTrigger(
				elem.firstChild,
				ContainerEvent.getEventId("CommonTextAreaPattern"));
			elem = null;
		},
		
		bindTemplate : function(/* Element */node) {
			// bind the events
			this.bindEvents(node);
			
			Class.get("msg.ui.tmpl.SimplePatternField").bindTemplate(this, node);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @import msg.ui.ContainerTemplate;
 * @import msg.ui.ContainerEvent;
 * @import msg.ui.tmpl.SimpleContainer;
 * @import msg.ui.tmpl.SimplePatternField;
 *
 * @class msg.ui.tmpl.TextPatternField
 * @extends msg.ui.tmpl.SimpleContainer
 *
 * @author Leegorous
 */
Class.create("msg.ui.tmpl.TextPatternField" ,"msg.ui.tmpl.SimpleContainer" ,null ,{
	$tatic : {
		_sink : {},
		
		bindEvents : function(/* Element */ elem) {//debugger;
			ContainerTemplate.bindEventTrigger(
				elem.firstChild,
				ContainerEvent.getEventId("CommonTextPattern"));
			elem = null;
		},
		
		bindTemplate : function(/* Element */node) {
			// bind the events
			this.bindEvents(node);
			
			Class.get("msg.ui.tmpl.SimplePatternField").bindTemplate(this, node);
		}
	},
	
	init : function(/* int */ bit) {
		if (!(bit > 0)) bit = 0;
		// find the template with the specific bit
		var elem = arguments.callee._sink[bit];
		if (!elem) throw new Error("Template undefined. ");
		// get a copy
		this._e = elem.cloneNode(true);
	}
});/**
 * @class msg.ui.impl.CommonObserver
 *
 * @author Leegorous
 */
Class.create("msg.ui.impl.CommonObserver" ,null ,null ,{
	
	/**
	 * @constructor
	 *
	 * @param {Container} container
	 */
	init : function(container) {
		this._container = container;

		this._enabledCollection = [];
	},
	
	destroy : function() {
		delete this._container;
		delete this._enabledCollection;
	},

	/**
	 * Add a listener for listening the enabled event
	 *
	 * @param {Object} listener
	 */
	addEnabledListener : function(listener) {
		if (!this._enabledCollection.include(listener)) 
			this._enabledCollection.push(listener);
	},

	/**
	 * Remove a enabled listener
	 *
	 * @param {Object} listener
	 */
	removeEnabledListener : function(listener) {
		this._enabledCollection.remove(listener);
	},

	/**
	 * Update state
	 *
	 * @param {String} type
	 */
	update : function(type) {
		switch(type) {
			case "enabled":
				this._updateEnabled();
				break;
		}
	},
	
	_updateEnabled : function() {
		var collection = this._enabledCollection.clone();
		for (var i=0, j=collection.length; i<j; i++) {
			collection[i].onEnabled(this._container);
		}
		collection = null;
	}
});/**
 * @author Leegorous
 */
Class.create("msg.ui.impl.GenerateXMLVisitor" ,null ,null ,{

	/**
	 * Generate xml for CompositeContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitComposite : function(container) {
		var xml=[], childsXml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				container.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				if (container.isVirtual()) xml.push(childsXml);
				else {
					var prefix = container.ownerDocument.nsPrefix || '';
					if (prefix.length > 0) prefix += ':';
					xml.push('<' + prefix + container.name + '>');
					xml.push(childsXml);
					xml.push('</' + prefix + container.name+'>');
				}
			}
		}
		return xml.join("");
	},

	/**
	 * Generate xml for DocumentContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitDocument : function(container) {
		var xml = [];//debugger;
		var prefix = container.ownerDocument.nsPrefix || '';
		xml.push('<' + (prefix.length >0?prefix + ':':prefix) + 'Document xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns'+(prefix.length >0?':'+prefix:prefix)+'="'+container.node.xmlns+'">');
		xml.push(container.getMessage().toXML());
		xml.push('</' + (prefix.length >0?prefix + ':':prefix) + 'Document>');
		return xml.join('');
	},
	
	/**
	 * Generate xml for MainContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitMessage : function(container) {
		var xml=[], childsXml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0) {
				var prefix = container.ownerDocument.nsPrefix || '';
				if (prefix.length > 0) prefix += ':';
				xml.push('<' + prefix + container.name + '>');
				xml.push(childsXml);
				xml.push('</' + prefix + container.name + '>');
			}
		}
		return xml.join("");
	},
	
	/**
	 * Generate xml for OptionContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitOption : function(container) {
		var xml=[], childsXml;
		if (container.enabled && container.curChild) {
			childsXml = container.curChild.toXML();
			if (childsXml.length>0 || 
				container.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				if (container.isVirtual()) xml.push(childsXml);
				else {
					var prefix = container.ownerDocument.nsPrefix || '';
					if (prefix.length > 0) prefix += ':';
					xml.push('<' + prefix + container.name + '>');
					xml.push(childsXml);
					xml.push('</' + prefix + container.name + '>');
				}
			}
		}
		return xml.join("");
	},

	/**
	 * Generate xml for PatternContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitPattern : function(container) {
		var xml = "";
		if (container.enabled) {
			/**
			 * I don't know why it have to do such replace in original script.
			 * But I keep it.
			 */
			var nodeName = container.name.replace(/_\d+/,'');
			var prefix = container.ownerDocument.nsPrefix || '';
			if (prefix.length > 0) prefix += ':';
			if (container.isVirtual()) xml = container.getValue();
			else xml = '<' + prefix + nodeName + ">" + container.getValue() + '</' + prefix + nodeName + '>';
		}
		return xml;
	},
	
	/**
	 * Generate xml for SequenceContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitSequence : function(container) {
		var xml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				xml.push(child.toXML());
			});
		}
		return xml.join("");
	},
	
	/**
	 * Generate xml for SequenceItemContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitSequenceItem : function(container) {
		var xml=[], childsXml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				container.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				var prefix = container.ownerDocument.nsPrefix || '';
				if (prefix.length > 0) prefix += ':';
				xml.push('<' + prefix + container.name + '>');
				xml.push(childsXml);
				xml.push('</' + prefix + container.name + '>');
			}
		}
		return xml.join("");
	}
});/**
 * @import msg.ui.impl.CommonObserver;
 *
 * @class msg.ui.impl.OptionFieldObserver
 * @extends msg.ui.impl.CommonObserver
 * @author Leegorous
 */
Class.create("msg.ui.impl.OptionFieldObserver" ,"msg.ui.impl.CommonObserver" ,null ,{

	/**
	 * @constructor
	 */
	init : function(container) {
		this[arguments.callee.parent](container);

		this._changeCollection = [];
	},

	destroy : function() {
		delete this._changeCollection;
		this[arguments.callee.parent]();
	},
	
	/**
	 * Add a listener for listening the change event
	 *
	 * @param {Object} listener
	 */
	addChangeListener : function(listener) {
		if (!this._changeCollection.include(listener)) 
			this._changeCollection.push(listener);
	},

	/**
	 * Remove a enabled listener
	 *
	 * @param {Object} listener
	 */
	removeChangeListener : function(listener) {
		this._changeCollection.remove(listener);
	},

	/**
	 * Update state
	 *
	 * @param {String} type
	 */
	update : function(type) {
		this[arguments.callee.parent](type);
		switch(type) {
			case "change":
				this._updateChange();
				break;
		}
	},
	
	_updateChange : function() {
		var collection = this._changeCollection.clone();
		for (var i=0, j=collection.length; i<j; i++) {
			collection[i].onChange(this._container);
		}
		collection = null;
	}

});/**
 * @import msg.ui.impl.CommonObserver;
 *
 * @class msg.ui.impl.PatternObserver
 * @extends msg.ui.impl.CommonObserver
 * @author Leegorous
 */
Class.create("msg.ui.impl.PatternObserver" ,"msg.ui.impl.CommonObserver" ,null ,{

	/**
	 * @constructor
	 */
	init : function(container) {
		this[arguments.callee.parent](container);

		this._changeCollection = [];
	},

	destroy : function() {
		delete this._changeCollection;
		this[arguments.callee.parent]();
	},
	
	/**
	 * Add a listener for listening the change event
	 *
	 * @param {Object} listener
	 */
	addChangeListener : function(listener) {
		if (!this._changeCollection.include(listener)) 
			this._changeCollection.push(listener);
	},

	/**
	 * Remove a enabled listener
	 *
	 * @param {Object} listener
	 */
	removeChangeListener : function(listener) {
		this._changeCollection.remove(listener);
	},

	/**
	 * Update state
	 *
	 * @param {String} type
	 */
	update : function(type) {
		this[arguments.callee.parent](type);
		switch(type) {
			case "change":
				this._updateChange();
				break;
		}
	},
	
	_updateChange : function() {
		var collection = this._changeCollection.clone();
		for (var i=0, j=collection.length; i<j; i++) {
			collection[i].onChange(this._container);
		}
		collection = null;
	}

});/**
 * Creator : Leegorous
 *
 * @agent true;
 * @classpath client/;
 *
 * @import msg.ui.*;
 * @import msg.ui.tmpl.*;
 * @import msg.ui.impl.*;
 *
 * @jsUnit ../jsunit/app/;
 * @test tests/msg/ui/;
 * @test tests/msg/ui/tmpl/;
 * @test tests/msg/ui/impl/;
 */﻿/**
 * @import msg.ui.PatternContainer;
 *
 * @abstract
 * @class csp.ui.PatternContainer
 * @extends msg.ui.PatternContainer
 *
 * It is the super class of all PatternContainers.
 *
 * @author Leegorous
 */
Class.create("csp.ui.PatternContainer","msg.ui.PatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	//toXML : new MethodAlias("_toXMLPatternImpl"),
	//_toXMLPatternImpl : function() {
	/*
	toXML : function() {
		var xml = [];
		if (this.enabled) {
			/**
			 * I don't know why it have to do such replace in original script.
			 * But I keep it.
			 * /
			var nodeName = this.name.replace(/_\d+/,'');
			xml.push('<'+nodeName+' type="SwiftSubFieldElement" typeName="'+this.node.type+'"');
			//-- output FinFormat
			if (this.node.FinFormat) xml.push(' FinFormat="'+this.node.FinFormat+'"');
			//-- output SepPrefix
			if (this.node.SepPrefix) xml.push(' prefix="'+this.node.SepPrefix.replace(/\n/g,"&#10;")+'"');
			//-- output SepSuffix
			if (this.node.SepSuffix) xml.push(' suffix="'+this.node.SepSuffix.replace(/\n/g,"&#10;")+'"');
			xml.push(">"+this.getValue()+"</"+nodeName+">");
		}
		return xml.join("");
	}*/
});﻿/**
 * @import csp.ui.PatternContainer;
 *
 * @class csp.ui.TextPatternContainer
 * @extends csp.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.TextPatternContainer","csp.ui.PatternContainer",null,{
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("TextPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("TextPatternContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyTextPatternImpl"),
	//_destroyTextPatternImpl : function() {
	destroy : function() {
		if (this._field) this._field.destroy();
		//this._destroyPatternImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Filtrate the input
	 * @param {Object} event the event object
	 * @param {int} charCode the charCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateInput:function(event,charCode,modifiers) {},
	/**
	 * Filtrate the text
	 * @param {Object} event the event object
	 * @param {int} keyCode the keyCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateText:function(event,keycode,modifiers) {},
	/**
	 * Get the value of this container.
	 * @returns {String} the value of this container.
	 */
	getValue:function() {
		return this._field.getText();
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//_initTextPatternImpl : new MethodAlias("_initPatternImpl"),
	//initBody : new MethodAlias("_initBodyTextPatternImpl"),
	//_initBodyTextPatternImpl:function() {
	initBody : function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this._field=new (Class.get("ui.TextBox"))(this._field);
		this.setMaxLength();
	},
	preventInput:function(event) {
		switch(event.keyCode) {
			case 8:
			case 9:
			case 37:
			case 38:
			case 39:
			case 40:break;
			//case 46:
			default:EvtMgr.preventDefault(event);
		}
	},
	setMaxLength:function() {
		var _max
		if (this.node.restriction && (_max = this.node.restriction.maxLength)>-1) {
			this._field.setMaxLength(_max);
			if (_max>10) this._field._e.style["width"]=Math.ceil(_max/5)*5*9+"px";
		}
	},
	setValue:function(v) {
		//-- if the body of this container has not been initialized, initialize it first.
		//if ((this.bit & ContainerTemplates.isOptional) && !this._field) this.initBody();
		if (this._field) this._field.setText(v);
	},
	//validate : new MethodAlias("_validateTextPatternImpl"),
	//_validateTextPatternImpl:function() {
	validate : function() {
		var _a;
		if (_a = this[arguments.callee.parent]()) {//this._validatePatternImpl()
			//-- check if the initial character is hyphen.
			if (!(_a=(this.getValue().charAt(0)!='-'))) this.setExtraInfo("cannotStartLineWithHyphen",_a);
		}
		return _a;
	},
	
	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		this._field.setReadOnly(readonly);
	}
	
	// deprecated
	/*
	_getMaxLength:function() {
		var _a;
		if ((_a=this.node.restriction) && (_a=_a.restriction)) {
			if (_a.maxLength) return parseInt(_a.maxLength);
			else if (_a.maxInclusive) return _a.maxInclusive.length;
		}
		return -1;
	}*/
});﻿/**
 * @import csp.ui.TextPatternContainer;
 *
 * @class csp.ui.AmountPatternContainer
 * @extends csp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.AmountPatternContainer","csp.ui.TextPatternContainer",null,{
	filtrateInput:function(event,charCode,modifiers) {
		if (this.readOnly) return;
		if (modifiers & Event.CONTROL_MASK) return;
		var _v=this.getValue();
		//var _a=this._field.getCursorPos();
		var _b=this._field.getSelectionLength();
		var _m=this.node.restriction.maxLength;
		/**
		 * If the input char is not in "0-9,.", then prevent it.
		 * The filter will convert "." to ",".
		 */
		if ((_v.length-_b<_m) && ((charCode<48 && charCode!=44) || charCode>57)) {
			this.preventInput(event);
			if (charCode==46) {
				var _a=this._field.getCursorPos();
				this.setValue(_v.slice(0,_a)+","+_v.slice(_a+_b));
				this._field.setSelectionRange(_a+1,0);
			}
		}
		//this._field.setSelectionRange(_a,_b);
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	/**
	 * Expand the current amount
	 */
	expand : function() {
		var _a = $doc.createElement("span");
		_a.style["color"]="#464646";
		_a.style["fontWeight"]="bold";
		_a.style["fontSize"]="11pt";
		var amount = this.getValue();
		if ((_c=this._parent.childs["Currency"]) && _c.getValue()!="JPY") amount+=this._getPadding(2-(amount.length-amount.indexOf(',')-1));
		_a.innerHTML = amount;
		this.setExtraInfo(_a);
	},
	_getPadding : function(i) {
		var s = "";
		while(i-->0) {
			s+="0";
		}
		return s;
	},
	/**
	 * Validate the value of this container.
	 * @return {Boolean} returns true if the value pass the validation, false if it does not, and the error infomation will show up.
	 */
	//validate : new MethodAlias("_validateAmountPatternImpl"),
	//_validateAmountPatternImpl : function() {
	validate : function() {
		var _a;
		if (_a=this[arguments.callee.parent]()) {
			var _b="",_c,re=this.node.restriction.pattern.slice(0,-1);
			if ((_c=this._parent.childs["Currency"]) && _c.getValue()=="JPY") re=re.slice(0,-5);
			else re+="{0,2}";
			re = new RegExp("^"+re+"$");
			if (!(_a=re.test(this.getValue()))) this.setExtraInfo("Invalid Amount.",true);
			else this.expand();
		}
		return _a;
		/*
		var _a,_b="",_c=false,re=this.node.restriction.pattern.slice(0,-1);
		if ((_a=this._parent["Currency"]) && _a.getValue()=="JPY") re=re.slice(0,-5);
		else re+="{0,2}";
		re = new RegExp("^"+re+"$");
		if (_c=!re.test(this.getValue())) _b="Invalid Amount.";
		this.setExtraInfo(_b,_c);
		return _c;*/
	}
});﻿/**
 * @import ui.ClickListener;
 *
 * @class csp.ui.BicExpansionSwitchListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.BicExpansionSwitchListener",null,["ui.ClickListener"],{
	destroy:function() {
		delete this._container;
		delete this.destroy;
	},
	/**
	 * @constructor
	 */
	init:function(container) {
		this._container=container;
	},
	onClick:function(event,sender) {
		sender.expanded=sender.expanded || false;
		var _c="bicExpandInfo";
		this._container.displayExpansion(sender.expanded=!sender.expanded);
		if (sender.expanded) sender.className=_c+" "+_c+"Expanded";
		else sender.className=_c+" "+_c+"Collapsed";
	}
});﻿/**
 * @import csp.ui.TextPatternContainer
 *
 * @class csp.ui.BICPatternContainer
 * @extends csp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.BICPatternContainer","csp.ui.TextPatternContainer",null,{
	/**
	 * Destroy this container
	 */
	destroy:function() {
		//if (this._expansion) DOM.detachNode(this._expansion);
		//delete this._callback;
		delete this._switcher;
		delete this._expansion;
		//this._destroyTextPatternImpl();
		this[arguments.callee.parent]();
	},
	displayExpansion:function(v) {
		if (v && this._expansionInfo) this.initExpansion();
		this._expansion.style["display"]=v?"":"none";
	},
	filtrateInput:function(event,charCode,modifiers) {
		if (this.readOnly) return;
		if (modifiers & Event.CONTROL_MASK) return;
		var _v=this.getValue();
		//var _a=this._field.getCursorPos();
		var _b=this._field.getSelectionLength();
		var _m=this.node.restriction.maxLength;
		/**
		 * If the input char is not in "a-zA-Z0-9", then prevent it.
		 * If the input char is in "a-z", then convert it to upper case.
		 */
		if ((_v.length-_b<_m) && (charCode<48 || (charCode>57 && charCode<65) || charCode>90)) {
			this.preventInput(event);
			if (charCode>96 && charCode<123) {
				var _a=this._field.getCursorPos();
				this.setValue(_v.slice(0,_a)+String.fromCharCode(charCode-32)+_v.slice(_a+_b));
				//_a++;_b=0;
				this._field.setSelectionRange(_a+1,0);
			}
		}
		//this._field.setSelectionRange(_a,_b);
	},
	init:function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//this._super(_node,_parentContainer);
		//this._callback=null;
		this._expansion=null;
	},
	initExpansion:function() {
		var _a;
		if (this._expansion) _a=this._expansion;
		else {
			_a=ContainerTemplates.get("BicExpansionContainer");
			DOM.insertAfter(_a,this._container);
		}
		var _b=_a.lastChild.firstChild.childNodes;
		var _c=this._expansionInfo;
		_b[0].lastChild.innerHTML=_c.institution;
		_b[1].lastChild.innerHTML=_c.branch;
		_b[2].lastChild.innerHTML=_c.city;
		_b[3].lastChild.innerHTML=_c.country;
		this._expansion=_a;
		this._expansionInfo=null;
		_a=_b=null;
	},
	setExpansion:function(expansion) {
		if (!expansion) {
			this.setExtraInfo("Unknown BIC");
			return;
		}
		if (!this._container) return;
		this._expansionInfo=expansion;
		var _c;
		if (this._switcher) _c=this._switcher;
		else {
			_c=$doc.createElement("a");
			_c.className="bicExpandInfo";
			_c.href="javascript:void(0);";
			_c.innerHTML="BIC Expanded";
			DOM.sinkEvent(_c,DOM.addEventListener(_c,"click", 
				new (Class.get("csp.ui.BicExpansionSwitchListener"))(this)));
			this._switcher=_c;
		}
		this.setExtraInfo(_c);
		_c=null;
	},
	//validate : new MethodAlias("_validateBICPatternImpl"),
	//_validateBICPatternImpl:function() {
	validate : function() {
		var _a;
		if (_a = this[arguments.callee.parent]()) {//this._validateTextPatternImpl()
			var _b=this.getValue();
			if (_b.length==8) this.setValue(_b+="XXX");
			if (_b!=this._previousValue) {
				this._previousValue = _b;
				this.setExtraInfo("Loading...");
				if (this._expansion) this._expansion.style["display"]="none";
				// add the container to BICManager.
				BICMgr.addListener(this);
			} else {
				this.setExpansion(BICMgr.getExpansion(_b));
			}
			/*
			// Get the expansion from system, cancel the previous request if necessary.
			if (this._callback) this._callback.cancel();
			var _c=new Request(PATH.GET_BIC_EXPANSION, 
				new (Class.get("csp.ui.GetBICExpansionCallback"))(this), 
				"POST");
			this._callback=_c;
			RequestPool.add(_c);*/
		}
		return _a;
	}
});﻿/**
 * @author Leegorous
 *
 * @import csp.ui.TextPatternContainer;
 *
 * @class csp.ui.BlockPatternContainer
 * @extends csp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.BlockPatternContainer","csp.ui.TextPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	initBody:function() {
		//this._initBodyTextPatternImpl();
		this[arguments.callee.parent]();
		//this._field._e.readOnly = true;
		this._field.setReadOnly(true);
		this._field.setValue(this.node.restriction.enumeration[0]);
	},
	/**
	 * This container is read only all the time, so it will ignore this action.
	 */
	_setFieldReadOnly : function() {}
});﻿/**
 * @class csp.ui.CommonContainerListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.CommonContainerListener",null,null,{
	findContainer:function(elem,level) {
		if (!elem) return null;
		var _a=elem,_c;
		/*
		 * The name of '_container' property is not proper to describe this property.
		 * '_ctnId' may be more suitable.
		 * And there is no need to pass the level argument any more.
		 */
		if (!(_c=CtnMgr.get(_a._ctnId))) {
			while(_a = _a.parentNode) {
				if (_a._ctnId && (_c = CtnMgr.get(_a._ctnId))) {
					elem._ctnId = _a._ctnId;
					break;
				}
			}
			/*
			while((level--)>0) {
				if (!(_a=_a.parentNode)) return null;
			}
			if (_c=CtnMgr.get(_a._ctnId)) {
				elem._ctnId=_a._ctnId;
			}*/
		}
		return _c;
	}
});﻿/**
 * @class csp.ui.CommonContainerRequestCallback
 * @implements http.RequestCallback
 *
 * @author Leegorous
 */
Class.create("csp.ui.CommonContainerRequestCallback",null,["http.RequestCallback"],{
	continuation:function() {},
	onError:function(request,response,exception) {
		this.continuation();
	},
	onResponseReceived:function(request,response) {}
});﻿/**
 * @import csp.ui.CommonContainerListener;
 * @import ui.ClickListener;
 *
 * @class csp.ui.ComplexContainerSwitchListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.ComplexContainerSwitchListener","csp.ui.CommonContainerListener", 
	["ui.ClickListener"],{
	onClick:function(event,sender) {
		var _a;
		if (_a=this.findContainer(sender,3)) {
		//if (_a && (_a=_a.parentNode) && (_a=_a.parentNode) && (_a=_a._container)) {
			//Console.put("Checkbox of '"+_a.name+"' has been clicked");
			_a.setEnabled(sender.checked);
		}
		_a=null;
	}
});﻿/**
 * @import ui.FocusListener;
 * @import ui.KeyboardListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.ComplexTextPatternValidateListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.FocusListener, ui.KeyboardListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.ComplexTextPatternValidateListener","csp.ui.CommonContainerListener", 
	["ui.FocusListener","ui.KeyboardListener"],{
	ignoreKeys:{
		"8":true,// BackSpace
		"9":true,// Tab
		"37":true,// Left
		"38":true,// Up
		"39":true,// Right
		"40":true// Down
		//"46":false// Delete
	},
	onFocus:function(event,sender) {},
	onLostFocus:function(event,sender) {
		var _a=this.findContainer(sender,3);
		if (_a) _a.validate();
	},
	onKeyDown:function(event,sender,keycode,modifiers) {
		//alert(keycode);
	},
	onKeyPress:function(event,sender,charcode,modifiers) {
		if (this.ignoreKeys[event.keyCode]) return;
		var _a=this.findContainer(sender,3);
		if (_a) _a.filtrateInput(event,charcode,modifiers);
	},
	onKeyUp:function(event,sender,keycode,modifiers) {
		if (this.ignoreKeys[event.keyCode]) return;
		var _a=this.findContainer(sender,3);
		if (_a) _a.filtrateText(event,keycode,modifiers);
	}
});/**
 * @import msg.ui.CompositeContainer;
 *
 * @class csp.ui.CompositeContainer
 * @extends msg.ui.CompositeContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.CompositeContainer","msg.ui.CompositeContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initCompositeImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	/**
	 * Get the XML string
	toXML : function() {
		var xml=[], childsXml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || this.ownerDocument.status==Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				xml.push('<'+this.name+' type="SwiftFieldElement" typeName="'+this.node.type+'"');
				if (this.node.Separator) xml.push(' separator="'+this.node.Separator.replace(/\n/g,"&#10;")+'"');
				xml.push('>'+childsXml+'</'+this.name+'>');
			}
		}
		return xml.join("");
	}
	 */
});/**
 * @import msg.ui.ContainerBuilder;
 *
 * @class csp.ui.ContainerBuilder
 * @extends msg.ui.ContainerBuilder
 *
 * @author Leegorous
 */
Class.create("csp.ui.ContainerBuilder", "msg.ui.ContainerBuilder", null,{

	getContainerClassName : function(node) {
		if (!node.base) return "csp.ui."+node._ui+"PatternContainer";
		else return this._containerMap[node.base];
	},
	
	getObserverClassName : function(node) {
		if (!node.base) return "msg.ui.impl.PatternObserver";
		else return this._observerMap[node.base];
	},
	/*
	build : function(_node, _parentContainer) {
		var name;
		//if (!_node.base) return new (Class.get("csp.ui."+_node._ui+"PatternContainer"))(_node,_parentContainer);
		if (!_node.base) name = "csp.ui."+_node._ui+"PatternContainer";
		else name = this.conf[_node.base];
		//return this.conf[_node.base](_node,_parentContainer);
		return new (Class.get(name))(_node,_parentContainer);
	},
	buildChild:function(_node,_parentContainer) {
		var _a,_b;
		if (!(_a=_node.childs)) return;
		for (var i=0,j=_a.length;i<j;i++) {
			this.build(_a[i], _parentContainer);
		}
	},*/
	_containerMap : (function() {
		var map = {
			Block : "csp.ui.FinBlockContainer",
			CompositeField : "csp.ui.CompositeContainer",
			FieldOption : "csp.ui.OptionContainer",
			Loop : "csp.ui.SequenceContainer",
			LoopItem : "csp.ui.SequenceItemContainer",
			Message : "csp.ui.MainContainer"
		};

		map.OptionField = map.FieldOption;
		map.Sequence = map.Loop;
		map.SequenceItem = map.LoopItem;
		map.SubSequence = map.Loop;
		map.SubSequenceItem = map.LoopItem;
		map.SequenceOption = map.FieldOption;

		return map;
	})(),

	_observerMap : (function() {
		var map = {
			Block : "msg.ui.impl.CommonObserver",
			CompositeField : "msg.ui.impl.CommonObserver",
			FieldOption : "msg.ui.impl.OptionFieldObserver",
			Loop : "msg.ui.impl.CommonObserver",
			LoopItem : "msg.ui.impl.CommonObserver",
			Message : "msg.ui.impl.CommonObserver"
		};

		map.OptionField = map.FieldOption;
		map.Sequence = map.Loop;
		map.SequenceItem = map.Loop;
		map.SubSequence = map.Loop;
		map.SubSequenceItem = map.Loop;
		map.SequenceOption = map.FieldOption;

		return map;
	})()
});

//var CtnBuilder = new (Class.get("csp.ui.ContainerBuilder"))();
/*

(function() {
	var _conf = CtnBuilder.conf;
	_conf.OptionField=_conf.FieldOption;
	_conf.Sequence=_conf.Loop;
	_conf.SubSequence=_conf.Loop;
	_conf.SequenceOption=_conf.FieldOption;
})();*/﻿/**
 * @class csp.ui.ContainerManager
 *
 * @author Leegorous
 */
Class.create("csp.ui.ContainerManager", null, null, {$tatic:{
	_stack : {},
	_containerId : 1,
	/**
	 * Add a container to the ContainerManager
	 * @param {SimpleContainer} container
	 * @returns the id of this container
	 */
	add : function(container) {
		this._stack[this._containerId] = container;
		return this._containerId++;
	},
	eachContainer : function(iterator) {
		var _c;
		for (var i in this._stack) {
			_c = this._stack[i];
			if (_c && typeof _c=="object") iterator(_c);
		}
	},
	/**
	 * Get the container with the specific id
	 * @param {int} id The container id
	 * @returns {SimpleContainer} container null if no container with this id
	 */
	get : function(id) {
		return this._stack[id] || null;
	},
	/**
	 * Remove a container with the specific id
	 */
	remove : function(id) {
		delete this._stack[id];
	},
	/**
	 * Search container with specific string
	 * @param {String} v
	 */
	search : function(owner, v) {
		var _a = [];
		this.eachContainer(function(container) {
			if (owner==container._ownerMessage && container.match(v)) _a.push(container);
		});
		return _a;
	}
}});﻿/*
 * ContainerTemplate is used to store the container templates
var ContainerTemplate={
	_templates:{},
	get:function(key) {
		return this._templates[key]||null;
	},
	init:function() {
		var compositeFieldTemplateBase=$doc.createElement("div");
		this.set("CompositeField0",obj);
		this.set("CompositeField1",obj);
	},
	set:function(key,obj) {
		if (!key || !obj) return;
		if (key.constructor==Array) {
			for (var i=key.length-1;i>=0;i--) {
				this._templates[key[i]]=obj;
			}
		} else this._templates[key]=obj;
	}
};
 */

//-- the container of message templates
var ContainerTemplates={
	isOptional:1,
	hasFormat:2,
	hasDescription:4,
	increasable:8,
	removable:16,
	conf:{},
	get:function(name) {
		if (this[name]) return this[name].cloneNode(true);
		else return null;
	},
	init:function() {
		var begin=(new Date()).getTime();
		//-- initialize the implements
		var _a=this.initImpl,_b=_a.PatternField,_c;
		//_a.ListPatternField=_b;
		//_a.TextAreaPatternField=_b;
		//_a.TextPatternField=_b;
		_b=null;
		var _a=XML.removeWhiteSpace($("messageContainerTemplates"),true);
		if (_a.hasChildNodes()) {
			var j=_a.childNodes.length;
			for (var i=0; i<j; i++) {
				var _b=_a.childNodes[i];
				this[_b.getAttribute("type")]=_b;
			}
			for (var i=j-1; i>=0; i--) {
				var _b=_a.childNodes[i];
				_c=_b.getAttribute("type");
				if (this.initImpl[_c] && this.conf[_c]) this.initImpl[_c](this,_b,this.conf[_c]);
			}
		}
		var _b=$doc.createElement("span");
		_b.className="fieldExtraInfo";
		this["FieldExtraInfo"]=_b;
		_b=_c=null;
		Console.put("Message templates initialized in "+((new Date()).getTime()-begin)+"ms");
	},
	initImpl:{
		/*
		"AmountPatternContainer":function(container,nodeTemplate,bits) {
			if (!container || !nodeTemplate || !bits) return;
			var bit,node,$1;
			for (var i=0,j=bits.length; i<j; i++) {
				bit=bits[i];node=nodeTemplate.cloneNode(true);
				$1=node.firstChild;
				if (bit & 0x00001) {
					if (bit!=0x00003) node=container["AmountPatternContainer3"];
					$1.firstChild.setAttribute("__evt",ContainerEvents.getEventId("ContainerSwitcher"));
					$1.firstChild.setAttribute("__events",ContainerEvents.getEventBit("ContainerSwitcher"));
				} else {
					//-- mandatory pattern field
					$1.removeChild($1.firstChild);
					node.appendChild(container.get("AmountPatternField"+bit));
				}
				container["AmountPatternContainer"+bit]=node;
			}
			node=$1=null;
		},*/
		"CompositeContainer" : function(container, nodeTemplate, bits) {
			if (!container || !nodeTemplate || !bits) return;
			var bit,node,$1;//,$2;
			for (var i=0,j=bits.length; i<j; i++) {
				bit=bits[i];
				node=nodeTemplate.cloneNode(true);
				$1=node.firstChild;
				//$2=node.lastChild;
				if (bit & container.isOptional) {
					DOM.detachNode(node.lastChild);
					$1.firstChild.firstChild.setAttribute("__evt",ContainerEvents.getEventId("ComplexContainerSwitcher"));
				} else {
					DOM.detachNode($1.firstChild.firstChild);
				}
				container["CompositeContainer"+bit]=node;
			}
			node=$1=null;
		},
		"OptionContainer" : function(container, nodeTemplate, bits) {
			if (!container || !nodeTemplate || !bits) return;
			var bit,node,$1;//,$2;
			for (var i=0,j=bits.length; i<j; i++) {
				bit=bits[i];
				node=nodeTemplate.cloneNode(true);
				$1=node.firstChild;
				//$2=node.lastChild;
				if (bit & container.isOptional) {
					DOM.detachNode(node.lastChild);
					$1.firstChild.firstChild.setAttribute("__evt",ContainerEvents.getEventId("ComplexContainerSwitcher"));
				} else {
					DOM.detachNode($1.firstChild.firstChild);
					DOM.insertAfter(container.get("OptionContainerSelector"),$1.lastChild.firstChild);
					$1.lastChild.childNodes[1].firstChild.setAttribute("__evt",ContainerEvents.getEventId("OptionContainerSwitcher"));
				}
				container["OptionContainer"+bit]=node;
			}
			node=$1=null;
		},
		"SequenceContainer" : function(container,nodeTemplate,bits) {
			if (!container || !nodeTemplate || !bits) return;
			var bit,node,$1,$2,$3;
			for (var i=0,j=bits.length; i<j; i++) {
				bit = bits[i];
				node = nodeTemplate.cloneNode(true);
				$1 = node.firstChild.firstChild;
				$2 = $1.nextSibling;
				$3 = $2.nextSibling;
				if (bit & container.increasable) {
					//-- delete the checkbox
					//DOM.detachNode($1.firstChild);
					DOM.detachNode($1.firstChild);
					//-- add event bit to the new occur button.
					$3.lastChild.setAttribute("__evt",ContainerEvents.getEventId("NewOccurrence"));
				} else {
					if (bit & container.isOptional) {
						//-- add event bit to the checkbox.
						$1.firstChild.setAttribute("__evt",ContainerEvents.getEventId("ComplexContainerSwitcher"));
						//DOM.detachNode($2.lastChild);
						//DOM.detachNode($3.lastChild);
					} else {
						//-- delete the checkbox
						DOM.detachNode($1.firstChild);
					}
					//-- delete the new occur button.
					DOM.detachNode($3.lastChild);
				}
				$3.firstChild.setAttribute("__evt",ContainerEvents.getEventId("ExpandSequenceContainer"));
				container["SequenceContainer"+bit]=node;
			}
			node = $1 = $2 = $3 = null;
		},
		"SequenceItemContainer" : function(container,nodeTemplate,bits) {
			var bit,node,removeBtn;
			for (var i=0,j=bits.length; i<j; i++) {
				bit = bits[i];
				node = nodeTemplate.cloneNode(true);
				removeBtn = node.firstChild.lastChild.firstChild;
				if (bit & container.removable) {
					removeBtn.setAttribute("__evt",ContainerEvents.getEventId("RemoveSequenceItem"));
					removeBtn = null;
				} else {
					DOM.detachNode(removeBtn);
				}
				container["SequenceItemContainer"+bit]=node;
			}
			node=removeBtn=null;
		},
		"PatternContainer":function(container,nodeTemplate,bits) {
			if (!container || !nodeTemplate || !bits) return;
			var type=["Boolean","CheckList","ComplexText","Text","TextArea","List"];
			//var type=["Text","TextArea","List"];
			var bit,node,$1;
			for (var m=0; m<type.length; m++) {
				if (!container[type[m]+"PatternField"]) continue;
				for (var i=0,j=bits.length; i<j; i++) {
					bit=bits[i];node=nodeTemplate.cloneNode(true);
					$1=node.firstChild;
					//$3=node.appendChild(container[type[m]+"PatternField"+bit].cloneNode(true));
					if (bit & 0x00001) {
						if (bit!=0x00001) node=container[type[m]+"PatternContainer1"];
						$1.firstChild.setAttribute("__evt",ContainerEvents.getEventId("PatternContainerSwitcher"));
						/**
						 * By testing the event fired sequence, only the click event will be fired
						 * immediately when the check box value has been changed in IE and Firefox.
						 * The change event can do that in Firefox also, but in IE, it will be fired
						 * only before the check box lose focus and its value has been changed,
						 * not so immediately.
						 * The user needs instant feedback after this operation.
						 */
						//$1.firstChild.setAttribute("__events",ContainerEvents.getEventBit("ContainerSwitcher"));
					} else {
						//-- mandatory pattern field
						$1.removeChild($1.firstChild);
						node.appendChild(container.get(type[m]+"PatternField"+bit));
					}
					container[type[m]+"PatternContainer"+bit]=node;
				}
			}
			node=$1=null;
		},
		"PatternField":function(container,nodeTemplate,bits) {
			var bit,node,$2,$3;
			for (var i=0,j=bits.length; i<j; i++) {
				bit=bits[i];node=nodeTemplate.cloneNode(true);
				$3=node.lastChild,$2=$3.previousSibling;
				//-- mandatory field
				if (!(bit & 0x00001)) node.className+=" mandatoryField";
				//-- no format info
				if (!(bit & 0x00002)) node.removeChild($2);
				//-- no description
				if (!(bit & 0x00004)) node.removeChild($3);
				container[node.getAttribute("type")+bit]=node;
			}
			node=$2=$3=null;
		},/*
		"AmountPatternField":function(container,nodeTemplate,bits) {
			var bit,node,$2,$3;
			var $1=nodeTemplate.firstChild;
			$1.setAttribute("__evt",ContainerEvents.getEventId("CommonTextPattern"));
			$1.setAttribute("__events",ContainerEvents.getEventBit("CommonTextPattern"));
			for (var i=0,j=bits.length; i<j; i++) {
				bit=bits[i];node=nodeTemplate.cloneNode(true);
				$3=node.lastChild,$2=$3.previousSibling;
				//-- mandatory field
				if (!(bit & 0x00001)) node.className+=" mandatoryField";
				//-- the format of Amount field is mandatory "15d"
				//-- no format info
				//if (!(bit & 0x00002)) node.removeChild($2);
				//-- no description
				if (!(bit & 0x00004)) node.removeChild($3);
				container[node.getAttribute("type")+bit]=node;
			}
			node=$1=$2=$3=null;
		},*/
		"ListPatternField":function(container,nodeTemplate,bits) {
			nodeTemplate=nodeTemplate.cloneNode(true);
			var $1=nodeTemplate.firstChild;
			$1.setAttribute("__evt",ContainerEvents.getEventId("CommonListPattern"));
			//$1.setAttribute("__events",ContainerEvents.getEventBit("CommonListPattern"));
			this.PatternField(container,nodeTemplate,bits);
			$1=null;
		},
		"TextAreaPatternField":function(container,nodeTemplate,bits) {
			nodeTemplate=nodeTemplate.cloneNode(true);
			var $1=nodeTemplate.firstChild;
			$1.setAttribute("__evt",ContainerEvents.getEventId("CommonTextAreaPattern"));
			//$1.setAttribute("__events",ContainerEvents.getEventBit("CommonTextAreaPattern"));
			this.PatternField(container,nodeTemplate,bits);
			$1=null;
		},
		"TextPatternField":function(container,nodeTemplate,bits) {
			nodeTemplate=nodeTemplate.cloneNode(true);
			var $1=nodeTemplate.firstChild;
			$1.setAttribute("__evt",ContainerEvents.getEventId("CommonTextPattern"));
			//$1.setAttribute("__events",ContainerEvents.getEventBit("CommonTextPattern"));
			this.PatternField(container,nodeTemplate,bits);
			$1=null;
		},
		"BooleanPatternField":function(container,nodeTemplate,bits) {
			nodeTemplate=nodeTemplate.cloneNode(true);
			//var $1=nodeTemplate.firstChild;
			//$1.setAttribute("__evt",ContainerEvents.getEventId("CommonTextPattern"));
			//$1.setAttribute("__events",ContainerEvents.getEventBit("CommonTextPattern"));
			this.PatternField(container,nodeTemplate,bits);
			//$1=null;
		},
		"CheckListPatternField":function(container,nodeTemplate,bits) {
			nodeTemplate=nodeTemplate.cloneNode(true);
			//var $1=nodeTemplate.firstChild;
			//$1.setAttribute("__evt",ContainerEvents.getEventId("CommonTextPattern"));
			//$1.setAttribute("__events",ContainerEvents.getEventBit("CommonTextPattern"));
			this.PatternField(container,nodeTemplate,bits);
			//$1=null;
		},
		"ComplexTextPatternField" : function(container,nodeTemplate,bits) {
			nodeTemplate=nodeTemplate.cloneNode(true);
			var $1=nodeTemplate.firstChild.firstChild;
			$1.setAttribute("__evt",ContainerEvents.getEventId("CommonComplexTextPattern"));
			//$1.setAttribute("__events",ContainerEvents.getEventBit("CommonTextPattern"));
			this.PatternField(container,nodeTemplate,bits);
			//$1=null;
		}
	},
	destroy:function() {
		for (var i in this) delete this[i];
	}
};
/**
 * The configuration for the container templates
 */
ContainerTemplates.conf={
	//"AmountPatternContainer":[ContainerTemplates.hasFormat, ContainerTemplates.isOptional|ContainerTemplates.hasFormat, ContainerTemplates.hasFormat|ContainerTemplates.hasDescription, ContainerTemplates.isOptional|ContainerTemplates.hasFormat|ContainerTemplates.hasDescription],
	"CompositeContainer" : [0, ContainerTemplates.isOptional, ContainerTemplates.hasDescription,
		ContainerTemplates.isOptional|ContainerTemplates.hasDescription],
	"SequenceContainer" : [0, ContainerTemplates.isOptional, ContainerTemplates.hasDescription,
		ContainerTemplates.isOptional|ContainerTemplates.hasDescription, ContainerTemplates.increasable, 
		ContainerTemplates.isOptional|ContainerTemplates.increasable, 
		ContainerTemplates.isOptional|ContainerTemplates.hasDescription|ContainerTemplates.increasable, 
		ContainerTemplates.hasDescription|ContainerTemplates.increasable],
	"SequenceItemContainer" : [0, ContainerTemplates.removable],
	"PatternContainer":[0, ContainerTemplates.isOptional, ContainerTemplates.hasFormat, 
		ContainerTemplates.isOptional|ContainerTemplates.hasFormat, 
		ContainerTemplates.hasDescription, 
		ContainerTemplates.isOptional|ContainerTemplates.hasDescription, 
		ContainerTemplates.hasFormat|ContainerTemplates.hasDescription, 
		ContainerTemplates.isOptional|ContainerTemplates.hasFormat|ContainerTemplates.hasDescription]
};
ContainerTemplates.conf["OptionContainer"]=ContainerTemplates.conf["CompositeContainer"];
ContainerTemplates.conf["PatternField"]=ContainerTemplates.conf["ListPatternField"]=ContainerTemplates.conf["TextAreaPatternField"]=ContainerTemplates.conf["TextPatternField"]=ContainerTemplates.conf["BooleanPatternField"]=ContainerTemplates.conf["CheckListPatternField"]=ContainerTemplates.conf["ComplexTextPatternField"]=ContainerTemplates.conf["PatternContainer"];
ContainerTemplates.conf["AmountPatternField"]=ContainerTemplates.conf["AmountPatternContainer"];﻿/**
 * @import csp.ui.TextPatternContainer;
 *
 * @class csp.ui.CurrencyPatternContainer
 * @extends csp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.CurrencyPatternContainer","csp.ui.TextPatternContainer",null,{
	filtrateInput:function(event,charCode,modifiers) {
		if (this.readOnly) return;
		if (modifiers & Event.CONTROL_MASK) return;
		var _v=this.getValue();
		//var _a=this._field.getCursorPos();
		var _b=this._field.getSelectionLength();
		var _m=this.node.restriction.maxLength;
		/**
		 * If the input char is not in "a-zA-Z", then prevent it.
		 * If the input char is in "a-z", then convert it to upper case.
		 */
		if ((_v.length-_b<_m) && (charCode<65 || charCode>90)) {
			this.preventInput(event);
			if (charCode>96 && charCode<123) {
				var _a=this._field.getCursorPos();
				this.setValue(_v.slice(0,_a)+String.fromCharCode(charCode-32)+_v.slice(_a+_b));
				//_a++;_b=0;
				this._field.setSelectionRange(_a+1,0);
			}
		}
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//this._callback=null;
	},
	setExpansion:function(expansion) {
		if (!expansion) this.setExtraInfo("");
		else this.setExtraInfo(expansion);
	},
	//validate : new MethodAlias("_validateCurrencyPatternImpl"),
	//_validateCurrencyPatternImpl:function() {
	validate : function() {
		var _a;
		if (_a = this[arguments.callee.parent]()) {//this._validateTextPatternImpl()
			var _b=this.getValue();
			if (_b!=this._previousValue) {
				this._previousValue = _b;
				this.setExtraInfo("Loading...");
				if (this._parent.childs["Amount"]) this._parent.childs["Amount"].validate();
				// add the container to CurrencyManager.
				CurMgr.addListener(this);
			} else {
				this.setExpansion(CurMgr.getExpansion(_b));
			}
		}
		return _a;
	}
});﻿/**
 * @import csp.ui.TextPatternContainer;
 *
 * @class csp.ui.DatePatternContainer
 * @extends csp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.DatePatternContainer","csp.ui.TextPatternContainer",null,{
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyDatePatternImpl"),
	//_destroyDatePatternImpl : function() {
	destroy : function() {
		if (this._field && this._field._e._calendar) this._field._e._calendar = null;
		//this._destroyTextPatternImpl();
		this[arguments.callee.parent]();
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	initBody:function() {
		//this._initBodyTextPatternImpl();
		this[arguments.callee.parent]();
		this._field.setAttribute("_format",this.node.FinFormat);
		this._field.setAttribute("__evt",ContainerEvents.getEventId("CommonDatePattern"));
	},
	//validate : new MethodAlias("_validateDatePatternImpl"),
	//_validateDatePatternImpl : function() {
	validate : function() {
		var _a;
		if (_a = this[arguments.callee.parent]()) {//this._validateTextPatternImpl()
			var str = "",_c = this._field._e._calendar, date;
			if (_c) date=_c._currentDate;
			else {
				date = this.getValue();
				var format = this.node.FinFormat || "yyyy-MM-DD";
				var _y = (new Date()).getFullYear();
				if (format.length==10) {
					format = "YYYYMMDD";
					if (date.length==19) date = date.substring(0,date.indexOf('T'));
					date = date.replace(/\-/g,'');
				}
				if (format.length==8) {
					_y = parseInt(date.substring(0,4));
					date = date.substring(4);
				} else if (format.length==6) {
					_y = parseInt(date.substring(0,2));
					if (_y>79) _y+=1900;
					else _y+=2000;
					date = date.substring(2);
				}
				var _m = parseInt(date.substring(0,2))-1;
				var _d = parseInt(date.substring(2));
				date = new Date(_y,_m,_d);
			}
			if (date) str=Format.formatDate(new Date(date),"m-dd-YY");
			this.setExtraInfo(str);
		}
		return _a;
	}
});﻿/**
 * @import ui.FocusListener;
 * @import ui.KeyboardListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.TextPatternValidateListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.FocusListener, ui.KeyboardListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.TextPatternValidateListener","csp.ui.CommonContainerListener", 
	["ui.FocusListener","ui.KeyboardListener"],{
	ignoreKeys:{
		"8":true,// BackSpace
		"9":true,// Tab
		"37":true,// Left
		"38":true,// Up
		"39":true,// Right
		"40":true// Down
		//"46":false// Delete
	},
	onFocus:function(event,sender) {},
	onLostFocus:function(event,sender) {
		var _a=this.findContainer(sender,2);
		if (_a) _a.validate();
	},
	onKeyDown:function(event,sender,keycode,modifiers) {
		//alert(keycode);
	},
	onKeyPress:function(event,sender,charcode,modifiers) {
		if (this.ignoreKeys[event.keyCode]) return;
		var _a=this.findContainer(sender,2);
		if (_a) _a.filtrateInput(event,charcode,modifiers);
	},
	onKeyUp:function(event,sender,keycode,modifiers) {
		if (this.ignoreKeys[event.keyCode]) return;
		var _a=this.findContainer(sender,2);
		if (_a) _a.filtrateText(event,keycode,modifiers);
	}
});﻿/**
 * @import csp.ui.TextPatternValidateListener;
 *
 * @class csp.ui.DatePatternValidateListener
 * @extends csp.ui.TextPatternValidateListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.DatePatternValidateListener","csp.ui.TextPatternValidateListener",null,{
	onChange : function(event, sender) {
		var _calendar=sender._calendar;
		if (!_calendar) return;
		_calendar._changeListener(event);
		_calendar=null;
	},
	onFocus : function(event, sender) {
		var _calendar,_a;
		_a =this.findContainer(sender,2);
		if (!_a || _a.readOnly) return;
		if (!(_calendar=sender._calendar)) {
			sender._calendar=_calendar=new (Class.get("ui.Calendar"))(sender);
			_calendar.validate=function() {_a.validate()};
			DOM.sinkEvent(sender,ContainerEvents.getEventId("CommonDatePattern"));
		}
		if ($win.$currentCalendar && $win.$currentCalendar != _calendar) 
			$win.$currentCalendar.hide();
		$win.$currentCalendar=_calendar;
		_calendar.stopListener();
		_calendar.show();
		_calendar.startListener();
		_calendar=null;
	},
	onLostFocus : function(event, sender) {
		var _calendar;
		if (!(_calendar=sender._calendar)) return;
		_calendar.checkLater();
		var _a=this.findContainer(sender,2);
		if (!_a) return;
		(new Timer(function(){_a.validate();this.destroy();})).schedule(200);
		_calendar=null;
	}
});﻿/**
 * @class csp.ui.ExpansionManager
 * BICManager will take care of getting expansions.
 *
 * @author Leegorous
 */
Class.create("csp.ui.ExpansionManager",null,null,{
	/**
	 * The default interval of the checker who will send the request for the BIC expansion.
	 * @type int
	 */
	DEFAULT_INTERVAL : 3000,
	//_sink : {},
	//_listeners : [],
	//_listenerQueue : [],
	/**
	 * Add the listener to BICManager
	 * @param {Object} container the container who listen to the result of BIC expansion.
	 */
	addListener : function(container) {
		this._listeners.push(container);
		this.checkLater();
	},
	/**
	 * Run the checker immediately
	 */
	check : function() {
		if (this._schedule && this._schedule.isRunning) this._schedule.cancel();
		this._check();
	},
	/**
	 * Set a trigger for the time-lapse checker.
	 */
	checkLater : function(interval) {
		if (!this._schedule) this._schedule=new Timer("_check",this);
		if (this._schedule.isRunning) return;
		this._schedule.schedule(interval || this.DEFAULT_INTERVAL);
	},
	/**
	 * Return the expansion of the specific BIC if the BICManager already have it.
	 * @param {String} bic the specific BIC
	 */
	getExpansion : function(_name) {
		return this._sink[_name] || null;
	},
	init:function(type, path, callbackClassName) {
		this._type=type;
		this._path=path;
		this._callbackClass=callbackClassName;
		this._sink = {};
		this._listeners = [];
		this._listenerQueue = [];
	},
	/**
	 * Add expansions to BICManager.
	 * Generally, it should be called by the callback of BIC expansions request.
	 */
	setExpansions : function(obj) {
		if (obj) Object.extend(this._sink,obj);
		var _a=this._listenerQueue,_cont,_name;
		this._listenerQueue=[];
		for (var i=0,j=_a.length; i<j; i++) {
			_cont=_a[i];
			if (_cont[_cont.validate.parent]()) {//_a[i]._validateTextPatternImpl()
				_name=_cont.getValue();
				_cont.setExpansion(this._sink[_name]);
				//if (this._sink[_name]) _cont.setExpansion(this._sink[_name]);
				//else _cont.setExtraInfo("Unknown "+this._type);
			}
		}
		_a=_cont=_name=null;
	},
	/**
	 * private
	 * The BIC expansions checker
	 */
	_check : function() {
		var _a=this._listeners.uniq();
		this._listeners=[];
		if (_a.length==0) return _a=null;
		var _b=[],_c=[],_cont,_name='';
		for (var i=0,j=_a.length; i<j; i++) {
			_cont=_a[i];
			
			if (_cont[_cont.validate.parent]()) {//_cont._validateTextPatternImpl()
				_name=_cont.getValue();
				if (this._sink[_name]) {
					_cont.setExpansion(this._sink[_name]);
				} else {
					_b.push(_name);
					_c.push(_cont);
				}
			}
		}
		_a=null;
		if (_b.length==0) return _b=_c=null;
		var _r=new Request(this._path, 
			//new (Class.get("csp.ui.GetBICExpansionCallback"))(), 
			new (Class.get(this._callbackClass))(), 
			"POST");
		_r.addParam("list",_b.uniq().join(','));
		RequestPool.add(_r);
		this._listenerQueue=_c;
		_b=_c=_cont=_name=_r=null;
	}
});﻿/**
 * @import csp.ui.PatternContainer;
 *
 * @class csp.ui.TextAreaPatternContainer
 * @extends csp.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.TextAreaPatternContainer","csp.ui.PatternContainer",null,{
	createContainer:function() {
		return ContainerTemplates.get("TextAreaPatternContainer"+this.bit);
	},
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("TextAreaPatternField"+this.bit));
	},
	/**
	 * when focus on this field, the timer of this container should be canceled.
	 */
	cancelTimer:function() {
		if (this._timer) {
			this._timer.cancel();
			this._timer.schedule(10);
		}
		//this._timer=null;
	},
	initBody:function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this.setDimension();
		/**
		 * the setAttribute method and _getColumnNumber method have performance problems.
		 */
		/*
		var _format=this.format;
		var _i=_format.indexOf('*');
		this._field.setAttribute("rows",parseInt(_format.substring(0,_i))||1);
		//this._getColumnNumber(_format.substring(_i+1));
		this._field.setAttribute("cols",this._getColumnNumber(_format.substring(_i+1)));*/
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._timer=null;
		//this._initTextAreaPatternImpl(_node,_parentContainer);
	//},
	//_initTextAreaPatternImpl : function(_node,_parentContainer) {
		//this._initPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this._timer=null;
	},
	/**
	 * Refine the text of the textarea field.
	 * The text could not include "\r", "\f", "\v".
	 * Hyphen "-" could not lead a line.
	 */
	refineText:function() {
		var elem=this._field;
		var v = elem.value, original = v;
		//var originalLength=v.length;
		v = v.replace(/[\r\f\v]/g,'').replace(/\n{2,}|\n[-]/g,'\n').replace(/\t/g,' ').trim();//[ ]{2,}|
		//var end;
		//if ((end=v.length)==0) return elem=null;
		if (v.length==0) return elem=null;
		//end--;
		//var row=elem.rows;
		//var col=elem.cols-1;
		elem.value = this._textFilter(elem.rows, elem.cols-1, v);
		/*
		var result=[];
		//var idx0=0,idx1=v.indexOf('\n'),idx3;
		var idx0=0,idx1=0,idx2=0;
		//-- re0 : represent characters that can not lead a line;
		var re0=/[-\n]/;
		//-- idx0 : always current position
		while(idx0<end) {
			while (re0.test(v.charAt(idx0))) idx0++;
			idx1=v.indexOf('\n',idx0);
			if (idx1==-1) idx1=end;
			if (idx1-idx0>col) {
				//-- long line;
				//-- idx1 : exact position
				//-- idx2 : predicted position
				idx2=idx0+col;
				if (v.charAt(idx2)==' ') {
					idx1=idx2+1;
				} else {
					idx1=v.lastIndexOf(' ',idx2);
					if (idx1<idx0) idx1=idx2;
					else idx1++;
				}
			}
			result.push(v.slice(idx0,idx1));
			idx0=idx1;
		}
		if (result.length>row) result.length=row;
		elem.value=result.join('\n');*/
		//var resultLength=elem.value.length;
		//if (resultLength<originalLength) {
		var _r=true,_info="";
		if (elem.value!=original) {
			_info = "Text refined ";
			/*
			if (this._timer) this._timer.cancel();
			else {
				var o=this;
				this._timer=new Timer(function() {o.setExtraInfo('');o=o._timer=null;});
			}
			this._timer.schedule(3000);*/
		}
		if (!(_r=this.validate())) _info+=(_info.length>0?"And ":"")+"Invalid";
		this.setExtraInfo(_info,!_r);
		if (_r) this._hideTipsLater();
		original=elem=v=null;
	},
	_hideTipsLater : function() {
		if (this._timer) this._timer.cancel();
		else {
			var o=this;
			this._timer=new Timer(function() {o.setExtraInfo('');o=o._timer=null;});
		}
		this._timer.schedule(3000);
	},
	setDimension:function() {
		this._field.setAttribute("rows",this.node.rows);
		this._field.setAttribute("cols",this.node.cols+1);
	},
	//validate : new MethodAlias("_validatePatternImpl"),
	
	_textFilter : function(row, col, v) {
		var result=[],end = v.length;
		//var idx0=0,idx1=v.indexOf('\n'),idx3;
		var idx0=0,idx1=0,idx2=0;
		//-- re0 : represent characters that can not lead a line;
		var re0=/[-\n]/;
		//-- idx0 : always current position
		while(idx0<end) {
			while (re0.test(v.charAt(idx0))) idx0++;
			idx1=v.indexOf('\n',idx0);
			if (idx1==-1) idx1=end;
			if (idx1-idx0>col) {
				//-- long line;
				//-- idx1 : exact position
				//-- idx2 : predicted position
				idx2=idx0+col;
				if (v.charAt(idx2)==' ') {
					idx1=idx2+1;
				} else {
					idx1=v.lastIndexOf(' ',idx2);
					if (idx1<idx0) idx1=idx2;
					else idx1++;
				}
			}
			result.push(v.slice(idx0,idx1));
			idx0=idx1;
		}
		if (result.length>row) result.length=row;
		return result.join('\n');
	}
	//_validateTextAreaPatternImpl : new MethodAlias("_validatePatternImpl")
	/**
	 * deprecated
	_getColumnNumber:function(str) {
		var value=0;
		var str=str.gsub(/\d+!?(n|a|c|x)/,function(match) {value+=parseInt(match,10);return '';});
		return value+=str.length;
	}*/
});﻿/**
 * @author Leegorous
 * 
 * @import csp.ui.TextAreaPatternContainer;
 *
 * @class csp.ui.F77EPatternContainer
 * @extends csp.ui.TextAreaPatternContainer
 */
Class.create("csp.ui.F77EPatternContainer","csp.ui.TextAreaPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextAreaPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	/**
	 * Refine the text of the textarea field.
	 * The text could not include "\r", "\f", "\v".
	 * Hyphen "-" could not lead a line.
	 */
	refineText:function() {
		var elem=this._field;
		var v = elem.value, original = v;
		//var originalLength=v.length;
		v = v.replace(/[\r\f\v]/g,'').replace(/\n{2,}|\n[-]/g,'\n').replace(/\t/g,' ');
		var row=elem.rows;
		var col=elem.cols-1;
		var line1 = '';
		if (v.indexOf("\n")==0) {
			line1 = "\n";
			row--;
			v = v.substring(1);
		}
		v=v.trim();//[ ]{2,}|
		//var end;
		//if ((end=v.length)==0) return elem=null;
		if (v.length==0) return elem=null;
		//end--;
		elem.value = line1+this._textFilter(row, col, v);
		//var resultLength=elem.value.length;
		//if (resultLength<originalLength) {
		var _r=true,_info="";
		if (elem.value!=original) _info = "Text refined";
		if (!(_r=this.validate())) _info+=(_info.length>0?"And ":"")+"Invalid";
		this.setExtraInfo(_info,!_r);
		if (_r) this._hideTipsLater();
		original=elem=v=null;
	}
});﻿/**
 * @import csp.ui.TextAreaPatternContainer;
 *
 * @class csp.ui.F79PatternContainer
 * @extends csp.ui.TextAreaPatternContainer
 * Actually, this kind of container is no need any more. 
 * Cause the filter in TextAreaPatternContainer will take care of the "-".
 *
 * @author Leegorous
 */
Class.create("csp.ui.F79PatternContainer","csp.ui.TextAreaPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextAreaPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//validate : new MethodAlias("_validateF79PatternImpl"),
	//_validateF79PatternImpl : function() {
	validate : function() {
		var _a;
		if (_a = this[arguments.callee.parent]()) {//this._validateTextAreaPatternImpl()
			if (!(_a=(this.getValue().search(/(^|\n)-/)==-1))) 
				this.setExtraInfo("Each line cannot lead with \"-\".",!_a);
		}
	}
});﻿/**
 * @import csp.ui.CommonContainerRequestCallback;
 *
 * @class csp.ui.GetBICExpansionCallback
 * @extends csp.ui.CommonContainerRequestCallback
 *
 * @author Leegorous
 */
Class.create("csp.ui.GetBICExpansionCallback","csp.ui.CommonContainerRequestCallback",null,{
	continuation:function() {
		//this._container=this._container._callback=null;
	},
	init:function(container) {
		//this._container=container;
	},
	onResponseReceived:function(request,response) {
		try {
			var _a=JSONParser.parse(response.text);
			//this._container.setExpansion(_a);
			BICMgr.setExpansions(_a);
			// push the expansion to the stack.
			/*
			_a.bic
			_a.branch
			_a.country
			_a.city
			_a.institution
			*/
		} catch(e) {
			this.onError(request,response,e);
			throw e;
		}
		this.continuation();
	}
});﻿/**
 * @import csp.ui.CommonContainerRequestCallback;
 *
 * @class csp.ui.GetCurrencyExpansionCallback
 * @extends csp.ui.CommonContainerRequestCallback
 *
 * @author Leegorous
 */
Class.create("csp.ui.GetCurrencyExpansionCallback","csp.ui.CommonContainerRequestCallback",null,{
	continuation:function() {
		//this._container=this._container._callback=null;
	},
	init:function(container) {
		//this._container=container;
	},
	onResponseReceived:function(request,response) {
		try {
			var _a=JSONParser.parse(response.text);
			//this._container.setExpansion(_a);
			CurMgr.setExpansions(_a);
			// push the expansion to the stack.
			/*
			_a.bic
			_a.branch
			_a.country
			_a.city
			_a.institution
			*/
		} catch(e) {
			this.onError(request,response,e);
			throw e;
		}
		this.continuation();
	}
});﻿/**
 * @import ui.ChangeListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.ListPatternChangeListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ChangeListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.ListPatternChangeListener","csp.ui.CommonContainerListener", 
	["ui.ChangeListener"],{
	onChange:function(event,sender) {
		var _a=this.findContainer(sender,2);
		if (_a) {
		//if (_a && (_a=_a.parentNode) && (_a=_a.parentNode) && (_a=_a._container)) {
			_a.setAlternativeFieldEnabled(sender.value==-1);
			_a.notify("change");
		}
		_a=null;
	}
});﻿/**
 * @import csp.ui.PatternContainer;
 *
 * @class csp.ui.ListPatternContainer
 * @extends csp.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.ListPatternContainer","csp.ui.PatternContainer",null,{
	createAlternativeField:function() {
		var _a=$doc.createElement("input");
		_a.type="text";
		_a.className="field";
		DOM.sinkEvent(_a,ContainerEvents.getEventId("CommonTextPattern"));
		if (this.node.restriction.maxLength) _a.maxLength=this.node.restriction.maxLength;
		this._field.parentNode.insertBefore(_a,this._field.nextSibling);
	},
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("ListPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("ListPatternContainer"+this.bit);
	},
	/**
	 * Destroy this container
	 */
	//destroy : new MethodAlias("_destroyListPatternImpl"),
	//_destroyListPatternImpl:function() {
	destroy : function() {
		delete this._choiceField;
		//this._destroyPatternImpl();
		this[arguments.callee.parent]();
	},
	/**
	 * Filtrate the input
	 * @param {Object} event the event object
	 * @param {int} charCode the charCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateInput:function(event,charCode,modifiers) {},
	/**
	 * Filtrate the text
	 * @param {Object} event the event object
	 * @param {int} keyCode the keyCode of the input
	 * @param {int} modifiers the modifiers of the input
	 */
	filtrateText:function(event,keycode,modifiers) {},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initListPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//_initListPatternImpl : new MethodAlias("_initPatternImpl"),
	initBody:function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this._choiceField=this._field;
		var enums=this.node.restriction.enumeration;
		for (var i=0,j=enums.length; i<j; i++) {
			this.addOption(enums[i],enums[i]);
		}
		//this.addOption("Other...",-1);
		enums=null;
	},
	setAlternativeFieldEnabled:function(enabled) {
		var skipValidate = false;
		if (enabled) {
			if (this._choiceField.nextSibling.nodeName!="INPUT") {
				this.createAlternativeField();
				skipValidate=true;
			}
			this._field=this._choiceField.nextSibling;
			this._field.style["display"]="";
		} else if (this._field!=this._choiceField) {
			this._field.style["display"]="none";
			this._field=this._choiceField;
		}
		if (!skipValidate) this.validate();
	},
	setValue : function(v) {
		if (this._field) {
			this._field.value=v;
			if (this._field.value!=v) {
				this._field.value=-1;
				this.setAlternativeFieldEnabled(true);
				this._field.value=v;
			}
		}
	},
	
	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		this._choiceField.disabled = readonly;
		if (this._field!=this._choiceField) this._field.readOnly = readonly;
	},
	impl:{
		moz:{
			addOption:function(name,value) {
				this._field.add(new Option(name,value),null);
			}
		},
		ie:{
			addOption:function(name,value) {
				this._field.add(new Option(name,value));
			}
		}
	}
});/**
 * @import msg.ui.MainContainer;
 *
 * @class csp.ui.MainContainer
 * @extends msg.ui.MainContainer
 *
 * It is the entry of the message container.
 * @author Leegorous
 */
Class.create("csp.ui.MainContainer","msg.ui.MainContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initMainImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	/**
	 * Get the XML string
	toXML : function() {
		var xml=[], childsXml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0) {
				xml.push('<'+this.name+' type="SwiftMessageElement" typeName="'+this.node.type+'"');
				xml.push('>'+childsXml+'</'+this.name+'>');
			}
		}
		return xml.join("");
	}
	 */
});﻿/**
 * @import msg.ui.MessageManager;
 *
 * @class csp.ui.MessageManager
 * @extends msg.ui.MessageManager
 *
 * @author Leegorous
 */
Class.create("csp.ui.MessageManager" ,"msg.ui.MessageManager" ,null,{$tatic:{
	_schemas : {},
	_dicts : {},
	/**
	 * Add schema to the manager.
	 * @param {Object} schema the schema object.
	 */
	addSchema : function(schema) {
		if (!schema || typeof schema!="object" || schema.name!="Document") return;
		if (!schema.childs || schema.childs.length!=1) return;
		var name = schema.childs[0].name;
		this._schemas[name] = schema;
		return name;
	},
	/**
	 * Add dictionary to the manager.
	 * @param {Object} dict the dictionary object.
	 */
	addDictionary : function(dict) {
		if (!dict || typeof dict!="object") return;
		var name = dict.name;
		this._dicts[name] = dict;
		return name;
	},
	getDictionary : function(name, lang) {
		return this._dicts[name+"_"+lang] || null;
	},
	createUI : function(parentContainer,msgId) {
		CurrentDictionary = this._dicts[msgId];
		CtnBuilder.buildChild(this._schemas[msgId],parentContainer);
	}
}});﻿/**
 * @import ui.ClickListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.NewOccurrenceListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.NewOccurrenceListener","csp.ui.CommonContainerListener",
	["ui.ClickListener"],{
	onClick:function(event,sender) {
		var _a=this.findContainer(sender,3);
		if (_a) _a.newOccur();
		_a = null;
	}
});﻿/**
 * @import csp.ui.TextAreaPatternContainer;
 *
 * @class csp.ui.NOTAGPatternContainer
 * @extends csp.ui.TextAreaPatternContainer
 *
 * NOTAGPatternContainer has the default dimension: 50*15 (cols*rows)
 * see setDimension();
 *
 * @author Leegorous
 */
Class.create("csp.ui.NOTAGPatternContainer","csp.ui.TextAreaPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextAreaPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	setDimension:function() {
		this._field.setAttribute("rows",15);
		this._field.setAttribute("cols",50);
	},
	/**
	 * final
	 * NOTAGPatternContainer has no validate implement of itself, so the _validateNOTAGPatternImpl 
	 * is not available, but we can get the implement of its ancestors.
	 * Note: NOTAGPatternContainer use _validatePatternImpl as its validate implement
	 */
	//validate : new MethodAlias("_validatePatternImpl")
	validate : function() {
		//alert(arguments.callee.parent);
		return this[arguments.callee.parent]();
	}
});/**
 * @import msg.ui.OptionContainer;
 *
 * @class csp.ui.OptionContainer
 * @extends msg.ui.OptionContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.OptionContainer","msg.ui.OptionContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initOptionImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	/**
	 * Get the XML string
	toXML : function() {
		var xml=[], childsXml;
		if (this.enabled && this.curChild) {
			childsXml = this.curChild.toXML();
			if (childsXml.length>0 || 
				this.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				xml.push('<'+this.name+' type="SwiftChoiceElement" typeName="'+this.node.type+'"');
				xml.push('>'+childsXml+'</'+this.name+'>');
			}
		}
		return xml.join("");
	}
	 */
});﻿/**
 * @import ui.ChangeListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.OptionContainerChangeListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ChangeListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.OptionContainerChangeListener","csp.ui.CommonContainerListener", 
	["ui.ChangeListener"],{
	onChange:function(event,sender) {
		var _a=this.findContainer(sender,4);
		if (_a) {
		//if (_a && (_a=_a.parentNode) && (_a=_a.parentNode) && (_a=_a._container)) {
			_a.setChild(sender.value);
		}
		_a=null;
	}
});﻿/**
 * @import ui.ClickListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.PatternContainerSwitchListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.PatternContainerSwitchListener","csp.ui.CommonContainerListener", 
	["ui.ClickListener"],{
	onClick:function(event,sender) {
		var _a;
		if (_a=this.findContainer(sender,2)) {
		//if (_a && (_a=_a.parentNode) && (_a=_a.parentNode) && (_a=_a._container)) {
			//Console.put("Checkbox of '"+_a.name+"' has been clicked");
			_a.setEnabled(sender.checked);
		}
		_a=null;
	}
});﻿/**
 * @import ui.ClickListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.RemoveSequenceItemListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.RemoveSequenceItemListener","csp.ui.CommonContainerListener",
	["ui.ClickListener"],{
	onClick:function(event,sender) {
		var _a=this.findContainer(sender,3);
		if (_a) _a.remove();
		_a = null;
	}
});﻿/**
 * @import csp.ui.TextPatternContainer;
 *
 * @class csp.ui.SenderRefPatternContainer
 * @extends csp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.SenderRefPatternContainer","csp.ui.TextPatternContainer",null,{
	/**
	 * final
	 */
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//validate : new MethodAlias("_validateSenderRefPatternImpl"),
	//_validateSenderRefPatternImpl : function() {
	validate : function() {
		//this._=Class.get("csp.ui.TextPatternContainer").prototype.validate;
		var _a;
		//if (_a=this._()) {
		/**
		 * To reuse the method of the super class, I only figure out this way yet.
		 * Cause "this" pointer is always point to the current object.
		 * Here, I have got some idea.
		 * See the following lines. for example: 
		 *   _validateTextPatternImpl is refer to the validate implement of TextPatternContainer.
		 *   _validatePatternImpl is refer to the validate implement of PatternContainer.
		 * With these referrences, it is easy to access the implements of the super method.
		 * Override is to the interface method, not to the implement itself.
		 * This may be the good way to realize the reuse in this framework.
		 */
		if (_a = this[arguments.callee.parent]()) {//this._validateTextPatternImpl()
			var v=this.getValue();
			if (v.indexOf("//")!=-1) this.setExtraInfo("Cannot include double slash",_a=false);
			else if (v.charAt(0)=="/" || v.charAt(v.length-1)=="/")
				this.setExtraInfo("Cannot start or end with slash",_a=false);
		}
		//delete this._;
		return _a;
	}
});﻿/**
 * @import msg.ui.SequenceContainer;
 *
 * @class csp.ui.SequenceContainer
 * @extends msg.ui.SequenceContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.SequenceContainer","msg.ui.SequenceContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initSequenceImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	/**
	 * Generate a new instance of this sequence container
	newOccur : function() {
		// do nothing when this container is read only
		if (this.readOnly) return false;

		if (!this._body) {
			this.createBody();
			this._body = this._container.lastChild;
		}
		if (this.created<this.maxOccurs) {
			this.created++;
			new (Class.get("csp.ui.SequenceItemContainer"))(this.node,this);
			this.updateCreated();
		}
		this.setEnabled(true);
		//if (!this.expanded) this.expand(true);
	}
	 */
});﻿/**
 * @import ui.ClickListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.SequenceContainerExpandListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.ClickListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.SequenceContainerExpandListener","csp.ui.CommonContainerListener",
	["ui.ClickListener"],{
	onClick:function(event,sender) {
		var _a=this.findContainer(sender,3);
		if (_a) _a.expand(!_a.expanded);
		_a = null;
	}
});/**
 * @import msg.ui.SequenceItemContainer;
 *
 * @class csp.ui.SequenceItemContainer
 * @extends msg.ui.SequenceItemContainer
 * 
 * @author Leegorous
 */
Class.create("csp.ui.SequenceItemContainer","msg.ui.SequenceItemContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initSequenceItemImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	/**
	 * Get the XML string
	toXML : function() {
		var xml=[], childsXml=[];
		if (this.enabled) {
			this.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				this.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				xml.push('<'+this.name+' type="Swift'+this.node.base+'Element" typeName="'+this.node.type+'"');
				xml.push('>'+childsXml+'</'+this.name+'>');
			}
		}
		return xml.join("");
	}
	 */
});﻿/**
 * @import csp.ui.ListPatternContainer;
 *
 * @class csp.ui.SignPatternContainer
 * @extends csp.ui.ListPatternContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.SignPatternContainer","csp.ui.ListPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initListPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	/**
	 * Here override the validate method to reuse the validate implement of 
	 * PatternContainer to avoid the validation in TextPatternContainer.
	 */
	//validate : new MethodAlias("_validatePatternImpl")
	validate : function() {
		this[this[arguments.callee.parent].parent]();
	}
});﻿/**
 * @class csp.ui.SimpleContainer
 * @extends Object
 * This is an abstract class, and the base class for all container.
 *
 * @author Leegorous
 */
Class.create("csp.ui.SimpleContainer",null,null,{
	/**
	 * @private
	 * The document fragment of this contaienr body.
	 */
	_body : null,
	/**
	 * @private
	 * The document fragment of this contaienr.
	 */
	_container : null,
	/**
	 * The 'bit' property indicate what should this container look like.
	 * It helps to locate the exactly template for this container.
	 */
	bit : 0,
	/**
	 * The 'readOnly' property indicate the edit status.
	 * True if this container is read only.
	 */
	readOnly : false,
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:Abstract.empty,
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:Abstract.empty,
	/**
	 * The container description.
	 */
	description : "",
	/**
	 * Destroy this container.
	 */
	//destroy : new MethodAlias("_destroySimpleImpl"),
	//_destroySimpleImpl:function() {
	destroy : function() {
		delete this.node;
		//delete this._ownerMessage; @2008-4-21
		delete this.ownerDocument
		delete this._container;
		delete this._body;
		delete this._parent;
		CtnMgr.remove(this.id);
	},
	
	init : function(_node,_parentContainer) {
		//this._initSimpleImpl(_node,_parentContainer);
	//},
	//_initSimpleImpl : function(_node,_parentContainer) {
		//Console.put("Creating "+_node.name+" of "+_parentContainer.name);
		if (!_parentContainer) throw new Error('Parent container requested.');
		//-- get the name from current node, and set it as the name of this container
		this.name=_node.name || '';
		//this.bit=0;
		//this._body=null;
		//-- set the data node
		this.node=_node;
		//-- set parent container
		this._parent=_parentContainer;
		//-- set the owner message
		//this._ownerMessage=_parentContainer._ownerMessage;

		// set the owner document
		this.ownerDocument = _parentContainer.ownerDocument;

		//-- checkup whether this pattern container is optional
		this.bit|=((this.enabled=(_node.minOccurs!=0))?0:ContainerTemplates.isOptional);
		var _dict;
		//-- get the longName, shortName and description(expansion) from corresponding dictionary
		if (_dict=CurrentDictionary[_node.type]) {
		//-- checkup whether the container has description
			if (_dict.expansion) {
				this.description=_dict.expansion;
				this.bit|=ContainerTemplates.hasDescription;
			}
			//-- get the long name from dictionary
			this.longName=_dict.longName;
			//-- get the short name from dictionary, 
			//-- if it is not exist, set the container name as its short name
			this.shortName = _dict.shortName || this.name;
		}
		_dict=null;
		//-- set the container id
		this.id = CtnMgr.add(this);
		//delete this._super;
	},
	
	/**
	 * Compare search string with the container infomation, return true if this container
	 * is matched.
	 * @param {String} v
	 * @returns {Boolean} true if this container matched.
	 */
	//match : new MethodAlias("_matchSimpleImpl"),
	//_matchSimpleImpl : function(v) {
	match : function(v) {
		if (this.name.indexOf(v)>=0) return true;
		if (this.description.indexOf(v)>=0) return true;
		if (this.longName && this.longName.indexOf(v)>=0) return true;
		if (this.shortName && this.shortName.indexOf(v)>=0) return true;
	}
	/*
	$tatic:{
		// deprecated.
		_valueSequence:[],
		initContainer:function() {
			var _a=this._valueSequence;
			for (var i=0,j=_a.length;i<j;i++) {
				_a[i].innerHTML=_a[++i];
			}
		}
	}*/
});﻿/**
 * @import ui.FocusListener;
 * @import csp.ui.CommonContainerListener;
 *
 * @class csp.ui.TextAreaPatternValidateListener
 * @extends csp.ui.CommonContainerListener
 * @implements ui.FocusListener
 *
 * @author Leegorous
 */
Class.create("csp.ui.TextAreaPatternValidateListener","csp.ui.CommonContainerListener", 
	["ui.FocusListener"],{
	onFocus:function(event,sender) {
		var _a;
		if (_a=this.findContainer(sender,2)) _a.cancelTimer();
		_a=null;
	},
	onLostFocus:function(event,sender) {
		var _a;
		if (_a=this.findContainer(sender,2)) _a.refineText();
		_a=null;
	}
});﻿/**
 * @import csp.ui.TextPatternContainer;
 *
 * @class csp.ui.TolerancePatternContainer
 * @extends csp.ui.TextPatternContainer
 * 
 * @author Leegorous
 */
Class.create("csp.ui.TolerancePatternContainer","csp.ui.TextPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//validate : new MethodAlias("_validateTolerancePatternImpl"),
	//_validateTolerancePatternImpl : function() {
	validate : function() {
		var _a,_b,_c=null;
		if (_a = this[arguments.callee.parent]()) {//this._validateTextPatternImpl()
			if (isNaN(_b=parseInt(this.getValue())) || _b>10) {
				_a=false;
				this.setExtraInfo("Tolerance out of range.",true);
			}
		}
		return _a;
	}
});﻿/**
 * @import msg.ui.DocumentContainer;
 *
 * @class csp.ui.DocumentContainer
 * @extends msg.ui.DocumentContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.DocumentContainer","msg.ui.DocumentContainer",null,{
	init : function(info) {
		//this._initDocumentImpl(_node, _wrapper,lang);
		//this[arguments.callee.parent](_node, _wrapper, lang);
		this[arguments.callee.parent](info);
	}
	/*
	toXML : function() {
		//var xml = [];
		//xml.push('<Document xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="'+this.node.xmlns+'">');
		//xml.push(this.message.toXML());
		//xml.push('</Document>');
		//return xml.join('');
		return this.message.toXML();
	}*/
});/**
 * @import msg.ui.CompositeContainer;
 *
 * @class csp.ui.FinBlockContainer
 * @extends msg.ui.CompositeContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.FinBlockContainer" ,"msg.ui.CompositeContainer" ,null ,{

	/**
	 * Create the DOM node of this container
	 */
	createContainer : function() {
		return Element.create("div");
	},

	/**
	 * @constructor
	 *
	 * @param {Object} node
	 * @param {Container} parentContainer
	 */
	init : function(node, parentContainer) {
		this[arguments.callee.parent](node, parentContainer);
	},

	/**
	 * Initialize the container body and its childs
	 */
	initBody : function() {
		this._body=this._container;
		this.ownerDocument.containerBuilder.buildChild(this.node, this);
	},

	setLabel : function() {}
});/**
 * @import msg.ui.DocumentContainer;
 *
 * @class csp.ui.FinMessageContainer
 * @extends msg.ui.DocumentContainer
 *
 * @author Leegorous
 */
Class.create("csp.ui.FinMessageContainer" ,"msg.ui.DocumentContainer" ,null ,{

	/**
	 * Add child to the container
	 * 
	 * @param {Container} container
	 */
	add : function(container) {
		this.childs[container.name] = container;
		this._container.add(container._container, container.name);
	},

	/**
	 * Build the message
	 */
	buildMessage : function() {
		// build container for FIN message
		this._container = this.createContainer();
		this._wrapper.appendChild(this._container._e);

		this[arguments.callee.parent]();

		// get dictionary
		//this.dict = this.messageManager.getDictionary(this.node.name, this.language);

		//this.containerBuilder.buildChild(this.node, this);
	},

	/**
	 * Create the current container
	 */
	createContainer : function() {
		return new (Class.get("ui.TabPanel"))();
	},

	/**
	 * @constructor
	 * Build the FIN message
	 *
	 * @param {Object} node
	 */
	init : function(node, parentContainer) {
		//if (!node) // || !info.node || !info.wrapper || !info.generateXmlVisitor || !info.containerBuilder
			//throw new Error("FinMessageContainer: infomation expected.");

		this[arguments.callee.parent](node, parentContainer);

		//this.ownerDocument = this;

		// document root node
		//this.node = info.node;

		//this.childs = {};

		//-- set the container id
		//this.id = CtnMgr.add(this);

		//this.generateXmlVisitor = new (Class.get(info.generateXmlVisitor))();
		
		//this.containerBuilder = new (Class.get(info.containerBuilder))();

		//this.dict = info.dict;

		//this._container = this.createContainer();
		
		//$(info.wrapper).appendChild(this._container._e);

		//this.initBody();
	},

	getMessage : function() {
		return this;
	},

	_getMessageType : function(node) {
		this.messageType = node.name
	},

	/**
	 * Build the child containers
	initBody : function() {
		this.containerBuilder.buildChild(this.node, this);
	},
	 */

	/**
	 * Get the child container
	 *
	 * @param {String} name
	 * @return {Container}
	 */
	getChild : function(name) {
		return this.childs[name] || null;
	},

	/**
	 * Select containers
	 *
	 * @param {String} path
	 * @returns {Container[]}
	 */
	select : function(path) {
		// check parameter
		if (!path || path.length==0 || !this.message) return null;
		if (path.indexOf("/") == 0) path = path.substring(1);
		var idx;
		if ((idx = path.indexOf("/")) > 0) {
			if (this.message.name == path.substring(0,idx)) {
				return this.message.select(path.substring(idx + 1));
			}
		} else {
			if (this.message.name == path) return [this.message];
		}
		return null;
		//return this.message.select(path);
	},

	/**
	 * To xml string
	 *
	 * @return {String}
	 */
	toXML : function() {
		return this.generateXmlVisitor.visitFinMessage(this);
	}
});/**
 * @import msg.ui.MessageBuilder;
 *
 * @class csp.ui.MessageBuilder
 * @extends msg.ui.MessageBuilder
 *
 * @author Leegorous
 */
Class.create("csp.ui.MessageBuilder" ,"msg.ui.MessageBuilder" ,null ,{
	init : function(message, impl) {
		this.bit = 7;
		this[arguments.callee.parent](message, impl);
		//this.message = message;
		//this.requestSchema();
		//this.requestMessageXml();
	},

	createMessage : function() {
		var msg = this.message;
		currentDocument = new (Class.get("csp.ui.DocumentContainer"))(msg.schema,msg.wrapper,msg.language);
		msg.document = currentDocument;
		this.fillMessage();
	}
});/**
 * @class csp.ui.impl.GenerateXMLVisitor
 *
 * @author Leegorous
 */
Class.create("csp.ui.impl.GenerateXMLVisitor" ,null ,null ,{

	/**
	 * Generate xml for CompositeContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitComposite : function(container) {
		var xml=[], childsXml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				container.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				if (container.isVirtual()) xml.push(childsXml);
				else {
					var prefix = container.ownerDocument.nsPrefix || '';
					if (prefix.length > 0) prefix += ':';
					xml.push('<' + prefix + container.name + ' type="SwiftFieldElement" typeName="'+container.node.type+'"');
					if (container.node.Separator) xml.push(' separator="'+container.node.Separator.replace(/\n/g,"&#10;")+'"');
					xml.push('>' + childsXml);
					xml.push('</' + prefix +container.name+'>');
				}
			}
		}
		return xml.join("");
	},

	/**
	 * Generate xml for DocumentContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitDocument : function(container) {
		return container.getMessage().toXML();
	},
	
	/**
	 * Generate xml for MainContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitMessage : function(container) {
		var xml=[], childsXml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0) {
				var prefix = container.ownerDocument.nsPrefix || '';
				if (prefix.length > 0) prefix += ':';
				xml.push('<' + prefix + container.name + ' type="SwiftMessageElement" typeName="'+container.node.type+'">');
				xml.push(childsXml);
				xml.push('</' + prefix + container.name + '>');
			}
		}
		return xml.join("");
	},
	
	/**
	 * Generate xml for OptionContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitOption : function(container) {
		var xml=[], childsXml;
		if (container.enabled && container.curChild) {
			childsXml = container.curChild.toXML();
			if (childsXml.length>0 || 
				container.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				if (container.isVirtual()) xml.push(childsXml);
				else {
					var prefix = container.ownerDocument.nsPrefix || '';
					if (prefix.length > 0) prefix += ':';
					xml.push('<' + prefix + container.name + ' type="SwiftChoiceElement" typeName="'+container.node.type+'">');
					xml.push(childsXml);
					xml.push('</' + prefix + container.name + '>');
				}
			}
		}
		return xml.join("");
	},

	/**
	 * Generate xml for PatternContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitPattern : function(container) {
		var xml = [];
		if (container.enabled) {
			/**
			 * I don't know why it have to do such replace in original script.
			 * But I keep it.
			 */
			var nodeName = container.name.replace(/_\d+/,'');
			if (container.isVirtual()) xml = container.getValue();
			else {
				var prefix = container.ownerDocument.nsPrefix || '';
				if (prefix.length > 0) prefix += ':';
				xml.push('<' + prefix +nodeName+' type="SwiftSubFieldElement" typeName="'+container.node.type+'"');
				//-- output FinFormat
				if (container.node.FinFormat) xml.push(' FinFormat="'+container.node.FinFormat+'"');
				//-- output SepPrefix
				if (container.node.SepPrefix) xml.push(' prefix="'+container.node.SepPrefix.replace(/\n/g,"&#10;")+'"');
				//-- output SepSuffix
				if (container.node.SepSuffix) xml.push(' suffix="'+container.node.SepSuffix.replace(/\n/g,"&#10;")+'"');
				xml.push(">"+container.getValue()+"</" + prefix+nodeName+">");
			}
		}
		return xml.join("");
	},

	/**
	 * Generate xml for SequenceContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitSequence : function(container) {
		var xml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				xml.push(child.toXML());
			});
		}
		return xml.join("");
	},
	
	/**
	 * Generate xml for SequenceItemContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitSequenceItem : function(container) {
		var xml=[], childsXml=[];
		if (container.enabled) {
			container.eachChild(function(child) {
				if (child.enabled) childsXml.push(child.toXML());
			});
			childsXml = childsXml.join("");
			if (childsXml.length>0 || 
				container.ownerDocument.status == Class.get("msg.ui.DocumentContainer").CREATE_TEMPLATE) {
				var prefix = container.ownerDocument.nsPrefix || '';
				if (prefix.length > 0) prefix += ':';
				xml.push('<' + prefix + container.name + ' type="Swift'+container.node.base+'Element" typeName="'+container.node.type+'">');
				xml.push(childsXml);
				xml.push('</' + prefix + container.name + '>');
			}
		}
		return xml.join("");
	}
});﻿/**
 * @import msg.ui.TextAreaPatternContainer;
 *
 * @class ecp.ui.AnyPatternContainer
 * @extends msg.ui.TextAreaPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.AnyPatternContainer","msg.ui.TextAreaPatternContainer",null,{
	/**
	 * Fill the value.
	 * This is the abstract method.
	 */
	fill : function(node) {
		if (this.bit & ContainerTemplates.isOptional) {
			this._container.firstChild.firstChild.checked=true;
			//defaultChecked=true;
			this.setEnabled(true);
		}//debugger;
		var ns = node.namespaceURI
		var xml = XML.toXml(node).replace(' xmlns="'+ns+'"','');
		this.setValue(xml);
	},

	init:function(_node,_parentContainer) {
		//this._initTextAreaPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
});﻿/**
 * @import msg.ui.TextPatternContainer
 *
 * @class ecp.ui.BICPatternContainer
 * @extends msg.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.BICPatternContainer","msg.ui.TextPatternContainer",null,{
	/**
	 * Destroy this container
	 */
	destroy:function() {
		//if (this._expansion) DOM.detachNode(this._expansion);
		//delete this._callback;
		delete this._switcher;
		delete this._expansion;
		//this._destroyTextPatternImpl();
		this[arguments.callee.parent]();
	},
	displayExpansion:function(v) {
		if (v && this._expansionInfo) this.initExpansion();
		this._expansion.style["display"]=v?"":"none";
	},
	filtrateInput:function(event,charCode,modifiers) {
		if (this.readOnly) return;
		if (modifiers & Event.CONTROL_MASK) return;
		var _v=this.getValue();
		//var _a=this._field.getCursorPos();
		var _b=this._field.getSelectionLength();
		var _m=this.node.restriction.maxLength;
		/**
		 * If the input char is not in "a-zA-Z0-9", then prevent it.
		 * If the input char is in "a-z", then convert it to upper case.
		 */
		if ((_v.length-_b<_m) && (charCode<48 || (charCode>57 && charCode<65) || charCode>90)) {
			this.preventInput(event);
			if (charCode>96 && charCode<123) {
				var _a=this._field.getCursorPos();
				this.setValue(_v.slice(0,_a)+String.fromCharCode(charCode-32)+_v.slice(_a+_b));
				//_a++;_b=0;
				this._field.setSelectionRange(_a+1,0);
			}
		}
		//this._field.setSelectionRange(_a,_b);
	},
	init:function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//this._super(_node,_parentContainer);
		//this._callback=null;
		this._expansion=null;
	},
	initExpansion:function() {
		var _a;
		if (this._expansion) _a=this._expansion;
		else {
			_a=ContainerTemplates.get("BicExpansionContainer");
			DOM.insertAfter(_a,this._container);
		}
		var _b=_a.lastChild.firstChild.childNodes;
		var _c=this._expansionInfo;
		_b[0].lastChild.innerHTML=_c.institution;
		_b[1].lastChild.innerHTML=_c.branch;
		_b[2].lastChild.innerHTML=_c.city;
		_b[3].lastChild.innerHTML=_c.country;
		this._expansion=_a;
		this._expansionInfo=null;
		_a=_b=null;
	},
	setExpansion:function(expansion) {
		if (!expansion) {
			this.setExtraInfo("Unknown BIC");
			return;
		}
		if (!this._container) return;
		this._expansionInfo=expansion;
		var _c;
		if (this._switcher) _c=this._switcher;
		else {
			_c=$doc.createElement("a");
			_c.className="bicExpandInfo";
			_c.href="javascript:void(0);";
			_c.innerHTML="BIC Expanded";
			DOM.sinkEvent(_c,DOM.addEventListener(_c,"click", 
				new (Class.get("csp.ui.BicExpansionSwitchListener"))(this)));
			this._switcher=_c;
		}
		this.setExtraInfo(_c);
		_c=null;
	},
	//validate : new MethodAlias("_validateBICPatternImpl"),
	//_validateBICPatternImpl:function() {
	validate : function() {
		var _a;
		if (_a = this[arguments.callee.parent]()) {//this._validateTextPatternImpl()
			var _b=this.getValue();
			if (_b.length==8) this.setValue(_b+="XXX");
			if (_b!=this._previousValue) {
				this._previousValue = _b;
				this.setExtraInfo("Loading...");
				if (this._expansion) this._expansion.style["display"]="none";
				// add the container to BICManager.
				BICMgr.addListener(this);
			} else {
				this.setExpansion(BICMgr.getExpansion(_b));
			}
			/*
			// Get the expansion from system, cancel the previous request if necessary.
			if (this._callback) this._callback.cancel();
			var _c=new Request(PATH.GET_BIC_EXPANSION, 
				new (Class.get("csp.ui.GetBICExpansionCallback"))(this), 
				"POST");
			this._callback=_c;
			RequestPool.add(_c);*/
		}
		return _a;
	}
});﻿/**
 * @import msg.ui.PatternContainer;
 *
 * @class ecp.ui.BooleanPatternContainer
 * @extends msg.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.BooleanPatternContainer","msg.ui.PatternContainer",null,{
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("BooleanPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("BooleanPatternContainer"+this.bit);
	},
	getValue:function() {
		return this._field.checked?true:false;
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initBooleanPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//_initBooleanPatternImpl : new MethodAlias("_initPatternImpl"),
	//initBody : new MethodAlias("_initBodyBooleanPatternImpl"),
	//_initBodyBooleanPatternImpl:function() {
	initBody : function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this._field = this._field.firstChild;
		//this._field=new (Class.get("ui.TextBox"))(this._field);
	},
	setValue:function(v) {
		//-- if the body of this container has not been initialized, initialize it first.
		//if ((this.bit & ContainerTemplates.isOptional) && !this._field) this.initBody();
		if (this._field) {
			if (v=='true') this._field.checked = true;
			else this._field.checked = false;
		}
	},
	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		this._field.disabled = readonly;
	}
});﻿/**
 * @import msg.ui.PatternContainer;
 *
 * @class ecp.ui.CheckListPatternContainer
 * @extends msg.ui.PatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.CheckListPatternContainer","msg.ui.PatternContainer",null,{
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("CheckListPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("CheckListPatternContainer"+this.bit);
	},
	getValue : function() {
		var v = [];
		if (this._fields) {
			for (var i=0,j=this._fields.length; i<j; i++) {
				if (this._fields[i].checked) v.push(this._fields[i].name);
			}
		}
		return v.join(',');
	},
	init:function(_node,_parentContainer) {
		//this._super(_node,_parentContainer);
		//this._initCheckListPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//initBody : new MethodAlias("_initBodyCheckListImpl"),
	//_initBodyCheckListImpl : function() {
	initBody : function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		this._fields = [];
		var _enum = this.node.restriction.enumeration;
		var dictEnums=null,name;
		if (this.dict) dictEnums = this.dict.enumeration;
		for (var i=0,j=_enum.length; i<j; i++) {
			if (dictEnums) name = dictEnums[_enum[i]];
			else name = _enum[i];
			var label = $doc.createElement('label');
			label.className = "labeledField";
			var cb = $doc.createElement('input');
			cb.type = "checkbox";
			cb.name = _enum[i];
			this._fields.push(cb);
			label.appendChild(cb);
			label.appendChild($doc.createTextNode("  "+name));
			this._field.appendChild(label);
		}
	},
	setValue : function(v) {
		for (var i=0,j=this._fields.length; i<j; i++) {
			if (this._fields[i].name==v) {
				this._fields[i].checked=true;
				return;
			}
		}
		// report error
	},
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitCheckListPattern(this);
		/*
		var xml = [];
		var values = this.getValue().split(',');
		for (var i=0,j=this.node.maxOccurs; i<j; i++) {
			if (values[i]) {
				xml.push('<'+this.name+'>'+values[i]+'</'+this.name+'>');
			}
		}
		return xml.join('');*/
	},
	//_initCheckListPatternImpl : new MethodAlias("_initPatternImpl"),
		
	_setFieldReadOnly : function(/* Boolean: */readonly) {
		var disabled = readonly?true:false;
		for (var i=0, j=this._fields.length; i<j; i++) {
			this._fields[i].disabled = disabled;
		}
	}
});﻿/**
 * @import msg.ui.TextPatternContainer;
 *
 * @class ecp.ui.ComplexTextPatternContainer
 * @extends msg.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ComplexTextPatternContainer","msg.ui.TextPatternContainer",null,{
	/**
	 * Create a document fragment node as the body of this container.
	 */
	createBody:function() {
		this._container.appendChild(ContainerTemplates.get("ComplexTextPatternField"+this.bit));
	},
	/**
	 * Create a document fragment node of this container.
	 */
	createContainer:function() {
		return ContainerTemplates.get("ComplexTextPatternContainer"+this.bit);
	},
	fill : function(node) {
		if (this.bit & ContainerTemplates.isOptional) {
			this._container.firstChild.firstChild.checked=true;
			//defaultChecked=true;
			this.setEnabled(true);
		}
		if (node.attributes) {
			for (var i=0,j=node.attributes.length; i<j; i++) {
				var attr = node.attributes.item(i);
				this.setAttribute(attr.nodeName,attr.nodeValue);
			}
		}
		if (node = node.firstChild) {
			this.setValue(node.nodeValue);
		}
	},
	getAttribute : function(name) {
		if (this._attrs[name]) return this._attrs[name].getValue();
		return null;
	},
	getValue : function() {
		return this._field.getValue();
	},
	init : function(_node,_parentContainer) {
		//this._initComplexTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	//initBody : new MethodAlias("_initBodyComplexTextPatternImpl"),
	//_initBodyComplexTextPatternImpl : function() {
	initBody : function() {
		//this._initBodyPatternImpl();
		this[arguments.callee.parent]();
		var _field = this._field._e.firstChild;
		this._field = new (Class.get("ui.TextBox"))(_field);
		this._attrs = {};
		for (var i=0,j=this.node.attrs.length; i<j; i++) {
			attr = this.node.attrs[i];
			var tb = new (Class.get("ui.TextBox"))();
			tb._e.className = "field";
			tb.name = attr.name;
			tb.type = attr.type;
			this._attrs[attr.name]=tb;
			_field.parentNode.insertBefore(tb._e,_field);
		}
	},
	setAttribute : function(name, v) {
		if (this._attrs[name]) this._attrs[name].setValue(v);
	},

	_setFieldReadOnly :  function(/* Boolean: */readonly) {
		// set all attributes first
		for (var i in this._attrs) {
			this._attrs[i].setReadOnly(readonly);
		}
		this._field.setReadOnly(readonly);
	},

	setValue : function(v) {
		if (this._field) this._field.setText(v);
	},
	//_initComplexTextPatternImpl : new MethodAlias("_initTextPatternImpl"),
	//toXML : new MethodAlias("_toXMLComplexTextPatternImpl"),
	//_toXMLComplexTextPatternImpl : function() {
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitComplexTextPattern(this);
		/*
		var xml = [];
		if (this.enabled) {
			xml.push('<'+this.name);
			for (var i in this._attrs) {
				var attr = this._attrs[i];
				if (attr && i==attr.name) xml.push(' '+attr.name+'="'+attr.getValue()+'"');
			}
			xml.push('>'+this.getValue()+'</'+this.name+'>');
		}
		return xml.join('');*/
	}
});/**
 * @import msg.ui.ContainerBuilder;
 *
 * @class ecp.ui.ContainerBuilder
 * @extends msg.ui.ContainerBuilder
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ContainerBuilder", "msg.ui.ContainerBuilder", null, {

	getContainerClassName : function(node) {
		if (!node.base) return "ecp.ui."+node._ui+"PatternContainer";
		else return this._containerMap[node.base];
	},

	getObserverClassName : function(node) {
		if (!node.base) return "msg.ui.impl.PatternObserver";
		else return this._observerMap[node.base];
	},
	/*
	build : function(_node, _parentContainer) {
		var name;
		if (!_node.base) name = "ecp.ui."+_node._ui+"PatternContainer";
		else name = this.conf[_node.base];
		return new (Class.get(name))(_node,_parentContainer);
	},
	buildChild:function(_node,_parentContainer) {
		var _a,_b;
		if (!(_a=_node.childs)) return;
		for (var i=0,j=_a.length;i<j;i++) {
			this.build(_a[i], _parentContainer);
		}
	},*/
	_containerMap : (function() {
		var map = {
			Block : "csp.ui.FinBlockContainer",
			CompositeField : "msg.ui.CompositeContainer",
			FieldOption : "msg.ui.OptionContainer",
			Loop : "msg.ui.SequenceContainer",
			LoopItem : "csp.ui.SequenceItemContainer",
			Message : "msg.ui.MainContainer"
		};

		map.OptionField = map.FieldOption;
		map.Sequence = map.Loop;
		map.SequenceItem = map.LoopItem;
		map.SubSequence = map.Loop;
		map.SubSequenceItem = map.LoopItem;
		map.SequenceOption = map.FieldOption;

		return map;
	})(),

	_observerMap : (function() {
		var map = {
			Block : "msg.ui.impl.CommonObserver",
			CompositeField : "msg.ui.impl.CommonObserver",
			FieldOption : "msg.ui.impl.OptionFieldObserver",
			Loop : "msg.ui.impl.CommonObserver",
			LoopItem : "msg.ui.impl.CommonObserver",
			Message : "msg.ui.impl.CommonObserver"
		};

		map.OptionField = map.FieldOption;
		map.Sequence = map.Loop;
		map.SequenceItem = map.Loop;
		map.SubSequence = map.Loop;
		map.SubSequenceItem = map.Loop;
		map.SequenceOption = map.FieldOption;

		return map;
	})()
});

/*
var CtnBuilder = Class.get("ecp.ui.ContainerBuilder");
(function() {
	var _conf = CtnBuilder.conf;
	_conf.OptionField=_conf.FieldOption;
	_conf.Sequence=_conf.Loop;
	_conf.SubSequence=_conf.Loop;
	_conf.SequenceOption=_conf.FieldOption;
})();*/﻿/**
 * @import ecp.ui.ComplexTextPatternContainer;
 *
 * @class ecp.ui.CurrencyAndAmountTextPatternContainer
 * @extends ecp.ui.ComplexTextPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.CurrencyAndAmountTextPatternContainer" ,"ecp.ui.ComplexTextPatternContainer" ,null ,{
	/**
	 * @constructor
	 */
	init : function(_node,_parentContainer) {
		//this._initComplexTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},

	initBody : function() {
		//this._initBodyComplexTextPatternImpl();
		this[arguments.callee.parent]();
		var ccy;
		if (ccy = this._attrs["Ccy"]) {
			ccy.setAttribute("__evt",ContainerEvents.getEventId("CommonComplexTextPattern"));
			ccy._e.onfocus = _$f;
			ccy._e.onblur = _$f;
			ccy._e.onkeypress = _$f;
			ccy._e.onkeyup = _$f;
			var o = {
				_container : this,
				validate : function() {
					return true;
				},
				setExpansion : function(expansion) {
					this._container.setExpansion(expansion);
				}
			};
			o.validate.parent = "validate";
			Object.extend(ccy, o);
		}
	},
	
	setExpansion : function(expansion) {
		if (!expansion) {
			this.setExtraInfo("");
			return;
		}
		if (this[this.validate.parent]()) {//this._validateTextPatternImpl()
			this.setExtraInfo(this.getValue() + " (" + expansion + ")");
		}
	},

	/**
	 * validate
	 */
	//validate : new MethodAlias("_validateCurrencyAndAmountTextImpl"),
	//_validateCurrencyAndAmountTextImpl : function() {
	validate : function() {
		var valid;
		var value = this._attrs["Ccy"].getValue().toUpperCase();
		this._attrs["Ccy"].setValue(value);
		if (value != this._previousValue) {
			this._previousValue = value;
			this.setExtraInfo("Loading...");
			CurMgr.addListener(this._attrs["Ccy"]);
		} else {
			this.setExpansion(CurMgr.getExpansion(value));
		}
		return valid;
	}
});﻿/**
 * @import csp.ui.DatePatternContainer;
 *
 * @class ecp.ui.DatePatternContainer
 * @extends csp.ui.DatePatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.DatePatternContainer","csp.ui.DatePatternContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
	//toXML : new MethodAlias("_toXMLDatePatternImpl"),
	//_toXMLDatePatternImpl : function() {
	/*
	toXML : function() {
		var xml = [];
		if (this.enabled) {
			/**
			 * I don't know why it have to do such replace in original script.
			 * But I keep it.
			 * /
			var nodeName = this.name.replace(/_\d+/,'');
			xml.push('<'+nodeName);
			xml.push(">"+this.getValue()+"</"+nodeName+">");
		}
		return xml.join("");
	}*/
});﻿/**
 * @import ecp.ui.DatePatternContainer;
 *
 * @class ecp.ui.DateTimePatternContainer
 * @extends ecp.ui.DatePatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.DateTimePatternContainer","ecp.ui.DatePatternContainer",null,{
	getValue : function() {
		return this._field.getValue()+"T"+this._field2.getValue();
	},
	init : function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	initBody:function() {
		//this._initBodyTextPatternImpl();
		this[arguments.callee.parent]();
		this._field.setAttribute("_format","yyyy-MM-DD");
		this._field.setAttribute("__evt",ContainerEvents.getEventId("CommonDatePattern"));
		var time = new (Class.get("ui.TextBox"))();
		time._e.className = "field";
		time.setValue('00:00:00');
		this._field2 = time;
		this._body.insertBefore(time._e,this._field._e.nextSibling);
	},
	setReadOnly : function(readonly) {
		//this._setReadOnlyPatternImpl(readonly);
		this[arguments.callee.parent](readonly);
		if (this.readOnly) readonly = true;
		if (this._field2) this._field2.setReadOnly(readonly);
	},
	setValue : function(v) {
		if (this._field && this._field2) {
			v = v.split('T');
			if (v.length==2) {
				this._field.setValue(v[0]);
				this._field2.setValue(v[1]);
			}
		}
	}
});﻿/**
 * @import msg.ui.ListPatternContainer;
 *
 * @class ecp.ui.ListPatternContainer
 * @extends msg.ui.ListPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ListPatternContainer","msg.ui.ListPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initListPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
});﻿/**
 * @import msg.ui.TextAreaPatternContainer;
 *
 * @class ecp.ui.MultilinePatternContainer
 * @extends msg.ui.TextAreaPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.MultilinePatternContainer","msg.ui.TextAreaPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initTextAreaPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	},
	setValue : function(v) {
		if (this._field) this._field.value+=(this._field.value.length>0?"\n":"")+v;
	},
	//toXML : new MethodAlias("_toXMLMultilinePatternImpl"),
	//_toXMLMultilinePatternImpl : function() {
	toXML : function() {
		return this.ownerDocument.generateXmlVisitor.visitMultilinePattern(this);
		/*
		var v = this.getValue().replace(/[\r]/g,'').split('\n');
		var xml = [];
		for (var i=0,j=v.length; i<j; i++) {
			xml.push('<'+this.name+'>'+v[i]+'</'+this.name+'>');
		}
		return xml.join('');*/
	}
});﻿/**
 * @import ecp.ui.BICPatternContainer
 *
 * @class ecp.ui.ReadOnlyBICPatternContainer
 * @extends ecp.ui.BICPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ReadOnlyBICPatternContainer","ecp.ui.BICPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		//this._super(_node,_parentContainer);
		//this._callback=null;
		this._expansion=null;
		this.setReadOnly(true);
	}
});﻿/**
 * @import ecp.ui.DatePatternContainer;
 *
 * @class ecp.ui.ReadOnlyDatePatternContainer
 * @extends ecp.ui.DatePatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ReadOnlyDatePatternContainer","ecp.ui.DatePatternContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this.setReadOnly(true);
	}
});﻿/**
 * @import ecp.ui.DateTimePatternContainer;
 *
 * @class ecp.ui.ReadOnlyDateTimePatternContainer
 * @extends ecp.ui.DateTimePatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ReadOnlyDateTimePatternContainer","ecp.ui.DateTimePatternContainer",null,{
	init : function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this.setReadOnly(true);
	}
});﻿/**
 * @import msg.ui.TextPatternContainer;
 *
 * @class ecp.ui.TextPatternContainer
 * @extends msg.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.TextPatternContainer","msg.ui.TextPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
});﻿/**
 * @import ecp.ui.TextPatternContainer;
 *
 * @class ecp.ui.ReadOnlyTextPatternContainer
 * @extends ecp.ui.TextPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.ReadOnlyTextPatternContainer","ecp.ui.TextPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initTextPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
		this.setReadOnly(true);
	}
});﻿/**
 * @import msg.ui.TextAreaPatternContainer;
 *
 * @class ecp.ui.TextAreaPatternContainer
 * @extends msg.ui.TextAreaPatternContainer
 *
 * @author Leegorous
 */
Class.create("ecp.ui.TextAreaPatternContainer","msg.ui.TextAreaPatternContainer",null,{
	init:function(_node,_parentContainer) {
		//this._initTextAreaPatternImpl(_node,_parentContainer);
		this[arguments.callee.parent](_node,_parentContainer);
	}
});/**
 * @import msg.ui.impl.GenerateXMLVisitor;
 *
 * @class ecp.ui.impl.GenerateXMLVisitor
 * @extends msg.ui.impl.GenerateXMLVisitor
 *
 * @author Leegorous
 */
Class.create("ecp.ui.impl.GenerateXMLVisitor" ,"msg.ui.impl.GenerateXMLVisitor" ,null ,{
	
	/**
	 * Generate xml for CheckListPatternContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitCheckListPattern : function(container) {
		var xml = [];
		var values = container.getValue().split(',');
		var prefix = container.ownerDocument.nsPrefix || '';
		if (prefix.length > 0) prefix += ':';
		for (var i = 0, j = container.node.maxOccurs; i<j; i++) {
			if (values[i]) {
				xml.push('<'+prefix +container.name+'>'+values[i]+'</'+prefix +container.name+'>');
			}
		}
		return xml.join('');
	},
	
	/**
	 * Generate xml for ComplexTextPatternContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitComplexTextPattern : function(container) {
		var xml = [], _attrs;
		if (container.enabled) {
			var prefix = container.ownerDocument.nsPrefix || '';
			if (prefix.length > 0) prefix += ':';
			xml.push('<' + prefix + container.name);
			_attrs = container._attrs;
			for (var i in _attrs) {
				var attr = _attrs[i];
				if (attr && i==attr.name) xml.push(' '+attr.name+'="'+attr.getValue()+'"');
			}
			xml.push('>'+container.getValue()+'</'+prefix+container.name+'>');
		}
		return xml.join('');
	},

	/**
	 * Generate xml for MultilinePatternContainer
	 *
	 * @param {Container} container
	 * @returns {String} the xml
	 */
	visitMultilinePattern : function(container) {
		var v = container.getValue().replace(/[\r]/g,'').split('\n');
		var xml = [];
		var prefix = container.ownerDocument.nsPrefix || '';
		if (prefix.length > 0) prefix += ':';
		for (var i=0,j=v.length; i<j; i++) {
			xml.push('<'+prefix + container.name+'>'+v[i]+'</'+prefix + container.name+'>');
		}
		return xml.join('');
	}
});var PATH;
if (!PATH) PATH={};
Object.extend(PATH,{
	GET_BIC_EXPANSION:"/powerx/BicValidateServlet",

	GET_CURRENCY_EXPANSION : "currency_expansion.json",

	getCamtSchemaPath : function(type) {
		return "jsons/" + type + ".json";
	},
	
	getCamtDictionaryPath : function(type, lang) {
		return "jsons/" + type + "_dict_" + lang + ".json";
	},
	
	getCamtRulePath : function(type) {
		return "rules/" + type + ".rule";
	},

	getCamtMessageXmlPath : function(id) {
		return "messageXML/"+id+".xml";
	},
	
	getFinSchemaPath : function(type) {
		return "jsons/" + type + ".json";
	},

	getFinDictionaryPath : function(type, lang) {
		return "jsons/" + type + "_dict_" + lang + ".json";
	},

	getFinMessageXmlPath : function(id) {
		return "messageXML/$fin."+id.replace("MT","")+".0705.xml";
	}
});

/**
 * @class ContainerEvents
 */
var ContainerEvents={
	_stack:{},
	add:function(name,id) {
		this._stack[name]={"id":id};//,"bit":bit
	},
	get:function(name) {
		return this._stack[name]||null;
	},
	getEventId:function(name) {
		if (this._stack[name]) return this._stack[name].id || 0;
		return -1;
	}
	/**
	 * deprecated
	getEventBit:function(name) {
		if (this._stack[name]) return this._stack[name].bit || 0;
		return -1;
	}*/
};

ContainerEvents.add("ComplexContainerSwitcher", 
	DOM.addEventListener(null,"click",new (Class.get("csp.ui.ComplexContainerSwitchListener"))()));

ContainerEvents.add("PatternContainerSwitcher", 
	DOM.addEventListener(null,"click",new (Class.get("csp.ui.PatternContainerSwitchListener"))()));

ContainerEvents.add("NewOccurrence", 
	DOM.addEventListener(null,"click",new (Class.get("csp.ui.NewOccurrenceListener"))()));

ContainerEvents.add("ExpandSequenceContainer", 
	DOM.addEventListener(null,"click",new (Class.get("csp.ui.SequenceContainerExpandListener"))()));

ContainerEvents.add("RemoveSequenceItem", 
	DOM.addEventListener(null,"click",new (Class.get("csp.ui.RemoveSequenceItemListener"))()));

ContainerEvents.add("OptionContainerSwitcher", 
	DOM.addEventListener(null,"change",new (Class.get("csp.ui.OptionContainerChangeListener"))()));

ContainerEvents.add("CommonListPattern", 
	DOM.addEventListener(null,"change",new (Class.get("csp.ui.ListPatternChangeListener"))()));

ContainerEvents.add("CommonTextPattern",
	DOM.addEventListener(null,"keyEvent,focusEvent",new (Class.get("csp.ui.TextPatternValidateListener"))()));

/*DOM.addPreEventListener(ContainerEvents.getEventId("CommonTextPattern"), 
	"focusEvent",new (Class.get("csp.ui.TextPatternValidateListener"))());*/

ContainerEvents.add("CommonTextAreaPattern",
	DOM.addEventListener(null,"focusEvent",new (Class.get("csp.ui.TextAreaPatternValidateListener"))()));

ContainerEvents.add("CommonDatePattern", 
	DOM.addEventListener(null,"keyEvent,focusEvent,change", 
	new (Class.get("csp.ui.DatePatternValidateListener"))()));

ContainerEvents.add("CommonComplexTextPattern",
	DOM.addEventListener(null,"keyEvent,focusEvent",new (Class.get("csp.ui.ComplexTextPatternValidateListener"))()));

/**
 * Define BICManager, CurrencyManager, ContainerManager
 */
var BICMgr=new (Class.get("csp.ui.ExpansionManager"))(
	"BIC", PATH.GET_BIC_EXPANSION, "csp.ui.GetBICExpansionCallback");

var CurMgr=new (Class.get("csp.ui.ExpansionManager"))(
	"Currency", PATH.GET_CURRENCY_EXPANSION, "csp.ui.GetCurrencyExpansionCallback");

var CtnMgr = Class.get("csp.ui.ContainerManager");

var finMsgMgr = new (Class.get("msg.ui.MessageManager"))({
	requestSchema : function(builder) {
		var msg,schema,dict;
		if (msg = builder.message) {
			// process getting schema
			if (schema = this.getSchema(msg.schemaType)) {
				builder.setSchema(schema);
			} else {
				this.sendRequest(PATH.getFinSchemaPath(msg.schemaType), builder, "addSchema");
			}

			// process getting dictionary
			if (dict = this.getDictionary(msg.schemaType, msg.language)) {
				builder.setDictionary(dict);
			} else {
				this.sendRequest(
					PATH.getFinDictionaryPath(msg.schemaType , msg.language), 
					builder, 
					"addDictionary");
			}
		}
	},

	addSchema : function(builder, response) {
		if (!response) {
			// TODO
			$win.alert("Build failure");
			builder.buildFailure();
			return;
		}

		// get the response text
		var text = response.text;
		// prepare the schema for the specific browser
		if (DOM.browser.isIE) {
			if (text.indexOf("\\r")<0) text = text.replace(/\\n/g,"\\\\r\\\\n");
		} else text = text.replace(/\\r\\n/g,"\\\\n");

		// add the schema to the manager
		this._addSchema(text);

		builder.setSchema(this.getSchema(builder.message.schemaType));
	},
	
	addDictionary : function(builder, response) {//debugger;
		if (!response) {
			builder.setDictionary(null);
			return;
		}

		// get the response text
		var text = response.text;

		// add the dictionary to the manager
		this._addDictionary(text);

		var msg = builder.message;
		builder.setDictionary(this.getDictionary(msg.schemaType, msg.language));
	}
});

var camtMsgMgr = new (Class.get("msg.ui.MessageManager"))({
	requestSchema : function(builder) {
		var msg, schema, dict, rule;
		if (msg = builder.message) {
			// process getting schema
			if (schema = this.getSchema(msg.schemaType)) {
				builder.setSchema(schema);
			} else {
				this.sendRequest(PATH.getCamtSchemaPath(msg.schemaType), builder, "addSchema");
			}

			// process getting dictionary
			if (dict = this.getDictionary(msg.schemaType, msg.language)) {
				builder.setDictionary(dict);
			} else {
				this.sendRequest(
					PATH.getCamtDictionaryPath(msg.schemaType , msg.language), 
					builder, 
					"addDictionary");
			}

			// process getting rules
			if (rule = this.getRule(msg.schemaType)) {
				builder.setRule(rule);
			} else {
				this.sendRequest(
					PATH.getCamtRulePath(msg.schemaType),
					builder,
					"addRule");
			}
		}
	},
	
	addSchema : function(builder, response) {
		if (!response) {
			// TODO
			builder.buildFailure();
			return;
		}

		// get the response text
		var text = response.text;
		// prepare the schema for the specific browser
		if (DOM.browser.isIE) {
			if (text.indexOf("\\r")<0) text = text.replace(/\\n/g,"\\\\r\\\\n");
		} else text = text.replace(/\\r\\n/g,"\\\\n");

		// add the schema to the manager
		this._addSchema(text);

		var name = builder.message.schemaType;
		if (name.indexOf("$") != -1) name = name.substring(name.indexOf("$") + 1);

		builder.setSchema(this.getSchema(name));
	},

	addDictionary : function(builder, response) {//debugger;
		if (!response) {
			builder.setDictionary(null);
			return;
		}

		// get the response text
		var text = response.text;

		// add the dictionary to the manager
		this._addDictionary(text);

		var msg = builder.message;
		builder.setDictionary(this.getDictionary(msg.schemaType, msg.language));
	},
	
	addRule : function(builder, response) {
		if (!response) {
			builder.setRule(null);
			return;
		}

		// get the response text
		var text = response.text;

		// add the rule to the manager
		this._addRule(text);

		builder.setRule(this.getRule(builder.message.schemaType));
	}
});
//var MsgMgr = Class.get("csp.ui.MessageManager");

var CurrentDictionary = {};
var currentDocument;

$win.loadMessage = function () {
	var type = $doc.getElementById("msgType").value;
	var id = $doc.getElementById("msgId").value;
	var lang = $doc.getElementById("msgLang").value;
	var readOnly = $doc.getElementById("editMode").value=="readOnly"?true:false;

	// load message UI entry
	switch(type) {
		case "fin" : loadFinMessage(id, lang, readOnly);break;
		case "camt" : loadCamtMessage(id, lang, readOnly);break;
	}

	/*
	var builder = new (Class.get("Builder"))();
	var filler = new (Class.get("Filler"))();
	var buildLocker = new (Class.get("util.SimpleLocker"))(builder,"build");
	var locker = new (Class.get("util.SimpleLocker"))(filler,"fill");
	builder.setLanguage(lang);
	builder.setLocker(locker);
	RequestPool.add(new Request("jsons/"+id+".json",new (Class.get("SchemaCallback"))(buildLocker,builder)));
	RequestPool.add(new Request("jsons/"+id+"_dict_"+lang+".json",new (Class.get("MessageDictCallback"))(buildLocker)));
	//RequestPool.add(new Request("messageXML/"+id+".xml",new (Class.get("MessageXMLCallback"))(locker,filler)));
	*/
}

/**
 * Entry for loading fin message
 */
function loadFinMessage(id, lang, readOnly) {
	var builder = new (Class.get("msg.ui.MessageBuilder"))({
		schemaType : id,
		wrapper : "innerWrapper",
		language : lang,
		xmlId : id,
		readOnly : readOnly,
		messageManager : finMsgMgr
	}, {
		bit : 3,

		createMessage : function() {
			var msg = this.message;
			currentDocument = new (Class.get("msg.ui.DocumentContainer"))(this.getSchema()
				//{
				//node : 
				//dict : this.getDictionary(),
				//wrapper : msg.wrapper
				//generateXmlVisitor : "msg.ui.impl.GenerateXMLVisitor",
				//containerBuilder : "csp.ui.ContainerBuilder"
			//}
			);
			currentDocument.setLanguage(lang);
			currentDocument.setContainerWrapper(msg.wrapper);
			currentDocument.setMessageManager(finMsgMgr);
			currentDocument.setGenerateXmlVisitor("csp.ui.impl.GenerateXMLVisitor");
			currentDocument.setContainerBuilder("csp.ui.ContainerBuilder");
			currentDocument.buildMessage();

			msg.document = currentDocument;
			this.fillMessage();
		},
		
		getXmlRoot : function(xml) {
			return xml;
		},
		
		requestMessageXml : function() {
			/*if (this.message.xmlId) {
				RequestPool.add(new Request(PATH.getFinMessageXmlPath(this.message.xmlId), new (Class.get("msg.ui.GetMessageXmlCallback"))(this)));
			}*/
			var xml = $("xml").value;
			if (xml.length == 0) return this.setXml(this.XML_NOT_EXIST);
			this.setXml(XML.parse(xml));
			$win._xml = xml;
		}
	});
}

/**
 * Entry for loading camt message
 */
function loadCamtMessage(id, lang, readOnly) {
	var builder = new (Class.get("msg.ui.MessageBuilder"))({
		schemaType : id,
		wrapper : "innerWrapper",
		language : lang,
		xmlId : id,
		readOnly : readOnly,
		messageManager : camtMsgMgr
	}, {
		bit : 7,

		createMessage : function() {
			var msg = this.message;
			currentDocument = new (Class.get("msg.ui.DocumentContainer"))(this.getSchema()
				//{
				//node : this.getSchema()
				//dict : this.getDictionary(),
				//rule : this.getRule(),
				//wrapper : msg.wrapper
				//generateXmlVisitor : "ecp.ui.impl.GenerateXMLVisitor",
				//containerBuilder : "ecp.ui.ContainerBuilder"
			//}
			);
			currentDocument.setLanguage(lang);
			currentDocument.setContainerWrapper(msg.wrapper);
			currentDocument.setMessageManager(camtMsgMgr);
			currentDocument.setGenerateXmlVisitor("ecp.ui.impl.GenerateXMLVisitor");
			currentDocument.setContainerBuilder("ecp.ui.ContainerBuilder");
			currentDocument.buildMessage();

			if (this.getRule()) currentDocument.applyRule(this.getRule());
			msg.document = currentDocument;
			this.fillMessage();
			fillCreator();
		},
		
		requestMessageXml : function() {
			/*if (this.message.xmlId) {
				RequestPool.add(new Request(PATH.getMessageXmlPath(this.message.xmlId), new (Class.get("msg.ui.GetMessageXmlCallback"))(this)));
			}*/
			var xml = $("xml").value;
			if (xml.length == 0) return this.setXml(this.XML_NOT_EXIST);
			this.setXml(XML.parse(xml));
			$win._xml = xml;
		}
	});
}

$win.showOptional = function(elem) {
	if (currentDocument) {
		var msg = currentDocument.ownerDocument.childs[currentDocument.ownerDocument.messageType];//.message
		if (elem.value=="hide") {
			msg.showOptional(false);
			elem.value="show";
		} else {
			msg.showOptional(true);
			elem.value="hide";
		}
	}
}

$win.selectContainer = function(elem) {
	var t = elem.value;
	if (currentDocument) {
		$win.selectedContainer = currentDocument.select(t);
		if ($win.selectedContainer) Console.put("Select [" + t + "] result: " + $win.selectedContainer.length);
	}
}

$win.searchContainer = function(elem) {
	var t = $('search').value;
	//$win.searchResult = CtnMgr.search($win.currentMsg,t);
	//if ($win.currentMsg) $win.currentMsg.showMatch(t);
	if (currentDocument) currentDocument.message.showMatch(t);
}

$win.showXML = function(elem) {
	//$('xml').value=$win.currentMsg.toXML();
	if (currentDocument) $('xml').value='<?xml version="1.0" encoding="UTF-8"?>'+currentDocument.toXML();
}

function fillCreator() {
	var v = $doc.getElementById('creator').value;
	var a = $doc.getElementById('assnge').value;
	var d = Format.formatDate(new Date(),"YY-MM-DDTHH:II:SS");
	if (currentDocument) {
		var msg = currentDocument.getMessage();
		var Hdr = msg.childs[msg.node.childs[0].name];
		var Case = msg.childs[msg.node.childs[1].name];
		if (v.length>0) {
			var creator = Hdr.childs[Hdr.node.childs[1].name];
			creator.setValue(v);
			creator.validate();
			creator = Case.childs[Case.node.childs[1].name];
			creator.setValue(v);
			creator.validate();
		}
		if (a.length>0) {
			var assnge = Hdr.childs[Hdr.node.childs[2].name];
			assnge.setValue(a);
			assnge.validate();
		}
		var dtTm = Hdr.childs[Hdr.node.childs[3].name];
		dtTm.setValue(d);
		dtTm.validate();
	}
}

/*
function setEditMode() {
	var m = $doc.getElementById('editMode').value;
	//if (m=="readOnly") {}
	if (currentDocument) {
		currentDocument.message.setReadOnly(m=="readOnly");
	}
}

Class.create("Builder",null,null,{
	_schema : null,
	setLocker : function(locker) {
		this._locker = locker;
		this._key = locker.getKey();
	},
	setLanguage : function(lang) {
		this.lang = lang;
	},
	build : function() {
		if (this._schema) {
			var begin=(new Date()).getTime();
			try {
				if (currentDocument) currentDocument.destroy();
				//$("innerWrapper").innerHTML = '';
				//_parentTestObj.childs={};
				//_parentTestObj.clear();
				var schema = this._schema;
				if (DOM.browser.isIE) schema = schema.replace(/\\n/g,"\\\\r\\\\n");
				$win._schema = _schema=eval('('+schema+')');
				MsgMgr.addSchema(_schema);
				currentDocument = new (Class.get("msg.ui.DocumentContainer"))(_schema,'innerWrapper',this.lang);
				/**
				 * The following execution is for the initializing process.
				 * /
				fillCreator();
				//UIObjectTree.walk(_schema,_parentTestObj);
				//MsgMgr.createUI(_parentTestObj,MsgMgr.addSchema(_schema));
				this._locker.insertKey(this._key);
			} catch(e) {
				Console.error(e.message);
				throw e;
			}
			Console.put("Created "+_schema.childs[0].name+" UI in "+((new Date()).getTime()-begin)+"ms");
		}
	}
});

Class.create("Filler",null,null,{
	fill : function() {
		var _xml,nodes;
		Console.put("begin to fill.");
		var begin=(new Date()).getTime();
		var xml = $("xml").value;
		if (xml.length==0) return setEditMode();
		xml = XML.parse(xml);
		//xml = XML.selectSingleNode(xml.documentElement,'/Document',null);
		$win._xml = _xml = xml;
		//return;
		//$win._xml = _xml = this._xml;
		//$win._parentTestObj = _parentTestObj;
		//nodes = _xml.childNodes;
		var root = _xml.documentElement;
		nodes = root.childNodes;
		for (var i=0,j=nodes.length; i<j; i++) {
			if (nodes[i].nodeType!=1) continue;
			var name = nodes[i].nodeName;
			//_parentTestObj.childs[name].fill(nodes[i]);
			currentDocument.message.fill(nodes[i]);
			//Console.put("Validate result: "+_parentTestObj.childs[name].validate());
			Console.put("Validate result: "+currentDocument.message.validate());
		}
		Console.put("Fill "+_schema.childs[0].name+" in "+((new Date()).getTime()-begin)+"ms");
		//Console.put("Finished fill.");
		setEditMode();
	},
	setXML : function(xml) {
		this._xml = xml;
	}
});

Class.create("MessageDictCallback",null,["http.RequestCallback"],{
	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
		this._locker.insertKey(this._key);
		delete this._key;
		delete this._locker;
	},
	init : function(locker) {
		this._locker = locker;
		this._key = locker.getKey();
	},
	onResponseReceived : function(request, response) {
		MsgMgr.addDictionary(eval('('+response.text+')'));
		this._locker.insertKey(this._key);
	}
});

Class.create("MessageXMLCallback",null,["http.RequestCallback"],{
	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
	},
	init : function(locker, filler) {
		this._locker = locker;
		this._filler = filler;
		this._key = locker.getKey();
	},
	onResponseReceived : function(request, response) {
		this._filler.setXML(response.xml);
		this._locker.insertKey(this._key);
	}
});

var _schema;
//var SchemaCallback={
Class.create("SchemaCallback",null,["http.RequestCallback"],{
	init : function(locker,builder) {
		this._locker = locker;
		this._key = locker.getKey();
		this._builder = builder;
	},
	onError : function(request, response, e) {
		Console.error("Error occur in getting url("+request.url+")");
		Console.error("Error message: "+e.message);
	},
	onResponseReceived:function(request,response) {
		this._builder._schema = response.text;
		this._locker.insertKey(this._key);
		return;
		/*var begin=(new Date()).getTime();
		try {
			$("innerWrapper").innerHTML = '';
			_parentTestObj.childs={};
			var schema = response.text;
			if (DOM.browser.isIE) schema = schema.replace(/\\n/g,"\\\\r\\\\n");
			$win._schema = _schema=eval('('+schema+')');
			//UIObjectTree.walk(_schema,_parentTestObj);
			MsgMgr.createUI(_parentTestObj,MsgMgr.addSchema(_schema));
			this._locker.insertKey(this._key);
		} catch(e) {
			Console.put(e.message);
			throw e;
		}
		Console.put("Created "+_schema.childs[0].name+" UI in "+((new Date()).getTime()-begin)+"ms");* /
	}
});

var _parentTestObj={
	childs:{},
	_c:[],
	add:function(obj) {
		$win.currentMsg = this.childs[obj.name]=obj;
		//this._c.push(obj._container);
		$("innerWrapper").appendChild(obj._container);
	},
	clear : function() {
		var child;
		for (var i in this.childs) {
			child = this.childs[i];
			if (child && i==child.name) child.destroy();
		}
		child = null;
		this.childs = {};
	},
	init:function() {
		var _a=this._c,_b=$("innerWrapper");
		for (var i=0,j=_a.length;i<j;i++) {
			_b.appendChild(_a[i]);
		}
	}
};
*/
Window.addLoadListener({
	onLoad:function() {
		ContainerTemplates.init();
		$win.loadMessage();
		$win._win = window;
	}
});

Window.addCloseListener({
	onClose:function() {
		ContainerTemplates.destroy();
	}
});/**
 * @agent true;
 * @classpath ../ajax/client/;
 * @classpath scripts/;
 * @classpath ../csp/scripts/;
 *
 * @base ../ajax/client.cfg.js;
 * @base ../ajax/clientUI.cfg.js;
 * @base ../ajax/messageUI.cfg.js;
 *
 * @import csp.ui.*;
 * @import csp.ui.impl.*;
 * @import ecp.ui.*;
 * @import ecp.ui.impl.*;
 * @import testUIGenerator;
 *
 * @jsUnit ../jsunit/app/;
 * @test tests/;
 */