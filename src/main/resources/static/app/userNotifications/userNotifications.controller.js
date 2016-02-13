(function () {
    angular.module('CCC')
        .controller('UserNotificationsController', UserNotificationsController);

    UserNotificationsController.$inject = ['$log','Users'];

    function UserNotificationsController($log, Users) {
        var vm = this;
        vm.add = add;
        vm.saveUserNotifications = saveUserNotifications;
        vm.removeUserNotifications = removeUserNotifications;
        vm.inserted = null;
        vm.users = null;
        
        Users.query({enabled: false}, function(data) {
			vm.users = data;
		});
        
        function add() {
        	vm.inserted = {id: 0};
        	vm.species.push(vm.inserted);
        }
        
        function checkCommonName(data, id) {
        	//TODO: validation impl
        	$log.debug('data', data);
        	$log.debug('id', id);
        }
        
        function checkLatinName(data, id) {
        	//TODO: validation impl
        	$log.debug('data', data);
        	$log.debug('id', id);
        }
        
        function checkShortcutKey(data, id) {
        	//TODO: validation impl
        	$log.debug('data', data);
        	$log.debug('id', id);
        }
        
        function saveUserNotifications(data, id) {
        	if (id === 0) {
                angular.extend(data, {id: id});
                return UserNotifications.save(data, function(species) {
                    $log.debug('save success');
                    UserNotifications.query(function(data) {
            			vm.species = data;
            		});
                });
        	} else {
        		vm.entry = UserNotifications.get({ id: id }, function() {
        			vm.entry.commonName = data.commonName;
        			vm.entry.latinName = data.latinName;
        			vm.entry.shortcutKey = data.shortcutKey;
        			vm.entry.$update(function(species) {
        				$log.debug('update success');
        				UserNotifications.query(function(data) {
        					vm.species = data;
        				});
        			});
        		});
        	}
        }
        
        function removeUserNotifications(id) {
        	UserNotifications.delete({ id: id }, function() {
        		$log.debug('Deleted from server');
        		UserNotifications.query(function(data) {
        			vm.species = data;
        		});
        	});
        }
    }
}());