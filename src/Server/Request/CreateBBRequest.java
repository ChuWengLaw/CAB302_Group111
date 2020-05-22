package Server.Request;

import java.io.Serializable;

public class CreateBBRequest implements Serializable {
    private String BillboardName, author, TextColour, BackgroundColour, Message, Image, Information;

    public CreateBBRequest(String BillboardName, String TextColour, String BackgroundColour,
                           String Message, String Image, String Information) {
        this.BillboardName = BillboardName;
        this.author = "a";
        this.TextColour = TextColour;
        this.BackgroundColour = BackgroundColour;
        this.Message = Message;
        this.Image = Image;
        this.Information= Information;
    }

    public String getBillboardName() {
        return BillboardName;
    }
    public String getAuthor() {
        return author;
    }
    public String getTextColour() { return TextColour; }
    public String getBackgroundColour() {
        return BackgroundColour;
    }
    public String getMessage() {
        return Message;
    }
    public String getImage() {
        return Image;
    }
    public String getInformation() {
        return Information;
    }
}
