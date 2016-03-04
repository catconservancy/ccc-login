(function () {
    angular.module('CCC')
        .factory('DetectionDetails', DetectionDetails);

    DetectionDetails.$inject = ['$resource'];

    function DetectionDetails($resource) {
        return $resource('/ccc/api/detectionDetail/:id', { id: '@_id' }, {
            update: {
                method: 'PUT'
            }
        });
    }
}());