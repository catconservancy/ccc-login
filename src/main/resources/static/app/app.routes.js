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
	
	        .state('userNotifications', {
	            url: '/userNotifications',
	            templateUrl: 'app/userNotifications/userNotifications.html',
	            controller: 'UserNotificationsController',
	            controllerAs: 'userNotificationsCtrl'
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
	            controllerAs: 'viewPhotosCtrl'
	        })
	
	        .state('idPhotos', {
	            url: '/idPhotos',
	            templateUrl: 'app/idPhotos/idPhotos.html',
	            controller: 'IdPhotosController',
	            controllerAs: 'idPhotosCtrl'
	        })
	
	        .state('deployments', {
	            url: '/deployments',
	            templateUrl: 'app/deployments/deployments.html',
	            controller: 'DeploymentsController',
	            controllerAs: 'deploymentsCtrl'
	        })
	
	        .state('folderLocation', {
	            url: '/folderLocation',
	            templateUrl: 'app/folderLocation/folderLocation.html',
	            controller: 'FolderLocationController',
	            controllerAs: 'folderLocationCtrl'
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
	            controllerAs: 'cameraMonitoringLogCtrl'
	        });

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    };
}());