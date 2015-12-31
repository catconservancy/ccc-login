(function () {
    angular.module('CCC')
        .factory('Species', Species);

    Species.$inject = ['$resource'];

    function Species($resource) {
        return $resource('/ccc/api/species/:id', { id: '@_id' }, {
        	update: {
        		method: 'PUT'
        	}
        });
    }
}());