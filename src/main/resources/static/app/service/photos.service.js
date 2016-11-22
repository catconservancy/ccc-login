(function () {
    angular.module('CCC')
        .factory('PhotosService', PhotosService);

    PhotosService.$inject = ['$resource'];

    function PhotosService($resource) {
        return $resource('/ccc/api/photos/:id', { id: '@_id' }, {
        	update: {
        		method: 'PUT'
        	},
        	
        	query: {
        		method: 'GET', 
        		isArray: true, 
        		params: {
        			path: '@path'
        		}
        	},
        	
        	archive: {
        		method: 'POST', 
        		params: {
        			id: '@id',
        			index: '@index'
        		}
        	}
        });
    }
}());