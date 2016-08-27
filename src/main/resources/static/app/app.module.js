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
        'ui.bootstrap',
        'uiGmapgoogle-maps'
    ])
        .config(config)
        .run(run);

    config.$inject = ['uiGmapGoogleMapApiProvider'];

    function config(uiGmapGoogleMapApiProvider) {
	    uiGmapGoogleMapApiProvider.configure({
	    	key: 'AIzaSyBK2HK4jkNN7dOQSJzK81qEMNkcq233D0A',
	    	v: '3.20', //defaults to latest 3.X anyhow
	    	libraries: 'weather,geometry,visualization'
	    });
    }

    run.$inject = ['$rootScope', '$log', '$filter'];

    function run($rootScope, $log, $filter) {
    	$log.debug('in run');

        $rootScope.getObjectById = function(array,id) {
            return $filter('filter')(array, function (d) {return d.id === id;})[0];
        }
    }
}());