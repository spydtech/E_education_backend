package com.Eonline.Education.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DateResponse {

    private long currentMonthUsers;

    private long currentYearUsers;
}
