(function () {
    angular.module('CCC')
        .controller('IdPhotosController', IdPhotosController);

    IdPhotosController.$inject = ['$log', '$rootScope', '$scope', '$filter',
                                  'Species', 'DetectionDetails', 'Detection', 'PhotosService'];

    function IdPhotosController($log, $rootScope, $scope, $filter,
    		Species, DetectionDetails, Detection, PhotosService) {
        var vm = this;
        vm.photos = [];
        vm.selectedPhoto = {};
        vm.selectedFolder = {};
        vm.breadCrumbList = [];
        vm.speciesList = [];
        vm.detectionDetailsList = [];
        vm.fileList = [];
        vm.treeData = [];
        vm.fullscreen = false;
        vm.showFilters = true;
        vm.treeDataLoaded = {};

        vm.selectThumb = selectThumb;
        vm.thumbClass = thumbClass;
        vm.selectFolder = selectFolder;
        vm.splitFolder = splitFolder;
        vm.saveSelectedPhoto = saveSelectedPhoto;
        vm.removeDetection = removeDetection;
        vm.addDetection = addDetection;
        vm.onSelectSpeciesCallback = onSelectSpeciesCallback;

        PhotosService.query({},function(data) {
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
        	$log.debug("vm.fileList", vm.fileList);

        	$('#filterDropdown .dropdown-menu').on({
        		"click":function(e){
        	      e.stopPropagation();
        	    }
        	});
        	initializeCarousel();
        });

        Species.query(function(data) {
            vm.speciesList = data;
        });

        function selectThumb(index) {
            $('#myCarousel').carousel(index);
        }
        
        function thumbClass(photo) {
        	return photo.selected ? 'active' : '';
        }
        
        function selectFolder(folder) {
        	vm.selectedFolder = folder;
        	splitFolder();
    		vm.fileList = [];
            vm.treeData = [];
        	PhotosService.query({path: folder.path},function(data) {
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
	                if (!vm.selectedPhoto.detections)
	                	vm.selectedPhoto.detections = [{}];
            	}
    			vm.treeDataLoaded = vm.treeData && vm.treeData.length > 0 ? {} : {notFound: true};
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
//        	PhotosService.save(vm.selectedPhoto, function(data) {
//        		vm.selectedPhoto = data;
//        	});
        	$log.debug("called saveSelectedPhoto");
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
        			vm.entry.$update(function(photo) {
        				$log.debug('update success');
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
        
        function removeDetection(detection) {
        	$log.debug("called removeDetection",detection);
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

    }
}());