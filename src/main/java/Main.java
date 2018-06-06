
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    static Document doc = null;
    static String URL;
    static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    public static void main(String[] args){

        inputURL();
        amountOfLines();
        amountOfParagraphs();
        amountOfImages();
        amountOfForms();
        showInputs();
        makePost();

    }

    private static void inputURL(){

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Digite la URL: ");
            URL = sc.nextLine();

            int index = URL.indexOf("http://");

            if( index != 0 ){
                URL = "http://" + URL;
            }

        }while( !loadDocument(URL));

    }

    private static boolean loadDocument(String url){
        try {
            doc = Jsoup.connect(url).userAgent(userAgent).timeout(5000).get();
        } catch (Exception e) {
            System.out.println("Hubo un error con la URL ingresada " );
            return false;
        }

        return true;
    }

    private static void amountOfLines(){
        String output = doc.outerHtml();
        String[] lines = output.split("\r\n|\n|\r");
        System.out.println("Cantidad de lineas: " + lines.length);
    }

    private static void amountOfParagraphs(){
        Elements elem = doc.select("p");
        System.out.println("B) Cantidad parrafos : " + elem.size());
    }

    private static void amountOfImages(){
        Elements elem = doc.select("p img");

        int amount_of_images = 0;
        for(Element paragraph : elem)
            amount_of_images += paragraph.select("img").size();

        System.out.println("C) Cantidad imagenes dentro de parrafos : " + amount_of_images);
    }

    private static void amountOfForms(){
        Elements elem = doc.select("form");

        int get_forms = 0;
        int post_forms = 0;

        for(Element e : elem){

            String action = e.attr("method");
            action = action.toLowerCase();

            if( action.equals("post")){
                post_forms++;
            }else{
                get_forms++;
            }
        }
        System.out.println("D) Cantidad forms GET : " + get_forms + " \n Cantidad forms POST : " + post_forms);
    }

    private static void showInputs(){

        Elements elem = doc.select("form");
        System.out.println("E) Forms y sus respectivos inputs");
        for(Element e : elem){

            String formAction = e.absUrl("action") , formMethod = e.attr("method");
            System.out.println("FORMULARIO " + formAction + " " + formMethod);

            for(Element i : elem.select("input")){
                String input_name = i.attr("name");
                String input_tipo = i.attr("type");
                System.out.println(input_name + " " + input_tipo);
            }

        }

    }

    private static void makePost(){

        Elements elem = doc.select("form");
        System.out.println("F) Form y hacer post");
        for(Element e : elem){

            String formAction = e.absUrl("action"), formMethod = e.attr("method");

            if( formAction == "" ){
                formAction = URL;
            }

            formMethod = formMethod.toLowerCase();

            if( formMethod.equals("post") ){

                String userAgent = "Mozilla";
                Document d = null;
                try {
                    d = Jsoup.connect(formAction).data("asignatura", "practica1").header("Matricula", "20101753").post();
                    String salida = d.outerHtml();
                    System.out.println("Mandar a " + formAction);
                    System.out.println(salida);
                } catch (Exception e1) {
                    System.out.println("ERROR CON " + formAction);
                }

            }
        }
    }
}
