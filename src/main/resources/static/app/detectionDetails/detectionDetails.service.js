(function () {
    angular.module('CCC')
        .factory('DetectionDetails', DetectionDetails);

    DetectionDetails.$inject = ['$resource'];

    function DetectionDetails($resource) {
        return $resource('/ccc/api/detectionDetail/:subquery_type/:id', { subquery_type:'@subquery_type', id: '@_id' }, {
            update: {
                method: 'PUT'
            },
            
            findBySpeciesId: {
            	method: 'GET',
            	cache: false,
            	isArray: true,
            	params: {
            		subquery_type: 'species',
            		id: '@id'
            	}
            }
        });
    }
}());