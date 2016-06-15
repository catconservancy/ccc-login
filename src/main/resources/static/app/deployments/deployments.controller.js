(function () {
    angular.module('CCC')
        .controller('DeploymentsController', DeploymentsController);

    DeploymentsController.$inject = ['$log', '$scope', 'StudyAreas', 'Deployments', 'LookupOption'];

    function DeploymentsController($log, $scope, StudyAreas, Deployments, LookupOption) {
        var vm = this;
        vm.inserted = {};
        vm.selectedDeployment = null;
        vm.successText = null;
        vm.deployments = [];
        vm.studyAreas = [];       
        vm.timeOfDayOptions = [];
        vm.startDatePopup = { opened: false };
        vm.endDatePopup = { opened: false }; 
        vm.dateOptions = {
		    formatYear: 'yy',
		    startingDay: 1
        };
        vm.lookups = [
                      'timeOfDay',
                      'dominantSubstrate',
                      'trailType',
                      'positionOnSlope',
                      'habitatRuggedness',
                      'topographicFeature',
                      'vegetationType',
                      'rangelandUse',
                      'humanVisitation',
                      'distanceToHumanHabitat',
                      'distanceToRoad',
                      'roadType',
                      'azimuth',
        ];
        vm.lookupLists = {};
        
        vm.add = add;
        vm.onSelectDeploymentCallback = onSelectDeploymentCallback;
        vm.onSelectStudyAreaCallback = onSelectStudyAreaCallback;
        vm.onSelectLookupOptionCallback = onSelectLookupOptionCallback;
        vm.startDateOpen = startDateOpen;
        vm.endDateOpen = endDateOpen;
        vm.save = save;
        
        Deployments.query(function(data) {
        	vm.deployments = data
        });

        StudyAreas.query(function(data) {
            vm.studyAreas = data;
        });

        for (var i = 0; i < vm.lookups.length; i++) {
            LookupOption.findByListCode({listCode: vm.lookups[i]}, function(options) {
            	vm.lookupLists[options[0].listCode] = options;
            });
        }
        
        function onSelectDeploymentCallback(item) {
        	vm.selectedDeployment = item;
            vm.successText = null;
        }
        
        function onSelectStudyAreaCallback(item) {
        	vm.selectedDeployment.studyArea = item;
        }
        
        function onSelectLookupOptionCallback(item) {
        	vm.selectedDeployment[item.listCode] = item.label;
        }

        function startDateOpen() {
        	vm.startDatePopup.opened = true;
        };

        function endDateOpen() {
        	vm.endDatePopup.opened = true;
        };

        function save() {
            if (!vm.selectedDeployment.id || vm.selectedDeployment.id === 0) {
                return Deployments.save(vm.selectedDeployment, function(deployment) {
                	vm.successText = 'You have successfully created deployment location (' +deployment.studyArea.name+':'+deployment.locationID+').';
                    $log.debug('save success');
                    vm.selectedDeployment = deployment;
                    Deployments.query(function(data) {
                        vm.deployments = data;
                    });
                });
            } else {
                vm.entry = Deployments.get({ id: vm.selectedDeployment.id }, function() {
                    vm.entry.azimuth = vm.selectedDeployment.azimuth;
                    vm.entry.distanceToHumanHabitat = vm.selectedDeployment.distanceToHumanHabitat;
                    vm.entry.distanceToRoad = vm.selectedDeployment.distanceToRoad;
                    vm.entry.dominantSubstrate = vm.selectedDeployment.dominantSubstrate;
                    vm.entry.endDate = vm.selectedDeployment.endDate;
                    vm.entry.habitatRuggedness = vm.selectedDeployment.habitatRuggedness;
                    vm.entry.humanHabitatType = vm.selectedDeployment.humanHabitatType;
                    vm.entry.humanVisitation = vm.selectedDeployment.humanVisitation;
                    vm.entry.locationID = vm.selectedDeployment.locationID;
                    vm.entry.notes = vm.selectedDeployment.notes;
                    vm.entry.ownership = vm.selectedDeployment.ownership;
                    vm.entry.positionOnSlope = vm.selectedDeployment.positionOnSlope;
                    vm.entry.rangelandUse = vm.selectedDeployment.rangelandUse;
                    vm.entry.roadType = vm.selectedDeployment.roadType;
                    vm.entry.startDate = vm.selectedDeployment.startDate;
                    vm.entry.timeOfDay = vm.selectedDeployment.timeOfDay;
                    vm.entry.topographicFeature = vm.selectedDeployment.topographicFeature;
                    vm.entry.trailType = vm.selectedDeployment.trailType;
                    vm.entry.utmDatum = vm.selectedDeployment.utmDatum;
                    vm.entry.utmE = vm.selectedDeployment.utmE;
                    vm.entry.utmN = vm.selectedDeployment.utmN;
                    vm.entry.utmZone = vm.selectedDeployment.utmZone;
                    vm.entry.vegetationType = vm.selectedDeployment.vegetationType;
                    vm.entry.cameraMonitors = vm.selectedDeployment.cameraMonitors;
                    vm.entry.studyArea = vm.selectedDeployment.studyArea;
                    vm.entry.photos = vm.selectedDeployment.photos;
                    
                    vm.entry.$update(function(deployment) {
                        $log.debug('update success');
                    	vm.successText = 'You have successfully updated deployment location (' +deployment.studyArea.name+':'+deployment.locationID+').';
                        vm.selectedDeployment = deployment
                        Deployments.query(function(data) {
                            vm.deployments = data;
                        });
                    });
                });
            }
        };
        
        function add() {
        	Deployments.get({ id: 0 }, function(data) {
                vm.successText = null;
        		vm.inserted = data;
        		vm.inserted.startDate = new Date();
        		vm.inserted.endDate = new Date();
                vm.deployments.push(vm.inserted);
                vm.selectedDeployment = vm.inserted;
        	});
        }
    }
}());