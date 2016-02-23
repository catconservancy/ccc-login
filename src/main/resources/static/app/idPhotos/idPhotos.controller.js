(function () {
    angular.module('CCC')
        .controller('IdPhotosController', IdPhotosController);

    IdPhotosController.$inject = ['$scope', 'PhotosService', 'Species'];

    function IdPhotosController($scope, PhotosService, Species) {
        var vm = this;
        vm.photos = [];
        vm.selectedPhoto = {};
        vm.speciesList = []

        vm.selectThumb = selectThumb;
        vm.thumbClass = thumbClass;

        PhotosService.getMockedPhotos({},function(data) {
            vm.photos = data;
            vm.photos[0].selected = true;
            vm.selectedPhoto = vm.photos[0];
            $scope.$on('$viewContentLoaded', initializeCarousel);
        });

        Species.query(function(data) {
            vm.speciesList = data;
        });

        function selectThumb(index) {
            //setSelectedPhotoByIndex(index);
            $('#myCarousel').carousel(index);
        }
        
        function thumbClass(photo) {
        	return photo.selected ? 'active' : '';
        }

        function initializeCarousel() {

            $('#myCarousel').carousel();

            // when the carousel slides, auto update
            $('#myCarousel').on('slid.bs.carousel', function (e) {
                var id = $('.item.active').data('slide-number');
                id = parseInt(id);
                setSelectedPhotoByIndex(id);
            });

            $(document).bind('keyup', function (e) {
                if (e.which == 39) {
                	$('#myCarousel').carousel('next');
                }
                else if (e.which == 37) {
                	$('#myCarousel').carousel('prev');
                }
            });
        }

        function setSelectedPhotoByIndex(index) {
        	$scope.$apply(function() { 
	            for (i = 0; i < vm.photos.length; i++) {
	                if (i === index) {
	                    vm.selectedPhoto = vm.photos[i];
	                    vm.photos[i].selected = true;
	                } else {
	                    vm.photos[i].selected = false;
	                }
	            }
        	});
        }

    }
}());