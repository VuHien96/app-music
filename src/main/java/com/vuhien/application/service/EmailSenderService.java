package com.vuhien.application.service;

import org.springframework.stereotype.Service;

/**
 * Created by VuHien96 on 02/08/2021 22:09
 */
@Service
public interface EmailSenderService {
    void sendEmail(String to,String name,String link);
}
