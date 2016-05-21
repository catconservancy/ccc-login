(function () {
    angular.module('CCC')
        .factory('LookupOption', LookupOption);

    LookupOption.$inject = ['$resource'];

    function LookupOption($resource) {
        return $resource('/ccc/api/lookupOption/:subquery_type/:listCode', { subquery_type:'@subquery_type', listCode: '@_listCode' }, {
            update: {
                method: 'PUT'
            },
            
            findByListCode: {
            	method: 'GET',
            	cache: false,
            	isArray: true,
            	params: {
            		subquery_type: 'options',
            		listCode: '@listCode'
            	}
            }
        });
    }
}());