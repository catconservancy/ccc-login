(function () {
    angular.module('CCC')
        .factory('Detection', Detection);

    Detection.$inject = ['$resource'];

    function Detection($resource) {
        return $resource('/ccc/api/detection/:id', { id: '@_id' }, {
            update: {
                method: 'PUT'
            }
        });
    }
}());