(function () {
    angular.module('CCC', [
        'ngResource',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'datatables',
        'xeditable',
        'mwl.confirm'
    ])
        .config(config)
        .run(run);

    config.$inject = [];

    function config() {
    	// TODO: config stuff
    	console.log('in config');
    }

    run.$inject = ['$rootScope', '$log'];

    function run($rootScope, $log) {
    	$log.debug('in run');
    }
}());