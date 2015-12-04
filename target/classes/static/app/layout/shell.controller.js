(function () {
    angular.module(window.angularModuleName)
        .controller('ShellController', ShellController);

    ShellController.$inject = ['$state', 'AuthService'];

    function ShellController($state, AuthService) {

        var vm = this;
        vm.user = AuthService.user;
        vm.logout = logout;
        vm.title = $state.current.data.title;

        function logout() {
            AuthService.logout(function () {
                $state.go('login');
            });
        }
    }
}());