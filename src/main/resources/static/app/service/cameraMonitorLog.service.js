(function () {
    angular.module('CCC')
        .factory('CameraMonitorLogs', CameraMonitorLogs);

    CameraMonitorLogs.$inject = ['$resource'];

    function CameraMonitorLogs($resource) {
        return $resource('/ccc/api/cameraMonitor/:subquery_type/:id', { subquery_type:'@subquery_type', id: '@_id' }, {
        	update: {
        		method: 'PUT'
        	},
            
            findByDeploymentId: {
            	method: 'GET',
            	cache: false,
            	isArray: true,
            	params: {
            		subquery_type: 'deployment',
            		id: '@id'
            	}
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