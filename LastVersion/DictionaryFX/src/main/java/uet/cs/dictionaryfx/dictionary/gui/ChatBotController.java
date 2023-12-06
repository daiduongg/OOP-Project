package uet.cs.dictionaryfx.dictionary.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import uet.cs.dictionaryfx.dictionary.model.ChatBot;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBotController implements Initializable {
    public static final Image UET_ICON = new Image(ChatBotController.class.getResourceAsStream("Assets/uet.png"));
    public static final Image GPT_ICON = new Image(ChatBotController.class.getResourceAsStream("Assets/gpt.png"));
    public static final Image SEND_MESSAGE_ICON = new Image(ChatBotController.class.getResourceAsStream("Assets/send.png"));
    public static final Image STOP_SEND_MESSAGE_ICON = new Image(ChatBotController.class.getResourceAsStream("Assets/stop.png"));

    @FXML
    private TextField messageInput;
    @FXML
    private VBox chatBox;
    @FXML
    private VBox introductionBox;
    @FXML
    private Button reloadButton;
    @FXML
    private Button clearBoxButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView iconSendButton;
    private boolean isSending;
    private String lastMessageInput;
    private boolean[] isNeedResponse = new boolean[1000];
    int count = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isSending = false;
        messageInput.setStyle("-fx-text-fill: #C8C8D1; -fx-background-color: #343541; -fx-border-radius: 50; " +
                "-fx-background-radius: 50; -fx-border-color:  white");
    }

    public void sendMessage(ActionEvent event) {
            String message = messageInput.getText();
            getResponseAndShow(message);
    }

    private void getResponseAndShow(String message) {
        if (!message.trim().isEmpty()) {
            if (!isSending) {
                messageInput.setDisable(true);
                introductionBox.setVisible(false);
                iconSendButton.setImage(STOP_SEND_MESSAGE_ICON);
                isSending = true;
                isNeedResponse[count] = true;
                int indexQuestion = count;
                lastMessageInput = message;

                Task<String> chatBotTask = new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        return ChatBot.askBot(message);
                    }
                };

                HBox messageContainer = createMessageContainer(UET_ICON, "You", message);
                chatBox.getChildren().add(messageContainer);
                messageInput.clear();

                chatBox.heightProperty().addListener((obs, oldVal, newVal) -> {
                    scrollToBottom();
                });

                chatBotTask.setOnSucceeded(workerStateEvent -> {
                    String response = chatBotTask.getValue();
                    System.out.println(response);
                    if (isNeedResponse[indexQuestion]) {
                        HBox responseContainer = createMessageContainer(GPT_ICON, "ChatGPT", proccessedString(response));
                        chatBox.getChildren().add(responseContainer);
                        scrollToBottom();
                        disconnect();
                    }
                });
                new Thread(chatBotTask).start();
            } else {
                disconnect();
            }
        }
    }

    private void disconnect() {
        isSending = false;
        iconSendButton.setImage(SEND_MESSAGE_ICON);
        messageInput.setDisable(false);
        isNeedResponse[count] = false;
        count++;
    }

    private String proccessedString(String s) {
        return s.replace("\\n", "\n");
    }

    private void scrollToBottom() {
        Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
            scrollPane.layout(); // Forces the layout to ensure correct scrolling
        });
    }

    private HBox createMessageContainer(Image iconImage, String name, String message) {
        ImageView icon = new ImageView(iconImage);
        icon.setFitWidth(35);
        icon.setFitHeight(35);

        TextFlow textFlow = new TextFlow();
        Text nameText = new Text(name + "\n");
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        nameText.setStyle("-fx-fill: #C8C8D1");

        Text messageText = new Text(message);
        messageText.setFont(Font.font("Arial", 19));
        messageText.setWrappingWidth(chatBox.getWidth()-10);
        messageText.setStyle("-fx-fill: #C8C8D1");

        textFlow.getChildren().addAll(nameText, messageText);

        HBox messageContainer = new HBox(10);
        messageContainer.getChildren().addAll(icon, textFlow);

        return messageContainer;
    }

    public void resendMessage(ActionEvent event) {
        if (chatBox.getChildren().size() > 0) {
            disconnect();
            chatBox.getChildren().remove(chatBox.getChildren().size() - 1);
            chatBox.getChildren().remove(chatBox.getChildren().size() - 1);
            getResponseAndShow(lastMessageInput);
        }
    }

    public void clearMessageInput(ActionEvent event) {
        if (chatBox.getChildren().size() > 0) {
            disconnect();
            chatBox.getChildren().clear();
            introductionBox.setVisible(true);
        }
    }
}
