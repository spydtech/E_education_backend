package com.Eonline.Education.repository;

import com.Eonline.Education.modals.ThemeSetting;
import com.Eonline.Education.user.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeSettingRepository extends JpaRepository<ThemeSetting,Long> {
    ThemeSetting getByTheme(Theme theme);
}
