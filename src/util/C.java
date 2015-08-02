/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * A class containing constants used in project Phoenix classes.
 *
 * The constants are grouped thus:
 * 1. Various int and int[] finals
 * 2. message types: public enum Msg; movement types: public enum MoveType
 * 3. EFS structures, resources; harvesting; resource and unit production: 
 *    final int and int[]
 * 4. EFS terrain types; harvest terrain order: final int and int[]
 * 5. Special unit type number; EFS faction numbers: final int
 * 6. Research related: final int
 * 7. Strings, Colors, miscellaneous: various finals
 * @author joulupunikki
 */
public class C {

// 1. Various int and int[] finals
    public static final int DEBUG_PRINT = 0;
    public static final int NORTH = 0;
    public static final int NORTHEAST = 1;
    public static final int SOUTHEAST = 2;
    public static final int SOUTH = 3;
    public static final int SOUTHWEST = 4;
    public static final int NORTHWEST = 5;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FOUR = 4;
    public static final int PLANET_NAME = 32;
    public static final int PLANET_MAP_WIDTH = 44;
    public static final int PLANET_MAP_HEIGHT = 65;
    public static final int PLANET_MAP_COLUMNS = 32;
    public static final int PLANET_MAP_SQUARES_X = 13;
    public static final int PLANET_MAP_SQUARES_Y = 10;
    public static final int UNIT_POS = 8;
    public static final int STAR_MAP_HEIGHT = 48;
    public static final int STAR_MAP_WIDTH = 50;
    public static final int STAR_MAP_SQUARES_X = 15;
    public static final int STAR_MAP_SQUARES_Y = 13;
    public static final int END_OF_SECTION = -2;
    public static final int END_OF_GROUP = -3;
    public static final int STRUCT_BIN_HEIGHT = 40;
    public static final int STRUCT_BIN_WIDTH = 48;
    public static final int EFSUNIT_BIN_HEIGHT = 32;
    public static final int EFSUNIT_BIN_WIDTH = 32;
    public static final int PALLETTE_LENGTH = 768;
    public static final int EFSPLAN_BIN_LENGTH = 128;
    public static final int EFSPLAN_BIN_P_SIZE = 1024;
    public static final int MAX_PLANETS = 500;
    public static final int MAX_JUMP_GATES = 2000;
    public static final int MAX_STRUCTURES = 10000;
    public static final int MAX_UNITS = 40000;
    public static final int UNIT_TYPES = 92;
    public static final int UNIT_T_LVLS = 6;
    public static final int PLANET_MAP_ORIGIN_X_OFFSET = 6;
    public static final int PLANET_MAP_ORIGIN_Y_OFFSET = 4;
    public static final int STRBUILD = 32;
    public static final int TERR_COST_HEX = 12;
    public static final int TERR_COST_PLANET = 5;
    public static final int TERR_COST_MOVE = 10;
    public static final int UNIT_SPOT_HEX = 13;
    public static final int UNIT_SPOT_PLANET = 5;
    public static final int UNIT_SPOT_MOVE = 10;
    public static final int TER_COLOR_HEX = 12;
    public static final int TER_COLOR_PLANET = 5;
    public static final int CARGO_WIDTH = 34;
    public static final int CARGO_HEIGHT = 29;
    public static final int STARTING_YEAR = 4956;

    public static final int HARVEST_TERRAINS = 11;    // RSW
    public static final int HARVEST_PLANETS = 5;    // RSW

