(function () {
    angular.module('CCC')
        .factory('DeploymentsService', DeploymentsService);

    DeploymentsService.$inject = ['$resource'];

    function DeploymentsService($resource) {
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