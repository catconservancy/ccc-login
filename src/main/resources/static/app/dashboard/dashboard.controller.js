(function () {
    angular.module('CCC')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$log', '$timeout', 'Deployments', 'GisUtility', 'uiGmapGoogleMapApi', 'DashboardService'];

    function DashboardController($log, $timeout, Deployments, GisUtility, uiGmapGoogleMapApi, DashboardService) {
        var vm = this;
        vm.counts = {};
        vm.selectedMarker = {};
        vm.map = {};
        vm.options = {};

        DashboardService.getCounts(function (resp) {
            vm.counts = resp.data;
        });

        DashboardService.getMapData(function (data) {
            vm.options = data.options;
            vm.map = data.mapData;
            vm.map.markersEvents = {
                click: function (marker, eventName, model) {
                    $log.debug('Click marker', marker);
                    vm.selectedMarker = marker;
                    vm.map.window.model = model;
                    vm.map.window.show = true;
                }
            };

            uiGmapGoogleMapApi.then(function (maps) {
                $log.debug('got to uiGmapGoogleMapApi.then', maps);
                $timeout(function () {
                    Deployments.query(function (data) {
                        var markers = [];
                        for (var i = 0; i < data.length; i++) {
                            var deployment = data[i];
                            if (deployment.utmE && deployment.utmN && deployment.utmZone) {
                                var myLatLng = GisUtility.convertUtmToLatLong(deployment.utmE, deployment.utmN, deployment.utmZone);
                                if (myLatLng) {
                                    var marker = {
                                        id: i,
                                        latitude: myLatLng.lat,
                                        longitude: myLatLng.lng,
                                        title: deployment.studyArea.name + ": " + deployment.locationID,
                                        deployment: deployment
                                    };
                                    markers.push(marker);
                                }
                            }
                        }
                        vm.deploymentMarkers = markers;
                    });
                });
            });
        });
    }
}());