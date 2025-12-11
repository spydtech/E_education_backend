package com.Eonline.Education.Controller;

import com.Eonline.Education.Service.ThemeSettingService;
import com.Eonline.Education.modals.ThemeSetting;
import com.Eonline.Education.user.Theme;
import com.zaxxer.hikari.metrics.prometheus.PrometheusMetricsTrackerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
public class ThemeSettingController
{
    @Autowired
    private ThemeSettingService themeSettingService;

    @PostMapping
    public ThemeSetting saveTheme(@RequestHeader("Authorization") String jwt,@RequestBody ThemeSetting themeSetting)
    {
        return themeSettingService.saveTheme(jwt,themeSetting);
    }

    @GetMapping("/getAll")
    public List<ThemeSetting> getAll()
    {
        return themeSettingService.getAll();
    }

    @GetMapping("/get/{theme}")
    public ThemeSetting getByTheme(@RequestHeader("Authorization") String jwt,@PathVariable Theme theme)
    {
        return themeSettingService.getByTheme(jwt,theme);
    }
}
