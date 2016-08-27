(function () {
    angular.module('CCC')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$log', '$scope', 'uiGmapGoogleMapApi'];

    function DashboardController($log, $scope, uiGmapGoogleMapApi) {
        var vm = this;
        // Do stuff with your $scope.
        // Note: Some of the directives require at least something to be defined originally!
        // e.g. $scope.markers = []

        // uiGmapGoogleMapApi is a promise.
        // The "then" callback function provides the google.maps object.
        vm.markers = [];
        vm.map = { center: { latitude: 45, longitude: -73 }, zoom: 8 };
        uiGmapGoogleMapApi.then(function(maps) {
        	$log.debug('got to uiGmapGoogleMapApi.then', maps);
        });
    }
}());