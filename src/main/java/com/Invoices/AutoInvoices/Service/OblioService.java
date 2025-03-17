package com.Invoices.AutoInvoices.Service;

import com.Invoices.AutoInvoices.Entity.*;
import com.Invoices.AutoInvoices.Repository.StudentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

@Service
public class OblioService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    private final AccessTokenHandlerFileStorage tokenHandler = new AccessTokenHandlerFileStorage();

    public String obtainAccessToken() {
        AccessToken token = tokenHandler.get();

        if (token == null) {
            // Dacă nu există un token valid, facem cererea pentru un token nou
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://www.oblio.eu/api/authorize/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "{}";//client.secret Oblio

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parsam raspunsul și cream un nou token
                token = new AccessToken().fromJsonString(response.getBody());
                // Salvam noul token
                tokenHandler.set(token);
            } else {
                throw new RuntimeException("Eroare la obținerea token-ului");
            }
        }

        return token.access_token; // Returnăm token-ul de acces
    }

    public String createProformaForStudent(int studentId){

        // Obține token-ul de acces
        String accessToken = obtainAccessToken();

        // URL-ul pentru crearea documentului de tip proforma
        String url = "https://www.oblio.eu/api/docs/proforma";

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        //Proforma
        Proforma proforma = new Proforma();
        proforma.setCif("CUI");

        Client client = new Client();
        client.setName(student.getNume_parinte());
        client.setEmail(student.getEmail_parinte());
        proforma.setClient(client);

        proforma.setIssueDate(LocalDate.now().toString());
        proforma.setDueDate(LocalDate.now().plusDays(15).toString());
        proforma.setSeriesName("TEST");
        proforma.setLanguage("RO");
        proforma.setCurrency("RON");

        //Produs
        Product produs = new Product();
        produs.setName("Ore robotica Melted");
        produs.setPrice(10.0);
        produs.setMeasuringUnit("buc"); //asa cum cere oblio
        produs.setQuantity(6);


        //Adaug produsul/produsele in proforma
        proforma.setProducts(List.of(produs));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // Verific daca accessToken e valid
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Proforma> requestEntity = new HttpEntity<>(proforma, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        //Sa vedem ce zice Oblio
        if(response.getStatusCode() == HttpStatus.OK){

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseJson = mapper.readTree(response.getBody());

                System.out.println("Răspuns JSON de la Oblio: " + responseJson.toString());

                // Accesăm câmpul 'link' din interiorul 'data'
                if (responseJson.has("data") && responseJson.get("data").has("link")) {
                    String proformaLink = responseJson.get("data").get("link").asText();
                    System.out.println("LinkProforma generat cu succes. Link: " + proformaLink);
                    return proformaLink;
                } else {
                    throw new RuntimeException("Câmpul 'link' lipsește din răspunsul JSON.");
                }
            } catch (Exception e) {
                throw new RuntimeException("Eroare la prelucrarea răspunsului JSON de la Oblio", e);
            }
        }
        else {
            throw new RuntimeException("Eroare la generarea proformei: " + response.getStatusCode());
        }
    }

    public void createAndSendProforma(int studentId){

        String proformaLink = createProformaForStudent(studentId);

        // 2. Obținem detaliile studentului
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        try {
            // 3. Descărcăm PDF-ul de la link
            byte[] pdfBytes = downloadPdf(proformaLink);

            // 4. Trimitem emailul cu atașamentul PDF
            String emailBody = "Stimate " + student.getNume_parinte() + ",\n\n"
                    + "Proforma este atașată acestui email.\n\n"
                    + "Va rugam sa efectuati plata in termen de 15 zile.\n\n"
                    + "O zi frumoasa,\n Melted";

            emailService.sendEmailWithAttachment(
                    student.getEmail_parinte(),
                    "Proforma pentru prezentele copilului " + student.getNume(),
                    emailBody,
                    pdfBytes,
                    "proforma_" + student.getNume() + ".pdf"
            );

            System.out.println("Email trimis cu succes către: " + student.getEmail_parinte());
        } catch (Exception e) {
            System.out.println("Eroare la generarea și trimiterea proformei PDF: " + e.getMessage());
        }
    }

    public byte[] downloadPdf(String pdfUrl) throws IOException {
        URL url = new URL(pdfUrl);
        try (InputStream inputStream = url.openStream()) {
            return StreamUtils.copyToByteArray(inputStream);
        }
    }

}

// Metoda pentru crearea unei proforme
//    public String createProforma(JSONObject proformaData) {
//        // Obține token-ul de acces
//        String accessToken = obtainAccessToken();
//
//        // URL-ul pentru crearea documentului de tip proforma
//        String url = "https://www.oblio.eu/api/docs/proforma";
//
//        // Configurare headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(accessToken);
//
//        // Crearea cererii POST
//        HttpEntity<String> request = new HttpEntity<>(proformaData.toString(), headers);
//
//        // Executarea cererii prin RestTemplate
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
//
//        // Verificarea răspunsului și returnarea rezultatului
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();  // Proforma creată cu succes
//        } else {
//            throw new RuntimeException("Eroare la crearea proformei: " + response.getBody());
//        }
//    }