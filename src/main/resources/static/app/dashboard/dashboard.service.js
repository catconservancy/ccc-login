(function () {
    angular.module('CCC')
        .factory('DashboardService', DashboardService);

    DashboardService.$inject = ['$http', '$log'];

    function DashboardService($http, $log) {
        return {
            getCounts: function (callback) {
                $http.get('/ccc/api/utils/dashboardCounts').then(function (response) {
                    callback && callback(response);
                }, function (response) {
                    $log.debug('Failure', response);
                    callback && callback(response.data);
                });
            },

            getMapData: function (callback) {
                callback && callback({
                    mapData: {
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
                            closeClick: function () {
                                this.show = false;
                            },
                            templateUrl: 'app/dashboard/deploymentMarkerPopup.html',
                            templateParameter: {
                                message: 'passed in from the opener'
                            },
                            options: {}
                        }
                    },
                    options: {
                        scrollwheel: false
                    }
                });
            }
        };
    }

}());