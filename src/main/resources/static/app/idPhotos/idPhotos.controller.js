(function () {
    angular.module('CCC')
        .controller('IdPhotosController', IdPhotosController);

    IdPhotosController.$inject = ['$q', '$log', '$rootScope', '$scope', '$filter', '$stateParams',
                                  'Species', 'DetectionDetails', 'Detection', 'PhotosService',
								  'StudyAreas', 'Deployments', 'SpinnerService'];

    function IdPhotosController($q, $log, $rootScope, $scope, $filter, $stateParams,
    		Species, DetectionDetails, Detection, PhotosService, StudyAreas, Deployments, SpinnerService) {
        var vm = this;
        vm.photos = [];
        vm.selectedPhoto = {};
        vm.selectedFolder = {};
        vm.breadCrumbList = [];
        vm.speciesList = [];
        vm.detectionDetailsList = [];
        vm.fileList = [];
        vm.treeData = [];
        vm.studyAreas = [];
        vm.deployments = [];
        vm.fullscreen = false;
        vm.showFilters = true;
        vm.treeDataLoaded = {};
        vm.photoQueryError = null;
        vm.selectedStudyArea = null;
        vm.selectedDeployment = null;

        vm.selectThumb = selectThumb;
        vm.thumbClass = thumbClass;
        vm.selectFolder = selectFolder;
        vm.splitFolder = splitFolder;
        vm.saveSelectedPhoto = saveSelectedPhoto;
        vm.removeDetection = removeDetection;
        vm.addDetection = addDetection;
        vm.highlightSelectedPhoto = highlightSelectedPhoto;
        vm.onSelectSpeciesCallback = onSelectSpeciesCallback;
        vm.deleteSelectedPhoto = deleteSelectedPhoto;
        vm.archiveTaggedPhotos = archiveTaggedPhotos;
        vm.saveSelectedStudyArea = saveSelectedStudyArea;
        vm.saveSelectedDeployment = saveSelectedDeployment;

        PhotosService.query($stateParams.dropboxPath ? {path:$stateParams.dropboxPath} : {},function(data) {
        	for (i = 0; i < data.length; i++) {
        		if (data[i].metadata.dir) {
        			vm.treeData.push({
        				text: data[i].metadata.name,
        				path: data[i].metadata.pathLower,
        				state: {expanded: false},
        				nodes: [{}]
        			});
        		} else {
        			vm.fileList.push(data[i].metadata);
        		}
        	}
			vm.treeDataLoaded = vm.treeData && vm.treeData.length > 0 ? {} : {notFound: true};
            SpinnerService.hide('folderSpinner');
            SpinnerService.hide('savingSpinner');
        	$log.debug("vm.fileList", vm.fileList);

        	$('#filterDropdown .dropdown-menu').on({
        		"click":function(e){
        	      e.stopPropagation();
        	    }
        	});
        	initializeCarousel();
        });

        Deployments.query(function(data) {
            vm.deployments = data;
        });

        StudyAreas.query(function(data) {
            vm.studyAreas = data;
        });

        Species.query(function(data) {
            vm.speciesList = data;
        });
        
        function deleteSelectedPhoto() {
        	PhotosService.delete({ path: vm.selectedPhoto.metadata.pathLower }, function() {
        		var photoIdx = vm.photos.indexOf(vm.selectedPhoto);
            	if (photoIdx > - 1) {
            		vm.photos.splice(photoIdx, 1);
            		vm.selectedPhoto = vm.photos[0];
            	}
        	});
        }
        
        function archiveTaggedPhotos() {
        	var indexesToRemove = [];
        	var loopPromises = [];
        	angular.forEach(vm.photos, function(photo, index) {
        	    var deferred = $q.defer();
        	    loopPromises.push(deferred.promise);
        		if (photo.detections && photo.detections.length && photo.detections[0].hasOwnProperty('id')) {
        			indexesToRemove.push(index);
		        	PhotosService.archive({id: photo.id, index: i}, function() {
		        		deferred.resolve();
		        	});
        		} else {
        			deferred.resolve();
        		}

        	});
        	$q.all(loopPromises).then(function () {
                for (var i = indexesToRemove.length - 1; i >= 0; i--) {
                    vm.photos.splice(indexesToRemove[i], 1);
                }
                vm.selectedPhoto = vm.photos.length ? vm.photos[0] : {};
        	});
        }

        function selectThumb(index) {
            $('#myCarousel').carousel(index);
        }
        
        function thumbClass(photo) {
        	return photo.selected ? 'active' : '';
        }
        
        function selectFolder(folder) {
            SpinnerService.show('folderSpinner');
        	vm.selectedFolder = folder;
        	splitFolder();
    		vm.fileList = [];
            vm.treeData = [];
        	PhotosService.query({path: folder.path},function(data) {
                vm.photoQueryError = null;
        		vm.photos = [];
            	for (i = 0; i < data.length; i++) {
            		if (data[i].metadata.dir) {
            			vm.treeData.push({
            				text: data[i].metadata.name,
            				path: data[i].metadata.pathLower,
            				state: {expanded: false},
            				nodes: [{}]
            			});
            		} else {
            			vm.photos.push(data[i]);
            			vm.fileList.push(data[i].metadata);
            		}
            	}
            	
            	if (vm.photos.length) {
	                vm.photos[0].selected = true;
	                vm.selectedPhoto = vm.photos[0];
                    if (!vm.selectedPhoto.detections) {
                        vm.selectedPhoto.detections = [{}];
                    }
            	}
    			vm.treeDataLoaded = vm.treeData && vm.treeData.length > 0 ? {} : {notFound: true};
                SpinnerService.hide('folderSpinner');
	        }, function(error) {
	            vm.photoQueryError = error.data.message;
                SpinnerService.hide('folderSpinner');
	        });
        }
        
        function splitFolder() {
        	if (vm.selectedFolder && vm.selectedFolder.path) {
	        	vm.breadCrumbList = [];
	        	var folderList = vm.selectedFolder.path.split("/");
	        	for (i = 0; i < folderList.length; i++) {
		        	var path = "/";
		        	for (j = 0; j < vm.breadCrumbList.length; j++) {
		        		path = path + vm.breadCrumbList[j].text + "/";
	        		}
	        		var link = {
	        				text: folderList[i],
	        				path: path + folderList[i] 
	        		};
	        		if (link && link.text) {
	        			vm.breadCrumbList.push(link);
	        		}
	        	}
        	}
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
	                    if (!vm.selectedPhoto.detections)
	                    	vm.selectedPhoto.detections = [{}]; 
	                } else {
	                    vm.photos[i].selected = false;
	                }
	            }
        	});
        }
        
        function saveSelectedPhoto() {
        	$log.debug("called saveSelectedPhoto");
            SpinnerService.show('savingSpinner');
        	if (!vm.selectedPhoto.id) {
                return PhotosService.save(vm.selectedPhoto, function(data) {
                    $log.debug('save success', data);
                    vm.selectedPhoto = data;
                });
        	} else {
        		vm.entry = PhotosService.get({ id: vm.selectedPhoto.id }, function() {
        			$log.debug("existing photo", vm.selectedPhoto);
        			$log.debug("vm.entry", vm.entry);
        			vm.entry.id = vm.selectedPhoto.id;
        			vm.entry.detections = vm.selectedPhoto.detections;
        			vm.entry.selected = vm.selectedPhoto.selected;
        			vm.entry.species = vm.selectedPhoto.species;
        			vm.entry.src = vm.selectedPhoto.src;
        			vm.entry.thumbSrc = vm.selectedPhoto.thumbSrc;
        			vm.entry.highlight = vm.selectedPhoto.highlight;
        			vm.entry.$update(function(photo) {
        				$log.debug('update success');
                        SpinnerService.hide('savingSpinner');
        				PhotosService.get({id:photo.id}, function(data) {
        					vm.selectedPhoto = data;
        					var photoIndex = -1;
        					$filter('filter')(vm.photos, function (d) {
        						if (vm.selectedPhoto.id === d.id) {
        							photoIndex = vm.photos.indexOf(d);
        						}
        					});
        					vm.photos[photoIndex].detections = data.detections; // updated to update flagged value in UI
        				});
        			});
        		});
        	}
        }
        
        function highlightSelectedPhoto() {
        	vm.selectedPhoto.highlight = !vm.selectedPhoto.highlight;
        	saveSelectedPhoto();
        }
        
        function removeDetection(detection) {
        	$log.debug("called removeDetection",detection);
        	var detectionIdx = vm.selectedPhoto.detections.indexOf(detection);
        	if (detectionIdx > - 1) {
        		vm.selectedPhoto.detections.splice(detectionIdx, 1);
        		saveSelectedPhoto();
        	}
        }
        
        function addDetection() {
        	$log.debug("called addDetection");
        	vm.selectedPhoto.detections.push({});
        }
        
        function onSelectSpeciesCallback(item) {

            DetectionDetails.findBySpeciesId({id: item.id}, function(data) {
            	item.detectionDetailsList = data;
                console.log("vm.selectedPhoto.detectionDetailsList", data);
            });
        }

        function saveSelectedStudyArea() {
            vm.entry = StudyAreas.get({ id: vm.selectedStudyArea.id }, function() {
                vm.entry.dropboxPath = vm.selectedFolder.path;
                vm.entry.$update(function(studyArea) {
                    $log.debug('update success');
                    StudyAreas.query(function(data) {
                        vm.studyAreas = data;
                        selectFolder(vm.selectedFolder);
                    });
                });
            });
        }

        function saveSelectedDeployment() {
            vm.entry = Deployments.get({ id: vm.selectedDeployment.id }, function() {
                vm.entry.dropboxPath = vm.selectedFolder.path;
                vm.entry.$update(function(deployment) {
                    $log.debug('update success');
                    Deployments.query(function(data) {
                        vm.deployments = data;
                        selectFolder(vm.selectedFolder);
                    });
                });
            });
        }

    }
}());