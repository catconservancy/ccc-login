window.angularModuleName = 'CCC';

(function () {
    angular.module(window.angularModuleName, [
        'ngResource',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'LocalForageModule',
        /*'angular-web-notification',*/
        'ngStomp',
        'ui.select',
        /*'chart.js',*/
        'ngToast'
    ])
        .config(config)
        .run(run);

    config.$inject = ['$compileProvider', 'ngToastProvider'];

    function config($compileProvider, ngToastProvider) {

        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);

        ngToastProvider.configure({
            horizontalPosition: 'right',
            verticalPosition: 'bottom',
            dismissOnClick: true
        });
    }

    run.$inject = ['$rootScope', '$log', '$http', '$state', 'AuthService'];

    function run($rootScope, $log, $http, $state, AuthService) {
        var bypass = false,
            checkInstall = true;
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
            if (bypass) {
                bypass = false;
                return;
            }
            event.preventDefault();
            bypass = true;

            if (checkInstall) {
                return $http.get('/auth/check-install').then(function () {
                    checkInstall = false;
                    doNavigation(toState, toParams);
                }, function () {
                    AuthService.logout(function () {
                        return $state.go('setup');
                    });
                });
            }

            doNavigation(toState, toParams);

        });

        function doNavigation(toState, toParams) {
            if (toState.name === 'setup') {
                return $state.go('login');
            }

            AuthService.checkAuthentication(function (isAuthenticated) {
                if (!isAuthenticated && (!toState.data || !toState.data.insecure)) {
                    return $state.go('login');
                }

                if (isAuthenticated && toState.name === 'login') {
                    return $state.go('shell.dashboard');
                }

                if (toParams)
                    return $state.go(toState.name, toParams);
                return $state.go(toState.name);
            });
        }
    }
}());