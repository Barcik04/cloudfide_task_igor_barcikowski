package com.example.cloudfide_igor_barcikowski_task.exception;


import java.time.LocalDate;

public record ApiResponse(
        String info,
        String message,
        LocalDate time
) {
}
