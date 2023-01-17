package com.example.abct;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AlbionBuildCreationTool extends Application {

    String css = this.getClass().getResource("layout.css").toExternalForm();
    Stage mainStage = new Stage();
    private Stage helpWindowStage;
    @FXML
    FlowPane selectionMenu;
    @FXML
    ScrollPane scrollPaneBuilds;
    @FXML
    HBox hboxPlaceBuildsHere;
    @FXML
    VBox vboxBuildsBuilds;
    @FXML
    Button btnSetStyles,btnResetStyles,btnHelp;
    @FXML
    TextField tfBackgroundColor,tfBuildBoxColor,tfBorderColor,tfBorderWidth;
    static File[] filenames; //t8 files are the first 195, t7 files are t8.index +195
    static Image[] imageArray;
    static ZipEntry entryArray[];
    static ImageView[] imageViewArray;
    static HBox[] buildHboxArray;
    static int numFiles = 0;
    static int buildIndex = 0;
    static int sceneWidth = 1400;
    static int sceneHeight = 900;
    static boolean helmetSwapActive = false;
    static boolean armorSwapActive = false;
    static boolean bootsSwapActive = false;
    static Items[] itemArray;
    static int drawEnable[];
    static int drawDisable[];
    public static boolean makeBuildFirstMethodCall = true;
    public boolean weaponIsOneHanded = false;
    static Build buildArray[] = new Build[1];
    static Color lightBlue = Color.web("#BCD2E8");
    static Color darkBlue = Color.web("#1E3F66");
    static Color medBlue = Color.web("#2E5984");
    @FXML
    CheckBox checkBoxWeapons,checkBoxOffhand,checkBoxArmor,checkBoxZvz,checkBoxSs,checkBoxGanking,
            checkBoxOther,checkBoxTier7,checkBoxHelmetSwap,checkBoxArmorSwap,checkBoxBootsSwap = new CheckBox();
    Boolean filterWeapons,filterZvz = true;
    Boolean filterSs,filterGanking,filterOther,filterTier7,filterOffhands,filterArmor = false;




    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        Scene scene = new Scene(root,1400,900);
        mainStage.setTitle("Albion Online Build Creation Tool");
        URL url = AlbionBuildCreationTool.class.getResource("misc/Golden Sextant.png");
        Image iconImage = new Image(url.openStream());
        mainStage.getIcons().add(iconImage);
        sceneWidth = (int) scene.getWidth();
        sceneHeight = (int) scene.getHeight();
        scene.getStylesheets().add(css);
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.setOnCloseRequest(e -> Platform.exit());

    }

    public static void main(String[] args) throws IOException {
        int i = 0;
        //File folder = new File("src/main/resources/com/example/abct/images");

        CodeSource src = AlbionBuildCreationTool.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while(true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if ((name.startsWith("com/example/abct/images")) && (name.endsWith(".png")))
                {
                    System.out.println("current entry: " + numFiles + "  name: " + name);
                    numFiles++;

                }
            }
        }
        else {
            System.out.println("LOAD FAILED");
        }
        filenames = new File[numFiles];
        entryArray = new ZipEntry[numFiles];
        int counter = 0;
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while(true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if ((name.startsWith("com/example/abct/images")) && (name.endsWith(".png"))) {

                    System.out.println("22222 current entry: "+numFiles+"  name: "+name);
                    filenames[counter] = new File(name);
                    entryArray[counter] = new ZipEntry(e);
                    System.out.println("entry array: "+entryArray[counter].getName());
                    System.out.println("filenames path: "+filenames[counter].getPath());
                    counter++;
                }
            }
        }
        else {
            System.out.println("22222 LOAD FAILED");
        }
        System.out.println("numFiles == "+numFiles);




