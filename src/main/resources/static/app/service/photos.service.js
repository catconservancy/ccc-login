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
                    isArchived: '@isArchived',
					studyAreaId: '@studyAreaId',
					locationId: '@locationId',
					highlighted: '@highlighted',
					startDate: '@startDate',
					endDate: '@endDate',
					speciesIds: '@speciesIds',
					page: '@page',
					size: '@size',
					sort: '@sort',
                    path: '@path'
        		}
        	},

            archive: {
                method: 'POST',
                params: {
                    id: '@id',
                    index: '@index'
                }
            },

            resetPhotos: {
                method: 'POST',
                params: {
                    id: 'reset'
                }
            }
        });
    }
}());