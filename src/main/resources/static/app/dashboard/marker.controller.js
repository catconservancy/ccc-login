(function () {
    angular.module('CCC')
        .controller('MarkerController', MarkerController);

    MarkerController.$inject = ['$log', '$scope'];

    function MarkerController($log, $scope) {
        var vm = this;
    }
}());