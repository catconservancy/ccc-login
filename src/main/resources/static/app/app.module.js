(function () {
    angular.module('CCC', [
        'ngResource',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'datatables',
        'xeditable',
        'mwl.confirm',
        'msl.uploads',
        'ui.select',
        'ui.bootstrap'
    ])
        .config(config)
        .run(run);

    config.$inject = [];

    function config() {
    	// TODO: config stuff
    	console.log('in config');
    }

    run.$inject = ['$rootScope', '$log', '$filter'];

    function run($rootScope, $log, $filter) {
    	$log.debug('in run');

        $rootScope.getObjectById = function(array,id) {
            return $filter('filter')(array, function (d) {return d.id === id;})[0];
        }
    }
}());