/*
        File folder = new File("src/main/resources/images/");
        File[] listOfFiles = folder.listFiles();
        numFiles = 0;
        for(int tempNumFiles = 0; tempNumFiles < listOfFiles.length; tempNumFiles++){
            if(listOfFiles[tempNumFiles].getName().endsWith(".png")){
                numFiles++;
            }
        }
        filenames = new File[numFiles];
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                filenames[i] = file; //populates array
                i++;
            }
        }*/
        imageArray = new Image[numFiles];
        imageViewArray = new ImageView[numFiles];
        itemArray = new Items[numFiles];
        buildHboxArray = new HBox[buildArray.length];
        drawDisable = new int[numFiles];
        drawEnable = new int[numFiles];  //give size to arrays

        for (i = 0; i < buildArray.length; i++){
            buildArray[i] = new Build(); //for builds in grid
            buildHboxArray[i] = new HBox();
        }

        i = 0;
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while(true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if ((name.startsWith("com/example/abct/images")) && (name.endsWith(".png"))) {


                    URL url = AlbionBuildCreationTool.class.getResource("images/"+filenames[i].getName());
                    System.out.println(url.toString());
                    Image currentImage = new Image(url.openStream());
                    imageArray[i] = currentImage;
                    imageViewArray[i] = new ImageView(imageArray[i]);
                    imageViewArray[i].setFitHeight(80);
                    imageViewArray[i].setFitWidth(80);

                    itemArray[i] = new Items();
                    itemArray[i].itemId = i;
                    System.out.println("LOAD COMPLETE "+i);
                    i++;
                }
            }
        }
        else {
            System.out.println("22222 LOAD FAILED");
        }






        for (i = 0; i < numFiles; i++){
            /*
            Image currentImage = new Image(AlbionBuildCreationTool.class.getResource("images/"+filenames[i].getName()).openStream());
            imageArray[i] = currentImage;
            imageViewArray[i] = new ImageView(entryArray[i].getName());
            imageViewArray[i].setFitHeight(80);
            imageViewArray[i].setFitWidth(80);

            itemArray[i] = new Items();
            itemArray[i].itemId = i;

         */

            //-----------------------BASIC TIERS------------------------//
            if(filenames[i].getName().toString().startsWith("Elder")){ //item tiers
                itemArray[i].tier = 8;
            } else if (filenames[i].getName().toString().startsWith("Grandmaster")) {
                itemArray[i].tier = 7;
            }
            //-----------------------ARMOR------------------------//
            if(filenames[i].getName().toString().endsWith("Helmet.png")
                    || filenames[i].getName().toString().startsWith("Elder's Helmet")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Helmet")){
                itemArray[i].isArmor = true; //helmets
                itemArray[i].isHelmet = true;
            }
            if(filenames[i].getName().toString().endsWith("Hood.png")
                    || filenames[i].getName().toString().startsWith("Elder's Hood")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Hood")){
                itemArray[i].isArmor = true; //hoods
                itemArray[i].isHelmet = true;
            }
            if(filenames[i].getName().toString().endsWith("Cowl.png")
                    || filenames[i].getName().toString().endsWith("Hat.png")
                    || filenames[i].getName().toString().startsWith("Elder's Cowl")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Cowl")){
                itemArray[i].isArmor = true; //cowls
                itemArray[i].isHelmet = true;
            }
            if(filenames[i].getName().toString().endsWith("Armor.png")
                    || filenames[i].getName().toString().startsWith("Elder's Armor")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Armor")){
                itemArray[i].isArmor = true; //armor
                itemArray[i].isChestpiece = true;
            }
            if(filenames[i].getName().toString().endsWith("Jacket.png")
                    || filenames[i].getName().toString().startsWith("Elder's Jacket")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Jacket")){
                itemArray[i].isArmor = true; //jackets
                itemArray[i].isChestpiece = true;
            }
            if(filenames[i].getName().toString().endsWith("Robe.png")
                    || filenames[i].getName().toString().startsWith("Elder's Robe")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Robe")){
                itemArray[i].isArmor = true; //robes
                itemArray[i].isChestpiece = true;
            }
            if(filenames[i].getName().toString().endsWith("Boots.png")
                    || filenames[i].getName().toString().startsWith("Elder's Boots")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Boots")){
                itemArray[i].isArmor = true; //boots
                itemArray[i].isBoots = true;
            }
            if(filenames[i].getName().toString().endsWith("Shoes.png")
                    || filenames[i].getName().toString().startsWith("Elder's Shoes")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Shoes")){
                itemArray[i].isArmor = true; //shoes
                itemArray[i].isBoots = true;
            }
            if(filenames[i].getName().toString().endsWith("Sandals.png")
                    || filenames[i].getName().toString().startsWith("Elder's Sandals")
                    || filenames[i].getName().toString().startsWith("Grandmaster's Sandals")){
                itemArray[i].isArmor = true; //sandals
                itemArray[i].isBoots = true;
            }
            //-----------------------OTHER------------------------//
            if(filenames[i].getName().toString().startsWith("Major Energy")
                    || filenames[i].getName().toString().startsWith("Major Healing")){ //item tiers
                itemArray[i].tier = 7; //in game it's 6 but for logic it's 7 here
                itemArray[i].isOther = true;
            } else if (filenames[i].getName().toString().startsWith("Major Gigantify")
                    || filenames[i].getName().toString().startsWith("Major Resistance")
                    || filenames[i].getName().toString().startsWith("Major Gigantify")
                    || filenames[i].getName().toString().startsWith("Major Sticky")
                    || filenames[i].getName().toString().endsWith("Omelette.png")
                    || filenames[i].getName().toString().endsWith("Catfish.png")) {
                itemArray[i].tier = 7;
                itemArray[i].isOther = true;
            } else if (filenames[i].getName().toString().startsWith("Major Poison")
                    || filenames[i].getName().toString().endsWith("Stew.png")
                    || filenames[i].getName().toString().endsWith("Sturgeon.png")){
                itemArray[i].tier = 8;
                itemArray[i].isOther = true; //other
            }
            if(filenames[i].getName().toString().endsWith("Cape.png")){
                itemArray[i].isOther = true; //capes
                itemArray[i].isCape = true;
            }
            if(filenames[i].getName().toString().endsWith("Stew.png")
                    || filenames[i].getName().toString().endsWith("Omelette.png")
                    || filenames[i].getName().toString().endsWith("Sturgeon.png")
                    || filenames[i].getName().toString().endsWith("Catfish.png")){
                itemArray[i].isFood = true;
            }
            if(filenames[i].getName().toString().endsWith("Potion.png")
                    || filenames[i].getName().toString().startsWith("Major")){
                itemArray[i].isPotion = true;
            }
            //-----------------------OFFHANDS------------------------//
            if(filenames[i].getName().toString().endsWith("Shield.png")
                    || filenames[i].getName().toString().endsWith("Aegis.png")
                    || filenames[i].getName().toString().endsWith("Censer.png")
                    || filenames[i].getName().toString().endsWith("Cryptcandle.png")
                    || filenames[i].getName().toString().endsWith("Facebreaker.png")
                    || filenames[i].getName().toString().endsWith("Secrets.png")
                    || filenames[i].getName().toString().endsWith("Cane.png")
                    || filenames[i].getName().toString().endsWith("Muisak.png")
                    || filenames[i].getName().toString().endsWith("Sarcophagus.png")
                    || filenames[i].getName().toString().endsWith("Scepter.png")
                    || filenames[i].getName().toString().endsWith("Taproot.png")
                    || filenames[i].getName().toString().endsWith("Spells.png")
                    || filenames[i].getName().toString().endsWith("Mistcaller.png")
                    || filenames[i].getName().toString().endsWith("Torch.png")){
                itemArray[i].isOffhand = true; //offhands
            }
            //-----------------------WEAPONS------------------------//
            if ((itemArray[i].isOffhand == false)
                    && (itemArray[i].isOther == false)
                    && (itemArray[i].isArmor == false)){
                itemArray[i].isWeapon = true; //remainder are weapons
            }
            //-----------------------GANKING------------------------//
            if(filenames[i].getName().toString().endsWith("Claws.png")
                    || filenames[i].getName().toString().endsWith("Bracers.png")
                    || filenames[i].getName().toString().endsWith("Paws.png")
                    || filenames[i].getName().toString().endsWith("Bloodletter.png")
                    || filenames[i].getName().toString().endsWith("Boltcasters.png")
                    || filenames[i].getName().toString().endsWith("Bow.png")
                    || filenames[i].getName().toString().endsWith("Brawler Gloves.png")
                    || filenames[i].getName().toString().endsWith("Chillhowl.png")
                    || filenames[i].getName().toString().endsWith("'s Crossbow.png")
                    || filenames[i].getName().toString().endsWith("'s Cursed Staff'.png")
                    || filenames[i].getName().toString().endsWith("Dagger Pair.png")
                    || filenames[i].getName().toString().endsWith("'s Dagger.png")
                    || filenames[i].getName().toString().endsWith("Deathgivers.png")
                    || filenames[i].getName().toString().endsWith("Bladed Staff.png")
                    || filenames[i].getName().toString().endsWith("Divine Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Frost Staff.png")
                    || filenames[i].getName().toString().endsWith("Glaive.png")
                    || filenames[i].getName().toString().endsWith("'s Fire Staff.png")
                    || filenames[i].getName().toString().endsWith("Heron Spear.png")
                    || filenames[i].getName().toString().endsWith("Hoarfrost Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Holy Staff.png")
                    || filenames[i].getName().toString().endsWith("Lifetouch Staff.png")
                    || filenames[i].getName().toString().endsWith("Ironroot Staff.png")
                    || filenames[i].getName().toString().endsWith("Infernal Staff.png")
                    || filenames[i].getName().toString().endsWith("Druidic Staff.png")
                    || filenames[i].getName().toString().endsWith("Great Nature Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Nature Staff.png")
                    || filenames[i].getName().toString().endsWith("Pike.png")
                    || filenames[i].getName().toString().endsWith("Hellfire Hands.png")
                    || filenames[i].getName().toString().endsWith("'s Spear.png")
                    || filenames[i].getName().toString().endsWith("Whispering Bow.png")
                    || filenames[i].getName().toString().endsWith("Tombhammer.png")
                    || filenames[i].getName().toString().endsWith("Warbow.png")){
                itemArray[i].isGanking = true; //ganking
            }
            //-----------------------ZvZ------------------------//
            if(filenames[i].getName().toString().endsWith("Bloodletter.png")
                    || filenames[i].getName().toString().endsWith("'s Arcane Staff.png")
                    || filenames[i].getName().toString().endsWith("Bedrock Mace.png")
                    || filenames[i].getName().toString().endsWith("Blazing Staff.png")
                    || filenames[i].getName().toString().endsWith("Blight Staff.png")
                    || filenames[i].getName().toString().endsWith("Badon.png")
                    || filenames[i].getName().toString().endsWith("Brimstone Staff.png")
                    || filenames[i].getName().toString().endsWith("Camlann Mace.png")
                    || filenames[i].getName().toString().endsWith("Damnation Staff.png")
                    || filenames[i].getName().toString().endsWith("Demonfang.png")
                    || filenames[i].getName().toString().endsWith("Dawnsong.png")
                    || filenames[i].getName().toString().endsWith("Shaper.png")
                    || filenames[i].getName().toString().endsWith("Enigmatic Staff.png")
                    || filenames[i].getName().toString().endsWith("Fallen Staff.png")
                    || filenames[i].getName().toString().endsWith("Galatine Pair.png")
                    || filenames[i].getName().toString().endsWith("Glacial Staff.png")
                    || filenames[i].getName().toString().endsWith("Grailseeker.png")
                    || filenames[i].getName().toString().endsWith("Great Arcane Staff.png")
                    || filenames[i].getName().toString().endsWith("Grovekeeper.png")
                    || filenames[i].getName().toString().endsWith("Justice.png")
                    || filenames[i].getName().toString().endsWith("Icicle Staff.png")
                    || filenames[i].getName().toString().endsWith("Kingmaker.png")
                    || filenames[i].getName().toString().endsWith("Lifecurse Staff.png")
                    || filenames[i].getName().toString().endsWith("Longbow.png")
                    || filenames[i].getName().toString().endsWith("'s Mace.png")
                    || filenames[i].getName().toString().endsWith("Locus.png")
                    || filenames[i].getName().toString().endsWith("Mistpiercer.png")
                    || filenames[i].getName().toString().endsWith("Morning Star.png")
                    || filenames[i].getName().toString().endsWith("Oathkeepers.png")
                    || filenames[i].getName().toString().endsWith("Occult Staff.png")
                    || filenames[i].getName().toString().endsWith("Prism.png")
                    || filenames[i].getName().toString().endsWith("Rampant Staff.png")
                    || filenames[i].getName().toString().endsWith("Cestus.png")
                    || filenames[i].getName().toString().endsWith("Realmbreaker.png")
                    || filenames[i].getName().toString().endsWith("Siegebow.png")
                    || filenames[i].getName().toString().endsWith("Soulscythe.png")
                    || filenames[i].getName().toString().endsWith("Spirithunter.png")
                    || filenames[i].getName().toString().endsWith("Wailing Bow.png")
                    || filenames[i].getName().toString().endsWith("Repeater.png")
                    || filenames[i].getName().toString().endsWith("Wildfire Staff.png")
                    || filenames[i].getName().toString().endsWith("Witchwork Staff.png")){
                itemArray[i].isZvz = true; //zvz
            }
            //-----------------------small-scale------------------------//
            if(filenames[i].getName().toString().endsWith("Bloodletter.png")
                    || filenames[i].getName().toString().endsWith("Blight Staff.png")
                    || filenames[i].getName().toString().endsWith("Demonfang.png")
                    || filenames[i].getName().toString().endsWith("Dawnsong.png")
                    || filenames[i].getName().toString().endsWith("Enigmatic Staff.png")
                    || filenames[i].getName().toString().endsWith("Fallen Staff.png")
                    || filenames[i].getName().toString().endsWith("Great Arcane Staff.png")
                    || filenames[i].getName().toString().endsWith("Justice.png")
                    || filenames[i].getName().toString().endsWith("Kingmaker.png")
                    || filenames[i].getName().toString().endsWith("'s Mace.png")
                    || filenames[i].getName().toString().endsWith("Oathkeepers.png")
                    || filenames[i].getName().toString().endsWith("Prism.png")
                    || filenames[i].getName().toString().endsWith("Realmbreaker.png")
                    || filenames[i].getName().toString().endsWith("Spirithunter.png")
                    || filenames[i].getName().toString().endsWith("Paws.png")
                    || filenames[i].getName().toString().endsWith("Wildfire Staff.png")){
                itemArray[i].isSs = true; //small-scale
            }
            //-----------------------ONE HANDED WEAPONS------------------------//
            if(filenames[i].getName().toString().endsWith("Bloodletter.png")
                    || filenames[i].getName().toString().endsWith("Arcane Staff.png")
                    || filenames[i].getName().toString().endsWith("Demonfang.png")
                    || filenames[i].getName().toString().endsWith("Battleaxe.png")
                    || filenames[i].getName().toString().endsWith("Bedrock Mace.png")
                    || filenames[i].getName().toString().endsWith("Broadsword.png")
                    || filenames[i].getName().toString().endsWith("Clarent Blade.png")
                    || filenames[i].getName().toString().endsWith("Chillhowl.png")
                    || filenames[i].getName().toString().endsWith("'s Cursed Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Dagger.png")
                    || filenames[i].getName().toString().endsWith("Daybreaker.png")
                    || filenames[i].getName().toString().endsWith("Druidic Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Fire Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Frost Staff.png")
                    || filenames[i].getName().toString().endsWith("Hallowfall.png")
                    || filenames[i].getName().toString().endsWith("'s Hammer.png")
                    || filenames[i].getName().toString().endsWith("Heron Spear.png")
                    || filenames[i].getName().toString().endsWith("Hoarfrost Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Holy Staff.png")
                    || filenames[i].getName().toString().endsWith("Incubus Mace.png")
                    || filenames[i].getName().toString().endsWith("Ironroot Staff.png")
                    || filenames[i].getName().toString().endsWith("Lifecurse Staff.png")
                    || filenames[i].getName().toString().endsWith("Lifetouch Staff.png")
                    || filenames[i].getName().toString().endsWith("Light Crossbow.png")
                    || filenames[i].getName().toString().endsWith("'s Mace.png")
                    || filenames[i].getName().toString().endsWith("'s Nature Staff.png")
                    || filenames[i].getName().toString().endsWith("Shadowcaller.png")
                    || filenames[i].getName().toString().endsWith("'s Spear.png")
                    || filenames[i].getName().toString().endsWith("'s Wildfire Staff.png")
                    || filenames[i].getName().toString().endsWith("'s Witchwork Staff.png")){
                itemArray[i].isOneHanded = true; //one handed weapons
            }
            //-----------------------ZVZ ARMOR------------------------//
            if(filenames[i].getName().toString().endsWith("Armor of Valor.png")
                    || filenames[i].getName().toString().endsWith("Assassin Hood.png")
                    || filenames[i].getName().toString().endsWith("Boots of Valor.png")
                    || filenames[i].getName().toString().endsWith("Cleric Cowl.png")
                    || filenames[i].getName().toString().endsWith("Cleric Robe.png")
                    || filenames[i].getName().toString().endsWith("Cowl of Purity.png")
                    || filenames[i].getName().toString().endsWith("Demon Armor.png")
                    || filenames[i].getName().toString().endsWith("Duskweaver Armor.png")
                    || filenames[i].getName().toString().endsWith("Duskweaver Boots.png")
                    || filenames[i].getName().toString().endsWith("Feyscale Hat.png")
                    || filenames[i].getName().toString().endsWith("Feyscale Sandals.png")
                    || filenames[i].getName().toString().endsWith("Graveguard Boots.png")
                    || filenames[i].getName().toString().endsWith("Guardian Armor.png")
                    || filenames[i].getName().toString().endsWith("Hellion Jacket.png")
                    || filenames[i].getName().toString().endsWith("Hunter Shoes.png")
                    || filenames[i].getName().toString().endsWith("Judicator Armor.png")
                    || filenames[i].getName().toString().endsWith("Judicator Helmet.png")
                    || filenames[i].getName().toString().endsWith("Knight Armor.png")
                    || filenames[i].getName().toString().endsWith("Knight Helmet.png")
                    || filenames[i].getName().toString().endsWith("Mistwalker Hood.png")
                    || filenames[i].getName().toString().endsWith("Mistwalker Jacket.png")
                    || filenames[i].getName().toString().endsWith("Robe of Purity.png")
                    || filenames[i].getName().toString().endsWith("Royal Hood.png")
                    || filenames[i].getName().toString().endsWith("Royal Jacket.png")
                    || filenames[i].getName().toString().endsWith("Royal Sandals.png")
                    || filenames[i].getName().toString().endsWith("Scholar Robe.png")
                    || filenames[i].getName().toString().endsWith("Scholar Sandals.png")
                    || filenames[i].getName().toString().endsWith("Soldier Armor.png")
                    || filenames[i].getName().toString().endsWith("Stalker Hood.png")
                    || filenames[i].getName().toString().endsWith("Stalker Jacket.png")
                    //capes
                    || filenames[i].getName().toString().endsWith("Lymhurst Cape.png")
                    || filenames[i].getName().toString().endsWith("Martlock Cape.png")
                    || filenames[i].getName().toString().endsWith("Fort Sterling Cape.png")
                    || filenames[i].getName().toString().endsWith("Morgana Cape.png")
                    //food and pots
                    || filenames[i].getName().toString().endsWith("Beef Stew.png")
                    || filenames[i].getName().toString().endsWith("Eel Stew.png")
                    || filenames[i].getName().toString().endsWith("Pork Omelette.png")
                    || filenames[i].getName().toString().endsWith("Resistance Potion.png")
                    || filenames[i].getName().toString().endsWith("Gigantify Potion.png")
                    //OFFHANDS
                    || filenames[i].getName().toString().endsWith("Facebreaker.png")
                    || filenames[i].getName().toString().endsWith("Muisak.png")
                    || filenames[i].getName().toString().endsWith("Aegis.png")
                    || filenames[i].getName().toString().endsWith("Mistcaller.png")) {
                itemArray[i].isZvz = true; //zvz armor
            }
            //-----------------------GANKING ARMOR------------------------//
            if(filenames[i].getName().toString().endsWith("Assassin Jacket.png")
                    || filenames[i].getName().toString().endsWith("Cultist Cowl.png")
                    || filenames[i].getName().toString().endsWith("Cultist Robe.png")
                    || filenames[i].getName().toString().endsWith("Demon Boots.png")
                    || filenames[i].getName().toString().endsWith("Demon Helmet.png")
                    || filenames[i].getName().toString().endsWith("Duskweaver Helmet.png")
                    || filenames[i].getName().toString().endsWith("Fiend Cowl.png")
                    || filenames[i].getName().toString().endsWith("Fiend Robe.png")
                    || filenames[i].getName().toString().endsWith("Graveguard Armor.png")
                    || filenames[i].getName().toString().endsWith("Hellion Hood.png")
                    || filenames[i].getName().toString().endsWith("Hunter Hood.png")
                    || filenames[i].getName().toString().endsWith("Hunter Jacket.png")
                    || filenames[i].getName().toString().endsWith("Mercenary Hood.png")
                    || filenames[i].getName().toString().endsWith("Mercenary Jacket.png")
                    || filenames[i].getName().toString().endsWith("Mage Cowl.png")
                    || filenames[i].getName().toString().endsWith("Mage Robe.png")
                    || filenames[i].getName().toString().endsWith("Royal Boots.png")
                    || filenames[i].getName().toString().endsWith("Royal Helmet.png")
                    || filenames[i].getName().toString().endsWith("Soldier Boots.png")
                    || filenames[i].getName().toString().endsWith("Specter Shoes.png")
                    //capes
                    || filenames[i].getName().toString().endsWith("Undead Cape.png")
                    || filenames[i].getName().toString().endsWith("Caerleon Cape.png")
                    || filenames[i].getName().toString().endsWith("Heretic Cape.png")
                    || filenames[i].getName().toString().endsWith("Demon Cape.png")
                    || filenames[i].getName().toString().endsWith("Bridgewatch Cape.png")
                    || filenames[i].getName().toString().endsWith("Thetford Cape.png")
                    || filenames[i].getName().toString().endsWith("Keeper Cape.png")
                    || filenames[i].getName().toString().endsWith("'s Cape.png")
                    //multi-role
                    || filenames[i].getName().toString().endsWith("Royal Sandals.png")
                    || filenames[i].getName().toString().endsWith("Hunter Shoes.png")
                    || filenames[i].getName().toString().endsWith("Cleric Robe.png")
                    || filenames[i].getName().toString().endsWith("Stalker Hood.png")
                    || filenames[i].getName().toString().endsWith("Cowl of Purity.png")
                    || filenames[i].getName().toString().endsWith("Stalker Jacket.png")
                    || filenames[i].getName().toString().endsWith("Royal Sandals.png")
                    //food and pots
                    || filenames[i].getName().toString().endsWith("Poison Potion.png")
                    || filenames[i].getName().toString().endsWith("Sticky Potion.png")
                    || filenames[i].getName().toString().endsWith("Sturgeon.png")
                    || filenames[i].getName().toString().endsWith("Healing Potion.png")
                    || filenames[i].getName().toString().endsWith("Catfish.png")
                    //OFFHANDS
                    || filenames[i].getName().toString().endsWith("Cryptcandle.png")
                    || filenames[i].getName().toString().endsWith("Torch.png")
                    || filenames[i].getName().toString().endsWith("Taproot.png")
                    || filenames[i].getName().toString().endsWith("Caitiff Shield.png")
                    || filenames[i].getName().toString().endsWith("Mistcaller.png")
                    || filenames[i].getName().toString().endsWith("Leering Cane.png")
                    || filenames[i].getName().toString().endsWith("'s Shield.png")
                    || filenames[i].getName().toString().endsWith("Sacred Scepter.png")
                    || filenames[i].getName().toString().endsWith("Sarcophagus.png")) {
                itemArray[i].isGanking = true; //ganking armor
            }

            //-----------------------Small Scale ARMOR------------------------//
            if(filenames[i].getName().toString().endsWith("Assassin Hood.png")
                    || filenames[i].getName().toString().endsWith("Cleric Cowl.png")
                    || filenames[i].getName().toString().endsWith("Cleric Robe.png")
                    || filenames[i].getName().toString().endsWith("Demon Armor.png")
                    || filenames[i].getName().toString().endsWith("Duskweaver Armor.png")
                    || filenames[i].getName().toString().endsWith("Duskweaver Boots.png")
                    || filenames[i].getName().toString().endsWith("Feyscale Hat.png")
                    || filenames[i].getName().toString().endsWith("Feyscale Sandals.png")
                    || filenames[i].getName().toString().endsWith("Guardian Armor.png")
                    || filenames[i].getName().toString().endsWith("Hellion Jacket.png")
                    || filenames[i].getName().toString().endsWith("Hunter Shoes.png")
                    || filenames[i].getName().toString().endsWith("Judicator Armor.png")
                    || filenames[i].getName().toString().endsWith("Judicator Helmet.png")
                    || filenames[i].getName().toString().endsWith("Knight Helmet.png")
                    || filenames[i].getName().toString().endsWith("Mistwalker Hood.png")
                    || filenames[i].getName().toString().endsWith("Mistwalker Jacket.png")
                    || filenames[i].getName().toString().endsWith("Robe of Purity.png")
                    || filenames[i].getName().toString().endsWith("Royal Sandals.png")
                    || filenames[i].getName().toString().endsWith("Scholar Robe.png")
                    || filenames[i].getName().toString().endsWith("Scholar Sandals.png")
                    || filenames[i].getName().toString().endsWith("Soldier Armor.png")
                    || filenames[i].getName().toString().endsWith("Stalker Hood.png")
                    || filenames[i].getName().toString().endsWith("Stalker Jacket.png")
                    //capes
                    || filenames[i].getName().toString().endsWith("Lymhurst Cape.png")
                    || filenames[i].getName().toString().endsWith("Martlock Cape.png")
                    || filenames[i].getName().toString().endsWith("Fort Sterling Cape.png")
                    || filenames[i].getName().toString().endsWith("Morgana Cape.png")
                    || filenames[i].getName().toString().endsWith("Thetford Cape.png")
                    //food and pots
                    || filenames[i].getName().toString().endsWith("Beef Stew.png")
                    || filenames[i].getName().toString().endsWith("Eel Stew.png")
                    || filenames[i].getName().toString().endsWith("Pork Omelette.png")
                    || filenames[i].getName().toString().endsWith("Resistance Potion.png")
                    || filenames[i].getName().toString().endsWith("Gigantify Potion.png")
                    //OFFHANDS
                    || filenames[i].getName().toString().endsWith("Facebreaker.png")
                    || filenames[i].getName().toString().endsWith("Muisak.png")
                    || filenames[i].getName().toString().endsWith("Aegis.png")
                    || filenames[i].getName().toString().endsWith("Mistcaller.png")
                    || filenames[i].getName().toString().endsWith("Taproot.png")
                    || filenames[i].getName().toString().endsWith("Cryptcandle.png")) {
                itemArray[i].isSs = true; //ss armor
            }

            if((itemArray[i].isZvz == false) && (itemArray[i].isGanking == false)) {
                    itemArray[i].isSs = true; //add unassigned to small-scale as well
            }
            //can add debug check here to troubleshoot files
        }
        System.out.println("Num Files: "+numFiles);
        populateBuildsWithDefaults();


        launch();


    }

    @FXML
    protected void initialize() { //builds the selection menu and grid with builds
        initializeVboxBuilds();
        selectionMenu.getChildren().clear();
        drawRules();
        addToSelectionMenu(); //builds selection menu
        if(!makeBuildFirstMethodCall) {
            try {
                makeBuild(buildIndex, weaponIsOneHanded);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        drawBuild();
        updateCssNoArgs();
        takeScreenshot();

    }

    protected void drawBuild() {
        hboxPlaceBuildsHere.getChildren().clear();
        hboxPlaceBuildsHere.getChildren().add(buildHboxArray[buildIndex]);
    }
    protected void initializeVboxBuilds(){
        vboxBuildsBuilds.setBackground(new Background(new BackgroundFill(medBlue,null,null)));
        vboxBuildsBuilds.setAlignment(Pos.CENTER);
        VBox.setVgrow(hboxPlaceBuildsHere,Priority.NEVER);
        hboxPlaceBuildsHere.setBackground(new Background(new BackgroundFill(medBlue,null,null)));
        hboxPlaceBuildsHere.setAlignment(Pos.CENTER);
        HBox.setHgrow(buildHboxArray[buildIndex],Priority.NEVER);
    }

    protected void takeScreenshot(){ //screenshots the current build and copies it to clipboard
        WritableImage screenshot = buildHboxArray[buildIndex].snapshot(new SnapshotParameters(), null);
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putImage(screenshot);
        clipboard.setContent(content);
    }



    protected void drawRules(){ //determines what should be displayed in selection menu based on check boxes
        int i;
        for(i=0; i<numFiles; i++){
            drawEnable[i] = 0;
            drawDisable[i] = 0;
        }
        for(i=0; i<numFiles; i++){
            if((itemArray[i].tier == 8 && checkBoxTier7.isSelected())
                        || (itemArray[i].tier == 7 && !checkBoxTier7.isSelected())){
                    drawDisable[i] = 1;
            }
            if(itemArray[i].isWeapon && checkBoxWeapons.isSelected()){
                if((itemArray[i].isZvz && checkBoxZvz.isSelected())
                        || (itemArray[i].isSs && checkBoxSs.isSelected())
                        || (itemArray[i].isGanking && checkBoxGanking.isSelected())){
                    drawEnable[i] = 1;
                }else if (!checkBoxZvz.isSelected() && !checkBoxSs.isSelected() && !checkBoxGanking.isSelected()){
                    drawEnable[i] = 1;
                }
            }
            if((itemArray[i].isOffhand && checkBoxOffhand.isSelected())) {
                if ((itemArray[i].isZvz && checkBoxZvz.isSelected())
                        || (itemArray[i].isSs && checkBoxSs.isSelected())
                        || (itemArray[i].isGanking && checkBoxGanking.isSelected())) {
                    drawEnable[i] = 1;
                }else if (!checkBoxZvz.isSelected() && !checkBoxSs.isSelected() && !checkBoxGanking.isSelected()){
                    drawEnable[i] = 1;
                }
            }
            if(itemArray[i].isArmor && checkBoxArmor.isSelected()){
                if((itemArray[i].isZvz && checkBoxZvz.isSelected())
                        || (itemArray[i].isSs && checkBoxSs.isSelected())
                        || (itemArray[i].isGanking && checkBoxGanking.isSelected())){
                    drawEnable[i] = 1;
                }else if (!checkBoxZvz.isSelected() && !checkBoxSs.isSelected() && !checkBoxGanking.isSelected()){
                    drawEnable[i] = 1;
                }
            }
            if(itemArray[i].isOther && checkBoxOther.isSelected()){
                if((itemArray[i].isZvz && checkBoxZvz.isSelected())
                        || (itemArray[i].isSs && checkBoxSs.isSelected())
                        || (itemArray[i].isGanking && checkBoxGanking.isSelected())){
                    drawEnable[i] = 1;
                }else if (!checkBoxZvz.isSelected() && !checkBoxSs.isSelected() && !checkBoxGanking.isSelected()){
                    drawEnable[i] = 1;
                }
            }
            if((itemArray[i].isZvz && checkBoxZvz.isSelected())
                    || (itemArray[i].isSs && checkBoxSs.isSelected())
                    || (itemArray[i].isGanking && checkBoxGanking.isSelected())) {
                if (!checkBoxWeapons.isSelected()
                        && !checkBoxOffhand.isSelected()
                        && !checkBoxArmor.isSelected()
                        && !checkBoxOther.isSelected()) {
                    drawEnable[i] = 1;
                }
            }

        }
    }

    @FXML
    protected void addToSelectionMenu(){ //draws components
        for(int i=0; i<numFiles; i++) {
            if (drawEnable[i] == 1 && drawDisable[i] != 1) {
                imageViewArray[i].setFitHeight(80);
                imageViewArray[i].setFitWidth(80);
                selectionMenu.getChildren().add(imageViewArray[i]);
                addMouseListenersToSelectionMenu(i);

            }
        }

    }
    public static void populateBuildsWithDefaults() throws IOException {
        for(int i = 0; i < buildArray.length; i++) {
            makeBuild(i,true);
        }
    }
    @FXML
    public static void makeBuild(int i,boolean isOneHanded) throws IOException {
        boolean isOneHandedDefault = true;

        int smallWidth = 60;
        int smallHeight = 60;
        int bigWidth = 100;
        int bigHeight = 100;

        StackPane weaponAndOffhandStackPane = new StackPane(); //stack pane holds weapon and offhand
        weaponAndOffhandStackPane.setMaxHeight(bigHeight);
        weaponAndOffhandStackPane.setMinHeight(bigHeight);
        weaponAndOffhandStackPane.setMaxWidth(bigWidth);
        weaponAndOffhandStackPane.setMinWidth(bigWidth);
        weaponAndOffhandStackPane.setAlignment(Pos.BOTTOM_RIGHT);

        AnchorPane capeFoodAndPotionPane = new AnchorPane(); //anchor pane holds cape,food,potion
        capeFoodAndPotionPane.setMinHeight(bigHeight+10);
        capeFoodAndPotionPane.setMaxHeight(bigHeight+10);
        capeFoodAndPotionPane.setMinWidth(bigWidth+10);
        capeFoodAndPotionPane.setMaxWidth(bigWidth+10);

        StackPane helmetSwapStackPane = new StackPane(); //stack pane holds helmet and swap
        helmetSwapStackPane.setMaxHeight(bigHeight);
        helmetSwapStackPane.setMinHeight(bigHeight);
        helmetSwapStackPane.setMaxWidth(bigWidth);
        helmetSwapStackPane.setMinWidth(bigWidth);
        helmetSwapStackPane.setAlignment(Pos.BOTTOM_RIGHT);

        StackPane armorSwapStackPane = new StackPane(); //stack pane holds armor and swap
        armorSwapStackPane.setMaxHeight(bigHeight);
        armorSwapStackPane.setMinHeight(bigHeight);
        armorSwapStackPane.setMaxWidth(bigWidth);
        armorSwapStackPane.setMinWidth(bigWidth);
        armorSwapStackPane.setAlignment(Pos.BOTTOM_RIGHT);

        StackPane bootsSwapStackPane = new StackPane(); //stack pane holds boots and swap
        bootsSwapStackPane.setMaxHeight(bigHeight);
        bootsSwapStackPane.setMinHeight(bigHeight);
        bootsSwapStackPane.setMaxWidth(bigWidth);
        bootsSwapStackPane.setMinWidth(bigWidth);
        bootsSwapStackPane.setAlignment(Pos.BOTTOM_RIGHT);

        if(makeBuildFirstMethodCall) {
            /*
            Image weaponImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Elder's Demonfang.png")); //get images
            Image offHandImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Elder's Muisak.png"));
            Image helmetImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Elder's Stalker Hood.png"));
            Image chestpieceImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Elder's Hellion Jacket.png"));
            Image bootsImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Elder's Mercenary Shoes.png"));
            Image capeImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Elder's Lymhurst Cape.png"));
            Image foodImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Deadwater Eel Stew.png"));
            Image potionImage = new Image(new FileInputStream("src/main/resources/com/example/abct/images/Major Resistance Potion.png"));
             */

            URL url = AlbionBuildCreationTool.class.getResource("images/Elder's Demonfang.png");
            Image weaponImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Muisak.png");
            Image offHandImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Stalker Hood.png");
            Image helmetImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Hellion Jacket.png");
            Image chestpieceImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Mercenary Shoes.png");
            Image bootsImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Grandmaster's Lymhurst Cape.png");
            Image capeImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Deadwater Eel Stew.png");
            Image foodImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Major Resistance Potion.png");
            Image potionImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Knight Helmet.png");
            Image helmetSwapImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Mistwalker Jacket.png");
            Image armorSwapImage = new Image(url.openStream());
            url = AlbionBuildCreationTool.class.getResource("images/Elder's Cleric Sandals.png");
            Image bootsSwapImage = new Image(url.openStream());





            buildArray[i].buildImageArray[0] = weaponImage;  //populates array and sets image sizes
            buildArray[i].buildImageViewArray[0] = new ImageView(weaponImage);
            buildArray[i].buildImageViewArray[0].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[0].setFitHeight(bigHeight);
            buildArray[i].buildImageArray[1] = offHandImage;
            buildArray[i].buildImageViewArray[1] = new ImageView(offHandImage);
            buildArray[i].buildImageViewArray[1].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[1].setFitHeight(smallHeight);
            buildArray[i].buildImageArray[2] = helmetImage;
            buildArray[i].buildImageViewArray[2] = new ImageView(helmetImage);
            buildArray[i].buildImageViewArray[2].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[2].setFitHeight(bigHeight);
            buildArray[i].buildImageArray[3] = chestpieceImage;
            buildArray[i].buildImageViewArray[3] = new ImageView(chestpieceImage);
            buildArray[i].buildImageViewArray[3].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[3].setFitHeight(bigHeight);
            buildArray[i].buildImageArray[4] = bootsImage;
            buildArray[i].buildImageViewArray[4] = new ImageView(bootsImage);
            buildArray[i].buildImageViewArray[4].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[4].setFitHeight(bigHeight);
            buildArray[i].buildImageArray[5] = capeImage;
            buildArray[i].buildImageViewArray[5] = new ImageView(capeImage);
            buildArray[i].buildImageViewArray[5].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[5].setFitHeight(smallHeight);
            buildArray[i].buildImageArray[6] = foodImage;
            buildArray[i].buildImageViewArray[6] = new ImageView(foodImage);
            buildArray[i].buildImageViewArray[6].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[6].setFitHeight(smallHeight);
            buildArray[i].buildImageArray[7] = potionImage;
            buildArray[i].buildImageViewArray[7] = new ImageView(potionImage);
            buildArray[i].buildImageViewArray[7].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[7].setFitHeight(smallHeight);
            buildArray[i].buildImageArray[8] = potionImage;
            buildArray[i].buildImageViewArray[8] = new ImageView(helmetSwapImage);
            buildArray[i].buildImageViewArray[8].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[8].setFitHeight(smallHeight);
            buildArray[i].buildImageArray[9] = potionImage;
            buildArray[i].buildImageViewArray[9] = new ImageView(armorSwapImage);
            buildArray[i].buildImageViewArray[9].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[9].setFitHeight(smallHeight);
            buildArray[i].buildImageArray[10] = potionImage;
            buildArray[i].buildImageViewArray[10] = new ImageView(bootsSwapImage);
            buildArray[i].buildImageViewArray[10].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[10].setFitHeight(smallHeight);

            if (isOneHandedDefault) { //if weapon is 2handed, put offhand behind it in stack pane, otherwise draw it on top
                weaponAndOffhandStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[0], buildArray[i].buildImageViewArray[1]);
            } else {
                weaponAndOffhandStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[1], buildArray[i].buildImageViewArray[0]);
            }
        }else{
            weaponAndOffhandStackPane.getChildren().clear();
            capeFoodAndPotionPane.getChildren().clear();
            buildHboxArray[i].getChildren().clear();

            buildArray[i].buildImageViewArray[0].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[0].setFitHeight(bigHeight);
            buildArray[i].buildImageViewArray[1].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[1].setFitHeight(smallHeight);
            buildArray[i].buildImageViewArray[2].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[2].setFitHeight(bigHeight);
            buildArray[i].buildImageViewArray[3].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[3].setFitHeight(bigHeight);
            buildArray[i].buildImageViewArray[4].setFitWidth(bigWidth);
            buildArray[i].buildImageViewArray[4].setFitHeight(bigHeight);
            buildArray[i].buildImageViewArray[5].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[5].setFitHeight(smallHeight);
            buildArray[i].buildImageViewArray[6].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[6].setFitHeight(smallHeight);
            buildArray[i].buildImageViewArray[7].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[7].setFitHeight(smallHeight);
            buildArray[i].buildImageViewArray[8].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[8].setFitHeight(smallHeight);
            buildArray[i].buildImageViewArray[9].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[9].setFitHeight(smallHeight);
            buildArray[i].buildImageViewArray[10].setFitWidth(smallWidth);
            buildArray[i].buildImageViewArray[10].setFitHeight(smallHeight);

            if (isOneHanded) { //if weapon is 1handed, put offhand in front of it in stack pane, otherwise draw it behind
                weaponAndOffhandStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[0], buildArray[i].buildImageViewArray[1]);
            } else {
                weaponAndOffhandStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[0]); //buildArray[i].buildImageViewArray[1], should go first
            }
        }

        if(helmetSwapActive){
            helmetSwapStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[2], buildArray[i].buildImageViewArray[8]);
        }else{
            helmetSwapStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[2]);
        }
        if(armorSwapActive){
            armorSwapStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[3], buildArray[i].buildImageViewArray[9]);
        }else{
            armorSwapStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[3]);
        }
        if(bootsSwapActive){
            bootsSwapStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[4], buildArray[i].buildImageViewArray[10]);
        }else{
            bootsSwapStackPane.getChildren().addAll(buildArray[i].buildImageViewArray[4]);
        }

        AnchorPane.setTopAnchor(buildArray[i].buildImageViewArray[7],0.0);
        AnchorPane.setLeftAnchor(buildArray[i].buildImageViewArray[7],0.0);
        AnchorPane.setBottomAnchor(buildArray[i].buildImageViewArray[6],0.0);
        AnchorPane.setLeftAnchor(buildArray[i].buildImageViewArray[6],0.0);
        AnchorPane.setTopAnchor(buildArray[i].buildImageViewArray[5], Double.valueOf(smallHeight/2-5));
        AnchorPane.setRightAnchor(buildArray[i].buildImageViewArray[5],0.0); //positions potion,food,cape in anchor pane

        capeFoodAndPotionPane.getChildren().addAll(buildArray[i].buildImageViewArray[7], //adds potion,food,cape to anchor pane
                buildArray[i].buildImageViewArray[6],
                buildArray[i].buildImageViewArray[5]);

        BorderWidths hBoxBorderWidths = new BorderWidths(2.0);  //decorations
        buildHboxArray[i].setBackground(new Background(new BackgroundFill(darkBlue, CornerRadii.EMPTY, Insets.EMPTY)));
        buildHboxArray[i].setBorder(new Border(new BorderStroke(lightBlue, BorderStrokeStyle.SOLID, null , hBoxBorderWidths)));

        buildHboxArray[i].getChildren().addAll(weaponAndOffhandStackPane, //makes the build
                helmetSwapStackPane,
                armorSwapStackPane,
                bootsSwapStackPane,
                capeFoodAndPotionPane);
        buildHboxArray[i].setAlignment(Pos.CENTER_LEFT);
        makeBuildFirstMethodCall = false;

    }



    public void addMouseListenersToSelectionMenu(int i){
        imageViewArray[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                makeBuildFirstMethodCall = false;

                if(itemArray[i].isWeapon) {
                    buildArray[buildIndex].buildImageViewArray[0] = new ImageView(imageViewArray[i].getImage());
                    if(itemArray[i].isOneHanded) weaponIsOneHanded = true;
                    else weaponIsOneHanded = false;
                }
                if(itemArray[i].isOffhand){
                    buildArray[buildIndex].buildImageViewArray[1] = new ImageView(imageViewArray[i].getImage());
                }
                if(itemArray[i].isHelmet && !checkBoxHelmetSwap.isSelected()){
                    buildArray[buildIndex].buildImageViewArray[2] = new ImageView(imageViewArray[i].getImage());
                }else if (itemArray[i].isHelmet && checkBoxHelmetSwap.isSelected()){
                    buildArray[buildIndex].buildImageViewArray[8] = new ImageView(imageViewArray[i].getImage());
                }
                if(itemArray[i].isChestpiece && !checkBoxArmorSwap.isSelected()){
                    buildArray[buildIndex].buildImageViewArray[3] = new ImageView(imageViewArray[i].getImage());
                }else if (itemArray[i].isChestpiece && checkBoxArmorSwap.isSelected()){
                    buildArray[buildIndex].buildImageViewArray[9] = new ImageView(imageViewArray[i].getImage());
                }
                if(itemArray[i].isBoots && !checkBoxBootsSwap.isSelected()){
                    buildArray[buildIndex].buildImageViewArray[4] = new ImageView(imageViewArray[i].getImage());
                }else if (itemArray[i].isBoots && checkBoxBootsSwap.isSelected()){
                    buildArray[buildIndex].buildImageViewArray[10] = new ImageView(imageViewArray[i].getImage());
                }
                if(itemArray[i].isCape){
                    buildArray[buildIndex].buildImageViewArray[5] = new ImageView(imageViewArray[i].getImage());
                }
                if(itemArray[i].isFood){
                    buildArray[buildIndex].buildImageViewArray[6] = new ImageView(imageViewArray[i].getImage());
                }
                if(itemArray[i].isPotion){
                    buildArray[buildIndex].buildImageViewArray[7] = new ImageView(imageViewArray[i].getImage());
                }


                try {
                    makeBuild(buildIndex,weaponIsOneHanded);
                    initialize();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mouseEvent.consume();
            }});
        imageViewArray[i].getStyleClass().add("selection-menu-image-view-id"); //adds css id for hover cursor
    }


    protected void updateCss(Color backgroundColor, Color buildBoxColor, Color borderColor, int borderWidth){
        scrollPaneBuilds.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        hboxPlaceBuildsHere.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        vboxBuildsBuilds.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        buildHboxArray[buildIndex].setBackground(new Background(new BackgroundFill(buildBoxColor, CornerRadii.EMPTY, Insets.EMPTY)));

        BorderWidths hBoxBorderWidths = new BorderWidths(borderWidth);  //decorations
        buildHboxArray[buildIndex].setBorder(new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, null, hBoxBorderWidths)));
    }


    protected void updateCssNoArgs(){
        Color backgroundColor, buildBoxColor, borderColor;
        int borderWidth;

        if (tfBackgroundColor.getText().startsWith("#")) backgroundColor = Color.web(tfBackgroundColor.getText().toString());
        else backgroundColor = medBlue;
        if (tfBuildBoxColor.getText().startsWith("#")) buildBoxColor = Color.web(tfBuildBoxColor.getText().toString());
        else buildBoxColor = darkBlue;
        if (tfBackgroundColor.getText().startsWith("#")) borderColor = Color.web(tfBorderColor.getText().toString());
        else borderColor = lightBlue;
        try { //get border width if it's a valid integer, otherwise sets it to 2
            borderWidth = Integer.parseInt(tfBorderWidth.getText().toString());
        }catch (Exception e){
            borderWidth = 2;
        }
        updateCss(backgroundColor,buildBoxColor,borderColor,borderWidth);
    }
    @FXML
    protected void btnHelpOnClick(ActionEvent event){
        if (helpWindowStage == null || !helpWindowStage.isShowing()) {
            try {
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("layout_help.fxml"));
                Parent root2 = (Parent) fxmlLoader2.load();
                helpWindowStage = new Stage();
                helpWindowStage.setTitle("Help");
                URL url2 = AlbionBuildCreationTool.class.getResource("misc/Golden Sextant.png");
                Image iconImage2 = new Image(url2.openStream());
                helpWindowStage.getIcons().add(iconImage2);
                root2.getStylesheets().add(css);
                helpWindowStage.setScene(new Scene(root2));
                helpWindowStage.show();
                helpWindowStage.setResizable(true);
            } catch (IOException e) {
            }
        }else{
            helpWindowStage.toFront();
        }
        if(helpWindowStage.isIconified()){
            helpWindowStage.setIconified(false);
        }

    }

    @FXML
    protected void btnSetStylesOnClick(ActionEvent event){ //sets styles to text field values
        Color backgroundColor, buildBoxColor, borderColor;
        int borderWidth;

        if (tfBackgroundColor.getText().startsWith("#")) backgroundColor = Color.web(tfBackgroundColor.getText().toString());
        else backgroundColor = medBlue;
        if (tfBuildBoxColor.getText().startsWith("#")) buildBoxColor = Color.web(tfBuildBoxColor.getText().toString());
        else buildBoxColor = darkBlue;
        if (tfBackgroundColor.getText().startsWith("#")) borderColor = Color.web(tfBorderColor.getText().toString());
        else borderColor = lightBlue;
        try { //get border width if it's a valid integer, otherwise sets it to 2
            borderWidth = Integer.parseInt(tfBorderWidth.getText().toString());
        }catch (Exception e){
            borderWidth = 2;
        }
        tfBackgroundColor.setText(colorToHexString(backgroundColor));
        tfBuildBoxColor.setText(colorToHexString(buildBoxColor));
        tfBorderColor.setText(colorToHexString(borderColor));
        tfBorderWidth.setText(String.valueOf(borderWidth));

        updateCss(backgroundColor,buildBoxColor,borderColor,borderWidth);
    }
    @FXML
    protected void btnResetStylesOnClick(ActionEvent event){ //resets styles and shows color values in text fields
        Color backgroundColor, buildBoxColor, borderColor;
        int borderWidth;

        backgroundColor = medBlue;
        buildBoxColor = darkBlue;
        borderColor = lightBlue;
        borderWidth = 2;

        tfBackgroundColor.setText(colorToHexString(backgroundColor));
        tfBuildBoxColor.setText(colorToHexString(buildBoxColor));
        tfBorderColor.setText(colorToHexString(borderColor));
        tfBorderWidth.setText(String.valueOf(borderWidth));

        updateCss(backgroundColor,buildBoxColor,borderColor,borderWidth);
    }
    public static String colorToHexString(Color color ) //gets hex color code from color object
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }


    @FXML
    public void checkBoxWeaponsClicked(ActionEvent event) {

        if (checkBoxWeapons.isSelected()){
            filterWeapons = true;
            initialize();
        } else {
            filterWeapons = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxArmorClicked(ActionEvent event) {
        if (checkBoxArmor.isSelected()){
            filterArmor = true;
            initialize();
        } else {
            filterArmor = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxZvzClicked(ActionEvent event) {
        if (checkBoxZvz.isSelected()){
            filterZvz = true;
            initialize();
        } else {
            filterZvz = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxSsClicked(ActionEvent event) {
        if (checkBoxSs.isSelected()){
            filterSs = true;
            initialize();
        } else {
            filterSs = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxGankingClicked(ActionEvent event) {
        if (checkBoxGanking.isSelected()){
            filterGanking = true;
            initialize();
        } else {
            filterGanking = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxOtherClicked(ActionEvent event) {
        if (checkBoxOther.isSelected()){
            filterOther = true;
            initialize();
        } else {
            filterOther = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxOffhandClicked(ActionEvent event) {
        if (checkBoxOffhand.isSelected()){
            filterOffhands = true;
            initialize();
        } else {
            filterOffhands = false;
            initialize();
        }
    }
    @FXML
    public void checkBoxTier7Clicked(ActionEvent event) {
        if (checkBoxTier7.isSelected()){
            filterTier7 = true;
            initialize();
        } else {
            filterTier7 = false;
            initialize();
        }
    }

    @FXML
    public void checkBoxHelmetSwapClicked(ActionEvent event) {
            helmetSwapActive = !helmetSwapActive;
            initialize();
    }

    @FXML
    public void checkBoxArmorSwapClicked(ActionEvent event) {
        armorSwapActive = !armorSwapActive;
        initialize();
    }

    @FXML
    public void checkBoxBootsSwapClicked(ActionEvent event) {
        bootsSwapActive = !bootsSwapActive;
        initialize();
    }

    /*
    public static void addMouseListenersToBuild(int i, int weaponId) { //adds mouse click listeners to build
        buildArray[i].buildImageViewArray[0].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("weapon ");
                mouseEvent.consume();
            }});
        if(itemArray[weaponId].isOneHanded){
            buildArray[i].buildImageViewArray[1].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("offhand ");
                    mouseEvent.consume();
                }});
        }
        buildArray[i].buildImageViewArray[2].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("helmet ");
                mouseEvent.consume();
            }});
        buildArray[i].buildImageViewArray[3].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("chestpiece ");
                mouseEvent.consume();
            }});
        buildArray[i].buildImageViewArray[4].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("boots ");
                mouseEvent.consume();
            }});
        buildArray[i].buildImageViewArray[5].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("cape ");
                mouseEvent.consume();
            }});
        buildArray[i].buildImageViewArray[6].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("food ");
                mouseEvent.consume();
            }});
        buildArray[i].buildImageViewArray[7].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("potion ");
                mouseEvent.consume();
            }});
    }
 */


}