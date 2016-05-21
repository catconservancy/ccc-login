(function () {
    angular.module('CCC')
        .controller('SpeciesController', SpeciesController);

    SpeciesController.$inject = ['$log','Species', 'DTOptionsBuilder'];

    function SpeciesController($log, Species, DTOptionsBuilder) {
        var vm = this;
        vm.add = add;
        vm.checkCommonName = checkCommonName;
        vm.checkLatinName = checkLatinName;
        vm.checkShortcutKey = checkShortcutKey;
        vm.saveSpecies = saveSpecies;
        vm.removeSpecies = removeSpecies;
        vm.inserted = null;
        vm.species = null;
        
        // DataTables configurable options
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withDisplayLength(5)
            .withOption('bLengthChange', false);
		
        Species.query(function(data) {
			vm.species = data;
		});
        
        function add() {
        	vm.inserted = {id: 0};
        	vm.species.push(vm.inserted);
        }
        
        function checkCommonName(data, id) {
        	//TODO: validation impl
        	$log.debug('data', data);
        	$log.debug('id', id);
        }
        
        function checkLatinName(data, id) {
        	//TODO: validation impl
        	$log.debug('data', data);
        	$log.debug('id', id);
        }
        
        function checkShortcutKey(data, id) {
        	//TODO: validation impl
        	$log.debug('data', data);
        	$log.debug('id', id);
        }
        
        function saveSpecies(data, id) {
        	if (id === 0) {
                angular.extend(data, {id: id});
                return Species.save(data, function(species) {
                    $log.debug('save success');
                    Species.query(function(data) {
            			vm.species = data;
            		});
                });
        	} else {
        		vm.entry = Species.get({ id: id }, function() {
        			vm.entry.commonName = data.commonName;
        			vm.entry.latinName = data.latinName;
        			vm.entry.shortcutKey = data.shortcutKey;
        			vm.entry.$update(function(species) {
        				$log.debug('update success');
        				Species.query(function(data) {
        					vm.species = data;
        				});
        			});
        		});
        	}
        }
        
        function removeSpecies(id) {
        	if (id === 0) {
        		for( i= vm.species.length-1; i>=0; i--) {
        		    if( vm.species[i].id == "0") 
        		    	vm.species.splice(i,1);
        		}
        	} else {
	        	Species.delete({ id: id }, function() {
	        		$log.debug('Deleted from server');
	        		Species.query(function(data) {
	        			vm.species = data;
	        		});
	        	});
        	}
        }
    }
}());