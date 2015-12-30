(function () {
    angular.module('CCC')
        .controller('AlertsController', AlertsController);

    AlertsController.$inject = ['$log', 'AlertsService'];

    function AlertsController($log, AlertsService) {
        var vm = this;
        vm.selectedAlert = null;
        vm.alerts = [];
        vm.displayDate = displayDate;
        vm.setSelectedAlert = setSelectedAlert;
        vm.editAlert = editAlert;
        vm.deleteAlert = deleteAlert;

        init();

        function init() {
            vm.alerts = AlertsService.query(function () {
                if (vm.alerts.length > 0) {
                    setSelectedAlert(0);
                }
                vm.alerts.forEach(function (alert) {
                    $log.debug('Alert', alert);
                });
            });
        }

        function displayDate(date) {
            return moment(date).fromNow();
        }

        function setSelectedAlert(index) {
            vm.selectedAlert = vm.alerts[index];
        }

        function editAlert(index) {
            $log.debug('index', index);
            $log.debug('Edit', edit);
        }

        function deleteAlert() {
            AlertsService.remove({id: vm.selectedAlert.id}, function () {
                $log.debug('Alert deleted');
                for (var i = 0; i < vm.alerts.length; ++i) {
                    if (vm.alerts[i].id === vm.selectedAlert.id) {
                        vm.alerts.splice(i, 1);
                        if (vm.alerts.length > 0) {
                            vm.selectedAlert = i > 0 ? vm.alerts[i - 1] : vm.alerts[0];
                        } else {
                            vm.selectedAlert = null;
                        }
                        break;
                    }
                }
            });
        }
    }
}());