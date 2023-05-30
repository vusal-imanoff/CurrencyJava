package com.example.currencyxml.controller;


import com.example.currencyxml.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import com.example.currencyxml.model.Currency;
import lombok.var;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api/currency")

public class CurrencyController {

    private final CurrencyRepository currencyRepository;


    @GetMapping("/getall")
    public List<Currency> getall() {


        try {

            LocalDate currentDate = LocalDate.now();
            String day = String.valueOf(currentDate.getDayOfMonth());
            String month = String.valueOf(currentDate.getMonthValue());
            String year = String.valueOf(currentDate.getYear());

            if (day.length() == 1)
                day = "0" + day;
            if (month.length() == 1)
                month = "0" + month;
            String fulldate = String.join(".", day, month, year);
            //String fulldate = "29.05.2023";
            List<Currency> cur = currencyRepository.findAll();
            for (Currency c : cur )
            {
                 if (!c.getDate().equals(fulldate))
                 {
                     currencyRepository.deleteAll();
                     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                     DocumentBuilder builder = factory.newDocumentBuilder();
                     Document document = builder.parse(String.format("https://www.cbar.az/currencies/%s.xml", fulldate));
                     Element valCursElement = document.getDocumentElement();
                     String date = valCursElement.getAttribute("Date");

                     NodeList valTypeList = document.getElementsByTagName("ValType");
                     for (int i = 0; i < valTypeList.getLength(); i++) {
                         Element valTypeElement = (Element) valTypeList.item(i);
                         String type = valTypeElement.getAttribute("Type");
                         if (type.equals("Xarici valyutalar")) {
                             NodeList valuteList = valTypeElement.getElementsByTagName("Valute");
                             for (int j = 0; j < valuteList.getLength(); j++) {
                                 Element valuteElement = (Element) valuteList.item(j);
                                 String code = valuteElement.getAttribute("Code");
                                 String name = valuteElement.getElementsByTagName("Name").item(0).getTextContent();
                                 String nominal = valuteElement.getElementsByTagName("Nominal").item(0).getTextContent();
                                 String value = valuteElement.getElementsByTagName("Value").item(0).getTextContent();

                                 Currency currency = new Currency();
                                 currency.code = code;
                                 currency.name = name;
                                 currency.nominial = nominal;
                                 currency.value = value;
                                 currency.date = date;
                                 currencyRepository.save(currency);
                             }

                         }
                     }
                 }
                 break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Currency> currencies = currencyRepository.findAll();
        return currencies;
    }
}
