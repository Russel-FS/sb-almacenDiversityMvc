package com.api.diversity.application.service.interfaces;

import com.api.diversity.application.dto.RolDto;

public interface IRolService {
    RolDto findById(Long id);
}