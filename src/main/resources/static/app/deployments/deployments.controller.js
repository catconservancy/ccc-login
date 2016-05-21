(function () {
    angular.module('CCC')
        .controller('DeploymentsController', DeploymentsController);

    DeploymentsController.$inject = ['StudyAreas', 'Deployments', 'LookupOption'];

    function DeploymentsController(StudyAreas, Deployments, LookupOption) {
        var vm = this;
        vm.inserted = {};
        vm.selectedDeployment = null;
        vm.deployments = [];
        vm.studyAreas = [];       
        vm.timeOfDayOptions = [];
        vm.startDatePopup = { opened: false };
        vm.endDatePopup = { opened: false }; 
        vm.dateOptions = {
//          dateDisabled: disabled,
		    formatYear: 'yy',
//    		maxDate: new Date(2020, 5, 22),
//    		minDate: new Date(),
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
        	console.debug("TODO: Save deployment", vm.selectedDeployment);
        };
        
        function add() {
        	Deployments.get({ id: 0 }, function(data) {
        		vm.inserted = data;
        		vm.inserted.startDate = new Date();
        		vm.inserted.endDate = new Date();
                vm.deployments.push(vm.inserted);
                vm.selectedDeployment = vm.inserted;
        	});
        }
    }
}());