(function () {
    angular.module(window.angularModuleName)
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$state', 'AuthService'];

    function LoginController($state, AuthService) {
        var vm = this;
        vm.invalidLogin = false;
        vm.invalidTwoFactor = false;
        vm.credentials = {
            email: null,
            password: null,
            validationCode: null
        };
        vm.requireTwoFactor = false;
        vm.submit = submit;

        function submit() {
            AuthService.login(vm.credentials, function (err) {
                if (err) {
                    if (err.message === '2FA') {
                        vm.invalidLogin = false;
                        vm.requireTwoFactor = true;
                    } else {
                        vm.invalidLogin = true;
                    }
                    return;
                }

                $state.go('shell.dashboard');
            });
        }
    }
}());