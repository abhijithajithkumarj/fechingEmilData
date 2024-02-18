package com.fechingEmailData.fechingEmilData.service;

import com.fechingEmailData.fechingEmilData.entity.FetchEmailData;
import jakarta.mail.Message;

public interface FetchingEmailService {


    FetchEmailData save (Message[] messages);
}
