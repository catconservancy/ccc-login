(function () {
    angular.module('CCC')
        .factory('PhotosService', PhotosService);

    PhotosService.$inject = ['$resource'/* $$_DEP_$$ */];

    function PhotosService($resource/* $$_DEP2_$$ */) {

        var fac = {};

        fac.getMockedPhotos = function(params, successCallback) {

            var photos =  [
                {
                    id: 1,
                    thumbSrc: 'http://localhost:8080/ccc/img/one-thumb.png',
                    src: 'http://localhost:8080/ccc/img/one.png',
                    species: []
                },
                {
                    id: 2,
                    thumbSrc: 'http://localhost:8080/ccc/img/two-thumb.png',
                    src: 'http://localhost:8080/ccc/img/two.png',
                    species: []
                },
                {
                    id: 3,
                    thumbSrc: 'http://localhost:8080/ccc/img/three-thumb.png',
                    src: 'http://localhost:8080/ccc/img/three.png',
                    species: []
                }
            ];
            successCallback && successCallback(photos);
        };

        return fac;
    }
}());
