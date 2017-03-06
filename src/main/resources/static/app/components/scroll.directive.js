(function() {
    angular.module('CCC').directive('scroll', scroll);
    scroll.$inject = [];
    function scroll() {
        return function(scope, element, attrs) {

            element.bind("scroll", function() {
                //visible width + pixel scrolled = total width
                if(this.offsetWidth + this.scrollLeft == this.scrollWidth) {
                    scope.$emit('getMore');

                }
                scope.$apply();
            });
        };
    }
}());