(function () {
    angular.module(window.angularModuleName)
        .config(config);

    config.$inject = [
        '$locationProvider', '$httpProvider', '$stateProvider', '$urlRouterProvider'
    ];

    function config($locationProvider, $httpProvider, $stateProvider, $urlRouterProvider) {

        $locationProvider.html5Mode(true);
        
        $urlRouterProvider.otherwise('/start');
        
        $stateProvider
            
            // HOME STATES AND NESTED VIEWS ========================================
            .state('start', {
                url: '/start',
                templateUrl: 'app/dashboard/dashboard.html',
                controller: 'DashboardController',
                controllerAs: 'dashboard'
            })
            
            // ABOUT PAGE AND MULTIPLE NAMED VIEWS =================================
            .state('about', {
                // we'll get to this in a bit       
            });

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    };
}());