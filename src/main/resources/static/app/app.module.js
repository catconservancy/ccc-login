window.angularModuleName = 'CCC';

(function () {
    angular.module(window.angularModuleName, [
        'ngResource',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'LocalForageModule',
        'ui.select'
    ])
        .config(config)
        .run(run);

    config.$inject = [];

    function config() {
    	// TODO: config stuff
    }

    run.$inject = ['$rootScope', '$log'];

    function run($rootScope, $log) {
    	// TODO: run stuff
    }
}());