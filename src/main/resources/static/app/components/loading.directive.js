//(function () {
//    angular.module('CCC').directive('ngLoading', ngLoading);
//
//    ngLoading.$inject = ['$compile'];
//
//    function ngLoading($compile) {
//
//        var loadingSpinner = '<div class="overlay"><i class="fa fa-refresh fa-spin"></i></div>';
//
//        return {
//            restrict: 'A',
//            link: function (scope, element, attrs) {
////                var originalContent = element.html();
//                var originalContent = $compile(element.contents())(scope);
//                element.html(loadingSpinner);
//                scope.$watch(attrs.ngLoading, function (val) {
//                    if(val) {
//                        if(val.notFound) {
//                            element.html(attrs.ngLoadingNotFound ? attrs.ngLoadingNotFound : "Not found.");
//                        } else {
//                            element.html(originalContent);
////                            $compile(element.contents())(scope);
//                        }
//                    } else {
//                        element.html(loadingSpinner);
//                    }
//                });
//            }
//        }
//    }
//}());
(function() {
	angular.module('CCC').directive('ngLoading', ngLoading);
	ngLoading.$inject = [ '$compile' ];
	function ngLoading($compile) {
		function matchSizeClass(size) {
			var sizeClass;
			switch (size) {
			case "sm":
				sizeClass = "fa-lg";
				break;
			case "md":
				sizeClass = "fa-3x";
				break;
			case "lg":
				sizeClass = "fa-4x";
				break;
			case "xl":
				sizeClass = "fa-5x";
				break;
			default:
				sizeClass = "fa-3x";
			}
			return sizeClass;
		}
		function matchTypeClass(type) {
			var typeClass;
			switch (type) {
			case "gears":
				typeClass = "fa-cog";
				break;
			case "dots":
				typeClass = "fa-spinner";
				break;
			default:
				typeClass = "fa-circle-o-notch";
			}
			return typeClass;
		}
		function matchTopPercent(size) {
			var topPercent;
			switch (size) {
			case "sm":
				topPercent = "53%";
				break;
			case "md":
				topPercent = "49%";
				break;
			case "lg":
				topPercent = "47%";
				break;
			case "xl":
				topPercent = "35%";
			}
			return topPercent;
		}
		function compileTemplate(scope, template, typeClass, sizeClass, topPct,
				leftPct, text) {
			var _template = _.clone(template);
			_template = _template.replace("{size}", sizeClass).replace(
					"{type}", typeClass);
			return $compile(angular.element(_template))(scope);
		}
		var link = function(scope, element, attrs) {
			var e = angular.element(element[0]), spinnerTemplate = (attrs.text ? '<h3 style="position:absolute; top:{{top}}; left:{{left}};">{{text}}</h3>'
					: '')
					+ '<i class="fa {size} {type} manual-loading-indicator"></i>', overlay = true, sizeClass = "fa-3x", typeClass = "fa-circle-o-notch", spinner, zIndex;
			if (attrs.overlay) {
				overlay = attrs.overlay === "true" ? true : false;
			}
			e.hide();
			zIndex = e.parent().css("z-index");
			if (isNaN(zIndex)) {
				e.css("z-index", 1);
			} else {
				e.css("z-index", zIndex + 1);
			}
			if (overlay) {
				e.addClass("manual-overlay");
			}
			attrs.$observe("loading", function(loading) {
				if (loading.toLowerCase() === "true") {
					e.show();
				} else {
					e.hide();
				}
			});
			attrs.$observe("type", function(type) {
				typeClass = matchTypeClass(type);
				element.empty();
				spinner = compileTemplate(scope, spinnerTemplate, typeClass,
						sizeClass);
				element.append(spinner);
			});
			attrs.$observe("size", function(size) {
				sizeClass = matchSizeClass(size);
				element.empty();
				spinner = compileTemplate(scope, spinnerTemplate, typeClass,
						sizeClass);
				spinner.css({
					top : matchTopPercent(size)
				});
				element.append(spinner);
			});
			attrs.$observe("text", function(text) {
				scope.text = text ? text : '';
			});
			attrs.$observe("left", function(left) {
				scope.left = left ? left : '30%';
				if (left) {
					spinner = element.children()[0];
					spinner.style.left = left;
				}
			});
			attrs.$observe("top", function(top) {
				scope.top = top ? top : '40%';
				if (top) {
					spinner = element.children()[0];
					spinner.style.top = top;
				}
			});
		};
		return {
			link : link,
			replace : true,
			restrict : "A"
		};
	}
}());
