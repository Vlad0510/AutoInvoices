package com.Invoices.AutoInvoices.Controller;

import com.Invoices.AutoInvoices.Service.OblioService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oblio")
public class OblioController {

//    @Autowired
//    private OblioService oblioService;
//
//    // Endpoint pentru crearea unei proforme
//    @PostMapping("/create-proforma")
//    public ResponseEntity<String> createProforma(@RequestBody String proformaData) {
//        // Transforma string-ul primit din body într-un JSONObject
//        JSONObject proformaJson = new JSONObject(proformaData);
//
//        // Apelează metoda din OblioService pentru crearea proformei
//        String proformaResponse = oblioService.createProforma(proformaJson);
//
//        // Returnează răspunsul API-ului Oblio
//        return ResponseEntity.ok(proformaResponse);
//    }
}
