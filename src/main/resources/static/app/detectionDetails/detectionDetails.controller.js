(function () {
    angular.module('CCC')
        .controller('DetectionDetailsController', DetectionDetailsController);

    DetectionDetailsController.$inject = ['$rootScope', '$log', '$filter', 'Species', 'DetectionDetails', 'DTOptionsBuilder'];

    function DetectionDetailsController($rootScope, $log, $filter, Species, DetectionDetails, DTOptionsBuilder) {
        var vm = this;
        vm.add = add;
        vm.checkSpecies = checkSpecies;
        vm.checkDetail = checkDetail;
        vm.showSpecies = showSpecies;
        vm.saveDetectionDetail = saveDetectionDetail;
        vm.removeDetectionDetail = removeDetectionDetail;
        vm.inserted = {};
        vm.detectionDetails = [];
        vm.species = [];

        // DataTables configurable options
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withDisplayLength(3)
            .withOption('bLengthChange', false);

        Species.query(function(data) {
            vm.species = data;
        });

        DetectionDetails.query(function(data) {
            vm.detectionDetails = data;
        });

        function add() {
            vm.inserted = {id: 0};
            vm.detectionDetails.push(vm.inserted);
        }

        function checkSpecies(detectionDetail, speciesId) {
            //TODO: validation impl
            $log.debug('detectionDetail', detectionDetail);
            $log.debug('speciesId', speciesId);
            detectionDetail.species = {id:speciesId};
        }

        function checkDetail(data, id) {
            //TODO: validation impl
            $log.debug('data', data);
            $log.debug('id', id);
        }

        function showSpecies(detectionDetail) {
            var selected = $filter('filter')(vm.species, {value: detectionDetail.species});
            return (detectionDetail.species && selected.length) ? selected[0].commonName : 'Not set';
        };

        function saveDetectionDetail(data, id) {
            var speciesId = data.species;
            data.species = $rootScope.getObjectById(vm.species, speciesId);
            if (id === 0) {
                angular.extend(data, {id: id});
                return DetectionDetails.save(data, function(detectionDetails) {
                    $log.debug('save success');
                    DetectionDetails.query(function(data) {
                        vm.detectionDetails = data;
                    });
                });
            } else {
                vm.entry = DetectionDetails.get({ id: id }, function() {
                    vm.entry.species = data.species;
                    vm.entry.detailText = data.detailText;
                    vm.entry.$update(function(detectionDetails) {
                        $log.debug('update success');
                        DetectionDetails.query(function(data) {
                            vm.detectionDetails = data;
                        });
                    });
                });
            }
        }

        function removeDetectionDetail(id) {
            DetectionDetails.delete({ id: id }, function() {
                $log.debug('Deleted from server');
                DetectionDetails.query(function(data) {
                    vm.detectionDetails = data;
                });
            });
        }
    }
}());