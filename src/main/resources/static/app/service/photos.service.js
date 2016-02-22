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
                    thumbSrc: 'http://placehold.it/80x60&amp;text=one',
                    src: 'http://placehold.it/1200x480&amp;text=one',
                    species: []
                },
                {
                    id: 2,
                    thumbSrc: 'http://placehold.it/80x60&amp;text=two',
                    src: 'http://placehold.it/1200x480&amp;text=two',
                    species: []
                },
                {
                    id: 3,
                    thumbSrc: 'http://placehold.it/80x60&amp;text=three',
                    src: 'http://placehold.it/1200x480&amp;text=three',
                    species: []
                }
            ];
            successCallback && successCallback(photos);
        };

        return fac;
    }
}());
