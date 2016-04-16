(function () {
    angular.module('CCC')
        .controller('IdPhotosController', IdPhotosController);

    IdPhotosController.$inject = ['$log', '$scope', 'Species', 'DetectionDetails', 'Detection', 'PhotosService'];

    function IdPhotosController($log, $scope, Species, DetectionDetails, Detection, PhotosService) {
        var vm = this;
        vm.photos = [];
        vm.selectedPhoto = {};
        vm.selectedFolder = {};
        vm.breadCrumbList = [];
        vm.speciesList = [];
        vm.detectionDetailsList = [];
        vm.fileList = [];
        vm.treeData = [];

        vm.selectThumb = selectThumb;
        vm.thumbClass = thumbClass;
        vm.selectFolder = selectFolder;
        vm.splitFolder = splitFolder;
        vm.saveDetection = saveDetection;
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
            			vm.photos.push({
                            id: 1,
                            thumbSrc: 'http://localhost:8080/ccc/api/dropbox/thumb?path='+data[i].metadata.pathLower,
                            src: 'http://localhost:8080/ccc/api/dropbox/image?path='+data[i].metadata.pathLower,
                            species: []
                        });
            			vm.fileList.push(data[i].metadata);
            		}
            	}
            	
            	if (vm.photos.length) {
	                vm.photos[0].selected = true;
	                vm.selectedPhoto = vm.photos[0];
	                if (!vm.selectedPhoto.detections)
	                	vm.selectedPhoto.detections = [{}];
            	}
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
        
        function saveDetection(detection) {
//        	$log.debug("called saveDetection",detection);
//        	vm.selectedPhoto.detections.push(detection);
        	PhotosService.save(vm.selectedPhoto, function(data) {
        		vm.selectedPhoto = data;
        	});
//        	detection.photo = vm.selectedPhoto;
//        	if (!detection.id) {
//                return Detection.save(detection, function(data) {
//                    $log.debug('save success', data);
//                    detection = data;
//                });
//        	} else {
//        		vm.entry = Detection.get({ id: detection.id }, function() {
//        			$log.debug("existing detection", detection);
////        			vm.entry.commonName = data.commonName;
////        			vm.entry.latinName = data.latinName;
////        			vm.entry.shortcutKey = data.shortcutKey;
////        			vm.entry.$update(function(species) {
////        				$log.debug('update success');
////        				Species.query(function(data) {
////        					vm.species = data;
////        				});
////        			});
//        		});
//        	}
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