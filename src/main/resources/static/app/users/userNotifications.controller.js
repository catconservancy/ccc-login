(function () {
    angular.module('CCC')
        .controller('UserNotificationsController', UserNotificationsController);

    UserNotificationsController.$inject = ['$log','Users'];

    function UserNotificationsController($log, Users) {
        var vm = this;
        vm.users = null;

        vm.saveUser = saveUser;
        
        Users.query({enabled: false}, function(data) {
			vm.users = data;
			$log.debug("vm.users",vm.users);
		});

        function saveUser(user) {
            Users.update(user,function(){
                $log.debug("saved user", user);
                Users.query({enabled: false}, function(data) {
                    vm.users = data;
                });
            });
        }
    }
}());