    public static final int PROD_CITIES = 7;    // RSW
    public static final int RES_TYPES = 13;    // RSW
    public static final int MAX_CARGO = 999;
    public static final int TILE_SETS = 5;
    public static final int STACK_SIZE = 20;
    public static final int STACK_WINDOW_UNITS_X = 5;
    public static final int STACK_WINDOW_UNITS_Y = 4;
    public static final int NR_FACTIONS = 14;
    public static final int NR_HOUSES = 5;    // RSW
    public static final int DAMAGE_DAT_X = 12;
    public static final int DAMAGE_DAT_Y = 10;
    public static final int TARGET_DAT_X = 9;
    public static final int TARGET_DAT_Y = 10;
    public static final int COMBAT_LOOP_ITER = 30;
    public static final int SKULL_SIDE = 40;
    // HexProc procedures
    public static final int INIT_SPOT = 0;
    public static final int SPOT = 1;
//    public static final int WATER_ATTK = 4;
//    public static final int INDIRECT_ATTK = 2;
//    public static final int AIR_ATTK = 2;
//    public static final int DIRECT_ATTK = 3;
//    public static final int CLOSE_ATTK = 4;
//    public static final int PSYCH_ATTK = 2;
    public static final int[] NR_ATTACKS = {4, 2, 2, 3, 4, 2, 2, 3, 4};
    public static final int[] GROUND_COMBAT_PHASES = {0, 1, 2, 3, 4, 5};
//    public static final int[] GROUND_COMBAT_ATTACKS = {4, 2, 2, 3, 4, 2};
    public static final int[] SPACE_COMBAT_PHASES = {6, 7, 8, 5};
//    public static final int[] SPACE_COMBAT_ATTACKS = {2, 3, 4, 2};
    public static final int[] PTS_COMBAT_PHASES = {6};
//    public static final int[] PTS_COMBAT_ATTACKS = {2};
//    public static final int GROUND_COMBAT_PHASES_NR = 6;
//    public static final int RANGED_SPACE_ATTK = 2;
//    public static final int DIRECT_SPACE_ATTK = 3;
//    public static final int CLOSE_SPACE_ATTK = 4;
//    public static final int[] SC_ATTACKS = {2, 3, 4, 2};
//    public static final int SPACE_COMBAT_PHASES_NR = 4;
    public static final int WATER = 0;
    public static final int INDIRECT = 1;
    public static final int AIR = 2;
    public static final int DIRECT = 3;
    public static final int CLOSE = 4;
    public static final int PSYCH = 5;
    public static final int RANGED_SPACE = 6;
    public static final int DIRECT_SPACE = 7;
    public static final int CLOSE_SPACE = 8;

    // message types
    public enum Msg {

        CITY_FULL,
        FAMINE,
        CANNOT_PRODUCE,
        COMBAT_REPORT
    }

    // movement types
    public enum MoveType {

        FOOT,
        WHEEL,
        TREAD,
        AIR,
        NAVAL,
        SPACE,
        HOVER,
        JUMP,
        CRAWLER,
        LANDER
    }

    // Structures    // RSW
    public static final int PALACE = 0;
    public static final int CHURCH = 1;
    public static final int MONASTERY = 2;
    public static final int FACTORY = 3;
    public static final int AGORA = 4;
    public static final int WETWARE = 5;
    public static final int ELECTRONICS = 6;
    public static final int HIVE = 7;
    public static final int CERAMSTEEL = 8;
    public static final int BIOPLANT = 9;
    public static final int VAU_CITY = 10;
    public static final int CHEMICALS = 11;
    public static final int CYCLOTRON = 12;
    public static final int FORT = 13;
    public static final int STARPORT = 14;
    public static final int RUINS = 15;
    public static final int ALIEN_RUINS = 16;
    public static final int SHIELD = 17;
    public static final int MINE = 18;
    public static final int WELL = 19;
    public static final int FUSORIUM = 20;
    public static final int UNIVERSITY = 21;
    public static final int HOSPITAL = 22;
    public static final int LAB = 23;
    public static final int FARM = 24;
    public static final int ARBORIUM = 25;
    public static final int TRACE = 26;
    public static final int GEMS = 27;
    public static final int EXOTICA = 28;
    public static final int FERTILE = 29;
    public static final int METAL = 30;
    public static final int ENERGY = 31;
    public static final int CITY_TYPES = 26;
    public static final int STRUCTURE_TYPES = 32;

    public static final int TRACE_SPECIAL = 26;    // RSW
    public static final int GEMS_SPECIAL = 27;
    public static final int EXOTICA_SPECIAL = 28;
    public static final int FERTILE_SPECIAL = 29;
    public static final int METAL_SPECIAL = 30;
    public static final int ENERGY_SPECIAL = 31;

    // Resource types    // RSW
    public static final int RES_FOOD = 0;
    public static final int RES_ENERGY = 1;
    public static final int RES_METAL = 2;
    public static final int RES_TRACE = 3;
    public static final int RES_EXOTICA = 4;
    public static final int RES_CHEMICALS = 5;
    public static final int RES_BIOCHEMS = 6;
    public static final int RES_ELECTRONICS = 7;
    public static final int RES_CERAMSTEEL = 8;
    public static final int RES_WETWARE = 9;
    public static final int RES_MONOPOLS = 10;
    public static final int RES_GEMS = 11;
    public static final int RES_SINGULARITIES = 12;

    // Harvesting city types - index into Harvest table    // RSW
    public static final int FARM_HARVESTING = 0;
    public static final int WELL_HARVESTING = 1;
    public static final int MINE_HARVESTING = 2;
    public static final int ARBORIUM_HARVESTING = 3;

