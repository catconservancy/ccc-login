(function () {
    angular.module('CCC')
        .controller('MarkerController', MarkerController);

    MarkerController.$inject = ['$log', '$scope'];

    function MarkerController($log, $scope) {
        $scope.templateValue = 'hello from the template itself';
        $scope.clickedButtonInWindow = function () {
            var msg = 'clicked a window in the template!';
            $log.info(msg);
            alert(msg);
        };
    }
}());