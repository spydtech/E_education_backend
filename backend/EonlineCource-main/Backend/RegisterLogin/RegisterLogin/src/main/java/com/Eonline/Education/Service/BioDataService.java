package com.Eonline.Education.Service;

import com.Eonline.Education.modals.BioData;

public interface BioDataService {

    public BioData post(BioData bioData);

    public BioData findById(Long id);

    BioData findByUserId(Long id);
}
