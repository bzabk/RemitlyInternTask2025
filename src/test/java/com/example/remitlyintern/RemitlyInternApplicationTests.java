package com.example.remitlyintern;

import com.example.remitlyintern.Controller.SwiftCodeController;
import com.example.remitlyintern.Dto.BranchDTO;
import com.example.remitlyintern.Dto.BranchWithCountryName;
import com.example.remitlyintern.Repository.SwiftCodeRepository;
import com.example.remitlyintern.Service.SwiftCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SwiftCodeController.class)
class RemitlyInternApplicationTests {



}
