window.angularModuleName = 'CCC';

(function () {
    angular.module(window.angularModuleName, [
        'ngResource',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'LocalForageModule',
        /*'angular-web-notification',*/
        'ngStomp',
        'ui.select',
        /*'chart.js',*/
        'ngToast'
    ])
        .config(config)
        .run(run);

    config.$inject = ['$compileProvider', 'ngToastProvider'];

    function config($compileProvider, ngToastProvider) {

        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);

        ngToastProvider.configure({
            horizontalPosition: 'right',
            verticalPosition: 'bottom',
            dismissOnClick: true
        });
    }

    run.$inject = ['$rootScope', '$log', '$http', '$state', 'AuthService'];

    function run($rootScope, $log, $http, $state, AuthService) {
        var bypass = false,
            checkInstall = true;
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
            if (bypass) {
                bypass = false;
                return;
            }
            event.preventDefault();
            bypass = true;

            if (checkInstall) {
                return $http.get('/auth/check-install').then(function () {
                    checkInstall = false;
                    doNavigation(toState, toParams);
                }, function () {
                    AuthService.logout(function () {
                        return $state.go('setup');
                    });
                });
            }

            doNavigation(toState, toParams);

        });

        function doNavigation(toState, toParams) {
            if (toState.name === 'setup') {
                return $state.go('login');
            }

            AuthService.checkAuthentication(function (isAuthenticated) {
                if (!isAuthenticated && (!toState.data || !toState.data.insecure)) {
                    return $state.go('login');
                }

                if (isAuthenticated && toState.name === 'login') {
                    return $state.go('shell.dashboard');
                }

                if (toParams)
                    return $state.go(toState.name, toParams);
                return $state.go(toState.name);
            });
        }
    }
}());
(function () {
    angular.module(window.angularModuleName)
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
(function () {
    angular.module(window.angularModuleName)
        .factory('AlertsService', AlertsService);

    AlertsService.$inject = ['$resource'];

    function AlertsService($resource) {
        return $resource('/api/alerts/:id', {}, {
            update: {method: 'PATCH', isArray: false}
        });
    }
}());

(function () {
    angular.module(window.angularModuleName)
        .config(config);

    config.$inject = [
        '$locationProvider', '$httpProvider', '$stateProvider', '$urlRouterProvider'
    ];

    function config($locationProvider, $httpProvider, $stateProvider, $urlRouterProvider) {

        $locationProvider.html5Mode(true);

        $urlRouterProvider.otherwise('/');

        $stateProvider
            .state('login', {
                url: '/login',
                templateUrl: '/app/login/login.html',
                controller: 'LoginController',
                controllerAs: 'login',
                data: {
                    insecure: true
                }
            })
            .state('register', {
                url: '/register',
                templateUrl: '/app/register/register.html',
                controller: 'RegisterController',
                controllerAs: 'register',
                data: {
                    insecure: true
                }

            })
            .state('shell', {
                templateUrl: '/app/layout/shell.html',
                controller: 'ShellController',
                controllerAs: 'shell'
            })
            .state('shell.dashboard', {
                url: '/',
                templateUrl: '/app/dashboard/dashboard.html',
                controller: 'DashboardController',
                controllerAs: 'dc',
                data: {
                    title: 'Dashboard'
                }
            })
            .state('shell.alerts', {
                url: '/alerts',
                templateUrl: '/app/alerts/alerts.html',
                controller: 'AlertsController',
                controllerAs: 'ac',
                data: {
                    title: 'Alerts'
                }
            });;

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
    }
}());
(function () {
    angular.module(window.angularModuleName)
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = [];

    function DashboardController() {
        var vm = this;
    }
}());
(function () {
    angular.module(window.angularModuleName)
        .controller('GlobalController', GlobalController);

    GlobalController.$inject = ['$scope'];

    function GlobalController($scope) {
        var vm = this;
        vm.bodyClass = 'login-page';

        $scope.$on('$stateChangeSuccess', function (event, toState) {
            switch (toState.name) {
                case 'setup':
                case 'login':
                    vm.bodyClass = 'login-page';
                    break;
                case 'register':
                    vm.bodyClass = 'register-page';
                    break;
                case 'lock':
                    vm.bodyClass = 'lockscreen';
                    break;
                default:
                    vm.bodyClass = 'skin-acct skin-green sidebar-mini';
                    break;
            }
        });
    }
}());
(function () {
    angular.module(window.angularModuleName)
        .controller('ShellController', ShellController);

    ShellController.$inject = ['$state', 'AuthService'];

    function ShellController($state, AuthService) {

        var vm = this;
        vm.user = AuthService.user;
        vm.logout = logout;
        vm.title = $state.current.data.title;

        function logout() {
            AuthService.logout(function () {
                $state.go('login');
            });
        }
    }
}());
(function () {
    angular.module(window.angularModuleName)
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$state', 'AuthService'];

    function LoginController($state, AuthService) {
        var vm = this;
        vm.invalidLogin = false;
        vm.invalidTwoFactor = false;
        vm.credentials = {
            email: null,
            password: null,
            validationCode: null
        };
        vm.requireTwoFactor = false;
        vm.submit = submit;

        function submit() {
            AuthService.login(vm.credentials, function (err) {
                if (err) {
                    if (err.message === '2FA') {
                        vm.invalidLogin = false;
                        vm.requireTwoFactor = true;
                    } else {
                        vm.invalidLogin = true;
                    }
                    return;
                }

                $state.go('shell.dashboard');
            });
        }
    }
}());
(function () {
    angular.module(window.angularModuleName)
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['$state', 'RegisterService'];

    function RegisterController($state, RegisterService) {

        var vm = this;
        vm.user = RegisterService.userModel;
        vm.repeatPassword = null;
        vm.termsAccepted = false;
        vm.submit = submit;

        function submit() {
            RegisterService.register(vm.user, function (err) {
                if (err) {
                    return;
                }

                $state.go('login');
            });
        }
    }
}());
(function () {
    angular.module(window.angularModuleName)
        .factory('RegisterService', RegisterService);

    RegisterService.$inject = ['$http', '$log'];

    function RegisterService($http, $log) {
        return {
            userModel: {
                email: null,
                name: null,
                password: null
            },
            register: function (user, callback) {
                $http.post('/auth/register', user).then(function (response) {
                    $log.debug('Success', response);
                    // TODO set a message
                    callback && callback();
                }, function (response) {
                    $log.debug('Failure', response);
                    callback && callback(response.data);
                });
            }
        };
    }

}());
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
(function () {
    angular.module(window.angularModuleName)
        .factory('NotificationService', NotificationService);

    NotificationService.$inject = ['$log'/*, 'webNotification'*/];

    function NotificationService($log/*, webNotification*/) {
        return {
            notify: function (notification) {
                $log.debug('Notification shown: ', notification);
                /*webNotification.showNotification(notification.title || 'CCC', {
                    body: notification.body,
                    icon: '/img/acct-logo-32.png'
                }, function onShow(error, hide) {
                    if (error) {
                        return $log.debug('Unable to show notification', error);
                    }
                    $log.debug('Notification shown');
                });*/
            }
        };
    }
}());
