package com.telegram_bot.commands;

public enum Bot_Commands {

    START,
    HELP,
    REQUETS_CITY,
    WEATHER;

    public static Bot_Commands getSTART() {
        return START;
    }

    public static Bot_Commands getHELP() {
        return HELP;
    }

    public static Bot_Commands getREQUETS_CITY() {
        return REQUETS_CITY;
    }

    public static Bot_Commands getWEATHER() {
        return WEATHER;
    }
}