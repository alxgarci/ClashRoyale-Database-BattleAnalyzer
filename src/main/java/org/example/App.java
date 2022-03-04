package org.example;

import jcrapi2.Api;
import jcrapi2.model.*;
import jcrapi2.request.GetClanMembersRequest;
import jcrapi2.request.GetClansRequest;
import jcrapi2.request.GetPlayerBattleLogRequest;
import jcrapi2.response.*;
import org.example.obj.DeckStats;
import org.example.obj.MatchHistory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final String API_KEY = "YOUR API KEY GOES HERE, JUST USE A PROXY TO GET RESPONSES FROM DIFFERENT IPs, GO TO ROYALEAPI FOR FURTHER INFORMATION";
    private static final String PROXY_URL = "https://proxy.royaleapi.dev";
    private static final String DATABASES_CHARSET = "UTF-8";

    private static final int OPT_STORE_TAGS = 1;
    private static final int OPT_RETRIEVE_TO_DB = 2;
    private static final int OPT_PRINT_DB = 3;
    private static final int OPT_EXIT = 0;
    private static final int OPT_PRINT_DECK_STATS = 4;

    public static final String FILE_LINE_IDENTIFIER = "[mtch]";
    public static final String ARRAY_SPLITTER = ";";
    public static final String KEY_CLANTAG = "CLAN_TAG";
    public static final String KEY_USERTAG = "USER_TAG";

    public static final String SETTINGS_FILENAME = "app_config";
    public static final String DB_1V1 = "localmatches1v1";


    private static Api api;
    private static Scanner sc;
    private static String clanTag;
    private static String userTag;

    public static void main( String[] args )
    {
        //API SETUP
        api = new Api(PROXY_URL + "/v1/",API_KEY);

        //Scanner setup for inputs
        sc = new Scanner(System.in);

        readPropertiesAtStartup();


        boolean c = true;
        while (c) {
            switch (mainMenu()) {
                case OPT_EXIT:
                    c = false;

                    break;


                case OPT_STORE_TAGS:

                    clanTag = readStringInput("Write your clan tag (#00000)");
                    userTag = readStringInput("Write your user tag (#00000)");

                    clanTag = clanTag.replace("#", "").trim();
                    userTag = userTag.replace("#", "").trim();


                    File appConfig = createLocalFile(SETTINGS_FILENAME, "properties");
                    try {
                        FileInputStream fileInputStream = new FileInputStream(appConfig.getPath());

                        Properties properties = new Properties();

                        properties.load(fileInputStream);

                        Writer writer = new FileWriter(appConfig);
                        properties.setProperty(KEY_CLANTAG, clanTag);
                        properties.setProperty(KEY_USERTAG, userTag);
                        properties.store(writer, "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;

                case OPT_RETRIEVE_TO_DB:
                    File matchDatabase = createLocalFile(DB_1V1, "");

                    if (getStoredTag(1) != null && getStoredTag(0) != null) {
                        ArrayList<MatchHistory> matchHistories = getPlayerBattleLogTeamCards1v1(getStoredTag(0), getStoredTag(1));
                        writeFileByArray(matchDatabase, matchHistories);
                    } else {
                        System.out.println("[ERROR] You need to configure a PlayerTag and a ClanTag before");
                    }
                    break;

                case OPT_PRINT_DB:
                    File f = createLocalFile(DB_1V1, "");
                    System.out.println("[I] Printing " + f.getPath());
                    ArrayList<MatchHistory> matchHistoriesRes = readFileToArray(f);
                    for (MatchHistory m : matchHistoriesRes) {
                        System.out.println(m.toString());
                    }
                    break;

                case OPT_PRINT_DECK_STATS:
                    File f2 = createLocalFile(DB_1V1, "");
                    System.out.println("[I] Printing deck stats from " + f2.getPath());
                    ArrayList<MatchHistory> mAnalisis = readFileToArray(f2);

                    ArrayList<DeckStats> deckStats = getUserMostUsedDeck(mAnalisis);
                    printDeckStats(deckStats);
                    break;

                default:
                    System.out.println("[SYS] Option given not listed");
                    break;
            }
        }

        //Crear fichero local para base de datos de partidas, si no existe el archivo
//        File matchDatabase = createLocalFile("localmatches", "");
//
//        ArrayList<MatchHistory> matchHistories = getPlayerBattleLogTeamCards1v1("#", "#");
//
//        writeFileByArray(matchDatabase, matchHistories);
//
//        ArrayList<MatchHistory> matchHistoriesRes = readFileToArray(matchDatabase);
//        System.out.println(" - -- - - - --DOC LEIDO -- - -- - -- - -");
//        for (MatchHistory m : matchHistoriesRes) {
//            System.out.println(m.toString());
//        }



//        GetClansResponse getClansResponse = api.getClans(GetClansRequest.builder()
//                .name("")
//                .minMembers(48)
//                .locationId("")
//                .build());
//
//        Collection<Clan> gc = getClansResponse.getItems();
//
//        String tag = "";
//
//        Clan[] array = gc.toArray(new Clan[0]);
//        System.out.println(array[0].getName());
//        GetClanMembersResponse getClanMembersResponse = api.getClanMembers(GetClanMembersRequest.builder(array[0].getTag()).build());
//        Collection<ClanMember> gcm = getClanMembersResponse.getItems();
//        for (ClanMember cm: gcm) {
//            if (cm.getName().equals("")) {
//                tag = cm.getTag();
//                System.out.println("[S] TAG=" + tag);
//            }
//        }
//            System.out.print(c.getName() + " - " + c.getLocation() + " - ");
//            System.out.println(c.getClanWarTrophies());
//            System.out.println(c.getMemberList());
//            List<ClanMember> clanMemberList = c.getMemberList();
//            for (ClanMember cm : clanMemberList ) {
//                System.out.println("   " + cm.getName() + " - " + cm.getTrophies());
//            }
//        GetPlayerBattleLogResponse getPlayerBattleLogResponse = api.getPlayerBattleLog(
//                GetPlayerBattleLogRequest.builder(array[0].getTag()).playerTag(tag)
//                .build());
//        PlayerBattleLog playerBattleLogs = getPlayerBattleLogResponse.get(0);
//        Collection<PlayerBattleLogTeamCard> playerBattleLogTeamCards = playerBattleLogs.getTeam().get(0).getCards();
//        int realLevel;
//        for (PlayerBattleLogTeamCard card :
//                playerBattleLogTeamCards) {
//            realLevel = card.getLevel() +5;
//            System.out.println(card.getName() + " [LVL " + realLevel + "]");
//        }
//        System.out.println(playerBattleLogs.getTeam().get(0).getCards().get(1) + " - " + playerBattleLogs.getArena().getName());


    }

    private static void printDeckStats(ArrayList<DeckStats> deckStats) {
        double avgCrowns;
        double avgTrophies;
        DecimalFormat formatter = new DecimalFormat("##.#");
        for (DeckStats d : deckStats) {
            System.out.print("[DECKSTATS] ");
            System.out.println("DECK USED: " + d.getDeckToString());
            System.out.println("        [STATS] " + d.getTotalCrowns() + " CROWNS ** " + d.getTotalTrophies() + " TROPHIES ** " + d.getTimesUsed() + " TIMES USED");
            System.out.print(  "                ");
            if (d.getTotalCrowns() != 0) {
                avgCrowns = (double) d.getTotalCrowns()/d.getTimesUsed();
                System.out.print(formatter.format(avgCrowns) + " AVERAGE CROWNS");
            }
            if (d.getTotalTrophies() != 0) {
                avgTrophies = (double) d.getTotalTrophies()/d.getTimesUsed();
                System.out.print(" ** " + formatter.format(avgTrophies)  + " AVERAGE TROPHIES");
            }
            System.out.println();
        }
    }

    private static ArrayList<DeckStats> getUserMostUsedDeck(ArrayList<MatchHistory> mAnalisis) {
        int totalTimesUsed;
        int totalCrowns;
        int totalTrophies;
        boolean contained;
        int contador;
        ArrayList<DeckStats> deckStats = new ArrayList<>();
        for (MatchHistory m : mAnalisis) {
            contained = false;
            contador = 0;
            for (DeckStats d : deckStats) {

                if (d.getDeck().equals(m.getDeck())) {
                    totalTimesUsed = d.getTimesUsed() + 1;
                    d.setTimesUsed(totalTimesUsed);

                    totalCrowns = d.getTotalCrowns() + m.getCrowns();
                    d.setTotalCrowns(totalCrowns);

                    totalTrophies = d.getTotalTrophies() + m.getTrophiesChange();
                    d.setTotalTrophies(totalTrophies);

                    deckStats.set(contador, d);
                    contained = true;
                }
                contador++;
            }
            if (!contained) {
                deckStats.add(new DeckStats(m.getDeck(), 1, m.getCrowns(), m.getTrophiesChange()));
            }
        }

        return deckStats;
    }

//    private static String getStoredClanTag() {
//        properties = new Properties();
//        try {
//            properties.load(new FileInputStream(createLocalFile(SETTINGS_FILENAME, "properties")));
//
//            if (properties.get(KEY_CLANTAG) != null) {
//                clanTag = "#" + properties.get(KEY_CLANTAG).toString();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return clanTag;
//    }

    private static String getStoredTag(int isClan) {
        Properties properties = new Properties();

        String tag = "";
        try {
            properties.load(new FileInputStream(createLocalFile(SETTINGS_FILENAME, "properties")));

            if (isClan == 1) {
                if (properties.get(KEY_CLANTAG) != null) {
                    clanTag = "#" + properties.get(KEY_CLANTAG).toString();
                    tag = clanTag;
                }
            } else if (isClan == 0) {
                if (properties.get(KEY_USERTAG) != null) {
                    userTag = "#" + properties.get(KEY_USERTAG).toString();
                    tag = userTag;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tag;
    }


//    private static String getStoredPlayerTag() {
//        properties = new Properties();
//        try {
//            properties.load(new FileInputStream(createLocalFile(SETTINGS_FILENAME, "properties")));
//
//            if (properties.get(KEY_USERTAG) != null) {
//                userTag = "#" + properties.get(KEY_USERTAG).toString();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return userTag;
//    }

    private static void readPropertiesAtStartup() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(createLocalFile("app_config", "properties")));

            if (properties.get(KEY_USERTAG) != null && properties.get(KEY_CLANTAG) != null) {
                clanTag = "#" + properties.get(KEY_CLANTAG).toString();
                userTag = "#" + properties.get(KEY_USERTAG).toString();
                System.out.println("[SYS] Clan and user tags retrieved from stored settings");
            } else {
                clanTag = null;
                userTag = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String readStringInput(String txt) {
        System.out.println("[I] " + txt);
        System.out.print(">>: ");
        return sc.nextLine();
    }

    private static int mainMenu() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("- WELCOME TO CLASH ROYALE SCRAPER, BY github.com/alxgarci -");
        System.out.println("-----------------------------------------------------------");
        System.out.println("[MAIN MENU] Select an option");
        System.out.println("  " + OPT_STORE_TAGS + " - Store your clan tag and player tag");
        System.out.println("  " + OPT_RETRIEVE_TO_DB + " - Retrieve match history and save it to local database");
        System.out.println("  " + OPT_PRINT_DB + " - Print all local database data");
        System.out.println("  " + OPT_PRINT_DECK_STATS + " - Print your deck stats");
        System.out.println("  " + OPT_EXIT + " - Exit");

        System.out.print(">>: ");
        int res = sc.nextInt();
        sc.nextLine();
        return res;
    }

    private static ArrayList<MatchHistory> readFileToArray(File matchDatabase) {
        ArrayList<MatchHistory> matchHistories = new ArrayList<>();
        Set<String> set = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(matchDatabase));
            String line;
            while ((line = reader.readLine()) != null) {
                if (set.contains(line))
                    continue;
                set.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> aux;
        ArrayList<String> auxDeck;
        ArrayList<String> auxDeck2;
        for (String string : set) {
            auxDeck = new ArrayList<>();
            auxDeck2 = new ArrayList<>();

            aux = Arrays.asList(string.replace(FILE_LINE_IDENTIFIER, "").split(ARRAY_SPLITTER));

            for (int i = 3; i < 9; i++) {
               auxDeck.add(aux.get(i));
            }
            for (int i = 9; i < 15; i++) {
                auxDeck2.add(aux.get(i));
            }
            matchHistories.add(new MatchHistory(aux.get(0), Integer.parseInt(aux.get(1)), Integer.parseInt(aux.get(2)),auxDeck, auxDeck2));
        }
        return matchHistories;
    }

    private static void writeFileByArray(File matchDatabase, ArrayList<MatchHistory> matchHistories) {

        try {
            //Check if the line being written is already in the database
            //lines are stored in the 'set' and are compared later with the
            //one going to be written
            Set<String> set = new HashSet<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(matchDatabase));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (set.contains(line))
                        continue;
                    set.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            FileOutputStream fos = new FileOutputStream(matchDatabase, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, DATABASES_CHARSET);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw, true);
            int cont = 0;

            for (MatchHistory m : matchHistories) {
                System.out.println(m.toStringWrite());
                if (!set.contains(m.toStringWrite())) {
                    pw.write(m.toStringWrite());
                    pw.write("\n");
                    cont++;
                }
            }
            pw.close();
            System.out.println("[SYS] -" + cont + "- LINES WRITTEN TO '" + matchDatabase.getPath() + "'");

        } catch (IOException e) {
            System.out.println("[ERROR] Couldn't write data into '" + matchDatabase.getPath() + "'");
            e.printStackTrace();
        }
    }

    private static File createLocalFile(String dbName, String extension) {
        String dbFormed;
        if (extension.equals("")) {
            dbFormed = dbName + ".db";
        } else {
            dbFormed = dbName + "." + extension;
        }

        File dir = new File("tmp/");
        dir.mkdirs();
        File creator = new File(dir, dbFormed);
        try {
            creator.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = "tmp/" + dbFormed;

        return new File(path);
    }

    //Get clans matching criteria (searching and filtering)
    public Collection<Clan> getClansMatchingCollection(String name, int minMembers, int maxMembers, String locationId) {
        GetClansResponse getClansResponse = api.getClans(GetClansRequest.builder()
                .name(name)
                .minMembers(minMembers)
                .maxMembers(maxMembers)
                .locationId(locationId)
                .build());

        return getClansResponse.getItems();
    }

    //Get clan members with clan tag
    public Collection<ClanMember> getClanMembersCollection(String clanTag) {
        GetClanMembersResponse getClanMembersResponse = api.getClanMembers(GetClanMembersRequest.builder(clanTag)
                .build());

        return getClanMembersResponse.getItems();
    }


    public static Collection<PlayerBattleLogTeamCard> getPlayerBattleLogTeamCards(String playerTag, String clanTag) {
        GetPlayerBattleLogResponse getPlayerBattleLogResponse = api.getPlayerBattleLog(
                GetPlayerBattleLogRequest.builder(clanTag).playerTag(playerTag)
                        .build());

        for (PlayerBattleLog p : getPlayerBattleLogResponse) {
            //To obtain the battle type, casual1v1, casual2v2, boatBattle...
            System.out.println(p.getType());
            for (PlayerBattleLogTeam playerBattleLogTeam : p.getTeam()) {
                System.out.print("USER: " + playerBattleLogTeam.getName() + " ");
                if (playerBattleLogTeam.getTag().equals(playerTag)) {
                    System.out.print("[TROPHIES] " + playerBattleLogTeam.getTrophyChange() + " - [CROWNS] " + playerBattleLogTeam.getCrowns());

                }
                System.out.println();
                for (PlayerBattleLogTeamCard c : playerBattleLogTeam.getCards()) {
                    System.out.print(c.getName() + ";");
                }
                System.out.println();
            }
            for (PlayerBattleLogOpponent pblo : p.getOpponent()) {
                System.out.println("OPPONENT: " + pblo.getName());
                for (PlayerBattleLogOpponentCard pbloc : pblo.getCards()) {
                    System.out.print(pbloc.getName() + ";");
                }
                System.out.println();
            }

        }
        return null;
    }

    public static ArrayList<MatchHistory> getPlayerBattleLogTeamCards1v1(String playerTag, String clanTag) {
        GetPlayerBattleLogResponse getPlayerBattleLogResponse = api.getPlayerBattleLog(
                GetPlayerBattleLogRequest.builder(clanTag).playerTag(playerTag)
                        .build());

        ArrayList<MatchHistory> matchHistories = new ArrayList<>();
        ArrayList<String> deck;
        ArrayList<String> opponentDeck;
        String opponentName = "null";
        int crowns = -400;
        int trophieChange = -400;
        int cont = 0;

        for (PlayerBattleLog p : getPlayerBattleLogResponse) {
            //Filter by battle type, casual 1v1 and 1v1 ladder
            deck = new ArrayList<>();
            opponentDeck = new ArrayList<>();
            System.out.print("[" + cont + "/--]" + " BATTLES ANALIZED...(" + p.getType() + ")                " + '\r');
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cont++;
            if (p.getType().equals("casual1v1") || p.getType().equals("1v1")) {

                for (PlayerBattleLogTeam playerBattleLogTeam : p.getTeam()) {
//                    System.out.print("USER: " + playerBattleLogTeam.getName() + " ");
//                    System.out.print("[TROPHIES] " + playerBattleLogTeam.getTrophyChange() + " - [CROWNS] " + playerBattleLogTeam.getCrowns());
//                    System.out.println();
                    trophieChange = playerBattleLogTeam.getTrophyChange();
                    crowns = playerBattleLogTeam.getCrowns();
                    for (PlayerBattleLogTeamCard c : playerBattleLogTeam.getCards()) {
//                        System.out.print(c.getName() + ";");
                        deck.add(c.getName());
                    }
//                    System.out.println();
                }
                for (PlayerBattleLogOpponent pblo : p.getOpponent()) {
//                    System.out.println("OPPONENT: " + pblo.getName());
                    opponentName = pblo.getName();
                    for (PlayerBattleLogOpponentCard pbloc : pblo.getCards()) {
//                        System.out.print(pbloc.getName() + ";");
                        opponentDeck.add(pbloc.getName());
                    }
//                    System.out.println();
                }
                matchHistories.add(new MatchHistory(opponentName, crowns, trophieChange, deck, opponentDeck));
            }

        }
        return matchHistories;
    }
}
