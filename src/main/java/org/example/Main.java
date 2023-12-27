package org.example;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Я жив");
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyBot("6738300225:AAGlHE_R9PytQbDynGVYXlr-nLJUjVIQRTg"));
            //(new Scanner(System.in)).nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class MyBot extends TelegramLongPollingBot {
    private String botToken;
    @Deprecated
    public MyBot(String botToken) {
        this.botToken = botToken;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            String currentChatId = String.valueOf(update.getMessage().getChatId());
            long myChatIdWithBot = 1022013109;

            if(message.getChatId().equals(myChatIdWithBot)){
                sendMessage.setText(message.getText());
                sendMessage.setChatId("-4079626459");
                customExecuteAsync(sendMessage);
            }

            if(message.hasContact() && message.getChat().isGroupChat()){
                DataBaseHandler dataBaseHendler = new DataBaseHandler();
                boolean a = dataBaseHendler.addUser(String.valueOf(message.getContact().getUserId()), message.getContact().getFirstName(), message.getContact().getPhoneNumber());
                if(a){
                    sendMessage.setText("User added");
                } else {
                    sendMessage.setText("There is already such a user");
                }
                sendMessage.setChatId(currentChatId);
                customExecuteAsync(sendMessage);
            }
            if(message.hasText()){
                System.out.println(message.getText());
            }
            if (message.hasText() && message.getText().equals("/userInfo")){

                DataBaseHandler db = new DataBaseHandler();

                ResultSet resultSet = db.getUserInfo(String.valueOf(message.getReplyToMessage().getFrom().getId()));

                try {
                    resultSet.next();
                    sendMessage.setText("Name: " + resultSet.getNString(Const.USERS_NIKNAME) + "\n" +
                            "Phone: " + resultSet.getNString(Const.USERS_NUMBERPHONE));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sendMessage.setChatId(currentChatId);
                customExecuteAsync(sendMessage);
            }

            if (message.hasText() && message.getText().equals("/getChatId") && String.valueOf(message.getFrom().getId()).equals("986035532")){
                sendMessage.setChatId(currentChatId);
                sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
                sendMessage.setText(currentChatId);
                customExecuteAsync(sendMessage);
            }
            if (message.hasText() && message.getText().equalsIgnoreCase("what holiday")){
                try {
                    sendMessage.setText(Links.hollyToDay());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                customExecuteAsync(sendMessage);
            }

            if (message.hasText() && message.getText().matches("(.*)(R|r)andom number from(.*)")){
                sendMessage.setChatId(currentChatId);
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(message.getText());
                int firstNumber = 0;
                int secondNumber = 0;
                if (matcher.find()) {
                    firstNumber = Integer.parseInt(matcher.group());
                }
                if (matcher.find()) {
                    secondNumber = Integer.parseInt(matcher.group());
                }
                if (firstNumber > secondNumber){
                    sendMessage.setText("Error, the first number is greater than the second");
                }
                else {
                    sendMessage.setText(Integer.toString((int)(Math.random()*(secondNumber - firstNumber) + firstNumber)));
                }
                customExecuteAsync(sendMessage);
            }

            if (message.hasText() && message.getText().equalsIgnoreCase("days until new year")){
                sendMessage.setChatId(currentChatId);
                sendMessage.setText(newYear());
                customExecuteAsync(sendMessage);
            }

        }
    }
    private String newYear(){

        LocalDate currentDate = LocalDate.now();
        LocalDate newYearDate = LocalDate.of(currentDate.getYear() + 1, 1, 1);
        long daysUntilNewYear = ChronoUnit.DAYS.between(currentDate, newYearDate)-1;
        long hoursUntilNewYear = Math.abs(LocalTime.now().getHour() - 24 - 1);
        long minuteUntilNewYear = Math.abs(LocalTime.now().getMinute() - 60);
        return daysUntilNewYear + " days  " + hoursUntilNewYear + " hours " + minuteUntilNewYear + " minutes";
    }
    @Override
    public String getBotUsername() {
        return "MyBot";
    }
    @Override
    @Deprecated
    public String getBotToken() {
        return botToken;
    }
    public void customExecuteAsync(SendMessage sendMessage){
        try {
            executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