    // Secondary production city types - index into Prod table    // RSW
    public static final int CHEMICALS_PRODUCTION = 0;
    public static final int ELECTRONICS_PRODUCTION = 1;
    public static final int BIOPLANT_PRODUCTION = 2;
    public static final int CERAMSTEEL_PRODUCTION = 3;
    public static final int WETWARE_PRODUCTION = 4;
    public static final int CYCLOTRON_PRODUCTION = 5;
    public static final int FUSORIUM_PRODUCTION = 6;

    // Harvesting cities
    public static final int[] HARVESTING_CITIES = {FARM, ARBORIUM, WELL, MINE};

    // Producing cities
    public static final int[] PRODUCING_CITIES = {CHEMICALS, BIOPLANT, ELECTRONICS, CERAMSTEEL, WETWARE, FUSORIUM,
        CYCLOTRON};

    // Resource production order
    public static final int[] PRODUCTION_ORDER = {FARM, ARBORIUM, WELL, MINE,
        CHEMICALS, BIOPLANT, ELECTRONICS, CERAMSTEEL, WETWARE, FUSORIUM,
        CYCLOTRON
    };

    // Resources used in unit building
    public static final int[] REQUIRED_RESOURCES = {RES_FOOD,
        RES_METAL,
        RES_TRACE,
        RES_CHEMICALS,
        RES_BIOCHEMS,
        RES_ELECTRONICS,
        RES_CERAMSTEEL,
        RES_WETWARE,
        RES_MONOPOLS,
        RES_GEMS,
        RES_SINGULARITIES};

    // Production 1 Consumption 0
    public static final int PROD = 1;
    public static final int CONS = 0;

    // terrain types
    public static final int OCEAN = 0;
    public static final int GRASS = 1;
    public static final int ARID_GRASS = 2;
    public static final int DESERT = 3;
    public static final int ICE = 4;
    public static final int TUNDRA = 5;
    public static final int MOUNTAIN = 6;
    public static final int HILL = 7;
    public static final int TREE = 8;
    public static final int RIVER = 9;
    public static final int DELTA = 10;
    public static final int ROAD = 11;
    public static final int STRUCTURE = 12;

    public static final int[] HARVEST_TERRAIN_ORDER = {
        OCEAN,
        GRASS,
        ARID_GRASS,
        DESERT,
        ICE,
        TUNDRA,
        TREE,
        MOUNTAIN,
        HILL,
        RIVER,
        DELTA
    };
//    // terrain types BUG FIX TEST ORIGINALS ABOVE
//    // moved tree before mountain
//    public static final int OCEAN = 0;
//    public static final int GRASS = 1;
//    public static final int ARID_GRASS = 2;
//    public static final int DESERT = 3;
//    public static final int ICE = 4;
//    public static final int TUNDRA = 5;
//    public static final int TREE = 6;
//    public static final int MOUNTAIN = 7;
//    public static final int HILL = 8;
//    public static final int RIVER = 9;
//    public static final int DELTA = 10;
//    public static final int ROAD = 11;
//    public static final int STRUCTURE = 12;

    public static final int BARREN_TILE_SET = 4;    // RSW

    // Unit types (only needed for units with special abilities)
    public static final int SPACE_CARRIER_UNIT_TYPE = 16;    // RSW
    public static final int FIGHTER_UNIT_TYPE = 17;    // RSW
    public static final int TORP_BMBR_UNIT_TYPE = 18;    // RSW
    public static final int ENGINEER_UNIT_TYPE = 53;
    public static final int CARGO_UNIT_TYPE = 91;    // RSW

    // faction id's
    public static final int HOUSE1 = 0;
    public static final int HOUSE2 = 1;
    public static final int HOUSE3 = 2;
    public static final int HOUSE4 = 3;
    public static final int HOUSE5 = 4;
    public static final int LEAGUE = 5;
    public static final int THE_CHURCH = 6;
    public static final int SYMBIOT = 7;
    public static final int VAU = 8;
    public static final int IMPERIAL = 9;
    public static final int FLEET = 10;
    public static final int STIGMATA = 11;
    public static final int THE_SPY = 12;
    public static final int NEUTRAL = 13;

//    //combat report array identifiers
//    public static final int CR_OWNER = 0;
//    public static final int CR_PREV_OWNER = 1;
//    public static final int CR_TYPE = 2;
//    public static final int CR_T_LVL = 3;
//    public static final int CR_HEALTH_BEGIN = 4;
//    public static final int CR_HEALTH_END = 5;
//    public static final int CR_ROUTED = 6;

    //research & technologies
    public static final int TECH0 = 0;
    public static final int TECH1 = 1;
    public static final int TECH2 = 2;
    public static final int TECH_COST = 3;
    public static final int TECH_VOL = 5;
    public static final int TECH_CH = 6;
    public static final int TECH_MAINT = 100;

