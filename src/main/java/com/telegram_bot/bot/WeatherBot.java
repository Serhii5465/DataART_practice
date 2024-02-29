package com.telegram_bot.bot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.telegram_bot.commands.Bot_Commands;
import com.telegram_bot.service.Weather;

import net.aksingh.owmjapis.OpenWeatherMap;

public class WeatherBot extends TelegramLongPollingBot  {

    private final String botToken = "";
    private final String botUserName = "";

    private Weather weather = null;
    private OpenWeatherMap openWeatherMap = null;
    private HashMap < Object, String > botCommandMap = null;
    private boolean isHandleCity = false;
    
    public WeatherBot(){
        weather = new Weather();
        openWeatherMap = weather.getOpenWeatherMap();

        botCommandMap = new HashMap < > ();

        botCommandMap.put(Bot_Commands.START, "/start");
        botCommandMap.put(Bot_Commands.HELP, "/help");
        botCommandMap.put(Bot_Commands.REQUETS_CITY, "/city");
        botCommandMap.put(Bot_Commands.WEATHER, "/weather");
    }


    private String getCityName(Update update){
        return update.getMessage().getText();
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    private String aboutBot(Update update) {
        return new StringBuilder().append(update.getMessage().getFrom().getFirstName()).append(",I can help you get to know weather in your region." +
            " You can control me by these commands:\n" +
            "/city - Change city\n" +
            "/weather - View current weather in your location\n" +
            "/help - View help\n\n" +
            "To start use command /city to set your current location").toString();
    }

    private Object getCommand(HashMap < Object, String > hashMap, String command) {
        Object const_command = null;

        for (Map.Entry < Object, String > entry: hashMap.entrySet()) {
            if (command.equals(entry.getValue())) {
                const_command = entry.getKey();
                break;
            }
        }

        return const_command;
    }

    private void shapingAndSendMessage(Update update, String text) {
        // SendMessage message1 = new SendMessage().setChatId(update.getMessage().getChatId()).setText(text);
        
        SendMessage message = new SendMessage();

        message.setChatId(update.getMessage().getChatId());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException ex) {
            Logger.getLogger(WeatherBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        boolean hasMessage = update.hasMessage();
        boolean isText = update.getMessage().hasText();

        if (hasMessage && isText && !isHandleCity) {
            String command = update.getMessage().getText();

            Bot_Commands comm = (Bot_Commands) getCommand(botCommandMap, command);

            if (comm == null) {
                shapingAndSendMessage(update, aboutBot(update));

            } else {
                switch (comm) {
                    case START:
                    case HELP:
                        shapingAndSendMessage(update, aboutBot(update));
                        break;

                    case REQUETS_CITY:
                        shapingAndSendMessage(update, "Please,send name of your city");
                        isHandleCity = true;
                        break;

                    case WEATHER:
                        if (weather.getCurrentWeather() == null) {
                            shapingAndSendMessage(update, "Send name of your city");
                            isHandleCity = true;
                        } else {
                            shapingAndSendMessage(update, weather.getWeather());
                        }
                        break;
                }
            }

        } else if (isHandleCity) {
            try {
                weather.setCurrentWeather(openWeatherMap.currentWeatherByCityName(getCityName(update)));
            } catch (IOException | JSONException ex) {
                Logger.getLogger(WeatherBot.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (weather.getCurrentWeather().getCityName() == null) {
                shapingAndSendMessage(update, "Invalid location.Re-Enter data");
            } else {
                isHandleCity = false;
                shapingAndSendMessage(update, weather.getWeather());
            }
        }
    }
}
