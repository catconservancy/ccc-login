(function () {
    angular.module(window.angularModuleName)
        .config(config);

    config.$inject = [
        '$locationProvider', '$httpProvider', '$stateProvider', '$urlRouterProvider'
    ];

    function config($locationProvider, $httpProvider, $stateProvider, $urlRouterProvider) {

        $locationProvider.html5Mode(true);

        $urlRouterProvider.otherwise('/');

        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: '/app/login/login.html',
                controller: 'LoginController',
                controllerAs: 'login',
                data: {
                    insecure: true
                }
            })
            .state('register', {
                url: '/register',
                templateUrl: '/app/register/register.html',
                controller: 'RegisterController',
                controllerAs: 'register',
                data: {
                    insecure: true
                }

            })
            .state('shell', {
                templateUrl: '/app/layout/shell.html',
                controller: 'ShellController',
                controllerAs: 'shell'
            })
            .state('shell.dashboard', {
                url: '/',
                templateUrl: '/app/dashboard/dashboard.html',
                controller: 'DashboardController',
                controllerAs: 'dc',
                data: {
                    title: 'Dashboard'
                }
            })
            .state('shell.alerts', {
                url: '/alerts',
                templateUrl: '/app/alerts/alerts.html',
                controller: 'AlertsController',
                controllerAs: 'ac',
                data: {
                    title: 'Alerts'
                }
            });;

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    }
}());