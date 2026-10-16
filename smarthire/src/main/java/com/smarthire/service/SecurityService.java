package com.smarthire.service;

import com.smarthire.entity.Company;
import com.smarthire.entity.User;

public interface SecurityService {

    User getCurrentUser();

    Company getCurrentUserCompany();
}
