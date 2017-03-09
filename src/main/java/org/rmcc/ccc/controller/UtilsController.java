package org.rmcc.ccc.controller;

import org.rmcc.ccc.repository.CameraMonitorRepository;
import org.rmcc.ccc.repository.PhotoRepository;
import org.rmcc.ccc.repository.SpeciesRepository;
import org.rmcc.ccc.repository.StudyAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/utils")
public class UtilsController extends BaseController {

    private StudyAreaRepository studyAreaRepository;
    private CameraMonitorRepository cameraMonitorRepository;
    private PhotoRepository photoRepository;
    private SpeciesRepository speciesRepository;

    @Autowired
    public UtilsController(StudyAreaRepository studyAreaRepository,
                           CameraMonitorRepository cameraMonitorRepository,
                           PhotoRepository photoRepository,
                           SpeciesRepository speciesRepository) {
        this.studyAreaRepository = studyAreaRepository;
        this.cameraMonitorRepository = cameraMonitorRepository;
        this.photoRepository = photoRepository;
        this.speciesRepository = speciesRepository;
    }

    @RequestMapping(value = "/dashboardCounts")
    public Map<String, Integer> getCounts() {
        Map<String, Integer> countsMap = new HashMap<>();
        countsMap.put("newTaggedCount", photoRepository.getNewTaggedCount());
        countsMap.put("newHighlightedCount", photoRepository.getNewHighlightedCount());
        countsMap.put("logCount", cameraMonitorRepository.getCount());
        countsMap.put("studyAreaCount", studyAreaRepository.getCount());
        countsMap.put("detectedSpecies", speciesRepository.getDetectedSpeciesCount());
        countsMap.put("taggedPhotos", photoRepository.getTaggedCount());
        countsMap.put("highlightedPhotos", photoRepository.getHighlightedCount());

        return countsMap;
    }
}
