(function () {
    angular.module('CCC')
        .factory('Dropbox', Dropbox);

    Dropbox.$inject = ['$resource'];

    function Dropbox($resource) {
        return $resource('/ccc/api/dropbox/:id', { id: '@_id' }, {
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