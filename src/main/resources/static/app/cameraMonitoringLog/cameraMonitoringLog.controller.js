(function () {
    angular.module('CCC')
        .controller('CameraMonitoringLogController', CameraMonitoringLogController);

    CameraMonitoringLogController.$inject = ['CameraMonitorLogs', 'Deployments', 'LookupOption'];

    function CameraMonitoringLogController(CameraMonitorLogs, Deployments, LookupOption) {
        var vm = this;
        vm.inserted = {};
        vm.selectedDeployment = null;
        vm.selectedLogEntry = null;
        vm.deployments = [];
        vm.logEntries = [];
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
        vm.add = add;
        
        Deployments.query(function(data) {
        	vm.deployments = data;
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