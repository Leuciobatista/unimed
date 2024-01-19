package com.example.api.service;

import com.example.api.domain.Address;
import com.example.api.domain.AddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ViaCepService(RestTemplate restTemplate) {
        ViaCepService.restTemplate = restTemplate;
    }
    public static AddressResponse getAddressFromCep(String cep) {
        AddressResponse response = new AddressResponse();

        if (isValidCep(cep)) {
            final String url = "https://viacep.com.br/ws/" + cep + "/json/";
            try {
                Address address = restTemplate.getForObject(url, Address.class);
                if (address != null && address.getLogradouro() != null) {
                    response.setAddress(address);
                } else {
                    response.setMessage("Endereço não encontrado para o CEP fornecido");
                }
            } catch (Exception e) {
                response.setMessage("Erro ao buscar CEP: " + e.getMessage());
            }
        } else {
            response.setMessage("Cep inválido");
        }

        return response;
    }


    public static boolean isValidCep(String cep) {
        return cep != null && cep.matches("\\d{8}");
    }

}
