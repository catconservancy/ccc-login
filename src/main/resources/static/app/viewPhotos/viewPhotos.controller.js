(function () {
    angular.module('CCC')
        .controller('ViewPhotosController', ViewPhotosController);

    ViewPhotosController.$inject = ['$scope','$timeout','$stateParams',
        'PhotosService','Deployments','StudyAreas','Species','SpinnerService'];

    function ViewPhotosController($scope, $timeout, $stateParams, 
                                  PhotosService, Deployments, StudyAreas, Species, SpinnerService) {
        var vm = this;
        vm.selectedPhoto = {};
        vm.selectedFolder = {};
        vm.photos = [];
        vm.fileList = [];
        vm.treeData = [];
        vm.studyAreas = [];
        vm.deployments = [];
        vm.currPage = 0;
        vm.photoQueryError = null;
        vm.fullscreen = false;
        vm.selectedHighlight = false;
        vm.selectedStudyArea = null;
        vm.selectedDeployment = null;
        vm.selectedSpecies = [];
        vm.selectedImageStartDate = new Date().setDate(new Date().getDate() - 30);
        vm.selectedImageEndDate = new Date();
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
        vm.resetPhotos = resetPhotos;

        Deployments.query(function(data) {
            SpinnerService.hide('viewPhotosSpinner');
            vm.deployments = data;
            if ($stateParams.deploymentId) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id === $stateParams.deploymentId) {
                        vm.selectedDeployment = data[i];
                        updateResults();
                        break;
                    }
                }
            } else if ($stateParams.cannedQuery) {
                vm.cannedQuery = $stateParams.cannedQuery;
                updateResults();
            }
        });

        StudyAreas.query(function(data) {
            vm.studyAreas = data;
        });

        Species.query(function(data) {
            vm.speciesList = data;
        });

        $scope.$on('getMore', function () {
            $timeout(function () {
                updateResults(true);
            });
        });

        function updateResults(nextPage) {
            SpinnerService.show('viewPhotosSpinner');

            if (!nextPage) {
                vm.currPage = 0;
            }

            var queryParams = {'isArchived':true, 'page': nextPage ? vm.currPage + 1 : vm.currPage};
            queryParams.studyAreaId = vm.selectedStudyArea ? vm.selectedStudyArea.id : null;
            queryParams.locationId = vm.selectedDeployment ? vm.selectedDeployment.id : null;
            queryParams.highlighted = vm.selectedHighlight ? true : null;
            queryParams.startDate = vm.selectedImageStartDate;
            queryParams.endDate = vm.selectedImageEndDate;
            queryParams.speciesIds = vm.selectedSpecies ? toIdList(vm.selectedSpecies) : null;
            queryParams.cannedQuery = vm.cannedQuery ? vm.cannedQuery : null;

            PhotosService.query(queryParams,function(data) {
                if (nextPage) {
                    vm.currPage++;
                    vm.photos = vm.photos.concat(data);
                } else {
                    vm.photos = data;
                }
                SpinnerService.hide('viewPhotosSpinner');
                if (!nextPage) {
                    setSelectedPhotoByIndex(0);
                }
                initializeCarousel();
            });

        }

        function startDateOpen() {
            vm.startDatePopup.opened = true;
        }
        function endDateOpen() {
            vm.endDatePopup.opened = true;
        }
        function selectThumb(index) {
            $timeout(function () {
                $('#myCarousel').carousel(index);
            });
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
            $timeout(function () {
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
            });
        }

        function toIdList(list) {
            var ids = [];
            for(var i = 0; i < list.length; i++) {
                ids.push(list[i].id);
            }
            return ids.join(",");
        }

        function resetPhotos() {
            PhotosService.resetPhotos(function () {
                updateResults();
            });
        }
    }
}());