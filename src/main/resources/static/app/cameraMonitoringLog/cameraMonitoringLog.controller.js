(function () {
    angular.module('CCC')
        .controller('CameraMonitoringLogController', CameraMonitoringLogController);

    CameraMonitoringLogController.$inject = ['$log', 'CameraMonitorLogs', 'Deployments', 'LookupOption', 'Species'];

    function CameraMonitoringLogController($log, CameraMonitorLogs, Deployments, LookupOption, Species) {
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
        vm.onSelectSpeciesCallback = onSelectSpeciesCallback;
        vm.checkDateOpen = checkDateOpen;
        vm.checkTimeChanged = checkTimeChanged;
        vm.save = save;
        vm.add = add;
        vm.remove = remove;
        
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
        	vm.selectedLogEntry = null;
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
        
        function onSelectSpeciesCallback(item) {
        	console.debug("onSelectSpeciesCallback",item);
        }

        function checkDateOpen() {
        	vm.checkDatePopup.opened = true;
        }
        
        function checkTimeChanged() {
        	console.log('check time changed to ', vm.selectedLogEntry.checkDate);
        }

        function save() {
        	console.debug('save() called', vm.selectedLogEntry);
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
                	vm.entry.cameraDateTimeCorrect = vm.selectedLogEntry.cameraDateTimeCorrect;
                    vm.entry.cameraDelaySetting = vm.selectedLogEntry.cameraDelaySetting;
                    vm.entry.cameraId = vm.selectedLogEntry.cameraId;
                    vm.entry.checkDate = vm.selectedLogEntry.checkDate;
                    vm.entry.comments = vm.selectedLogEntry.comments;
                    vm.entry.lureInfo = vm.selectedLogEntry.lureInfo;
                    vm.entry.newBatteries = vm.selectedLogEntry.newBatteries;
                    vm.entry.newCameraId = vm.selectedLogEntry.newCameraId;
                    vm.entry.newCard = vm.selectedLogEntry.newCard;
                    vm.entry.numPics = vm.selectedLogEntry.numPics;
                    vm.entry.numPicsPerBurst = vm.selectedLogEntry.numPicsPerBurst;
                    vm.entry.numVideos = vm.selectedLogEntry.numVideos;
                    vm.entry.reasercherNames = vm.selectedLogEntry.reasercherNames;
                    vm.entry.setLetter = vm.selectedLogEntry.setLetter;
                    vm.entry.species = vm.selectedLogEntry.species;
                    vm.entry.videoLength = vm.selectedLogEntry.videoLength;
                    vm.entry.weatherTempF = vm.selectedLogEntry.weatherTempF;
                    vm.entry.wildlifeSign = vm.selectedLogEntry.wildlifeSign;
                    vm.entry.deployment = vm.selectedLogEntry.deployment;
                    
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
        		vm.inserted.deployment = vm.selectedDeployment;
        		vm.inserted.checkDate = new Date();
                vm.logEntries.push(vm.inserted);
                vm.selectedLogEntry = vm.inserted;
        	});
        }
        
        function remove() {
        	if (vm.selectedLogEntry.id === 0) {
        		for( i= vm.logEntries.length-1; i>=0; i--) {
        		    if( vm.logEntries[i].id == "0") 
        		    	vm.logEntries.splice(i,1);
        		}
        	} else {
        		CameraMonitorLogs.delete({ id: vm.selectedLogEntry.id }, function() {
	        		$log.debug('Deleted from server');
	                CameraMonitorLogs.findByDeploymentId({id: vm.selectedDeployment.id}, function(data) {
	                	vm.logEntries = data;
	                	vm.selectedLogEntry = null;
	                });
	        	});
        	}        	
        }
    }
}());