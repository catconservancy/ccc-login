(function () {
    angular.module(window.angularModuleName)
        .controller('GlobalController', GlobalController);

    GlobalController.$inject = ['$scope'];

    function GlobalController($scope) {
        var vm = this;
        vm.bodyClass = 'login-page';

        $scope.$on('$stateChangeSuccess', function (event, toState) {
            switch (toState.name) {
                case 'setup':
                case 'login':
                    vm.bodyClass = 'login-page';
                    break;
                case 'register':
                    vm.bodyClass = 'register-page';
                    break;
                case 'lock':
                    vm.bodyClass = 'lockscreen';
                    break;
                default:
                    vm.bodyClass = 'skin-acct skin-green sidebar-mini';
                    break;
            }
        });
    }
}());