package com.github.tl114.aakontroll;

import com.github.tl114.aakontroll.sorteerimismeetodid.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class GUI extends Application {

    private static final VBox kasutajaSisendid = new VBox(); //Siia salvestatakse kasutaja sisendid ja nende kontroll, et seda hiljem kuvada.

    private static int tooEtte = 0;
    private static String failiNimi = "";

    private static SorteerimisMeetod sorteerimisMeetod;

    public static void main(String[] args) {
        launch(args);
    }

    /*
     Meetod, mis muudab failist loetud järjendi täisarvujärjendiks
     */
    private int[] ridaJärjendiks(String rida) {
        String[] tükid = rida.split("[, \\[\\]]");
        List<Integer> numbrid = new ArrayList<>();
        for (String s : tükid) {
            if (!s.equals("")) {
                numbrid.add(Integer.parseInt(s));
            }
        }
        int[] järjend = new int[numbrid.size()];
        for (int i = 0; i < järjend.length; i++) {
            järjend[i] = numbrid.get(i);
        }
        return järjend;
    }

    /*
     Meetod, mis saab ette faili kausttee ning valib sealt ühe suvalise rea
     ning muudab selle täisarvujärjendiks
     */
    private int[] valiSuvalineJärjendFailist(String kaustTee) {
        List<String> read = new ArrayList<>();
        try (FileReader reader = new FileReader(kaustTee); BufferedReader br = new BufferedReader(reader)) {
            String rida;
            while ((rida = br.readLine()) != null) {
                read.add(rida);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Viga");
            alert.setHeaderText(null);
            alert.setContentText("Vigane fail.");
            alert.showAndWait();
        }
        return ridaJärjendiks(read.get(new Random().nextInt(read.size())));
    }

    /*
     Erijuhtum olukorrale, kus ülesanne on kiirmeetodil ette toomisena. Kuna sellisel juhul ei sorteerita järjendit
     vaid tuuakse ette mingi kindel arv väiksemaid numbreid, peab selle arvu ka kuidagi kätte saama.
     Siin on eeldatud, et failis on read kujul [järjend];Täisarv
     */
    private String ridaFailistTooEtte(String kaustTee) {
        List<String> read = new ArrayList<>();
        try (FileReader reader = new FileReader(kaustTee); BufferedReader br = new BufferedReader(reader)) {
            String rida;
            while ((rida = br.readLine()) != null) {
                read.add(rida);
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
        return read.get(new Random().nextInt(read.size()));
    }

    /*
     Meetod, mis saab kasutaja käest kätte meetodinime, ning seejärel valib õigest failist ühe suvalise järjendi
     */
    private int[] järjendFailist(String kaustTee, boolean valikuKiirmeetod) {
        try {
            if (valikuKiirmeetod) {
                String rida = ridaFailistTooEtte(kaustTee);
                String[] tükid = rida.split(";");
                tooEtte = Integer.parseInt(tükid[1]);
                return ridaJärjendiks(tükid[0]);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Viga");
            alert.setHeaderText(null);
            alert.setContentText("Valiku kiirmeetodi sisend peab olema kujul: [Järjend];Arv, kus arv näitab kui mitu väiksemat ette tuuakse.");
            alert.showAndWait();
        }

        return valiSuvalineJärjendFailist(kaustTee);
    }

    /*
     Meetod, mis viib programmi tagasi algseisu.
     Faili nimi jäetakse alles.
     */
    private void taastaAlgSeisund(String kaustTee) {
        kasutajaSisendid.getChildren().clear();
        failiNimi = kaustTee;
        tooEtte = 0;
    }

    /*
     Meetod, mis viib ette antud täisarvu järjendi graafilisele kujule.
     */
    private GridPane looJärjend(int[] järjend, boolean viga) {
        GridPane järjendiVaade = new GridPane();
        järjendiVaade.setHgap(30);
        for (int i = 0; i < järjend.length; i++) {
            ColumnConstraints column = new ColumnConstraints(50);
            järjendiVaade.getColumnConstraints().add(column);
            TextField gap = new TextField("" + järjend[i]);
            gap.setEditable(false);
            gap.setAlignment(Pos.CENTER);
            if (viga) {
                gap.setStyle("-fx-text-box-border: red");
            }
            järjendiVaade.add(gap, i, 0);
        }
        return järjendiVaade;
    }

    /*
     Meetod, mis teeb tühjad lüngad kuhu saab sisse kirjutada
     */
    private GridPane sisendLüngad(int pikkus) {
        GridPane lüngad = new GridPane();
        lüngad.setHgap(30);
        for (int i = 0; i < pikkus; i++) {
            ColumnConstraints column = new ColumnConstraints(50);
            lüngad.getColumnConstraints().add(column);
            TextField gap = new TextField("");
            gap.setAlignment(Pos.CENTER);
            lüngad.add(gap, i, 0);
        }
        return lüngad;
    }

    /*
     Meetod, mis muudab programmi vaadet selliseks, et kasutaja saaks sinna lahendust kirjutada
     */
    private VBox looKasutajaSisendVaade(int[] sisend, GridPane lukud) {
        VBox vBox = new VBox();
        vBox.setSpacing(15);

        Text text = new Text("Järjendi seisund: ");
        Text text2 = new Text("Sisesta järgmine samm algoritmis: ");

        GridPane algJärjend = looJärjend(sisend, false);
        lukud.setPadding(new Insets(-10, 0, 0, 12));
        GridPane kasutajaSisend = sisendLüngad(sisend.length);
        lukud.setPickOnBounds(false);
        vBox.getChildren().addAll(text, algJärjend, text2, kasutajaSisend, lukud);

        return vBox;

    }

    /*
     Meetod, täidab kasutaja eest ära kõik tühjad lahtrid kopeerides need järjendi hetkeseisust.
     */
    private void kopeeriTühjadLahtrid(GridPane grid1, GridPane grid2) {
        for (int i = 0; i < grid1.getChildren().size(); i++) {
            TextField firstGridCell = (TextField) grid1.getChildren().get(i);
            TextField secondGridCell = (TextField) grid2.getChildren().get(i);
            if (secondGridCell.getText().equals("")) {
                secondGridCell.setText(firstGridCell.getText());
            }
        }
    }

    /*
     Meetod, mis muudab graafilisel kujul oleva järjendi täisarvujärjendiks
     */
    private int[] sisendJärjendiks(GridPane gridPane) {
        int[] järjend = new int[gridPane.getChildren().size()];
        for (int i = 0; i < järjend.length; i++) {
            TextField text = (TextField) gridPane.getChildren().get(i);
            järjend[i] = Integer.parseInt(text.getText());
        }
        return järjend;
    }

    /*
     Meetod mis lisab GridPane kindla suurusega vahed.
     */
    private void lisaLukkudeVahed(GridPane gridPane, int lahtreid) {
        for (int i = 0; i < lahtreid; i++) {
            ColumnConstraints column = new ColumnConstraints(80);
            gridPane.getColumnConstraints().add(column);
        }
    }

    /*
     Meetod, mis teeb nupud millega saab kasutaja lahtreid ükshaaval lukku panna ja lahti teha.
     */
    private GridPane looLukud(int arv) {
        GridPane grid = new GridPane();
        for (int i = 0; i < arv; i++) {
            Button button = new Button("");
            button.setMinSize(25, 25);
            button.setMaxSize(25, 25);
            button.setAlignment(Pos.CENTER);
            Image img = new Image(Objects.requireNonNull(getClass().getResource("/images/lock.png")).toExternalForm());
            ImageView view = new ImageView(img);
            view.setFitHeight(20);
            view.setPreserveRatio(true);
            button.setGraphic(view);
            grid.add(button, i, 0);
        }
        return grid;
    }

    private void kuvaTulemus() {
        Stage popUp = new Stage();
        popUp.setMinWidth(250);
        popUp.setMinHeight(400);
        BorderPane layOut = new BorderPane();
        layOut.setPadding(new Insets(15, 15, 15, 15));
        VBox vBox = new VBox();
        vBox.getChildren().addAll(kasutajaSisendid.getChildren());
        layOut.setLeft(vBox);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Tulemus");

        Button sulge = new Button("Sulge");
        sulge.setOnAction(e -> popUp.close());
        layOut.setBottom(sulge);
        Scene scene = new Scene(layOut);
        popUp.setScene(scene);
        popUp.sizeToScene();
        popUp.showAndWait();

    }

    private boolean kontrolliJärjend(int[] sisend) {
        boolean viga = false;

        sorteerimisMeetod.järgmineSamm();
        int[] korrektneSisend = sorteerimisMeetod.getJärjend().clone();

        if (!Arrays.equals(korrektneSisend, sisend)) {
            viga = true;
            sorteerimisMeetod.setJärjend(sisend);
        }
        salvestaKasutajaSisend(korrektneSisend, sisend);
        return viga;
    }

    private void salvestaKasutajaSisend(int[] korrektneSisend, int[] sisendJärjend) {
        Text sisendiTekst = new Text("Sisestatud järjend: ");
        sisendiTekst.setFont(new Font(15));
        Text kontrolliTekst = new Text("Algoritmi samm: ");
        kontrolliTekst.setFont(new Font(15));

        GridPane gp = new GridPane();
        gp.setHgap(8);
        gp.add(sisendiTekst, 0, 0);
        gp.add(kontrolliTekst, 0, 1);
        gp.getColumnConstraints().add(new ColumnConstraints(120));
        TextFlow sisend = new TextFlow();
        TextFlow korrektne = new TextFlow();

        for (int i = 0; i < sisendJärjend.length; i++) {
            int sisendArv = sisendJärjend[i];
            int korrektneArv = korrektneSisend[i];
            Text sisendArvuTekst = new Text(String.valueOf(sisendArv));
            Text kontrollArvuTekst = new Text(String.valueOf(korrektneArv));
            sisendArvuTekst.setFont(new Font(15));
            kontrollArvuTekst.setFont(new Font(15));
            if (sisendArv != korrektneArv) {
                sisendArvuTekst.setFill(Color.RED);
            }
            sisend.getChildren().add(sisendArvuTekst);
            korrektne.getChildren().add(kontrollArvuTekst);
            if (i != sisendJärjend.length - 1) {
                sisend.getChildren().add(new Text(", "));
                korrektne.getChildren().add(new Text(", "));
            }
        }
        gp.add(sisend, 1, 0);
        gp.add(korrektne, 1, 1);
        kasutajaSisendid.getChildren().add(gp);
    }

    private SorteerimisMeetod valiMeetod(String meetodiNimi, int[] järjend) {
        SorteerimisMeetod meetod = null;
        switch (meetodiNimi) {
            case "Mullimeetod":
                meetod = new MulliMeetod(järjend);
                break;
            case "Valikumeetod":
                meetod = new ValikuMeetod(järjend);
                break;
            case "Pistemeetod":
                meetod = new PisteMeetod(järjend);
                break;
            case "Kuhjasta":
                meetod = new KuhjaMeetod(true, järjend);
                break;
            case "Järjestamine kuhjameetodil":
                meetod = new KuhjaMeetod(false, järjend);
                break;
            case "Kiirmeetod":
                meetod = new KiirMeetod(false, 0, järjend);
                break;
            case "Valiku Kiirmeetod":
                meetod = new KiirMeetod(true, tooEtte, järjend);
                break;
            case "Põimemeetod":
                meetod = new PõimeMeetod(false, järjend);
                break;
            case "Alt ülesse põimemeetod":
                meetod = new PõimeMeetod(true, järjend);
        }
        return meetod;
    }

    @Override
    public void start(Stage primaryStage) {

        //Nupud
        Button valiFail = new Button("Vali fail");
        Button alusta = new Button("Alusta");
        Button tagasi = new Button("Tagasi");
        tagasi.setDisable(true);
        Button sisesta = new Button("Järgmine samm");
        Button eelmine = new Button("Eelmine samm");
        Button vabasta = new Button("Vabasta väljad");
        Button lõpeta = new Button("Lõpeta");
        Button kopeeriTühjad = new Button("Kopeeri tühjad lahtrid");

        HBox nupuGrupp = new HBox();
        HBox nupuGrupp2 = new HBox();
        nupuGrupp.getChildren().addAll(kopeeriTühjad, vabasta);
        nupuGrupp2.getChildren().addAll(eelmine, sisesta);
        nupuGrupp.setSpacing(15);
        nupuGrupp2.setSpacing(15);


        //Radiobuttonid

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton mullimeetod = new RadioButton("Mullimeetod");
        RadioButton kiirmeetod = new RadioButton("Kiirmeetod");
        RadioButton valikuMeetod = new RadioButton("Valikumeetod");
        RadioButton kuhjasta = new RadioButton("Kuhjasta");
        RadioButton kuhjameetod = new RadioButton("Järjestamine Kuhjameetodil");
        RadioButton pistemeetod = new RadioButton("Pistemeetod");
        RadioButton põimemeetod = new RadioButton("Põimemeetod");
        RadioButton altÜlesse = new RadioButton("Alt ülesse põimemeetod");
        RadioButton tooEtte = new RadioButton("Valiku Kiirmeetod");

        mullimeetod.setToggleGroup(toggleGroup);
        kiirmeetod.setToggleGroup(toggleGroup);
        valikuMeetod.setToggleGroup(toggleGroup);
        kuhjameetod.setToggleGroup(toggleGroup);
        kuhjasta.setToggleGroup(toggleGroup);
        pistemeetod.setToggleGroup(toggleGroup);
        põimemeetod.setToggleGroup(toggleGroup);
        altÜlesse.setToggleGroup(toggleGroup);
        tooEtte.setToggleGroup(toggleGroup);
        mullimeetod.setSelected(true);

        //Avavaate loomine
        HBox root = new HBox();
        VBox avaVaade = new VBox();
        VBox failiValik = new VBox();
        HBox avaNupud = new HBox();
        avaNupud.getChildren().addAll(alusta, tagasi);
        avaNupud.setSpacing(15);
        avaNupud.setPadding(new Insets(15, 0, 0, 0));
        TextField failiVäli = new TextField(failiNimi);
        failiValik.getChildren().addAll(failiVäli, valiFail);
        failiValik.setSpacing(10);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vali fail");

        mullimeetod.setPadding(new Insets(15, 0, 0, 0));
        avaVaade.getChildren().addAll(failiValik, mullimeetod, valikuMeetod, pistemeetod, kuhjameetod, kuhjasta, põimemeetod, altÜlesse, kiirmeetod, tooEtte, avaNupud);
        avaVaade.setSpacing(5);
        avaVaade.setPadding(new Insets(15, 0, 0, 15));
        root.getChildren().addAll(avaVaade);


        Scene scene = new Scene(root, 250, 380);
        primaryStage.setY(200);
        primaryStage.setX(600);
        primaryStage.setTitle("Automaatkontroll");
        primaryStage.setScene(scene);
        primaryStage.show();


        GridPane lukuNupud = new GridPane();


        valiFail.setOnAction(e -> {
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            fileChooser.setInitialDirectory(new File(currentPath));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                failiVäli.setText(file.getPath());
            }
        });

        /*
         Funktsioon mis loob kasutajale sisestus vaate.
         Annab ka globaalsetele muutujatele korrektsed väärtused
         */
        alusta.setOnAction(actionEvent -> {
            try {
                String meetodiNimi = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                boolean valikuKiirmeetod = Objects.equals(meetodiNimi, "Valiku Kiirmeetod");
                int[] lahendatavJärjend = järjendFailist(failiVäli.getText(), valikuKiirmeetod);
                if (lahendatavJärjend.length == 0) {
                    throw new IllegalArgumentException();
                }
                sorteerimisMeetod = valiMeetod(meetodiNimi, lahendatavJärjend);

                VBox looKasutajaSisend = looKasutajaSisendVaade(lahendatavJärjend, lukuNupud);
                looKasutajaSisend.setPadding(new Insets(15, 0, 0, 15));
                looKasutajaSisend.getChildren().addAll(nupuGrupp, nupuGrupp2, lõpeta);

                GridPane lukud = looLukud(lahendatavJärjend.length);
                lukuNupud.getChildren().addAll(lukud.getChildren());
                lisaLukkudeVahed(lukuNupud, lahendatavJärjend.length);

                alusta.setDisable(true);
                tagasi.setDisable(false);

                toggleGroup.getToggles().forEach(toggle -> {
                    Node node = (Node) toggle;
                    node.setDisable(true);
                });

                root.getChildren().add(looKasutajaSisend);
                primaryStage.setWidth(lahendatavJärjend.length * 80 + 250);
            }
            //Kui kasutajal ei ole vajalikke faile antakse talle sellest dialoogiga teada
            catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Järjendis leidub sümbol mis ei ole number.");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Järjendis pole elemente.");
                alert.showAndWait();
            }
        });

        /*
         Funktsioon mis laseb kasutajal taastada programmi algseisu ilma tulemusfaili loomata
         */
        tagasi.setOnAction(e -> {
            taastaAlgSeisund(failiVäli.getText());
            GUI app = new GUI();
            app.start(primaryStage);
            primaryStage.setWidth(250);
        });

        /*
         Funktsioon mis kopeerib kõik tühjad lahtrid kasutajale järjendi hetkeseisust
         */
        kopeeriTühjad.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane järjend = (GridPane) sisendiVaade.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            kopeeriTühjadLahtrid(järjend, kasutajaLahtrid);
        });

        /*
         Funktsioon, mis laseb kasutajal sisesta algoritmi sammu,
         Kontrollib kas kasutaja on jätnud lünga tühjaks, kirjutanud sinna sümboli mis ei ole number, või kirjutanud
         sinna numbri, mida algses järjendis ei leidu, ning teavitab teda sellest.
         */
        sisesta.setOnAction(actionEvent -> {
            try {

                VBox sisendiVaade = (VBox) root.getChildren().get(1);


                GridPane järjendiSeis = (GridPane) sisendiVaade.getChildren().get(1);
                GridPane sisendLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
                int[] järjend = sisendJärjendiks(järjendiSeis);
                int[] kasutajaSisend = sisendJärjendiks(sisendLahtrid);

                int[] järjendKloon = järjend.clone();
                int[] sisendKloon = kasutajaSisend.clone();
                Arrays.sort(järjendKloon);
                Arrays.sort(sisendKloon);
                if (!Arrays.equals(järjendKloon, sisendKloon)) {
                    throw new IllegalArgumentException();
                }

                boolean viga = kontrolliJärjend(kasutajaSisend);
                if (viga) {
                    ((RadioButton) toggleGroup.getSelectedToggle()).setStyle("-fx-text-fill: red");
                }

                GridPane uuenda = looJärjend(kasutajaSisend, viga);
                sisendiVaade.getChildren().remove(1);
                sisendiVaade.getChildren().add(1, uuenda);

                for (Node child : sisendLahtrid.getChildren()) {
                    TextField text = (TextField) child;
                    if (!text.isDisabled()) {
                        text.setText("");
                    }
                }
                sisendLahtrid.getChildren().get(0).requestFocus();

            } catch (NumberFormatException a) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Vigane sisend lahtris");
                alert.showAndWait();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Sisestati number mida järjendis ei leidu");
                alert.showAndWait();
            }
        });

        /*
         Funktsioon, mis laseb kasutajal minna tagasi, kui ta märkab, et ta on teinud vea
         */
        eelmine.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane järjendiSeis = (GridPane) sisendiVaade.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);

            ((RadioButton) toggleGroup.getSelectedToggle()).setStyle("-fx-text-fill: grey");
            sorteerimisMeetod.sammTagasi();
            kasutajaSisendid.getChildren().remove(kasutajaSisendid.getChildren().size() - 1);

            try {
                for (int i = 0; i < sorteerimisMeetod.getJärjend().length; i++) {
                    TextField lahter = (TextField) järjendiSeis.getChildren().get(i);
                    lahter.setText(String.valueOf(sorteerimisMeetod.getJärjend()[i]));
                    lahter.setStyle("-fx-text-fill: grey");
                }
                for (Node child : kasutajaLahtrid.getChildren()) {
                    TextField text = (TextField) child;
                    text.setText("");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Eelmist sammu ei leidu");
                alert.showAndWait();
            }


        });

        /*
         Funktsioon, mis laseb lahtreid ükshaaval lukku panna ja lukust lahti teha
         */
        lukuNupud.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            String clickedClass = mouseEvent.getTarget().getClass().getTypeName();
            if (clickedClass.equals("javafx.scene.control.Button")) {
                Node node = (Node) mouseEvent.getTarget();
                int indeks = GridPane.getColumnIndex(node);
                TextField textField = (TextField) kasutajaLahtrid.getChildren().get(indeks);
                textField.setDisable(!textField.isDisabled());
            }
        });

        /*
         Funktsioon, mis teeb kõik lahtrid lukust lahti
         */
        vabasta.setOnAction(actionEvent -> {
            VBox sisendiVaade = (VBox) root.getChildren().get(1);
            GridPane kasutajaLahtrid = (GridPane) sisendiVaade.getChildren().get(3);
            for (Node child : kasutajaLahtrid.getChildren()) {
                TextField text = (TextField) child;
                text.setDisable(false);
            }
        });

        /*
         Funktsioon, mis loob tulemusfaili ning viib programmi tagasi algkujule.
         Nime ei taastata.
         */
        lõpeta.setOnAction(actionEvent -> {
            GUI app = new GUI();
            app.start(primaryStage);
            kuvaTulemus();
            taastaAlgSeisund(failiVäli.getText());
            primaryStage.setWidth(250);
        });
    }
}


