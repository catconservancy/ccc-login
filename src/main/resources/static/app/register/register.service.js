(function () {
    angular.module('CCC')
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