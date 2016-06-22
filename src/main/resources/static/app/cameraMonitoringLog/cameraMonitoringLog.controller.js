(function () {
    angular.module('CCC')
        .controller('CameraMonitoringLogController', CameraMonitoringLogController);

    CameraMonitoringLogController.$inject = ['CameraMonitorLogs', 'Deployments', 'LookupOption', 'Species'];

    function CameraMonitoringLogController(CameraMonitorLogs, Deployments, LookupOption, Species) {
        var vm = this;
        vm.inserted = {};
        vm.selectedDeployment = null;
        vm.selectedLogEntry = null;
        vm.deployments = [];
        vm.logEntries = [];
        vm.species = [];
        vm.checkDatePopup = { opened: false }; 
        vm.dateOptions = {
		    formatYear: 'yy',
		    startingDay: 1
        };
        vm.lookups = [
                      'cameraDelaySetting',
                      'lureInfo',
                      'wildlifeSign'
        ];
        vm.lookupLists = {};

        vm.onSelectDeploymentCallback = onSelectDeploymentCallback;
        vm.onSelectLogEntryCallback = onSelectLogEntryCallback;
        vm.onSelectLookupOptionCallback = onSelectLookupOptionCallback;
        vm.checkDateOpen = checkDateOpen;
        vm.checkTimeChanged = checkTimeChanged;
        vm.add = add;
        
        Deployments.query(function(data) {
        	vm.deployments = data;
        });

        Species.query(function(data) {
            vm.species = data;
        });

        for (var i = 0; i < vm.lookups.length; i++) {
            LookupOption.findByListCode({listCode: vm.lookups[i]}, function(options) {
            	vm.lookupLists[options[0].listCode] = options;
            });
        }
        
        function onSelectDeploymentCallback(item) {
        	vm.selectedDeployment = item;
            CameraMonitorLogs.findByDeploymentId({id: item.id}, function(data) {
            	vm.logEntries = data;
            });
            vm.successText = null;
        }
        
        function onSelectLogEntryCallback(item) {
        	vm.selectedLogEntry = item;
            vm.successText = null;
        }
        
        function onSelectLookupOptionCallback(item) {
        	vm.selectedLogEntry[item.listCode] = item.label;
        }

        function checkDateOpen() {
        	vm.checkDatePopup.opened = true;
        }
        
        function checkTimeChanged() {
        	console.log('check time changed to ', vm.selectedLogEntry.checkDate);
        }

        function save() {
            if (!vm.selectedLogEntry.id || vm.selectedLogEntry.id === 0) {
                return CameraMonitorLogs.save(vm.selectedLogEntry, function(logEntry) {
                	vm.successText = 'Camera Monitor Log entry created successfully.';
                    $log.debug('save success');
                    vm.selectedLogEntry = logEntry;
                    CameraMonitorLogs.query(function(data) {
                        vm.logEntries = data;
                    });
                });
            } else {
                vm.entry = CameraMonitorLogs.get({ id: vm.selectedLogEntry.id }, function() {
                    vm.entry.cameraDelaySetting = vm.selectedLogEntry.cameraDelaySetting;
                    vm.entry.cameraDateTimeCorrect = vm.selectedLogEntry.cameraDateTimeCorrect;
                    vm.entry.checkDate = vm.selectedLogEntry.checkDate;
                    vm.entry.checkTime = vm.selectedLogEntry.checkTime;
                    vm.entry.comments = vm.selectedLogEntry.comments;
                    vm.entry.lureInfo = vm.selectedLogEntry.lureInfo;
                    vm.entry.newBatteries = vm.selectedLogEntry.newBatteries;
                    vm.entry.newCameraId = vm.selectedLogEntry.newCameraId;
                    vm.entry.newCard = vm.selectedLogEntry.newCard;
                    vm.entry.numPics = vm.selectedLogEntry.numPics;
                    vm.entry.reasercherNames = vm.selectedLogEntry.reasercherNames;
                    vm.entry.weatherTempF = vm.selectedLogEntry.weatherTempF;
                    vm.entry.wildlifeSeen = vm.selectedLogEntry.wildlifeSeen;
                    vm.entry.wildlifeSign = vm.selectedLogEntry.wildlifeSign;
                    vm.entry.wildlifeSignSpecies = vm.selectedLogEntry.wildlifeSignSpecies;
                    vm.entry.species = vm.selectedLogEntry.species;
                    vm.entry.deployment = vm.selectedLogEntry.deployment;
                    vm.entry.studyArea = vm.selectedLogEntry.studyArea;
                    
                    vm.entry.$update(function(logEntry) {
                        $log.debug('update success');
                    	vm.successText = 'Camera Monitor Log entry updated successfully.';
                        vm.selectedLogEntry = logEntry;
                        CameraMonitorLogs.query(function(data) {
                            vm.logEntries = data;
                        });
                    });
                });
            }
        };
        
        function add() {
        	CameraMonitorLogs.get({ id: 0 }, function(data) {
                vm.successText = null;
        		vm.inserted = data;
        		vm.inserted.checkDate = new Date();
                vm.logEntries.push(vm.inserted);
                vm.selectedLogEntry = vm.inserted;
        	});
        }
    }
}());