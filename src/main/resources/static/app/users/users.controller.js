(function () {
    angular.module('CCC')
        .controller('UsersController', UsersController);

    UsersController.$inject = ['$log','Users', 'DTOptionsBuilder'];

    function UsersController($log, Users, DTOptionsBuilder) {
        var vm = this;
        vm.users = null;

        vm.saveUser = saveUser;
        vm.removeUser = removeUser;
        
        // DataTables configurable options
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withDisplayLength(4)
            .withOption('bLengthChange', false);
        
        Users.query(function(data) {
			vm.users = data;
			$log.debug("vm.users",vm.users);
		});

        function saveUser(user) {
            Users.update(user,function(){
                $log.debug("saved user", user);
                Users.query(function(data) {
                    vm.users = data;
                })
            });
        }

        function removeUser(id) {
            Users.delete({ id: id }, function() {
                $log.debug('User deleted from server');
                Users.query(function(data) {
                    vm.users = data;
                });
            });
        }
    }
}());