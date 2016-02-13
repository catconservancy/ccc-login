(function () {
    angular.module('CCC')
        .factory('Users', Users);

    Users.$inject = ['$resource'];

    function Users($resource) {
        return $resource('/ccc/api/users/:id', { id: '@_id' }, {
        	update: {
        		method: 'PUT'
        	},
        	
        	query: {
        		method: 'GET', 
        		isArray: true, 
        		params: {
        			enabled: '@enabled'
        		}
        	}
        });
    }
}());