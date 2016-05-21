(function () {
    angular.module('CCC')
        .controller('StudyAreasController', StudyAreasController);

    StudyAreasController.$inject = ['$rootScope', '$log', '$filter', 'StudyAreas', 'DTOptionsBuilder'];

    function StudyAreasController($rootScope, $log, $filter, StudyAreas, DTOptionsBuilder) {
        var vm = this;
        vm.add = add;
        vm.checkDetail = checkDetail;
        vm.saveStudyArea = saveStudyArea;
        vm.removeStudyArea = removeStudyArea;
        vm.inserted = {};
        vm.studyAreas = [];

        // DataTables configurable options
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withDisplayLength(2)
            .withOption('bLengthChange', false);

        StudyAreas.query(function(data) {
            vm.studyAreas = data;
        });

        function add() {
            vm.inserted = {id: 0};
            vm.studyAreas.push(vm.inserted);
        }

        function checkDetail(data, id) {
            //TODO: validation impl
            $log.debug('data', data);
            $log.debug('id', id);
        }

        function saveStudyArea(data, id) {
            if (id === 0) {
                angular.extend(data, {id: id});
                return StudyAreas.save(data, function(studyArea) {
                    $log.debug('save success');
                    StudyAreas.query(function(data) {
                        vm.studyAreas = data;
                    });
                });
            } else {
                vm.entry = StudyAreas.get({ id: id }, function() {
                    vm.entry.name = data.name;
                    vm.entry.$update(function(studyArea) {
                        $log.debug('update success');
                        StudyAreas.query(function(data) {
                            vm.studyAreas = data;
                        });
                    });
                });
            }
        }

        function removeStudyArea(id) {
        	if (id === 0) {
        		for( i= vm.studyAreas.length-1; i>=0; i--) {
        		    if( vm.studyAreas[i].id == "0") 
        		    	vm.studyAreas.splice(i,1);
        		}
        	} else {
	            StudyAreas.delete({ id: id }, function() {
	                $log.debug('Deleted from server');
	                StudyAreas.query(function(data) {
	                    vm.studyAreas = data;
	                });
	            });
        	}
        }
    }
}());