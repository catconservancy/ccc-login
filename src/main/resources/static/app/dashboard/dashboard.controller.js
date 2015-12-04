(function () {
    angular.module(window.angularModuleName)
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = [];

    function DashboardController() {
        var vm = this;
    }
}());