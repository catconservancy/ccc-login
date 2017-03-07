(function () {
    angular.module('CCC')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$log', '$scope', '$timeout', 'Deployments', 'GisUtility', 'uiGmapGoogleMapApi'];

    function DashboardController($log, $scope, $timeout, Deployments, GisUtility, uiGmapGoogleMapApi) {
        var vm = this;
        vm.selectedMarker = {};
        vm.map = {
            center: {
                latitude: 40.1451,
                longitude: -99.6680
            },
            zoom: 4,
            bounds: {
                northeast: {
                    latitude: 45.1451,
                    longitude: -80.6680
                },
                southwest: {
                    latitude: 30.000,
                    longitude: -120.6680
                }
            },
            window: {
                marker: {},
                show: false,
                closeClick: function() {
                    this.show = false;
                },
                templateUrl: 'app/dashboard/deploymentMarkerPopup.html',
                templateParameter: {
                    message: 'passed in from the opener'
                },
                options: {}
            },
            markersEvents: {
                click: function(marker, eventName, model) {
                    $log.debug('Click marker', marker);
                    vm.selectedMarker = marker;
                    vm.map.window.model = model;
                    vm.map.window.show = true;
                }
            },
        };
        vm.options = {
            scrollwheel: false
        };

        uiGmapGoogleMapApi.then(function(maps) {
            $log.debug('got to uiGmapGoogleMapApi.then', maps);
            $timeout(function () {
                Deployments.query(function (data) {
                    var markers = [];
                    // var bounds = new maps.LatLngBounds();
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
                                }
                                // bounds.extend(marker.getPosition());
                                markers.push(marker);
                            }
                        }
                    }
                    vm.deploymentMarkers = markers;


                    // show="dashboard.map.window.show"
                    // coords="dashboard.map.window.model"
                    // options="dashboard.map.window.options"
                    // closeClick="dashboard.map.window.closeClick()"
                    // template="dashboard.map.template"
                    // var bounds = new maps.LatLngBounds();
                    // for (var i = 0; i < vm.markers.length; i++) {
                    //     bounds.extend(vm.markers[i].getPosition());
                    // }
                    // vm.map.bounds = bounds;

                    // vm.map = {
                    //     center: {latitude: bounds.getCenter().lat(), longitude: bounds.getCenter().lng()},
                    //     zoom: 8
                    // };
                    //// vm.map.fitBounds(bounds);

                });
            });
        });
    }
}());