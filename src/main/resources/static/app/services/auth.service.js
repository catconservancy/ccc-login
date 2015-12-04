(function () {
    angular.module(window.angularModuleName)
        .service('AuthService', AuthService);

    AuthService.$inject = ['$rootScope', '$http', '$log', '$localForage', '$stomp', 'NotificationService'];

    function AuthService($rootScope, $http, $log, $localForage, $stomp, NotificationService) {
        var vm = this;

        vm.user = null;
        vm.authenticated = false;
        vm.login = login;
        vm.logout = logout;
        vm.lock = lock;
        vm.checkAuthentication = checkAuthentication;

        function checkAuthentication(callback) {

            if (!vm.authenticated) {
                $localForage.getItem('Auth').then(function (Auth) {
                    if (!Auth || !Auth.token || !Auth.user) {
                        return (callback && callback(false));
                    }

                    $http.defaults.headers.common.Authorization = 'Bearer ' + Auth.token;
                    vm.user = Auth.user;
                    vm.authenticated = true;
                    initStomp();
                    callback && callback(true);
                });
            } else {
                callback && callback(true);
            }
        }

        function login(credentials, callback) {
            $http.post('/auth/login', credentials).then(function (response) {
                $log.debug('Success', response);

                $localForage.setItem('Auth', response.data).then(function () {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    vm.authenticated = true;
                    vm.user = response.data.user;
                    $rootScope.$broadcast('authEvent');
                    initStomp();
                    callback && callback();
                });
            }, function (response) {
                $log.debug('Failure', response);
                $http.defaults.headers.common.Authorization = '';

                vm.authenticated = false;
                vm.user = null;
                $rootScope.$broadcast('authEvent');
                callback && callback(response.data);
            });
        }

        function logout(callback) {
            $localForage.removeItem('Auth').then(function () {
                $http.defaults.headers.common.Authorization = '';
                vm.user = null;
                vm.authenticated = false;

                callback && callback();
            });
        }

        function lock(callback) {
            $localForage.removeItem('Auth').then(function () {
                $http.defaults.headers.common.Authorization = '';
                vm.authenticated = false;

                callback && callback();
            });
        }

        var wsSubscription;

        function initStomp() {
            $stomp
                .connect('/ws')
                .then(function () {

                    $log.debug('Subscribing to /topic/notifications-' + vm.user.id);

                    wsSubscription = $stomp.subscribe('/topic/notifications-' + vm.user.id, function (notification) {
                        $log.debug('Notification received', notification);
                        NotificationService.notify(notification);
                    });
                });
        }
    }
}());