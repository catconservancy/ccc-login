(function () {
    angular.module(window.angularModuleName)
        .factory('AlertsService', AlertsService);

    AlertsService.$inject = ['$resource'];

    function AlertsService($resource) {
        return $resource('/api/alerts/:id', {}, {
            update: {method: 'PATCH', isArray: false}
        });
    }
}());