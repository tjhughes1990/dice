package ui.src.main.java.tim.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private Integer diceNumberDefault = new Integer(1);
    private Integer successThresholdDefault = new Integer(1);
    private boolean maxRollDefault = false;
    private boolean botchDefault = false;

    private static final String configFilename = "config.txt";

    private static final List<String> PROPERTY_NAMES = new ArrayList<String>(
        Arrays.asList(
        "NumberOfDice",
        "SuccessThreshold",
        "MaxRollCountsAsTwoSuccesses",
        "BotchNoSuccesses"
    ));

    public Config() {
        final File configFile = new File(this.getClass()
            .getProtectionDomain().getCodeSource().getLocation().getPath());
        final String configAbsFilename = configFile.getPath()
            .split(configFile.getName())[0] + configFilename;

        try {
            BufferedReader br = new BufferedReader(
                new FileReader(configAbsFilename));

            String line;
            while((line = br.readLine()) != null) {
                String[] property = line.split(":");
                if(property.length == 2) {
                    setProperty(property[0], property[1]);
                }
            }
        } catch(Exception e) {
            // Leave values as initialised.
            return;
        }
    }

    private void setProperty(String prop, String value) {
        if(prop.contains(PROPERTY_NAMES.get(0))) {
            diceNumberDefault = parseIntegerProperty(value, diceNumberDefault);
        } else if(prop.contains(PROPERTY_NAMES.get(1))) {
            successThresholdDefault = parseIntegerProperty(
                value, successThresholdDefault);
        } else if(prop.contains(PROPERTY_NAMES.get(2))) {
            maxRollDefault = parseBooleanProperty(value, maxRollDefault);
        } else if(prop.contains(PROPERTY_NAMES.get(3))) {
            botchDefault = parseBooleanProperty(value, botchDefault);
        }
    }

    boolean parseBooleanProperty(String value, boolean currentValue) {
        try {
            Integer val = Integer.parseInt(value.replaceAll("\\s", ""));
            if(val.equals(1)) {
                return true;
            } else if(val.equals(0)) {
                return false;
            }
        } catch(Exception e) {
            // Not an integer
            if(value.toUpperCase().contains("TRUE")) {
                return true;
            } else if(value.toUpperCase().contains("FALSE")) {
                return false;
            }
        }
        return currentValue;
    }

    Integer parseIntegerProperty(String value, Integer currentValue) {
        Integer var;
        try {
            var = Integer.parseInt(value.replaceAll("\\s", ""));
        } catch(Exception e) {
            var = currentValue;
        }
        return var;
    }

    public Integer getDiceNumberDefault() {
        return diceNumberDefault;
    }

    public void setDiceNumberDefault(int diceNumberDefault) {
        this.diceNumberDefault = diceNumberDefault;
    }

    public Integer getSuccessThresholdDefault() {
        return successThresholdDefault;
    }

    public void setSuccessThresholdDefault(int successThresholdDefault) {
        this.successThresholdDefault = successThresholdDefault;
    }

    public boolean getMaxRollDefault() {
        return maxRollDefault;
    }

    public void setMaxRollDefault(boolean maxRollDefault) {
        this.maxRollDefault = maxRollDefault;
    }

    public boolean getBotchDefault() {
        return botchDefault;
    }

    public void setbotchDefault(boolean botchDefault) {
        this.botchDefault = botchDefault;
    }
}