package com.Eonline.Education.Service;

import com.Eonline.Education.modals.ContactUs;
import com.Eonline.Education.repository.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactUsServiceImpl implements ContactUsService{
    @Autowired
    ContactUsRepository contactUsRepository;
    @Override
    public ContactUs saveContactDetails(ContactUs contactUs) {

        return contactUsRepository.save(contactUs);
    }
}
