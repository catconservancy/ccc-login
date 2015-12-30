(function () {
    angular.module('CCC')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['$state', 'RegisterService'];

    function RegisterController($state, RegisterService) {

        var vm = this;
        vm.user = RegisterService.userModel;
        vm.repeatPassword = null;
        vm.termsAccepted = false;
        vm.submit = submit;

        function submit() {
            RegisterService.register(vm.user, function (err) {
                if (err) {
                    return;
                }

                $state.go('login');
            });
        }
    }
}());