import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigInteger;

public class ElhamBot extends TelegramLongPollingBot {
    private final String username = "elham_mohamadzadeh_bot";
    private final String token = "5522072895:AAEHmhS-2sQP-6Q0DzF4G3hM6dYxfYEtuwY";
    BigInteger number1, number2;
    int state = 0;
    String sign = null;
    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        if (state == 0) {
            if (!(message.equals("/sum") || message.equals("/subtraction") || message.equals("/division") || message.equals("/multiply")))
                sendMessageToUser(update.getMessage().getChatId(), "The input is invalid. Please select a science from the menu.");
            else {
                sign = message;
                state = 1;
                sendMessageToUser(update.getMessage().getChatId(), "Enter number 1 :");
            }
        } else if (state == 1) {
            try {
                number1 = new BigInteger(message);
                state = 2;
                sendMessageToUser(update.getMessage().getChatId(), "Enter number 2 :");
            } catch (NumberFormatException e) {
                sendMessageToUser(update.getMessage().getChatId(), "The entered number is invalid\nEnter number 1 :");
            }
        } else if (state == 2) {
            try {
                number2 = new BigInteger(message);
                state = 3;
                sendResult(number1, number2, update.getMessage().getChatId());
            } catch (NumberFormatException e) {
                sendMessageToUser(update.getMessage().getChatId(), "The entered number is invalid\nEnter number 2 :");
            }
        }
    }

    public void sendResult(BigInteger number1, BigInteger number2, Long id) {
        state = 0;
        BigInteger result;
        switch (sign) {
            case "/sum" -> result = number1.add(number2);
            case "/subtraction" -> result = number1.subtract(number2);
            case "/division" -> result = number1.divide(number2);
            case "/multiply" -> result = number1.multiply(number2);
            default -> {
                sendMessageToUser(id, "The operation is invalid.");
                return;
            }
        }
        sendMessageToUser(id, "Answer : " + result);
    }

    private void sendMessageToUser(Long id, String textMessage) {
        SendMessage message = new SendMessage();
        message.setText(textMessage);
        message.setChatId(id);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
