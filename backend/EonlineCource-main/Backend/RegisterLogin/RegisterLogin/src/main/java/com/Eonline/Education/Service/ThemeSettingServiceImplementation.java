package com.Eonline.Education.Service;

import com.Eonline.Education.modals.ThemeSetting;
import com.Eonline.Education.repository.ThemeSettingRepository;
import com.Eonline.Education.user.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ThemeSettingServiceImplementation implements ThemeSettingService
{
    @Autowired
    private ThemeSettingRepository themeSettingRepository;
    public ThemeSetting saveTheme(String jwt,ThemeSetting themeSetting)
    {
        ThemeSetting themeSetting1=new ThemeSetting();
        themeSetting1.setTheme(themeSetting.getTheme());
        return themeSettingRepository.save(themeSetting1);
    }
    public List<ThemeSetting> getAll()
    {
        return themeSettingRepository.findAll();
    }

    @Override
    public ThemeSetting getByTheme(String jwt,Theme theme) {
//        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        return themeSettingRepository.getByTheme(theme);
    }

}
