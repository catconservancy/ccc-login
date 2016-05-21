(function () {
    angular.module('CCC')
        .factory('Deployments', Deployments);

    Deployments.$inject = ['$resource'];

    function Deployments($resource) {
        return $resource('/ccc/api/deployment/:id', { id: '@_id' }, {
        	update: {
        		method: 'PUT'
        	},
        	
        	query: {
        		method: 'GET', 
        		isArray: true, 
        		params: {
        			path: '@path'
        		}
        	}
        });
    }
}());