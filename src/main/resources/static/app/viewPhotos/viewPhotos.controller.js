(function () {
    angular.module('CCC')
        .controller('ViewPhotosController', ViewPhotosController);

    ViewPhotosController.$inject = ['$scope','PhotosService','Deployments','StudyAreas','Species'];

    function ViewPhotosController($scope, PhotosService, Deployments, StudyAreas, Species) {
        var vm = this;
        vm.selectedPhoto = {};
        vm.selectedFolder = {};
        vm.breadCrumbList = [];
        vm.photos = [];
        vm.fileList = [];
        vm.treeData = [];
        vm.studyAreas = [];
        vm.deployments = [];
        vm.photoQueryError = null;
        vm.fullscreen = false;
        vm.selectedHighlight = false;
        vm.selectedStudyArea = null;
        vm.selectedDeployment = null;
        vm.selectedSpecies = null;
        vm.selectedImageStartDate = null;
        vm.selectedImageEndDate = null;
        vm.treeDataLoaded = {};
        vm.startDatePopup = { opened: false };
        vm.endDatePopup = { opened: false };
        vm.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        vm.selectThumb = selectThumb;
        vm.thumbClass = thumbClass;
        vm.updateResults = updateResults;
        vm.startDateOpen = startDateOpen;
        vm.endDateOpen = endDateOpen;

        Deployments.query(function(data) {
            vm.deployments = data;
        });

        StudyAreas.query(function(data) {
            vm.studyAreas = data;
        });

        Species.query(function(data) {
            vm.speciesList = data;
        });

        function updateResults() {

            var queryParams = {'isArchived':true};
            queryParams.studyAreaId = vm.selectedStudyArea ? vm.selectedStudyArea.id : null;
            queryParams.locationId = vm.selectedDeployment ? vm.selectedDeployment.id : null;
            queryParams.highlighted = vm.selectedHighlight ? true : null;
            queryParams.startDate = vm.selectedImageStartDate;
            queryParams.endDate = vm.selectedImageEndDate;
            queryParams.speciesIds = vm.selectedSpecies ? toIdList(selectedSpecies) : null;

            PhotosService.query(queryParams,function(data) {
                vm.photos = data;
                setSelectedPhotoByIndex(0);
                initializeCarousel();
            });

        }

        function startDateOpen() {
            vm.startDatePopup.opened = true;
        };

        function endDateOpen() {
            vm.endDatePopup.opened = true;
        };

        function selectThumb(index) {
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
            // $scope.$apply(function() {
                for (i = 0; i < vm.photos.length; i++) {
                    if (i === index) {
                        vm.selectedPhoto = vm.photos[i];
                        vm.photos[i].selected = true;
                        if (!vm.selectedPhoto.detections)
                            vm.selectedPhoto.detections = [{}];
                    } else {
                        vm.photos[i].selected = false;
                    }
                }
            // });
        }

        function toIdList(list) {
            var ids = [];
            for(var i = 0; i < list.size; i++) {
                ids.push(list[i].id);
            }
            return ids;
        }
    }
}());