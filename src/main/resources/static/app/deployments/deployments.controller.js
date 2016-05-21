(function () {
    angular.module('CCC')
        .controller('DeploymentsController', DeploymentsController);

    DeploymentsController.$inject = [];

    function DeploymentsController() {
        var vm = this;
        vm.selectedDeployments = {};
        vm.deployments = [];
        
        vm.onSelectDeploymentCallback = onSelectDeploymentCallback;
        
        DeploymentService.get({}, function(data) {
        	vm.deployments = data
        });
        
        
        function onSelectDeploymentCallback(item) {
        	vm.selectedDeployment = item;
        }
    }
}());