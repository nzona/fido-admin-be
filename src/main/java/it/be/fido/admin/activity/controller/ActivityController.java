package it.be.fido.admin.activity.controller;

import it.be.fido.admin.activity.dto.ActivityDto;
import it.be.fido.admin.services.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @GetMapping("/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActivityDto>> getActivities() throws Exception {
        List<ActivityDto> activityDtos = activityService.getActivityDtos();
        return ResponseEntity.ok(activityDtos);
    }

    @GetMapping("/activities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityDto> getActivity(@Valid @PathVariable("id") Long id) throws Exception {
        ActivityDto activityDto = activityService.getActivityDto(id);
        return ResponseEntity.ok(activityDto);
    }

    @PostMapping("/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityDto> createActivity(@Valid @RequestBody ActivityDto activityDtoRequest) throws Exception {
        ActivityDto response = activityService.createOrUpdateActivityEntity(activityDtoRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/activities")
    public ResponseEntity<ActivityDto> updateActivity(@Valid @RequestBody ActivityDto activityDtoRequest) throws Exception {
        ActivityDto response = activityService.createOrUpdateActivityEntity(activityDtoRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/activities/{id}")
    public ResponseEntity<HttpStatus> deleteActivity(@PathVariable("id") Long id) throws Exception {
        activityService.deleteActivityEntity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
