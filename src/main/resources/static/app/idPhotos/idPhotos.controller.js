(function () {
    angular.module('CCC')
        .controller('IdPhotosController', IdPhotosController);

    IdPhotosController.$inject = ['$log', '$scope', 'PhotosService', 'Species', 'Dropbox'];

    function IdPhotosController($log, $scope, PhotosService, Species, Dropbox) {
        var vm = this;
        vm.photos = [];
        vm.selectedPhoto = {};
        vm.speciesList = [];
        vm.fileList = [];
        vm.treeData = [];

        vm.selectThumb = selectThumb;
        vm.thumbClass = thumbClass;

        PhotosService.getMockedPhotos({},function(data) {
            vm.photos = data;
            vm.photos[0].selected = true;
            vm.selectedPhoto = vm.photos[0];
            $scope.$on('$viewContentLoaded', initializeCarousel);
        });

        Dropbox.query({},function(data) {
        	for (i = 0; i < data.length; i++) {
        		if (data[i].dir) {
        			vm.treeData.push({
        				text: data[i].name,
        				path: data[i].pathLower,
        				state: {expanded: false},
        				nodes: [{}]
        			});
        		} else {
        			vm.fileList.push(data[i]);
        		}
        	}
        	$log.debug("vm.fileList", vm.fileList);
        	
        	initializeFilters(); 
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
            
            initializeFilters();
        }
        
        function initializeFilters() {
	    	$('#tree').treeview({data: vm.treeData});
	    	$('#myDropdown .dropdown-menu').on({
	    		"click":function(e){
	    	      e.stopPropagation();
	    	    }
	    	});
	    	$('#tree').on('nodeExpanded', function(event, expandedNode) {
	    		$log.debug("vm.treeData", vm.treeData);
	    		$log.debug("nodeExpanded", expandedNode);
	    		$log.debug("vm.treeData[expandedNode.parentId]", vm.treeData[expandedNode.parentId]);
	    		$log.debug("vm.treeData[expandedNode.nodeId]", vm.treeData[expandedNode.nodeId]);

    	        Dropbox.query({path: expandedNode.path},function(data) {
    	        	fetchedNodes = [];
    	        	vm.fileList = [];
    	        	for (i = 0; i < data.length; i++) {
    	        		if (data[i].dir) {
    	        			fetchedNodes.push({
    	        				text: data[i].name,
    	        				path: data[i].pathLower,
    	        				state: {expanded: false},
    	        				nodes: [{}]
    	        			});
    	        		} else {
    	        			vm.fileList.push(data[i]);
    	        		}
    	        	}
    	        	vm.treeData[expandedNode.nodeId].state = {expanded: true};
    	        	vm.treeData[expandedNode.nodeId].nodes = fetchedNodes;
    	        	$log.debug("vm.fileList", vm.fileList);
    	        	
    	        	initializeFilters(); 
    	        });
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