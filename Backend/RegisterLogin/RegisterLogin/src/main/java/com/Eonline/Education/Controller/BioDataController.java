package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.BioDataService;

import com.Eonline.Education.modals.BioData;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class BioDataController {
    @Autowired
    private BioDataService bioDataService;



    @PostMapping("/save")
    public BioData postDetails(@RequestBody BioData bioData){
        return bioDataService.post(bioData);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BioData>UpdateDetails(@PathVariable Long id,@RequestBody BioData bioData){

        BioData existingDetails=bioDataService.findByUserId(id);

        if (existingDetails==null){
            return  ResponseEntity.notFound().build();
        }
        existingDetails.setFirstName(bioData.getFirstName());
        existingDetails.setLastName(bioData.getLastName());
        existingDetails.setDateOfBirth(bioData.getDateOfBirth());
        existingDetails.setGender(bioData.getGender());
        existingDetails.setLocation(bioData.getLocation());
        existingDetails.setPhoneNumber(bioData.getPhoneNumber());
        existingDetails.setEmail(bioData.getEmail());

        BioData savedDetails=bioDataService.post(existingDetails);

        return ResponseEntity.ok(savedDetails);


    }

}
