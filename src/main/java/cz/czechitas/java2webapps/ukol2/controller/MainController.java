package cz.czechitas.java2webapps.ukol2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final Random random;

    //metoda pro naplneni seznamu citatu
    private static List<String> readAllLines(String resource) throws IOException {
        //Soubory z resources se získávají pomocí classloaderu. Nejprve musíme získat aktuální classloader.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        //Pomocí metody getResourceAsStream() získáme z classloaderu InpuStream, který čte z příslušného souboru.
        //Následně InputStream převedeme na BufferedRead, který čte text v kódování UTF-8
        try (InputStream inputStream = classLoader.getResourceAsStream(resource);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            //Metoda lines() vrací stream řádků ze souboru. Pomocí kolektoru převedeme Stream<String> na List<String>.
            return reader
                    .lines()
                    .collect(Collectors.toList());
        }
    }

    public List<String> listOfQuotations = readAllLines("citaty.txt");
    public List<String> listOfPicturesUrl = readAllLines("pictures_URL.txt");


    public MainController() throws IOException {
        random = new Random();

    }

    @GetMapping("/")
    public ModelAndView showQuotation() {

        int randomNumberForQuotation = random.nextInt(listOfQuotations.size());
        int randomNumberForPictures = random.nextInt(listOfPicturesUrl.size());

        ModelAndView result = new ModelAndView("citaty"); //viewName odpovida souboru /templates/citaty.html

        //citat - oddeluji jmeno a text citatu, abych mohla jmeno zobrazit oddelene od citatu, jinou barvou,atd.
        String[] splitedQuotation = listOfQuotations.get(randomNumberForQuotation).split(":");
        result.addObject("nameQuotation", splitedQuotation[0]);
        result.addObject("textQuotation", splitedQuotation[1]);

        //obrazek
        result.addObject("onePicture", "background-image: url(" + listOfPicturesUrl.get(randomNumberForPictures) + "/1600x900);");

        return result;
    }
}
