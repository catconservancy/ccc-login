(function () {
    angular.module('CCC')
        .config(config);

    config.$inject = [
        '$locationProvider', '$httpProvider', '$stateProvider', '$urlRouterProvider'
    ];

    function config($locationProvider, $httpProvider, $stateProvider, $urlRouterProvider) {

        $locationProvider.html5Mode(true);
        
        $urlRouterProvider.otherwise('/dashboard');
        
        $stateProvider

	        .state('dashboard', {
	            url: '/dashboard',
	            templateUrl: 'app/dashboard/dashboard.html',
	            controller: 'DashboardController',
	            controllerAs: 'dashboard'
	        })
	
	        .state('users', {
	            url: '/users',
	            templateUrl: 'app/users/users.html',
	            controller: 'UsersController',
	            controllerAs: 'userCtrl'
	        })
	
	        .state('importPhotos', {
	            url: '/importPhotos',
	            templateUrl: 'app/importPhotos/importPhotos.html',
	            controller: 'ImportPhotosController',
	            controllerAs: 'ipc'
	        })
	
	        .state('viewPhotos', {
	            url: '/viewPhotos',
	            templateUrl: 'app/viewPhotos/viewPhotos.html',
	            controller: 'ViewPhotosController',
	            controllerAs: 'viewPhotosCtrl',
                params: {
                    "deploymentId": null,
                    "cannedQuery": null
                }
	        })
	
	        .state('idPhotos', {
	            url: '/idPhotos',
	            templateUrl: 'app/idPhotos/idPhotos.html',
	            controller: 'IdPhotosController',
	            controllerAs: 'idPhotosCtrl',
                params: {"dropboxPath":null}
	        })
	
	        .state('deployments', {
	            url: '/deployments',
	            templateUrl: 'app/deployments/deployments.html',
	            controller: 'DeploymentsController',
	            controllerAs: 'deploymentsCtrl',
                params: {"deploymentId":null}
	        })
	
	        .state('studyAreas', {
	            url: '/studyAreas',
	            templateUrl: 'app/studyAreas/studyAreas.html',
	            controller: 'StudyAreasController',
	            controllerAs: 'studyAreaCtrl'
	        })
	
	        .state('species', {
	            url: '/species',
	            templateUrl: 'app/species/species.html',
	            controller: 'SpeciesController',
	            controllerAs: 'speciesCtrl'
	        })
	
	        .state('detectionDetails', {
	            url: '/detectionDetails',
	            templateUrl: 'app/detectionDetails/detectionDetails.html',
	            controller: 'DetectionDetailsController',
	            controllerAs: 'detectionDetailsCtrl'
	        })
	
	        .state('initialSetup', {
	            url: '/initialSetup',
	            templateUrl: 'app/initialSetup/initialSetup.html',
	            controller: 'InitialSetupController',
	            controllerAs: 'initialSetupCtrl'
	        })
	
	        .state('cameraMonitoringLog', {
	            url: '/cameraMonitoringLog',
	            templateUrl: 'app/cameraMonitoringLog/cameraMonitoringLog.html',
	            controller: 'CameraMonitoringLogController',
	            controllerAs: 'cameraLogCtrl'
	        })

			.state('userProfile', {
				url: '/userProfile',
				templateUrl: 'app/userProfile/userProfile.html',
				controller: 'UserProfileController',
				controllerAs: 'userProfileCtrl'
			});

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    }
}());