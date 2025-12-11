package com.Eonline.Education.Service;

import com.Eonline.Education.modals.ThemeSetting;
import com.Eonline.Education.user.Theme;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ThemeSettingService {
    ThemeSetting saveTheme(String jwt,ThemeSetting themeSetting);
    List<ThemeSetting> getAll();

    ThemeSetting getByTheme(String jwt,Theme theme);
}