    public static final String WS = "[\\s]*"; // java Pattern whitespace string
    public static final String S_PLANET_MAP = "planet map";
    public static final String S_STAR_MAP = "star map";
    public static final String S_UNIT_INFO = "unit info";
    public static final String S_MAIN_MENU = "main menu";
    public static final String S_MAIN_MENU1 = "main menu1";
    public static final String S_COMBAT_WINDOW = "combat window";
    public static final String S_X_PLAYER_SCREEN = "x player screen";
    public static final String S_MESSAGES = "messages window";
    // command line options
    public static final String OPT_DOUBLE_RES = "d";
    public static final String OPT_NAMED_GALAXY = "g";
    public static final String OPT_HELP = "h";
    // options mostly used for testing
    public static final String OPT_ROBOT_TEST = "robottest";
    public static final String OPT_WAIT_BEFORE_START = "wait";
    public static final String OPT_ECONOMY_PRINT = "printeconomy";
    public static final String OPT_AUTO_DELAY = "autodelay";
    public static final String OPT_CLEAN_UP_2CLICK = "cleanupb42click";
    public static final String OPT_GAME_STATE_FILE = "gamestatefile";
    
    public static final String S_ALL = "all";
    public static final String S_COMBAT = "combat";
    public static final String S_NONCOMBAT = "noncombat";
    public static final String S_TRANSPORT = "transport";

    public static final Color COLOR_GOLD_BRIGHT = new Color(232, 224, 100);
    public static final Color COLOR_GOLD = new Color(255, 215, 0);
    public static final Color COLOR_GOLD_DARK = new Color(164, 116, 40);
    public static final Color COLOR_RED_DARK = new Color(52, 4, 0);
    public static final Color COLOR_MANOWITZ_TEXT = new Color(92, 44, 16);
    public static final Color COLOR_DARK_GREEN = new Color(53, 94, 59);
    public static final Color COLOR_LIGHT_GREEN = new Color(76, 187, 23);
    public static final Color COLOR_RES_DISP_GREEN = new Color(96, 208, 64);
    public static final int INDEX_COLOR_EFS_BLACK = 14;
    public static final int INDEX_COLOR_BLACK = -96;
    public static final String GROUND_COMBAT = "ground_combat";
    public static final String SPACE_COMBAT = "space_combat";

    public static final String ATTACKING_CMBT_STACK = "attacking_combat_stack";
    public static final String DEFENDING_CMBT_STACK = "defending_combat_stack";
    public static final double GRAY_SCALING_FACTOR = 0.5;

//    public static final String S_RESOURCE[] = {"Food", "Energy", "Metal", "Trace", "Exotica", "Chemicals", "Biochems", "Electronics", "Ceramsteel", "Wetware", "Monopols", "Gems", "Singularities"};    // Temporary, until we have reader for RES.DAT
    public static final int STARTING_FIREBIRDS = 4500;

    public static final Border GOLD_BORDER = BorderFactory.createLineBorder(C.COLOR_GOLD);

    /**
     * Short print method for debugging, fixed message.
     *
     * @param s
     */
    public static void p() {
        System.out.println("********DEBUG PRINT********");
    }

    /**
     * Short print method for debugging, with String message.
     *
     * @param s
     */
    public static void p(String s) {
        System.out.println(s);
    }

    /**
     * Short print method for debugging, with int message.
     *
     * @param s
     */
    public static void p(int n) {
        System.out.println(n + "DEBUG PRINT");
    }

    /**
     * Prints out the constants which determine unit, structure and planet
     * allegiance. For debugging purposes.
     */
    public static void print() {
        System.out.println("HOUSE1:" + HOUSE1);
        System.out.println("HOUSE2:" + HOUSE2);
        System.out.println("HOUSE3:" + HOUSE3);
        System.out.println("HOUSE4:" + HOUSE4);
        System.out.println("HOUSE5:" + HOUSE5);
        System.out.println("LEAGUE:" + LEAGUE);
        System.out.println("THE_CHURCH:" + THE_CHURCH);
        System.out.println("SYMBIOT:" + SYMBIOT);
        System.out.println("VAU:" + VAU);
        System.out.println("IMPERIAL:" + IMPERIAL);
        System.out.println("FLEET:" + FLEET);
        System.out.println("STIGMATA:" + STIGMATA);
        System.out.println("THE_SPY:" + THE_SPY);
        System.out.println("NEUTRAL:" + NEUTRAL);
    }
}
/*
 * forest
 */
