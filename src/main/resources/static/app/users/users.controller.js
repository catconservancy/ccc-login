(function () {
    angular.module('CCC')
        .controller('UsersController', UsersController);

    UsersController.$inject = ['$log','Users', 'DTOptionsBuilder'];

    function UsersController($log, Users, DTOptionsBuilder) {
        var vm = this;
        vm.users = null;
        
        // DataTables configurable options
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withDisplayLength(5)
            .withOption('bLengthChange', false);
        
        Users.query(function(data) {
			vm.users = data;
			$log.debug("vm.users",vm.users);
		});
    }
}());