(function () {
    angular.module('CCC')
        .factory('StudyAreas', StudyAreas);

    StudyAreas.$inject = ['$resource'];

    function StudyAreas($resource) {
        return $resource('/ccc/api/studyAreas/:id', { id: '@_id' }, {
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