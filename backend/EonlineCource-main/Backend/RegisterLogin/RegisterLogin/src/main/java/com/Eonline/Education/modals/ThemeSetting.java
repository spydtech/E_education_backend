package com.Eonline.Education.modals;

import com.Eonline.Education.user.Theme;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ThemeSetting
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "theme", nullable = false)
    private Theme theme = Theme.LIGHT;
}
