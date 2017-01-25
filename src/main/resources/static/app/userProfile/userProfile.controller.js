(function () {
    angular.module('CCC')
        .controller('UserProfileController', UserProfileController);

    UserProfileController.$inject = ['Users'];

    function UserProfileController(Users) {
        var vm = this;
        vm.user = null;

        vm.save = save;

        function save() {
            Users.update(vm.user,function(){
                $log.debug("saved user", vm.user);
                Users.get({ id: 0 }, function(data) {
                    vm.user = data;
                });
            });
        }

        Users.get({ id: 0 }, function(data) {
            vm.user = data;
        });

    }
}());