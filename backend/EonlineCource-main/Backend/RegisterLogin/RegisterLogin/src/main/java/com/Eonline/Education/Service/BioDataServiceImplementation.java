package com.Eonline.Education.Service;

import com.Eonline.Education.modals.BioData;
import com.Eonline.Education.repository.BioDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BioDataServiceImplementation implements BioDataService{
    @Autowired
    private BioDataRepository bioDataRepository;
    @Override
    public BioData post(BioData bioData) {
        return bioDataRepository.save(bioData);
    }

    @Override
    public BioData findById(Long id) {
        return bioDataRepository.findById(id).orElse(null);
    }

    @Override
    public BioData findByUserId(Long id) {
        return bioDataRepository.findByUserId(id);
    }
}
